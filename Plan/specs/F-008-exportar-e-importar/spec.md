# F-008 · Exportar e importar

Cubre FT-113, FT-114 y FT-118 del inventario (entrega 1, Q-07).

## Qué resuelve
Un ilustrador empieza una obra en el celular y la termina en la PC (o al revés) sin nube ni
cuentas, y saca su trabajo terminado como imagen para mostrarlo. También trae imágenes de
afuera: referencias, fotos o un boceto escaneado.

## Alcance

**Entra**
- Exportar una obra como archivo de proyecto, guardándolo donde se elija o, en Android,
  compartiéndolo con otra app.
- Importar un archivo de proyecto desde la app o con "abrir con", con sus casos que salen mal.
- Exportar la obra como PNG o JPEG.
- Importar una imagen PNG, JPEG o WebP como capa nueva o como obra nueva.

**Queda afuera**
- Exportar o importar PSD: FT-115, importante (S) (Q-07).
- Exportar una capa sola, o en CMYK: FT-116 y FT-117, deseables (C).
- Enviar el archivo por internet desde la app: la app no usa internet (Q-03). El ilustrador lo
  comparte con la app que quiera (Q-59).
- Que el historial viaje: no viaja (Q-35).
- Pegar una imagen desde otra app: es de F-006.
- El formato interno del archivo de proyecto: es técnico, está en
  `Docs/Architecture/project-file-format.md` (paso 6).

## Reglas de negocio

### RN-nombre-del-archivo · origin: Q-04, Q-54, QT-30
El archivo de proyecto exportado se llama como la obra, seguido de la extensión `.nkpainter`:
`Retrato.nkpainter`.

### RN-destino-de-exportacion · origin: Q-59
Al exportar, el ilustrador elige dónde guardar el archivo. En Android, también puede
compartirlo con cualquier otra app del equipo (por ejemplo WhatsApp).

### RN-app-sin-internet · origin: Q-03, Q-59
Exportar, compartir e importar funcionan sin conexión. La app entrega el archivo; si el
ilustrador lo envía por internet, lo hace otra app.

### RN-contenido-del-archivo · origin: Q-65
El archivo de proyecto lleva el nombre de la obra, su tamaño y dpi, el fondo, todas las capas
(también las ocultas) con su orden, nombre, opacidad, recorte, bloqueo alfa y visibilidad, y
la vista.

### RN-fuera-del-archivo · origin: Q-65, Q-35
El archivo de proyecto no lleva el historial ni los ajustes de pinceles, colores, cuentagotas o
balde, que son del equipo.

### RN-ida-y-vuelta-sin-perdidas · origin: Q-67
Una obra exportada en un equipo e importada en otro se ve idéntica, píxel por píxel, y tiene
las mismas capas y propiedades.

### RN-formas-de-importar · origin: Q-66
Un archivo de proyecto se importa eligiéndolo desde la app, o abriéndolo desde otra app o el
explorador de archivos (en Android) o con doble clic (en Windows). Las reglas son las mismas
en todos los casos.

### RN-obra-importada-en-galeria · origin: Q-67
Una obra importada aparece en la galería, fuera de cualquier carpeta, y se abre.

### RN-conflicto-de-nombre · origin: Q-60
Si ya hay en la galería una obra con el mismo nombre, se pregunta si se reemplaza la existente
o se le cambia el nombre a la importada.

### RN-reemplazar-al-importar · origin: Q-60, Q-52
Reemplazar pide confirmación, y la obra que estaba en la galería se elimina para siempre.

### RN-renombrar-al-importar · origin: Q-60, Q-54
Cambiar el nombre propone el mismo nombre seguido de " (2)", o el siguiente número libre, y el
ilustrador puede escribir otro que cumpla las reglas de nombre (F-007).

### RN-importar-sobre-linea-roja · origin: Q-29
Si la obra del archivo supera la línea roja del equipo donde se importa, no se importa y se
explica que este equipo no la soporta.

### RN-archivo-de-version-nueva · origin: Q-61, QT-29
Si el archivo usa una versión del formato más nueva que la que esta app sabe leer, no se
importa y se indica que hay que actualizar la app. Un archivo escrito por una versión más
nueva de la app con un formato que esta app sabe leer se importa.

### RN-archivo-de-version-anterior · origin: Q-61, QT-29
Un archivo con cualquier versión anterior del formato se importa siempre.

### RN-archivo-invalido · origin: Q-61
Si el archivo está dañado o no es un archivo de proyecto de nk-painter, no se importa y se dice
por qué.

### RN-importar-falla-sin-cambios · origin: Q-61
Una importación que no se completa no deja nada en la galería ni cambia ninguna obra.

### RN-exportar-imagen-formatos · origin: Q-62
La obra se exporta como PNG o JPEG, combinando las capas visibles y el fondo.

### RN-exportar-imagen-tamano · origin: Q-62
La imagen se exporta al tamaño del lienzo, o escalada de 25 % a 400 %.

### RN-calidad-jpeg · origin: Q-62
La calidad del JPEG va de 1 a 100 y empieza en 90.

### RN-transparencia-al-exportar · origin: Q-62
Con fondo transparente, el PNG conserva la transparencia y el JPEG sale con fondo blanco.

### RN-importar-imagen-formatos · origin: Q-63, Q-67
Se importan imágenes PNG, JPEG y WebP. Si el archivo no es una de ellas o está dañado, no se
importa y se dice por qué.

### RN-imagen-en-obra · origin: Q-63
Una imagen importada dentro de una obra va a una capa nueva sobre la activa, centrada y lista
para transformar (RN-pegar-como-capa, F-006).

### RN-imagen-mas-grande · origin: Q-63
Si la imagen es más grande que el lienzo, se achica manteniendo sus proporciones hasta entrar
entera.

### RN-imagen-como-obra · origin: Q-63
Una imagen importada desde la galería crea una obra nueva del tamaño de la imagen, con la
imagen en su primera capa. Se aplican el aviso y la línea roja de F-001.

### RN-imagen-en-linea-roja-de-capas · origin: Q-29
Si importar una imagen dentro de una obra llevaría la obra por encima de la línea roja de capas
del equipo, no se importa y se avisa (RN-linea-roja-capas, F-003).

## Requisitos no funcionales
- Exportar o importar el archivo de proyecto de la obra de referencia (A4 a 300 dpi, 30 capas)
  tarda menos de 10 segundos en un equipo de gama media (Q-64).
- Exportar la obra de referencia como PNG tarda menos de 5 segundos en un equipo de gama media
  (Q-64).
- Mientras se exporta o importa se ve el progreso, y la app no se bloquea (Q-64).

## Historias
| Historia | Qué cubre |
|---|---|
| US-028-exportar-proyecto | Exportar con el nombre de la obra, elegir dónde o compartir en Android, qué lleva y qué no |
| US-029-importar-proyecto | Importar desde la app o con "abrir con", ida y vuelta sin pérdidas, conflicto de nombre |
| US-030-importacion-rechazada | Línea roja del equipo, versión más nueva, archivo dañado o ajeno, sin cambios en la galería |
| US-031-exportar-imagen | PNG y JPEG, tamaño, calidad y transparencia |
| US-032-importar-imagen | Como capa o como obra, formatos, imagen más grande, línea roja |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- (ninguna pregunta abierta)
- Para arquitectura: "abrir con" y asociación de la extensión por plataforma, y compartir sin
  permiso de internet (paso 5). Formato, extensión y versión: resueltos en el paso 6
  (`Docs/Architecture/project-file-format.md`, ADR-002).
