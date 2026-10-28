package com.nkpainter.canvases

/** Why a new artwork is not created. All of them are told together. */
sealed interface RejectionReason {
    data class SideBelowMinimum(val widthTooSmall: Boolean, val heightTooSmall: Boolean) : RejectionReason

    data object ResolutionOutOfRange : RejectionReason

    data object ResolutionNotWhole : RejectionReason

    data class AboveRedLine(val largestAllowed: AreaSize) : RejectionReason
}
