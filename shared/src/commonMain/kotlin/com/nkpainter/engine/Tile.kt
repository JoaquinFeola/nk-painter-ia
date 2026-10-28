package com.nkpainter.engine

/** A square of RGBA pixels, 8 bits per channel, premultiplied by alpha. */
class Tile {
    companion object {
        const val SIZE = 256
        const val BYTES_PER_PIXEL = 4
        const val BYTE_COUNT = SIZE * SIZE * BYTES_PER_PIXEL
    }

    val pixels = TilePixels(BYTE_COUNT)

    fun offsetOf(x: Int, y: Int): Int = (y * SIZE + x) * BYTES_PER_PIXEL
}
