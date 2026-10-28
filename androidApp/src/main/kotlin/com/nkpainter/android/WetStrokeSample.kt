package com.nkpainter.android

import com.nkpainter.brushes.StrokeStyle
import com.nkpainter.engine.StrokePoint

/**
 * A point of the stroke still under the pen, as the front buffer receives it, with its event time
 * (uptime ms) and the brush the stroke started with.
 */
data class WetStrokeSample(
    val point: StrokePoint,
    val startsStroke: Boolean,
    val eventTimeMs: Long,
    val style: StrokeStyle,
)
