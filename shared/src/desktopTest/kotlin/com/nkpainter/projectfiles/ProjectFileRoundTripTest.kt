package com.nkpainter.projectfiles

import com.nkpainter.engine.PremultipliedColor
import com.nkpainter.engine.RoundBrush
import com.nkpainter.engine.StrokePoint
import com.nkpainter.engine.StrokeRasterizer
import com.nkpainter.engine.TileGrid
import com.nkpainter.engine.toByteArray
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Integration level: writes and reads real files on this machine's disk. */
class ProjectFileRoundTripTest {
    private val folder: File = createTempDirectory("nkpainter-test").toFile()
    private val writer = ProjectFileWriter(appVersion = "0.1.0")
    private val reader = ProjectFileReader()
    private val widthPx = 600
    private val heightPx = 400

    @AfterTest
    fun deleteFolder() {
        folder.deleteRecursively()
    }

    private fun paintedGrid(color: PremultipliedColor, y: Float): TileGrid {
        val grid = TileGrid(widthPx, heightPx)
        StrokeRasterizer(RoundBrush(30f, color)).paint(grid, listOf(StrokePoint(20f, y, 0.3f), StrokePoint(580f, y + 50f, 1f)))
        return grid
    }

    private fun sampleContent() = ProjectFileContent(
        name = "Retrato",
        widthPx = widthPx,
        heightPx = heightPx,
        dpi = 300,
        background = "#FFFFFF",
        layers = listOf(
            ProjectFileLayer("layer-1", "Boceto", opacity = 40, visible = false, clipping = false, alphaLock = false, pixels = paintedGrid(PremultipliedColor(10, 10, 10, 128), 50f)),
            ProjectFileLayer("layer-2", "Lineart", opacity = 100, visible = true, clipping = true, alphaLock = true, pixels = paintedGrid(PremultipliedColor.opaque(20, 60, 200), 200f)),
            ProjectFileLayer("layer-3", "Vacía", opacity = 100, visible = true, clipping = false, alphaLock = false, pixels = TileGrid(widthPx, heightPx)),
        ),
        view = ProjectManifest.ManifestView(centerX = 300f, centerY = 200f, zoom = 1.5f, rotationDeg = 90f),
    )

    private fun bytesOf(content: ProjectFileContent): ByteArray =
        ByteArrayOutputStream().also { writer.write(content, it) }.toByteArray()

    private fun zipWith(vararg entries: Pair<String, ByteArray>): ByteArray {
        val output = ByteArrayOutputStream()
        ZipOutputStream(output).use { zip ->
            entries.forEach { (name, bytes) ->
                zip.putNextEntry(ZipEntry(name))
                zip.write(bytes)
                zip.closeEntry()
            }
        }
        return output.toByteArray()
    }

    @Test
    fun aProjectWrittenToDiskReadsBackWithTheSameLayersAndPixels() {
        val original = sampleContent()
        val file = File(folder, "Retrato.${ProjectFileFormat.EXTENSION}")
        file.outputStream().use { writer.write(original, it) }

        val result = file.inputStream().use(reader::read)

        val read = assertIs<ProjectFileReadResult.Read>(result).content
        assertEquals(original.copy(layers = emptyList()), read.copy(layers = emptyList()))
        val noPixels = TileGrid(1, 1)
        assertEquals(original.layers.map { it.copy(pixels = noPixels) }, read.layers.map { it.copy(pixels = noPixels) })
        original.layers.zip(read.layers).forEach { (before, after) ->
            assertEquals(before.pixels.paintedTiles().keys, after.pixels.paintedTiles().keys)
            before.pixels.paintedTiles().forEach { (coordinate, tile) ->
                assertContentEquals(tile.pixels.toByteArray(), after.pixels.tileAt(coordinate)?.pixels?.toByteArray(), "${before.id} tile $coordinate")
            }
        }
    }

    @Test
    fun aFileFromANewerFormatIsRejectedAsNewer() {
        val manifest = """{"formatVersion":2,"appVersion":"9.0","name":"X","widthPx":100,"heightPx":100,"dpi":300,"background":"transparent","tileSize":256,"layers":[],"view":{"centerX":0,"centerY":0,"zoom":1,"rotationDeg":0}}"""
        val file = zipWith(ProjectFileFormat.MIME_TYPE_ENTRY to ProjectFileFormat.MIME_TYPE.toByteArray(), ProjectFileFormat.MANIFEST_ENTRY to manifest.toByteArray())

        val result = reader.read(ByteArrayInputStream(file))

        assertEquals(ProjectFileReadResult.NewerFormat(2), result)
    }

    @Test
    fun aZipFromAnotherAppIsInvalid() {
        val file = zipWith("readme.txt" to "hello".toByteArray())

        val result = reader.read(ByteArrayInputStream(file))

        assertIs<ProjectFileReadResult.Invalid>(result)
    }

    @Test
    fun aFileThatIsNotAZipIsInvalid() {
        val result = reader.read(ByteArrayInputStream("just some text".toByteArray()))

        assertIs<ProjectFileReadResult.Invalid>(result)
    }

    @Test
    fun aFileWithACorruptedByteInsideALayerIsInvalid() {
        val bytes = bytesOf(sampleContent())
        val lineartEntryName = ProjectFileFormat.layerEntryName("layer-2").toByteArray()
        val compressedDataStart = bytes.indexOfSequence(lineartEntryName) + lineartEntryName.size + 40
        val corrupted = bytes.copyOf().also { it[compressedDataStart] = (it[compressedDataStart] + 1).toByte() }

        val result = reader.read(ByteArrayInputStream(corrupted))

        assertIs<ProjectFileReadResult.Invalid>(result)
    }

    @Test
    fun aFileCutShortIsInvalid() {
        val bytes = bytesOf(sampleContent())

        val result = reader.read(ByteArrayInputStream(bytes.copyOf(bytes.size / 2)))

        assertIs<ProjectFileReadResult.Invalid>(result)
    }

    private fun ByteArray.indexOfSequence(sequence: ByteArray): Int =
        (0..size - sequence.size).first { start -> sequence.indices.all { this[start + it] == sequence[it] } }
}
