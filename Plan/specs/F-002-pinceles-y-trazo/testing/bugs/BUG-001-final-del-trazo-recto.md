# BUG-001 · El final de cada trazo sale recto: se pierden los últimos puntos antes de levantar
state: verified
found: 2026-10-28 · escenario E-13 · historia US-005 · criterio "Un trazo largo y rápido sale continuo" (CA-13, requisito no funcional de F-002)

## Qué pasa
En Android, un círculo dibujado con la Tinta sale continuo y redondo salvo su final: los últimos
~3 puntos antes de levantar no se siguen y el trazo va en línea recta desde el punto anterior
hasta donde se levantó. En un círculo de 60 puntos es un tramo recto de unos 23°, bien visible.
Pasa igual a cualquier velocidad: en 0,3 s (8 tandas de puntos) y en 2,1 s (un punto cada 38 ms).

## Esperado
"La línea sale continua, sin cortes ni tramos rectos visibles" (US-005 CA-13).

## Cómo reproducir
1. Emulador `Pixel_10_Pro_XL`, obra de 1400×1400 px, pincel Tinta.
2. Mandar un círculo de 60 puntos por la consola del emulador
   (`adb emu event send EV_ABS:ABS_MT_POSITION_X:… EV_ABS:ABS_MT_POSITION_Y:… EV_SYN:0:0`),
   radio 480 px de pantalla, centro (672, 1521), y levantar en el punto 60 (= el primero).
3. El tramo entre los puntos 56 y 60 sale recto.
Evidencia: `evidence/2026-10-28/android/a-E13-04-circulo.png` (0,3 s),
`a-E13-05-repeticion-y-control.png` (0,37 s y control lento de 2,1 s),
`a-E13-06-recorte-final-recto.png` (recorte ampliado de los tres finales).

## Lo que no se sabe
- **Causa sospechada, no confirmada:** algo que suaviza o interpola el trazo necesita puntos
  posteriores y, al levantar, une el último punto procesado con el final en línea recta.
- No se probó todavía en Windows (la corrida se interrumpió) ni con un lápiz real.

## Historial
- 2026-10-28 · abierto · pending
- 2026-10-28 · build informa el cambio de código (unidad 117: el final del trazo repasa la curva en vez de saltar recto; causa que build nombró: el estabilizador de fábrica 3 de la Tinta atrasa el punto dibujado y al levantar se unía en línea recta) · fixed, pending verification
- 2026-10-28 · re-corrida de E-13 en Android passed (US-005, corrida 2) · sigue fixed, pending verification hasta re-correrlo en Windows (QT-112)
- 2026-10-28 · re-corrida de E-13 en Windows (US-005, corrida 3): el final sigue la curva y el tramo recto no aparece; pero E-13 falló por otra causa (BUG-002, costuras entre mosaicos) · sigue fixed, pending verification: se verifica cuando E-13 pase entero en Windows
- 2026-10-29 · re-corrida de E-13 en Windows passed (US-005, corrida 2026-10-29, unidad 120): el final sigue la curva y la línea es continua · verified
