package com.nkpainter.canvases

import kotlin.math.sqrt

/** How much canvas this device handles: a warning above one size, a refusal above another. */
data class DeviceCanvasLimit(val warningPixels: Long, val redLinePixels: Long) {
    companion object {
        // One full layer, the composite with its reduced levels, the images on screen, and margin (QT-62).
        private const val BYTES_PER_CANVAS_PIXEL = 16
        private const val WARNING_SHARE_OF_RED_LINE = 2

        fun fromBudget(budgetBytes: Long): DeviceCanvasLimit {
            val redLine = budgetBytes / BYTES_PER_CANVAS_PIXEL
            return DeviceCanvasLimit(redLine / WARNING_SHARE_OF_RED_LINE, redLine)
        }
    }

    fun isAboveRedLine(size: CanvasSize): Boolean = size.pixelCount > redLinePixels

    fun isAboveWarning(size: CanvasSize): Boolean = size.pixelCount > warningPixels

    /** The largest canvas with the same shape as the one asked for that is not above the red line. */
    fun largestWithSameShape(widthPx: Int, heightPx: Int): AreaSize {
        var width = sqrt(redLinePixels.toDouble() * widthPx / heightPx).toLong()
        while (width * heightPx / widthPx * width > redLinePixels) width--
        return AreaSize(width.toInt(), (width * heightPx / widthPx).toInt())
    }
}
