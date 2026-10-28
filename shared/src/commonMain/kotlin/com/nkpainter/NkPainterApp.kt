package com.nkpainter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.nkpainter.canvases.AreaSize
import com.nkpainter.canvases.Artwork
import com.nkpainter.canvases.DeviceCanvasLimit
import com.nkpainter.canvases.NewArtworkCheck
import com.nkpainter.canvases.NewArtworkForm
import com.nkpainter.canvases.NewArtworkScreen
import com.nkpainter.canvases.RecentCanvasSizeStore
import com.nkpainter.states.ArtworkSession
import com.nkpainter.themes.NkPainterColors
import com.nkpainter.themes.NkPainterTheme

private const val WARNING_SIGN = "\u26A0"

/**
 * The app on both platforms: the new artwork screen until an artwork is open, then the
 * platform's editor, which draws the canvas and reads the pen.
 */
@Composable
fun NkPainterApp(
    session: ArtworkSession,
    recentStore: RecentCanvasSizeStore,
    canvasLimit: DeviceCanvasLimit,
    screenSize: () -> AreaSize?,
    editor: @Composable (Artwork) -> Unit,
) {
    NkPainterTheme {
        val open = session.artwork
        if (open != null) return@NkPainterTheme editor(open)
        // The editor is drawn edge to edge; this screen keeps clear of the system bars.
        BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).safeDrawingPadding()) {
            // The editor fills the same area, so the new artwork is fitted to it.
            val drawingArea = AreaSize(constraints.maxWidth, constraints.maxHeight)
            val recent = remember { recentStore.load() }
            val form = remember { NewArtworkForm(screenSize() ?: drawingArea) }
            var awaitingConfirmation by remember { mutableStateOf<NewArtworkCheck.Ready?>(null) }
            val create = { accepted: NewArtworkCheck.Ready ->
                val artwork = Artwork.create(accepted, drawingArea)
                session.open(artwork)
                recentStore.save(recent.afterCreating(artwork.canvasSize))
            }
            NewArtworkScreen(form, recent, screenSize) { request ->
                when (val check = request.check(canvasLimit)) {
                    is NewArtworkCheck.Ready -> create(check)
                    is NewArtworkCheck.NeedsConfirmation -> awaitingConfirmation = check.ready
                    is NewArtworkCheck.Rejected -> form.showRejection(check.reasons)
                }
            }
            awaitingConfirmation?.let { ready ->
                AlertDialog(
                    onDismissRequest = { awaitingConfirmation = null },
                    icon = { Text(WARNING_SIGN, color = NkPainterColors.Warning) },
                    text = { Text("Este lienzo es grande para este equipo. ¿Quieres crearlo igual?") },
                    confirmButton = {
                        Button(onClick = {
                            awaitingConfirmation = null
                            create(ready)
                        }) { Text("Crear igual") }
                    },
                    dismissButton = { TextButton(onClick = { awaitingConfirmation = null }) { Text("Cancelar") } },
                )
            }
        }
    }
}
