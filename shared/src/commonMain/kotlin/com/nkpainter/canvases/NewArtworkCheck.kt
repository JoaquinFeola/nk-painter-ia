package com.nkpainter.canvases

/** What the device says about a new artwork request. */
sealed interface NewArtworkCheck {
    /** Only this can become an artwork. */
    data class Ready(val size: CanvasSize, val background: CanvasBackground) : NewArtworkCheck

    /** Big for this device: the illustrator decides whether to go on. */
    data class NeedsConfirmation(val ready: Ready) : NewArtworkCheck

    data class Rejected(val reasons: List<RejectionReason>) : NewArtworkCheck
}
