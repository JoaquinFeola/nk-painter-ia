# Análisis — F-002 Pinceles y trazo

## Abiertas

(ninguna)

## Respondidas

### Q-107 · Un trazo semitransparente que se cruza consigo mismo, ¿se oscurece donde se superpone?
- bloquea: US-005 y US-006 (cómo se ve la opacidad), y QT-94 del diseño
- propuesta: no; un trazo nunca pasa de su opacidad, aunque se cruce. Dos trazos distintos sí se suman. El Aerógrafo es la excepción y acumula mientras se sigue pintando.
- respuesta: no, salvo el Aerógrafo, que acumula mientras se sigue pintando; dos trazos distintos sí se suman (aceptó la propuesta). · 2026-10-28

### Q-104 · ¿Con qué forma de pincel borra la goma la primera vez?
- bloquea: US-008, el criterio de la goma sin elegir forma
- propuesta: con la del Pincel redondo, que deja bordes suaves pero definidos.
- respuesta: Pincel redondo (aceptó la propuesta). · 2026-10-28


### Q-105 · La goma, ¿responde a la presión como el pincel cuya forma usa?
- bloquea: US-008, el criterio de presión de la goma
- propuesta: sí; con forma de Aerógrafo borra más cuanto más se apoya, y con forma de Rotulador borra igual con cualquier presión.
- respuesta: sí, como el pincel cuya forma usa (aceptó la propuesta). · 2026-10-28


### Q-106 · Los ajustes de la goma (forma, grosor, opacidad, estabilizador), ¿se conservan al reabrir la app?
- bloquea: US-008
- propuesta: sí, como los de los pinceles.
- respuesta: sí, se conservan (aceptó la propuesta). · 2026-10-28

### Q-101 · ¿Cómo se ajusta la curva de presión?
- bloquea: US-007, el criterio de la curva
- propuesta: tres opciones fijas (suave, normal, firme): suave da más efecto con poca presión, firme pide apretar más; normal deja la presión como llega.
- respuesta: tres opciones fijas: suave, normal y firme (aceptó la propuesta). · 2026-10-28


### Q-102 · Con la presión más leve, ¿cuánto grosor u opacidad queda?
- bloquea: US-007, el dato de los criterios de presión
- propuesta: proporcional a la presión (la mitad de presión da la mitad del valor del pincel), nunca menos de 1 px de grosor ni 1 % de opacidad.
- respuesta: proporcional a la presión, nunca menos de 1 px ni 1 % (aceptó la propuesta). · 2026-10-28


### Q-103 · La curva de presión elegida, ¿se conserva al cerrar y reabrir la app?
- bloquea: US-007
- propuesta: sí, como los ajustes de los pinceles.
- respuesta: sí, se conserva (aceptó la propuesta). · 2026-10-28

### Q-98 · Si pido un grosor o una opacidad fuera de rango (grosor 0 u 800, opacidad 150 %), ¿qué pasa?
- bloquea: US-006, su camino triste
- propuesta: se lleva al límite más cercano (grosor 1 o 500, opacidad 100 %) y se ve el valor que quedó.
- respuesta: se lleva al límite más cercano y se ve el valor que quedó (aceptó la propuesta). · 2026-10-28


### Q-99 · Cambiar el grosor o la opacidad de un pincel, ¿cambia los trazos que ya hice con él?
- bloquea: US-006
- propuesta: no; solo cambia los trazos siguientes.
- respuesta: no; solo cambia los trazos siguientes (aceptó la propuesta). · 2026-10-28


### Q-100 · ¿Se puede volver un pincel a sus valores de fábrica en la entrega 1?
- bloquea: el alcance de US-006
- propuesta: no en la entrega 1; queda afuera.
- respuesta: no en la entrega 1; queda afuera (aceptó la propuesta). · 2026-10-28

### Q-94 · ¿Qué pincel está activo la primera vez que se abre la app, y cuál al reabrirla?
- bloquea: US-005, el pincel con el que se dibuja sin haber elegido
- propuesta: Lápiz la primera vez; al reabrir, el último que se usó.
- respuesta: Lápiz la primera vez; al reabrir, el último que se usó. (aceptó la propuesta) · 2026-10-28


### Q-95 · Un toque del lápiz digital sin moverlo, ¿deja pintura?
- bloquea: US-005, el trazo más corto posible
- propuesta: sí, un punto con la forma y el grosor del pincel activo.
- respuesta: sí, un punto con la forma y el grosor del pincel activo. (aceptó la propuesta) · 2026-10-28


### Q-96 · Un trazo que sale del lienzo y vuelve a entrar, ¿qué deja?
- bloquea: US-005, el camino triste del borde
- propuesta: lo que cae afuera del lienzo no se guarda; lo de adentro queda como un solo trazo continuo, aunque haya salido.
- respuesta: lo de afuera no se guarda; lo de adentro queda como un solo trazo continuo. (aceptó la propuesta) · 2026-10-28


### Q-97 · Antes de que exista elegir color (F-005, más adelante en la entrega), ¿con qué color pinta el pincel?
- bloquea: US-005, el color actual que usan sus criterios y su construcción
- propuesta: negro (#000000) como color actual inicial; F-005 lo confirma o lo cambia.
- respuesta: negro (#000000) hasta que F-005 lo confirme o lo cambie. (aceptó la propuesta) · 2026-10-28


### Q-22 · ¿Qué pinceles trae el set base de la entrega 1?
- bloquea: RN del set base y la historia de elegir pincel
- respuesta: Set base de 7: Lápiz, Tinta (pluma), Rotulador, Pincel redondo, Aerógrafo, Pincel suave y Píxel (sin suavizado). Acuarela y difuminado quedan para después (FT-15, FT-19). · 2026-10-27

### Q-23 · ¿Entre qué valores van el grosor y la opacidad, y cada pincel recuerda los suyos?
- bloquea: RN de grosor y opacidad
- respuesta: Grosor de 1 a 500 px y opacidad de 1 a 100 %. Cada pincel recuerda los suyos, y la goma tiene los propios. · 2026-10-27

### Q-24 · ¿Qué cambia la presión del lápiz digital, la curva es una sola o por pincel, y qué pasa al dibujar con el dedo o el mouse?
- bloquea: RN de presión y el caso sin lápiz digital
- respuesta: Cada pincel trae definido si la presión le cambia el grosor, la opacidad o ambos (el Lápiz ambos, el Aerógrafo la opacidad). Hay una sola curva de presión para toda la app. Con dedo o mouse se dibuja a presión completa. · 2026-10-27

### Q-25 · ¿Cómo se regula el estabilizador?
- bloquea: RN del estabilizador
- respuesta: Nivel de 0 (apagado) a 10, por pincel. Valor inicial: 0 en Lápiz y 3 en Tinta. · 2026-10-27

### Q-26 · ¿Con qué número medimos que el trazo sigue al lápiz "sin demora que se note"?
- bloquea: el requisito no funcional de demora del trazo
- respuesta: Menos de 50 ms desde que el lápiz toca hasta que la pintura aparece en pantalla, con estabilizador en 0, la obra de referencia y un equipo de gama media. · 2026-10-27

### Q-27 · ¿Cómo se comporta la goma?
- bloquea: RN de la goma y su caso sobre el fondo
- respuesta: La goma usa la forma de cualquier pincel, con grosor y opacidad propios. Borra solo la capa activa y nunca el fondo del lienzo: para un fondo transparente se cambia el fondo. · 2026-10-27

## Notas técnicas
- Sin verificar: muchas pantallas Android ya tienen, por hardware, una demora del contacto a la imagen cercana a 50 ms. Arquitectura tiene que verificar si la meta de Q-26 se mide de punta a punta o como demora agregada por la app, y si hace falta predicción de trazo (FT-26, importante). Paso 5.
- La presión en Windows depende de cómo el framework lee la tableta gráfica o el lápiz. Paso 5.

## Objeciones
