# Mediciones del esqueleto en los equipos del usuario (unidad 20)

Plan de medición de QT-05, QT-06, QT-08, QT-09, QT-11 y QT-33. Los resultados se pasan a
cada pregunta en `architecture-decisions.md`; acá queda el plan, cada corrida con su fecha y
sus capturas en `evidence/skeleton/<fecha>/`.

- **Estado del plan:** aprobado por el usuario · 2026-10-27.
- **Datos:** la obra de referencia la genera el código (`ReferenceArtwork`, A4 a 300 dpi,
  30 capas, 2017 mosaicos pintados, 504 MB sin comprimir). La medición borra el archivo que
  exporta al terminar; si se corta antes, queda `referencia.nkpainter` en la carpeta de caché
  de la app (Android) o en la carpeta temporal (Windows) y se borra a mano.
- **Equipos:** los de gama media del producto (Q-05): Android de 4 GB y PC de 8 GB.
  Cuáles tiene el usuario: QT-53.
- **Efectos:** ninguno fuera del equipo; la app no usa internet.

## Escenarios

| Id | Pregunta | Qué se hace | Equipo | Repeticiones | Se compara con | Lo corre |
|---|---|---|---|---|---|---|
| M-01 | QT-05 | Arrancar la app empaquetada (`./gradlew :desktopApp:createDistributable`, la que lleva el runtime recortado), esperar 10 s sin tocar, leer la memoria del proceso | PC de 8 GB | 5 arranques en frío | línea de base; la meta de memoria no tiene número (Q-05): pregunta abierta | usuario |
| M-02 | QT-09 | `./gradlew :desktopApp:run -Pbenchmark` | PC de 8 GB | 5 | pasa si termina y el equipo no se queda sin memoria; tiempos como línea de base | usuario |
| M-03 | QT-09, QT-52 | `adb shell am start -n com.nkpainter/.android.MainActivity --ez benchmark true`, log `nkpainter-benchmark` | Android de 4 GB | 5 | pasa si termina con `low memory false` y sin cierre; tiempos como línea de base | usuario |
| M-04 | QT-33 | Comparar los dos resúmenes (`layer digest`, `composite digest`) de M-02 y M-03 | ARM contra x86 | de M-02 y M-03 | pasa si los dos resúmenes son iguales en los dos equipos | agente, con los logs |
| M-05 | QT-08 | 20 trazos cortos con el lápiz y log `nkpainter-latency` (demora de la app, del evento al cuadro). Sin cámara lenta: la parte de la pantalla y el digitalizador no se mide (QT-53) | Android de 4 GB | 20 trazos | p95 de la demora de la app por debajo de 50 ms (QT-54); es un piso, no la meta de punta a punta | usuario |
| M-06 | QT-08 | Demora de punta a punta en Windows | PC de 8 GB con lápiz | — | `blocked`: sin cámara lenta y sin log de demora de la app en Windows (QT-53) | — |
| M-07 | QT-06, QT-48 | Lápiz físico en Windows, con `-PpenLog`: trazos con presión suave y fuerte (el rango de presión sale en el registro); escala de pantalla al 100 % y al 150 %, el trazo tiene que caer bajo la punta | PC con lápiz | 3 trazos por escala | pasa si el trazo se engrosa con la presión y cae bajo la punta en las dos escalas | usuario |
| M-08 | QT-06 | `./gradlew :desktopApp:run -PpenLog`; 3 trazos con la punta y 3 con la goma; cada trazo imprime `pen: stroke tool=… samples=… pressure=…` | PC con lápiz | 3 | pasa si llega como goma, y la punta como lápiz | usuario |
| M-09 | QT-11 | Combinar una pantalla Full HD de 30 capas (línea de M-02 y M-03) | los dos | de M-02 y M-03 | línea de base. Los 60 cuadros por segundo al navegar no se miden acá: navegar es F-001, y su diseño (unidad 22) no recombina por cuadro (QT-51) | — |

Cada tiempo se informa como p50 y p95 de sus repeticiones, con el equipo, la versión de
Android o de Windows y si fue en frío o en caliente.

## Preguntas de este plan

- **QT-53 · ¿Qué equipos y herramientas tenés para medir?**
  - usuario · 2026-10-27: Android de unos 4 GB con lápiz, PC de unos 8 GB, lápiz o tableta en Windows. Sin celular con cámara lenta.
  - consecuencia: la demora de punta a punta no se mide. M-05 mide solo la demora de la app en Android; M-06 queda `blocked` (sin cámara lenta). QT-08 sigue abierta en su parte de punta a punta.
- **QT-54 · ¿Cómo se decide la meta de 50 ms con 20 trazos?**
  - usuario · 2026-10-27: pasa si el p95 queda por debajo de 50 ms.
- **QT-55 · ¿Se agrega un registro del tipo de puntero en Windows para observar la goma?**
  - usuario · 2026-10-27: sí, una unidad de build chica (104) antes de correr el plan.
- **QT-56 · Mientras se esperan las mediciones, ¿se empieza el tramo 3?**
  - usuario · 2026-10-27: sí, empieza la unidad 21 (historias de F-001). La unidad 20 queda abierta y cierra con los logs; si un resultado cambia algo de F-001, se vuelve a la historia afectada.

## Corridas

Ninguna todavía.
