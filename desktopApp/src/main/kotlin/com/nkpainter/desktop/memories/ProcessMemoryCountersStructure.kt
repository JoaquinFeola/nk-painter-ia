package com.nkpainter.desktop.memories

import com.sun.jna.Structure
import com.sun.jna.platform.win32.BaseTSD

/** Win32 PROCESS_MEMORY_COUNTERS. */
@Structure.FieldOrder(
    "cb", "pageFaultCount", "peakWorkingSetSize", "workingSetSize", "quotaPeakPagedPoolUsage", "quotaPagedPoolUsage",
    "quotaPeakNonPagedPoolUsage", "quotaNonPagedPoolUsage", "pagefileUsage", "peakPagefileUsage",
)
class ProcessMemoryCountersStructure : Structure() {
    @JvmField var cb: Int = 0
    @JvmField var pageFaultCount: Int = 0
    @JvmField var peakWorkingSetSize: BaseTSD.SIZE_T = BaseTSD.SIZE_T()
    @JvmField var workingSetSize: BaseTSD.SIZE_T = BaseTSD.SIZE_T()
    @JvmField var quotaPeakPagedPoolUsage: BaseTSD.SIZE_T = BaseTSD.SIZE_T()
    @JvmField var quotaPagedPoolUsage: BaseTSD.SIZE_T = BaseTSD.SIZE_T()
    @JvmField var quotaPeakNonPagedPoolUsage: BaseTSD.SIZE_T = BaseTSD.SIZE_T()
    @JvmField var quotaNonPagedPoolUsage: BaseTSD.SIZE_T = BaseTSD.SIZE_T()
    @JvmField var pagefileUsage: BaseTSD.SIZE_T = BaseTSD.SIZE_T()
    @JvmField var peakPagefileUsage: BaseTSD.SIZE_T = BaseTSD.SIZE_T()
}
