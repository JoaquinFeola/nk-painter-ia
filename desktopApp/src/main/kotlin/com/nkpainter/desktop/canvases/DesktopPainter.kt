package com.nkpainter.desktop.canvases

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.nkpainter.benchmarks.StrokeLatencies
import com.nkpainter.brushes.DrawingState
import com.nkpainter.brushes.Stabilizer
import com.nkpainter.canvases.AreaSize
import com.nkpainter.canvases.Artwork
import com.nkpainter.engine.BrushStroke
import com.nkpainter.engine.StrokePoint
import com.nkpainter.engine.Tile
import com.nkpainter.engine.TileCoordinate
import com.nkpainter.engine.toByteArray
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo

/**
 * Paints each new point of the stroke into the tiles with the active brush as it arrives. Windows
 * has no front buffer path, so the tiles themselves are what the screen shows. Points arrive in
 * pixels of the drawing area and are taken to the canvas through the view. Used only from the UI
 * thread. With `latencyLog`, prints per stroke the time from each input to the frame that drew it.
 */
class DesktopPainter(val artwork: Artwork, private val drawing: DrawingState, private val latencyLog: ((String) -> Unit)? = null) {
    companion object {
        private val tileImageInfo = ImageInfo(Tile.SIZE, Tile.SIZE, ColorType.RGBA_8888, ColorAlphaType.PREMUL)
        private const val NANOS_PER_MILLI = 1_000_000L
    }

    private val grid = artwork.activeLayer.pixels
    private val images = HashMap<TileCoordinate, ImageBitmap>()
    private var stroke: BrushStroke? = null
    private var stabilizer = Stabilizer(0)
    private var lastRealPoint: StrokePoint? = null
    private val latencies = StrokeLatencies()
    private var undrawnInputNanos = 0L

    /** The drawing area as last laid out; the canvas sets it when it draws. */
    var drawingArea = AreaSize(0, 0)

    /** Where the drawing area starts inside the window, in pixels: the editor's bar sits above it. */
    var drawingAreaOrigin = Offset.Zero

    /** A point the Pointer API read in window pixels, in pixels of the drawing area. */
    fun inDrawingArea(windowPoint: StrokePoint): StrokePoint =
        windowPoint.copy(x = windowPoint.x - drawingAreaOrigin.x, y = windowPoint.y - drawingAreaOrigin.y)

    /** Changes whenever a tile image changes, so the canvas that reads it redraws. */
    var version by mutableIntStateOf(0)
        private set

    val tileImages: Map<TileCoordinate, ImageBitmap> get() = images

    init {
        refresh(grid.paintedTiles().keys)
    }

    fun startStroke(screenPoint: StrokePoint, receivedAtNanos: Long = System.nanoTime()) {
        val style = drawing.strokeStyle()
        val started = style.startStroke(grid)
        stroke = started
        stabilizer = style.newStabilizer()
        paint(started, screenPoint, receivedAtNanos)
    }

    fun continueStroke(screenPoint: StrokePoint, receivedAtNanos: Long = System.nanoTime()) {
        val current = stroke ?: return startStroke(screenPoint, receivedAtNanos)
        paint(current, screenPoint, receivedAtNanos)
    }

    fun endStroke() {
        val current = stroke
        val lastReal = lastRealPoint
        if (current != null && lastReal != null) stabilizer.landing(lastReal)?.let { refresh(current.addPoint(it)) }
        // What the tiles were before is history's; until there is one it is dropped.
        current?.finish()
        stroke = null
        lastRealPoint = null
        latencies.summarizeAndReset()?.let { line -> latencyLog?.invoke(line) }
    }

    /** Called by the canvas once it has drawn the tiles. */
    fun frameDrawn() {
        if (undrawnInputNanos == 0L || latencyLog == null) return
        latencies.record((System.nanoTime() - undrawnInputNanos) / NANOS_PER_MILLI)
        undrawnInputNanos = 0L
    }

    private fun paint(current: BrushStroke, screenPoint: StrokePoint, receivedAtNanos: Long) {
        if (undrawnInputNanos == 0L) undrawnInputNanos = receivedAtNanos
        val real = toCanvas(screenPoint)
        lastRealPoint = real
        refresh(current.addPoint(stabilizer.follow(real)))
    }

    private fun toCanvas(screenPoint: StrokePoint): StrokePoint {
        val (x, y) = artwork.viewport.screenToCanvas(screenPoint.x, screenPoint.y, drawingArea)
        return screenPoint.copy(x = x, y = y)
    }

    private fun refresh(changed: Set<TileCoordinate>) {
        if (changed.isEmpty()) return
        changed.forEach { coordinate ->
            val tile = grid.tileAt(coordinate) ?: return@forEach
            images[coordinate] = Image.makeRaster(tileImageInfo, tile.pixels.toByteArray(), Tile.SIZE * Tile.BYTES_PER_PIXEL).toComposeImageBitmap()
        }
        version++
    }
}
