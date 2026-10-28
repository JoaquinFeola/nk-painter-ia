package com.nkpainter.engine

/** A sampled point of a stroke, in canvas pixels, with the pen pressure normalised to 0–1. */
data class StrokePoint(val x: Float, val y: Float, val pressure: Float)
