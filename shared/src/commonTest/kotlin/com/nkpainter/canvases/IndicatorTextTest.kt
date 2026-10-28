package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals

class IndicatorTextTest {
    @Test
    fun showsZoomAsAPercentageAndTheTurnInDegrees() {
        val text = indicatorText(Viewport(0f, 0f, 2.5f, 45f))

        assertEquals("250 % · 45°", text)
    }

    @Test
    fun aTurnJustUnderAFullTurnRoundsToZeroNotThreeSixty() {
        val text = indicatorText(Viewport(0f, 0f, 1f, 359.7f))

        assertEquals("100 % · 0°", text)
    }

    @Test
    fun aTurnBackPastZeroReadsAs359() {
        val text = indicatorText(Viewport(0f, 0f, 1f, 359f))

        assertEquals("100 % · 359°", text)
    }
}
