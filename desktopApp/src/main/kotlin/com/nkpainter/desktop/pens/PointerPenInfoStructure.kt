package com.nkpainter.desktop.pens

import com.sun.jna.Structure

/** Win32 POINTER_PEN_INFO. */
@Structure.FieldOrder("pointerInfo", "penFlags", "penMask", "pressure", "rotation", "tiltX", "tiltY")
open class PointerPenInfoStructure : Structure() {
    @JvmField var pointerInfo: PointerInfoStructure.ByValue = PointerInfoStructure.ByValue()
    @JvmField var penFlags: Int = 0
    @JvmField var penMask: Int = 0
    @JvmField var pressure: Int = 0
    @JvmField var rotation: Int = 0
    @JvmField var tiltX: Int = 0
    @JvmField var tiltY: Int = 0

    class ByValue : PointerPenInfoStructure(), Structure.ByValue
}
