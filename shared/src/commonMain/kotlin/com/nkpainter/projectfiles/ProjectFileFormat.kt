package com.nkpainter.projectfiles

/** The constants of the project file contract, Docs/Architecture/project-file-format.md. */
object ProjectFileFormat {
    const val CURRENT_VERSION = 1
    const val EXTENSION = "nkpainter"
    const val MIME_TYPE = "application/vnd.nkpainter.project"
    const val MIME_TYPE_ENTRY = "mimetype"
    const val MANIFEST_ENTRY = "manifest.json"
    const val LAYERS_FOLDER = "layers/"
    const val LAYER_FILE_SUFFIX = ".tiles"
    const val TRANSPARENT_BACKGROUND = "transparent"

    /** "NKTL" in ASCII, the first four bytes of every layer file. */
    val LAYER_SIGNATURE = byteArrayOf(0x4E, 0x4B, 0x54, 0x4C)

    fun layerEntryName(layerId: String): String = LAYERS_FOLDER + layerId + LAYER_FILE_SUFFIX
}
