package com.nkpainter.canvases

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ViewportGestureTest {
    private val area = AreaSize(1000, 1000)
    private val tolerance = 0.01f
    private val atHundredPercent = Viewport(1240f, 1000f, 1f, 0f)

    private fun assertNear(expected: Pair<Float, Float>, actual: Pair<Float, Float>) {
        val near = abs(expected.first - actual.first) < tolerance && abs(expected.second - actual.second) < tolerance
        assertTrue(near, "expected canvas point $expected, was $actual")
    }

    private fun assertNear(expected: Float, actual: Float) {
        assertTrue(abs(expected - actual) < tolerance, "expected $expected, was $actual")
    }

    private fun pinch(fromX: Float, fromY: Float, scale: Float) = ViewGestureStep(fromX, fromY, fromX, fromY, scale, 0f)

    @Test
    fun pinchingOpenKeepsTheCanvasPixelUnderTheFingersCentre() {
        val fingersCentreOverPixel = Pair(460f, 300f)

        val zoomed = atHundredPercent.movedBy(pinch(fingersCentreOverPixel.first, fingersCentreOverPixel.second, 2f), area)

        assertNear(Pair(1200f, 800f), zoomed.screenToCanvas(fingersCentreOverPixel.first, fingersCentreOverPixel.second, area))
    }

    @Test
    fun pinchingOpenAtTheLargestZoomStaysThere() {
        val atLargest = atHundredPercent.copy(zoom = 32f)

        val zoomed = atLargest.movedBy(pinch(500f, 500f, 1.5f), area)

        assertEquals(32f, zoomed.zoom)
    }

    @Test
    fun pinchingClosedAtTheSmallestZoomStaysThere() {
        val atSmallest = atHundredPercent.copy(zoom = 0.05f)

        val zoomed = atSmallest.movedBy(pinch(500f, 500f, 0.5f), area)

        assertEquals(0.05f, zoomed.zoom)
    }

    @Test
    fun aPinchStoppedAtTheLimitStillKeepsThePixelUnderTheFingers() {
        val atLargest = atHundredPercent.copy(zoom = 32f)
        val under = atLargest.screenToCanvas(300f, 700f, area)

        val zoomed = atLargest.movedBy(pinch(300f, 700f, 1.5f), area)

        assertNear(under, zoomed.screenToCanvas(300f, 700f, area))
    }

    @Test
    fun draggingTwoFingersRightMovesTheDrawingTheSameDistanceRight() {
        val before = atHundredPercent.screenToCanvas(500f, 500f, area)

        val moved = atHundredPercent.movedBy(ViewGestureStep(500f, 500f, 800f, 500f, 1f, 0f), area)

        assertNear(before, moved.screenToCanvas(800f, 500f, area))
    }

    @Test
    fun turningTwoFingersTurnsTheView() {
        val turned = atHundredPercent.movedBy(ViewGestureStep(500f, 500f, 500f, 500f, 1f, 30f), area)

        assertNear(30f, turned.rotationDegrees)
    }

    @Test
    fun turningBackPastZeroReadsAsAlmostAFullTurn() {
        val turned = atHundredPercent.movedBy(ViewGestureStep(500f, 500f, 500f, 500f, 1f, -1f), area)

        assertNear(359f, turned.rotationDegrees)
    }

    @Test
    fun aTurnEndingNearARightAngleSnapsToIt() {
        val snapped = atHundredPercent.copy(rotationDegrees = 86f).snapped()

        assertEquals(90f, snapped.rotationDegrees)
    }

    @Test
    fun aTurnEndingFarFromARightAngleStaysWhereItWasLeft() {
        val snapped = atHundredPercent.copy(rotationDegrees = 80f).snapped()

        assertEquals(80f, snapped.rotationDegrees)
    }

    @Test
    fun aTurnEndingJustUnderAFullTurnSnapsToZero() {
        val snapped = atHundredPercent.copy(rotationDegrees = 357f).snapped()

        assertEquals(0f, snapped.rotationDegrees)
    }

    @Test
    fun aTurnEndingFiveDegreesAwaySnaps() {
        val snapped = atHundredPercent.copy(rotationDegrees = 185f).snapped()

        assertEquals(180f, snapped.rotationDegrees)
    }
}
