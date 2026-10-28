package com.nkpainter.projectfiles

import com.nkpainter.engine.Tile
import com.nkpainter.engine.TileCoordinate
import com.nkpainter.engine.TileGrid
import java.io.DataInputStream
import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream

/** Writes and reads a layer file: signature, tile count, tile index, then the tiles, little-endian. */
object LayerTilesCodec {
    private const val INT_BYTES = 4
    private const val SHORT_BYTES = 2
    private const val BYTE_MASK = 0xFF
    private const val BITS_PER_BYTE = 8

    fun write(grid: TileGrid, output: OutputStream) {
        val tiles = grid.paintedTiles().entries.sortedWith(compareBy({ it.key.row }, { it.key.column }))
        output.write(ProjectFileFormat.LAYER_SIGNATURE)
        output.write(littleEndian(tiles.size, INT_BYTES))
        tiles.forEach { (coordinate, _) ->
            output.write(littleEndian(coordinate.column, SHORT_BYTES))
            output.write(littleEndian(coordinate.row, SHORT_BYTES))
        }
        val bytes = ByteArray(Tile.BYTE_COUNT)
        tiles.forEach { (_, tile) ->
            tile.pixels.copyInto(bytes)
            output.write(bytes)
        }
    }

    /** Reads the tiles into [grid]; returns why the layer is invalid, or null when it read entirely. */
    fun read(input: InputStream, grid: TileGrid, expectedTileCount: Int): String? {
        val data = DataInputStream(input)
        return try {
            readInto(data, grid, expectedTileCount)
        } catch (truncated: EOFException) {
            "layer file ends before its tiles"
        }
    }

    private fun readInto(data: DataInputStream, grid: TileGrid, expectedTileCount: Int): String? {
        val signature = ByteArray(ProjectFileFormat.LAYER_SIGNATURE.size).also(data::readFully)
        if (!signature.contentEquals(ProjectFileFormat.LAYER_SIGNATURE)) return "layer file has no NKTL signature"
        val tileCount = readLittleEndian(data, INT_BYTES)
        if (tileCount != expectedTileCount) return "layer file has $tileCount tiles, manifest says $expectedTileCount"

        val coordinates = List(tileCount) { TileCoordinate(readLittleEndian(data, SHORT_BYTES), readLittleEndian(data, SHORT_BYTES)) }
        val outside = coordinates.firstOrNull { it.column >= grid.columnCount || it.row >= grid.rowCount }
        if (outside != null) return "tile $outside falls outside the canvas"
        if (coordinates.toSet().size != coordinates.size) return "layer file repeats a tile"

        val bytes = ByteArray(Tile.BYTE_COUNT)
        coordinates.forEach { coordinate ->
            data.readFully(bytes)
            grid.tileForPainting(coordinate).pixels.copyFrom(bytes)
        }
        return if (data.read() == -1) null else "layer file has bytes after its tiles"
    }

    private fun littleEndian(value: Int, byteCount: Int): ByteArray =
        ByteArray(byteCount) { index -> (value shr (index * BITS_PER_BYTE)).toByte() }

    private fun readLittleEndian(data: DataInputStream, byteCount: Int): Int {
        var value = 0
        repeat(byteCount) { index -> value = value or (data.readUnsignedByte() shl (index * BITS_PER_BYTE)) }
        return value
    }
}
