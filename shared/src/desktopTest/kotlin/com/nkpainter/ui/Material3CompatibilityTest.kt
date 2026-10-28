package com.nkpainter.ui

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.use
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Material 3 1.9.0 is older than Compose 1.12.2 (QT-47). Rendering a themed button off screen
 * catches a binary mismatch, which only shows when the code runs.
 */
class Material3CompatibilityTest {
    @Test
    fun aThemedButtonRendersWithThisComposeVersion() {
        val colors = darkColorScheme()
        val pixels = ImageComposeScene(width = 200, height = 80).use { scene ->
            scene.setContent {
                MaterialTheme(colorScheme = colors) {
                    Button(onClick = {}) { Text("Crear") }
                }
            }
            scene.render().peekPixels()!!.buffer.bytes
        }
        assertTrue(pixels.any { it != 0.toByte() }, "the button left the image blank")
    }
}
