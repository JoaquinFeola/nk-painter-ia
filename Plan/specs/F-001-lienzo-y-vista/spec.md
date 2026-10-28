# F-001 · Lienzo y vista

Cubre FT-01, FT-02 y FT-05 del inventario (entrega 1, Q-07).

## Qué resuelve
El ilustrador necesita empezar una obra del tamaño justo para su destino (imprimir o
pantalla) y moverse por ella con la soltura de una hoja de papel, sin que un lienzo
demasiado grande para su equipo le haga perder el trabajo.

## Alcance

**Entra**
- Crear una obra nueva eligiendo un tamaño prearmado o uno personalizado, con su resolución.
- Elegir el fondo del lienzo al crear la obra, y cambiarlo después.
- Acercar, alejar, desplazar y girar la vista con gestos, y volver a la vista ajustada.
- Avisar o impedir lienzos que el equipo no soportaría.

**Queda afuera**
- Cambiar el tamaño del lienzo, recortarlo o redimensionar la imagen de una obra que ya
  existe: es FT-04, importante (S), fuera de la entrega 1 (Q-07).
- Espejar la vista: es FT-03, importante (S) (Q-07).
- Cuadrícula, papeles de lienzo y modo de color CMYK o escala de grises: FT-06, FT-07 y
  FT-08, deseables (C) (Q-07).
- Los atajos de teclado para zoom y giro: son de F-009 Entrada.

## Reglas de negocio

### RN-tamanos-prearmados · origin: Q-13, Q-75
Una obra nueva ofrece estos tamaños prearmados: A4 a 300 dpi (2480×3508 px), A5 a 300 dpi
(1748×2480 px), Full HD a 72 dpi (1920×1080 px), 2K cuadrado a 72 dpi (2048×2048 px) y el
tamaño de la pantalla del dispositivo a 72 dpi.

### RN-prearmado-pantalla · origin: Q-76
El prearmado de pantalla toma el ancho y el alto de la pantalla tal como está el equipo en
ese momento: una tableta de 1200×2000 px en vertical da 1200×2000, y en horizontal 2000×1200.

### RN-tamano-personalizado · origin: Q-13
Un tamaño personalizado se indica en píxeles, milímetros o pulgadas, con una resolución
entre 72 y 600 dpi. En milímetros o pulgadas, el tamaño en píxeles es la medida por la
resolución, redondeado al píxel más cercano.

### RN-dpi-fuera-de-rango · origin: Q-13, Q-80
La resolución es un número entero de dpi. Si la indicada no es entera, es menor que 72 dpi o
es mayor que 600 dpi, la obra no se crea y se informa el rango permitido.

### RN-tamano-minimo · origin: Q-21
Si alguno de los dos lados del lienzo es menor que 16 píxeles, la obra no se crea y se
informa el mínimo.

### RN-tamanos-recientes · origin: Q-13, Q-77
Los últimos 5 tamaños distintos usados para crear obras se ofrecen al crear una nueva, del
más reciente al más viejo. Un tamaño es su ancho, su alto y su resolución: el mismo ancho y
alto con otra resolución es otro tamaño.

### RN-obra-nueva-lista · origin: Q-78
Al crearse, la obra nueva se abre lista para dibujar, con la vista ajustada.

### RN-aviso-lienzo-grande · origin: Q-17
Si el lienzo pedido supera lo que este equipo maneja con soltura, se avisa antes de crearlo
y el ilustrador decide si sigue.

### RN-linea-roja-lienzo · origin: Q-17, Q-79
Si el lienzo pedido supera lo que este equipo puede soportar sin que el sistema cierre la
app, la obra no se crea y se informa el tamaño más grande que este equipo admite con la
misma proporción que el pedido.

### RN-errores-juntos · origin: Q-81
Si un pedido de obra nueva tiene más de un motivo para no crearse, se informan todos juntos.

### RN-fondo-inicial · origin: Q-15
Una obra nueva tiene fondo blanco salvo que el ilustrador elija otro color o transparente al
crearla.

### RN-fondo-cambiable · origin: Q-15, Q-85
El fondo del lienzo se puede cambiar en cualquier momento a un color o a transparente, y el
cambio entra en el historial. Elegir el fondo que ya tiene no es un cambio y no entra en el
historial.

### RN-vista-no-cambia-obra · origin: Q-18
Acercar, alejar, desplazar o girar la vista no cambia la obra y no entra en el historial.

### RN-vista-se-recuerda · origin: Q-18, Q-84
Al reabrir una obra, la vista vuelve con el mismo zoom, posición y giro que tenía al
cerrarla. También al importarla en otro equipo, porque la vista viaja en el archivo de
proyecto (RN-contenido-del-archivo, F-008).

### RN-limites-zoom · origin: Q-19
El zoom de la vista va del 5 % al 3200 %. Al llegar a un extremo, el gesto ya no acerca o
aleja más.

### RN-rotacion-con-imanes · origin: Q-16, Q-82
La vista gira libremente y se pega a 0°, 90°, 180° o 270° cuando el giro queda a 5° o menos
de uno de esos ángulos.

### RN-zoom-centro-de-dedos · origin: Q-83
Al pellizcar, el punto del dibujo que está bajo el centro de los dedos queda bajo los dedos
mientras la vista se acerca o se aleja.

### RN-ajustar-vista · origin: Q-16
La acción de ajustar deja la vista en 0° con el lienzo entero visible y centrado.

### RN-gestos-de-vista · origin: Q-33
Con dos dedos, pellizcar acerca o aleja, arrastrar desplaza y girar gira la vista. Estos
gestos nunca pintan.

## Requisitos no funcionales
- Acercar, alejar, desplazar y girar la vista se muestra a 60 cuadros por segundo con la
  obra de referencia (A4 a 300 dpi, 30 capas), en un Android de 4 GB y en una PC de 8 GB
  (Q-20, Q-09, Q-10).
- Ningún lienzo que la app deja crear hace que el sistema la cierre por falta de memoria en
  el equipo donde se creó (Q-17, objetivo "nunca se pierde trabajo").

## Historias
| Historia | Qué cubre |
|---|---|
| US-001-crear-obra | Crear una obra con un tamaño prearmado, personalizado o reciente, y su fondo; se abre lista para dibujar |
| US-002-lienzo-invalido-o-grande | Tamaño menor al mínimo, resolución fuera de rango, aviso y línea roja del equipo |
| US-003-navegar-vista | Zoom con sus límites, desplazar, girar con imanes, ajustar, y que la vista se recuerde |
| US-004-cambiar-fondo | Cambiar el fondo de una obra existente y deshacerlo |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- Q-91 a Q-93 (textos de error y aviso, color de fondo provisorio, A4 elegido al entrar):
  respondidas en `ux-ui/screen-obra-nueva.md`.
- (ninguna pregunta abierta)
- Resuelto en el diseño técnico (`tech-design/design.md`, QT-62): cómo se calculan el umbral
  de aviso y la línea roja según la memoria del equipo. Las fracciones se calibran en la
  unidad 20.
- Los criterios que piden deshacer, reabrir o importar esperan a F-004, F-007 y F-008
  (QT-59): F-001 no se cierra antes.
