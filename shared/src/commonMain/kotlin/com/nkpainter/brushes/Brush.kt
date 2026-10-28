package com.nkpainter.brushes

import com.nkpainter.engine.BrushStroke
import com.nkpainter.engine.BrushTip
import com.nkpainter.engine.DabSize
import com.nkpainter.engine.PremultipliedColor
import com.nkpainter.engine.TileGrid
import kotlin.math.max
import kotlin.math.roundToInt

/** What the stylus pressure changes in a brush's dabs. */
enum class PressureResponse(val changesSize: Boolean, val changesOpacity: Boolean) {
    SIZE(true, false),
    OPACITY(false, true),
    SIZE_AND_OPACITY(true, true),
    NOTHING(false, false),
}

/**
 * The base set, in the order the illustrator sees it. Sizes, opacities and the airbrush's flow
 * are factory values, calibrated by eye while building.
 */
enum class Brush(
    val displayName: String,
    val tip: BrushTip,
    val pressureResponse: PressureResponse,
    val factorySizePx: Int,
    val factoryOpacityPercent: Int,
    private val buildsUp: Boolean = false,
    val factoryStabilizer: Int = 0,
) {
    PENCIL("Lápiz", BrushTip.GRAINY_ROUND, PressureResponse.SIZE_AND_OPACITY, factorySizePx = 4, factoryOpacityPercent = 100),
    INK("Tinta", BrushTip.HARD_ROUND, PressureResponse.SIZE, factorySizePx = 8, factoryOpacityPercent = 100, factoryStabilizer = 3),
    MARKER("Rotulador", BrushTip.HARD_ROUND, PressureResponse.NOTHING, factorySizePx = 16, factoryOpacityPercent = 100),
    ROUND("Pincel redondo", BrushTip.HARD_ROUND, PressureResponse.SIZE, factorySizePx = 20, factoryOpacityPercent = 100),
    AIRBRUSH("Aerógrafo", BrushTip.SOFT_ROUND, PressureResponse.OPACITY, factorySizePx = 60, factoryOpacityPercent = 100, buildsUp = true),
    SOFT("Pincel suave", BrushTip.SOFT_ROUND, PressureResponse.OPACITY, factorySizePx = 40, factoryOpacityPercent = 100),
    PIXEL("Píxel", BrushTip.SQUARE, PressureResponse.NOTHING, factorySizePx = 1, factoryOpacityPercent = 100);

    private companion object {
        const val MIN_SIZE_PX = 1f
        const val MIN_OPACITY_PERCENT = 1f
        const val FULL_PERCENT = 100f
        const val MAX_OPACITY = 255

        // Each airbrush dab lays down this share of the opacity, so paint builds up while it keeps going.
        const val AIRBRUSH_FLOW = 0.08f
    }

    /** Starts a stroke on `grid` with this brush's shape, at the given size and opacity; `erases` takes paint away instead. */
    fun startStroke(
        grid: TileGrid,
        color: PremultipliedColor,
        sizePx: Int,
        opacityPercent: Int,
        curve: PressureCurve = PressureCurve.NORMAL,
        erases: Boolean = false,
    ): BrushStroke {
        return BrushStroke(grid, tip, color, buildsUp, erases) { pressure -> dabAt(curve.apply(pressure), sizePx, opacityPercent) }
    }

    /** The dab at this pressure: what the brush lets pressure change shrinks with it, never below 1 px and 1 %. */
    fun dabAt(pressure: Float, sizePx: Int, opacityPercent: Int): DabSize {
        val diameter = if (pressureResponse.changesSize) max(MIN_SIZE_PX, sizePx * pressure) else sizePx.toFloat()
        val percent = if (pressureResponse.changesOpacity) max(MIN_OPACITY_PERCENT, opacityPercent * pressure) else opacityPercent.toFloat()
        val flow = if (buildsUp) AIRBRUSH_FLOW else 1f
        val ceiling = toOpacity(percent)
        return DabSize(diameter, max(1, (ceiling * flow).roundToInt()), ceiling)
    }

    private fun toOpacity(percent: Float): Int = (percent * MAX_OPACITY / FULL_PERCENT).roundToInt()
}
