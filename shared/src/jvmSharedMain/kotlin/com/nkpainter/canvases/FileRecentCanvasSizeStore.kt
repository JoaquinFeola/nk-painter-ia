package com.nkpainter.canvases

import java.io.File
import java.io.IOException

/** One line per size, "width height dpi", newest first. */
class FileRecentCanvasSizeStore(private val file: File) : RecentCanvasSizeStore {
    private companion object {
        const val SEPARATOR = " "
        const val FIELD_COUNT = 3
    }

    override fun load(): RecentCanvasSizes {
        val lines = try {
            if (file.exists()) file.readLines() else emptyList()
        } catch (_: IOException) {
            emptyList()
        }
        return RecentCanvasSizes(lines.mapNotNull(::parse))
    }

    override fun save(recent: RecentCanvasSizes) {
        file.parentFile?.mkdirs()
        file.writeText(recent.sizes.joinToString("\n") { "${it.widthPx}$SEPARATOR${it.heightPx}$SEPARATOR${it.resolution.dpi}" })
    }

    private fun parse(line: String): CanvasSize? {
        val fields = line.split(SEPARATOR).mapNotNull(String::toIntOrNull)
        if (fields.size != FIELD_COUNT) return null
        return CanvasSize(fields[0], fields[1], Resolution(fields[2]))
    }
}
