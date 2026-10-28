package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals

class CanvasPresetTest {
    private val anyScreen = AreaSize(1200, 2000)

    @Test
    fun a4IsItsExactPrintSize() {
        val size = CanvasPreset.A4.size(anyScreen)

        assertEquals(CanvasSize(2480, 3508, Resolution(300)), size)
    }

    @Test
    fun fullHdIsCreatedAt72Dpi() {
        val size = CanvasPreset.FULL_HD.size(anyScreen)

        assertEquals(CanvasSize(1920, 1080, Resolution(72)), size)
    }

    @Test
    fun theScreenPresetFollowsHowTheDeviceIsHeld() {
        val tabletHeldLandscape = AreaSize(2000, 1200)

        val size = CanvasPreset.SCREEN.size(tabletHeldLandscape)

        assertEquals(CanvasSize(2000, 1200, Resolution(72)), size)
    }
}
