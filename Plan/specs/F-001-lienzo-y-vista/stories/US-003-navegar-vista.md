# US-003 · Navegar la vista
feature: F-001

Como ilustrador, quiero acercar, alejar, desplazar y girar el lienzo con la soltura de una
hoja de papel, para trabajar el detalle y la composición sin que se mueva el dibujo.

## Criterios

- [x] **Pellizcar acerca dejando quieto lo que está bajo los dedos.** (RN-gestos-de-vista, RN-zoom-centro-de-dedos)
  Cuando pellizco abriendo los dedos con su centro sobre el píxel (1200, 800) de una obra A4
  vista al 100 %, la vista se acerca y el píxel (1200, 800) sigue bajo el centro de mis
  dedos.
  android ✓ · windows ✓ · qa ✓

- [x] **No se acerca más allá del 3200 %.** (RN-limites-zoom)
  Cuando pellizco abriendo los dedos con la vista al 3200 %, la vista se queda en 3200 %.
  android ✓ · windows ✓ · qa ✓

- [x] **No se aleja más allá del 5 %.** (RN-limites-zoom)
  Cuando pellizco cerrando los dedos con la vista al 5 %, la vista se queda en 5 %.
  android ✓ · windows ✓ · qa ✓

- [x] **Arrastrar con dos dedos desplaza la vista.** (RN-gestos-de-vista)
  Cuando arrastro dos dedos 300 px hacia la derecha con la vista al 100 % y a 0°, el dibujo
  se mueve 300 px hacia la derecha en la pantalla.
  android ✓ · windows ✓ · qa ✓

- [x] **Girar con dos dedos gira la vista.** (RN-gestos-de-vista, RN-rotacion-con-imanes)
  Cuando giro dos dedos 30° con la vista a 0°, la vista queda a 30°.
  android ✓ · windows ✓ · qa ✓

- [x] **Cerca de un ángulo recto, el giro se pega.** (RN-rotacion-con-imanes)
  Cuando termino de girar la vista a 86°, la vista queda a 90°.
  android ✓ · windows ✓ · qa ✓

- [x] **Lejos de un ángulo recto, el giro queda donde lo dejé.** (RN-rotacion-con-imanes)
  Cuando termino de girar la vista a 80°, la vista queda a 80°.
  android ✓ · windows ✓ · qa ✓

- [x] **Ajustar devuelve el lienzo entero, centrado y derecho.** (RN-ajustar-vista)
  Cuando ajusto la vista de una obra A4 que estaba al 400 %, girada 37° y desplazada hacia
  una esquina, veo el lienzo entero, centrado y a 0°.
  android ✓ · windows ✓ · qa ✓

- [x] **Los gestos de vista nunca pintan.** (RN-gestos-de-vista)
  Cuando pellizco, arrastro y giro con dos dedos sobre una zona vacía de "Capa 1", la capa
  sigue vacía.
  android ✓ · windows ✓ · qa ✓

- [ ] **Mover la vista no entra en el historial.** (RN-vista-no-cambia-obra)
  Cuando, después de pintar un trazo y acercar la vista al 300 %, deshago una vez, se quita el
  trazo y la vista sigue al 300 %.
  android — · windows — · qa —

- [ ] **Al reabrir la obra, la vista vuelve como estaba.** (RN-vista-se-recuerda)
  Cuando cierro una obra con la vista al 250 %, girada 45° y centrada en el píxel (1200, 800),
  y la vuelvo a abrir, la vista está al 250 %, a 45° y centrada en el píxel (1200, 800).
  android — · windows — · qa —

- [ ] **La vista viaja con la obra a otro equipo.** (RN-vista-se-recuerda)
  Cuando importo en la tableta una obra exportada desde la PC con la vista al 250 %, a 45° y
  centrada en el píxel (1200, 800), la obra se abre al 250 %, a 45° y centrada en el píxel
  (1200, 800).
  android — · windows — · qa —

- [ ] **La vista se mueve fluida con la obra de referencia.** (requisito no funcional de F-001)
  Cuando acerco, desplazo y giro durante 10 s la obra de referencia (A4 a 300 dpi, 30 capas)
  en un Android de 4 GB, y aparte en una PC de 8 GB, se muestra a 60 cuadros por segundo.
  android — · windows — · qa —

## Notas
- Casos que salen mal: los límites de zoom (5 % y 3200 %) y un giro que no llega al imán.
- En Windows, estos gestos necesitan una pantalla táctil. La rueda del mouse y espacio con
  arrastre son de F-009 (RN-rueda-zoom, US-035). Si la PC de QA no es táctil, los criterios de
  gestos quedan `blocked` en Windows hasta tener una.
- "Deshacer" es de F-004 y "reabrir" e "importar" necesitan F-007 y F-008. Esos tres criterios
  se pueden construir y probar recién cuando esas features existan, o con lo mínimo que
  decida el diseño técnico (unidad 22).
- 60 cuadros por segundo choca con lo medido en QT-51: recombinar 30 capas por cuadro toma
  193 ms. El diseño técnico de F-001 (unidad 22) tiene que resolverlo sin recombinar en cada
  cuadro.
