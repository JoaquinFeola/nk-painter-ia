package com.nkpainter.desktop.memories

import com.nkpainter.canvases.DeviceCanvasLimit
import com.sun.management.HotSpotDiagnosticMXBean
import com.sun.management.OperatingSystemMXBean
import java.lang.management.ManagementFactory

/**
 * The canvas limit of this PC, read once when the app opens. QA can fix it at launch:
 * `./gradlew :desktopApp:run -PwarnPixels=40000000 -PredLinePixels=75000000`
 */
object DesktopCanvasLimit {
    const val WARN_PIXELS_PROPERTY = "nkpainter.warnPixels"
    const val RED_LINE_PIXELS_PROPERTY = "nkpainter.redLinePixels"

    // Calibrated with the measurements of unit 20 (QT-62).
    private const val SHARE_OF_PHYSICAL_MEMORY = 0.25
    private const val SHARE_OF_DIRECT_MEMORY = 0.8
    private const val MAX_DIRECT_MEMORY_OPTION = "MaxDirectMemorySize"
    private const val DIRECT_MEMORY_FOLLOWS_HEAP = "0"

    fun read(): DeviceCanvasLimit {
        val warn = System.getProperty(WARN_PIXELS_PROPERTY)?.toLongOrNull()
        val redLine = System.getProperty(RED_LINE_PIXELS_PROPERTY)?.toLongOrNull()
        if (warn != null && redLine != null) return DeviceCanvasLimit(warn, redLine)
        val physical = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean::class.java).totalMemorySize
        val budget = minOf(physical * SHARE_OF_PHYSICAL_MEMORY, maxDirectMemory() * SHARE_OF_DIRECT_MEMORY)
        return DeviceCanvasLimit.fromBudget(budget.toLong())
    }

    /** The tiles live in direct buffers, which the JVM caps; left unset, the cap is the heap's maximum. */
    fun maxDirectMemory(): Long {
        val option = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean::class.java).getVMOption(MAX_DIRECT_MEMORY_OPTION).value
        return if (option == DIRECT_MEMORY_FOLLOWS_HEAP) Runtime.getRuntime().maxMemory() else option.toLong()
    }
}
