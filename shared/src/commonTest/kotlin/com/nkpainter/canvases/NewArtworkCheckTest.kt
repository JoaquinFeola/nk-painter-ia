package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals

class NewArtworkCheckTest {
    private val roomy = DeviceCanvasLimit(warningPixels = 200_000_000, redLinePixels = 250_000_000)
    private val midRange = DeviceCanvasLimit(warningPixels = 40_000_000, redLinePixels = 75_000_000)

    private fun pixels(width: Int, height: Int, dpi: Double) = NewArtworkRequest(width.toDouble(), height.toDouble(), LengthUnit.PIXELS, dpi)

    private fun ready(width: Int, height: Int, dpi: Int) = NewArtworkCheck.Ready(CanvasSize(width, height, Resolution(dpi)), CanvasBackground.White)

    @Test
    fun aSideBelow16PxIsNotCreated() {
        val check = pixels(15, 500, 72.0).check(roomy)

        assertEquals(NewArtworkCheck.Rejected(listOf(RejectionReason.SideBelowMinimum(widthTooSmall = true, heightTooSmall = false))), check)
    }

    @Test
    fun sixteenPxPerSideIsCreated() {
        assertEquals(ready(16, 16, 72), pixels(16, 16, 72.0).check(roomy))
    }

    @Test
    fun aSizeInMillimetresBelow16PxIsNotCreated() {
        val check = NewArtworkRequest(5.0, 5.0, LengthUnit.MILLIMETERS, 72.0).check(roomy)

        assertEquals(NewArtworkCheck.Rejected(listOf(RejectionReason.SideBelowMinimum(widthTooSmall = true, heightTooSmall = true))), check)
    }

    @Test
    fun aResolutionBelow72DpiIsNotCreated() {
        assertEquals(NewArtworkCheck.Rejected(listOf(RejectionReason.ResolutionOutOfRange)), pixels(1000, 1000, 71.0).check(roomy))
    }

    @Test
    fun aResolutionAbove600DpiIsNotCreated() {
        assertEquals(NewArtworkCheck.Rejected(listOf(RejectionReason.ResolutionOutOfRange)), pixels(1000, 1000, 601.0).check(roomy))
    }

    @Test
    fun theEndsOfTheRangeAreCreated() {
        assertEquals(ready(1000, 1000, 600), pixels(1000, 1000, 600.0).check(roomy))
    }

    @Test
    fun aResolutionWithDecimalsIsNotCreated() {
        assertEquals(NewArtworkCheck.Rejected(listOf(RejectionReason.ResolutionNotWhole)), pixels(1000, 1000, 72.5).check(roomy))
    }

    @Test
    fun twoReasonsAreToldTogether() {
        val check = pixels(10, 10, 700.0).check(roomy)

        val expected = listOf(RejectionReason.SideBelowMinimum(widthTooSmall = true, heightTooSmall = true), RejectionReason.ResolutionOutOfRange)
        assertEquals(NewArtworkCheck.Rejected(expected), check)
    }

    @Test
    fun aCanvasBigForTheDeviceAsksBeforeBeingCreated() {
        assertEquals(NewArtworkCheck.NeedsConfirmation(ready(6000, 8000, 72)), pixels(6000, 8000, 72.0).check(midRange))
    }

    @Test
    fun aCanvasAboveTheRedLineIsNotCreatedAndTheLargestOfTheSameShapeIsTold() {
        val check = pixels(12000, 16000, 72.0).check(midRange)

        assertEquals(NewArtworkCheck.Rejected(listOf(RejectionReason.AboveRedLine(AreaSize(7500, 10000)))), check)
    }

    @Test
    fun theSameCanvasIsCreatedWithoutWarningOnABiggerDevice() {
        assertEquals(ready(12000, 16000, 72), pixels(12000, 16000, 72.0).check(roomy))
    }

    @Test
    fun aCanvasExactlyAtTheWarningDoesNotWarn() {
        val atWarning = DeviceCanvasLimit(warningPixels = 48_000_000, redLinePixels = 75_000_000)

        assertEquals(ready(6000, 8000, 72), pixels(6000, 8000, 72.0).check(atWarning))
    }
}
