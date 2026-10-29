# BUG-002 · En Windows, con zoom menor a 100 %, el trazo muestra costuras claras en los bordes de mosaico
state: verified
found: 2026-10-28 · escenario E-13 · historia US-005 · criterio "Un trazo largo y rápido sale continuo" (CA-13, requisito no funcional de F-002)

## Qué pasa
En Windows, en una obra de 1400×1400 ajustada a la ventana (zoom 64 %), el círculo de la Tinta tiene
líneas claras de 1 px de pantalla que lo cruzan donde pasa por los bordes de mosaico x = 512 y
x = 1024 del lienzo. En esa columna de pantalla el punto más oscuro de la línea da 58 y 40 (en una
escala de 0 a 255), y a los costados da 0. La tinta no falta: es la línea la que se ve partida.

## Esperado
"La línea sale continua, sin cortes ni tramos rectos visibles" (US-005 CA-13).

## Cómo reproducir
1. Windows, `:desktopApp:run`, ventana de 1500×1000. Obra de 1400×1400, fondo blanco.
2. Tinta (grosor 8). Círculo de 1000 px con `tools/inject-pen.ps1` (61 puntos, cada 5 ms).
3. Ampliar la captura donde la línea cruza x ≈ 681 y x ≈ 1008 de la ventana.
Evidencia: `evidence/2026-10-28/windows/w-E13-01-circulo.png`, `w-E13-02-costuras-y-final.png`.

## Lo que no se sabe
- **Causa sospechada, no confirmada:** con zoom menor a 100 % cada mosaico se dibuja por separado con
  filtro (`DesktopCanvas.kt`, `FilterQuality.Low`), y el filtro mezcla el borde de cada mosaico con lo
  que queda afuera de él.
- No se vio en Android con la misma obra (zoom 96 %, también con filtro). En Windows con zoom de más
  de 100 % (obra de 1200×600 al 115 %, sin filtro) no se ve.
- Afecta a todo lo pintado, no solo a este trazo: es de cómo se muestra la vista (F-001) más que del pincel.

## Historial
- 2026-10-28 · abierto · pending
- 2026-10-28 · build informa el cambio de código (unidad 119, commit `3faea35`: los mosaicos se dibujan sin suavizado de bordes en `DesktopCanvas`; el test `DesktopCanvasTileSeamTest` falla antes y pasa después) · fixed, pending verification
- 2026-10-29 · re-corrida de E-13 en Windows passed (US-005, unidad 120): al 92 % las líneas que cruzan los bordes de mosaico 512 y 1024 llegan a negro, sin costuras · verified
