package com.nkpainter.projectfiles

/** Everything a project file carries, layers listed from bottom to top. */
data class ProjectFileContent(
    val name: String,
    val widthPx: Int,
    val heightPx: Int,
    val dpi: Int,
    val background: String,
    val layers: List<ProjectFileLayer>,
    val view: ProjectManifest.ManifestView,
)
