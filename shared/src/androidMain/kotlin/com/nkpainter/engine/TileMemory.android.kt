package com.nkpainter.engine

import com.nkpainter.nativememory.NativeMemory
import java.nio.ByteBuffer

internal actual object TileMemory {
    actual fun allocate(byteCount: Int): ByteBuffer =
        NativeMemory.allocate(byteCount) ?: throw OutOfMemoryError("no native memory left for a tile of $byteCount bytes")

    actual fun free(buffer: ByteBuffer) {
        NativeMemory.free(buffer)
    }
}
