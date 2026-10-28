package com.nkpainter.brushes

import kotlin.math.pow

/**
 * Turns the pressure the pen reports into the one brushes use; one for the whole app. The exponents
 * are factory values, calibrated by eye (QT-96).
 */
enum class PressureCurve(val displayName: String, private val exponent: Float) {
    SOFT("Suave", 0.6f),
    NORMAL("Normal", 1f),
    FIRM("Firme", 1.7f);

    fun apply(pressure: Float): Float = pressure.coerceIn(0f, 1f).pow(exponent)
}
