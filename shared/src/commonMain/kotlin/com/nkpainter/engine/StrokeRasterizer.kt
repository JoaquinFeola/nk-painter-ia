package com.nkpainter.engine

import kotlin.math.max
import kotlin.math.sqrt

/** Turns the sampled points of a stroke into evenly spaced dabs of a brush. */
class StrokeRasterizer(private val brush: RoundBrush) {
    companion object {
        private const val SPACING_RATIO = 0.15f
        private const val MIN_SPACING_PX = 1f
    }

    fun paint(grid: TileGrid, points: List<StrokePoint>): Set<TileCoordinate> {
        val first = points.firstOrNull() ?: return emptySet()
        val changed = HashSet<TileCoordinate>()
        changed += brush.stamp(grid, first.x, first.y, first.pressure)

        var distanceToNextDab = spacingAt(first.pressure)
        for (index in 1 until points.size) {
            val from = points[index - 1]
            val to = points[index]
            val dx = to.x - from.x
            val dy = to.y - from.y
            val segmentLength = sqrt(dx * dx + dy * dy)
            var travelled = distanceToNextDab
            while (travelled <= segmentLength) {
                val t = travelled / segmentLength
                val pressure = from.pressure + (to.pressure - from.pressure) * t
                changed += brush.stamp(grid, from.x + dx * t, from.y + dy * t, pressure)
                travelled += spacingAt(pressure)
            }
            distanceToNextDab = travelled - segmentLength
        }
        return changed
    }

    private fun spacingAt(pressure: Float): Float = max(MIN_SPACING_PX, brush.diameterAt(pressure) * SPACING_RATIO)
}
