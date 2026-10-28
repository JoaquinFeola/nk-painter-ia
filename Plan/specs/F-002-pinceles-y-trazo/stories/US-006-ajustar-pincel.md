# US-006 · Ajustar un pincel
feature: F-002

Como ilustrador, quiero ajustar el grosor, la opacidad y el estabilizador de cada pincel y
que los recuerde, para dejar cada uno listo para su uso sin volver a configurarlo.

## Criterios

- [ ] **El grosor elegido es el que pinta.** (RN-rango-grosor)
  Cuando pongo el Pincel redondo en grosor 40 y toco el lienzo sin moverme, queda un punto de
  40 px de diámetro.
  android ✓ · windows ✓ · qa —

- [ ] **La opacidad elegida es la que pinta.** (RN-rango-opacidad)
  Cuando pongo el Rotulador en opacidad 50 % y dibujo una línea negra sobre fondo blanco, el
  centro de la línea es #808080 (± 1 en cada canal).
  android ✓ · windows ✓ · qa —

- [ ] **Un trazo que se cruza consigo mismo no se oscurece.** (RN-trazo-no-se-suma-a-si-mismo)
  Cuando dibujo con el Rotulador en opacidad 50 %, sobre fondo blanco y sin levantar, una
  línea que se cruza consigo misma, el cruce es #808080 (± 1 en cada canal), igual que el
  resto de la línea.
  android ✓ · windows ✓ · qa —

- [ ] **Dos trazos que se cruzan sí se suman.** (RN-trazo-no-se-suma-a-si-mismo)
  Cuando dibujo con el Rotulador en opacidad 50 %, sobre fondo blanco, dos líneas separadas
  que se cruzan, el cruce es #404040 (± 1 en cada canal).
  android ✓ · windows ✓ · qa —

- [ ] **El Aerógrafo acumula mientras se sigue pintando.** (RN-trazo-no-se-suma-a-si-mismo)
  Cuando pinto con el Aerógrafo en opacidad 60 % yendo y viniendo diez veces sobre la misma
  zona sin levantar, el centro de la zona queda más oscuro que tras la primera pasada y
  nunca por encima del 60 % de opacidad.
  android ✓ · windows ✓ · qa —

- [ ] **Se aceptan los extremos de los rangos.** (RN-rango-grosor, RN-rango-opacidad)
  Cuando pongo el Pincel redondo en grosor 1 y opacidad 1 %, y después en grosor 500 y
  opacidad 100 %, cada valor queda tal cual.
  android ✓ · windows ✓ · qa —

- [ ] **Un grosor fuera de rango queda en el límite.** (RN-ajuste-fuera-de-rango)
  Cuando pido grosor 800 para el Pincel redondo, el grosor queda en 500 y veo 500; cuando
  pido 0, queda en 1 y veo 1.
  android ✓ · windows ✓ · qa —

- [ ] **Una opacidad fuera de rango queda en el límite.** (RN-ajuste-fuera-de-rango)
  Cuando pido opacidad 150 % para el Rotulador, la opacidad queda en 100 % y veo 100 %.
  android ✓ · windows ✓ · qa —

- [ ] **El estabilizador va de 0 a 10.** (RN-estabilizador-por-pincel)
  Cuando pongo el estabilizador del Lápiz en 10, queda en 10; cuando intento llevarlo a 11,
  sigue en 10.
  android ✓ · windows ✓ · qa —

- [ ] **Un estabilizador alto alisa el temblor.** (RN-estabilizador-por-pincel)
  Cuando dibujo con la Tinta la misma línea temblorosa, de (100, 500) a (1100, 500) con un
  vaivén de ± 6 px, una vez con estabilizador 0 y otra con 10, la línea con 10 se aparta de
  la recta menos que la de 0.
  android ✓ · windows ✓ · qa —

- [ ] **Cada pincel recuerda sus ajustes.** (RN-ajustes-por-pincel)
  Cuando pongo la Tinta en grosor 8 y estabilizador 3, cambio al Aerógrafo, lo pongo en grosor
  120, y vuelvo a la Tinta, la Tinta está en grosor 8 y estabilizador 3 y el Aerógrafo en 120.
  android ✓ · windows ✓ · qa —

- [ ] **Los ajustes siguen al reabrir la app.** (RN-ajustes-persisten)
  Cuando pongo el Lápiz en grosor 12 y opacidad 70 %, cierro la app y la vuelvo a abrir, el
  Lápiz está en grosor 12 y opacidad 70 %.
  android ✓ · windows ✓ · qa —

- [ ] **Cambiar un ajuste no toca lo ya pintado.** (RN-ajustes-no-cambian-lo-pintado)
  Cuando dibujo una línea con el Rotulador en grosor 10 y después lo paso a grosor 50, la
  línea ya dibujada sigue de 10 px y la siguiente sale de 50 px.
  android ✓ · windows ✓ · qa —

## Notas
- Casos que salen mal: grosor y opacidad fuera de rango, y el estabilizador por encima de 10.
- Volver un pincel a sus valores de fábrica queda afuera de la entrega 1 (Q-100).
- Los ajustes de la goma son de US-008; la presión y la curva, de US-007.
- Construido en la unidad 39. Los controles del panel (deslizador y campo) no los alcanza un
  test: los prueba QA (unidad 41).
- El criterio del temblor compara dos trazos del mismo recorrido: el diseño técnico de F-002
  (unidad 33) dice cómo se repite el recorrido en QA.
