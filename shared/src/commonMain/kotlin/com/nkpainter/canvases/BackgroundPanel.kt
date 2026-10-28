package com.nkpainter.canvases

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * What the editor's Fondo panel holds. Every valid choice changes the artwork's background at
 * once, with nothing to confirm; a mistyped color leaves it as it was.
 */
class BackgroundPanel(private val artwork: Artwork) {
    enum class Choice { WHITE, COLOR, TRANSPARENT }

    private companion object {
        val hexColor = Regex("^#[0-9A-Fa-f]{6}$")
        const val HEX_RADIX = 16
        const val HEX_DIGITS = 6
        const val TYPED_COLOR_LENGTH = HEX_DIGITS + 1
    }

    var choice by mutableStateOf(choiceOf(artwork.background))
        private set
    var colorText by mutableStateOf(textOf(artwork.background))
        private set

    /** Only once the color is as long as #RRGGBB: halfway through typing it is not a mistake yet. */
    val showsColorError: Boolean get() = choice == Choice.COLOR && colorText.length >= TYPED_COLOR_LENGTH && typedColor() == null

    fun choose(choice: Choice) {
        this.choice = choice
        apply()
    }

    fun typeColor(text: String) {
        colorText = text
        apply()
    }

    /** The color the field holds, or null while it is not #RRGGBB. */
    fun typedColor(): CanvasBackground.Solid? =
        if (hexColor.matches(colorText)) CanvasBackground.Solid(colorText.drop(1).toInt(HEX_RADIX)) else null

    private fun apply() {
        val chosen = when (choice) {
            Choice.WHITE -> CanvasBackground.White
            Choice.TRANSPARENT -> CanvasBackground.Transparent
            Choice.COLOR -> typedColor() ?: return
        }
        artwork.changeBackground(chosen)
    }

    private fun choiceOf(background: CanvasBackground): Choice = when (background) {
        CanvasBackground.White -> Choice.WHITE
        CanvasBackground.Transparent -> Choice.TRANSPARENT
        is CanvasBackground.Solid -> Choice.COLOR
    }

    private fun textOf(background: CanvasBackground): String = when (background) {
        is CanvasBackground.Solid if background != CanvasBackground.White -> "#" + background.rgb.toString(HEX_RADIX).uppercase().padStart(HEX_DIGITS, '0')
        else -> "#"
    }
}
