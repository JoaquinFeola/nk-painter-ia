# Arquitectura — nk-painter

> Primera versión: la escribió el paso de stack (unidad 5); la unidad 6 sumó el formato del
> archivo de proyecto. Crece con el diseño técnico de cada feature.

## Componentes
| Componente | Raíz | Qué hace | Áreas | Stack |
|---|---|---|---|---|
| nk-painter | `./` | La app de dibujo: un solo código Kotlin con Compose Multiplatform, compilado para Android y para Windows | android, windows | `Docs/Architecture/android-stack.md`, `Docs/Architecture/windows-stack.md` |

Dentro del componente hay cuatro módulos: `shared` con el código compartido, `androidApp` y
`desktopApp` con la entrada de cada plataforma, y `nativememory`, que en Android reserva la memoria de los mosaicos fuera de la memoria de Java (QT-52). El código compartido (dominio, motor de pintura, UI) no conoce la
plataforma. Lo propio de cada una queda detrás de interfaces comunes: entrada del lápiz,
archivos, portapapeles, memoria del equipo, tamaño de pantalla (F-001) y compartir.

La vista se mueve sobre una composición de las capas guardada por mosaico, con niveles
reducidos; ningún cuadro recombina capas (diseño de F-001, QT-58). Al pintar, cada mosaico tocado se
recompone con dos composiciones guardadas, lo de abajo y lo de arriba de la capa activa
(diseño de F-002, QT-89).

## Sistemas externos
| Sistema | Para qué | Sin internet |
|---|---|---|
| Sistema de archivos del equipo | Galería, autoguardado, exportar e importar | Sí |
| Menú de compartir de Android y selector de archivos del sistema | Entregar el archivo de proyecto a otra app o carpeta (F-008) | Sí; la app que lo envía es la que puede usar internet |
| Portapapeles del sistema | Pegar imágenes desde otras apps (F-006) | Sí |
| Pointer API de Windows | Presión y tipo de puntero del lápiz (ADR-001) | Sí |

## Decisiones
- Compose Multiplatform con un módulo nativo de lápiz en Windows: `adr/ADR-001-compose-multiplatform-con-lapiz-nativo.md`.
- El archivo de proyecto es un ZIP con el mismo esquema que la obra en la galería: `adr/ADR-002-archivo-de-proyecto-zip-con-el-esquema-de-la-galeria.md`; el contrato del formato, en `project-file-format.md`.
- Preguntas y respuestas técnicas: `architecture-decisions.md`.

## Riesgos a medir en el esqueleto
- Memoria base de la JVM en una PC de 8 GB (QT-05).
- Engancharse a los eventos de lápiz en la ventana de Compose en Windows (QT-06, QT-24).
- Demora del trazo frente a 50 ms en el Android de referencia (QT-08).
- Rendimiento del motor en CPU con la obra de referencia, y memoria real de los mosaicos (QT-09, QT-11).
- Que la composición de la obra se vea idéntica en ARM y en x86 (QT-33), y que la biblioteca ZIP de cada plataforma aguante la obra de referencia (QT-31).
