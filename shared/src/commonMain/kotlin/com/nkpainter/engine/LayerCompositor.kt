package com.nkpainter.engine

/**
 * Combines visible layers over an opaque background, one tile at a time, with the same integer
 * arithmetic as the brush. Clipping and alpha lock are not applied yet.
 */
class LayerCompositor(private val background: PremultipliedColor) {
    companion object {
        private const val MAX_CHANNEL = 255
        private const val FULL_OPACITY_PERCENT = 100
        private const val CHANNELS = 4
    }

    /** A layer as the compositor sees it: its pixels and how strongly it shows. */
    class Source(val pixels: TileGrid, val opacityPercent: Int, val visible: Boolean)

    fun compositeTile(sources: List<Source>, coordinate: TileCoordinate, into: ByteArray = ByteArray(Tile.BYTE_COUNT)): ByteArray {
        fillWithBackground(into)
        sources.filter { it.visible }.forEach { source ->
            val tile = source.pixels.tileAt(coordinate) ?: return@forEach
            val opacity = (source.opacityPercent * MAX_CHANNEL + FULL_OPACITY_PERCENT / 2) / FULL_OPACITY_PERCENT
            blendTile(tile.pixels, opacity, into)
        }
        return into
    }

    private fun fillWithBackground(into: ByteArray) {
        for (offset in into.indices step CHANNELS) {
            into[offset] = background.red.toByte()
            into[offset + 1] = background.green.toByte()
            into[offset + 2] = background.blue.toByte()
            into[offset + 3] = background.alpha.toByte()
        }
    }

    private fun blendTile(source: TilePixels, opacity: Int, into: ByteArray) {
        for (offset in 0 until Tile.BYTE_COUNT step CHANNELS) {
            val sourceAlpha = divideBy255((source[offset + 3].toInt() and 0xFF) * opacity)
            if (sourceAlpha == 0) continue
            val keep = MAX_CHANNEL - sourceAlpha
            for (channel in 0 until CHANNELS) {
                val scaledSource = divideBy255((source[offset + channel].toInt() and 0xFF) * opacity)
                val destination = into[offset + channel].toInt() and 0xFF
                into[offset + channel] = (scaledSource + divideBy255(destination * keep)).toByte()
            }
        }
    }

    // Exact rounded division by 255 for 0..65025, without floating point.
    private fun divideBy255(value: Int): Int = (value + 128 + ((value + 128) shr 8)) shr 8
}
