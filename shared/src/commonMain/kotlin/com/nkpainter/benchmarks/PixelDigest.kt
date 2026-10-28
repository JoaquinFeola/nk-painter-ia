package com.nkpainter.benchmarks

/** FNV-1a over bytes: two devices that print the same digest produced the same pixels. */
class PixelDigest {
    companion object {
        private const val OFFSET_BASIS = -0x340d631b7bdddcdbL
        private const val PRIME = 0x100000001b3L
    }

    private var hash = OFFSET_BASIS

    fun add(bytes: ByteArray) {
        for (byte in bytes) {
            hash = (hash xor (byte.toLong() and 0xFF)) * PRIME
        }
    }

    fun hex(): String = hash.toULong().toString(16).padStart(16, '0')
}
