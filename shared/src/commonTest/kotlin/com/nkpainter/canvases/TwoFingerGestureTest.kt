package com.nkpainter.canvases

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TwoFingerGestureTest {
    private val tolerance = 0.01f

    private fun assertNear(expected: Float, actual: Float) {
        assertTrue(abs(expected - actual) < tolerance, "expected $expected, was $actual")
    }

    private fun twoFingersDown(firstX: Float, firstY: Float, secondX: Float, secondY: Float) = TwoFingerGesture().apply {
        press(Finger(1, firstX, firstY))
        press(Finger(2, secondX, secondY))
    }

    @Test
    fun spreadingTheFingersToTwiceTheDistanceScalesByTwo() {
        val gesture = twoFingersDown(450f, 500f, 550f, 500f)

        val step = gesture.move(listOf(Finger(1, 400f, 500f), Finger(2, 600f, 500f)))

        assertNear(2f, step!!.scale)
    }

    @Test
    fun turningTheFingersThirtyDegreesTurnsByThirty() {
        val gesture = twoFingersDown(400f, 500f, 600f, 500f)
        val radius = 100f
        val thirtyDegreesX = radius * 0.8660254f
        val thirtyDegreesY = radius * 0.5f

        val step = gesture.move(listOf(Finger(1, 500f - thirtyDegreesX, 500f - thirtyDegreesY), Finger(2, 500f + thirtyDegreesX, 500f + thirtyDegreesY)))

        assertNear(30f, step!!.rotationDegrees)
    }

    @Test
    fun movingBothFingersRightMovesTheirCentreRight() {
        val gesture = twoFingersDown(400f, 500f, 600f, 500f)

        val step = gesture.move(listOf(Finger(1, 700f, 500f), Finger(2, 900f, 500f)))!!

        assertEquals(Pair(500f, 500f), Pair(step.fromX, step.fromY))
        assertEquals(Pair(800f, 500f), Pair(step.toX, step.toY))
    }

    @Test
    fun movingOneFingerAtATimeStillGivesAStep() {
        val gesture = twoFingersDown(400f, 500f, 600f, 500f)

        val step = gesture.move(listOf(Finger(2, 800f, 500f)))

        assertNear(2f, step!!.scale)
    }

    @Test
    fun oneFingerAloneIsNotAGesture() {
        val gesture = TwoFingerGesture().apply { press(Finger(1, 400f, 500f)) }

        val step = gesture.move(listOf(Finger(1, 450f, 500f)))

        assertNull(step)
    }

    @Test
    fun aThirdFingerIsIgnored() {
        val gesture = twoFingersDown(400f, 500f, 600f, 500f)
        gesture.press(Finger(3, 100f, 100f))

        val step = gesture.move(listOf(Finger(3, 900f, 900f)))

        assertNull(step)
    }

    @Test
    fun liftingOneOfTheTwoFingersEndsTheGesture() {
        val gesture = twoFingersDown(400f, 500f, 600f, 500f)

        val ended = gesture.release(2)

        assertTrue(ended)
        assertFalse(gesture.isActive)
    }

    @Test
    fun liftingAFingerThatWasIgnoredDoesNotEndTheGesture() {
        val gesture = twoFingersDown(400f, 500f, 600f, 500f)
        gesture.press(Finger(3, 100f, 100f))

        val ended = gesture.release(3)

        assertFalse(ended)
        assertTrue(gesture.isActive)
    }
}
