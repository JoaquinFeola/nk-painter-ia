package com.nkpainter.desktop.pens

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

/** The pointer functions of user32 that jna-platform does not map. */
@Suppress("FunctionName")
interface PointerApi : StdCallLibrary {
    companion object {
        val instance: PointerApi = Native.load("user32", PointerApi::class.java, W32APIOptions.DEFAULT_OPTIONS)
    }

    fun GetPointerType(pointerId: Int, pointerType: IntByReference): Boolean

    fun GetPointerInfo(pointerId: Int, pointerInfo: PointerInfoStructure): Boolean

    fun GetPointerPenInfo(pointerId: Int, penInfo: PointerPenInfoStructure): Boolean

    fun GetPointerPenInfoHistory(pointerId: Int, entriesCount: IntByReference, penInfo: Array<PointerPenInfoStructure>): Boolean

    fun CreateSyntheticPointerDevice(pointerType: Int, maxCount: Int, mode: Int): Pointer?

    fun InjectSyntheticPointerInput(device: Pointer, pointerInfo: Array<PointerTypeInfoStructure>, count: Int): Boolean

    fun DestroySyntheticPointerDevice(device: Pointer)

    fun WindowFromPoint(point: WinDef.POINT.ByValue): WinDef.HWND?

    fun ScreenToClient(window: WinDef.HWND, point: WinDef.POINT): Boolean
}
