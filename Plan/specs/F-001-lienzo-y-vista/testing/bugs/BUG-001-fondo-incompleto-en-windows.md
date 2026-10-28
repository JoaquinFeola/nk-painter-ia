# BUG-001 · En Windows, el fondo de color pinta solo una parte del lienzo
state: verified
found: 2026-10-28 · escenarios US-001 E-11, E-12, E-14 · historia US-001 · reglas RN-fondo-inicial, RN-obra-nueva-lista

## Qué pasa
En el editor de Windows, el contorno del lienzo aparece entero y centrado, pero el fondo de
color (blanco, o el color elegido) cubre solo un rectángulo pegado a la esquina superior
izquierda del lienzo. El resto del lienzo se ve del gris del editor. El tamaño del rectángulo
varía con el lienzo y no sigue su proporción:

| Lienzo | Contorno en pantalla (px) | Parte con fondo (px, aprox.) |
|---|---|---|
| A4 2480×3508 | 677 × 952 | 403 × 258 |
| Full HD 1920×1080 | 1482 × 836 | 1146 × 737 |
| 5000×8000 | ≈ 597 × 952 | ≈ 90 × 110 |
| 12000×16000 | ≈ 717 × 952 | ≈ 90 × 50 |

El fondo transparente (damero) sí cubre el lienzo entero (US-001 E-13 pasó). En Android el
fondo cubre todo el lienzo en todos los escenarios.

## Esperado
- US-001 CA-11: al crear una A4 sin elegir fondo, el fondo del lienzo es blanco (#FFFFFF), en todo el lienzo.
- US-001 CA-12: con fondo #F5E6C8, el fondo del lienzo es #F5E6C8.
- US-001 CA-14: la obra nueva se ve entera.

## Cómo reproducirlo
1. Windows, `gradlew :desktopApp:run`, monitor de 2560×1440 al 125 %, ventana de 1500×1000 px.
2. En Obra nueva, dejar A4 y Blanco elegidos; Crear.
3. El editor muestra el contorno de la A4 de arriba a abajo, y blanco solo en su esquina superior izquierda.
Evidencia: `evidence/2026-10-28/windows/US-001-E-01-E-11-E-14-editor.png`,
`US-001-E-01-editor-again.png` (3 s después, igual), `US-001-E-02-fullhd.png`,
`US-001-E-12-editor.png`, `US-002-E-12c.png`, `US-002-E-13.png`.

## Causa sospechada (sin confirmar)
El rectángulo del fondo sólido se dibuja con un tamaño que no es el del lienzo en pantalla:
quizá la escala de la vista o el escalado de Windows (125 %) se aplican dos veces, o no se
aplican, solo en ese trazo. El damero usa otro camino y sale bien. **No confirmado: no se
leyó el código del pintor de Windows durante la prueba.**

## Historia
- 2026-10-28 · abierto · pending
- 2026-10-28 · build (unidad 108) informa el arreglo: el fondo sólido se dibujaba con el tamaño de la composable en coordenadas del lienzo; ahora con el del lienzo · fixed, pending verification
- 2026-10-28 · re-corrida de US-001 E-11, E-12, E-14 en Windows: passed · verified
