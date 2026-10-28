package com.nkpainter.engine

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * A round brush whose diameter follows the pressure. Pixels are blended with integer
 * arithmetic so the same stroke gives the same bytes on every processor.
 */
class RoundBrush(val maxDiameterPx: Float, val color: PremultipliedColor) {
    companion object {
        private const val MIN_DIAMETER_PX = 1f
        private const val MAX_CHANNEL = 255
        private const val EDGE_SOFTNESS_PX = 0.5f
    }

    fun diameterAt(pressure: Float): Float = max(MIN_DIAMETER_PX, maxDiameterPx * pressure.coerceIn(0f, 1f))

    /** Stamps one dab centred on (x, y) and returns the tiles it changed. */
    fun stamp(grid: TileGrid, x: Float, y: Float, pressure: Float): Set<TileCoordinate> {
        val radius = diameterAt(pressure) / 2f
        val reach = radius + EDGE_SOFTNESS_PX
        val left = max(0, floor(x - reach).toInt())
        val top = max(0, floor(y - reach).toInt())
        val right = min(grid.widthPx - 1, ceil(x + reach).toInt())
        val bottom = min(grid.heightPx - 1, ceil(y + reach).toInt())
        if (left > right || top > bottom) return emptySet()

        val changed = HashSet<TileCoordinate>()
        for (py in top..bottom) {
            for (px in left..right) {
                val coverage = coverageAt(px + 0.5f - x, py + 0.5f - y, radius)
                if (coverage == 0) continue
                val coordinate = TileCoordinate(px / Tile.SIZE, py / Tile.SIZE)
                val tile = grid.tileForPainting(coordinate)
                blendSourceOver(tile, tile.offsetOf(px % Tile.SIZE, py % Tile.SIZE), coverage)
                changed += coordinate
            }
        }
        return changed
    }

    private fun coverageAt(dx: Float, dy: Float, radius: Float): Int {
        val distance = sqrt(dx * dx + dy * dy)
        val coverage = (radius + EDGE_SOFTNESS_PX - distance).coerceIn(0f, 1f)
        return (coverage * MAX_CHANNEL + 0.5f).toInt()
    }

    private fun blendSourceOver(tile: Tile, offset: Int, coverage: Int) {
        val pixels = tile.pixels
        val keep = MAX_CHANNEL - divideBy255(color.alpha * coverage)
        pixels[offset] = blendChannel(color.red, coverage, pixels[offset], keep)
        pixels[offset + 1] = blendChannel(color.green, coverage, pixels[offset + 1], keep)
        pixels[offset + 2] = blendChannel(color.blue, coverage, pixels[offset + 2], keep)
        pixels[offset + 3] = blendChannel(color.alpha, coverage, pixels[offset + 3], keep)
    }

    private fun blendChannel(source: Int, coverage: Int, destination: Byte, keep: Int): Byte {
        val destinationValue = destination.toInt() and 0xFF
        return (divideBy255(source * coverage) + divideBy255(destinationValue * keep)).toByte()
    }

    // Exact rounded division by 255 for 0..65025, without floating point.
    private fun divideBy255(value: Int): Int = (value + 128 + ((value + 128) shr 8)) shr 8
}
