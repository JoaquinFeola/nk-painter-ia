# F-004 · Historial

Cubre FT-110 del inventario (entrega 1, Q-07).

## Qué resuelve
Un ilustrador prueba, se equivoca y vuelve atrás todo el tiempo. Sin deshacer confiable, cada
trazo es un riesgo y se dibuja con miedo.

## Alcance

**Entra**
- Deshacer y rehacer las acciones que cambian la obra.
- Que el historial siga disponible al reabrir la obra en el mismo equipo.

**Queda afuera**
- Los gestos y atajos para deshacer y rehacer (dos dedos, Ctrl+Z): son de F-009 Entrada.
- Ver la lista del historial y saltar a un paso intermedio: se deshace de a una acción
  (Q-37).
- Llevar el historial en el archivo de proyecto: el usuario decidió que no viaja (Q-35).
- Guardar estados pasados de la obra como archivos aparte (IPV en ibis Paint): no está en el
  inventario de la entrega 1.

## Reglas de negocio

### RN-que-entra-en-historial · origin: Q-36
Entran en el historial las acciones que cambian la obra: trazos de pincel y goma, rellenos,
acciones de capa, cambios de selección, transformaciones y cambios de fondo. Cada una es un
paso.

### RN-que-no-entra-en-historial · origin: Q-36
Cambiar la vista, el pincel elegido, los ajustes de un pincel o el color actual no entra en el
historial.

### RN-deshacer · origin: Q-07
Deshacer devuelve la obra exactamente al estado anterior al último paso, y ese paso queda
disponible para rehacer.

### RN-rehacer · origin: Q-07
Rehacer vuelve a aplicar el último paso deshecho, dejando la obra exactamente como estaba
antes de deshacerlo.

### RN-accion-nueva-descarta-rehacer · origin: Q-36
Cuando se hace una acción nueva después de deshacer, los pasos deshechos ya no se pueden
rehacer.

### RN-largo-historial · origin: Q-34
Se pueden deshacer como máximo las últimas 50 acciones, en cualquier equipo.

### RN-tope-historial · origin: Q-34
Cuando se hace la acción 51, la más vieja deja de poder deshacerse.

### RN-nada-para-deshacer · origin: Q-37
Si no queda nada para deshacer (o para rehacer), la acción no cambia nada y se indica que no
hay pasos disponibles.

### RN-historial-sobrevive-en-equipo · origin: Q-35
Al cerrar y reabrir una obra en el mismo equipo, el historial sigue igual: se puede deshacer y
rehacer como antes de cerrarla.

### RN-historial-no-viaja · origin: Q-35
Una obra importada desde un archivo de proyecto empieza con el historial vacío.

## Requisitos no funcionales
- Deshacer o rehacer un trazo se ve en pantalla en menos de 100 ms con la obra de referencia
  (A4 a 300 dpi, 30 capas), en un equipo de gama media. (Q-37)
- Guardar 50 pasos no hace que la obra pase la línea roja del equipo (Q-17, Q-34).

## Historias
| Historia | Qué cubre |
|---|---|
| US-014-deshacer-y-rehacer | Deshacer y rehacer pasos, acción nueva que descarta rehacer, nada para deshacer |
| US-015-largo-del-historial | Tope de 50, la acción 51 pierde la más vieja, qué entra y qué no |
| US-016-historial-al-reabrir | Sobrevive al reabrir en el mismo equipo y empieza vacío al importar |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- (ninguna pregunta abierta)
- Para arquitectura: dónde vive el historial para respetar la línea roja (notas técnicas de
  `analysis.md`).
