package com.nkpainter.desktop.canvases

import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.use
import com.nkpainter.brushes.Brush
import com.nkpainter.brushes.DrawingSettings
import com.nkpainter.brushes.DrawingSettingsStore
import com.nkpainter.brushes.DrawingState
import com.nkpainter.canvases.AreaSize
import com.nkpainter.canvases.Artwork
import com.nkpainter.canvases.CanvasBackground
import com.nkpainter.canvases.DeviceCanvasLimit
import com.nkpainter.canvases.LengthUnit
import com.nkpainter.canvases.NewArtworkCheck
import com.nkpainter.canvases.NewArtworkRequest
import com.nkpainter.engine.StrokePoint
import com.nkpainter.engine.Tile
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Color
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertTrue

class DesktopCanvasTileSeamTest {
    // A 1400 px canvas fitted to a 900 px area shows at 64 %, so the tile edge falls between screen pixels.
    private val area = AreaSize(900, 900)
    private val canvasSidePx = 1400.0
    private val roomy = DeviceCanvasLimit(warningPixels = 200_000_000, redLinePixels = 250_000_000)
    private val white = 0xFFFFFF
    private val strokeCanvasY = 700f
    private val inkTolerance = 8

    @Test
    fun aStrokeAcrossATileEdgeShowsNoLightLineWhenZoomedOut() {
        val request = NewArtworkRequest(canvasSidePx, canvasSidePx, LengthUnit.PIXELS, 300.0, background = CanvasBackground.Solid(white))
        val artwork = Artwork.create(request.check(roomy) as NewArtworkCheck.Ready, area)
        val drawing = DrawingState(object : DrawingSettingsStore {
            override fun load() = DrawingSettings.FACTORY.copy(activeBrush = Brush.MARKER)
            override fun save(settings: DrawingSettings) = Unit
        })
        val painter = DesktopPainter(artwork, drawing).apply { drawingArea = area }
        val zoom = artwork.viewport.zoom
        fun onScreen(canvasX: Float) = area.widthPx / 2f + (canvasX - artwork.viewport.centerX) * zoom
        val screenY = area.heightPx / 2f + (strokeCanvasY - artwork.viewport.centerY) * zoom
        painter.startStroke(StrokePoint(onScreen(Tile.SIZE - 100f), screenY, 1f))
        painter.continueStroke(StrokePoint(onScreen(Tile.SIZE + 100f), screenY, 1f))
        painter.endStroke()

        val pixels = ImageComposeScene(width = area.widthPx, height = area.heightPx).use { scene ->
            scene.setContent { DesktopCanvas(painter) }
            Bitmap.makeFromImage(scene.render())
        }

        val row = screenY.roundToInt()
        val tileEdgeColumn = onScreen(Tile.SIZE.toFloat()).toInt()
        val brightnessAcrossTheEdge = (tileEdgeColumn - 3..tileEdgeColumn + 3).associateWith { Color.getR(pixels.getColor(it, row)) }
        assertTrue(brightnessAcrossTheEdge.values.all { it <= inkTolerance }, "light line where the stroke crosses the tile edge: $brightnessAcrossTheEdge")
    }
}
