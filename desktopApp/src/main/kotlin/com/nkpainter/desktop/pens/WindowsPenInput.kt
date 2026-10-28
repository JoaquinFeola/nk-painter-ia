package com.nkpainter.desktop.pens

import com.nkpainter.engine.StrokePoint
import com.sun.jna.CallbackReference
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.ptr.IntByReference

/**
 * Reads pen and touch from the Windows Pointer API, which AWT does not expose, by subclassing
 * the window and every child window: Compose draws into a child window, and pointer messages
 * go to the deepest window under the pen (compose-multiplatform issue 4934).
 */
class WindowsPenInput(private val onSample: (PenSample) -> Unit) {
    companion object {
        private const val WM_POINTERUPDATE = 0x0245
        private const val WM_POINTERDOWN = 0x0246
        private const val WM_POINTERUP = 0x0247
        private const val PT_TOUCH = 2
        private const val PT_PEN = 3
        private const val PEN_FLAG_INVERTED = 0x2
        private const val PEN_FLAG_ERASER = 0x4
        private const val PEN_MASK_PRESSURE = 0x1
        private const val POINTER_FLAG_INCONTACT = 0x4
        private const val MAX_PEN_PRESSURE = 1024f
        private const val FULL_PRESSURE = 1f
        private const val POINTER_ID_MASK = 0xFFFF
        private const val HANDLED: Long = 0
    }

    private val user32 = User32.INSTANCE
    private val pointerApi = PointerApi.instance
    private val originalProcedures = HashMap<Pointer, Pointer>()
    // Held for as long as this object lives: JNA frees a callback once nothing references it.
    private val procedure = WinUser.WindowProc { window, message, wParam, lParam -> handle(window, message, wParam, lParam) }

    val subclassedWindowCount: Int get() = originalProcedures.size

    /** Pointer messages seen, read or not; tells a missing hook apart from an unreadable pointer. */
    @Volatile var pointerMessageCount = 0
        private set

    fun isSubclassed(window: WinDef.HWND): Boolean = originalProcedures.containsKey(window.pointer)

    /** Subclasses the top-level window and all of its current child windows. */
    fun attach(topLevelWindow: WinDef.HWND) {
        subclass(topLevelWindow)
        user32.EnumChildWindows(topLevelWindow, { child, _ -> subclass(child); true }, null)
    }

    private fun subclass(window: WinDef.HWND) {
        val key = window.pointer
        if (originalProcedures.containsKey(key)) return
        val previous = user32.SetWindowLongPtr(window, WinUser.GWL_WNDPROC, CallbackReference.getFunctionPointer(procedure))
        originalProcedures[key] = previous
    }

    private fun handle(window: WinDef.HWND, message: Int, wParam: WinDef.WPARAM, lParam: WinDef.LPARAM): WinDef.LRESULT {
        val phase = phaseOf(message)
        if (phase != null) pointerMessageCount++
        val samples = phase?.let { readSamples(window, (wParam.toLong() and POINTER_ID_MASK.toLong()).toInt(), it) }.orEmpty()
        if (samples.isNotEmpty()) {
            samples.forEach(onSample)
            return WinDef.LRESULT(HANDLED)
        }
        val original = originalProcedures[window.pointer] ?: return user32.DefWindowProc(window, message, wParam, lParam)
        return user32.CallWindowProc(original, window, message, wParam, lParam)
    }

    private fun phaseOf(message: Int): PenPhase? = when (message) {
        WM_POINTERDOWN -> PenPhase.Down
        WM_POINTERUPDATE -> PenPhase.Move
        WM_POINTERUP -> PenPhase.Up
        else -> null
    }

    private fun readSamples(window: WinDef.HWND, pointerId: Int, phase: PenPhase): List<PenSample> {
        val receivedAt = System.nanoTime()
        val type = IntByReference()
        if (!pointerApi.GetPointerType(pointerId, type)) return emptyList()
        return when (type.value) {
            PT_PEN -> readPen(window, pointerId, phase, receivedAt)
            PT_TOUCH -> listOfNotNull(readTouch(window, pointerId, phase, receivedAt))
            else -> emptyList()
        }
    }

    private fun readPen(window: WinDef.HWND, pointerId: Int, phase: PenPhase, receivedAt: Long): List<PenSample> {
        val info = PointerPenInfoStructure()
        if (!pointerApi.GetPointerPenInfo(pointerId, info)) return emptyList()
        // A pen hovering above the screen still reports moves; only contact paints.
        if (phase == PenPhase.Move && info.pointerInfo.pointerFlags and POINTER_FLAG_INCONTACT == 0) return emptyList()
        val readings = if (phase == PenPhase.Move) coalescedReadings(pointerId, info) else listOf(info)
        return readings.map { reading ->
            val pressure = if (reading.penMask and PEN_MASK_PRESSURE != 0) reading.pressure / MAX_PEN_PRESSURE else FULL_PRESSURE
            val erasing = reading.penFlags and (PEN_FLAG_ERASER or PEN_FLAG_INVERTED) != 0
            val tool = if (erasing) PenTool.Eraser else PenTool.Pen
            PenSample(toClientPoint(window, reading.pointerInfo.ptPixelLocation, pressure), tool, phase, pointerId, receivedAt)
        }
    }

    /**
     * When the app falls behind, Windows folds several pen moves into one message; without them a
     * fast curve turns into straight segments. Windows lists them newest first.
     */
    @Suppress("UNCHECKED_CAST")
    private fun coalescedReadings(pointerId: Int, latest: PointerPenInfoStructure): List<PointerPenInfoStructure> {
        val count = latest.pointerInfo.historyCount
        if (count <= 1) return listOf(latest)
        // JNA passes a structure array only if it was allocated contiguously by toArray.
        val entries = PointerPenInfoStructure().toArray(count) as Array<PointerPenInfoStructure>
        val available = IntByReference(count)
        if (!pointerApi.GetPointerPenInfoHistory(pointerId, available, entries)) return listOf(latest)
        return entries.take(minOf(count, available.value)).reversed()
    }

    private fun readTouch(window: WinDef.HWND, pointerId: Int, phase: PenPhase, receivedAt: Long): PenSample? {
        val info = PointerInfoStructure()
        if (!pointerApi.GetPointerInfo(pointerId, info)) return null
        return PenSample(toClientPoint(window, info.ptPixelLocation, FULL_PRESSURE), PenTool.Touch, phase, pointerId, receivedAt)
    }

    private fun toClientPoint(window: WinDef.HWND, screen: WinDef.POINT, pressure: Float): StrokePoint {
        val client = WinDef.POINT(screen.x, screen.y)
        pointerApi.ScreenToClient(window, client)
        return StrokePoint(client.x.toFloat(), client.y.toFloat(), pressure.coerceIn(0f, FULL_PRESSURE))
    }
}
