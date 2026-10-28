package com.nkpainter.canvases

import kotlin.math.floor

/** What a custom canvas size is written in. */
enum class LengthUnit {
    PIXELS,
    MILLIMETERS,
    INCHES;

    private companion object {
        const val MILLIMETERS_PER_INCH = 25.4
        const val HALF_PIXEL = 0.5
    }

    /** Rounds to the nearest pixel, a half pixel up. */
    fun toPixels(measure: Double, dpi: Int): Int {
        val pixels = when (this) {
            PIXELS -> measure
            MILLIMETERS -> measure * dpi / MILLIMETERS_PER_INCH
            INCHES -> measure * dpi
        }
        return floor(pixels + HALF_PIXEL).toInt()
    }
}
