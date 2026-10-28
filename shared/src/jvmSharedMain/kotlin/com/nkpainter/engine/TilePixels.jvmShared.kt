package com.nkpainter.engine

import java.nio.ByteBuffer

actual class TilePixels actual constructor(byteCount: Int) {
    private val buffer: ByteBuffer = TileMemory.allocate(byteCount)

    actual operator fun get(index: Int): Byte = buffer.get(index)

    actual operator fun set(index: Int, value: Byte) {
        buffer.put(index, value)
    }

    actual fun copyInto(destination: ByteArray) {
        buffer.duplicate().apply { rewind() }.get(destination)
    }

    actual fun copyFrom(source: ByteArray) {
        buffer.duplicate().apply { rewind() }.put(source)
    }

    actual fun release() {
        TileMemory.free(buffer)
    }
}

/** Where tile bytes are reserved on each JVM. */
internal expect object TileMemory {
    fun allocate(byteCount: Int): ByteBuffer

    fun free(buffer: ByteBuffer)
}
