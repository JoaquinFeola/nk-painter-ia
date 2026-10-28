package com.nkpainter.brushes

import com.nkpainter.engine.StrokePoint

/**
 * Smooths a stroke by drawing behind the pen: each sample, the drawn point covers 1 / (1 + level)
 * of the way to the real one (QT-95). At level 0 the drawn points are the real ones. The same
 * samples always give the same points.
 */
class Stabilizer(private val level: Int) {
    private var drawn: StrokePoint? = null
    private val recentReal = ArrayDeque<StrokePoint>()

    /** The point to draw for this real one. The first point of a stroke is drawn where it is. */
    fun follow(real: StrokePoint): StrokePoint {
        val from = drawn
        val next = if (from == null) {
            real
        } else {
            val share = 1f / (1 + level)
            StrokePoint(from.x + (real.x - from.x) * share, from.y + (real.y - from.y) * share, real.pressure)
        }
        drawn = next
        recentReal.addLast(real)
        if (recentReal.size > remembered()) recentReal.removeFirst()
        return next
    }

    /**
     * Where the stroke still has to go when the pen lifts, ending on the last real point; empty if it is
     * already there. A straight jump from the lagging point would cut the last bend (BUG-001 of F-002), so
     * it retraces the real points after the one nearest the drawn point, shifted by the lag, and the shift
     * fades to nothing by the last one.
     */
    fun landing(): List<StrokePoint> {
        val from = drawn ?: return emptyList()
        val lastReal = recentReal.last()
        if (from == lastReal) return emptyList()
        val nearest = recentReal.indices.minBy { squaredDistance(recentReal[it], from) }
        val lastIndex = recentReal.lastIndex
        if (nearest == lastIndex) return listOf(lastReal).also { drawn = lastReal }
        val lagX = from.x - recentReal[nearest].x
        val lagY = from.y - recentReal[nearest].y
        val path = (nearest + 1..lastIndex).map { index ->
            val shiftLeft = 1f - (index - nearest).toFloat() / (lastIndex - nearest)
            val real = recentReal[index]
            StrokePoint(real.x + lagX * shiftLeft, real.y + lagY * shiftLeft, real.pressure)
        }
        drawn = lastReal
        return path
    }

    // The drawn point trails about `level` samples behind; three times that always covers it.
    private fun remembered() = 3 * level + 2

    private fun squaredDistance(a: StrokePoint, b: StrokePoint): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return dx * dx + dy * dy
    }
}
