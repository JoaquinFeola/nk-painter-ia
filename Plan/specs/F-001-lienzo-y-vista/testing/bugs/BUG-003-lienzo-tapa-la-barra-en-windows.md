# BUG-003 · En Windows, el lienzo acercado o girado tapa la barra superior
state: verified
found: 2026-10-28 · escenarios US-003 E-01, E-05 · historia US-003 · pantalla `ux-ui/screen-editor.md` (barra superior "siempre visible")

## Qué pasa
Cuando el lienzo es más grande que el área de dibujo (acercado, o girado y desbordando), se
dibuja también encima de la barra superior: **Ajustar vista** y **Fondo** dejan de verse. Los
botones siguen ahí: un clic donde estaba Ajustar vista ajusta la vista y la barra vuelve a
verse. En Android no pasa: el lienzo queda debajo de la barra.

## Esperado
`screen-editor.md`, regiones: "Barra superior — fina, siempre visible". US-003 CA-08 se cumple
igual (Ajustar funciona), pero el ilustrador no ve dónde tocar.

## Cómo reproducirlo
1. Windows, `gradlew :desktopApp:run`, ventana de 1500×1000 px; A4 a 300 dpi, Crear.
2. Pellizcar abriendo con dos dedos hasta ≈76 %.
3. El lienzo blanco cubre toda la ventana, barra incluida.
Evidencia: `evidence/2026-10-28-u31/windows/US-003-E-01-despues.png`,
`US-003-E-01-dedos-abajo.png`, `BUG-003-ajustar-tapado.png` (después del clic a ciegas, la barra vuelve).

## Causa
Confirmada en la unidad 110: el `Canvas` de Compose no recorta a sus límites. Un test que
fallaba antes del arreglo (`DesktopCanvasBoundsTest`) queda como regresión.

## Causa sospechada en la apertura
El lienzo de Compose de Windows dibuja fuera de sus propios límites (no recorta al área de
dibujo), y la barra queda debajo. **No confirmado**.

## Impacto
Medio: con la vista acercada, las acciones de la barra quedan invisibles; se recuperan solo
sabiendo dónde estaban.

## Historia
- 2026-10-28 · abierto en la QA de la unidad 31.
- 2026-10-28 · arreglo en la unidad 110 · fixed, pending verification
- 2026-10-28 · re-corrida (unidad 112): E-01 al 76 % y E-05 girado, la barra visible · verified
