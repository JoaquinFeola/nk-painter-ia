package com.nkpainter.engine

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

/** The shape one dab leaves: how much of each pixel around its centre it covers, 0–255. */
enum class BrushTip {
    /** A disc with a half-pixel soft edge. */
    HARD_ROUND,

    /** A disc that fades from the centre to nothing at its edge. */
    SOFT_ROUND,

    /** A hard disc broken by a grain fixed to the canvas, so overlapping dabs keep the same texture. */
    GRAINY_ROUND,

    /** Whole pixels on the canvas grid, with no soft edge. */
    SQUARE;

    private companion object {
        const val MAX_COVERAGE = 255
        const val EDGE_SOFTNESS_PX = 0.5f
        const val LIGHTEST_GRAIN = 140
        const val GRAIN_STEPS = MAX_COVERAGE - LIGHTEST_GRAIN + 1
    }

    /** The pixels a dab of this diameter centred on (x, y) can touch: left, top, right, bottom, inclusive. */
    fun reach(x: Float, y: Float, diameterPx: Float): IntArray {
        if (this == SQUARE) {
            val side = max(1, diameterPx.roundToInt())
            val left = floor(x - side / 2f + 0.5f).toInt()
            val top = floor(y - side / 2f + 0.5f).toInt()
            return intArrayOf(left, top, left + side - 1, top + side - 1)
        }
        val reach = diameterPx / 2f + EDGE_SOFTNESS_PX
        return intArrayOf(floor(x - reach).toInt(), floor(y - reach).toInt(), floor(x + reach).toInt(), floor(y + reach).toInt())
    }

    /** How much of pixel (px, py) a dab centred on (x, y) covers. */
    fun coverage(px: Int, py: Int, x: Float, y: Float, diameterPx: Float): Int {
        if (this == SQUARE) return MAX_COVERAGE
        val dx = px + 0.5f - x
        val dy = py + 0.5f - y
        val distance = sqrt(dx * dx + dy * dy)
        val radius = diameterPx / 2f
        return when (this) {
            HARD_ROUND -> hardEdge(radius, distance)
            GRAINY_ROUND -> hardEdge(radius, distance) * grainAt(px, py) / MAX_COVERAGE
            else -> softFalloff(radius, distance)
        }
    }

    private fun hardEdge(radius: Float, distance: Float): Int =
        ((radius + EDGE_SOFTNESS_PX - distance).coerceIn(0f, 1f) * MAX_COVERAGE + 0.5f).toInt()

    private fun softFalloff(radius: Float, distance: Float): Int {
        if (distance >= radius) return 0
        val toEdge = 1f - distance / radius
        return (toEdge * toEdge * (3f - 2f * toEdge) * MAX_COVERAGE + 0.5f).toInt()
    }

    // Integer hashing wraps the same way on every processor, so the grain is the same on ARM and x86.
    private fun grainAt(px: Int, py: Int): Int {
        var hash = px * 73856093 xor py * 19349663
        hash = (hash xor (hash ushr 13)) * 1274126177
        hash = hash xor (hash ushr 16)
        return LIGHTEST_GRAIN + (hash and Int.MAX_VALUE) % GRAIN_STEPS
    }
}
