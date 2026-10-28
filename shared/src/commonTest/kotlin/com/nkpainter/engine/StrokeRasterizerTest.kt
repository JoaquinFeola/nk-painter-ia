package com.nkpainter.engine

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class StrokeRasterizerTest {
    private val black = PremultipliedColor.opaque(0, 0, 0)
    private val canvasWidth = 1000
    private val canvasHeight = 800
    private val fullPressure = 1f
    private val opaque = 255
    private val transparent = 0

    private fun alphaAt(grid: TileGrid, x: Int, y: Int): Int {
        val tile = grid.tileAt(TileCoordinate(x / Tile.SIZE, y / Tile.SIZE)) ?: return transparent
        return tile.pixels[tile.offsetOf(x % Tile.SIZE, y % Tile.SIZE) + 3].toInt() and 0xFF
    }

    @Test
    fun aStrokeInsideOneTileCreatesOnlyThatTile() {
        val grid = TileGrid(canvasWidth, canvasHeight)
        val rasterizer = StrokeRasterizer(RoundBrush(10f, black))

        rasterizer.paint(grid, listOf(StrokePoint(20f, 20f, fullPressure), StrokePoint(60f, 20f, fullPressure)))

        assertEquals(setOf(TileCoordinate(0, 0)), grid.paintedTiles().keys)
    }

    @Test
    fun aStrokeAcrossATileBorderPaintsBothTiles() {
        val grid = TileGrid(canvasWidth, canvasHeight)
        val rasterizer = StrokeRasterizer(RoundBrush(10f, black))

        val changed = rasterizer.paint(grid, listOf(StrokePoint(240f, 20f, fullPressure), StrokePoint(280f, 20f, fullPressure)))

        assertEquals(setOf(TileCoordinate(0, 0), TileCoordinate(1, 0)), changed)
        assertEquals(opaque, alphaAt(grid, 250, 20))
        assertEquals(opaque, alphaAt(grid, 270, 20))
    }

    @Test
    fun lowerPressurePaintsAThinnerLine() {
        val grid = TileGrid(canvasWidth, canvasHeight)
        val rasterizer = StrokeRasterizer(RoundBrush(40f, black))
        val pixelsFromCentre = 12

        rasterizer.paint(grid, listOf(StrokePoint(100f, 100f, fullPressure), StrokePoint(200f, 100f, fullPressure)))
        rasterizer.paint(grid, listOf(StrokePoint(100f, 300f, 0.25f), StrokePoint(200f, 300f, 0.25f)))

        assertEquals(opaque, alphaAt(grid, 150, 100 + pixelsFromCentre))
        assertEquals(transparent, alphaAt(grid, 150, 300 + pixelsFromCentre))
    }

    @Test
    fun aStrokeOutsideTheCanvasPaintsNothing() {
        val grid = TileGrid(canvasWidth, canvasHeight)
        val rasterizer = StrokeRasterizer(RoundBrush(10f, black))

        rasterizer.paint(grid, listOf(StrokePoint(-50f, -50f, fullPressure), StrokePoint(-30f, -50f, fullPressure)))

        assertEquals(0, grid.paintedTileCount)
    }

    @Test
    fun theSameStrokeGivesTheSameBytes() {
        val stroke = listOf(StrokePoint(10.3f, 15.7f, 0.4f), StrokePoint(180.9f, 90.1f, 0.9f), StrokePoint(300.2f, 40.5f, 0.6f))
        val color = PremultipliedColor(40, 20, 10, 128)
        val first = TileGrid(canvasWidth, canvasHeight)
        val second = TileGrid(canvasWidth, canvasHeight)

        StrokeRasterizer(RoundBrush(24f, color)).paint(first, stroke)
        StrokeRasterizer(RoundBrush(24f, color)).paint(second, stroke)

        assertEquals(first.paintedTiles().keys, second.paintedTiles().keys)
        first.paintedTiles().forEach { (coordinate, tile) ->
            assertContentEquals(tile.pixels.toByteArray(), second.tileAt(coordinate)?.pixels?.toByteArray())
        }
    }
}
