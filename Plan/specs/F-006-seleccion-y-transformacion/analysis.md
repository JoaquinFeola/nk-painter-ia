# Análisis — F-006 Selección y transformación

## Abiertas

(ninguna)

## Respondidas

### Q-44 · ¿Cómo se arma una selección: solo nueva, o también se suma y se resta?
- bloquea: RN de modos de selección
- respuesta: Cada forma (rectángulo, elipse, lazo) puede reemplazar la selección, sumarse o restarse a ella. El lazo se cierra solo al levantar el lápiz. Se deselecciona con una acción. Invertir y expandir quedan para después (FT-62). · 2026-10-27

### Q-45 · ¿Qué limita una selección, y qué pasa con ella al cambiar de capa?
- bloquea: RN de efecto de la selección sobre pincel, goma y balde
- respuesta: Mientras hay selección, pincel, goma y balde solo actúan adentro. La selección es de la obra, no de la capa: sigue igual al cambiar de capa activa. Su borde es suavizado. · 2026-10-27

### Q-46 · ¿Dónde aparece lo pegado, y entre qué se puede copiar y pegar?
- bloquea: RN de pegar y de portapapeles
- respuesta: Pegar crea una capa nueva sobre la activa, en la misma posición de donde se copió (o centrada si viene de afuera), lista para transformar. Se puede pegar en otra obra de la app y pegar una imagen copiada en otra app. · 2026-10-27

### Q-47 · ¿Qué se transforma, y cómo se escala, rota y confirma?
- bloquea: RN de transformación
- respuesta: Transforma lo seleccionado de la capa activa, o toda la capa si no hay selección. Escala proporcional por defecto (se puede liberar), rota libre con imán cada 15°, voltea horizontal y vertical. Calidad suave o "píxel exacto". Hasta confirmar se ajusta sin perder calidad; cancelar deja todo como estaba; confirmar es un solo paso del historial. · 2026-10-27

### Q-48 · ¿Qué pasa en los casos borde: cortar sin selección, copiar una zona vacía, pegar en la línea roja, transformar fuera del lienzo?
- bloquea: los casos que salen mal de las historias de copiar y transformar
- respuesta: Los cuatro así: cortar o copiar sin selección actúa sobre toda la capa activa; copiar una zona sin pintura no cambia el portapapeles y avisa; pegar en la línea roja de capas no pega y avisa (como Q-29); lo que queda fuera del lienzo al confirmar una transformación se pierde. · 2026-10-27

### Q-49 · ¿Qué pasa al pegar cuando el portapapeles no tiene una imagen (por ejemplo, texto copiado en otra app)?
- bloquea: un caso que sale mal de US-021; no bloquea las reglas
- respuesta: No pega y avisa que no hay una imagen para pegar. · 2026-10-27

### Q-50 · ¿La vista previa al transformar también tiene que ir a 60 cuadros por segundo?
- bloquea: el requisito no funcional de la vista previa de transformación
- respuesta: 30 cuadros por segundo alcanza. · 2026-10-27

## Notas técnicas
- Pegar desde otras apps depende de cómo cada plataforma expone el portapapeles de imágenes. Para arquitectura (paso 5).
- "Ajustar sin perder calidad hasta confirmar" implica transformar siempre desde el original. Para arquitectura.

## Objeciones
