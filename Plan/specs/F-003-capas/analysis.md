# Análisis — F-003 Capas

## Abiertas

(ninguna)

## Respondidas

### Q-28 · ¿Con cuántas capas empieza una obra, y se puede borrar la última?
- bloquea: RN de capas mínimas y el caso de borrar la única capa
- respuesta: Una obra nueva empieza con "Capa 1" vacía sobre el fondo. Nunca hay menos de 1 capa: la única capa no se puede borrar, solo vaciar. · 2026-10-27

### Q-29 · ¿Qué pasa al agregar o duplicar una capa cuando la obra ya está en el límite del equipo?
- bloquea: RN de límite de capas (pendiente de F-001, Q-17)
- respuesta: Mismo criterio que el lienzo, medido en el equipo donde corre la app en ese momento: pasado el umbral avisa, y en la línea roja no deja crear ni duplicar la capa, para no llenar la memoria. Una obra pasada a una PC potente puede seguir creciendo ahí; si después se importa en un equipo que no la soporta, ese equipo no deja importarla porque pone en riesgo el dispositivo. · 2026-10-27

### Q-30 · ¿Qué pasa si se pinta sobre una capa oculta?
- bloquea: el caso de pintar en capa oculta (pendiente de F-002)
- respuesta: No pinta y avisa que la capa está oculta. · 2026-10-27

### Q-31 · ¿Cómo se comporta el recorte cuando la capa de abajo se oculta, y se pueden recortar varias capas a la misma base?
- bloquea: RN de recorte
- respuesta: Varias capas seguidas con recorte usan la misma base (la primera capa sin recorte debajo de ellas). Si la base se oculta, las capas recortadas tampoco se ven. · 2026-10-27

### Q-32 · Al aplanar, ¿qué pasa con las capas ocultas?
- bloquea: RN de aplanar
- respuesta: Se juntan las capas visibles y las ocultas se descartan, avisando antes cuántas se van a perder. Se puede deshacer. · 2026-10-27

### Q-33 · ¿Confirmás los comportamientos estándar que propuse en F-001 a F-003?
- bloquea: el origen de RN-una-capa-activa, RN-capa-nueva-sobre-activa, RN-orden-de-apilado, RN-opacidad-capa, RN-bloqueo-alfa, RN-combinar-conserva-aspecto, RN-combinar-la-de-abajo, RN-acciones-de-capa-en-historial (F-003), RN-pinta-en-capa-activa, RN-trazo-en-historial (F-002) y RN-gestos-de-vista (F-001)
- respuesta: Confirmo todos: la capa nueva o duplicada va encima de la activa y queda activa; las capas se apilan tapando las de abajo; combinar conserva el aspecto; la capa de más abajo no se combina hacia abajo; el bloqueo alfa no cambia la transparencia; el pincel pinta solo en la capa activa; cada trazo y cada acción de capa entra en el historial; los gestos de dos dedos nunca pintan. · 2026-10-27

## Notas técnicas
- Mostrar "cuántas capas más admite el equipo" exige estimar cuánto ocupa la obra y cuánto queda libre. Para arquitectura (paso 5), junto con el umbral y la línea roja de F-001.
- Q-29 también fija una regla de F-008: un equipo no deja importar una obra que supera su línea roja.

## Objeciones
