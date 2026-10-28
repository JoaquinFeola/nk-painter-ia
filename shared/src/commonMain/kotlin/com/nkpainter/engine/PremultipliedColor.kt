package com.nkpainter.engine

/** A color whose red, green and blue are already multiplied by its alpha, each 0–255. */
data class PremultipliedColor(val red: Int, val green: Int, val blue: Int, val alpha: Int) {
    companion object {
        private const val MAX_CHANNEL = 255

        fun opaque(red: Int, green: Int, blue: Int) = PremultipliedColor(red, green, blue, MAX_CHANNEL)
    }

    init {
        require(alpha in 0..MAX_CHANNEL) { "alpha $alpha outside 0..$MAX_CHANNEL" }
        require(red in 0..alpha && green in 0..alpha && blue in 0..alpha) {
            "premultiplied channels must not exceed alpha $alpha"
        }
    }
}
