package com.nkpainter.brushes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nkpainter.engine.PremultipliedColor

/**
 * What the drawing tools work with: the active tool; one active brush with its own settings; the
 * eraser with its shape and settings; the pressure curve; and the current color. It lives with the
 * app, not with an artwork, and every change is saved once it ends.
 */
class DrawingState(private val store: DrawingSettingsStore) {
    private var settings = store.load()

    /** Not remembered between sessions: the app always opens with the brush, so the first stroke paints. */
    var activeTool: Tool by mutableStateOf(Tool.BRUSH)
        private set

    var activeBrush: Brush by mutableStateOf(settings.activeBrush)
        private set

    var pressureCurve: PressureCurve by mutableStateOf(settings.pressureCurve)
        private set

    /** The brush whose shape the eraser takes away paint with. */
    var eraserShape: Brush by mutableStateOf(settings.eraserShape)
        private set

    private val brushSettings = mutableStateMapOf<Brush, BrushSettings>().apply {
        Brush.entries.forEach { put(it, settings.settingsOf(it)) }
    }

    private var eraserSettings by mutableStateOf(settings.eraserSettings)

    /** The size, opacity and stabilizer of the active tool: the active brush, or the eraser. */
    val activeSettings: BrushSettings
        get() = if (activeTool == Tool.ERASER) eraserSettings else brushSettings.getValue(activeBrush)

    /** Black until colors can be chosen. */
    val activeColor: PremultipliedColor = PremultipliedColor.opaque(0, 0, 0)

    fun use(tool: Tool) {
        activeTool = tool
    }

    fun choose(brush: Brush) {
        if (brush == activeBrush) return
        activeBrush = brush
        save(settings.copy(activeBrush = brush))
    }

    fun chooseEraserShape(brush: Brush) {
        if (brush == eraserShape) return
        eraserShape = brush
        save(settings.copy(eraserShape = brush))
    }

    fun choose(curve: PressureCurve) {
        if (curve == pressureCurve) return
        pressureCurve = curve
        save(settings.copy(pressureCurve = curve))
    }

    // Adjusting follows a slider as it moves; saving waits for the change to end (QT-98).
    fun adjustSize(px: Int) = adjust { it.withSize(px) }

    fun adjustOpacity(percent: Int) = adjust { it.withOpacity(percent) }

    fun adjustStabilizer(level: Int) = adjust { it.withStabilizer(level) }

    /** Saves the adjustments made since the last save. */
    fun saveAdjustments() {
        if (brushSettings.toMap() != settings.brushSettings || eraserSettings != settings.eraserSettings) save(settings)
    }

    /** What a stroke starting now paints, or erases, with. */
    fun strokeStyle(): StrokeStyle {
        val adjusted = activeSettings
        val erases = activeTool == Tool.ERASER
        val shape = if (erases) eraserShape else activeBrush
        return StrokeStyle(shape, activeColor, adjusted.sizePx, adjusted.opacityPercent, pressureCurve, adjusted.stabilizer, erases)
    }

    private fun adjust(change: (BrushSettings) -> BrushSettings) {
        if (activeTool == Tool.ERASER) eraserSettings = change(eraserSettings) else brushSettings[activeBrush] = change(brushSettings.getValue(activeBrush))
    }

    // Whatever is saved carries the adjustments so far, so choosing never drops an unsaved one.
    private fun save(changed: DrawingSettings) {
        settings = changed.copy(brushSettings = brushSettings.toMap(), eraserSettings = eraserSettings)
        store.save(settings)
    }
}
