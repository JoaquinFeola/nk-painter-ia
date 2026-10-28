package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals

class NewArtworkRequestTest {
    @Test
    fun aSizeInPixelsIsKeptAsIs() {
        val request = NewArtworkRequest(3000.0, 2000.0, LengthUnit.PIXELS, 150.0)

        assertEquals(CanvasSize(3000, 2000, Resolution(150)), request.canvasSize())
    }

    @Test
    fun aSizeInMillimetresIsRoundedToTheNearestPixel() {
        val request = NewArtworkRequest(100.0, 150.0, LengthUnit.MILLIMETERS, 300.0)

        assertEquals(CanvasSize(1181, 1772, Resolution(300)), request.canvasSize())
    }

    @Test
    fun aSizeInInchesIsTurnedIntoPixels() {
        val request = NewArtworkRequest(8.0, 10.0, LengthUnit.INCHES, 72.0)

        assertEquals(CanvasSize(576, 720, Resolution(72)), request.canvasSize())
    }
}
