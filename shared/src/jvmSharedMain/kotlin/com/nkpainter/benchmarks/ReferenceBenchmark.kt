package com.nkpainter.benchmarks

import com.nkpainter.engine.LayerCompositor
import com.nkpainter.engine.PremultipliedColor
import com.nkpainter.engine.Tile
import com.nkpainter.engine.TileCoordinate
import com.nkpainter.projectfiles.ProjectFileContent
import com.nkpainter.projectfiles.ProjectFileFormat
import com.nkpainter.projectfiles.ProjectFileReadResult
import com.nkpainter.projectfiles.ProjectFileReader
import com.nkpainter.projectfiles.ProjectFileWriter
import java.io.File
import java.util.Locale

/**
 * Measures the reference artwork on the device it runs on: building it, memory, combining the
 * layers, and exporting and importing the project file. Prints one line per result.
 */
class ReferenceBenchmark(private val workFolder: File, private val memoryReport: () -> String, private val log: (String) -> Unit) {
    companion object {
        private const val NANOS_PER_MILLI = 1_000_000.0
        private const val BYTES_PER_MEGABYTE = 1024.0 * 1024.0
        // A Full HD screen covers 8 × 5 tiles at 100 % zoom.
        private const val SCREEN_COLUMNS = 8
        private const val SCREEN_ROWS = 5
        private val white = PremultipliedColor.opaque(255, 255, 255)
    }

    fun run() {
        log("benchmark: start · ${memoryReport()}")
        val file = File(workFolder, "referencia.${ProjectFileFormat.EXTENSION}")
        val originalDigests = buildMeasureAndExport(file)
        importAndCompare(file, originalDigests)
        file.delete()
        log("benchmark: done")
    }

    private fun buildMeasureAndExport(file: File): Pair<String, String> {
        val (artwork, buildMs) = timed { ReferenceArtwork.build() }
        val tiles = artwork.layers.sumOf { it.pixels.paintedTileCount }
        log("benchmark: built ${artwork.layers.size} layers, $tiles painted tiles (${megabytes(tiles.toLong() * Tile.BYTE_COUNT)} MB raw) in ${millis(buildMs)} ms")
        log("benchmark: after build · ${memoryReport()}")

        val digests = layerDigest(artwork) to compositeDigest(artwork)
        log("benchmark: layer digest ${digests.first} · composite digest ${digests.second}")

        val (_, screenMs) = timed { compositeScreen(artwork) }
        log("benchmark: combine one Full HD screen of tiles (${SCREEN_COLUMNS * SCREEN_ROWS} tiles, 30 layers) in ${millis(screenMs)} ms")

        val (_, exportMs) = timed { file.outputStream().buffered().use { ProjectFileWriter("benchmark").write(artwork, it) } }
        log("benchmark: export ${megabytes(file.length())} MB in ${millis(exportMs)} ms")
        release(artwork)
        return digests
    }

    private fun importAndCompare(file: File, originalDigests: Pair<String, String>) {
        val (result, importMs) = timed { file.inputStream().use(ProjectFileReader()::read) }
        val imported = (result as? ProjectFileReadResult.Read)?.content
        if (imported == null) {
            log("benchmark: import FAILED: $result")
            return
        }
        log("benchmark: import in ${millis(importMs)} ms · ${memoryReport()}")
        val same = layerDigest(imported) == originalDigests.first && compositeDigest(imported) == originalDigests.second
        log("benchmark: imported pixels identical to exported: $same")
        release(imported)
    }

    private fun release(content: ProjectFileContent) {
        content.layers.forEach { it.pixels.release() }
    }

    private fun layerDigest(content: ProjectFileContent): String {
        val digest = PixelDigest()
        val bytes = ByteArray(Tile.BYTE_COUNT)
        content.layers.forEach { layer ->
            layer.pixels.paintedTiles().entries.sortedWith(compareBy({ it.key.row }, { it.key.column })).forEach {
                it.value.pixels.copyInto(bytes)
                digest.add(bytes)
            }
        }
        return digest.hex()
    }

    private fun compositeDigest(content: ProjectFileContent): String {
        val compositor = LayerCompositor(white)
        val sources = sourcesOf(content)
        val buffer = ByteArray(Tile.BYTE_COUNT)
        val digest = PixelDigest()
        val grid = content.layers.first().pixels
        for (row in 0 until grid.rowCount) {
            for (column in 0 until grid.columnCount) {
                digest.add(compositor.compositeTile(sources, TileCoordinate(column, row), buffer))
            }
        }
        return digest.hex()
    }

    private fun compositeScreen(content: ProjectFileContent) {
        val compositor = LayerCompositor(white)
        val sources = sourcesOf(content)
        val buffer = ByteArray(Tile.BYTE_COUNT)
        for (row in 0 until SCREEN_ROWS) {
            for (column in 0 until SCREEN_COLUMNS) {
                compositor.compositeTile(sources, TileCoordinate(column, row), buffer)
            }
        }
    }

    private fun sourcesOf(content: ProjectFileContent) =
        content.layers.map { LayerCompositor.Source(it.pixels, it.opacity, it.visible) }

    private inline fun <T> timed(block: () -> T): Pair<T, Long> {
        val start = System.nanoTime()
        val value = block()
        return value to System.nanoTime() - start
    }

    private fun millis(nanos: Long): String = String.format(Locale.ROOT, "%.1f", nanos / NANOS_PER_MILLI)

    private fun megabytes(bytes: Long): String = String.format(Locale.ROOT, "%.1f", bytes / BYTES_PER_MEGABYTE)
}
