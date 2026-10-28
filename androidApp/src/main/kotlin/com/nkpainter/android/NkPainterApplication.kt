package com.nkpainter.android

import android.app.Application
import android.content.Intent
import com.nkpainter.brushes.DrawingState
import com.nkpainter.brushes.FileDrawingSettingsStore
import com.nkpainter.canvases.DeviceCanvasLimit
import com.nkpainter.canvases.FileRecentCanvasSizeStore
import com.nkpainter.states.ArtworkSession
import java.io.File

/** Holds what must outlive the Activity, which Android recreates when the device rotates. */
class NkPainterApplication : Application() {
    private companion object {
        const val RECENT_SIZES_FILE = "recent-canvas-sizes.txt"
        const val DRAWING_SETTINGS_FILE = "drawing-settings.txt"
    }

    val session = ArtworkSession()

    val drawing by lazy { DrawingState(FileDrawingSettingsStore(File(filesDir, DRAWING_SETTINGS_FILE))) }

    val recentStore by lazy { FileRecentCanvasSizeStore(File(filesDir, RECENT_SIZES_FILE)) }

    private var limit: DeviceCanvasLimit? = null

    /** Read once per process, so the same request gets the same answer for the whole session. */
    fun canvasLimit(launch: Intent): DeviceCanvasLimit = limit ?: AndroidCanvasLimit.of(this, launch).also { limit = it }
}
