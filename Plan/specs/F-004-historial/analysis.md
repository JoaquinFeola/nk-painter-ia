# Análisis — F-004 Historial

## Abiertas

(ninguna)

## Respondidas

### Q-34 · ¿Cuántas acciones se pueden deshacer?
- bloquea: RN de largo del historial y el caso de llegar al tope
- respuesta: Fijo en 50, igual en todos los equipos. Al pasar el tope, la acción más vieja deja de poder deshacerse. · 2026-10-27

### Q-35 · ¿El historial sobrevive a cerrar y reabrir la obra, o a exportarla e importarla?
- bloquea: RN de persistencia del historial y su relación con el archivo de proyecto (F-008)
- respuesta: Sobrevive en el equipo: al reabrir la obra en el mismo equipo se puede seguir deshaciendo. El archivo de proyecto no lo lleva: al importar en otro equipo, el historial empieza vacío. · 2026-10-27

### Q-36 · ¿Qué entra en el historial y qué pasa con lo deshecho al hacer algo nuevo?
- bloquea: RN de qué es una acción del historial y RN de rehacer
- respuesta: Solo lo que cambia la obra: trazos, rellenos, acciones de capa, selección, transformaciones y cambio de fondo. No entran la vista, el pincel elegido ni el color actual. Hacer algo nuevo después de deshacer descarta lo que se podía rehacer. · 2026-10-27

### Q-37 · ¿Confirmás lo que propuse en F-004?
- bloquea: RN-nada-para-deshacer, el requisito de demora de deshacer y el alcance de la lista de historial
- respuesta: Confirmo los tres: deshacer o rehacer un trazo se ve en menos de 100 ms con la obra de referencia en gama media; si no queda nada para deshacer o rehacer, no cambia nada y se indica que no hay pasos; no hay lista para saltar a un paso intermedio, se deshace de a una acción. · 2026-10-27

## Notas técnicas
- 50 acciones sobre la obra de referencia (por ejemplo, 50 transformaciones de capas A4 completas) también ocupan memoria o disco. Arquitectura tiene que decidir dónde vive el historial para respetar la línea roja del equipo. Paso 5.
- Q-35 también fija una regla de F-008: el archivo de proyecto no lleva el historial.

## Objeciones
