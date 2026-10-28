# ADR-002 · El archivo de proyecto es un ZIP con el mismo esquema que la obra en la galería
status: accepted · 2026-10-28 · decided by: el usuario (QT-27, QT-28)

## Qué forzó esto
Una obra tiene que viajar entre un Android y una PC sin nube y volver idéntica, con sus capas
y sus propiedades (F-008, RN-ida-y-vuelta-sin-perdidas). Toda versión futura de la app tiene
que poder leer los archivos que existan (RN-archivo-de-version-anterior), así que el formato
dura para siempre. A la vez, la obra en la galería se guarda sola después de cada paso sin
frenar el trazo (F-007), lo que pide reescribir solo lo que cambió.

## Opciones
| Opción | Costo |
|---|---|
| Galería y archivo con el mismo esquema: en la galería, desempaquetado; al exportar, empaquetado en un ZIP | Dos formas de guardar el mismo esquema (carpeta y ZIP). Depende de que la biblioteca ZIP de las dos plataformas aguante la obra de referencia (QT-31, sin verificar) |
| La galería guarda el archivo de proyecto tal cual | Cada paso reescribe un archivo de cientos de MB: choca con la meta de 50 ms mientras se guarda (F-007) |
| Dos formatos independientes, uno interno y otro de intercambio | Dos lectores y dos escritores que mantener, y una conversión entre ellos que puede perder datos |
| Contenedor binario propio en lugar de ZIP | Escribir y mantener el contenedor, su integridad y sus herramientas; difícil de diagnosticar |
| Contenedor SQLite en lugar de ZIP | Una dependencia nativa más y un archivo más pesado, sin ganancia en un archivo que se escribe una sola vez |

## Decisión
Un único esquema —un manifiesto JSON y un archivo de mosaicos por capa— que la galería guarda
desempaquetado y el archivo de proyecto `.nkpainter` empaqueta en un ZIP. Detalle en
`Docs/Architecture/project-file-format.md`.

## Qué aceptamos
- El formato queda atado a ZIP para siempre: cualquier versión de la app lee los ZIP de las
  anteriores.
- El archivo no se puede abrir en otros programas de dibujo: es un ZIP, pero los píxeles van
  en una codificación propia (mosaicos premultiplicados, QT-34).
- Un cambio en el esquema toca a la vez el autoguardado y el archivo de proyecto, y suma un
  paso de migración (QT-39).
- Revertirla significa escribir un formato nuevo y conservar el lector de ZIP para los
  archivos que ya existan.

## Reemplaza a
Nada.
