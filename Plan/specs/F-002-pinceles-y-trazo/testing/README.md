# Pruebas de F-002

| Historia | Archivo | Última ejecución | Resultado |
|---|---|---|---|
| US-005 | `US-005-dibujar-con-pincel.md` | 2026-10-28 (Android completa; Windows interrumpida) | Android: 9 passed, 1 failed (E-13), 3 blocked · Windows: 1 passed, 12 pending |
| US-007 | `US-007-presion.md` | 2026-10-28 (solo Android, lo del agente) | Android: 2 passed, 2 blocked, 8 pendientes del usuario · Windows: sin correr |

- **Bugs abiertos:** BUG-001 (el final del trazo sale recto), pending.
- **Criterios sin escenario:** US-007 CA-10 en Windows (el dedo no pinta hasta F-009, QT-102).
- **Blocked de antemano:** US-005 CA-06 (F-003), CA-10 y CA-11 (F-004); US-007 CA-11 y CA-12 en Android (sin mouse ni lápiz sin presión a mano).
- **Automatizado:** nada de punta a punta; las reglas tienen tests del código común (`BrushStrokeTest`, `PressureTest`, `DrawingStateTest`) y del archivo de preferencias (build).
- **Datos dejados atrás:** tamaños en los recientes (400×300, 30×30, 600×600, 1400×1400 en Android; 400×300 en Windows); `drawing-settings.txt` en Windows lo creó la app (no había uno antes).
- **Mecanismo de datos:** usando la app (QT-78), más US-005 E-02: `pm clear` en el emulador y apartar `drawing-settings.txt` en Windows (QT-108).
- **Cómo se maneja cada área:** Android, emulador por `adb`; Windows, mouse y teclado sintéticos (QT-79) y lápiz sintético con presión (`tools/inject-pen.ps1`, QT-108). Presión en Android: el usuario en su tableta (QT-90).
