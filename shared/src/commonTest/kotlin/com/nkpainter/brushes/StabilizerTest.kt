package com.nkpainter.brushes

import com.nkpainter.engine.StrokePoint
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StabilizerTest {
    private val lineY = 500f
    private val tremblePx = 6f
    private val fullPressure = 1f

    // From (100, 500) to (1100, 500), swinging ±6 px every 10 px.
    private val tremblingLine = (0..100).map { step ->
        val swing = if (step % 2 == 0) tremblePx else -tremblePx
        StrokePoint(100f + step * 10f, lineY + swing, fullPressure)
    }

    private fun widestSwing(level: Int): Float {
        val stabilizer = Stabilizer(level)
        // The first point is drawn where it is at any level, so it says nothing about smoothing.
        return tremblingLine.map(stabilizer::follow).drop(1).maxOf { abs(it.y - lineY) }
    }

    @Test
    fun atLevelZeroTheDrawnPointsAreTheRealOnes() {
        val stabilizer = Stabilizer(0)

        val drawn = tremblingLine.map(stabilizer::follow)

        assertEquals(tremblingLine, drawn)
    }

    @Test
    fun atLevel10ATremblingLineStraysLessThanAtLevel0() {
        val atZero = widestSwing(0)

        val atTen = widestSwing(10)

        assertTrue(atTen < atZero, "level 10 strays $atTen px, level 0 strays $atZero px")
    }

    @Test
    fun whenThePenLiftsTheStrokeEndsAtTheLastRealPoint() {
        val stabilizer = Stabilizer(10)
        tremblingLine.forEach(stabilizer::follow)

        val landing = stabilizer.landing(tremblingLine.last())

        assertEquals(tremblingLine.last(), landing)
    }

    @Test
    fun atLevelZeroThereIsNothingLeftToLandOn() {
        val stabilizer = Stabilizer(0)
        tremblingLine.forEach(stabilizer::follow)

        assertNull(stabilizer.landing(tremblingLine.last()))
    }
}
