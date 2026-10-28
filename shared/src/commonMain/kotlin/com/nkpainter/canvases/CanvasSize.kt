package com.nkpainter.canvases

/** The width and height of a canvas in pixels, with its resolution. */
data class CanvasSize(val widthPx: Int, val heightPx: Int, val resolution: Resolution) {
    val pixelCount: Long get() = widthPx.toLong() * heightPx
}
