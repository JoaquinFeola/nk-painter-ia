package com.nkpainter.canvases

/** Keeps the recent canvas sizes of this device between sessions. */
interface RecentCanvasSizeStore {
    /** Empty when nothing was saved yet or what was saved cannot be read. */
    fun load(): RecentCanvasSizes

    /** A size that cannot be saved is lost silently: there is nothing the illustrator could do about it. */
    fun save(recent: RecentCanvasSizes)
}
