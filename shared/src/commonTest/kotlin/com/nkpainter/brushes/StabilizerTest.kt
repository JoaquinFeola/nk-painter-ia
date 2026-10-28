package com.nkpainter.brushes

import com.nkpainter.engine.StrokePoint
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.test.Test
import kotlin.test.assertEquals
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

        val landing = stabilizer.landing()

        assertEquals(tremblingLine.last(), landing.last())
    }

    @Test
    fun atLevelZeroThereIsNothingLeftToLandOn() {
        val stabilizer = Stabilizer(0)
        tremblingLine.forEach(stabilizer::follow)

        assertTrue(stabilizer.landing().isEmpty())
    }

    @Test
    fun aCircleEndsFollowingItsBendInsteadOfJumpingStraightToThePen() {
        val stabilizer = Stabilizer(INK_FACTORY_LEVEL)
        val drawnCircle = circle.map(stabilizer::follow)

        val landing = stabilizer.landing()

        val ending = listOf(drawnCircle.last()) + landing
        val longestStep = ending.zipWithNext(::distance).max()
        assertTrue(longestStep <= circleSampleSpacing * 1.5f, "the ending jumps $longestStep px; the pen moved $circleSampleSpacing px a sample")
    }

    private companion object {
        const val INK_FACTORY_LEVEL = 3
        const val CIRCLE_SAMPLES = 60
        const val CIRCLE_RADIUS = 480f
    }

    // The QA circle of the defect: 60 samples, closing on its first point.
    private val circle = (0..CIRCLE_SAMPLES).map { step ->
        val angle = 2 * PI * step / CIRCLE_SAMPLES
        StrokePoint(700f + CIRCLE_RADIUS * cos(angle).toFloat(), 700f + CIRCLE_RADIUS * sin(angle).toFloat(), fullPressure)
    }
    private val circleSampleSpacing = distance(circle[0], circle[1])

    private fun distance(a: StrokePoint, b: StrokePoint): Float = hypot(a.x - b.x, a.y - b.y)
}
