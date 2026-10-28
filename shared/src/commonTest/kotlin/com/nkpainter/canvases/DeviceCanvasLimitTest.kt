package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeviceCanvasLimitTest {
    private val gigabyte = 1L shl 30

    @Test
    fun theRedLineIsTheBudgetAt16BytesPerPixelAndTheWarningHalfOfIt() {
        val limit = DeviceCanvasLimit.fromBudget(gigabyte)

        assertEquals(DeviceCanvasLimit(warningPixels = gigabyte / 32, redLinePixels = gigabyte / 16), limit)
    }

    @Test
    fun theLargestCanvasOfTheSameShapeIsNeverAboveTheRedLine() {
        val limit = DeviceCanvasLimit(warningPixels = 1, redLinePixels = 10_000_000)

        val largest = limit.largestWithSameShape(3000, 7000)

        assertTrue(largest.widthPx.toLong() * largest.heightPx <= 10_000_000, "largest was $largest")
    }
}
