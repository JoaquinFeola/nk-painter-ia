# US-002 · Un lienzo que no se puede crear, o que conviene pensar
feature: F-001

Como ilustrador, quiero saber antes de empezar si el lienzo que pedí está mal o es demasiado
para mi equipo, para no descubrirlo cuando la app se cierra y pierdo lo dibujado.

## Criterios

- [x] **Un lado menor que 16 px no se crea.** (RN-tamano-minimo)
  Cuando pido una obra de 15×500 px a 72 dpi, la obra no se crea y se me informa que el
  mínimo es 16 px por lado.
  android ✓ · windows ✓ · qa ✓

- [x] **16 px por lado sí se crea.** (RN-tamano-minimo)
  Cuando pido una obra de 16×16 px a 72 dpi, la obra se crea con un lienzo de 16×16 px.
  android ✓ · windows ✓ · qa ✓

- [x] **Un tamaño en milímetros que da menos de 16 px no se crea.** (RN-tamano-minimo, RN-tamano-personalizado)
  Cuando pido una obra de 5×5 mm a 72 dpi (14×14 px), la obra no se crea y se me informa que
  el mínimo es 16 px por lado.
  android ✓ · windows ✓ · qa ✓

- [x] **Una resolución menor que 72 dpi no se crea.** (RN-dpi-fuera-de-rango)
  Cuando pido una obra de 1000×1000 px a 71 dpi, la obra no se crea y se me informa que la
  resolución va de 72 a 600 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Una resolución mayor que 600 dpi no se crea.** (RN-dpi-fuera-de-rango)
  Cuando pido una obra de 1000×1000 px a 601 dpi, la obra no se crea y se me informa que la
  resolución va de 72 a 600 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Los extremos del rango sí se crean.** (RN-dpi-fuera-de-rango)
  Cuando pido una obra de 1000×1000 px a 600 dpi, la obra se crea a 600 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Una resolución con decimales no se crea.** (RN-dpi-fuera-de-rango)
  Cuando pido una obra de 1000×1000 px a 72,5 dpi, la obra no se crea y se me informa que la
  resolución es un número entero de 72 a 600 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Dos errores se informan juntos.** (RN-errores-juntos, RN-tamano-minimo, RN-dpi-fuera-de-rango)
  Cuando pido una obra de 10×10 px a 700 dpi, la obra no se crea y se me informan a la vez el
  mínimo de 16 px por lado y el rango de 72 a 600 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Un lienzo grande para el equipo avisa antes de crearse.** (RN-aviso-lienzo-grande)
  Cuando pido una obra de 6000×8000 px en un equipo cuyo umbral de aviso es 40 millones de
  píxeles y cuya línea roja es 75 millones, se me avisa antes de crearla y la obra todavía no
  existe.
  android ✓ · windows ✓ · qa ✓

- [ ] **Si sigo después del aviso, la obra se crea.** (RN-aviso-lienzo-grande)
  Cuando decido seguir después del aviso por una obra de 6000×8000 px, la obra se crea con
  ese tamaño.
  android — · windows — · qa ✓

- [ ] **Si no sigo después del aviso, no se crea nada.** (RN-aviso-lienzo-grande)
  Cuando decido no seguir después del aviso por una obra de 6000×8000 px, no se crea ninguna
  obra.
  android — · windows — · qa ✓

- [x] **Un lienzo por encima de la línea roja no se crea, y se informa el máximo con la misma forma.** (RN-linea-roja-lienzo)
  Cuando pido una obra de 12000×16000 px en un equipo cuya línea roja es 75 millones de
  píxeles, la obra no se crea y se me informa que este equipo admite hasta 7500×10000 px.
  android ✓ · windows ✓ · qa ✓

- [x] **El mismo lienzo puede estar bien en un equipo y no en otro.** (RN-linea-roja-lienzo)
  Cuando pido una obra de 12000×16000 px en un equipo cuya línea roja es 250 millones de
  píxeles y su umbral de aviso es 200 millones, la obra se crea sin aviso.
  android ✓ · windows ✓ · qa ✓

## Notas
- Los umbrales de los criterios (40, 75, 200 y 250 millones de píxeles) son datos de prueba,
  no reglas. Cómo se calculan el umbral de aviso y la línea roja de cada equipo es del
  diseño técnico de F-001 (unidad 22). QA necesita poder fijarlos para probar estos criterios.
- El máximo informado con la misma proporción se redondea hacia abajo, para que el tamaño
  informado se pueda crear.
- Un lienzo de exactamente 40 o 75 millones de píxeles no los "supera": no avisa ni se
  rechaza.
- Build (unidad 26): las reglas se prueban en tests del código común. "Si sigo" y "si no sigo"
  después del aviso están construidos en el diálogo, pero ningún test los recorre: sin marcar,
  sin cubrir hasta QA. QA fija los umbrales al lanzar: en Android con
  `--el warnPixels 40000000 --el redLinePixels 75000000`, en Windows con
  `-PwarnPixels=40000000 -PredLinePixels=75000000`.
- QA (unidad 27, 2026-10-28): CA-10 y CA-11 pasaron en Android y Windows por la pantalla; las
  marcas de área siguen en — porque son de build.
