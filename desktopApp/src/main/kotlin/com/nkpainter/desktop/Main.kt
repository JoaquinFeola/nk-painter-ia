package com.nkpainter.desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.nkpainter.desktop.canvases.DesktopCanvas
import com.nkpainter.desktop.canvases.DesktopPainter
import com.nkpainter.desktop.memories.DesktopCanvasLimit
import com.nkpainter.desktop.memories.DesktopMemoryReport
import com.nkpainter.desktop.pens.PenPhase
import com.nkpainter.desktop.pens.PenSample
import com.nkpainter.desktop.pens.PenStrokeLog
import com.nkpainter.desktop.pens.PenTool
import com.nkpainter.desktop.pens.PointerApi
import com.nkpainter.desktop.pens.SyntheticPenProbe
import com.nkpainter.desktop.pens.WindowsPenInput
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.nkpainter.NkPainterApp
import com.nkpainter.benchmarks.ReferenceBenchmark
import com.nkpainter.brushes.DrawingState
import com.nkpainter.brushes.FileDrawingSettingsStore
import com.nkpainter.canvases.AreaSize
import com.nkpainter.canvases.EditorScreen
import com.nkpainter.canvases.Finger
import com.nkpainter.canvases.ViewNavigation
import com.nkpainter.canvases.FileRecentCanvasSizeStore
import com.nkpainter.states.ArtworkSession
import java.awt.Point
import java.io.File
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReference
import javax.swing.SwingUtilities

private const val APP_DATA_FOLDER = "nk-painter"
private const val RECENT_SIZES_FILE = "recent-canvas-sizes.txt"
private const val DRAWING_SETTINGS_FILE = "drawing-settings.txt"
private const val PEN_PROBE_PROPERTY = "nkpainter.penProbe"
private const val BENCHMARK_PROPERTY = "nkpainter.benchmark"
private const val PEN_LOG_PROPERTY = "nkpainter.penLog"
private const val LATENCY_LOG_PROPERTY = "nkpainter.latencyLog"
private const val PROBE_START_OFFSET_PX = 100

fun main() {
    if (System.getProperty(BENCHMARK_PROPERTY).toBoolean()) return runBenchmark()
    runApp()
}

// Running out of memory is one of the results this measures, so it is reported, not rethrown.
private fun runBenchmark() {
    val work = File(System.getProperty("java.io.tmpdir"))
    runCatching { ReferenceBenchmark(work, DesktopMemoryReport::describe, ::println).run() }
        .onFailure { println("benchmark: FAILED ${it::class.simpleName}: ${it.message} · ${DesktopMemoryReport.describe()}") }
}

private fun runApp() = application {
    val session = remember { ArtworkSession() }
    val recentStore = remember { FileRecentCanvasSizeStore(File(appDataFolder(), RECENT_SIZES_FILE)) }
    val drawing = remember { DrawingState(FileDrawingSettingsStore(File(appDataFolder(), DRAWING_SETTINGS_FILE))) }
    val latencyLog = remember { if (System.getProperty(LATENCY_LOG_PROPERTY).toBoolean()) { line: String -> println("latency: $line") } else null }
    val canvasLimit = remember {
        DesktopCanvasLimit.read().also {
            println("canvas limit: warning ${it.warningPixels} px, red line ${it.redLinePixels} px, max direct memory ${DesktopCanvasLimit.maxDirectMemory()} bytes")
        }
    }
    // The pen arrives on its own thread before any artwork exists; it paints on whatever is open then.
    val openPainter = remember { AtomicReference<DesktopPainter?>(null) }
    val openNavigation = remember { AtomicReference<ViewNavigation?>(null) }
    val received = remember { CopyOnWriteArrayList<PenSample>() }
    val strokeLog = remember { if (System.getProperty(PEN_LOG_PROPERTY).toBoolean()) PenStrokeLog(::println) else null }
    val penInput = remember {
        WindowsPenInput { sample ->
            received += sample
            strokeLog?.record(sample)
            SwingUtilities.invokeLater {
                val painter = openPainter.get() ?: return@invokeLater
                // Touch only moves the view until a finger can paint without a pen (F-009).
                if (sample.tool == PenTool.Touch) openNavigation.get()?.let { navigate(it, painter, sample) } else paint(painter, sample)
            }
        }
    }

    Window(onCloseRequest = ::exitApplication, title = "nk-painter", state = rememberWindowState(width = 1200.dp, height = 800.dp)) {
        LaunchedEffect(window) {
            // Compose creates its child window when the frame opens; subclassing earlier misses it.
            window.addWindowListener(object : WindowAdapter() {
                override fun windowOpened(event: WindowEvent) {
                    penInput.attach(WinDef.HWND(Pointer(window.windowHandle)))
                    window.toFront()
                    if (System.getProperty(PEN_PROBE_PROPERTY).toBoolean()) {
                        val origin = window.contentPane.locationOnScreen
                        Thread { runProbe(penInput, received, origin) }.start()
                    }
                }
            })
        }
        NkPainterApp(session, recentStore, canvasLimit, screenSize = { screenSizeOf(window) }) { artwork ->
            val painter = remember(artwork) { DesktopPainter(artwork, drawing, latencyLog).also(openPainter::set) }
            val navigation = remember(artwork) { ViewNavigation(artwork).also(openNavigation::set) }
            EditorScreen(artwork, navigation, drawing) { DesktopCanvas(painter) }
        }
    }
}

private fun appDataFolder(): File = File(System.getenv("APPDATA") ?: System.getProperty("user.home"), APP_DATA_FOLDER)

/** The monitor the window is on, in its own pixels. Whether Windows scaling changes this is QT-60. */
private fun screenSizeOf(window: java.awt.Window): AreaSize? {
    val mode = window.graphicsConfiguration?.device?.displayMode ?: return null
    return AreaSize(mode.width, mode.height)
}

private fun paint(painter: DesktopPainter, sample: PenSample) {
    val point = painter.inDrawingArea(sample.point)
    when (sample.phase) {
        PenPhase.Down -> painter.startStroke(point, sample.receivedAtNanos)
        PenPhase.Move -> painter.continueStroke(point, sample.receivedAtNanos)
        PenPhase.Up -> painter.endStroke()
    }
}

private fun navigate(navigation: ViewNavigation, painter: DesktopPainter, sample: PenSample) {
    val point = painter.inDrawingArea(sample.point)
    val finger = Finger(sample.pointerId, point.x, point.y)
    when (sample.phase) {
        PenPhase.Down -> navigation.press(finger)
        PenPhase.Move -> navigation.move(listOf(finger), painter.drawingArea)
        PenPhase.Up -> navigation.release(sample.pointerId)
    }
}

private fun runProbe(penInput: WindowsPenInput, received: List<PenSample>, contentOrigin: Point) {
    val probe = SyntheticPenProbe()
    val (penSize, typeSize) = probe.structSizes()
    println("probe: struct sizes pen=$penSize (expected ${SyntheticPenProbe.EXPECTED_STRUCT_SIZE_PEN}) type=$typeSize (expected ${SyntheticPenProbe.EXPECTED_STRUCT_SIZE_TYPE})")
    println("probe: subclassed windows=${penInput.subclassedWindowCount}")
    val start = WinDef.POINT(contentOrigin.x + PROBE_START_OFFSET_PX, contentOrigin.y + PROBE_START_OFFSET_PX)
    val target = PointerApi.instance.WindowFromPoint(WinDef.POINT.ByValue(start.x, start.y))
    println("probe: window under start=${target?.pointer} subclassed=${target?.let(penInput::isSubclassed)} foreground=${User32.INSTANCE.GetForegroundWindow()?.pointer}")
    val injected = probe.injectStroke(start)
    Thread.sleep(500)
    println("probe: injected=$injected pointerMessages=${penInput.pointerMessageCount} received=${received.size} error=${Kernel32.INSTANCE.GetLastError()}")
    received.forEach { println("probe: ${it.phase} ${it.tool} x=${it.point.x} y=${it.point.y} pressure=${it.point.pressure}") }
    println("probe: done")
}
