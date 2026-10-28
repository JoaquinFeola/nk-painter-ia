package com.nkpainter.desktop.pens

import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.platform.win32.WinDef

/** Win32 POINTER_INFO. Field order and types follow the SDK header; JNA applies the MSVC alignment. */
@Structure.FieldOrder(
    "pointerType", "pointerId", "frameId", "pointerFlags", "sourceDevice", "hwndTarget",
    "ptPixelLocation", "ptHimetricLocation", "ptPixelLocationRaw", "ptHimetricLocationRaw",
    "dwTime", "historyCount", "inputData", "dwKeyStates", "performanceCount", "buttonChangeType",
)
open class PointerInfoStructure : Structure() {
    @JvmField var pointerType: Int = 0
    @JvmField var pointerId: Int = 0
    @JvmField var frameId: Int = 0
    @JvmField var pointerFlags: Int = 0
    @JvmField var sourceDevice: Pointer? = null
    @JvmField var hwndTarget: Pointer? = null
    @JvmField var ptPixelLocation: WinDef.POINT = WinDef.POINT()
    @JvmField var ptHimetricLocation: WinDef.POINT = WinDef.POINT()
    @JvmField var ptPixelLocationRaw: WinDef.POINT = WinDef.POINT()
    @JvmField var ptHimetricLocationRaw: WinDef.POINT = WinDef.POINT()
    @JvmField var dwTime: Int = 0
    @JvmField var historyCount: Int = 0
    @JvmField var inputData: Int = 0
    @JvmField var dwKeyStates: Int = 0
    @JvmField var performanceCount: Long = 0
    @JvmField var buttonChangeType: Int = 0

    class ByValue : PointerInfoStructure(), Structure.ByValue
}
