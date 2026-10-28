package com.nkpainter.engine

import java.nio.ByteBuffer

// On the desktop JVM a direct buffer already lives outside the Java heap, and the garbage
// collector gives it back.
internal actual object TileMemory {
    actual fun allocate(byteCount: Int): ByteBuffer = ByteBuffer.allocateDirect(byteCount)

    actual fun free(buffer: ByteBuffer) = Unit
}
