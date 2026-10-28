package com.nkpainter.brushes

import com.nkpainter.engine.BrushStroke
import com.nkpainter.engine.DabSize
import com.nkpainter.engine.PremultipliedColor
import com.nkpainter.engine.TileGrid

/** The brush as a stroke takes it when it starts; changes made while drawing apply to the next stroke. */
data class StrokeStyle(
    val brush: Brush,
    val color: PremultipliedColor,
    val sizePx: Int,
    val opacityPercent: Int,
    val pressureCurve: PressureCurve = PressureCurve.NORMAL,
    val stabilizerLevel: Int = 0,
    val erases: Boolean = false,
) {
    fun newStabilizer(): Stabilizer = Stabilizer(stabilizerLevel)

    fun startStroke(grid: TileGrid): BrushStroke = brush.startStroke(grid, color, sizePx, opacityPercent, pressureCurve, erases)

    /** The dab for the pressure the pen reports, before the curve. */
    fun dabAt(pressure: Float): DabSize = brush.dabAt(pressureCurve.apply(pressure), sizePx, opacityPercent)
}
