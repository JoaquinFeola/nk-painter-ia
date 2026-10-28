package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BackgroundPanelTest {
    private val roomy = DeviceCanvasLimit(warningPixels = 200_000_000, redLinePixels = 250_000_000)

    private fun artworkWith(background: CanvasBackground) =
        Artwork.create(NewArtworkRequest(2480.0, 3508.0, LengthUnit.PIXELS, 300.0, background).check(roomy) as NewArtworkCheck.Ready, AreaSize(1200, 800))

    @Test
    fun opensWithTheArtworksCurrentColorChosen() {
        val panel = BackgroundPanel(artworkWith(CanvasBackground.Solid(0x1E2A3A)))

        assertEquals(BackgroundPanel.Choice.COLOR, panel.choice)
        assertEquals("#1E2A3A", panel.colorText)
    }

    @Test
    fun choosingTransparentChangesTheBackgroundAtOnce() {
        val artwork = artworkWith(CanvasBackground.White)
        val panel = BackgroundPanel(artwork)

        panel.choose(BackgroundPanel.Choice.TRANSPARENT)

        assertEquals(CanvasBackground.Transparent, artwork.background)
    }

    @Test
    fun typingAValidColorChangesTheBackgroundAtOnce() {
        val artwork = artworkWith(CanvasBackground.White)
        val panel = BackgroundPanel(artwork)
        panel.choose(BackgroundPanel.Choice.COLOR)

        panel.typeColor("#1e2a3a")

        assertEquals(CanvasBackground.Solid(0x1E2A3A), artwork.background)
    }

    @Test
    fun aMistypedColorLeavesTheBackgroundAndSaysWhy() {
        val artwork = artworkWith(CanvasBackground.White)
        val panel = BackgroundPanel(artwork)
        panel.choose(BackgroundPanel.Choice.COLOR)

        panel.typeColor("#12345G")

        assertEquals(CanvasBackground.White, artwork.background)
        assertTrue(panel.showsColorError)
    }

    @Test
    fun aColorStillBeingTypedIsNotAnErrorYet() {
        val panel = BackgroundPanel(artworkWith(CanvasBackground.White))
        panel.choose(BackgroundPanel.Choice.COLOR)

        panel.typeColor("#1E2")

        assertFalse(panel.showsColorError)
    }
}
