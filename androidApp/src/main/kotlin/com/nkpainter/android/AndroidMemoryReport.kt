package com.nkpainter.android

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import java.util.Locale

/** Memory of this app and of the device, as Android reports it. */
class AndroidMemoryReport(private val context: Context) {
    companion object {
        private const val BYTES_PER_MEGABYTE = 1024.0 * 1024.0
        private const val KILOBYTES_PER_MEGABYTE = 1024.0
    }

    fun describe(): String {
        val runtime = Runtime.getRuntime()
        val heapUsed = runtime.totalMemory() - runtime.freeMemory()
        val device = ActivityManager.MemoryInfo().also { context.getSystemService(ActivityManager::class.java).getMemoryInfo(it) }
        return "app PSS ${String.format(Locale.ROOT, "%.0f", Debug.getPss() / KILOBYTES_PER_MEGABYTE)} MB · " +
            "java heap ${mb(heapUsed)}/${mb(runtime.maxMemory())} MB · native heap ${mb(Debug.getNativeHeapAllocatedSize())} MB · " +
            "device available ${mb(device.availMem)}/${mb(device.totalMem)} MB · low memory ${device.lowMemory}"
    }

    private fun mb(bytes: Long): String = String.format(Locale.ROOT, "%.0f", bytes / BYTES_PER_MEGABYTE)
}
