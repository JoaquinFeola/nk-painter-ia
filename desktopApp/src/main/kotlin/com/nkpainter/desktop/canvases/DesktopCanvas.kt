package com.nkpainter.desktop.canvases

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import com.nkpainter.canvases.AreaSize
import com.nkpainter.canvases.CanvasBackground
import com.nkpainter.canvases.Viewport
import com.nkpainter.engine.StrokePoint
import com.nkpainter.engine.Tile
import com.nkpainter.themes.NkPainterColors

private const val MOUSE_PRESSURE = 1f
private const val CHECKER_SQUARE_PX = 8f
private const val CANVAS_BORDER_PX = 1f
private const val OPAQUE_ALPHA = 0xFF000000

/**
 * Shows the canvas through the view, over its background, and paints with the mouse. Pen and
 * touch arrive from the Pointer API instead. Reading the view here redraws it when it moves.
 * Compose does not clip a Canvas to its bounds: without the clip, a zoomed-in canvas covers the top bar.
 */
@Composable
fun DesktopCanvas(painter: DesktopPainter) {
    val artwork = painter.artwork
    Canvas(
        modifier = Modifier.fillMaxSize().clipToBounds().onGloballyPositioned { painter.drawingAreaOrigin = it.positionInWindow() }.pointerInput(painter) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull() ?: continue
                    if (change.type != PointerType.Mouse) continue
                    val point = StrokePoint(change.position.x, change.position.y, MOUSE_PRESSURE)
                    when (event.type) {
                        PointerEventType.Press -> painter.startStroke(point)
                        PointerEventType.Move -> if (change.pressed) painter.continueStroke(point)
                        PointerEventType.Release -> painter.endStroke()
                    }
                }
            }
        },
    ) {
        painter.drawingArea = AreaSize(size.width.toInt(), size.height.toInt())
        painter.version
        drawRect(NkPainterColors.DrawingArea)
        val canvasSize = Size(artwork.canvasSize.widthPx.toFloat(), artwork.canvasSize.heightPx.toFloat())
        val viewport = artwork.viewport
        withTransform({ throughView(viewport, this@Canvas.size) }) {
            clipRect(0f, 0f, canvasSize.width, canvasSize.height) {
                when (val background = artwork.background) {
                    // Without a size, drawRect takes the composable's, which through the view is not the canvas's.
                    is CanvasBackground.Solid -> drawRect(Color(OPAQUE_ALPHA or background.rgb.toLong()), size = canvasSize)
                    CanvasBackground.Transparent -> withTransform({ backFromView(viewport, this@Canvas.size) }) { drawCheckerboard() }
                }
                // Above 100 % each canvas pixel shows as a square, so the Píxel brush keeps hard edges.
                val filter = if (viewport.zoom > 1f) FilterQuality.None else FilterQuality.Low
                // drawImage smooths the edges of each tile; where a tile edge falls inside a screen pixel the
                // two partial tiles let the background through as a light line (BUG-002 of F-002).
                val tilePaint = Paint().apply {
                    isAntiAlias = false
                    filterQuality = filter
                }
                drawIntoCanvas { canvas ->
                    painter.tileImages.forEach { (coordinate, image) ->
                        val origin = IntOffset(coordinate.column * Tile.SIZE, coordinate.row * Tile.SIZE)
                        canvas.drawImageRect(image, dstOffset = origin, paint = tilePaint)
                    }
                }
            }
            drawRect(NkPainterColors.ControlBorder, size = canvasSize, style = Stroke(width = CANVAS_BORDER_PX / viewport.zoom))
        }
        painter.frameDrawn()
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawTransform.throughView(viewport: Viewport, area: Size) {
    translate(area.width / 2f, area.height / 2f)
    rotate(viewport.rotationDegrees, Offset.Zero)
    scale(viewport.zoom, viewport.zoom, Offset.Zero)
    translate(-viewport.centerX, -viewport.centerY)
}

// The checkerboard stays the same size on screen whatever the zoom, so it is drawn in screen pixels.
private fun androidx.compose.ui.graphics.drawscope.DrawTransform.backFromView(viewport: Viewport, area: Size) {
    translate(viewport.centerX, viewport.centerY)
    scale(1f / viewport.zoom, 1f / viewport.zoom, Offset.Zero)
    rotate(-viewport.rotationDegrees, Offset.Zero)
    translate(-area.width / 2f, -area.height / 2f)
}

private fun DrawScope.drawCheckerboard() {
    drawRect(NkPainterColors.CheckerLight)
    val columns = (size.width / CHECKER_SQUARE_PX).toInt() + 1
    val rows = (size.height / CHECKER_SQUARE_PX).toInt() + 1
    for (row in 0 until rows) {
        for (column in (row % 2) until columns step 2) {
            drawRect(NkPainterColors.CheckerDark, Offset(column * CHECKER_SQUARE_PX, row * CHECKER_SQUARE_PX), Size(CHECKER_SQUARE_PX, CHECKER_SQUARE_PX))
        }
    }
}
