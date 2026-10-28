package com.nkpainter.canvases

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot

/** Where one finger touches the drawing area, in its pixels; `id` tells fingers apart while they stay down. */
data class Finger(val id: Int, val x: Float, val y: Float)

/**
 * Follows the first two fingers on the drawing area and turns each move into a step of the view.
 * A third finger is ignored, and the gesture ends when one of the two lifts. Platforms that
 * report one finger per event and those that report all of them at once both fit.
 */
class TwoFingerGesture {
    private companion object {
        const val FINGERS = 2
        const val DEGREES_PER_RADIAN = 180f / PI.toFloat()
        const val HALF_TURN = 180f
        const val FULL_TURN = 360f
    }

    private val fingers = LinkedHashMap<Int, Finger>()

    val isActive: Boolean get() = fingers.size == FINGERS

    fun press(finger: Finger) {
        if (fingers.size < FINGERS) fingers[finger.id] = finger
    }

    /** The step these moves make, or null when there are not two fingers or neither of them moved. */
    fun move(moved: List<Finger>): ViewGestureStep? {
        if (!isActive) return null
        val tracked = moved.filter { it.id in fingers }
        if (tracked.isEmpty()) return null
        val (firstBefore, secondBefore) = fingers.values.toList()
        tracked.forEach { fingers[it.id] = it }
        val (firstNow, secondNow) = fingers.values.toList()
        val distanceBefore = hypot(secondBefore.x - firstBefore.x, secondBefore.y - firstBefore.y)
        val distanceNow = hypot(secondNow.x - firstNow.x, secondNow.y - firstNow.y)
        // Two fingers on the same pixel give no direction or scale to compare against.
        if (distanceBefore == 0f || distanceNow == 0f) return null
        return ViewGestureStep(
            fromX = (firstBefore.x + secondBefore.x) / 2f,
            fromY = (firstBefore.y + secondBefore.y) / 2f,
            toX = (firstNow.x + secondNow.x) / 2f,
            toY = (firstNow.y + secondNow.y) / 2f,
            scale = distanceNow / distanceBefore,
            rotationDegrees = shortestTurn(angleOf(firstNow, secondNow) - angleOf(firstBefore, secondBefore)),
        )
    }

    /** Lifts a finger; true when that ends the gesture. */
    fun release(id: Int): Boolean {
        val wasActive = isActive
        if (fingers.remove(id) == null) return false
        return wasActive
    }

    fun releaseAll(): Boolean {
        val wasActive = isActive
        fingers.clear()
        return wasActive
    }

    private fun angleOf(first: Finger, second: Finger): Float = atan2(second.y - first.y, second.x - first.x) * DEGREES_PER_RADIAN

    // Crossing the line where atan2 jumps from 180° to -180° is a small turn, not almost a full one.
    private fun shortestTurn(degrees: Float): Float = when {
        degrees > HALF_TURN -> degrees - FULL_TURN
        degrees < -HALF_TURN -> degrees + FULL_TURN
        else -> degrees
    }
}
