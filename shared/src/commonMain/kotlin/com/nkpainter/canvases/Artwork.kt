package com.nkpainter.canvases

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nkpainter.engine.TileGrid

/** The artwork being drawn: its canvas, background, layers and how it is being looked at. */
class Artwork private constructor(
    val canvasSize: CanvasSize,
    initialBackground: CanvasBackground,
    val layers: List<Layer>,
    val activeLayer: Layer,
    initialViewport: Viewport,
) {
    companion object {
        const val FIRST_LAYER_NAME = "Capa 1"

        /** Opens ready to draw: one empty active layer, and the whole canvas in view. */
        /** Only a request the device accepted becomes an artwork. */
        fun create(accepted: NewArtworkCheck.Ready, drawingArea: AreaSize): Artwork {
            val size = accepted.size
            val firstLayer = Layer(FIRST_LAYER_NAME, TileGrid(size.widthPx, size.heightPx))
            return Artwork(size, accepted.background, listOf(firstLayer), firstLayer, Viewport.fit(size, drawingArea))
        }
    }

    /** Drawn under the layers and never mixed into them, so changing it repaints no tile. */
    var background: CanvasBackground by mutableStateOf(initialBackground)
        private set

    /** Observable, so whatever draws the canvas redraws when the view moves. It never goes through the tiles. */
    var viewport: Viewport by mutableStateOf(initialViewport)
        private set

    /** True until the view is moved; a fitted view is fitted again when the drawing area changes size. */
    var viewIsFitted: Boolean = true
        private set

    /** True when it changed; choosing the background it already has changes nothing. */
    fun changeBackground(to: CanvasBackground): Boolean {
        if (to == background) return false
        background = to
        return true
    }

    fun moveView(to: Viewport) {
        viewport = to
        viewIsFitted = false
    }

    fun fitView(drawingArea: AreaSize) {
        viewport = Viewport.fit(canvasSize, drawingArea)
        viewIsFitted = true
    }
}
