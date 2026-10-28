import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.shared)
    implementation(compose.desktop.currentOs)
    implementation(libs.jna.platform)
    testImplementation(libs.kotlin.test)
}

compose.desktop {
    application {
        mainClass = "com.nkpainter.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "nk-painter"
            packageVersion = "0.1.0"
            windows {
                perUserInstall = true
                menu = true
            }
        }
    }
}

// `-PpenProbe` injects a synthetic pen stroke at start-up and prints what the app received.
tasks.withType<JavaExec>().configureEach {
    if (project.hasProperty("penProbe")) systemProperty("nkpainter.penProbe", "true")
    // `-Pbenchmark` measures the reference artwork instead of opening the window.
    if (project.hasProperty("benchmark")) systemProperty("nkpainter.benchmark", "true")
    // `-PwarnPixels=… -PredLinePixels=…` fix this PC's canvas limit, so QA can test the warning and the red line.
    project.findProperty("warnPixels")?.let { systemProperty("nkpainter.warnPixels", it) }
    project.findProperty("redLinePixels")?.let { systemProperty("nkpainter.redLinePixels", it) }
    // `-PpenLog` prints one line per stroke: the tool Windows reported (pen, eraser, touch) and the pressure range.
    if (project.hasProperty("penLog")) systemProperty("nkpainter.penLog", "true")
    // `-PlatencyLog` prints one line per stroke: the time from each pen or mouse input to the frame that drew it.
    if (project.hasProperty("latencyLog")) systemProperty("nkpainter.latencyLog", "true")
}
