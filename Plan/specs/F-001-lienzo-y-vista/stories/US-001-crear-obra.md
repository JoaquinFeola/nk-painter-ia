# US-001 · Crear una obra
feature: F-001

Como ilustrador, quiero empezar una obra del tamaño justo para donde va a terminar, impresa o
en pantalla, para no tener que rehacerla después por haber elegido mal el lienzo.

## Criterios

- [x] **Un prearmado de impresión da su tamaño exacto.** (RN-tamanos-prearmados)
  Cuando creo una obra con el prearmado A4, el lienzo mide 2480×3508 px a 300 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Un prearmado de pantalla se crea a 72 dpi.** (RN-tamanos-prearmados)
  Cuando creo una obra con el prearmado Full HD, el lienzo mide 1920×1080 px a 72 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **El prearmado de pantalla sigue la orientación del equipo.** (RN-prearmado-pantalla)
  Cuando creo una obra con el prearmado de pantalla en una tableta de 1200×2000 px sostenida
  en horizontal, el lienzo mide 2000×1200 px a 72 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Un tamaño personalizado en píxeles se respeta tal cual.** (RN-tamano-personalizado)
  Cuando creo una obra de 3000×2000 px a 150 dpi, el lienzo mide 3000×2000 px a 150 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Un tamaño en milímetros se pasa a píxeles redondeando.** (RN-tamano-personalizado)
  Cuando creo una obra de 100×150 mm a 300 dpi, el lienzo mide 1181×1772 px a 300 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Un tamaño en pulgadas se pasa a píxeles.** (RN-tamano-personalizado)
  Cuando creo una obra de 8×10 pulgadas a 72 dpi, el lienzo mide 576×720 px a 72 dpi.
  android ✓ · windows ✓ · qa ✓

- [x] **Los tamaños usados se ofrecen del más reciente al más viejo.** (RN-tamanos-recientes)
  Cuando creo una obra después de haber creado, en este orden, una A4, una de 3000×2000 px a
  150 dpi y una Full HD, se me ofrecen como recientes Full HD, 3000×2000 px a 150 dpi y A4,
  en ese orden.
  android ✓ · windows ✓ · qa ✓

- [x] **Solo se ofrecen los últimos 5 tamaños.** (RN-tamanos-recientes)
  Cuando creo una obra después de haber usado 6 tamaños distintos, el primero que usé ya no
  se ofrece y los otros 5 sí.
  android ✓ · windows ✓ · qa ✓

- [x] **Repetir un tamaño no lo duplica entre los recientes.** (RN-tamanos-recientes)
  Cuando creo una A4 habiendo creado antes una A4 y después una Full HD, los recientes son A4
  y Full HD, en ese orden, sin una segunda A4.
  android ✓ · windows ✓ · qa ✓

- [x] **El mismo ancho y alto con otra resolución es otro tamaño reciente.** (RN-tamanos-recientes)
  Cuando creo una obra de 2480×3508 px a 150 dpi habiendo creado antes una A4 a 300 dpi, se
  ofrecen las dos como recientes.
  android ✓ · windows ✓ · qa ✓

- [x] **Sin elegir fondo, el fondo es blanco.** (RN-fondo-inicial)
  Cuando creo una obra A4 sin elegir fondo, el fondo del lienzo es blanco (#FFFFFF).
  android ✓ · windows ✓ · qa ✓

- [x] **El fondo elegido al crear es el que tiene la obra.** (RN-fondo-inicial)
  Cuando creo una obra eligiendo el color #F5E6C8 como fondo, el fondo del lienzo es #F5E6C8.
  android ✓ · windows ✓ · qa ✓

- [x] **Se puede crear con fondo transparente.** (RN-fondo-inicial)
  Cuando creo una obra eligiendo fondo transparente, el lienzo no tiene color detrás de las
  capas.
  android ✓ · windows ✓ · qa ✓

- [ ] **La obra nueva se abre lista para dibujar.** (RN-obra-nueva-lista, RN-ajustar-vista, RN-obra-nueva-una-capa de F-003)
  Cuando termino de crear una obra A4, la veo entera, centrada y a 0°, con una sola capa
  vacía llamada "Capa 1" como capa activa.
  android ✓ · windows ✓ · qa —

## Notas
- Casos que salen mal: los rechazos al crear (tamaño menor al mínimo, resolución fuera de
  rango) y el aviso y la línea roja del equipo son US-002. Esta historia no tiene camino
  triste propio: todo tamaño válido se crea.
- El nombre inicial de la obra (RN-nombre-inicial) y su lugar en la galería son de F-007.
- Build (unidad 25): las reglas se prueban en tests del código común. Sin cubrir hasta QA:
  el prearmado de pantalla leído en una tableta girada, y que "Capa 1" se vea como capa
  activa, que no tiene dónde verse hasta el panel de capas de F-003.
