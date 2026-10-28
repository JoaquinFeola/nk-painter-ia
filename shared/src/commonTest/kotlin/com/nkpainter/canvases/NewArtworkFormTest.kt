package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NewArtworkFormTest {
    private val screen = AreaSize(1200, 2000)

    @Test
    fun a4IsChosenWhenTheScreenOpens() {
        val form = NewArtworkForm(screen)

        assertEquals(NewArtworkRequest(2480.0, 3508.0, LengthUnit.PIXELS, 300.0), form.toRequest())
    }

    @Test
    fun choosingARecentSizeFillsTheCustomFields() {
        val form = NewArtworkForm(screen)

        form.chooseRecent(CanvasSize(3000, 2000, Resolution(150)))

        assertEquals(NewArtworkRequest(3000.0, 2000.0, LengthUnit.PIXELS, 150.0), form.toRequest())
    }

    @Test
    fun aDecimalCommaIsReadAsADecimalPoint() {
        val form = NewArtworkForm(screen)

        form.dpiText = "72,5"

        assertEquals(72.5, form.toRequest()?.dpi)
    }

    @Test
    fun aFieldThatIsNotANumberCreatesNothing() {
        val form = NewArtworkForm(screen)

        form.widthText = "ancho"

        assertNull(form.toRequest())
    }

    @Test
    fun theChosenColorBecomesTheBackground() {
        val form = NewArtworkForm(screen)
        form.background = NewArtworkForm.BackgroundChoice.COLOR

        form.colorText = "#F5E6C8"

        assertEquals(CanvasBackground.Solid(0xF5E6C8), form.toRequest()?.background)
    }

    @Test
    fun aColorThatIsNotRrggbbCreatesNothing() {
        val form = NewArtworkForm(screen)
        form.background = NewArtworkForm.BackgroundChoice.COLOR

        form.colorText = "#F5E6"

        assertNull(form.toRequest())
    }
}
