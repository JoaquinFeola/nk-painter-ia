package com.nkpainter.desktop.canvases

import com.nkpainter.brushes.DrawingSettings
import com.nkpainter.brushes.DrawingSettingsStore

/** Drawing settings that start from the factory values and are never kept. */
class FactoryDrawingSettings : DrawingSettingsStore {
    override fun load() = DrawingSettings.FACTORY

    override fun save(settings: DrawingSettings) = Unit
}
