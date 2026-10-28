# BUG-002 · En Android, el lienzo dejó de redibujarse después de un pellizco
state: verified
found: 2026-10-28 · escenario US-003 E-01 (primera corrida) · historia US-003 · regla RN-zoom-centro-de-dedos

## Qué pasa
Después de pellizcar abriendo (×3), el indicador decía `163 % · 0°`, pero el lienzo en pantalla
quedó en una vista intermedia (≈1,2×) y no cambió más. Desde ahí, nada se redibujó: ni
**Ajustar vista**, ni otros arrastres de dos dedos, ni un trazo nuevo de un dedo (no apareció
ni el trazo en curso). El log de la app mostró, a la hora del pellizco:
`W BufferPool: Waiting for buffer to become available, current allocation count: 3`.

## Esperado
US-003 CA-01: la vista se acerca y el píxel bajo el centro de los dedos sigue ahí. Además, que la
pantalla muestre siempre la vista actual (screen-editor: "el lienzo sigue a los dedos").

## Cómo reproducirlo
**No se pudo reproducir a pedido.** Pasó una vez, en el emulador `Pixel_10_Pro_XL`, con la app
recién abierta, una A4 creada y, antes del pellizco, varios toques inyectados por la consola
del emulador (algunos sin presión, que no llegaron a pintar). En una app reabierta, el mismo
pellizco, un arrastre, giros, Ajustar vista y otra vez un toque sin presión seguido de un
pellizco **no** lo reprodujeron.

Evidencia: `evidence/2026-10-28-u31/android/US-003-E-01-dedos-abajo.png` (163 % con el lienzo
a ≈1,2×), `US-003-E-01-despues.png`, `BUG-002-ajustar.png`, `BUG-002-tras-trazo.png`.

## Causa
**Leída en el código de la librería, no observada en el momento** (graphics-core, fuente de
androidx-main; la 1.0.4 tiene las mismas piezas): cada cambio de la vista pedía un redibujo
completo sin esperar al anterior. Encolados más de los búferes que tiene el renderizador (3),
su hilo se queda esperando un búfer que solo él mismo puede devolver.

## Causa sospechada en la apertura
El hilo del renderizador de baja demora quedó bloqueado esperando un búfer de la capa de varios
búferes: la vista pide un redibujo (`renderMultiBufferedLayer`) por cada cambio de la vista,
uno por cuadro, y esos pedidos se acumulan. **No confirmado**: no se leyó el código del
renderizador durante la prueba.

## Impacto
Alto si vuelve a pasar: el lienzo queda congelado y no hay forma de recuperarlo sin cerrar la
app (la obra no se pierde, vive en la app). Probabilidad desconocida: una vez en unas 30
gestos en el emulador.

## Historia
- 2026-10-28 · abierto en la QA de la unidad 31.
- 2026-10-28 · arreglo en la unidad 111: un solo redibujo de la vista en vuelo, los pedidos de
  mientras tanto se juntan en uno · fixed, pending verification
- 2026-10-28 · re-corrida (unidad 112): E-01 pasa y 30 ciclos de pellizco seguidos sin
  congelarse · verified. **Límite:** el original nunca se reprodujo a pedido (1 vez en ~30
  gestos), así que 30 ciclos limpios son evidencia, no prueba; el log aún muestra esperas de
  búfer que se resuelven solas.
