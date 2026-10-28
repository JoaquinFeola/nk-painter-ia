package com.nkpainter.canvases

/** A named canvas size offered when creating an artwork. */
enum class CanvasPreset {
    A4,
    A5,
    FULL_HD,
    SQUARE_2K,
    SCREEN;

    private companion object {
        val print = Resolution(300)
        val screen = Resolution(72)
    }

    /** The screen preset takes the screen as it is held right now, so it needs the current screen size. */
    fun size(screenSize: AreaSize): CanvasSize = when (this) {
        A4 -> CanvasSize(2480, 3508, print)
        A5 -> CanvasSize(1748, 2480, print)
        FULL_HD -> CanvasSize(1920, 1080, screen)
        SQUARE_2K -> CanvasSize(2048, 2048, screen)
        SCREEN -> CanvasSize(screenSize.widthPx, screenSize.heightPx, screen)
    }
}
