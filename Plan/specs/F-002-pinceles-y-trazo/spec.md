# F-002 · Pinceles y trazo

Cubre FT-10, FT-11, FT-12, FT-13 y FT-14 del inventario (entrega 1, Q-07).

## Qué resuelve
Un ilustrador necesita que el trazo responda como un instrumento: más grueso u opaco cuanto
más aprieta, líneas limpias aunque le tiemble la mano, y un set de pinceles que alcance para
bocetar, entintar y colorear una obra completa.

## Alcance

**Entra**
- Elegir uno de los 7 pinceles del set base y dibujar en la capa activa con el color actual.
- Ajustar el grosor, la opacidad y el estabilizador de cada pincel.
- Que la presión del lápiz digital cambie el trazo según el pincel, y ajustar la curva de
  presión.
- Borrar con la goma, usando la forma de cualquier pincel.

**Queda afuera**
- Difuminar y mezclar colores (acuarela): FT-15 y FT-19, importantes (S) (Q-22).
- Volver un pincel a sus valores de fábrica (Q-100).
- Editar la forma, el espaciado, la textura o la dinámica de un pincel, y crear pinceles
  propios: FT-16 a FT-22, importantes (S) o deseables (C) (Q-07).
- Predicción de trazo: FT-26, importante (S). Arquitectura evalúa si hace falta para la meta
  de demora (notas técnicas de `analysis.md`).
- Rechazo de palma, gestos y atajos: son de F-009 Entrada.
- Elegir el color: es de F-005 Color y relleno.
- Qué pasa al pintar en una capa oculta: es de F-003 Capas.

## Reglas de negocio

### RN-set-base · origin: Q-22
El set base trae 7 pinceles: Lápiz, Tinta, Rotulador, Pincel redondo, Aerógrafo, Pincel suave
y Píxel.

### RN-pincel-pixel-sin-suavizado · origin: Q-22
El pincel Píxel pinta píxeles enteros, sin bordes suavizados, a cualquier zoom.

### RN-un-pincel-activo · origin: Q-22
Hay exactamente un pincel activo por vez, y es el que usa la herramienta pincel.

### RN-pincel-inicial · origin: Q-94
La primera vez que se abre la app, el pincel activo es el Lápiz. Al reabrirla, el pincel
activo es el último que se usó.

### RN-toque-deja-punto · origin: Q-95
Un toque del lápiz digital, el dedo o el mouse sin moverse deja un punto con la forma y el
grosor del pincel activo.

### RN-trazo-recortado-al-lienzo · origin: Q-96
Lo que un trazo pinta fuera del lienzo no se guarda. Un trazo que sale del lienzo y vuelve a
entrar sigue siendo un solo trazo.

### RN-pinta-en-capa-activa · origin: Q-33
Un trazo de pincel pinta solo en la capa activa, con el color actual.

### RN-trazo-no-se-suma-a-si-mismo · origin: Q-107
Un trazo no pasa de su opacidad aunque se cruce consigo mismo; dos trazos distintos sí se
suman. El Aerógrafo es la excepción: acumula pintura mientras se sigue pintando, hasta su
opacidad.

### RN-rango-grosor · origin: Q-23
El grosor de un pincel va de 1 a 500 píxeles.

### RN-rango-opacidad · origin: Q-23
La opacidad de un pincel va de 1 % a 100 %.

### RN-ajuste-fuera-de-rango · origin: Q-98
Un grosor o una opacidad pedidos fuera de su rango quedan en el límite más cercano, y se ve
el valor que quedó.

### RN-ajustes-no-cambian-lo-pintado · origin: Q-99
Cambiar el grosor, la opacidad o el estabilizador de un pincel vale desde el trazo siguiente;
lo ya pintado no cambia.

### RN-ajustes-por-pincel · origin: Q-23
Cada pincel recuerda su grosor, su opacidad y su estabilizador. Cambiar de pincel y volver
recupera los valores que tenía.

### RN-ajustes-persisten · origin: Q-23
Los ajustes de cada pincel se conservan al cerrar y reabrir la app.

### RN-presion-segun-pincel · origin: Q-24
Cada pincel define si la presión del lápiz digital le cambia el grosor, la opacidad, ambos o
nada.

### RN-curva-presion-unica · origin: Q-24
Hay una sola curva de presión para toda la app, y se aplica a todos los pinceles y a la goma.

### RN-curva-tres-opciones · origin: Q-101
La curva de presión se elige entre tres opciones: suave, normal y firme. Normal usa la presión
tal como llega; suave da más efecto con poca presión; firme pide apretar más para el mismo
efecto.

### RN-presion-proporcional · origin: Q-102
Con la curva normal, el valor que la presión cambia es proporcional a ella: la mitad de
presión da la mitad del grosor o de la opacidad del pincel. Nunca baja de 1 px de grosor ni
de 1 % de opacidad.

### RN-curva-persiste · origin: Q-103
La curva de presión elegida se conserva al cerrar y reabrir la app.

### RN-sin-presion · origin: Q-24
Cuando el trazo se hace con el dedo, el mouse o un lápiz que no informa presión, se dibuja a
presión completa.

### RN-estabilizador-por-pincel · origin: Q-25
El estabilizador va de 0 (apagado) a 10, y cada pincel tiene el suyo.

### RN-goma-usa-forma-de-pincel · origin: Q-27
La goma borra con la forma del pincel que el ilustrador elija para ella, con su propio
grosor, opacidad y estabilizador.

### RN-goma-forma-inicial · origin: Q-104
La primera vez, la goma borra con la forma del Pincel redondo.

### RN-goma-presion-como-su-pincel · origin: Q-105
La presión le cambia a la goma lo mismo que al pincel cuya forma usa: grosor, opacidad, ambos
o nada.

### RN-goma-ajustes-persisten · origin: Q-106
La forma, el grosor, la opacidad y el estabilizador de la goma se conservan al cerrar y
reabrir la app.

### RN-goma-solo-capa-activa · origin: Q-27
La goma quita pintura solo de la capa activa.

### RN-goma-no-borra-fondo · origin: Q-27
Si se borra sobre una zona donde solo queda el fondo del lienzo, el fondo no cambia. Un fondo
transparente se obtiene cambiando el fondo (RN-fondo-cambiable, F-001).

### RN-trazo-en-historial · origin: Q-33
Cada trazo de pincel o de goma entra en el historial como una acción que se puede deshacer.

## Valores iniciales

Son configuración de fábrica, no reglas. Los fijó el usuario donde dice Q-NN; el resto es una
propuesta que se calibra al construir.

| Pincel | La presión cambia | Estabilizador | Grosor | Opacidad |
|---|---|---|---|---|
| Lápiz | grosor y opacidad (Q-24) | 0 (Q-25) | 4 px *(propuesto)* | 100 % *(propuesto)* |
| Tinta | grosor *(propuesto)* | 3 (Q-25) | 8 px *(propuesto)* | 100 % *(propuesto)* |
| Rotulador | nada *(propuesto)* | 0 *(propuesto)* | 16 px *(propuesto)* | 100 % *(propuesto)* |
| Pincel redondo | grosor *(propuesto)* | 0 *(propuesto)* | 20 px *(propuesto)* | 100 % *(propuesto)* |
| Aerógrafo | opacidad (Q-24) | 0 *(propuesto)* | 60 px *(propuesto)* | 100 % *(propuesto)* |
| Pincel suave | opacidad *(propuesto)* | 0 *(propuesto)* | 40 px *(propuesto)* | 100 % *(propuesto)* |
| Píxel | nada *(propuesto)* | 0 *(propuesto)* | 1 px *(propuesto)* | 100 % *(propuesto)* |

Grosor y opacidad de fábrica, propuestos al construir US-005 (unidad 35).

Curva de presión inicial: normal *(propuesto)*.

Color actual inicial: negro, #000000 (Q-97), hasta que F-005 lo confirme o lo cambie.

## Requisitos no funcionales
- La pintura aparece en pantalla menos de 50 ms después de que el lápiz digital toca el
  lienzo, con estabilizador en 0, la obra de referencia (A4 a 300 dpi, 30 capas), en un
  Android de 4 GB y en una PC de 8 GB (Q-26). Arquitectura confirma cómo se mide (notas
  técnicas).
- Un trazo largo y rápido no pierde puntos: la línea sale continua y no se corta en tramos
  rectos visibles (PRD, objetivo de fluidez).

## Historias
| Historia | Qué cubre |
|---|---|
| US-005-dibujar-con-pincel | Elegir uno de los pinceles del set y dibujar en la capa activa con el color actual |
| US-006-ajustar-pincel | Grosor, opacidad y estabilizador por pincel, en sus rangos, y que se recuerden |
| US-007-presion | La presión según el pincel, la curva única y dibujar sin presión con dedo o mouse |
| US-008-borrar | La goma con la forma de un pincel, solo en la capa activa y sin borrar el fondo |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- **Q-111 (abierta, QA de US-006, 2026-10-29):** con un estabilizador alto, ¿el trazo tiene que salir liso hasta el final, o está bien que en su último tramo vaya a buscar el punto donde se levantó el lápiz y repita el temblor? Medido con estabilizador 10: liso hasta ~120 px antes del final; ahí vuelve todo el vaivén, con picos de 8 px contra 6 de la mano. Frena US-006 CA-10.
- **Q-112 (abierta, QA de US-007, 2026-10-29):** el Aerógrafo a 50 % de presión mide 86 px de ancho visible y a presión completa 89 (grosor 100): 1,5 px menos por lado, en las filas más claras del borde. ¿Cuenta como "el mismo ancho"? Frena US-007 CA-02.
- Respondidas: Q-94 a Q-97 respondidas al escribir US-005; Q-98 a Q-100, al escribir US-006; Q-101 a Q-103, al escribir US-007; Q-104 a Q-106, al escribir US-008; Q-107, en el diseño técnico; Q-108, en la QA de US-007 (usuario · 2026-10-28: el 40 % del Aerógrafo es el tope que alcanza al acumular, no lo que deja una pasada); Q-109, en la QA de US-007 (usuario · 2026-10-28: el ancho de un pincel es su grosor nominal, el diámetro del pincel; el borde difuso se desvanece adentro); Q-110, en la QA de US-007 (usuario · 2026-10-29: a media presión alcanza con que el centro y el ancho estén bien; el borde difuso no tiene que dar la mitad píxel por píxel).
- Los valores iniciales marcados *(propuesto)* se calibran al construir.
- Para arquitectura: cómo se mide la meta de 50 ms y la presión en Windows (notas técnicas
  de `analysis.md`).
