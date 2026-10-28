package com.nkpainter.desktop.canvases

import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.use
import com.nkpainter.brushes.DrawingState
import com.nkpainter.canvases.AreaSize
import com.nkpainter.canvases.Artwork
import com.nkpainter.canvases.CanvasBackground
import com.nkpainter.canvases.DeviceCanvasLimit
import com.nkpainter.canvases.LengthUnit
import com.nkpainter.canvases.NewArtworkCheck
import com.nkpainter.canvases.NewArtworkRequest
import org.jetbrains.skia.Bitmap
import kotlin.test.Test
import kotlin.test.assertEquals

class DesktopCanvasBackgroundTest {
    private val area = AreaSize(600, 400)
    private val roomy = DeviceCanvasLimit(warningPixels = 200_000_000, redLinePixels = 250_000_000)

    private fun render(background: CanvasBackground): Bitmap {
        val request = NewArtworkRequest(2480.0, 3508.0, LengthUnit.PIXELS, 300.0, background = background)
        val artwork = Artwork.create(request.check(roomy) as NewArtworkCheck.Ready, area)
        return ImageComposeScene(width = area.widthPx, height = area.heightPx).use { scene ->
            scene.setContent { DesktopCanvas(DesktopPainter(artwork, DrawingState(FactoryDrawingSettings()))) }
            Bitmap.makeFromImage(scene.render())
        }
    }

    @Test
    fun aSolidBackgroundCoversTheWholeCanvas() {
        val pixels = render(CanvasBackground.Solid(0xF5E6C8))
        // The A4 is fitted to the height, so its bottom right corner sits near the bottom of the area, right of center.
        val nearBottomRight = pixels.getColor(area.widthPx / 2 + 120, area.heightPx - 10)
        val nearTopLeft = pixels.getColor(area.widthPx / 2 - 120, 10)

        assertEquals(0xFFF5E6C8.toInt(), nearBottomRight)
        assertEquals(0xFFF5E6C8.toInt(), nearTopLeft)
    }
}
