package com.nkpainter.brushes

import java.io.File
import java.io.IOException

/** One `key=value` line per setting. */
class FileDrawingSettingsStore(private val file: File) : DrawingSettingsStore {
    private companion object {
        const val SEPARATOR = "="
        const val ACTIVE_BRUSH = "activeBrush"
        const val PRESSURE_CURVE = "pressureCurve"
        const val ERASER_SHAPE = "eraserShape"
        const val ERASER_PREFIX = "eraser"
        const val BRUSH_PREFIX = "brush."
        const val SIZE = ".size"
        const val OPACITY = ".opacity"
        const val STABILIZER = ".stabilizer"
    }

    override fun load(): DrawingSettings {
        val values = try {
            if (file.exists()) file.readLines().mapNotNull(::parse).toMap() else emptyMap()
        } catch (_: IOException) {
            emptyMap()
        }
        // Each setting falls back to its factory value alone, so one unreadable line does not undo the rest.
        val factory = DrawingSettings.FACTORY
        return DrawingSettings(
            activeBrush = Brush.entries.firstOrNull { it.name == values[ACTIVE_BRUSH] } ?: factory.activeBrush,
            pressureCurve = PressureCurve.entries.firstOrNull { it.name == values[PRESSURE_CURVE] } ?: factory.pressureCurve,
            brushSettings = Brush.entries.associateWith { settingsAt("$BRUSH_PREFIX${it.name}", BrushSettings.factoryOf(it), values) },
            eraserShape = Brush.entries.firstOrNull { it.name == values[ERASER_SHAPE] } ?: factory.eraserShape,
            eraserSettings = settingsAt(ERASER_PREFIX, factory.eraserSettings, values),
        )
    }

    override fun save(settings: DrawingSettings) {
        val brushLines = Brush.entries.flatMap { linesOf("$BRUSH_PREFIX${it.name}", settings.settingsOf(it)) }
        val eraserLines = listOf("$ERASER_SHAPE$SEPARATOR${settings.eraserShape.name}") + linesOf(ERASER_PREFIX, settings.eraserSettings)
        try {
            file.parentFile?.mkdirs()
            file.writeText(
                (
                    listOf(
                        "$ACTIVE_BRUSH$SEPARATOR${settings.activeBrush.name}",
                        "$PRESSURE_CURVE$SEPARATOR${settings.pressureCurve.name}",
                    ) + brushLines + eraserLines
                ).joinToString("\n"),
            )
        } catch (_: IOException) {
            // Kept for this session only; see the interface.
        }
    }

    private fun linesOf(prefix: String, adjusted: BrushSettings) = listOf(
        "$prefix$SIZE$SEPARATOR${adjusted.sizePx}",
        "$prefix$OPACITY$SEPARATOR${adjusted.opacityPercent}",
        "$prefix$STABILIZER$SEPARATOR${adjusted.stabilizer}",
    )

    // A value that is not a number falls back to the factory one; one out of range stays at its end.
    private fun settingsAt(prefix: String, factory: BrushSettings, values: Map<String, String>): BrushSettings {
        fun read(suffix: String, fallback: Int) = values["$prefix$suffix"]?.toIntOrNull() ?: fallback
        return BrushSettings.of(read(SIZE, factory.sizePx), read(OPACITY, factory.opacityPercent), read(STABILIZER, factory.stabilizer))
    }

    private fun parse(line: String): Pair<String, String>? {
        val parts = line.split(SEPARATOR, limit = 2)
        return if (parts.size == 2) parts[0] to parts[1] else null
    }
}
