package com.nkpainter.canvases

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * How the canvas is being looked at: the canvas point at the centre of the drawing area, the
 * zoom (1 is one canvas pixel per screen pixel) and the rotation in degrees, from 0 up to 360.
 */
data class Viewport(val centerX: Float, val centerY: Float, val zoom: Float, val rotationDegrees: Float) {
    companion object {
        const val SMALLEST_ZOOM = 0.05f
        const val LARGEST_ZOOM = 32f
        private const val STRAIGHT = 0f
        private const val FULL_TURN = 360f
        private const val RIGHT_ANGLE = 90f
        private const val SNAP_DISTANCE_DEGREES = 5f

        /** The whole canvas inside the area, centred and at 0°. */
        fun fit(canvas: CanvasSize, area: AreaSize): Viewport {
            val zoom = minOf(area.widthPx.toFloat() / canvas.widthPx, area.heightPx.toFloat() / canvas.heightPx)
            return Viewport(canvas.widthPx / 2f, canvas.heightPx / 2f, zoom.coerceIn(SMALLEST_ZOOM, LARGEST_ZOOM), STRAIGHT)
        }

        private fun withinOneTurn(degrees: Float): Float = ((degrees % FULL_TURN) + FULL_TURN) % FULL_TURN
    }

    /** The canvas point under a point of the drawing area, both in pixels. */
    fun screenToCanvas(x: Float, y: Float, area: AreaSize): Pair<Float, Float> {
        val fromCentreX = x - area.widthPx / 2f
        val fromCentreY = y - area.heightPx / 2f
        val radians = -rotationDegrees * PI.toFloat() / 180f
        val unrotatedX = fromCentreX * cos(radians) - fromCentreY * sin(radians)
        val unrotatedY = fromCentreX * sin(radians) + fromCentreY * cos(radians)
        return Pair(centerX + unrotatedX / zoom, centerY + unrotatedY / zoom)
    }

    /**
     * The view after one step of a two-finger gesture: the canvas point that was under the
     * fingers' centre stays under it, even when the zoom has reached a limit and stops.
     */
    fun movedBy(step: ViewGestureStep, area: AreaSize): Viewport {
        val (anchorX, anchorY) = screenToCanvas(step.fromX, step.fromY, area)
        val turned = Viewport(0f, 0f, (zoom * step.scale).coerceIn(SMALLEST_ZOOM, LARGEST_ZOOM), withinOneTurn(rotationDegrees + step.rotationDegrees))
        // With the centre at the canvas origin, this is how far the fingers' new centre sits from the view's centre, in canvas pixels.
        val (offsetX, offsetY) = turned.screenToCanvas(step.toX, step.toY, area)
        return turned.copy(centerX = anchorX - offsetX, centerY = anchorY - offsetY)
    }

    /** The view once the gesture ends: close enough to a right angle, it sticks to it. */
    fun snapped(): Viewport {
        val nearestRightAngle = (rotationDegrees / RIGHT_ANGLE).roundToInt() * RIGHT_ANGLE
        if (abs(rotationDegrees - nearestRightAngle) > SNAP_DISTANCE_DEGREES) return this
        return copy(rotationDegrees = withinOneTurn(nearestRightAngle))
    }
}
