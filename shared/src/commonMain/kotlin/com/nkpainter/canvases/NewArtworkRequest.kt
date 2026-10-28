package com.nkpainter.canvases

import kotlin.math.floor
import kotlin.math.roundToInt

/** What the illustrator asked for when creating an artwork, as typed. */
data class NewArtworkRequest(
    val width: Double,
    val height: Double,
    val unit: LengthUnit,
    val dpi: Double,
    val background: CanvasBackground = CanvasBackground.White,
) {
    private companion object {
        const val MINIMUM_SIDE_PX = 16
        val dpiRange = 72..600
    }

    /** Every reason not to create it, together; otherwise whether this device should warn first. */
    fun check(limit: DeviceCanvasLimit): NewArtworkCheck {
        val reasons = mutableListOf<RejectionReason>()
        val dpiIsWhole = dpi == floor(dpi)
        if (!dpiIsWhole) reasons += RejectionReason.ResolutionNotWhole
        if (dpiIsWhole && dpi.toInt() !in dpiRange) reasons += RejectionReason.ResolutionOutOfRange
        // Millimetres and inches need a usable resolution to become pixels.
        val sizeIsKnown = unit == LengthUnit.PIXELS || reasons.isEmpty()
        val size = canvasSize()
        if (sizeIsKnown) reasons += sizeReasons(size, limit)
        if (reasons.isNotEmpty()) return NewArtworkCheck.Rejected(reasons.sortedBy(::reportOrder))
        val ready = NewArtworkCheck.Ready(size, background)
        return if (limit.isAboveWarning(size)) NewArtworkCheck.NeedsConfirmation(ready) else ready
    }

    fun canvasSize(): CanvasSize {
        val wholeDpi = dpi.roundToInt()
        return CanvasSize(unit.toPixels(width, wholeDpi), unit.toPixels(height, wholeDpi), Resolution(wholeDpi))
    }

    private fun sizeReasons(size: CanvasSize, limit: DeviceCanvasLimit): List<RejectionReason> {
        val widthTooSmall = size.widthPx < MINIMUM_SIDE_PX
        val heightTooSmall = size.heightPx < MINIMUM_SIDE_PX
        if (widthTooSmall || heightTooSmall) return listOf(RejectionReason.SideBelowMinimum(widthTooSmall, heightTooSmall))
        if (limit.isAboveRedLine(size)) return listOf(RejectionReason.AboveRedLine(limit.largestWithSameShape(size.widthPx, size.heightPx)))
        return emptyList()
    }

    // The size is told first, the resolution after, as the screen reads top to bottom.
    private fun reportOrder(reason: RejectionReason): Int = when (reason) {
        is RejectionReason.SideBelowMinimum -> 0
        is RejectionReason.AboveRedLine -> 1
        RejectionReason.ResolutionNotWhole, RejectionReason.ResolutionOutOfRange -> 2
    }
}
