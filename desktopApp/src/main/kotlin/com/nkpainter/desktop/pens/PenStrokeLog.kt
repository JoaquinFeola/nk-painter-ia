package com.nkpainter.desktop.pens

import java.util.Locale

/**
 * Prints one line per stroke with what Windows reported: the tool, how many samples arrived and
 * the pressure range. One line per stroke, not per sample, so printing does not slow the input.
 */
class PenStrokeLog(private val print: (String) -> Unit) {
    private var tools = linkedSetOf<PenTool>()
    private var sampleCount = 0
    private var minPressure = Float.MAX_VALUE
    private var maxPressure = Float.MIN_VALUE

    fun record(sample: PenSample) {
        if (sample.phase == PenPhase.Down) reset()
        tools += sample.tool
        sampleCount++
        minPressure = minOf(minPressure, sample.point.pressure)
        maxPressure = maxOf(maxPressure, sample.point.pressure)
        if (sample.phase == PenPhase.Up) {
            print("pen: stroke tool=${tools.joinToString("+")} samples=$sampleCount pressure=${format(minPressure)}..${format(maxPressure)}")
        }
    }

    private fun reset() {
        tools = linkedSetOf()
        sampleCount = 0
        minPressure = Float.MAX_VALUE
        maxPressure = Float.MIN_VALUE
    }

    private fun format(pressure: Float): String = String.format(Locale.ROOT, "%.3f", pressure)
}
