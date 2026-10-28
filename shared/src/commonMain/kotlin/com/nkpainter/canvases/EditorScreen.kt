package com.nkpainter.canvases

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.nkpainter.brushes.BrushChoices
import com.nkpainter.brushes.DrawingTools
import com.nkpainter.brushes.EraserChoices
import com.nkpainter.brushes.Tool
import com.nkpainter.brushes.tapTool
import com.nkpainter.brushes.DrawingState
import com.nkpainter.themes.NkPainterColors
import kotlin.math.roundToInt

private val barPadding = 4.dp
private val indicatorMargin = 12.dp
private val indicatorPadding = 8.dp
private val compactWidth = 600.dp
private val panelWidth = 320.dp
private val toolSpacing = 4.dp
private val panelPadding = 16.dp
private val swatchSize = 40.dp
private const val PERCENT = 100
private const val OPAQUE_ALPHA = 0xFF000000
private const val FULL_TURN = 360

// Digits of the same width, so the indicator does not jitter while the numbers change.
private val fixedWidthDigits = TextStyle(fontFeatureSettings = "tnum")

/** The view as the indicator shows it, like `250 % · 45°`, with the turn from 0° to 359°. */
fun indicatorText(viewport: Viewport): String {
    val percent = (viewport.zoom * PERCENT).roundToInt()
    val degrees = viewport.rotationDegrees.roundToInt() % FULL_TURN
    return "$percent % · $degrees°"
}

/**
 * The editor around the platform's canvas: the top bar, the toolbar, the Fondo, Pincel and Goma panels,
 * and the indicator while two fingers move the view. The canvas fills the drawing area beside the
 * toolbar, which sits at the left, or at the bottom on a narrow screen.
 */
@Composable
fun EditorScreen(artwork: Artwork, navigation: ViewNavigation, drawing: DrawingState, canvas: @Composable () -> Unit) {
    var drawingArea by remember { mutableStateOf<AreaSize?>(null) }
    var backgroundPanel by remember(artwork) { mutableStateOf<BackgroundPanel?>(null) }
    var toolPanel by remember { mutableStateOf<Tool?>(null) }
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                .padding(horizontal = barPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = { drawingArea?.let(artwork::fitView) },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            ) { Text("Ajustar vista") }
            val panelOpen = backgroundPanel != null
            TextButton(
                onClick = {
                    backgroundPanel = if (panelOpen) null else BackgroundPanel(artwork)
                    toolPanel = null
                },
                // Open, it is the active tool: accent and a filled shape, not color alone.
                colors = if (panelOpen) {
                    ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                } else {
                    ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                },
            ) { Text("Fondo") }
        }
        BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth()) {
            val narrow = maxWidth < compactWidth
            val toolbar = @Composable {
                DrawingTools(drawing) { tool ->
                    toolPanel = tapTool(drawing, tool, toolPanel)
                    backgroundPanel = null
                }
            }
            val drawingBox = @Composable { modifier: Modifier ->
                Box(
                    modifier = modifier.onSizeChanged { size ->
                        val area = AreaSize(size.width, size.height)
                        drawingArea = area
                        // The artwork was fitted to the whole screen before the bars took their share.
                        if (artwork.viewIsFitted) artwork.fitView(area)
                    },
                ) {
                    canvas()
                    if (navigation.isNavigating) {
                        Surface(
                            modifier = Modifier.align(Alignment.TopCenter).padding(top = indicatorMargin),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small,
                        ) {
                            Text(
                                indicatorText(artwork.viewport),
                                modifier = Modifier.padding(horizontal = indicatorPadding * 1.5f, vertical = indicatorPadding / 2),
                                style = MaterialTheme.typography.labelLarge.merge(fixedWidthDigits),
                            )
                        }
                    }
                    backgroundPanel?.let { panel -> PanelOverlay(narrow, onClose = { backgroundPanel = null }) { BackgroundChoices(panel) } }
                    toolPanel?.let { tool ->
                        PanelOverlay(narrow, onClose = { toolPanel = null }) {
                            if (tool == Tool.ERASER) EraserChoices(drawing, narrow) else BrushChoices(drawing, narrow)
                        }
                    }
                }
            }
            if (narrow) {
                Column(modifier = Modifier.fillMaxSize()) {
                    drawingBox(Modifier.weight(1f).fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth().padding(barPadding), horizontalArrangement = Arrangement.spacedBy(toolSpacing, Alignment.CenterHorizontally)) { toolbar() }
                }
            } else {
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(barPadding), verticalArrangement = Arrangement.spacedBy(toolSpacing)) { toolbar() }
                    drawingBox(Modifier.weight(1f).fillMaxHeight())
                }
            }
        }
    }
}

/**
 * Floats beside the toolbar, or rises from the bottom on a narrow screen. A tap outside closes it
 * and never reaches the canvas, so closing does not paint.
 */
@Composable
private fun PanelOverlay(narrow: Boolean, onClose: () -> Unit, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClose))
        Surface(
            modifier = if (narrow) Modifier.align(Alignment.BottomCenter).fillMaxWidth() else Modifier.align(Alignment.TopStart).padding(barPadding).width(panelWidth),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = MaterialTheme.shapes.medium,
        ) {
            content()
        }
    }
}

@Composable
private fun BackgroundChoices(panel: BackgroundPanel) {
    val swatch = Modifier.size(swatchSize).border(1.dp, NkPainterColors.ControlBorder)
    Column(modifier = Modifier.padding(panelPadding), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Fondo", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = panel.choice == BackgroundPanel.Choice.WHITE, onClick = { panel.choose(BackgroundPanel.Choice.WHITE) }, label = { Text("Blanco") })
            FilterChip(selected = panel.choice == BackgroundPanel.Choice.COLOR, onClick = { panel.choose(BackgroundPanel.Choice.COLOR) }, label = { Text("Color") })
            FilterChip(selected = panel.choice == BackgroundPanel.Choice.TRANSPARENT, onClick = { panel.choose(BackgroundPanel.Choice.TRANSPARENT) }, label = { Text("Transparente") })
        }
        if (panel.choice == BackgroundPanel.Choice.COLOR) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = panel.colorText,
                    onValueChange = panel::typeColor,
                    label = { Text("Color") },
                    isError = panel.showsColorError,
                    supportingText = if (panel.showsColorError) ({ Text("Escribe un color como #F5E6C8.") }) else null,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                val typed = panel.typedColor()
                Box(if (typed != null) swatch.background(Color(OPAQUE_ALPHA or typed.rgb.toLong())) else swatch)
            }
        }
        if (panel.choice == BackgroundPanel.Choice.TRANSPARENT) Checkerboard(swatch)
    }
}
