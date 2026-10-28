package com.nkpainter.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// One dark, untinted theme: the interface must not tint how the artwork's colors are seen.
private val colors = darkColorScheme(
    primary = Color(0xFF3DD6B5),
    onPrimary = Color(0xFF0F1F1B),
    // What is chosen (a preset, a tool) is marked with the accent, never with Material's default tint.
    secondaryContainer = Color(0xFF1F3D37),
    onSecondaryContainer = Color(0xFF3DD6B5),
    background = Color(0xFF1C1C1C),
    onBackground = Color(0xFFEDEDED),
    surface = Color(0xFF1C1C1C),
    onSurface = Color(0xFFEDEDED),
    surfaceVariant = Color(0xFF262626),
    surfaceContainer = Color(0xFF262626),
    onSurfaceVariant = Color(0xFFA8A8A8),
    outline = NkPainterColors.ControlBorder,
    error = Color(0xFFFF7A6E),
    onError = Color(0xFF1C1C1C),
)

@Composable
fun NkPainterTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colors, content = content)
}
