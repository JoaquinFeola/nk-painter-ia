package com.nkpainter.engine

import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * How big and how strong one dab is: its diameter and its opacity, 0–255. A building-up stroke
 * stops adding where it already reached `ceiling`, the opacity this dab's pressure allows.
 */
data class DabSize(val diameterPx: Float, val opacity: Int, val ceiling: Int = opacity)

/** What a finished stroke changed: every tile it touched, as it was before; null where it created the tile. */
class StrokeChanges(val tilesBefore: Map<TileCoordinate, ByteArray?>)

/**
 * One stroke on one layer, painted as its points arrive. Dabs raise a coverage mask, and each
 * touched pixel is the pixel from before the stroke plus the color through that mask, so a stroke
 * never darkens where it crosses itself. A building-up stroke adds its dabs instead, up to each
 * dab's ceiling. An erasing stroke takes away instead: each touched pixel keeps 1 − coverage of
 * what it had before the stroke, in all four channels. Pixels outside the canvas are never written.
 */
class BrushStroke(
    private val grid: TileGrid,
    private val tip: BrushTip,
    private val color: PremultipliedColor,
    private val buildsUp: Boolean,
    private val erases: Boolean = false,
    private val dabSizeAt: (pressure: Float) -> DabSize,
) {
    private companion object {
        const val MAX_CHANNEL = 255
        const val SPACING_RATIO = 0.1f
        const val MIN_SPACING_PX = 0.5f
        const val PIXELS_PER_TILE = Tile.SIZE * Tile.SIZE
    }

    private val masks = HashMap<TileCoordinate, ByteArray>()
    private val tilesBefore = HashMap<TileCoordinate, ByteArray?>()
    private var previous: StrokePoint? = null
    private var distanceToNextDab = 0f

    /** Paints up to this point and returns the tiles it changed. The first point leaves one dab. */
    fun addPoint(point: StrokePoint): Set<TileCoordinate> {
        val changed = HashSet<TileCoordinate>()
        val from = previous
        previous = point
        if (from == null) {
            stamp(point.x, point.y, point.pressure, changed)
            distanceToNextDab = spacingAt(point.pressure)
            return changed
        }
        val dx = point.x - from.x
        val dy = point.y - from.y
        val segmentLength = sqrt(dx * dx + dy * dy)
        var travelled = distanceToNextDab
        while (travelled <= segmentLength) {
            val t = travelled / segmentLength
            val pressure = from.pressure + (point.pressure - from.pressure) * t
            stamp(from.x + dx * t, from.y + dy * t, pressure, changed)
            travelled += spacingAt(pressure)
        }
        distanceToNextDab = travelled - segmentLength
        return changed
    }

    /** Ends the stroke, drops its mask and hands back what the tiles were before it. */
    fun finish(): StrokeChanges {
        masks.clear()
        return StrokeChanges(HashMap(tilesBefore))
    }

    private fun spacingAt(pressure: Float): Float = max(MIN_SPACING_PX, dabSizeAt(pressure).diameterPx * SPACING_RATIO)

    private fun stamp(x: Float, y: Float, pressure: Float, changed: MutableSet<TileCoordinate>) {
        val dab = dabSizeAt(pressure)
        val (rawLeft, rawTop, rawRight, rawBottom) = tip.reach(x, y, dab.diameterPx)
        val left = max(0, rawLeft)
        val top = max(0, rawTop)
        val right = min(grid.widthPx - 1, rawRight)
        val bottom = min(grid.heightPx - 1, rawBottom)
        for (py in top..bottom) {
            for (px in left..right) {
                val dabCoverage = divideBy255(tip.coverage(px, py, x, y, dab.diameterPx) * dab.opacity)
                if (dabCoverage == 0) continue
                val coordinate = TileCoordinate(px / Tile.SIZE, py / Tile.SIZE)
                val mask = maskFor(coordinate)
                val pixelIndex = (py % Tile.SIZE) * Tile.SIZE + px % Tile.SIZE
                val old = mask[pixelIndex].toInt() and 0xFF
                val new = if (buildsUp) max(old, min(dab.ceiling, old + dabCoverage)) else max(old, dabCoverage)
                if (new == old) continue
                mask[pixelIndex] = new.toByte()
                repaint(coordinate, pixelIndex, new)
                changed += coordinate
            }
        }
    }

    private fun maskFor(coordinate: TileCoordinate): ByteArray = masks.getOrPut(coordinate) {
        tilesBefore[coordinate] = grid.tileAt(coordinate)?.pixels?.toByteArray()
        ByteArray(PIXELS_PER_TILE)
    }

    private fun repaint(coordinate: TileCoordinate, pixelIndex: Int, coverage: Int) {
        val pixels = grid.tileForPainting(coordinate).pixels
        val before = tilesBefore[coordinate]
        val offset = pixelIndex * Tile.BYTES_PER_PIXEL
        if (erases) {
            val kept = MAX_CHANNEL - coverage
            for (channel in 0 until Tile.BYTES_PER_PIXEL) pixels[offset + channel] = blend(0, 0, before, offset + channel, kept)
            return
        }
        val keep = MAX_CHANNEL - divideBy255(color.alpha * coverage)
        pixels[offset] = blend(color.red, coverage, before, offset, keep)
        pixels[offset + 1] = blend(color.green, coverage, before, offset + 1, keep)
        pixels[offset + 2] = blend(color.blue, coverage, before, offset + 2, keep)
        pixels[offset + 3] = blend(color.alpha, coverage, before, offset + 3, keep)
    }

    private fun blend(source: Int, coverage: Int, before: ByteArray?, index: Int, keep: Int): Byte {
        val destination = before?.let { it[index].toInt() and 0xFF } ?: 0
        return (divideBy255(source * coverage) + divideBy255(destination * keep)).toByte()
    }

    // Exact rounded division by 255 for 0..65025, without floating point.
    private fun divideBy255(value: Int): Int = (value + 128 + ((value + 128) shr 8)) shr 8
}
