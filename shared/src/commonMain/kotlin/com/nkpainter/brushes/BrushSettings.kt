package com.nkpainter.brushes

/** What each brush, and the eraser, remembers of its own: size, opacity and stabilizer. */
data class BrushSettings(val sizePx: Int, val opacityPercent: Int, val stabilizer: Int) {
    init {
        require(sizePx in SIZE_RANGE && opacityPercent in OPACITY_RANGE && stabilizer in STABILIZER_RANGE) { "out of range: $this" }
    }

    companion object {
        val SIZE_RANGE = 1..500
        val OPACITY_RANGE = 1..100
        val STABILIZER_RANGE = 0..10

        /** A value asked for outside its range stays at the nearest end of it. */
        fun of(sizePx: Int, opacityPercent: Int, stabilizer: Int) =
            BrushSettings(sizePx.coerceIn(SIZE_RANGE), opacityPercent.coerceIn(OPACITY_RANGE), stabilizer.coerceIn(STABILIZER_RANGE))

        fun factoryOf(brush: Brush) = of(brush.factorySizePx, brush.factoryOpacityPercent, brush.factoryStabilizer)
    }

    fun withSize(px: Int) = of(px, opacityPercent, stabilizer)

    fun withOpacity(percent: Int) = of(sizePx, percent, stabilizer)

    fun withStabilizer(level: Int) = of(sizePx, opacityPercent, level)
}
