package com.nkpainter.desktop.memories

import com.sun.jna.platform.win32.Kernel32
import java.lang.management.ManagementFactory
import java.util.Locale

/** Memory of this process and of the PC: the working set includes the JVM's own base cost. */
object DesktopMemoryReport {
    private const val BYTES_PER_MEGABYTE = 1024.0 * 1024.0

    fun describe(): String {
        val runtime = Runtime.getRuntime()
        val heapUsed = runtime.totalMemory() - runtime.freeMemory()
        val system = ManagementFactory.getOperatingSystemMXBean() as com.sun.management.OperatingSystemMXBean
        val counters = ProcessMemoryCountersStructure().also { it.cb = it.size() }
        ProcessMemoryApi.instance.K32GetProcessMemoryInfo(Kernel32.INSTANCE.GetCurrentProcess(), counters, counters.cb)
        return "process working set ${mb(counters.workingSetSize.toLong())} MB (peak ${mb(counters.peakWorkingSetSize.toLong())}) · heap ${mb(heapUsed)}/${mb(runtime.maxMemory())} MB · " +
            "PC free ${mb(system.freeMemorySize)}/${mb(system.totalMemorySize)} MB"
    }

    private fun mb(bytes: Long): String = String.format(Locale.ROOT, "%.0f", bytes / BYTES_PER_MEGABYTE)
}
