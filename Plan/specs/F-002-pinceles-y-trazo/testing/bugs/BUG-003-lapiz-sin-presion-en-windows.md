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
- **Causa sospechada, no confirmada:** la entrada de Windows toma el campo de presión aunque el lápiz
  no marque que lo informa (`PEN_MASK_PRESSURE`), y lee 0.
- Probado con el lápiz sintético de Windows (`InjectSyntheticPointerInput` con `penMask` en 0), que es
  el medio que aprobó el plan (QT-108). No se probó con un lápiz físico sin presión.

## Historial
- 2026-10-28 · abierto · pending
