package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArtworkBackgroundTest {
    private val roomy = DeviceCanvasLimit(warningPixels = 200_000_000, redLinePixels = 250_000_000)
    private val drawingArea = AreaSize(1200, 800)
    private val navy = CanvasBackground.Solid(0x1E2A3A)
    private val cream = CanvasBackground.Solid(0xF5E6C8)

    private fun artworkWith(background: CanvasBackground) =
        Artwork.create(NewArtworkRequest(2480.0, 3508.0, LengthUnit.PIXELS, 300.0, background).check(roomy) as NewArtworkCheck.Ready, drawingArea)

    @Test
    fun aWhiteBackgroundChangesToAnotherColor() {
        val artwork = artworkWith(CanvasBackground.White)

        artwork.changeBackground(navy)

        assertEquals(navy, artwork.background)
    }

    @Test
    fun aWhiteBackgroundChangesToTransparent() {
        val artwork = artworkWith(CanvasBackground.White)

        artwork.changeBackground(CanvasBackground.Transparent)

        assertEquals(CanvasBackground.Transparent, artwork.background)
    }

    @Test
    fun aTransparentBackgroundChangesToAColor() {
        val artwork = artworkWith(CanvasBackground.Transparent)

        artwork.changeBackground(cream)

        assertEquals(cream, artwork.background)
    }

    @Test
    fun changingToAnotherBackgroundReportsAChange() {
        val artwork = artworkWith(CanvasBackground.White)

        val changed = artwork.changeBackground(navy)

        assertTrue(changed)
    }

    @Test
    fun choosingTheSameBackgroundChangesNothing() {
        val artwork = artworkWith(CanvasBackground.White)

        val changed = artwork.changeBackground(CanvasBackground.Solid(0xFFFFFF))

        assertFalse(changed)
    }

    @Test
    fun changingTheBackgroundLeavesTheLayerAsItWas() {
        val artwork = artworkWith(CanvasBackground.White)
        val layerBefore = artwork.activeLayer.pixels.paintedTiles().keys.toSet()

        artwork.changeBackground(navy)

        assertEquals(layerBefore, artwork.activeLayer.pixels.paintedTiles().keys)
    }
}
