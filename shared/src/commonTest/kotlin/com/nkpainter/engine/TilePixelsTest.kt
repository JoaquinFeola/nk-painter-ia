package com.nkpainter.engine

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class TilePixelsTest {
    @Test
    fun aNewTileIsTransparent() {
        assertContentEquals(ByteArray(Tile.BYTE_COUNT), Tile().pixels.toByteArray())
    }

    @Test
    fun bytesCopiedInComeBackOutUnchanged() {
        val bytes = ByteArray(Tile.BYTE_COUNT) { index -> (index * 31).toByte() }
        val pixels = Tile().pixels

        pixels.copyFrom(bytes)

        assertContentEquals(bytes, pixels.toByteArray())
        assertEquals(bytes[Tile.BYTE_COUNT - 1], pixels[Tile.BYTE_COUNT - 1])
    }

    @Test
    fun releasingAGridEmptiesIt() {
        val grid = TileGrid(1000, 800)
        grid.tileForPainting(TileCoordinate(1, 2))

        grid.release()

        assertEquals(0, grid.paintedTileCount)
    }
}
