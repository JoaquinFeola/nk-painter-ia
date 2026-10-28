package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ArtworkCreationTest {
    private val drawingArea = AreaSize(1200, 800)
    private val a4 = NewArtworkRequest(2480.0, 3508.0, LengthUnit.PIXELS, 300.0)
    private val roomy = DeviceCanvasLimit(warningPixels = 200_000_000, redLinePixels = 250_000_000)

    private fun create(request: NewArtworkRequest) = Artwork.create(request.check(roomy) as NewArtworkCheck.Ready, drawingArea)

    @Test
    fun withoutChoosingABackgroundItIsWhite() {
        val artwork = create(a4)

        assertEquals(CanvasBackground.Solid(0xFFFFFF), artwork.background)
    }

    @Test
    fun theChosenBackgroundColorIsTheArtworkBackground() {
        val artwork = create(a4.copy(background = CanvasBackground.Solid(0xF5E6C8)))

        assertEquals(CanvasBackground.Solid(0xF5E6C8), artwork.background)
    }

    @Test
    fun anArtworkCanBeCreatedWithATransparentBackground() {
        val artwork = create(a4.copy(background = CanvasBackground.Transparent))

        assertEquals(CanvasBackground.Transparent, artwork.background)
    }

    @Test
    fun aNewArtworkHasOneEmptyActiveLayerCalledCapa1() {
        val artwork = create(a4)

        assertEquals(listOf("Capa 1"), artwork.layers.map { it.name })
        assertSame(artwork.layers.single(), artwork.activeLayer)
        assertEquals(0, artwork.activeLayer.pixels.paintedTileCount)
    }

    @Test
    fun aNewArtworkOpensWholeCentredAndStraight() {
        val artwork = create(a4)

        val heightFits = 800f / 3508f
        assertEquals(Viewport(1240f, 1754f, heightFits, 0f), artwork.viewport)
    }
}
