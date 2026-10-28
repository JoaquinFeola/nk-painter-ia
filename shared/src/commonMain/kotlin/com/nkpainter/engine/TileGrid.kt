package com.nkpainter.engine

/** The pixels of one layer. Only the tiles that were painted exist. */
class TileGrid(val widthPx: Int, val heightPx: Int) {
    private val tiles = HashMap<TileCoordinate, Tile>()

    val columnCount: Int get() = (widthPx + Tile.SIZE - 1) / Tile.SIZE
    val rowCount: Int get() = (heightPx + Tile.SIZE - 1) / Tile.SIZE
    val paintedTileCount: Int get() = tiles.size

    fun tileAt(coordinate: TileCoordinate): Tile? = tiles[coordinate]

    fun tileForPainting(coordinate: TileCoordinate): Tile = tiles.getOrPut(coordinate) { Tile() }

    fun paintedTiles(): Map<TileCoordinate, Tile> = tiles

    /** Gives back the memory of every tile; the grid is empty afterwards. A tile kept from before must not be touched. */
    fun release() {
        tiles.values.forEach { it.pixels.release() }
        tiles.clear()
    }
}
