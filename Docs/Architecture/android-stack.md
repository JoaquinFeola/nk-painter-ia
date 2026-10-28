# Android stack — nk-painter
component: nk-painter (`./`) · area: android

| Axis | Choice | When |
|---|---|---|
| Runtime | Android 10 (API 29) como mínimo (QT-15); compila contra API 37, que pide Compose 1.12 | — |
| Language | Kotlin 2.4.21 (QT-45) | — |
| UI framework | Compose Multiplatform 1.12.2 con Material 3 (QT-47) | toda la UI, compartida entre android y windows; Material 3 es la base del sistema de diseño |
| Rendering | motor Kotlin común: mezcla los píxeles con aritmética entera en mosaicos de 256×256; Skia y los bitmaps de la plataforma solo los muestran | todo lo que pinta el lienzo (QT-11, QT-09, QT-46) |
| Architecture | vertical-slice | una carpeta por feature (F-001…F-009) en el código compartido |
| Domain model | ddd | la obra es el agregado que hace cumplir sus reglas |
| Persistence | repositories | una interfaz por agregado, implementación de archivos por plataforma |
| Database | ninguna | todo se guarda como archivos locales |
| Tests | kotlin.test | los tests de `shared/src/commonTest` corren en la JVM con `:shared:desktopTest` |
| Low-latency stroke | `CanvasFrontBufferedRenderer` de graphics-core sobre un SurfaceView | el trazo en curso; al levantar el lápiz se pinta en los mosaicos (QT-07) |

## Opt-ins
- **DDD:** aggregates yes · value objects yes · domain events no · typed ids no
- **Test levels:** unit yes · integration against the real engine: yes (disco real del equipo) · ephemeral containers: no · in-process API: no
- **Test-first:** yes, for business rules (RN-…)
- **Automated end-to-end tests:** no
- **Test data:** generados por código en los tests, más un juego de archivos de proyecto de muestra (incluida la obra de referencia A4 a 300 dpi con 30 capas) para QA y mediciones. En QA de pantalla, los datos se crean solo usando la app, sin escribir sus archivos a mano (QT-78); para probar "la primera vez", se borran los datos de la app (Android, `pm clear` en el emulador) o se aparta su archivo de preferencias y se devuelve al final (Windows) (QT-108)

## Packages
| Package | What for | When to use it |
|---|---|---|
| Kotlin Multiplatform y Compose Compiler 2.4.21 | compilar el código compartido y la UI | siempre |
| Android Gradle Plugin 9.3.1 | `androidApp` (`com.android.application`) y `shared` (`com.android.kotlin.multiplatform.library`) | siempre; con AGP 9 la app y el código compartido van en módulos separados (QT-45) |
| Android NDK 27.1.12297006 y CMake 3.22.1 | la biblioteca en C que reserva la memoria de los mosaicos fuera de la memoria de Java (QT-52) | solo en `nativememory` (`com.android.library`): el plugin de `shared` no compila código nativo |
| Compose Multiplatform 1.12.2 (runtime, foundation, ui) | toda la UI | siempre |
| androidx.activity:activity-compose 1.13.0 | la Activity que muestra la UI de Compose | solo en `androidApp` |
| androidx.graphics:graphics-core 1.0.4 | trazo de baja demora en el front buffer | solo en `androidApp`, en la superficie de dibujo |
| kotlinx-serialization-json 1.12.0 (con su plugin 2.4.21) | el manifiesto del archivo de proyecto | `shared/projectfiles` |
| kotlinx-coroutines-core 1.11.0 | trabajo en segundo plano (guardar, exportar) | código compartido; agregado, todavía sin uso |
| org.jetbrains.compose.material3:material3 1.9.0 | componentes y tema de Material 3 (QT-47) | toda la UI, en `shared`, con el tema `NkPainterTheme`; probada con Compose 1.12.2 en la JVM y en el emulador de Android |
| kotlin-test 2.4.21 | tests | solo en tests |

## Planned
- androidx.input:input-motionprediction: predicción de trazo, si la medición de QT-08 la pide. Sin confirmar.

## Conventions
- Módulos: `shared` (código compartido, objetivos android y desktop; `jvmSharedMain` tiene lo que usa java.io y java.util.zip, común a las dos), `androidApp` (entrada de Android), `desktopApp` (entrada e instalador de Windows), `nativememory` (solo Android: reserva y libera memoria con `calloc`/`free` por JNI; lo usa `shared/androidMain`).
- Los píxeles de un mosaico viven en `TilePixels`: en Android, memoria nativa que se libera solo con `TileGrid.release()`; en Windows, un `ByteBuffer` directo que libera el recolector. Quien descarta una capa la libera (QT-52).
- El código compartido tiene una carpeta por feature, en plural (`canvases` para F-001), más `themes` (el tema de Material 3) y `states` (lo que vive más que una pantalla, como la obra abierta). La app de cada plataforma solo pone su editor y lo propio del equipo.
- QA fija el límite del equipo al lanzar: en Android con `--el warnPixels … --el redLinePixels …`, en Windows con `-PwarnPixels=… -PredLinePixels=…` (QT-63).
- Convenciones oficiales de Kotlin y de Compose; NZT no tiene guía de build para Kotlin (QT-20).
- El código compartido no usa APIs de una plataforma; lo propio de cada una va detrás de una interfaz común.
- La app no declara permiso de internet ni lo usa (Q-03).
- Los nombres de código salen del glosario (`Docs/Domain/glossary.md`); un término que falta vuelve a discovery.
- El rechazo de palma usa el tipo de herramienta del evento y descarta los punteros cancelados (QT-07).
Versiones leídas el 2026-10-28 de la página de releases de Compose Multiplatform. Versiones fijadas en `gradle/libs.versions.toml` (QT-45), con Gradle 9.5.1 y JDK 17.
