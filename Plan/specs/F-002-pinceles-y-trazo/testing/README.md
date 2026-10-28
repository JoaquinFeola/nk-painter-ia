# Pruebas de F-002

| Historia | Archivo | Última ejecución | Resultado |
|---|---|---|---|
| US-005 | `US-005-dibujar-con-pincel.md` | 2026-10-28 (Android completa + re-corrida de E-13; Windows completa en la corrida 3) | Android: 10 passed (E-13 en la re-corrida), 3 blocked · Windows: 9 passed (E-12 parcial), 1 failed (E-13, BUG-002), 3 blocked |
| US-007 | `US-007-presion.md` | 2026-10-28 (Android lo del agente; Windows completa) | Android: 2 passed, 2 blocked, 8 pendientes del usuario · Windows: 9 passed, 2 failed (E-02 por Q-109, E-12 por BUG-003) |

- **Bugs abiertos:** BUG-001 (el final del trazo sale recto), fixed, pending verification: ya no aparece en ninguna de las dos áreas, pero E-13 de Windows falló por BUG-002 y se verifica cuando pase entero. BUG-002 (costuras entre mosaicos en Windows con zoom menor a 100 %), pending. BUG-003 (en Windows un lápiz sin presión dibuja con la mínima), pending.
- **Preguntas abiertas:** Q-109 (cómo se mide el ancho de un pincel de borde difuso; frena US-007 CA-02).
- **Criterios sin escenario:** US-007 CA-10 en Windows (el dedo no pinta hasta F-009, QT-102).
- **Blocked de antemano:** US-005 CA-06 (F-003), CA-10 y CA-11 (F-004); US-007 CA-11 y CA-12 en Android (sin mouse ni lápiz sin presión a mano).
- **Automatizado:** nada de punta a punta; las reglas tienen tests del código común (`BrushStrokeTest`, `PressureTest`, `DrawingStateTest`) y del archivo de preferencias (build).
- **Datos dejados atrás:** tamaños en los recientes (400×300, 30×30, 600×600, 1400×1400 en Android; 400×300 en Windows); `drawing-settings.txt` en Windows lo creó la app (no había uno antes); en la corrida 3 la Tinta se llevó de 476 a 8 y se devolvió a 476. Windows suma en los recientes 30×30, 600×600, 1400×1400 y 1200×600.
- **Mecanismo de datos:** usando la app (QT-78), más US-005 E-02: `pm clear` en el emulador y apartar `drawing-settings.txt` en Windows (QT-108).
- **Cómo se maneja cada área:** Android, emulador por `adb`; Windows, mouse y teclado sintéticos (QT-79) y lápiz sintético con presión (`tools/inject-pen.ps1`, QT-108). Presión en Android: el usuario en su tableta (QT-90).
