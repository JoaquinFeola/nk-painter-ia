package com.nkpainter.benchmarks

import com.nkpainter.engine.PremultipliedColor
import com.nkpainter.engine.RoundBrush
import com.nkpainter.engine.StrokePoint
import com.nkpainter.engine.StrokeRasterizer
import com.nkpainter.engine.Tile
import com.nkpainter.engine.TileCoordinate
import com.nkpainter.engine.TileGrid
import com.nkpainter.projectfiles.ProjectFileContent
import com.nkpainter.projectfiles.ProjectFileLayer
import com.nkpainter.projectfiles.ProjectManifest
import kotlin.random.Random

/**
 * The reference artwork of the product: A4 at 300 dpi with 30 layers. Each layer paints a fixed
 * share of its tiles with smooth colour and a few brush strokes, from a fixed seed, so every
 * device builds the same bytes. How well it compresses says little about real drawings.
 */
object ReferenceArtwork {
    const val WIDTH_PX = 2480
    const val HEIGHT_PX = 3508
    const val DPI = 300
    const val LAYER_COUNT = 30
    const val PAINTED_SHARE = 0.4
    private const val SEED = 20261028
    private const val STROKES_PER_LAYER = 6
    private const val STROKE_POINTS = 12
    private const val STROKE_REACH_PX = 300f
    private const val MAX_CHANNEL = 255
    private const val WHITE_BACKGROUND = "#FFFFFF"

    fun build(): ProjectFileContent {
        val random = Random(SEED)
        val layers = (1..LAYER_COUNT).map { index ->
            val grid = TileGrid(WIDTH_PX, HEIGHT_PX)
            fillShare(grid, random)
            paintStrokes(grid, random)
            ProjectFileLayer("layer-$index", "Capa $index", opacity = 100, visible = true, clipping = false, alphaLock = false, pixels = grid)
        }
        return ProjectFileContent(
            name = "Obra de referencia",
            widthPx = WIDTH_PX,
            heightPx = HEIGHT_PX,
            dpi = DPI,
            background = WHITE_BACKGROUND,
            layers = layers,
            view = ProjectManifest.ManifestView(WIDTH_PX / 2f, HEIGHT_PX / 2f, zoom = 1f, rotationDeg = 0f),
        )
    }

    private fun fillShare(grid: TileGrid, random: Random) {
        val all = (0 until grid.rowCount).flatMap { row -> (0 until grid.columnCount).map { column -> TileCoordinate(column, row) } }
        val chosen = all.shuffled(random).take((all.size * PAINTED_SHARE).toInt())
        chosen.forEach { coordinate -> fillSmooth(grid.tileForPainting(coordinate), random) }
    }

    // A soft gradient with a little noise, translucent, the way shading looks.
    private fun fillSmooth(tile: Tile, random: Random) {
        val alpha = random.nextInt(96, MAX_CHANNEL + 1)
        val red = random.nextInt(alpha + 1)
        val green = random.nextInt(alpha + 1)
        val blue = random.nextInt(alpha + 1)
        for (y in 0 until Tile.SIZE) {
            for (x in 0 until Tile.SIZE) {
                val offset = tile.offsetOf(x, y)
                val shade = (x + y) * alpha / (2 * Tile.SIZE) / 4
                tile.pixels[offset] = (red - minOf(red, shade) + random.nextInt(2)).coerceAtMost(alpha).toByte()
                tile.pixels[offset + 1] = (green - minOf(green, shade)).toByte()
                tile.pixels[offset + 2] = (blue - minOf(blue, shade)).toByte()
                tile.pixels[offset + 3] = alpha.toByte()
            }
        }
    }

    private fun paintStrokes(grid: TileGrid, random: Random) {
        repeat(STROKES_PER_LAYER) {
            val color = PremultipliedColor.opaque(random.nextInt(MAX_CHANNEL + 1), random.nextInt(MAX_CHANNEL + 1), random.nextInt(MAX_CHANNEL + 1))
            val rasterizer = StrokeRasterizer(RoundBrush(random.nextInt(4, 60).toFloat(), color))
            // A short stroke around a random spot, as a hand draws, not a line across the canvas.
            val startX = random.nextFloat() * WIDTH_PX
            val startY = random.nextFloat() * HEIGHT_PX
            val points = List(STROKE_POINTS) {
                StrokePoint(startX + random.nextFloat() * STROKE_REACH_PX, startY + random.nextFloat() * STROKE_REACH_PX, random.nextFloat())
            }
            rasterizer.paint(grid, points)
        }
    }
}
