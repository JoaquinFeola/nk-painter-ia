package com.nkpainter.benchmarks

/**
 * Time from a pen event to the frame that shows it, per stroke. It is the delay the app adds; the
 * screen and the digitiser add theirs on top.
 */
class StrokeLatencies {
    private companion object {
        const val PERCENTILE_95 = 0.95
    }

    private val samplesMs = ArrayList<Long>()

    fun record(latencyMs: Long) {
        samplesMs += latencyMs
    }

    /** The stroke's line, like `stroke: 40 frames · average 12 ms · p95 18 ms · max 21 ms`, or null if nothing was recorded. */
    fun summarizeAndReset(): String? {
        if (samplesMs.isEmpty()) return null
        val sorted = samplesMs.sorted()
        val p95 = sorted[((sorted.size - 1) * PERCENTILE_95).toInt()]
        samplesMs.clear()
        return "stroke: ${sorted.size} frames · average ${sorted.average().toInt()} ms · p95 $p95 ms · max ${sorted.last()} ms"
    }
}
