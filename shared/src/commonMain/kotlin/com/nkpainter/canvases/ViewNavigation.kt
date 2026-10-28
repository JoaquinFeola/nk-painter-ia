package com.nkpainter.canvases

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Moves the artwork's view with two fingers. Each platform hands it the fingers it reads, in
 * pixels of the drawing area; the artwork itself is never touched, only how it is seen.
 */
class ViewNavigation(private val artwork: Artwork) {
    private val gesture = TwoFingerGesture()

    /** True while two fingers are moving the view; the editor shows the zoom and turn meanwhile. */
    var isNavigating by mutableStateOf(false)
        private set

    fun press(finger: Finger) {
        gesture.press(finger)
        isNavigating = gesture.isActive
    }

    fun move(fingers: List<Finger>, drawingArea: AreaSize) {
        val step = gesture.move(fingers) ?: return
        artwork.moveView(artwork.viewport.movedBy(step, drawingArea))
    }

    fun release(id: Int) {
        if (gesture.release(id)) end()
    }

    fun releaseAll() {
        if (gesture.releaseAll()) end()
    }

    private fun end() {
        artwork.moveView(artwork.viewport.snapped())
        isNavigating = false
    }
}
