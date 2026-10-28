package com.nkpainter.brushes

import kotlin.test.Test
import kotlin.test.assertEquals

class BrushSettingsTest {
    private class MemoryStore(var saved: DrawingSettings? = null) : DrawingSettingsStore {
        var saves = 0

        override fun load(): DrawingSettings = saved ?: DrawingSettings.FACTORY

        override fun save(settings: DrawingSettings) {
            saved = settings
            saves++
        }
    }

    private fun drawingWith(brush: Brush, store: MemoryStore = MemoryStore()) = DrawingState(store).apply { choose(brush) }

    @Test
    fun theChosenSizeIsTheOneTheStrokePaintsWith() {
        val drawing = drawingWith(Brush.ROUND)

        drawing.adjustSize(40)

        assertEquals(40, drawing.strokeStyle().sizePx)
    }

    @Test
    fun theChosenOpacityIsTheOneTheStrokePaintsWith() {
        val drawing = drawingWith(Brush.MARKER)

        drawing.adjustOpacity(50)

        assertEquals(50, drawing.strokeStyle().opacityPercent)
    }

    @Test
    fun theEndsOfTheRangesAreKept() {
        val drawing = drawingWith(Brush.ROUND)

        drawing.adjustSize(1)
        drawing.adjustOpacity(1)
        val lowest = drawing.activeSettings
        drawing.adjustSize(500)
        drawing.adjustOpacity(100)

        assertEquals(BrushSettings(1, 1, 0), lowest)
        assertEquals(BrushSettings(500, 100, 0), drawing.activeSettings)
    }

    @Test
    fun aSizeAboveTheRangeStaysAt500() {
        val drawing = drawingWith(Brush.ROUND)

        drawing.adjustSize(800)

        assertEquals(500, drawing.activeSettings.sizePx)
    }

    @Test
    fun aSizeOfZeroStaysAt1() {
        val drawing = drawingWith(Brush.ROUND)

        drawing.adjustSize(0)

        assertEquals(1, drawing.activeSettings.sizePx)
    }

    @Test
    fun anOpacityAboveTheRangeStaysAt100() {
        val drawing = drawingWith(Brush.MARKER)

        drawing.adjustOpacity(150)

        assertEquals(100, drawing.activeSettings.opacityPercent)
    }

    @Test
    fun theStabilizerGoesUpTo10AndNoFurther() {
        val drawing = drawingWith(Brush.PENCIL)
        drawing.adjustStabilizer(10)

        drawing.adjustStabilizer(11)

        assertEquals(10, drawing.activeSettings.stabilizer)
    }

    @Test
    fun eachBrushKeepsItsOwnSettings() {
        val drawing = drawingWith(Brush.INK)
        drawing.adjustSize(8)
        drawing.adjustStabilizer(3)
        drawing.choose(Brush.AIRBRUSH)
        drawing.adjustSize(120)

        drawing.choose(Brush.INK)

        assertEquals(BrushSettings(8, 100, 3), drawing.activeSettings)
        drawing.choose(Brush.AIRBRUSH)
        assertEquals(120, drawing.activeSettings.sizePx)
    }

    @Test
    fun theSettingsComeBackOnReopening() {
        val store = MemoryStore()
        val drawing = drawingWith(Brush.PENCIL, store)
        drawing.adjustSize(12)
        drawing.adjustOpacity(70)
        drawing.saveAdjustments()

        val reopened = DrawingState(store)

        assertEquals(BrushSettings(12, 70, 0), reopened.activeSettings)
    }

    @Test
    fun movingASliderDoesNotSaveUntilTheChangeEnds() {
        val store = MemoryStore()
        val drawing = drawingWith(Brush.ROUND, store)
        val savesBefore = store.saves

        listOf(21, 22, 23, 24).forEach(drawing::adjustSize)

        assertEquals(savesBefore, store.saves)
        drawing.saveAdjustments()
        assertEquals(savesBefore + 1, store.saves)
    }

    @Test
    fun aStrokeKeepsTheSizeItStartedWith() {
        val drawing = drawingWith(Brush.MARKER)
        drawing.adjustSize(10)
        val firstStroke = drawing.strokeStyle()

        drawing.adjustSize(50)

        assertEquals(10, firstStroke.sizePx)
        assertEquals(50, drawing.strokeStyle().sizePx)
    }

    @Test
    fun theInkStartsWithStabilizer3() {
        val drawing = drawingWith(Brush.INK)

        assertEquals(3, drawing.strokeStyle().stabilizerLevel)
    }
}
