# US-007 · Dibujar con presión
feature: F-002

Como ilustrador, quiero que el trazo se engrose o se cargue cuanto más apoyo el lápiz
digital, y poder adaptar esa respuesta a mi mano, para controlar la línea como con un lápiz
de verdad.

## Criterios

- [ ] **La presión cambia el grosor de un pincel que responde en grosor.** (RN-presion-segun-pincel, RN-presion-proporcional)
  Cuando, con la curva normal, apoyo el lápiz digital al 50 % de presión con el Lápiz en
  grosor 20, la línea mide 10 px de ancho.
  android ✓ · windows ✓ · qa —

- [ ] **La presión cambia la opacidad de un pincel que responde en opacidad, y no el grosor.** (RN-presion-segun-pincel, RN-presion-proporcional)
  Cuando, con la curva normal, apoyo el lápiz digital al 50 % de presión con el Aerógrafo en
  grosor 100 y opacidad 80 %, la línea mide 100 px de ancho y, pintando encima sin levantar,
  la opacidad sube hasta un tope del 40 % y no lo pasa (Q-108: el 40 % es el tope, no una pasada).
  android ✓ · windows ✓ · qa —

- [ ] **Un pincel que no responde a la presión pinta igual con cualquier presión.** (RN-presion-segun-pincel)
  Cuando apoyo el lápiz digital al 30 % de presión con el Rotulador en grosor 20 y opacidad
  100 %, la línea mide 20 px y pinta al 100 %.
  android ✓ · windows ✓ · qa —

- [ ] **La presión cambia dentro de un mismo trazo.** (RN-presion-segun-pincel)
  Cuando dibujo con el Lápiz en grosor 20 una línea de (100, 300) a (1100, 300), empezando al
  10 % de presión y terminando al 100 %, la línea empieza en 2 px y termina en 20 px, y se
  ensancha sin saltos.
  android ✓ · windows ✓ · qa —

- [ ] **La presión más leve nunca baja de 1 px.** (RN-presion-proporcional)
  Cuando apoyo el lápiz digital al 1 % de presión con el Lápiz en grosor 20, la línea mide
  1 px.
  android ✓ · windows ✓ · qa —

- [ ] **La curva suave da más efecto con poca presión.** (RN-curva-tres-opciones)
  Cuando, con la curva suave, apoyo el lápiz digital al 50 % de presión con el Lápiz en grosor
  20, la línea mide más de 10 px.
  android ✓ · windows ✓ · qa —

- [ ] **La curva firme pide apretar más.** (RN-curva-tres-opciones)
  Cuando, con la curva firme, apoyo el lápiz digital al 50 % de presión con el Lápiz en grosor
  20, la línea mide menos de 10 px.
  android ✓ · windows ✓ · qa —

- [ ] **La curva vale para todos los pinceles.** (RN-curva-presion-unica)
  Cuando elijo la curva firme y después dibujo al 50 % de presión con el Aerógrafo en
  opacidad 80 %, la línea pinta a menos del 40 % de opacidad.
  android ✓ · windows ✓ · qa —

- [ ] **La curva sigue al reabrir la app.** (RN-curva-persiste)
  Cuando elijo la curva suave, cierro la app y la vuelvo a abrir, la curva es suave.
  android ✓ · windows ✓ · qa ✓

- [ ] **Con el dedo se dibuja a presión completa.** (RN-sin-presion)
  Cuando dibujo con el dedo con el Lápiz en grosor 20 y opacidad 100 %, la línea mide 20 px y
  pinta al 100 %.
  android ✓ · windows — · qa ✓

- [ ] **Con el mouse se dibuja a presión completa.** (RN-sin-presion)
  Cuando dibujo con el mouse con el Lápiz en grosor 20 y opacidad 100 %, la línea mide 20 px
  y pinta al 100 %.
  android ✓ · windows ✓ · qa —

- [ ] **Un lápiz digital que no informa presión dibuja a presión completa.** (RN-sin-presion)
  Cuando dibujo con un lápiz digital que no informa presión con el Lápiz en grosor 20, la
  línea mide 20 px.
  android ✓ · windows ✓ · qa —

## Notas
- Casos que salen mal: dedo, mouse y lápiz digital sin presión (tres criterios) y la presión
  más leve.
- Que la curva se aplique también a la goma es de US-008.
- En Android, el mouse es raro pero posible; si el equipo de QA no tiene uno, ese criterio
  queda `blocked` en Android. En Windows, el dedo necesita pantalla táctil, como los gestos de
  US-003.
- La presión del emulador llega siempre al 100 % (unidad 16): la QA de Android necesita
  presión inyectada o un equipo físico. El diseño técnico (unidad 33) dice cuál.
- En Windows el dedo todavía no pinta (QT-102): el criterio del dedo queda en `windows —`
  hasta F-009.
- Cuánto más o menos dan las curvas suave y firme es un valor de fábrica que se calibra al
  construir; los criterios solo piden el sentido.
