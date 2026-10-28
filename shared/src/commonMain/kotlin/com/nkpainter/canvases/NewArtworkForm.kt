package com.nkpainter.canvases

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * What the new artwork screen holds while the illustrator fills it. It checks only that each
 * field is a number or a color; whether the canvas can be created is the request's business.
 */
class NewArtworkForm(screenSize: AreaSize) {
    enum class BackgroundChoice { WHITE, COLOR, TRANSPARENT }

    private companion object {
        val hexColor = Regex("^#[0-9A-Fa-f]{6}$")
        const val HEX_RADIX = 16
        const val DECIMAL_COMMA = ','
        const val DECIMAL_POINT = '.'
    }

    var widthText by mutableStateOf("")
    var heightText by mutableStateOf("")
    var unit by mutableStateOf(LengthUnit.PIXELS)
    var dpiText by mutableStateOf("")
    var selectedPreset: CanvasPreset? by mutableStateOf(null)
        private set
    var selectedRecent: CanvasSize? by mutableStateOf(null)
        private set
    var background by mutableStateOf(BackgroundChoice.WHITE)
    var colorText by mutableStateOf("#")
    var showShapeErrors by mutableStateOf(false)
        private set

    /** Why the device refused the last request; cleared as soon as the illustrator changes something. */
    var rejection: List<RejectionReason> by mutableStateOf(emptyList())
        private set

    val widthIsNumber: Boolean get() = parse(widthText) != null
    val heightIsNumber: Boolean get() = parse(heightText) != null
    val dpiIsNumber: Boolean get() = parse(dpiText) != null
    val colorIsValid: Boolean get() = background != BackgroundChoice.COLOR || hexColor.matches(colorText)

    init {
        choosePreset(CanvasPreset.A4, screenSize)
    }

    fun showRejection(reasons: List<RejectionReason>) {
        rejection = reasons
    }

    fun choosePreset(preset: CanvasPreset, screenSize: AreaSize) {
        fill(preset.size(screenSize))
        selectedPreset = preset
    }

    fun chooseRecent(size: CanvasSize) {
        fill(size)
        selectedRecent = size
    }

    /** Typing a size by hand means neither a preset nor a recent size is chosen any more. */
    fun editedByHand() {
        selectedPreset = null
        selectedRecent = null
        rejection = emptyList()
    }

    /** The size in pixels a size in millimetres or inches turns into, while its fields are numbers. */
    fun pixelPreview(): CanvasSize? {
        val width = parse(widthText) ?: return null
        val height = parse(heightText) ?: return null
        val dpi = parse(dpiText) ?: return null
        return NewArtworkRequest(width, height, unit, dpi).canvasSize()
    }

    /** Null while a field is not a number or the color is not #RRGGBB; the fields then show why. */
    fun toRequest(): NewArtworkRequest? {
        val width = parse(widthText)
        val height = parse(heightText)
        val dpi = parse(dpiText)
        if (width == null || height == null || dpi == null || !colorIsValid) {
            showShapeErrors = true
            return null
        }
        showShapeErrors = false
        return NewArtworkRequest(width, height, unit, dpi, chosenBackground())
    }

    private fun fill(size: CanvasSize) {
        widthText = size.widthPx.toString()
        heightText = size.heightPx.toString()
        unit = LengthUnit.PIXELS
        dpiText = size.resolution.dpi.toString()
        selectedPreset = null
        selectedRecent = null
        rejection = emptyList()
    }

    private fun chosenBackground(): CanvasBackground = when (background) {
        BackgroundChoice.WHITE -> CanvasBackground.White
        BackgroundChoice.TRANSPARENT -> CanvasBackground.Transparent
        BackgroundChoice.COLOR -> CanvasBackground.Solid(colorText.drop(1).toInt(HEX_RADIX))
    }

    private fun parse(text: String): Double? = text.trim().replace(DECIMAL_COMMA, DECIMAL_POINT).toDoubleOrNull()
}
