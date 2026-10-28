# BUG-003 · En Windows, un lápiz que no informa presión dibuja con la presión mínima
state: pending
found: 2026-10-28 · escenario E-12 · historia US-007 · criterio "Un lápiz sin presión dibuja a presión completa" (CA-12, RN-presion-segun-pincel)

## Qué pasa
Con el Pincel redondo (grosor 20), una línea hecha con un lápiz que no informa presión sale gris y de
2,6 px, que es lo que deja la presión mínima. El registro del lápiz (`-PpenLog`) dice
`tool=Pen samples=42 pressure=0.000..0.000`.

## Esperado
La línea mide 20 px (±1): un lápiz sin presión dibuja a presión completa (US-007 CA-12).

## Cómo reproducir
1. Windows, `:desktopApp:run -PpenLog`, obra de 1200×600, Pincel redondo, curva Normal.
2. `tools/inject-pen.ps1 -NoPressure` con una línea de (600, 260) a (1000, 260) del lienzo.
3. La línea sale gris y de 2,6 px; el registro muestra presión 0.
Evidencia: `evidence/2026-10-28/windows/w-US007-E05-E12-detalle.png` (la línea de la derecha).

## Lo que no se sabe
- ~~Causa sospechada: la entrada de Windows toma el campo de presión aunque el lápiz no marque que lo
  informa.~~ **Descartada** al investigar (unidad 118): la app ya usa presión completa cuando falta
  `PEN_MASK_PRESSURE` (`WindowsPenInput.kt`).
- **Hecho observado (2026-10-28):** con una línea temporal que imprimía lo que la app recibe, el trazo
  inyectado con `penMask` en 0 llegó con `penMask=15` (presión, rotación e inclinaciones) y presión 0; el
  inyectado con presión llegó con `penMask=15` y 512. Windows le pone la máscara completa al lápiz
  sintético, así que **con esta herramienta no se puede simular un lápiz sin presión**. No está
  confirmado en la documentación de Windows; es lo que se observó en esta PC.
- Probado con el lápiz sintético de Windows (`InjectSyntheticPointerInput` con `penMask` en 0), que es
  el medio que aprobó el plan (QT-108). No se probó con un lápiz físico sin presión.

## Historial
- 2026-10-28 · abierto · pending
- 2026-10-28 · investigado en la unidad 118: lo que falla es la simulación, no se encontró defecto en la app · sigue pending hasta que se decida (QT-114)
