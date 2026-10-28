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

class BrushStrokeTest {
    private val black = PremultipliedColor.opaque(0, 0, 0)
    private val a4Width = 2480
    private val a4Height = 3508
    private val fullPressure = 1f
    private val opaque = 255
    private val transparent = 0

    private fun pixelAt(grid: TileGrid, x: Int, y: Int): List<Int> {
        val tile = grid.tileAt(TileCoordinate(x / Tile.SIZE, y / Tile.SIZE)) ?: return listOf(0, 0, 0, 0)
        val offset = tile.offsetOf(x % Tile.SIZE, y % Tile.SIZE)
        return (0 until Tile.BYTES_PER_PIXEL).map { tile.pixels[offset + it].toInt() and 0xFF }
    }

    private fun alphaAt(grid: TileGrid, x: Int, y: Int): Int = pixelAt(grid, x, y)[3]

    private fun draw(grid: TileGrid, brush: Brush, sizePx: Int, vararg points: Pair<Int, Int>) {
        val stroke = brush.startStroke(grid, black, sizePx, opacityPercent = 100)
        points.forEach { (x, y) -> stroke.addPoint(StrokePoint(x.toFloat(), y.toFloat(), fullPressure)) }
        stroke.finish()
    }

    @Test
    fun theMarkerPaintsTheCentreOfItsLineInTheCurrentColor() {
        val grid = TileGrid(a4Width, a4Height)

        draw(grid, Brush.MARKER, Brush.MARKER.factorySizePx, 100 to 100, 500 to 100)

        assertEquals(listOf(0, 0, 0, opaque), pixelAt(grid, 300, 100))
    }

    @Test
    fun thePixelBrushLeavesEveryPixelOpaqueOrUntouched() {
        val grid = TileGrid(a4Width, a4Height)

        draw(grid, Brush.PIXEL, 1, 10 to 10, 20 to 20)

        val alphas = (5..25).flatMap { y -> (5..25).map { x -> alphaAt(grid, x, y) } }
        assertTrue(alphas.all { it == opaque || it == transparent }, "an in-between tone: ${alphas.filter { it in 1..254 }}")
        assertEquals(opaque, alphaAt(grid, 15, 15))
    }

    @Test
    fun aTapWithoutMovingLeavesADotAsWideAsTheBrush() {
        val grid = TileGrid(a4Width, a4Height)

        draw(grid, Brush.ROUND, 20, 300 to 300)

        val paintedAcross = (270..330).filter { x -> alphaAt(grid, x, 300) > transparent }
        val paintedDown = (270..330).filter { y -> alphaAt(grid, 300, y) > transparent }
        assertEquals(290..309, paintedAcross.first()..paintedAcross.last())
        assertEquals(290..309, paintedDown.first()..paintedDown.last())
    }

    @Test
    fun aStrokeThatLeavesTheCanvasPaintsBothLegsUpToTheEdgeAndNothingBeyond() {
        val grid = TileGrid(a4Width, a4Height)
        val lastColumn = a4Width - 1
        val stroke = Brush.MARKER.startStroke(grid, black, Brush.MARKER.factorySizePx, opacityPercent = 100)

        listOf(2300 to 1000, 2700 to 1000, 2300 to 1200).forEach { (x, y) -> stroke.addPoint(StrokePoint(x.toFloat(), y.toFloat(), fullPressure)) }
        val changes = stroke.finish()

        assertEquals(opaque, alphaAt(grid, lastColumn, 1000), "the way out does not reach the edge")
        assertEquals(opaque, alphaAt(grid, lastColumn, 1110), "the way back does not reach the edge")
        assertEquals(opaque, alphaAt(grid, 2300, 1200))
        val edgeTile = grid.tileAt(TileCoordinate(lastColumn / Tile.SIZE, 1000 / Tile.SIZE))!!
        val firstPixelBeyond = edgeTile.offsetOf(a4Width % Tile.SIZE, 1000 % Tile.SIZE)
        assertEquals(transparent, edgeTile.pixels[firstPixelBeyond + 3].toInt() and 0xFF, "paint was kept beyond the canvas")
        assertEquals(grid.paintedTiles().keys, changes.tilesBefore.keys)
    }

    @Test
    fun aStrokeDoesNotGetDarkerWhereItCrossesItself() {
        val grid = TileGrid(a4Width, a4Height)
        val stroke = Brush.MARKER.startStroke(grid, black, 10, opacityPercent = 50)

        listOf(100 to 100, 300 to 100, 200 to 0, 200 to 200).forEach { (x, y) -> stroke.addPoint(StrokePoint(x.toFloat(), y.toFloat(), fullPressure)) }
        stroke.finish()

        val halfOpaque = 128
        assertEquals(halfOpaque, alphaAt(grid, 150, 100))
        assertEquals(halfOpaque, alphaAt(grid, 200, 100), "the crossing got darker")
    }

    @Test
    fun twoStrokesBuildUpWhereTheyOverlap() {
        val grid = TileGrid(a4Width, a4Height)
        listOf(100, 300).forEach { y ->
            val stroke = Brush.MARKER.startStroke(grid, black, 10, opacityPercent = 50)
            stroke.addPoint(StrokePoint(200f, 100f, fullPressure))
            stroke.addPoint(StrokePoint(200f, y.toFloat() + 100f, fullPressure))
            stroke.finish()
        }

        assertTrue(alphaAt(grid, 200, 150) > alphaAt(grid, 200, 350))
    }

    @Test
    fun aTapWithSize40LeavesADotOf40Pixels() {
        val grid = TileGrid(a4Width, a4Height)

        draw(grid, Brush.ROUND, 40, 300 to 300)

        val paintedAcross = (250..350).filter { x -> alphaAt(grid, x, 300) > transparent }
        assertEquals(40, paintedAcross.size)
    }

    @Test
    fun twoHalfOpaqueStrokesCrossAtThreeQuarters() {
        val grid = TileGrid(a4Width, a4Height)
        listOf((100 to 200) to (300 to 200), (200 to 100) to (200 to 300)).forEach { (from, to) ->
            val stroke = Brush.MARKER.startStroke(grid, black, 10, opacityPercent = 50)
            stroke.addPoint(StrokePoint(from.first.toFloat(), from.second.toFloat(), fullPressure))
            stroke.addPoint(StrokePoint(to.first.toFloat(), to.second.toFloat(), fullPressure))
            stroke.finish()
        }

        // Over white, 75 % black is #404040.
        val threeQuarters = 191
        assertTrue(abs(alphaAt(grid, 200, 200) - threeQuarters) <= 1, "crossing alpha ${alphaAt(grid, 200, 200)}")
    }

    @Test
    fun theAirbrushBuildsUpWhileItKeepsGoingButNeverPastItsOpacity() {
        val grid = TileGrid(a4Width, a4Height)
        val stroke = Brush.AIRBRUSH.startStroke(grid, black, 60, opacityPercent = 60)
        stroke.addPoint(StrokePoint(100f, 300f, fullPressure))
        stroke.addPoint(StrokePoint(500f, 300f, fullPressure))
        val afterFirstPass = alphaAt(grid, 300, 300)

        repeat(9) { pass -> stroke.addPoint(StrokePoint(if (pass % 2 == 0) 100f else 500f, 300f, fullPressure)) }
        stroke.finish()

        val sixtyPercent = 153
        assertTrue(alphaAt(grid, 300, 300) > afterFirstPass, "no build-up: $afterFirstPass then ${alphaAt(grid, 300, 300)}")
        assertTrue(alphaAt(grid, 300, 300) <= sixtyPercent, "past 60 %: ${alphaAt(grid, 300, 300)}")
    }

    @Test
    fun theStrokeHandsBackEachTileAsItWasBefore() {
        val grid = TileGrid(a4Width, a4Height)
        draw(grid, Brush.MARKER, 10, 20 to 20, 60 to 20)
        val before = pixelAt(grid, 40, 20)

        val stroke = Brush.MARKER.startStroke(grid, PremultipliedColor.opaque(255, 0, 0), 10, opacityPercent = 100)
        stroke.addPoint(StrokePoint(40f, 10f, fullPressure))
        stroke.addPoint(StrokePoint(300f, 30f, fullPressure))
        val changes = stroke.finish()

        val firstTile = changes.tilesBefore.getValue(TileCoordinate(0, 0))!!
        val offset = (20 * Tile.SIZE + 40) * Tile.BYTES_PER_PIXEL
        assertEquals(before, (0 until Tile.BYTES_PER_PIXEL).map { firstTile[offset + it].toInt() and 0xFF })
        assertNull(changes.tilesBefore.getValue(TileCoordinate(1, 0)), "a tile the stroke created had nothing before")
    }
}
