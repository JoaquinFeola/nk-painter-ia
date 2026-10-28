package com.nkpainter.brushes

import java.io.File
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileDrawingSettingsStoreTest {
    private val folder: File = Files.createTempDirectory("drawing-settings").toFile()
    private val file = File(folder, "drawing-settings.txt")

    @AfterTest
    fun deleteFolder() {
        folder.deleteRecursively()
    }

    @Test
    fun withNoFileTheFactorySettingsApply() {
        assertEquals(DrawingSettings.FACTORY, FileDrawingSettingsStore(file).load())
    }

    @Test
    fun theSavedActiveBrushIsReadBackByANewStore() {
        FileDrawingSettingsStore(file).save(DrawingSettings(activeBrush = Brush.INK))

        assertEquals(Brush.INK, FileDrawingSettingsStore(file).load().activeBrush)
    }

    @Test
    fun theSavedCurveIsReadBackByANewStore() {
        FileDrawingSettingsStore(file).save(DrawingSettings(activeBrush = Brush.INK, pressureCurve = PressureCurve.FIRM))

        assertEquals(DrawingSettings(activeBrush = Brush.INK, pressureCurve = PressureCurve.FIRM), FileDrawingSettingsStore(file).load())
    }

    @Test
    fun anUnreadableCurveFallsBackAloneAndKeepsTheBrush() {
        file.writeText("activeBrush=INK\npressureCurve=WOBBLY")

        assertEquals(DrawingSettings(activeBrush = Brush.INK, pressureCurve = PressureCurve.NORMAL), FileDrawingSettingsStore(file).load())
    }

    @Test
    fun eachBrushsSettingsAreReadBackByANewStore() {
        val adjusted = DrawingSettings.FACTORY.brushSettings + (Brush.PENCIL to BrushSettings(12, 70, 2))
        FileDrawingSettingsStore(file).save(DrawingSettings(activeBrush = Brush.PENCIL, brushSettings = adjusted))

        assertEquals(BrushSettings(12, 70, 2), FileDrawingSettingsStore(file).load().settingsOf(Brush.PENCIL))
    }

    @Test
    fun aSettingThatIsNotANumberFallsBackAloneAndAnOutOfRangeOneStaysAtItsEnd() {
        file.writeText("activeBrush=INK\nbrush.INK.size=grueso\nbrush.INK.opacity=900\nbrush.INK.stabilizer=5")

        assertEquals(BrushSettings(8, 100, 5), FileDrawingSettingsStore(file).load().settingsOf(Brush.INK))
    }

    @Test
    fun theEraserShapeAndSettingsAreReadBackByANewStore() {
        val saved = DrawingSettings(activeBrush = Brush.INK, eraserShape = Brush.AIRBRUSH, eraserSettings = BrushSettings(80, 60, 2))
        FileDrawingSettingsStore(file).save(saved)

        assertEquals(saved, FileDrawingSettingsStore(file).load())
    }

    @Test
    fun anUnreadableFileGivesTheFactorySettings() {
        file.writeText("activeBrush=CRAYON\nnonsense")

        assertEquals(DrawingSettings.FACTORY, FileDrawingSettingsStore(file).load())
    }
}
