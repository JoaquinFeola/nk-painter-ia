package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals

class ViewportFitTest {
    private val resolution = Resolution(72)

    @Test
    fun aWideCanvasFitsByItsWidth() {
        val viewport = Viewport.fit(CanvasSize(4000, 1000, resolution), AreaSize(1000, 1000))

        assertEquals(0.25f, viewport.zoom)
    }

    @Test
    fun aTinyCanvasStopsAtTheLargestZoom() {
        val largestZoom = 32f

        val viewport = Viewport.fit(CanvasSize(16, 16, resolution), AreaSize(2000, 2000))

        assertEquals(largestZoom, viewport.zoom)
    }

    @Test
    fun aHugeCanvasStopsAtTheSmallestZoom() {
        val smallestZoom = 0.05f

        val viewport = Viewport.fit(CanvasSize(100_000, 100_000, resolution), AreaSize(1000, 1000))

        assertEquals(smallestZoom, viewport.zoom)
    }
}
