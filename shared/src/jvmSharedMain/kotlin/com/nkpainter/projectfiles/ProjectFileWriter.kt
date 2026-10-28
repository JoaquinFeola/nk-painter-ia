package com.nkpainter.projectfiles

import com.nkpainter.engine.Tile
import kotlinx.serialization.json.Json
import java.io.OutputStream
import java.util.zip.CRC32
import java.util.zip.Deflater
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/** Packs a project into the .nkpainter ZIP, entries in the order the reader expects them. */
class ProjectFileWriter(private val appVersion: String) {
    private val json = Json { prettyPrint = false }

    /** Writes [content] to [output]; [onLayerWritten] gets the layers done and the total. */
    fun write(content: ProjectFileContent, output: OutputStream, onLayerWritten: (done: Int, total: Int) -> Unit = { _, _ -> }) {
        ZipOutputStream(output).use { zip ->
            zip.setLevel(Deflater.BEST_SPEED)
            writeMimeType(zip)
            zip.putNextEntry(ZipEntry(ProjectFileFormat.MANIFEST_ENTRY))
            zip.write(json.encodeToString(ProjectManifest.serializer(), manifestOf(content)).toByteArray(Charsets.UTF_8))
            zip.closeEntry()
            content.layers.forEachIndexed { index, layer ->
                zip.putNextEntry(ZipEntry(ProjectFileFormat.layerEntryName(layer.id)))
                LayerTilesCodec.write(layer.pixels, zip)
                zip.closeEntry()
                onLayerWritten(index + 1, content.layers.size)
            }
        }
    }

    // Stored uncompressed and first, so the file is recognised without inflating anything.
    private fun writeMimeType(zip: ZipOutputStream) {
        val bytes = ProjectFileFormat.MIME_TYPE.toByteArray(Charsets.US_ASCII)
        val entry = ZipEntry(ProjectFileFormat.MIME_TYPE_ENTRY).apply {
            method = ZipEntry.STORED
            size = bytes.size.toLong()
            compressedSize = bytes.size.toLong()
            crc = CRC32().apply { update(bytes) }.value
        }
        zip.putNextEntry(entry)
        zip.write(bytes)
        zip.closeEntry()
    }

    private fun manifestOf(content: ProjectFileContent) = ProjectManifest(
        formatVersion = ProjectFileFormat.CURRENT_VERSION,
        appVersion = appVersion,
        name = content.name,
        widthPx = content.widthPx,
        heightPx = content.heightPx,
        dpi = content.dpi,
        background = content.background,
        tileSize = Tile.SIZE,
        layers = content.layers.map { layer ->
            ProjectManifest.ManifestLayer(
                id = layer.id,
                name = layer.name,
                opacity = layer.opacity,
                visible = layer.visible,
                clipping = layer.clipping,
                alphaLock = layer.alphaLock,
                tileCount = layer.pixels.paintedTileCount,
                file = ProjectFileFormat.layerEntryName(layer.id),
            )
        },
        view = content.view,
    )
}
