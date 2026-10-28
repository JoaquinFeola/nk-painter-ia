package com.nkpainter.canvases

import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileRecentCanvasSizeStoreTest {
    private val folder = createTempDirectory("recent-sizes").toFile()
    private val file = File(folder, "recent-canvas-sizes.txt")

    @AfterTest
    fun deleteFolder() {
        folder.deleteRecursively()
    }

    @Test
    fun savedSizesComeBackInTheSameOrder() {
        val recent = RecentCanvasSizes(listOf(CanvasSize(1920, 1080, Resolution(72)), CanvasSize(2480, 3508, Resolution(300))))

        FileRecentCanvasSizeStore(file).save(recent)

        assertEquals(recent, FileRecentCanvasSizeStore(file).load())
    }

    @Test
    fun nothingSavedYetIsNoRecentSizes() {
        assertEquals(RecentCanvasSizes(emptyList()), FileRecentCanvasSizeStore(file).load())
    }

    @Test
    fun aLineThatCannotBeReadIsSkipped() {
        file.writeText("1920 1080 72\nbroken\n2480 3508 300")

        val recent = FileRecentCanvasSizeStore(file).load()

        assertEquals(listOf(CanvasSize(1920, 1080, Resolution(72)), CanvasSize(2480, 3508, Resolution(300))), recent.sizes)
    }
}
