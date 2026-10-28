package com.nkpainter.projectfiles

import com.nkpainter.engine.Tile
import com.nkpainter.engine.TileGrid
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Reads a .nkpainter file in one pass. The manifest comes before the pixels, so the version and
 * the shape of the file are checked before any tile is inflated. The ZIP stream checks each
 * entry's CRC when the entry has been read to its end.
 */
class ProjectFileReader {
    companion object {
        private const val MIN_SIDE_PX = 16
        private val dpiRange = 72..600
        private val opacityRange = 0..100
    }

    private val json = Json { ignoreUnknownKeys = true }

    fun read(input: InputStream): ProjectFileReadResult = try {
        ZipInputStream(BufferedInputStream(input)).use(::readZip)
    } catch (damaged: IOException) {
        ProjectFileReadResult.Invalid("damaged file: ${damaged.message}")
    } catch (badManifest: SerializationException) {
        ProjectFileReadResult.Invalid("unreadable manifest: ${badManifest.message}")
    } catch (badManifest: IllegalArgumentException) {
        ProjectFileReadResult.Invalid("unreadable manifest: ${badManifest.message}")
    }

    private fun readZip(zip: ZipInputStream): ProjectFileReadResult {
        val first = zip.nextEntry ?: return ProjectFileReadResult.Invalid("not a ZIP file or empty")
        if (first.name != ProjectFileFormat.MIME_TYPE_ENTRY) return ProjectFileReadResult.Invalid("first entry is not mimetype")
        if (zip.readBytes().toString(Charsets.US_ASCII) != ProjectFileFormat.MIME_TYPE) return ProjectFileReadResult.Invalid("not an nk-painter project")

        val second = zip.nextEntry ?: return ProjectFileReadResult.Invalid("manifest missing")
        if (second.name != ProjectFileFormat.MANIFEST_ENTRY) return ProjectFileReadResult.Invalid("manifest missing")
        val manifest = json.decodeFromString(ProjectManifest.serializer(), zip.readBytes().toString(Charsets.UTF_8))
        if (manifest.formatVersion > ProjectFileFormat.CURRENT_VERSION) return ProjectFileReadResult.NewerFormat(manifest.formatVersion)
        problemIn(manifest)?.let { return ProjectFileReadResult.Invalid(it) }

        return readLayers(zip, manifest)
    }

    private fun problemIn(manifest: ProjectManifest): String? = when {
        manifest.formatVersion < 1 -> "format version ${manifest.formatVersion}"
        manifest.widthPx < MIN_SIDE_PX || manifest.heightPx < MIN_SIDE_PX -> "canvas smaller than $MIN_SIDE_PX px"
        manifest.dpi !in dpiRange -> "dpi ${manifest.dpi} outside $dpiRange"
        manifest.tileSize != Tile.SIZE -> "tile size ${manifest.tileSize}"
        manifest.layers.isEmpty() -> "no layers"
        manifest.layers.any { it.opacity !in opacityRange } -> "layer opacity outside $opacityRange"
        manifest.layers.any { it.file != ProjectFileFormat.layerEntryName(it.id) } -> "layer file name does not match its id"
        manifest.layers.map { it.id }.toSet().size != manifest.layers.size -> "two layers share an id"
        else -> null
    }

    private fun readLayers(zip: ZipInputStream, manifest: ProjectManifest): ProjectFileReadResult {
        val expected = manifest.layers.associateBy { it.file }
        val grids = HashMap<String, TileGrid>()
        var entry = zip.nextEntry
        while (entry != null) {
            val layer = expected[entry.name]
            if (layer != null) {
                val grid = TileGrid(manifest.widthPx, manifest.heightPx)
                LayerTilesCodec.read(zip, grid, layer.tileCount)?.let { return ProjectFileReadResult.Invalid("${layer.file}: $it") }
                grids[layer.file] = grid
            }
            entry = zip.nextEntry
        }
        val missing = expected.keys - grids.keys
        if (missing.isNotEmpty()) return ProjectFileReadResult.Invalid("missing layer files: $missing")

        return ProjectFileReadResult.Read(contentOf(manifest, grids))
    }

    private fun contentOf(manifest: ProjectManifest, grids: Map<String, TileGrid>) = ProjectFileContent(
        name = manifest.name,
        widthPx = manifest.widthPx,
        heightPx = manifest.heightPx,
        dpi = manifest.dpi,
        background = manifest.background,
        layers = manifest.layers.map { layer ->
            ProjectFileLayer(layer.id, layer.name, layer.opacity, layer.visible, layer.clipping, layer.alphaLock, grids.getValue(layer.file))
        },
        view = manifest.view,
    )
}
