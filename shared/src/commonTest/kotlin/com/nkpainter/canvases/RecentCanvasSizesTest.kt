package com.nkpainter.canvases

import kotlin.test.Test
import kotlin.test.assertEquals

class RecentCanvasSizesTest {
    private val a4 = CanvasSize(2480, 3508, Resolution(300))
    private val custom = CanvasSize(3000, 2000, Resolution(150))
    private val fullHd = CanvasSize(1920, 1080, Resolution(72))
    private val none = RecentCanvasSizes(emptyList())

    private fun afterCreating(vararg sizes: CanvasSize) = sizes.fold(none) { recent, size -> recent.afterCreating(size) }

    @Test
    fun usedSizesAreOfferedNewestFirst() {
        val recent = afterCreating(a4, custom, fullHd)

        assertEquals(listOf(fullHd, custom, a4), recent.sizes)
    }

    @Test
    fun onlyTheLastFiveSizesAreOffered() {
        val first = CanvasSize(100, 100, Resolution(72))
        val others = (2..6).map { CanvasSize(it * 100, 100, Resolution(72)) }

        val recent = afterCreating(first, *others.toTypedArray())

        assertEquals(others.reversed(), recent.sizes)
    }

    @Test
    fun repeatingASizeDoesNotDuplicateIt() {
        val recent = afterCreating(a4, fullHd, a4)

        assertEquals(listOf(a4, fullHd), recent.sizes)
    }

    @Test
    fun theSameWidthAndHeightAtAnotherResolutionIsAnotherSize() {
        val a4At150 = CanvasSize(2480, 3508, Resolution(150))

        val recent = afterCreating(a4, a4At150)

        assertEquals(listOf(a4At150, a4), recent.sizes)
    }
}
