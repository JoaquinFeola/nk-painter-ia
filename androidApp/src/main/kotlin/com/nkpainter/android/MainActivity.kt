package com.nkpainter.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.nkpainter.NkPainterApp
import com.nkpainter.canvases.EditorScreen
import com.nkpainter.canvases.ViewNavigation
import com.nkpainter.benchmarks.ReferenceBenchmark

class MainActivity : ComponentActivity() {
    companion object {
        /** `adb shell am start -n com.nkpainter/.android.MainActivity --ez benchmark true` */
        private const val BENCHMARK_EXTRA = "benchmark"
        private const val BENCHMARK_LOG_TAG = "nkpainter-benchmark"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as NkPainterApplication
        val canvasLimit = app.canvasLimit(intent)
        setContent {
            NkPainterApp(app.session, app.recentStore, canvasLimit, screenSize = { AndroidScreenSize.of(this) }) { artwork ->
                val navigation = remember(artwork) { ViewNavigation(artwork) }
                EditorScreen(artwork, navigation, app.drawing) {
                    AndroidView(
                        factory = { context -> PenCanvasView(context, artwork, navigation, app.drawing) },
                        modifier = Modifier.fillMaxSize(),
                        // Reading them here redraws the canvas whenever the view moves or the background changes.
                        update = { view -> view.show(artwork.viewport, artwork.background) },
                    )
                }
            }
        }
        if (intent.getBooleanExtra(BENCHMARK_EXTRA, false)) startBenchmark()
    }

    private fun startBenchmark() {
        val memory = AndroidMemoryReport(applicationContext)
        val log = { line: String -> Log.i(BENCHMARK_LOG_TAG, line); Unit }
        Thread {
            // Running out of memory is one of the results this measures, so it is reported, not rethrown.
            runCatching { ReferenceBenchmark(cacheDir, memory::describe, log).run() }
                .onFailure { log("benchmark: FAILED ${it::class.simpleName}: ${it.message} · ${memory.describe()}") }
        }.start()
    }
}
