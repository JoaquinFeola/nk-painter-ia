package com.nkpainter.brushes

/** What drawing keeps between sessions on this device. It belongs to the app, never to an artwork. */
data class DrawingSettings(
    val activeBrush: Brush,
    val pressureCurve: PressureCurve = PressureCurve.NORMAL,
    val brushSettings: Map<Brush, BrushSettings> = Brush.entries.associateWith(BrushSettings::factoryOf),
    val eraserShape: Brush = FACTORY_ERASER_SHAPE,
    val eraserSettings: BrushSettings = BrushSettings.factoryOf(FACTORY_ERASER_SHAPE),
) {
    companion object {
        val FACTORY_ERASER_SHAPE = Brush.ROUND
        val FACTORY = DrawingSettings(activeBrush = Brush.PENCIL)
    }

    fun settingsOf(brush: Brush): BrushSettings = brushSettings[brush] ?: BrushSettings.factoryOf(brush)
}

/** Keeps the drawing settings of this device between sessions. */
interface DrawingSettingsStore {
    /** The factory settings when nothing was saved yet or what was saved cannot be read. */
    fun load(): DrawingSettings

    /** Settings that cannot be saved stay for this session only: there is nothing the illustrator could do about it. */
    fun save(settings: DrawingSettings)
}
