package com.nkpainter.canvases

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nkpainter.themes.NkPainterColors

private val compactWidth = 600.dp
private val gap = 16.dp
private val swatchSize = 40.dp
private const val HEX_RADIX = 16
private const val OPAQUE_ALPHA = 0xFF000000

// Only the screen preset reads the screen; the others ignore it.
private val noScreen = AreaSize(0, 0)

/**
 * Where a new artwork is chosen: presets, recent sizes, a custom size and the background.
 * Choosing a preset or a recent size fills the custom fields, and those are what gets created.
 */
@Composable
fun NewArtworkScreen(
    form: NewArtworkForm,
    recent: RecentCanvasSizes,
    screenSize: () -> AreaSize?,
    onCreate: (NewArtworkRequest) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val create: () -> Unit = { form.toRequest()?.let(onCreate) }
            if (maxWidth < compactWidth) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(gap), verticalArrangement = Arrangement.spacedBy(gap)) {
                        Title()
                        Presets(form, screenSize, scrollsSideways = true)
                        Recents(form, recent)
                        CustomSize(form)
                        Background(form)
                    }
                    CreateBar(form, onCreate = create)
                }
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(gap), verticalArrangement = Arrangement.spacedBy(gap)) {
                    Title()
                    Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(gap)) {
                        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(gap)) {
                            Presets(form, screenSize, scrollsSideways = false)
                            Recents(form, recent)
                        }
                        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(gap)) {
                            CustomSize(form)
                            Background(form)
                        }
                    }
                    CreateBar(form, onCreate = create)
                }
            }
        }
    }
}

@Composable
private fun Title() {
    Text("Obra nueva", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun Presets(form: NewArtworkForm, screenSize: () -> AreaSize?, scrollsSideways: Boolean) {
    // The screen preset is read when the screen opens and again when it is chosen, so it follows rotation.
    val screen = screenSize()
    val offered = CanvasPreset.entries.filter { it != CanvasPreset.SCREEN || screen != null }
    fun choose(preset: CanvasPreset) {
        val current = screenSize() ?: screen ?: return
        form.choosePreset(preset, current)
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Prearmados")
        val chips: @Composable () -> Unit = {
            offered.forEach { preset ->
                val size = preset.size(screen ?: noScreen)
                FilterChip(
                    selected = form.selectedPreset == preset,
                    onClick = { choose(preset) },
                    label = { Text("${preset.label()} · ${size.describe()}") },
                )
            }
        }
        if (scrollsSideways) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) { chips() }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { chips() }
        }
    }
}

@Composable
private fun Recents(form: NewArtworkForm, recent: RecentCanvasSizes) {
    if (recent.sizes.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SectionTitle("Recientes")
        recent.sizes.forEach { size ->
            FilterChip(selected = form.selectedRecent == size, onClick = { form.chooseRecent(size) }, label = { Text(size.describe()) })
        }
    }
}

@Composable
private fun CustomSize(form: NewArtworkForm) {
    val numberError = "Escribe un número."
    val side = form.rejection.filterIsInstance<RejectionReason.SideBelowMinimum>().firstOrNull()
    val redLine = form.rejection.filterIsInstance<RejectionReason.AboveRedLine>().firstOrNull()
    val dpiReason = form.rejection.firstOrNull { it == RejectionReason.ResolutionNotWhole || it == RejectionReason.ResolutionOutOfRange }
    fun sideError(tooSmall: Boolean, isNumber: Boolean): String? = when {
        form.showShapeErrors && !isNumber -> numberError
        tooSmall -> SIDE_MESSAGE
        redLine != null -> redLine.message()
        else -> null
    }
    val widthError = sideError(side?.widthTooSmall == true, form.widthIsNumber)
    val heightError = sideError(side?.heightTooSmall == true, form.heightIsNumber)
    val dpiError = if (form.showShapeErrors && !form.dpiIsNumber) numberError else dpiReason?.message()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Personalizado")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NumberField("Ancho", form.widthText, widthError, Modifier.weight(1f)) {
                form.widthText = it
                form.editedByHand()
            }
            NumberField("Alto", form.heightText, heightError, Modifier.weight(1f)) {
                form.heightText = it
                form.editedByHand()
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LengthUnit.entries.forEach { unit ->
                FilterChip(selected = form.unit == unit, onClick = { form.unit = unit; form.editedByHand() }, label = { Text(unit.label()) })
            }
        }
        NumberField("Resolución (dpi)", form.dpiText, dpiError, Modifier.fillMaxWidth()) {
            form.dpiText = it
            form.editedByHand()
        }
        if (form.unit != LengthUnit.PIXELS) {
            form.pixelPreview()?.let { Text("${it.widthPx}×${it.heightPx} px", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable
private fun NumberField(label: String, value: String, error: String?, modifier: Modifier, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier,
    )
}

@Composable
private fun Background(form: NewArtworkForm) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("Fondo")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = form.background == NewArtworkForm.BackgroundChoice.WHITE, onClick = { form.background = NewArtworkForm.BackgroundChoice.WHITE }, label = { Text("Blanco") })
            FilterChip(selected = form.background == NewArtworkForm.BackgroundChoice.COLOR, onClick = { form.background = NewArtworkForm.BackgroundChoice.COLOR }, label = { Text("Color") })
            FilterChip(selected = form.background == NewArtworkForm.BackgroundChoice.TRANSPARENT, onClick = { form.background = NewArtworkForm.BackgroundChoice.TRANSPARENT }, label = { Text("Transparente") })
        }
        if (form.background == NewArtworkForm.BackgroundChoice.COLOR) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val showError = form.showShapeErrors && !form.colorIsValid
                OutlinedTextField(
                    value = form.colorText,
                    onValueChange = { form.colorText = it },
                    label = { Text("Color") },
                    isError = showError,
                    supportingText = if (showError) ({ Text("Escribe un color como #F5E6C8.") }) else null,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Swatch(form)
            }
        }
        if (form.background == NewArtworkForm.BackgroundChoice.TRANSPARENT) Swatch(form)
    }
}

@Composable
private fun Swatch(form: NewArtworkForm) {
    val border = Modifier.size(swatchSize).border(1.dp, NkPainterColors.ControlBorder)
    when {
        form.background == NewArtworkForm.BackgroundChoice.TRANSPARENT -> Checkerboard(border)
        form.colorIsValid -> Box(border.background(Color(OPAQUE_ALPHA or form.colorText.drop(1).toLong(HEX_RADIX))))
        else -> Box(border)
    }
}

@Composable
internal fun Checkerboard(modifier: Modifier) {
    Column(modifier) {
        repeat(2) { row ->
            Row(Modifier.weight(1f)) {
                repeat(2) { column ->
                    val light = (row + column) % 2 == 0
                    Box(Modifier.weight(1f).fillMaxSize().background(if (light) NkPainterColors.CheckerLight else NkPainterColors.CheckerDark))
                }
            }
        }
    }
}

@Composable
private fun CreateBar(form: NewArtworkForm, onCreate: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(gap), horizontalArrangement = Arrangement.spacedBy(gap), verticalAlignment = Alignment.CenterVertically) {
        // Every reason together, so the illustrator fixes them all in one pass.
        Column(modifier = Modifier.weight(1f)) {
            form.rejection.forEach { Text(it.message(), color = MaterialTheme.colorScheme.error) }
        }
        Button(onClick = onCreate) { Text("Crear") }
    }
}

private const val SIDE_MESSAGE = "El mínimo es 16 px por lado."

private fun RejectionReason.message(): String = when (this) {
    is RejectionReason.SideBelowMinimum -> SIDE_MESSAGE
    RejectionReason.ResolutionOutOfRange -> "La resolución va de 72 a 600 dpi."
    RejectionReason.ResolutionNotWhole -> "La resolución es un número entero de 72 a 600 dpi."
    is RejectionReason.AboveRedLine -> "Este equipo admite hasta ${largestAllowed.widthPx}×${largestAllowed.heightPx} px."
}

private fun CanvasPreset.label(): String = when (this) {
    CanvasPreset.A4 -> "A4"
    CanvasPreset.A5 -> "A5"
    CanvasPreset.FULL_HD -> "Full HD"
    CanvasPreset.SQUARE_2K -> "2K cuadrado"
    CanvasPreset.SCREEN -> "Pantalla"
}

private fun LengthUnit.label(): String = when (this) {
    LengthUnit.PIXELS -> "px"
    LengthUnit.MILLIMETERS -> "mm"
    LengthUnit.INCHES -> "pulg"
}

private fun CanvasSize.describe(): String = "${widthPx}×${heightPx} px · ${resolution.dpi} dpi"
