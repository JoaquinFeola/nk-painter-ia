package com.nkpainter.canvases

/**
 * One step of a two-finger gesture, in pixels of the drawing area: where the centre of the
 * fingers was and where it is now, how many times farther apart they are, and how many degrees
 * they turned, clockwise on screen.
 */
data class ViewGestureStep(val fromX: Float, val fromY: Float, val toX: Float, val toY: Float, val scale: Float, val rotationDegrees: Float)
