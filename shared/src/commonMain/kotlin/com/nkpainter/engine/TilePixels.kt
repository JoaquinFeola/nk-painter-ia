package com.nkpainter.engine

/**
 * The bytes of one tile, zeroed when created. Each platform decides where they live: on Android
 * outside the Java heap, whose per-app cap does not fit the reference artwork (QT-52).
 */
expect class TilePixels(byteCount: Int) {
    operator fun get(index: Int): Byte

    operator fun set(index: Int, value: Byte)

    fun copyInto(destination: ByteArray)

    fun copyFrom(source: ByteArray)

    /** Gives the memory back. The pixels must not be touched afterwards. */
    fun release()
}

fun TilePixels.toByteArray(byteCount: Int = Tile.BYTE_COUNT): ByteArray = ByteArray(byteCount).also(::copyInto)
