package com.nkpainter.projectfiles

import com.nkpainter.engine.TileGrid

/** A layer as the project file carries it: its properties and its painted tiles. */
data class ProjectFileLayer(
    val id: String,
    val name: String,
    val opacity: Int,
    val visible: Boolean,
    val clipping: Boolean,
    val alphaLock: Boolean,
    val pixels: TileGrid,
)
