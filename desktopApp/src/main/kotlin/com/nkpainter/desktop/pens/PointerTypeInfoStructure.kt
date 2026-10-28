package com.nkpainter.desktop.pens

import com.sun.jna.Structure

/**
 * Win32 POINTER_TYPE_INFO with its union holding the pen variant. The union is as large as
 * its touch variant, so the padding keeps the size the API expects for each array element.
 */
@Structure.FieldOrder("type", "penInfo", "unionPadding")
class PointerTypeInfoStructure : Structure() {
    companion object {
        // sizeof(POINTER_TOUCH_INFO) - sizeof(POINTER_PEN_INFO) on 64-bit Windows: 144 - 120.
        private const val UNION_PADDING_BYTES = 24
    }

    @JvmField var type: Int = 0
    @JvmField var penInfo: PointerPenInfoStructure.ByValue = PointerPenInfoStructure.ByValue()
    @JvmField var unionPadding: ByteArray = ByteArray(UNION_PADDING_BYTES)
}
