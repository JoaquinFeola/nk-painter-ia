# US-005 · Dibujar con un pincel
feature: F-002

Como ilustrador, quiero elegir el pincel justo para cada etapa (bocetar, entintar, colorear) y
que el trazo siga a mi mano sin demora, para dibujar como lo haría en papel.

## Criterios

- [ ] **El set base trae los 7 pinceles.** (RN-set-base)
  Cuando elijo pincel, se ofrecen exactamente Lápiz, Tinta, Rotulador, Pincel redondo,
  Aerógrafo, Pincel suave y Píxel.
  android ✓ · windows ✓ · qa —

- [ ] **La primera vez se dibuja con el Lápiz.** (RN-pincel-inicial)
  Cuando abro la app por primera vez en el equipo y dibujo una línea sin elegir pincel, la
  línea está hecha con el Lápiz.
  android ✓ · windows ✓ · qa —

- [ ] **Al reabrir la app vuelve el último pincel usado.** (RN-pincel-inicial)
  Cuando dibujo con la Tinta, cierro la app y la vuelvo a abrir, el pincel activo es la Tinta.
  android ✓ · windows ✓ · qa —

- [ ] **Elegir un pincel lo deja como el único activo.** (RN-un-pincel-activo)
  Cuando, con el Lápiz activo, elijo el Aerógrafo y dibujo una línea, la línea está hecha con
  el Aerógrafo y el Lápiz ya no es el pincel activo.
  android ✓ · windows ✓ · qa —

- [ ] **El trazo pinta con el color actual.** (RN-pinta-en-capa-activa)
  Cuando, sin haber elegido color, dibujo con el Rotulador una línea de (100, 100) a
  (500, 100) en una obra A4 con fondo blanco, los píxeles del centro de la línea son #000000.
  android ✓ · windows ✓ · qa —

- [ ] **El trazo pinta solo en la capa activa.** (RN-pinta-en-capa-activa)
  Cuando, con la Capa 2 activa encima de la Capa 1, dibujo una línea de (100, 100) a
  (500, 100), la línea queda en la Capa 2 y la Capa 1 sigue vacía.
  android — · windows — · qa —

- [ ] **El pincel Píxel no suaviza los bordes.** (RN-pincel-pixel-sin-suavizado)
  Cuando dibujo con el pincel Píxel de grosor 1 una diagonal de (10, 10) a (20, 20) y la miro
  al 3200 %, cada píxel es #000000 opaco o queda sin pintar: ninguno tiene un tono intermedio.
  android ✓ · windows ✓ · qa —

- [ ] **Un toque sin mover deja un punto.** (RN-toque-deja-punto)
  Cuando toco el punto (300, 300) con el Pincel redondo de grosor 20 y levanto sin moverme,
  queda un punto de 20 px de diámetro centrado en (300, 300).
  android ✓ · windows ✓ · qa —

- [ ] **Lo que cae fuera del lienzo no se pinta, y el trazo no se corta.** (RN-trazo-recortado-al-lienzo)
  Cuando, en una obra A4 (2480×3508 px), dibujo sin levantar de (2300, 1000) a (2700, 1000) y
  de vuelta a (2300, 1200), quedan pintados los dos tramos dentro del lienzo, unidos en el
  borde derecho, y no queda pintura fuera de él.
  android ✓ · windows ✓ · qa —

- [ ] **Un trazo que salió del lienzo se deshace de una vez.** (RN-trazo-recortado-al-lienzo, RN-trazo-en-historial)
  Cuando, después del trazo anterior que salió y volvió a entrar, deshago una vez, desaparecen
  los dos tramos.
  android — · windows — · qa —

- [ ] **Cada trazo se deshace por separado.** (RN-trazo-en-historial)
  Cuando dibujo dos líneas y deshago una vez, se quita solo la segunda y la primera sigue.
  android — · windows — · qa —

- [ ] **La pintura aparece sin demora que se note.** (requisito no funcional de F-002)
  Cuando apoyo el lápiz digital en la obra de referencia (A4 a 300 dpi, 30 capas) con el
  Lápiz y estabilizador en 0, en un Android de 4 GB y aparte en una PC de 8 GB, la pintura
  aparece en pantalla menos de 50 ms después.
  android — · windows — · qa —

- [ ] **Un trazo largo y rápido sale continuo.** (requisito no funcional de F-002)
  Cuando dibujo un círculo de 1000 px de diámetro en 0,3 s con la Tinta, la línea sale
  continua, sin cortes ni tramos rectos visibles.
  android — · windows — · qa —

## Notas
- Casos que salen mal: el trazo que sale del lienzo (dos criterios). Pintar en una capa
  oculta o bloqueada es de F-003; dibujar sin presión, con el dedo o el mouse, es US-007.
- Dependen de otras features, como en F-001 (QT-59): "solo en la capa activa" necesita
  varias capas (F-003, US-009); los dos de deshacer necesitan el historial (F-004, US-014).
  Hasta entonces quedan `blocked` en QA.
- El color actual inicial negro es provisorio (Q-97): si F-005 lo cambia, cambia el dato del
  criterio del color y vuelve a QA.
- Cómo se mide la demora de 50 ms (de punta a punta o lo que agrega la app) lo decide el
  diseño técnico de F-002 (unidad 33); la medición del esqueleto (unidad 20) da la base.
- Construido en la unidad 35. Sin marca de área: "solo en la capa activa" (F-003) y los dos de
  deshacer (F-004), blocked; el motor ya pinta solo la capa activa y entrega los mosaicos de antes
  (QT-100). La demora y el trazo continuo no los alcanza un test: los mide QA (QT-92), con
  `nkpainter-latency` en Android y `-PlatencyLog` en Windows.
- Grosor, opacidad y estabilizador son de US-006; la presión, de US-007; la goma, de US-008.
