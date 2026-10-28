package com.nkpainter.canvases

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class ViewportScreenToCanvasTest {
    private val area = AreaSize(1200, 800)
    private val tolerancePx = 0.001f

    private fun assertNear(expected: Pair<Float, Float>, actual: Pair<Float, Float>) {
        val near = abs(expected.first - actual.first) < tolerancePx && abs(expected.second - actual.second) < tolerancePx
        assertTrue(near, "expected canvas point $expected, was $actual")
    }

    @Test
    fun theCentreOfTheAreaIsTheCentreOfTheView() {
        val viewport = Viewport(1240f, 1754f, 0.25f, 0f)

        assertNear(Pair(1240f, 1754f), viewport.screenToCanvas(600f, 400f, area))
    }

    @Test
    fun aScreenDistanceIsDividedByTheZoom() {
        val viewport = Viewport(1240f, 1754f, 0.25f, 0f)

        assertNear(Pair(1280f, 1754f), viewport.screenToCanvas(610f, 400f, area))
    }

    @Test
    fun aRotatedViewTurnsTheScreenDistanceBack() {
        val viewport = Viewport(100f, 100f, 1f, 90f)

        assertNear(Pair(100f, 90f), viewport.screenToCanvas(610f, 400f, area))
    }
}
