package com.nkpainter.desktop.pens

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef

/**
 * Proves the pen path without a physical pen: injects a stroke with rising pressure through
 * the Windows synthetic pointer API and reports what [WindowsPenInput] received.
 */
class SyntheticPenProbe(private val pointerApi: PointerApi = PointerApi.instance) {
    companion object {
        private const val PT_PEN = 3
        private const val POINTER_FEEDBACK_DEFAULT = 1
        private const val POINTER_FLAG_INRANGE = 0x2
        private const val POINTER_FLAG_INCONTACT = 0x4
        private const val POINTER_FLAG_DOWN = 0x10000
        private const val POINTER_FLAG_UPDATE = 0x20000
        private const val POINTER_FLAG_UP = 0x40000
        private const val PEN_MASK_PRESSURE = 0x1
        private const val FIRST_PRESSURE = 200
        private const val LAST_PRESSURE = 1000
        private const val STEP_COUNT = 40
        private const val STEP_PX = 10
        private const val STEP_DELAY_MS = 8L
        const val EXPECTED_STRUCT_SIZE_PEN = 120
        const val EXPECTED_STRUCT_SIZE_TYPE = 152
    }

    fun structSizes(): Pair<Int, Int> = PointerPenInfoStructure().size() to PointerTypeInfoStructure().size()

    /** Injects the stroke starting at the given screen point; returns false if Windows refused it. */
    fun injectStroke(screenStart: WinDef.POINT): Boolean {
        val device = pointerApi.CreateSyntheticPointerDevice(PT_PEN, 1, POINTER_FEEDBACK_DEFAULT) ?: return false
        try {
            val inContact = POINTER_FLAG_INRANGE or POINTER_FLAG_INCONTACT
            // A real pen hovers before it touches; Windows may drop a contact that never hovered.
            if (!inject(device, screenStart.x, screenStart.y, 0, POINTER_FLAG_INRANGE or POINTER_FLAG_UPDATE)) return false
            if (!inject(device, screenStart.x, screenStart.y, FIRST_PRESSURE, inContact or POINTER_FLAG_DOWN)) return false
            for (step in 1..STEP_COUNT) {
                val pressure = FIRST_PRESSURE + (LAST_PRESSURE - FIRST_PRESSURE) * step / STEP_COUNT
                Thread.sleep(STEP_DELAY_MS)
                if (!inject(device, screenStart.x + step * STEP_PX, screenStart.y, pressure, inContact or POINTER_FLAG_UPDATE)) return false
            }
            return inject(device, screenStart.x + STEP_COUNT * STEP_PX, screenStart.y, 0, POINTER_FLAG_INRANGE or POINTER_FLAG_UP)
        } finally {
            pointerApi.DestroySyntheticPointerDevice(device)
        }
    }

    private fun inject(device: Pointer, x: Int, y: Int, pressure: Int, flags: Int): Boolean {
        // JNA passes a structure array only if it was allocated contiguously by toArray.
        @Suppress("UNCHECKED_CAST")
        val entries = PointerTypeInfoStructure().toArray(1) as Array<PointerTypeInfoStructure>
        val pen = entries[0].penInfo
        entries[0].type = PT_PEN
        pen.pointerInfo.pointerType = PT_PEN
        pen.pointerInfo.pointerFlags = flags
        pen.pointerInfo.ptPixelLocation = WinDef.POINT(x, y)
        pen.penMask = PEN_MASK_PRESSURE
        pen.pressure = pressure
        return pointerApi.InjectSyntheticPointerInput(device, entries, 1)
    }
}
