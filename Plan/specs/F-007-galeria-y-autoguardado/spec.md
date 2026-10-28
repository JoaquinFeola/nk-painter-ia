# F-007 · Galería y autoguardado

Cubre FT-111 y FT-112 del inventario (entrega 1, Q-07).

## Qué resuelve
Un ilustrador acumula decenas de obras y necesita encontrarlas y ordenarlas. Ante todo, no
puede perder horas de trabajo porque se cerró la app, se apagó el equipo o se olvidó de
guardar.

## Alcance

**Entra**
- La galería local: miniaturas, carpetas de un nivel y tres órdenes.
- Abrir, renombrar, duplicar, mover a carpeta y borrar obras, y crear, renombrar y borrar
  carpetas.
- El autoguardado después de cada paso, y la recuperación tras un cierre de golpe.
- El nombre de la obra y sus reglas.

**Queda afuera**
- Crear una obra nueva (tamaño, fondo): es de F-001.
- Exportar e importar archivos de proyecto e imágenes: es de F-008.
- Papelera para recuperar obras borradas: el usuario eligió borrado definitivo (Q-52).
- Carpetas dentro de carpetas (Q-51).
- Buscar obras por nombre: no se pidió.
- Sincronizar la galería entre equipos: requiere internet (Q-03). Se pasa por archivo de
  proyecto (F-008).

## Reglas de negocio

### RN-galeria-local · origin: Q-03
La galería muestra solo las obras guardadas en este equipo.

### RN-miniaturas · origin: Q-51
Cada obra se muestra en la galería con su miniatura, nombre, fecha de última modificación y
tamaño del lienzo.

### RN-carpetas-un-nivel · origin: Q-51
Las obras pueden estar sueltas o dentro de una carpeta. Una carpeta no puede contener otras
carpetas.

### RN-orden-galeria · origin: Q-51, Q-58
La galería se ordena por última modificación (la más reciente primero, por defecto), por
nombre o por fecha de creación. El orden elegido se conserva al cerrar la app.

### RN-acciones-sobre-obra · origin: Q-52
Desde la galería, una obra se puede abrir, renombrar, duplicar, mover a una carpeta o sacar de
ella, y borrar.

### RN-duplicar-obra · origin: Q-52, Q-58
Duplicar crea una obra nueva en la misma carpeta, con el mismo contenido y el nombre seguido
de " (2)", o el siguiente número libre.

### RN-borrar-obra · origin: Q-52
Borrar una obra pide confirmación nombrándola. Una vez confirmado, la obra se elimina para
siempre y no se puede recuperar.

### RN-borrar-carpeta · origin: Q-56
Borrar una carpeta borra también sus obras. La confirmación dice cuántas obras se van a borrar
para siempre.

### RN-nombre-inicial · origin: Q-54
Una obra nueva se llama "Obra AAAA-MM-DD HH.MM" con la fecha y la hora de creación, por
ejemplo "Obra 2026-10-27 23.35".

### RN-nombre-unico · origin: Q-54
Si el nombre elegido ya existe en la misma carpeta, no se acepta y se ofrece el mismo nombre
seguido de " (2)", o el siguiente número libre.

### RN-nombre-caracteres · origin: Q-54, Q-58
Si el nombre está vacío, tiene más de 100 caracteres o contiene alguno de \ / : * ? " < > |,
no se acepta y se indica qué está mal.

### RN-carpetas-nombre · origin: Q-58
Los nombres de carpeta siguen las mismas reglas que los de obra: únicos en la galería, sin los
caracteres prohibidos y con un máximo de 100 caracteres.

### RN-autoguardado-por-paso · origin: Q-53
La obra se guarda sola después de cada paso del historial, y al salir de la app o pasar a
otra.

### RN-sin-guardar-manual · origin: Q-53
No hay acción de guardar: una obra nunca tiene cambios sin guardar, salvo el paso que se está
guardando en ese momento.

### RN-indicador-de-guardado · origin: Q-53
Mientras se está guardando, se ve un indicador discreto que no interrumpe el dibujo.

### RN-sin-espacio · origin: Q-55
Si el equipo no tiene espacio para guardar, se avisa que la obra no se pudo guardar, se sigue
pudiendo dibujar, y se vuelve a intentar en el próximo paso.

### RN-version-anterior-intacta · origin: Q-55
Un guardado que falla o se interrumpe nunca daña la última versión guardada de la obra.

### RN-recuperar-tras-cierre · origin: Q-55, Q-12
Si la app se cerró de golpe, al reabrir la obra está como en el último autoguardado, sin
preguntar nada.

## Requisitos no funcionales
- La obra de referencia (A4 a 300 dpi, 30 capas) se abre desde la galería en menos de
  3 segundos, en un equipo de gama media (Q-57).
- Una galería de 200 obras se muestra en menos de 1 segundo, en un equipo de gama media
  (Q-57).
- Autoguardar nunca demora el trazo siguiente: la meta de 50 ms de F-002 se cumple también
  mientras se guarda (Q-57, Q-26).
- Si la app se cierra de golpe, se pierde como mucho el último paso (Q-12).

## Historias
| Historia | Qué cubre |
|---|---|
| US-023-recorrer-galeria | Miniaturas, carpetas de un nivel, los tres órdenes y que el orden se recuerde |
| US-024-gestionar-obras | Abrir, renombrar (nombre repetido o inválido), duplicar, mover, borrar con confirmación |
| US-025-gestionar-carpetas | Crear, renombrar y borrar carpetas con sus obras |
| US-026-autoguardado | Guardar después de cada paso, indicador, sin espacio, versión anterior intacta |
| US-027-recuperar-tras-cierre | Reabrir después de un cierre de golpe y encontrar la obra como en el último autoguardado |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- (ninguna pregunta abierta)
- Para arquitectura: guardar solo lo que cambió, en segundo plano y sin dejar archivos a medio
  escribir (notas técnicas de `analysis.md`).
