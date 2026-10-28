package com.nkpainter.brushes

import com.nkpainter.engine.PremultipliedColor
import com.nkpainter.engine.StrokePoint
import com.nkpainter.engine.Tile
import com.nkpainter.engine.TileCoordinate
import com.nkpainter.engine.TileGrid
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PressureTest {
    private val black = PremultipliedColor.opaque(0, 0, 0)
    private val fullPressure = 1f
    private val halfPressure = 0.5f

    private fun style(brush: Brush, sizePx: Int, opacityPercent: Int = 100, curve: PressureCurve = PressureCurve.NORMAL) =
        StrokeStyle(brush, black, sizePx, opacityPercent, curve)

    @Test
    fun halfPressureHalvesTheWidthOfABrushThatRespondsInSize() {
        val dab = style(Brush.PENCIL, sizePx = 20).dabAt(halfPressure)

        assertEquals(10f, dab.diameterPx)
    }

    @Test
    fun halfPressureHalvesTheOpacityOfTheAirbrushAndKeepsItsWidth() {
        val dab = style(Brush.AIRBRUSH, sizePx = 100, opacityPercent = 80).dabAt(halfPressure)

        assertEquals(style(Brush.AIRBRUSH, sizePx = 100, opacityPercent = 40).dabAt(fullPressure), dab)
    }

    @Test
    fun aHalfPressureAirbrushBuildsUpToHalfItsOpacityAndNoMore() {
        val grid = TileGrid(2480, 3508)
        val stroke = style(Brush.AIRBRUSH, sizePx = 100, opacityPercent = 80).startStroke(grid)

        stroke.addPoint(StrokePoint(100f, 300f, halfPressure))
        repeat(20) { pass -> stroke.addPoint(StrokePoint(if (pass % 2 == 0) 1100f else 100f, 300f, halfPressure)) }
        stroke.finish()

        val fortyPercent = 102
        assertTrue(alphaAt(grid, 600, 300) in fortyPercent - 2..fortyPercent, "centre alpha ${alphaAt(grid, 600, 300)}")
    }

    @Test
    fun aBrushThatDoesNotRespondPaintsTheSameAtAnyPressure() {
        val marker = style(Brush.MARKER, sizePx = 20)

        assertEquals(marker.dabAt(fullPressure), marker.dabAt(0.3f))
    }

    @Test
    fun theLightestPressureNeverGoesBelowOnePixel() {
        val dab = style(Brush.PENCIL, sizePx = 20).dabAt(0.01f)

        assertEquals(1f, dab.diameterPx)
    }

    @Test
    fun theSoftCurveGivesMoreWidthAtHalfPressure() {
        val dab = style(Brush.PENCIL, sizePx = 20, curve = PressureCurve.SOFT).dabAt(halfPressure)

        assertTrue(dab.diameterPx > 10f, "soft curve at half pressure: ${dab.diameterPx} px")
    }

    @Test
    fun theFirmCurveGivesLessWidthAtHalfPressure() {
        val dab = style(Brush.PENCIL, sizePx = 20, curve = PressureCurve.FIRM).dabAt(halfPressure)

        assertTrue(dab.diameterPx < 10f, "firm curve at half pressure: ${dab.diameterPx} px")
    }

    @Test
    fun theFirmCurveAlsoLowersTheAirbrushOpacity() {
        val firm = style(Brush.AIRBRUSH, sizePx = 100, opacityPercent = 80, curve = PressureCurve.FIRM).dabAt(halfPressure)

        val atFortyPercent = style(Brush.AIRBRUSH, sizePx = 100, opacityPercent = 40).dabAt(fullPressure)
        assertTrue(firm.opacity < atFortyPercent.opacity, "firm: ${firm.opacity}, 40 %: ${atFortyPercent.opacity}")
    }

    @Test
    fun theStrokeWidensWithThePressureWithoutJumps() {
        val grid = TileGrid(2480, 3508)
        val stroke = style(Brush.PENCIL, sizePx = 20).startStroke(grid)

        stroke.addPoint(StrokePoint(100f, 300f, 0.1f))
        stroke.addPoint(StrokePoint(1100f, 300f, fullPressure))
        stroke.finish()

        val widths = (100..1100 step 50).map { x -> paintedHeight(grid, x, aroundY = 300) }
        assertTrue(widths.first() in 1..3, "starts at ${widths.first()} px")
        assertTrue(widths.last() in 19..21, "ends at ${widths.last()} px")
        val steps = widths.zipWithNext { before, after -> after - before }
        assertTrue(steps.all { it in -1..2 }, "widths along the line: $widths")
    }

    private fun paintedHeight(grid: TileGrid, x: Int, aroundY: Int): Int {
        val painted = (aroundY - 30..aroundY + 30).filter { y -> alphaAt(grid, x, y) > 0 }
        return if (painted.isEmpty()) 0 else painted.last() - painted.first() + 1
    }

    private fun alphaAt(grid: TileGrid, x: Int, y: Int): Int {
        val tile = grid.tileAt(TileCoordinate(x / Tile.SIZE, y / Tile.SIZE)) ?: return 0
        return tile.pixels[tile.offsetOf(x % Tile.SIZE, y % Tile.SIZE) + 3].toInt() and 0xFF
    }
}
