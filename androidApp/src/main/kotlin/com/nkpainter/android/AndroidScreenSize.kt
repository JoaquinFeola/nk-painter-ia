package com.nkpainter.android

import android.app.Activity
import android.graphics.Point
import android.os.Build
import com.nkpainter.canvases.AreaSize

/** The whole screen in pixels, as the device is held right now (QT-60). */
object AndroidScreenSize {
    fun of(activity: Activity): AreaSize {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = activity.windowManager.maximumWindowMetrics.bounds
            return AreaSize(bounds.width(), bounds.height())
        }
        val size = Point()
        @Suppress("DEPRECATION")
        activity.windowManager.defaultDisplay.getRealSize(size)
        return AreaSize(size.x, size.y)
    }
}
