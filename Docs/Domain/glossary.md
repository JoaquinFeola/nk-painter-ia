# Glosario — nk-painter

Términos del producto, qué significan acá y el único nombre que llevan en el código. Si una
spec, una pantalla o el código usan otra palabra para algo de esta lista, el que está mal es
ese documento, no el glosario.

## Obra y archivos

### Obra · `Artwork`
Lo que el ilustrador crea y sigue editando: un lienzo con sus capas y su estado. Vive en la
galería del dispositivo y se guarda sola.
- El usuario también le dice "dibujo". Es lo mismo; los documentos usan *obra*.
- No confundir con **archivo de proyecto**, que es una copia transportable de la obra, ni
  con **imagen exportada**, que es solo cómo se ve.

### Archivo de proyecto · `ProjectFile`
Un archivo `nombreObra.nkpainter` que contiene la obra completa, con todas sus capas y
propiedades, para abrirla en otro dispositivo y seguir dibujando (Q-04). Se crea al
**exportar la obra** y se lee al **importarla**.
- No confundir con **imagen exportada**: la imagen pierde las capas y no se puede seguir
  editando como obra.

### Imagen exportada · `ExportedImage`
Una foto plana de la obra en PNG o JPEG, para mostrarla o publicarla fuera de la app. El
PNG puede tener fondo transparente.

### Galería · `Gallery`
La colección de obras guardadas en el dispositivo. Es solo local: no hay galería online
(Q-03).

### Autoguardado · `Autosave`
El guardado que la app hace sola mientras se dibuja, sin que el ilustrador lo pida. Es lo
que cumple el objetivo "nunca se pierde trabajo" (Q-12).
- No confundir con **exportar la obra**: el autoguardado mantiene la obra en la galería, y
  exportar genera un archivo de proyecto para llevarla a otro lado.

## Lienzo y vista

### Lienzo · `Canvas`
La superficie de la obra, con un tamaño fijo en píxeles y una resolución en dpi. Cambiar el
lienzo cambia la obra.
- No confundir con **vista**.

### Resolución · `Resolution`
Cuántos píxeles del lienzo van en una pulgada impresa, en dpi, de 72 a 600. No cambia los
píxeles del lienzo: dice de qué tamaño sale impreso.

### Tamaño de lienzo · `CanvasSize`
El ancho y el alto en píxeles de un lienzo, junto con su resolución. El mismo ancho y alto
con otra resolución es otro tamaño (Q-77).

### Unidad de medida · `LengthUnit`
En qué se escribe un tamaño personalizado: píxeles, milímetros o pulgadas. En milímetros o
pulgadas, la resolución lo pasa a píxeles (RN-tamano-personalizado).

### Tamaño prearmado · `CanvasPreset`
Un tamaño de lienzo con nombre que la app ofrece al crear una obra: A4, A5, Full HD, 2K
cuadrado y pantalla.
- No confundir con **tamaño reciente**, que sale de lo que el ilustrador creó antes.

### Tamaño reciente · `RecentCanvasSize`
Uno de los últimos 5 tamaños de lienzo distintos con que se crearon obras en este equipo.

### Vista · `Viewport`
Cómo se está mirando el lienzo en este momento: zoom, desplazamiento y rotación. Cambiar la
vista no cambia la obra ni se guarda en el historial.

### Ajustar vista · `FitToScreen`
Dejar la vista en 0° con el lienzo entero visible y centrado.

### Límite del equipo · `DeviceCanvasLimit`
Cuánto lienzo puede manejar el equipo donde corre la app. Tiene dos niveles: el **umbral de
aviso**, a partir del cual se avisa antes de crear, y la **línea roja**, a partir de la cual
no se deja crear (Q-17).
- Depende del equipo: la misma obra puede estar dentro del límite en la PC y pasarlo en el
  celular.

### Fondo del lienzo · `CanvasBackground`
Lo que se ve detrás de todas las capas: un color o transparente. Con fondo transparente, el
PNG exportado conserva la transparencia.
- No es una capa.

## Capas

### Capa · `Layer`
Una hoja transparente de la obra donde se pinta. Las capas se apilan; la de arriba tapa a la
de abajo según su opacidad.

### Capa activa · `ActiveLayer`
La capa sobre la que actúan el pincel, la goma, el balde y las transformaciones. Hay
exactamente una por vez.

### Opacidad de capa · `LayerOpacity`
Cuánto se ve una capa entera, de 0 % (invisible) a 100 %.
- No confundir con la **opacidad del pincel**, que es la de cada trazo.

### Recorte · `Clipping`
Una propiedad de la capa: lo que se pinta en ella solo se ve donde la capa de abajo tiene
pintura. Es la forma estándar de colorear dentro del lineart sin salirse.
- No confundir con **bloqueo alfa**: el recorte usa la capa de abajo como molde, y el
  bloqueo alfa usa la propia capa.
- No confundir con **recortar el lienzo** (achicarlo), que no está en la entrega 1.

### Bloqueo alfa · `AlphaLock`
Una propiedad de la capa: solo se puede pintar donde la capa ya tiene pintura. Sirve para
cambiar el color de un trazo sin salirse.

### Combinar hacia abajo · `MergeDown`
Juntar una capa con la de abajo en una sola.
- No confundir con **aplanar**, que junta todas las capas.

### Aplanar · `Flatten`
Juntar todas las capas visibles en una sola.

## Dibujo

### Herramienta · `Tool`
Lo que hace la entrada sobre el lienzo: pincel, goma, balde, selección, transformación,
cuentagotas. Hay una herramienta activa por vez.
- No confundir con **pincel**: el pincel es un tipo de herramienta, y cada pincel es una
  configuración distinta.

### Pincel · `Brush`
Una configuración con nombre que define cómo deja pintura un trazo: grosor, opacidad, forma
y respuesta a la presión. "Lápiz", "Tinta" y "Aerógrafo" son pinceles distintos de la misma
herramienta.

### Pincel activo · `ActiveBrush`
El pincel con el que pinta la herramienta pincel. Hay uno por vez; la primera vez es el
Lápiz, y después la app recuerda el último que se usó.

### Goma · `Eraser`
La herramienta que quita pintura de la capa activa. Usa la forma de un pincel, pero borra en
lugar de pintar.

### Trazo · `Stroke`
Un movimiento continuo, desde que el lápiz digital, el dedo o el mouse tocan el lienzo hasta
que se levantan.

### Lápiz digital · `Stylus`
El lápiz físico con el que se dibuja sobre la pantalla o la tableta gráfica.
- No confundir con el pincel **Lápiz** (`PencilBrush`), que imita un lápiz de grafito. Los
  documentos dicen siempre *lápiz digital* para el dispositivo.

### Presión · `Pressure`
Qué tan fuerte apoya el lápiz digital, leída en cada punto del trazo. Cambia el grosor o la
opacidad según el pincel.

### Curva de presión · `PressureCurve`
El ajuste del usuario que traduce la presión real en la que usa el pincel. Sirve para
adaptarse a una mano liviana o pesada. Hay una para toda la app, con tres opciones: suave,
normal y firme.

### Estabilizador · `Stabilizer`
El suavizado que corrige el temblor de la mano mientras se dibuja el trazo. Un nivel alto da
líneas más limpias, pero que siguen al lápiz con algo de demora.

### Color actual · `ActiveColor`
El color con el que pintan el pincel y el balde.

### Cuentagotas · `Eyedropper`
La herramienta que toma un color del lienzo y lo vuelve el color actual.

### Balde · `BucketFill`
La herramienta que pinta de un toque una zona cerrada de color parecido.

### Tolerancia · `Tolerance`
Qué tan distinto puede ser un color para que el balde (o una selección por color) lo trate
como parte de la misma zona.

## Selección y transformación

### Selección · `Selection`
La zona del lienzo delimitada para que las acciones (pintar, borrar, copiar, transformar)
solo la afecten. Puede ser rectangular, elíptica o a mano alzada (lazo).

### Transformar · `Transform`
Mover, escalar, rotar o voltear el contenido de la selección o de la capa activa.

## Historial

### Historial · `History`
La lista de acciones de la obra que se pueden deshacer y rehacer. Mover la **vista** no
entra en el historial.

### Deshacer · `Undo` / Rehacer · `Redo`
Volver atrás una acción del historial, o volver a aplicarla.

## Entrada

### Gesto · `Gesture`
Un movimiento con los dedos sobre la pantalla que dispara una acción en lugar de pintar.
Por ejemplo, pellizcar para hacer zoom o tocar con dos dedos para deshacer.

### Atajo de teclado · `Shortcut`
Una combinación de teclas que dispara una acción en Windows.

### Rechazo de palma · `PalmRejection`
Ignorar el contacto de la mano o de los dedos sobre la pantalla mientras se dibuja con el
lápiz digital, para que no deje marcas.
