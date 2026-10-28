package com.nkpainter.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nkpainter.canvases.Artwork

/**
 * The artwork that is open. It lives with the app, not with a screen, so Android recreating its
 * screen on rotation does not lose it. Until the gallery exists it lives only in memory.
 */
class ArtworkSession {
    var artwork: Artwork? by mutableStateOf(null)
        private set

    fun open(artwork: Artwork) {
        this.artwork = artwork
    }
}
