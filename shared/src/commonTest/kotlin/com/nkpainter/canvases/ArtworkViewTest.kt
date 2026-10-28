package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArtworkViewTest {
    private val a4 = NewArtworkRequest(2480.0, 3508.0, LengthUnit.PIXELS, 300.0)
    private val roomy = DeviceCanvasLimit(warningPixels = 100_000_000, redLinePixels = 200_000_000)
    private val drawingArea = AreaSize(1000, 1400)

    private fun newArtwork() = Artwork.create(a4.check(roomy) as NewArtworkCheck.Ready, drawingArea)

    @Test
    fun fittingAMovedViewShowsTheWholeCanvasCentredAndStraight() {
        val artwork = newArtwork()
        artwork.moveView(Viewport(300f, 200f, 4f, 37f))

        artwork.fitView(drawingArea)

        assertEquals(Viewport.fit(artwork.canvasSize, drawingArea), artwork.viewport)
        assertEquals(0f, artwork.viewport.rotationDegrees)
    }

    @Test
    fun aNewArtworkStartsFitted() {
        val artwork = newArtwork()

        assertTrue(artwork.viewIsFitted)
    }

    @Test
    fun movingTheViewLeavesItNoLongerFitted() {
        val artwork = newArtwork()

        artwork.moveView(Viewport(300f, 200f, 4f, 37f))

        assertFalse(artwork.viewIsFitted)
    }
}
