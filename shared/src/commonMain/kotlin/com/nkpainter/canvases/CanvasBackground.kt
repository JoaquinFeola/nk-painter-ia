package com.nkpainter.canvases

/** What shows behind every layer: a color or nothing. It is not a layer. */
sealed interface CanvasBackground {
    data object Transparent : CanvasBackground

    /** An opaque sRGB color, as 0xRRGGBB. */
    data class Solid(val rgb: Int) : CanvasBackground

    companion object {
        val White: CanvasBackground = Solid(0xFFFFFF)
    }
}
