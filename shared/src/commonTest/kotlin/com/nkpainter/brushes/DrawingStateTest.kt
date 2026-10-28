package com.nkpainter.brushes

import com.nkpainter.engine.PremultipliedColor
import kotlin.test.Test
import kotlin.test.assertEquals

class DrawingStateTest {
    private class MemoryStore(var saved: DrawingSettings? = null) : DrawingSettingsStore {
        override fun load(): DrawingSettings = saved ?: DrawingSettings.FACTORY

        override fun save(settings: DrawingSettings) {
            saved = settings
        }
    }

    @Test
    fun theBaseSetHasTheSevenBrushesInOrder() {
        assertEquals(
            listOf("Lápiz", "Tinta", "Rotulador", "Pincel redondo", "Aerógrafo", "Pincel suave", "Píxel"),
            Brush.entries.map { it.displayName },
        )
    }

    @Test
    fun theFirstTimeThePencilIsActive() {
        val drawing = DrawingState(MemoryStore())

        assertEquals(Brush.PENCIL, drawing.activeBrush)
    }

    @Test
    fun choosingABrushLeavesItAsTheOnlyActiveOne() {
        val drawing = DrawingState(MemoryStore())

        drawing.choose(Brush.AIRBRUSH)

        assertEquals(Brush.AIRBRUSH, drawing.activeBrush)
    }

    @Test
    fun theLastBrushUsedComesBackOnReopening() {
        val store = MemoryStore()
        DrawingState(store).choose(Brush.INK)

        val reopened = DrawingState(store)

        assertEquals(Brush.INK, reopened.activeBrush)
    }

    @Test
    fun theFirstTimeThePressureCurveIsNormal() {
        val drawing = DrawingState(MemoryStore())

        assertEquals(PressureCurve.NORMAL, drawing.pressureCurve)
    }

    @Test
    fun theChosenCurveComesBackOnReopening() {
        val store = MemoryStore()
        DrawingState(store).choose(PressureCurve.SOFT)

        val reopened = DrawingState(store)

        assertEquals(PressureCurve.SOFT, reopened.pressureCurve)
    }

    @Test
    fun theChosenCurveAppliesToTheNextStrokeOfAnyBrush() {
        val drawing = DrawingState(MemoryStore())
        drawing.choose(PressureCurve.FIRM)

        drawing.choose(Brush.AIRBRUSH)

        assertEquals(PressureCurve.FIRM, drawing.strokeStyle().pressureCurve)
    }

    @Test
    fun withoutAColorChosenTheBrushPaintsBlack() {
        val drawing = DrawingState(MemoryStore())

        assertEquals(PremultipliedColor.opaque(0, 0, 0), drawing.activeColor)
    }
}
