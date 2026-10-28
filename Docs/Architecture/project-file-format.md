# Formato del archivo de proyecto — nk-painter

Contrato del archivo `.nkpainter` (F-008) y del esquema con que la galería guarda cada obra
(F-007). Decisión de fondo: `adr/ADR-002-archivo-de-proyecto-zip-con-el-esquema-de-la-galeria.md`.
Las preguntas y sus respuestas, QT-27 a QT-42, están en `architecture-decisions.md`.

Versión del formato que describe este documento: **1**.

## Un esquema, dos formas de guardarlo
| Dónde | Forma | Para qué |
|---|---|---|
| Galería del equipo | Una carpeta por obra con el manifiesto y un archivo por capa | Autoguardado que reescribe solo lo que cambió (F-007). Su organización interna la decide el diseño técnico de F-007 (QT-42) |
| Archivo de proyecto | Las mismas piezas empaquetadas en un ZIP llamado `<nombre de la obra>.nkpainter` | Llevar la obra a otro equipo (F-008) |

Tipo MIME: `application/vnd.nkpainter.project`, sin registrar en IANA. Lo usan la asociación
de Windows y el filtro de "abrir con" en Android (QT-13, QT-30).

## Contenido del ZIP
| Entrada | Compresión | Qué lleva |
|---|---|---|
| `mimetype` | ninguna, primera entrada | El texto `application/vnd.nkpainter.project`, para reconocer el archivo sin descomprimir nada |
| `manifest.json` | deflate | Todo lo que no son píxeles |
| `layers/<id de capa>.tiles` | deflate | Los mosaicos pintados de una capa |

Nada más entra en el ZIP. Una entrada que la app no conoce se ignora al importar.

## El manifiesto
| Campo | Qué es | Regla |
|---|---|---|
| `formatVersion` | Entero; 1 en esta versión | RN-archivo-de-version-nueva, RN-archivo-de-version-anterior |
| `appVersion` | Versión de la app que escribió el archivo; solo informativa, no decide nada | QT-29 |
| `name` | Nombre de la obra | RN-contenido-del-archivo, RN-nombre-caracteres |
| `widthPx`, `heightPx`, `dpi` | Tamaño del lienzo y resolución | RN-contenido-del-archivo, RN-tamano-minimo, RN-dpi-fuera-de-rango |
| `background` | `transparent`, o un color sRGB `#RRGGBB` | RN-contenido-del-archivo, RN-fondo-cambiable |
| `tileSize` | 256 en esta versión | QT-09 |
| `layers` | Lista de abajo hacia arriba: `id`, `name`, `opacity`, `visible`, `clipping`, `alphaLock`, `tileCount`, `file` | RN-contenido-del-archivo, RN-orden-de-apilado |
| `view` | Centro de la vista en píxeles del lienzo, `zoom` y `rotationDeg` | RN-contenido-del-archivo, RN-vista-se-recuerda |

- `opacity` es un entero de 0 a 100, el porcentaje de RN-opacidad-capa (QT-49).
- La vista va en coordenadas del lienzo y no de la pantalla, para que tenga sentido en un
  equipo con otra pantalla (QT-35).
- `tileCount` deja calcular la memoria de la obra sin descomprimir píxeles (QT-38).

**No va en el archivo**: el historial (RN-historial-no-viaja), los ajustes de pinceles,
colores, cuentagotas y balde (RN-fuera-del-archivo), la selección y la capa activa, que
RN-contenido-del-archivo no pide.

## El archivo de una capa
Enteros en little-endian:

| Parte | Tamaño | Qué es |
|---|---|---|
| Firma | 4 bytes | `NKTL` |
| Cantidad de mosaicos | 4 bytes | Igual al `tileCount` del manifiesto |
| Índice | 4 bytes por mosaico | Columna y fila del mosaico, 2 bytes cada una |
| Mosaicos | 256 × 256 × 4 bytes cada uno | RGBA de 8 bits, premultiplicado, en el orden del índice |

- Solo se guardan los mosaicos con pintura; una capa vacía tiene cero mosaicos.
- Los bytes son los del motor, sin convertir el alfa: la capa vuelve idéntica
  (RN-ida-y-vuelta-sin-perdidas). Si el motor usa otro orden de canales, se reordena, y
  reordenar no pierde nada (QT-34).
- En los bordes derecho e inferior el mosaico se guarda entero; lo que cae fuera del lienzo
  vale cero.

## Importar
1. Se abre el ZIP. Si no es un ZIP o su entrada `mimetype` no coincide, el archivo es
   inválido.
2. Se lee solo el manifiesto. Si `formatVersion` es mayor que la que esta app sabe leer, se
   rechaza pidiendo actualizar la app. Va primero, para que un archivo más nuevo no se
   informe como dañado.
3. Si la versión es menor, se aplican en cadena los pasos de migración (1→2, 2→3…).
4. Se valida el manifiesto: campos completos, nombre según RN-nombre-caracteres, tamaño y dpi
   dentro de F-001, al menos una capa, un archivo por capa.
5. Con el tamaño, las capas y los mosaicos se controla la línea roja del equipo.
6. Si el nombre ya existe en la galería, se pregunta si se reemplaza o se renombra.
7. Las capas se desempaquetan en una carpeta temporal de la app y se validan enteras: CRC de
   cada entrada, firma, cantidad de mosaicos y que cada mosaico caiga dentro del lienzo.
8. La carpeta temporal se mueve a la galería en un solo paso. Al reemplazar, la obra anterior
   se borra después de que la nueva quedó en su lugar.

Si cualquier paso falla, se borra la carpeta temporal y la galería queda como estaba. Las
carpetas temporales que sobrevivan a un cierre de golpe se borran al abrir la app.

## Exportar
1. Al pedir exportar se toma una foto de la obra, así seguir dibujando o el autoguardado no
   mezclan capas de dos pasos distintos.
2. El ZIP se escribe en segundo plano en un temporal de la app, con progreso por capa.
3. El temporal se copia al destino elegido o se entrega al menú de compartir de Android.
   Si la copia falla, se borra lo escrito en el destino.

## Dónde se cumple cada regla
| Regla | Se cumple en |
|---|---|
| RN-nombre-del-archivo | Al exportar: `<name>.nkpainter` |
| RN-contenido-del-archivo | El manifiesto y los archivos de capa |
| RN-fuera-del-archivo | Lo que el manifiesto no tiene |
| RN-ida-y-vuelta-sin-perdidas | Los bytes del motor sin convertir; la composición se mide en QT-33 |
| RN-archivo-de-version-nueva | Importar, paso 2, con `formatVersion` |
| RN-archivo-de-version-anterior | Importar, paso 3: migraciones, más un archivo de muestra por versión importado en cada build |
| RN-archivo-invalido | Importar, pasos 1, 4 y 7 |
| RN-importar-sobre-linea-roja | Importar, paso 5, antes de descomprimir píxeles |
| RN-conflicto-de-nombre, RN-reemplazar-al-importar, RN-renombrar-al-importar | Importar, pasos 6 y 8 |
| RN-importar-falla-sin-cambios | Carpeta temporal y movimiento en un solo paso (pasos 7 y 8) |
| RN-historial-no-viaja | El historial no está en el archivo |

El nombre de la obra sale del manifiesto y no del nombre del archivo, que WhatsApp o el
usuario pueden cambiar (QT-36).

## Cambiar el formato
- `formatVersion` sube solo cuando cambia lo que se escribe. Agregar una versión de la app
  sin cambiar el formato no lo toca.
- Cada versión nueva trae su paso de migración desde la anterior y un archivo de muestra en
  el juego de datos de prueba.
- Un cambio de esquema afecta a la vez a la galería y al archivo de proyecto (ADR-002).

## Sin verificar
- ZIP y ZIP64 con la biblioteca estándar en Android y en la JVM (QT-31): test de integración
  al crear el proyecto.
- Que WhatsApp entregue el documento sin cambios y su tamaño máximo (QT-32): prueba en el
  tramo de F-008.
- Que la composición sea idéntica en ARM y x86 (QT-33): medición en el esqueleto.
- Estimación, sin medir: la obra de referencia ocupa de 300 a 500 MB sin comprimir (QT-09);
  cuánto la achica deflate no se sabe hasta medirlo, ni si exportarla entra en 10 segundos.
