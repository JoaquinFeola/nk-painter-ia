package com.nkpainter.android

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import com.nkpainter.canvases.DeviceCanvasLimit

/**
 * The canvas limit of this device, read once when the app opens. QA can fix it at launch:
 * `adb shell am start -n com.nkpainter/.android.MainActivity --el warnPixels 40000000 --el redLinePixels 75000000`
 */
object AndroidCanvasLimit {
    private const val WARN_PIXELS_EXTRA = "warnPixels"
    private const val RED_LINE_PIXELS_EXTRA = "redLinePixels"

    // A quarter of the device's memory goes to the artwork; calibrated with the measurements of unit 20 (QT-62).
    private const val SHARE_OF_DEVICE_MEMORY = 4

    fun of(context: Context, launch: Intent): DeviceCanvasLimit {
        if (launch.hasExtra(WARN_PIXELS_EXTRA) && launch.hasExtra(RED_LINE_PIXELS_EXTRA)) {
            return DeviceCanvasLimit(launch.getLongExtra(WARN_PIXELS_EXTRA, 0), launch.getLongExtra(RED_LINE_PIXELS_EXTRA, 0))
        }
        val memory = ActivityManager.MemoryInfo()
        context.getSystemService(ActivityManager::class.java).getMemoryInfo(memory)
        return DeviceCanvasLimit.fromBudget(memory.totalMem / SHARE_OF_DEVICE_MEMORY)
    }
}
