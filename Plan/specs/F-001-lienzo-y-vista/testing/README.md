# Pruebas de F-001

| Historia | Archivo | Última ejecución | Resultado |
|---|---|---|---|
| US-001 | `US-001-crear-obra.md` | 2026-10-28 (re-corrida en Windows) | pasa en Android y Windows; CA-14 "Capa 1" blocked hasta F-003; E-16 blocked |
| US-002 | `US-002-lienzo-invalido-o-grande.md` | 2026-10-28 | pasa entera en Android y Windows |
| US-003 | `US-003-navegar-vista.md` | 2026-10-28 (unidad 112) | CA-01 a CA-09 pasan en las dos; CA-10 a CA-12 blocked; CA-13 sin escenario |
| US-004 | `US-004-cambiar-fondo.md` | 2026-10-28 (unidad 112) | CA-01 a CA-03 y la parte visible de CA-06 pasan en las dos; CA-04, CA-05, CA-07 blocked |

- **Bugs abiertos:** ninguno. BUG-001, BUG-002 y BUG-003 verificados el 2026-10-28 (BUG-002 con el límite que dice su archivo).
- **Criterios sin escenario:** US-003 CA-13 (fluidez: niveles reducidos sin construir; la mide la unidad 20). US-001 CA-14, la parte de "Capa 1" activa: blocked hasta F-003.
- **Automatizado:** nada de punta a punta; las reglas tienen tests del código común, y el editor de Windows tiene `DesktopCanvasBackgroundTest` y `DesktopCanvasBoundsTest` (build).
- **Datos dejados atrás:** los recientes del emulador y de esta PC quedan con tamaños de prueba: la app no tiene cómo borrarlos y QT-78 descarta tocar el archivo. Windows ya tenía `2480 3508 300` antes de la corrida.
- **Huecos:** US-001 E-03 con el monitor en vertical (Windows), sin correr; US-001 E-16 blocked (QT-78); los tres que quedaron sin correr en Windows (US-003 E-02, US-004 E-03, E-06 a 700 px) se corrieron en la unidad 112 y pasan.
- **Mecanismo de datos:** solo usando la app (QT-78). US-001 E-16 blocked por eso.
- **Cómo se maneja cada área:** Android, emulador por `adb`; Windows, capturas y mouse/teclado
  sintéticos desde PowerShell (QT-79, aprobado). Dos dedos: consola del emulador (`adb emu event send`) y `InjectTouchInput` en Windows (QT-81).
