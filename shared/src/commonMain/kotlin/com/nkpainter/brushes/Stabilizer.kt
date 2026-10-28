package com.nkpainter.brushes

import com.nkpainter.engine.StrokePoint

/**
 * Smooths a stroke by drawing behind the pen: each sample, the drawn point covers 1 / (1 + level)
 * of the way to the real one (QT-95). At level 0 the drawn points are the real ones. The same
 * samples always give the same points.
 */
class Stabilizer(private val level: Int) {
    private var drawn: StrokePoint? = null

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
        return next
    }

    /** Where the stroke still has to go when the pen lifts: the last real point, or null if it is already there. */
    fun landing(lastReal: StrokePoint): StrokePoint? = lastReal.takeIf { it != drawn }?.also { drawn = it }
}
