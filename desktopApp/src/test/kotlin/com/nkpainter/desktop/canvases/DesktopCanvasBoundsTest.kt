package com.nkpainter.desktop.canvases

import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.use
import com.nkpainter.brushes.DrawingState
import com.nkpainter.canvases.AreaSize
import com.nkpainter.canvases.Artwork
import com.nkpainter.canvases.CanvasBackground
import com.nkpainter.canvases.DeviceCanvasLimit
import com.nkpainter.canvases.EditorScreen
import com.nkpainter.canvases.LengthUnit
import com.nkpainter.canvases.NewArtworkCheck
import com.nkpainter.canvases.NewArtworkRequest
import com.nkpainter.canvases.ViewNavigation
import com.nkpainter.themes.NkPainterTheme
import org.jetbrains.skia.Bitmap
import kotlin.test.Test
import kotlin.test.assertNotEquals

class DesktopCanvasBoundsTest {
    private val window = AreaSize(600, 400)
    private val roomy = DeviceCanvasLimit(warningPixels = 200_000_000, redLinePixels = 250_000_000)
    private val canvasColor = 0xF5E6C8

    @Test
    fun aZoomedInCanvasLeavesTheTopBarVisible() {
        val request = NewArtworkRequest(2480.0, 3508.0, LengthUnit.PIXELS, 300.0, background = CanvasBackground.Solid(canvasColor))
        val artwork = Artwork.create(request.check(roomy) as NewArtworkCheck.Ready, window)
        artwork.moveView(artwork.viewport.copy(zoom = 1f))

        val pixels = ImageComposeScene(width = window.widthPx, height = window.heightPx).use { scene ->
            scene.setContent {
                NkPainterTheme {
                    val drawing = DrawingState(FactoryDrawingSettings())
                    EditorScreen(artwork, ViewNavigation(artwork), drawing) { DesktopCanvas(DesktopPainter(artwork, drawing)) }
                }
            }
            scene.render()
            Bitmap.makeFromImage(scene.render())
        }

        val topBarCorner = pixels.getColor(window.widthPx - 5, 5)
        assertNotEquals(0xFF000000.toInt() or canvasColor, topBarCorner, "the canvas was drawn over the top bar")
    }
}
