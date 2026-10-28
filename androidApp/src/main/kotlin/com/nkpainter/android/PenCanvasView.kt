package com.nkpainter.android

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.os.SystemClock
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.graphics.lowlatency.CanvasFrontBufferedRenderer
import androidx.graphics.surface.SurfaceControlCompat
import com.nkpainter.canvases.AreaSize
import com.nkpainter.canvases.Artwork
import com.nkpainter.canvases.CanvasBackground
import com.nkpainter.canvases.Finger
import com.nkpainter.canvases.ViewNavigation
import com.nkpainter.canvases.Viewport
import com.nkpainter.brushes.DrawingState
import com.nkpainter.brushes.Stabilizer
import com.nkpainter.engine.BrushTip
import com.nkpainter.engine.PremultipliedColor
import com.nkpainter.engine.RedrawsInFlight
import com.nkpainter.engine.StrokePoint
import com.nkpainter.engine.Tile
import com.nkpainter.engine.TileCoordinate
import java.nio.ByteBuffer

/**
 * Draws the stroke under the pen straight into the front buffer, so it shows before the next
 * frame, and paints it with the active brush into the artwork's active layer when the pen lifts. Both are drawn
 * through the artwork's view. Two fingers move the view instead: the stroke the first finger
 * had started is dropped before it reaches the layer.
 */
@SuppressLint("ViewConstructor")
class PenCanvasView(
    context: Context,
    private val artwork: Artwork,
    private val navigation: ViewNavigation,
    private val drawing: DrawingState,
) : SurfaceView(context) {
    companion object {
        // Roles of Docs/UX/design-system.md; the shared theme holds the same values for Compose.
        private const val DRAWING_AREA_COLOR = 0xFF2B2B2B.toInt()
        private const val CANVAS_BORDER_COLOR = 0xFF767676.toInt()
        private const val CHECKER_LIGHT = 0xFFFFFFFF.toInt()
        private const val CHECKER_DARK = 0xFFD9D9D9.toInt()
        private const val CHECKER_SQUARE_PX = 8
        private const val CANVAS_BORDER_PX = 1f
        private const val OPAQUE_ALPHA = 0xFF000000.toInt()
        private const val MAX_CHANNEL = 255
        private const val FULL_PRESSURE = 1f
    }

    private val renderer = CanvasFrontBufferedRenderer(this, RendererCallback())
    private var navigating = false
    private var strokeStyle = drawing.strokeStyle()
    private var stabilizer = Stabilizer(0)
    private var lastRealPoint: StrokePoint? = null

    // A pinch moves the view every frame; handing each move to the renderer stalled it for good.
    private val redraws = RedrawsInFlight { post { renderer.renderMultiBufferedLayer(emptyList()) } }

    // Added after the renderer's own callback, so it runs once the renderer has the new surface. A
    // redraw asked for before the surface existed never finishes, so a new surface starts afresh.
    init {
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) = Unit

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                redraws.forget()
                redraws.ask()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) = redraws.forget()
        })
    }

    // Read on the renderer's thread; the drawing follows what the editor last handed over.
    @Volatile private var shownViewport = artwork.viewport
    @Volatile private var shownBackground = artwork.background

    /** Redraws the canvas after the view moved or the background changed; no tile is repainted. */
    fun show(viewport: Viewport, background: CanvasBackground) {
        shownViewport = viewport
        shownBackground = background
        if (width > 0) redraws.ask()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                strokeStyle = drawing.strokeStyle()
                stabilizer = strokeStyle.newStabilizer()
                renderer.renderFrontBufferedLayer(sampleOf(event, startsStroke = true))
            }
            MotionEvent.ACTION_POINTER_DOWN -> when {
                navigating -> navigation.press(fingerAt(event, event.actionIndex))
                allFingers(event) -> startNavigating(event)
            }
            MotionEvent.ACTION_MOVE -> if (navigating) navigation.move(fingersOf(event), AreaSize(width, height)) else renderMoves(event)
            MotionEvent.ACTION_POINTER_UP -> if (navigating) navigation.release(event.getPointerId(event.actionIndex))
            MotionEvent.ACTION_UP -> if (navigating) stopNavigating() else commitStroke(event)
            MotionEvent.ACTION_CANCEL -> if (navigating) stopNavigating() else renderer.cancel()
        }
        return true
    }

    // A pen with a hand resting beside it is not a view gesture; telling palms apart is F-009's.
    private fun allFingers(event: MotionEvent): Boolean =
        (0 until event.pointerCount).all { event.getToolType(it) == MotionEvent.TOOL_TYPE_FINGER }

    private fun startNavigating(event: MotionEvent) {
        renderer.cancel()
        navigating = true
        for (index in 0 until event.pointerCount) navigation.press(fingerAt(event, index))
    }

    private fun commitStroke(event: MotionEvent) {
        lastRealPoint?.let(stabilizer::landing)?.let { landing ->
            renderer.renderFrontBufferedLayer(WetStrokeSample(landing, startsStroke = false, event.eventTime, strokeStyle))
        }
        lastRealPoint = null
        redraws.startedElsewhere()
        renderer.commit()
    }

    private fun stopNavigating() {
        navigation.releaseAll()
        navigating = false
    }

    private fun fingerAt(event: MotionEvent, index: Int) = Finger(event.getPointerId(index), event.getX(index), event.getY(index))

    private fun fingersOf(event: MotionEvent): List<Finger> = (0 until event.pointerCount).map { fingerAt(event, it) }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        // Shows the canvas before the first stroke, and again after Android recreates the view.
        redraws.ask()
    }

    override fun onDetachedFromWindow() {
        renderer.release(true)
        super.onDetachedFromWindow()
    }

    private fun renderMoves(event: MotionEvent) {
        for (index in 0 until event.historySize) {
            val pressure = if (reportsPressure(event)) event.getHistoricalPressure(index) else FULL_PRESSURE
            val point = stabilized(toCanvas(event.getHistoricalX(index), event.getHistoricalY(index), pressure))
            renderer.renderFrontBufferedLayer(WetStrokeSample(point, startsStroke = false, event.getHistoricalEventTime(index), strokeStyle))
        }
        renderer.renderFrontBufferedLayer(sampleOf(event, startsStroke = false))
    }

    private fun sampleOf(event: MotionEvent, startsStroke: Boolean): WetStrokeSample {
        val pressure = if (reportsPressure(event)) event.pressure else FULL_PRESSURE
        return WetStrokeSample(stabilized(toCanvas(event.x, event.y, pressure)), startsStroke, event.eventTime, strokeStyle)
    }

    // A finger reports its contact area as pressure and a mouse reports nothing useful; both draw at
    // full pressure, as does a pen whose device has no pressure axis.
    private fun reportsPressure(event: MotionEvent): Boolean {
        val tool = event.getToolType(0)
        val isPen = tool == MotionEvent.TOOL_TYPE_STYLUS || tool == MotionEvent.TOOL_TYPE_ERASER
        return isPen && event.device?.getMotionRange(MotionEvent.AXIS_PRESSURE, event.source) != null
    }

    private fun stabilized(real: StrokePoint): StrokePoint {
        lastRealPoint = real
        return stabilizer.follow(real)
    }

    private fun toCanvas(x: Float, y: Float, pressure: Float): StrokePoint {
        val (canvasX, canvasY) = artwork.viewport.screenToCanvas(x, y, AreaSize(width, height))
        return StrokePoint(canvasX, canvasY, pressure.coerceIn(0f, 1f))
    }

    private fun viewMatrix(): Matrix {
        val viewport = shownViewport
        return Matrix().apply {
            setTranslate(-viewport.centerX, -viewport.centerY)
            postScale(viewport.zoom, viewport.zoom)
            postRotate(viewport.rotationDegrees)
            postTranslate(width / 2f, height / 2f)
        }
    }

    /** Runs on the renderer's own thread; it is the only code that touches the tiles. */
    private inner class RendererCallback : CanvasFrontBufferedRenderer.Callback<WetStrokeSample> {
        private val wetPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
        private val tilePaint = Paint()
        private val borderPaint = Paint().apply {
            color = CANVAS_BORDER_COLOR
            style = Paint.Style.STROKE
        }
        private val backgroundPaint = Paint()
        private val checker by lazy { checkerTile() }
        private val tileBitmaps = HashMap<TileCoordinate, Bitmap>()
        private val layer = artwork.activeLayer.pixels
        private val tileBytes = ByteArray(Tile.BYTE_COUNT)
        private var previousWetPoint: StrokePoint? = null
        private val latency = StrokeLatencyRecorder()
        private var lastRenderedEventTimeMs = 0L

        override fun onDrawFrontBufferedLayer(canvas: Canvas, bufferWidth: Int, bufferHeight: Int, param: WetStrokeSample) {
            val point = param.point
            val from = if (param.startsStroke) point else previousWetPoint ?: point
            // Only an outline of the brush: the real dabs replace it when the pen lifts.
            val style = param.style
            val dab = style.dabAt(point.pressure)
            val color = style.color
            // Erasing reveals what is under the only layer there is until F-003: the background.
            wetPaint.color = if (style.erases) erasedLook() else straightColor(color)
            wetPaint.alpha = if (style.erases) dab.opacity else dab.opacity * color.alpha / MAX_CHANNEL
            wetPaint.strokeWidth = dab.diameterPx
            wetPaint.isAntiAlias = style.brush.tip != BrushTip.SQUARE
            wetPaint.strokeCap = if (style.brush.tip == BrushTip.SQUARE) Paint.Cap.SQUARE else Paint.Cap.ROUND
            canvas.save()
            canvas.concat(viewMatrix())
            canvas.drawLine(from.x, from.y, point.x, point.y, wetPaint)
            canvas.restore()
            previousWetPoint = point
            lastRenderedEventTimeMs = param.eventTimeMs
        }

        override fun onFrontBufferedLayerRenderComplete(frontBufferedLayerSurfaceControl: SurfaceControlCompat, transaction: SurfaceControlCompat.Transaction) {
            latency.record(SystemClock.uptimeMillis() - lastRenderedEventTimeMs)
        }

        override fun onMultiBufferedLayerRenderComplete(
            frontBufferedLayerSurfaceControl: SurfaceControlCompat,
            multiBufferedLayerSurfaceControl: SurfaceControlCompat,
            transaction: SurfaceControlCompat.Transaction,
        ) {
            redraws.finished()
        }

        override fun onDrawMultiBufferedLayer(canvas: Canvas, bufferWidth: Int, bufferHeight: Int, params: Collection<WetStrokeSample>) {
            latency.logAndReset()
            params.firstOrNull()?.let { first ->
                val stroke = first.style.startStroke(layer)
                val changed = params.flatMapTo(HashSet()) { stroke.addPoint(it.point) }
                // What the tiles were before is history's; until there is one it is dropped.
                stroke.finish()
                changed.forEach(::refreshBitmap)
            }
            layer.paintedTiles().keys.filter { it !in tileBitmaps }.forEach(::refreshBitmap)

            val view = viewMatrix()
            val canvasWidth = artwork.canvasSize.widthPx.toFloat()
            val canvasHeight = artwork.canvasSize.heightPx.toFloat()
            canvas.drawColor(DRAWING_AREA_COLOR)
            canvas.save()
            canvas.concat(view)
            drawBackground(canvas, view, canvasWidth, canvasHeight)
            canvas.clipRect(0f, 0f, canvasWidth, canvasHeight)
            // Above 100 % each canvas pixel shows as a square, so the Píxel brush keeps hard edges.
            tilePaint.isFilterBitmap = shownViewport.zoom < 1f
            tileBitmaps.forEach { (coordinate, bitmap) ->
                canvas.drawBitmap(bitmap, (coordinate.column * Tile.SIZE).toFloat(), (coordinate.row * Tile.SIZE).toFloat(), tilePaint)
            }
            canvas.restore()
            canvas.save()
            canvas.concat(view)
            borderPaint.strokeWidth = CANVAS_BORDER_PX / shownViewport.zoom
            canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, borderPaint)
            canvas.restore()
        }

        private fun erasedLook(): Int = when (val background = shownBackground) {
            is CanvasBackground.Solid -> background.rgb or OPAQUE_ALPHA
            CanvasBackground.Transparent -> CHECKER_LIGHT
        }

        // Paint takes its color unpremultiplied.
        private fun straightColor(color: PremultipliedColor): Int {
            if (color.alpha == 0) return Color.TRANSPARENT
            return Color.rgb(color.red * MAX_CHANNEL / color.alpha, color.green * MAX_CHANNEL / color.alpha, color.blue * MAX_CHANNEL / color.alpha)
        }

        private fun drawBackground(canvas: Canvas, view: Matrix, canvasWidth: Float, canvasHeight: Float) {
            when (val background = shownBackground) {
                is CanvasBackground.Solid -> backgroundPaint.apply {
                    shader = null
                    color = background.rgb or OPAQUE_ALPHA
                }
                // The checkerboard stays the same size on screen whatever the zoom.
                CanvasBackground.Transparent -> backgroundPaint.apply {
                    shader = BitmapShader(checker, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT).apply {
                        setLocalMatrix(Matrix().also(view::invert))
                    }
                }
            }
            canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, backgroundPaint)
        }

        private fun checkerTile(): Bitmap {
            val side = CHECKER_SQUARE_PX * 2
            return Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888).apply {
                eraseColor(CHECKER_LIGHT)
                val square = Bitmap.createBitmap(CHECKER_SQUARE_PX, CHECKER_SQUARE_PX, Bitmap.Config.ARGB_8888).apply { eraseColor(CHECKER_DARK) }
                Canvas(this).apply {
                    drawBitmap(square, CHECKER_SQUARE_PX.toFloat(), 0f, null)
                    drawBitmap(square, 0f, CHECKER_SQUARE_PX.toFloat(), null)
                }
            }
        }

        private fun refreshBitmap(coordinate: TileCoordinate) {
            val tile = layer.tileAt(coordinate) ?: return
            val bitmap = tileBitmaps.getOrPut(coordinate) { Bitmap.createBitmap(Tile.SIZE, Tile.SIZE, Bitmap.Config.ARGB_8888) }
            tile.pixels.copyInto(tileBytes)
            // ARGB_8888 keeps its pixels premultiplied and in RGBA byte order, the same layout as a tile.
            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(tileBytes))
        }
    }
}
