package com.nkpainter.brushes

import com.nkpainter.engine.PremultipliedColor
import com.nkpainter.engine.StrokePoint
import com.nkpainter.engine.Tile
import com.nkpainter.engine.TileCoordinate
import com.nkpainter.engine.TileGrid
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EraserTest {
    private class MemoryStore(var saved: DrawingSettings? = null) : DrawingSettingsStore {
        override fun load(): DrawingSettings = saved ?: DrawingSettings.FACTORY

        override fun save(settings: DrawingSettings) {
            saved = settings
        }
    }

    private val black = PremultipliedColor.opaque(0, 0, 0)
    private val fullPressure = 1f
    private val halfPressure = 0.5f
    private val opaque = 255
    private val transparent = 0
    private val half = 128

    private fun alphaAt(grid: TileGrid, x: Int, y: Int): Int {
        val tile = grid.tileAt(TileCoordinate(x / Tile.SIZE, y / Tile.SIZE)) ?: return 0
        return tile.pixels[tile.offsetOf(x % Tile.SIZE, y % Tile.SIZE) + 3].toInt() and 0xFF
    }

    // A 200×200 px block of opaque black from (100, 100), painted with a wide marker.
    private fun blackBlock(): TileGrid {
        val grid = TileGrid(1200, 600)
        val stroke = Brush.MARKER.startStroke(grid, black, 200, opacityPercent = 100)
        stroke.addPoint(StrokePoint(200f, 200f, fullPressure))
        stroke.finish()
        return grid
    }

    private fun erase(grid: TileGrid, style: StrokeStyle, vararg points: StrokePoint) {
        val stroke = style.startStroke(grid)
        points.forEach(stroke::addPoint)
        stroke.finish()
    }

    private fun eraserStyle(shape: Brush, sizePx: Int, opacityPercent: Int, curve: PressureCurve = PressureCurve.NORMAL) =
        StrokeStyle(shape, black, sizePx, opacityPercent, curve, erases = true)

    // Back and forth ten times across the block's middle row, so a building-up shape reaches its ceiling.
    private fun backAndForth(pressure: Float): Array<StrokePoint> =
        Array(11) { pass -> StrokePoint(if (pass % 2 == 0) 120f else 280f, 200f, pressure) }

    @Test
    fun theFirstTimeTheEraserHasThePencilRedondoShape() {
        val drawing = DrawingState(MemoryStore())

        drawing.use(Tool.ERASER)

        assertEquals(Brush.ROUND, drawing.strokeStyle().brush)
        assertTrue(drawing.strokeStyle().erases)
    }

    @Test
    fun aPixelShapedEraserLeavesEachPixelWholeOrGone() {
        val grid = blackBlock()

        erase(grid, eraserStyle(Brush.PIXEL, 1, 100), StrokePoint(110f, 110f, fullPressure), StrokePoint(120f, 120f, fullPressure))

        val alphas = (105..125).flatMap { y -> (105..125).map { x -> alphaAt(grid, x, y) } }
        assertTrue(alphas.all { it == opaque || it == transparent }, "half erased: ${alphas.filter { it in 1..254 }}")
        assertEquals(transparent, alphaAt(grid, 115, 115))
    }

    @Test
    fun theEraserHasItsOwnSizeAndOpacity() {
        val drawing = DrawingState(MemoryStore())
        drawing.choose(Brush.ROUND)
        drawing.adjustSize(10)
        drawing.use(Tool.ERASER)
        drawing.adjustSize(30)
        drawing.adjustOpacity(50)
        val grid = blackBlock()

        erase(grid, drawing.strokeStyle(), StrokePoint(200f, 200f, fullPressure))

        val erasedAcross = (150..250).filter { x -> alphaAt(grid, x, 200) < opaque }
        assertEquals(30, erasedAcross.size)
        assertTrue(abs(alphaAt(grid, 200, 200) - half) <= 1, "left ${alphaAt(grid, 200, 200)}")
    }

    @Test
    fun theEraserSettingsComeBackOnReopening() {
        val store = MemoryStore()
        val drawing = DrawingState(store)
        drawing.use(Tool.ERASER)
        drawing.chooseEraserShape(Brush.AIRBRUSH)
        drawing.adjustSize(80)
        drawing.adjustOpacity(60)
        drawing.adjustStabilizer(2)
        drawing.saveAdjustments()

        val reopened = DrawingState(store).apply { use(Tool.ERASER) }

        assertEquals(Brush.AIRBRUSH, reopened.eraserShape)
        assertEquals(BrushSettings(80, 60, 2), reopened.activeSettings)
    }

    @Test
    fun adjustingTheEraserLeavesTheBrushAsItWas() {
        val drawing = DrawingState(MemoryStore())
        val brushBefore = drawing.activeSettings
        drawing.use(Tool.ERASER)

        drawing.adjustSize(90)

        drawing.use(Tool.BRUSH)
        assertEquals(brushBefore, drawing.activeSettings)
    }

    @Test
    fun anAirbrushShapedEraserAtHalfPressureLeavesHalfThePaint() {
        val grid = blackBlock()

        erase(grid, eraserStyle(Brush.AIRBRUSH, 60, 100), *backAndForth(halfPressure))

        assertTrue(abs(alphaAt(grid, 200, 200) - half) <= 2, "left ${alphaAt(grid, 200, 200)}")
    }

    @Test
    fun aMarkerShapedEraserTakesEverythingAtAnyPressure() {
        val grid = blackBlock()

        erase(grid, eraserStyle(Brush.MARKER, 20, 100), StrokePoint(150f, 200f, 0.3f), StrokePoint(250f, 200f, 0.3f))

        assertEquals(transparent, alphaAt(grid, 200, 200))
    }

    @Test
    fun withTheFirmCurveTheAirbrushShapedEraserLeavesMoreThanHalf() {
        val grid = blackBlock()

        erase(grid, eraserStyle(Brush.AIRBRUSH, 60, 100, PressureCurve.FIRM), *backAndForth(halfPressure))

        assertTrue(alphaAt(grid, 200, 200) > half, "left ${alphaAt(grid, 200, 200)}")
    }

    @Test
    fun erasingALineLeavesTheLayerClearSoTheBackgroundShows() {
        val grid = blackBlock()

        erase(grid, eraserStyle(Brush.ROUND, 40, 100), StrokePoint(200f, 200f, fullPressure))

        assertEquals(transparent, alphaAt(grid, 200, 200))
    }

    @Test
    fun erasingWhereThereIsNoPaintLeavesNothing() {
        val grid = TileGrid(1200, 600)

        erase(grid, eraserStyle(Brush.ROUND, 40, 100), StrokePoint(600f, 300f, fullPressure))

        assertEquals(transparent, alphaAt(grid, 600, 300))
    }
}

class ToolTapTest {
    private class MemoryStore : DrawingSettingsStore {
        override fun load(): DrawingSettings = DrawingSettings.FACTORY

        override fun save(settings: DrawingSettings) = Unit
    }

    @Test
    fun theAppOpensWithTheBrush() {
        assertEquals(Tool.BRUSH, DrawingState(MemoryStore()).activeTool)
    }

    @Test
    fun tappingAToolThatIsNotActiveMakesItActiveWithoutOpeningItsPanel() {
        val drawing = DrawingState(MemoryStore())

        val panel = tapTool(drawing, Tool.ERASER, openPanel = null)

        assertEquals(Tool.ERASER, drawing.activeTool)
        assertNull(panel)
    }

    @Test
    fun tappingTheActiveToolOpensItsPanel() {
        val drawing = DrawingState(MemoryStore())

        val panel = tapTool(drawing, Tool.BRUSH, openPanel = null)

        assertEquals(Tool.BRUSH, panel)
    }

    @Test
    fun tappingTheActiveToolAgainClosesItsPanel() {
        val drawing = DrawingState(MemoryStore())

        val panel = tapTool(drawing, Tool.BRUSH, openPanel = Tool.BRUSH)

        assertNull(panel)
    }
}
