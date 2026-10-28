# Windows stack — nk-painter
component: nk-painter (`./`) · area: windows

| Axis | Choice | When |
|---|---|---|
| Runtime | JVM 17+ recortada con jlink, dentro del instalador (QT-05) | — |
| Language | Kotlin 2.4.21 (QT-45) | — |
| UI framework | Compose Multiplatform 1.12.2 con Material 3 (QT-47) | toda la UI, compartida entre android y windows; Material 3 es la base del sistema de diseño |
| Rendering | motor Kotlin común: mezcla los píxeles con aritmética entera en mosaicos de 256×256; Skia solo los muestra | todo lo que pinta el lienzo (QT-11, QT-09, QT-46) |
| Architecture | vertical-slice | una carpeta por feature (F-001…F-009) en el código compartido |
| Domain model | ddd | la obra es el agregado que hace cumplir sus reglas |
| Persistence | repositories | una interfaz por agregado, implementación de archivos por plataforma |
| Database | ninguna | todo se guarda como archivos locales |
| Tests | kotlin.test | los tests de `shared/src/commonTest` corren en la JVM con `:shared:desktopTest`; lo que pinta el editor de Windows se prueba fuera de pantalla con `ImageComposeScene` en `:desktopApp:test` |
| Pen input | JNA sobre la Pointer API de Windows (WM_POINTER), reemplazando el procedimiento de ventana del marco y de sus ventanas hijas; el mouse llega por Compose | presión, tipo de puntero y borrador (ADR-001, QT-24, QT-48) |
| Packaging | instalador .msi del plugin de Compose | distribución a mano (Q-11, QT-16) |

## Opt-ins
- **DDD:** aggregates yes · value objects yes · domain events no · typed ids no
- **Test levels:** unit yes · integration against the real engine: yes (disco real del equipo) · ephemeral containers: no · in-process API: no
- **Test-first:** yes, for business rules (RN-…)
- **Automated end-to-end tests:** no
- **Test data:** generados por código en los tests, más un juego de archivos de proyecto de muestra (incluida la obra de referencia A4 a 300 dpi con 30 capas) para QA y mediciones. En QA de pantalla, los datos se crean solo usando la app, sin escribir sus archivos a mano (QT-78); para probar "la primera vez", se borran los datos de la app (Android, `pm clear` en el emulador) o se aparta su archivo de preferencias y se devuelve al final (Windows) (QT-108)

## Packages
| Package | What for | When to use it |
|---|---|---|
| Kotlin Multiplatform, Kotlin JVM y Compose Compiler 2.4.21 | compilar el código compartido, `desktopApp` y la UI | siempre |
| Compose Multiplatform 1.12.2 (runtime, foundation, ui, desktop) | toda la UI y el instalador | siempre |
| net.java.dev.jna:jna-platform 5.19.1 (trae jna) | llamar a user32 para el lápiz | solo en `desktopApp/pens` |
| kotlinx-serialization-json 1.12.0 (con su plugin 2.4.21) | el manifiesto del archivo de proyecto | `shared/projectfiles` |
| kotlinx-coroutines-core 1.11.0 | trabajo en segundo plano (guardar, exportar) | código compartido; agregado, todavía sin uso |
| org.jetbrains.compose.material3:material3 1.9.0 | componentes y tema de Material 3 (QT-47) | toda la UI, en `shared`, con el tema `NkPainterTheme`; probada con Compose 1.12.2 en la JVM y en el emulador de Android |
| kotlin-test 2.4.21 | tests | solo en tests |

## Planned
- Nada por ahora.

## Conventions
- Módulos: `shared` (código compartido, objetivos android y desktop; `jvmSharedMain` tiene lo que usa java.io y java.util.zip, común a las dos), `androidApp` (entrada de Android), `desktopApp` (entrada e instalador de Windows).
- El código compartido tiene una carpeta por feature, en plural (`canvases` para F-001), más `themes` (el tema de Material 3) y `states` (lo que vive más que una pantalla, como la obra abierta). La app de cada plataforma solo pone su editor y lo propio del equipo.
- `-PlatencyLog` imprime una línea por trazo con la demora de la app, del mensaje de puntero (o el evento del mouse) al cuadro que lo dibuja: cuadros, promedio, p95 y máximo, como `nkpainter-latency` en Android (QT-92).
- QA fija el límite del equipo al lanzar: en Android con `--el warnPixels … --el redLinePixels …`, en Windows con `-PwarnPixels=… -PredLinePixels=…` (QT-63).
- Convenciones oficiales de Kotlin y de Compose; NZT no tiene guía de build para Kotlin (QT-20).
- El código compartido no usa APIs de una plataforma; lo propio de cada una va detrás de una interfaz común.
- La app no declara permiso de internet ni lo usa (Q-03).
- Los nombres de código salen del glosario (`Docs/Domain/glossary.md`); un término que falta vuelve a discovery.
- El instalador se arma en Windows; no hay compilación cruzada (QT-16).
Versiones leídas el 2026-10-28 de la página de releases de Compose Multiplatform. Versiones fijadas en `gradle/libs.versions.toml` (QT-45), con Gradle 9.5.1 y JDK 17.
