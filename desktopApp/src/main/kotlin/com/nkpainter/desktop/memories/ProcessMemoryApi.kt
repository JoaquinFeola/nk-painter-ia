package com.nkpainter.desktop.memories

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

/** The kernel32 memory function that jna-platform does not map. */
@Suppress("FunctionName")
interface ProcessMemoryApi : StdCallLibrary {
    companion object {
        val instance: ProcessMemoryApi = Native.load("kernel32", ProcessMemoryApi::class.java, W32APIOptions.DEFAULT_OPTIONS)
    }

    fun K32GetProcessMemoryInfo(process: WinNT.HANDLE, counters: ProcessMemoryCountersStructure, size: Int): Boolean
}
