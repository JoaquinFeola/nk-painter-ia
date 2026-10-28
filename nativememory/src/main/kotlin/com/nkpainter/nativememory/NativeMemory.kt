package com.nkpainter.nativememory

import java.nio.ByteBuffer

/**
 * Memory reserved with calloc, outside the Java heap and its per-app cap. ByteBuffer.allocateDirect
 * does not do this on Android: there it is a byte[] inside the Java heap (QT-52).
 * Nothing frees it but [free].
 */
object NativeMemory {
    init {
        System.loadLibrary("nkpainter_native_memory")
    }

    /** A zeroed buffer of [byteCount] bytes, or null when the device has no memory left. */
    external fun allocate(byteCount: Int): ByteBuffer?

    /** Gives back a buffer from [allocate]. The buffer must not be touched afterwards. */
    external fun free(buffer: ByteBuffer)
}
