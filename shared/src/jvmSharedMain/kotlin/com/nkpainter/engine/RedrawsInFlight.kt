package com.nkpainter.engine

/**
 * Lets a full redraw start only when no other is still on its way to the screen, and folds the
 * redraws asked for meanwhile into one. A renderer with a small buffer pool blocks its own thread
 * when it is handed more redraws than it has buffers, waiting for a buffer that only that thread
 * can give back. Redraws started elsewhere count too: they hold a buffer just the same.
 */
class RedrawsInFlight(private val redraw: () -> Unit) {
    private var inFlight = 0
    private var asked = false

    @Synchronized
    fun ask() {
        if (inFlight == 0) start() else asked = true
    }

    @Synchronized
    fun startedElsewhere() {
        inFlight++
    }

    @Synchronized
    fun finished() {
        inFlight = maxOf(inFlight - 1, 0)
        if (inFlight == 0 && asked) start()
    }

    /** The surface is gone with whatever was in flight; nothing more will finish. */
    @Synchronized
    fun forget() {
        inFlight = 0
        asked = false
    }

    private fun start() {
        asked = false
        inFlight++
        redraw()
    }
}
