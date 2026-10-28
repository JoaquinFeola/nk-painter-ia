package com.nkpainter.android

import android.util.Log
import com.nkpainter.benchmarks.StrokeLatencies

/** Logs, per stroke, the time from a pen event to the moment its front-buffer frame is handed to the display. */
class StrokeLatencyRecorder {
    companion object {
        const val LOG_TAG = "nkpainter-latency"
    }

    private val latencies = StrokeLatencies()

    fun record(latencyMs: Long) = latencies.record(latencyMs)

    fun logAndReset() {
        latencies.summarizeAndReset()?.let { Log.i(LOG_TAG, it) }
    }
}
