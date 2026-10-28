package com.nkpainter.projectfiles

import kotlinx.serialization.Serializable

/** manifest.json: everything in a project file that is not pixels. */
@Serializable
data class ProjectManifest(
    val formatVersion: Int,
    val appVersion: String,
    val name: String,
    val widthPx: Int,
    val heightPx: Int,
    val dpi: Int,
    val background: String,
    val tileSize: Int,
    val layers: List<ManifestLayer>,
    val view: ManifestView,
) {
    @Serializable
    data class ManifestLayer(
        val id: String,
        val name: String,
        val opacity: Int,
        val visible: Boolean,
        val clipping: Boolean,
        val alphaLock: Boolean,
        val tileCount: Int,
        val file: String,
    )

    @Serializable
    data class ManifestView(val centerX: Float, val centerY: Float, val zoom: Float, val rotationDeg: Float)
}
