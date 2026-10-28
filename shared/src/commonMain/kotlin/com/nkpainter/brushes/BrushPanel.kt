package com.nkpainter.brushes

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val toolWidth = 72.dp
private val toolPadding = 4.dp
private val toolGlyphSize = 12.dp
private val panelPadding = 16.dp
private val choicePadding = 12.dp
private val valueFieldWidth = 112.dp
private val fixedWidthDigits = TextStyle(fontFeatureSettings = "tnum")

/**
 * What a tap on a tool of the toolbar does: on a tool that is not active it makes it active and
 * opens nothing; on the active tool it opens or closes that tool's panel. Returns the panel to show.
 */
fun tapTool(drawing: DrawingState, tool: Tool, openPanel: Tool?): Tool? {
    if (drawing.activeTool != tool) {
        drawing.use(tool)
        return null
    }
    return if (openPanel == tool) null else tool
}

/** The Pincel and Goma tools of the toolbar, each with its brush or shape under its glyph; the active one is marked. */
@Composable
fun DrawingTools(drawing: DrawingState, onTap: (Tool) -> Unit) {
    ToolButton(Tool.BRUSH.displayName, drawing.activeBrush.displayName, drawing.activeTool == Tool.BRUSH) { onTap(Tool.BRUSH) }
    ToolButton(Tool.ERASER.displayName, drawing.eraserShape.displayName, drawing.activeTool == Tool.ERASER) { onTap(Tool.ERASER) }
}

// Active means accent background and accent text together, never color alone.
@Composable
private fun ToolButton(label: String, detail: String, active: Boolean, onTap: () -> Unit) {
    val content = if (active) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
    Surface(
        modifier = Modifier.width(toolWidth).selectable(selected = active, role = Role.Button, onClick = onTap),
        color = if (active) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
        contentColor = content,
        shape = MaterialTheme.shapes.small,
    ) {
        Column(modifier = Modifier.padding(toolPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.padding(toolPadding).size(toolGlyphSize).background(content, CircleShape))
            Text(label, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
            Text(detail, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
        }
    }
}

/**
 * The brushes of the set, the active one marked; the settings of the active brush; and the
 * pressure curve. Choosing or adjusting changes the drawing state and leaves the panel open.
 */
@Composable
fun BrushChoices(drawing: DrawingState, narrow: Boolean) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(panelPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Pincel", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        BrushList(narrow, chosen = drawing.activeBrush, onChoose = drawing::choose)
        Adjusters(drawing)
        Text("Presión", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            PressureCurve.entries.forEach { Choice(it.displayName, it == drawing.pressureCurve, Modifier) { drawing.choose(it) } }
        }
    }
}

/**
 * The shapes the eraser can take, the chosen one marked, and the eraser's own settings. The
 * pressure curve is chosen in the Pincel panel and also applies here.
 */
@Composable
fun EraserChoices(drawing: DrawingState, narrow: Boolean) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(panelPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Forma de la goma", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        BrushList(narrow, chosen = drawing.eraserShape, onChoose = drawing::chooseEraserShape)
        Adjusters(drawing)
    }
}

@Composable
private fun BrushList(narrow: Boolean, chosen: Brush, onChoose: (Brush) -> Unit) {
    if (narrow) {
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Brush.entries.forEach { Choice(it.displayName, it == chosen, Modifier) { onChoose(it) } }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Brush.entries.forEach { Choice(it.displayName, it == chosen, Modifier.fillMaxWidth()) { onChoose(it) } }
        }
    }
}

/** Size, opacity and stabilizer of the active tool. */
@Composable
private fun Adjusters(drawing: DrawingState) {
    val adjusted = drawing.activeSettings
    Adjuster("Grosor", "px", adjusted.sizePx, BrushSettings.SIZE_RANGE, drawing::saveAdjustments) {
            drawing.adjustSize(it)
            drawing.activeSettings.sizePx
        }
        Adjuster("Opacidad", "%", adjusted.opacityPercent, BrushSettings.OPACITY_RANGE, drawing::saveAdjustments) {
            drawing.adjustOpacity(it)
            drawing.activeSettings.opacityPercent
        }
        Adjuster("Estabilizador", if (adjusted.stabilizer == 0) "apagado" else "", adjusted.stabilizer, BrushSettings.STABILIZER_RANGE, drawing::saveAdjustments) {
            drawing.adjustStabilizer(it)
            drawing.activeSettings.stabilizer
        }
}

// Chosen means accent background and accent text together, never color alone.
@Composable
private fun Choice(label: String, chosen: Boolean, modifier: Modifier, onChoose: () -> Unit) {
    Surface(
        modifier = modifier.selectable(selected = chosen, role = Role.RadioButton, onClick = onChoose),
        color = if (chosen) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainer,
        contentColor = if (chosen) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.small,
    ) {
        Text(label, modifier = Modifier.padding(choicePadding), style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * A slider and a number field for one setting. The slider adjusts as it moves and saves when it is
 * let go; the field applies what was typed on Enter or when it loses focus. `adjust` returns the
 * value that stayed, which the field then shows: one asked for out of range shows its limit, and
 * text that is not a number goes back to the value there was.
 */
@Composable
private fun Adjuster(label: String, unit: String, value: Int, range: IntRange, save: () -> Unit, adjust: (Int) -> Int) {
    var typed by remember(value) { mutableStateOf(value.toString()) }
    var focused by remember { mutableStateOf(false) }
    fun apply() {
        val kept = typed.trim().toIntOrNull()?.let(adjust) ?: value
        typed = kept.toString()
        save()
    }
    Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Slider(
            value = value.toFloat(),
            onValueChange = { adjust(it.roundToInt()) },
            onValueChangeFinished = save,
            valueRange = range.first.toFloat()..range.last.toFloat(),
            // Few values get a stop at each one; the size and opacity ranges are too long for that.
            steps = if (range.last - range.first <= SLIDER_STOPS_LIMIT) range.last - range.first - 1 else 0,
            modifier = Modifier.weight(1f),
        )
        OutlinedTextField(
            value = typed,
            onValueChange = { typed = it },
            suffix = if (unit.isEmpty()) null else ({ Text(unit) }),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.merge(fixedWidthDigits),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { apply() }),
            modifier = Modifier.width(valueFieldWidth)
                .onFocusChanged { state ->
                    if (focused && !state.isFocused) apply()
                    focused = state.isFocused
                }
                .onPreviewKeyEvent { event ->
                    val enter = event.type == KeyEventType.KeyDown && (event.key == Key.Enter || event.key == Key.NumPadEnter)
                    if (enter) apply()
                    enter
                },
        )
    }
}

private const val SLIDER_STOPS_LIMIT = 10
