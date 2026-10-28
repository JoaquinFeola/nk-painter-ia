package com.nkpainter.desktop.pens

import com.nkpainter.engine.StrokePoint

/**
 * One reading of a Windows pointer, in client pixels of the window that received it.
 * `pointerId` tells apart the fingers that touch the screen at the same time. `receivedAtNanos`
 * is when its message arrived, on the `System.nanoTime` clock.
 */
data class PenSample(val point: StrokePoint, val tool: PenTool, val phase: PenPhase, val pointerId: Int, val receivedAtNanos: Long)
