package com.nkpainter.canvases

/** The last distinct canvas sizes artworks were created with on this device, newest first. */
data class RecentCanvasSizes(val sizes: List<CanvasSize>) {
    private companion object {
        const val KEPT = 5
    }

    fun afterCreating(size: CanvasSize): RecentCanvasSizes = RecentCanvasSizes((listOf(size) + (sizes - size)).take(KEPT))
}
