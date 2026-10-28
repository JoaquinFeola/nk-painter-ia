# Análisis — F-001 Lienzo y vista

## Abiertas

(ninguna)

## Respondidas

### Q-13 · ¿Qué tamaños prearmados ofrece el lienzo nuevo, y en qué unidades se elige uno personalizado?
- bloquea: RN de creación de lienzo y la historia de crear obra
- respuesta: Prearmados: A4 y A5 a 300 dpi, Full HD, 2K cuadrado y el tamaño de la pantalla. Personalizado en px, mm o pulgadas, con dpi entre 72 y 600. Los últimos 5 tamaños usados quedan a mano. · 2026-10-27

### Q-14 · ¿Qué pasa si el ilustrador pide un lienzo más grande que el de referencia (A4 a 300 dpi)?
- bloquea: la regla de límite de tamaño y el caso que sale mal al crear
- respuesta: Sin tope, con aviso. · 2026-10-27 — Precisado por Q-17.

### Q-15 · ¿El fondo del lienzo se elige al crear la obra, y se puede cambiar después?
- bloquea: las reglas de fondo y si cambiar el fondo entra en el historial
- respuesta: Se elige al crear (blanco por defecto, otro color o transparente) y se puede cambiar en cualquier momento. El cambio se puede deshacer. · 2026-10-27

### Q-16 · ¿La rotación de la vista es libre, y cómo se vuelve a la vista normal?
- bloquea: las reglas de vista
- respuesta: Rotación libre con dos dedos, que se pega al pasar cerca de 0°, 90°, 180° y 270°. Una acción vuelve a 0° con el lienzo entero en pantalla. · 2026-10-27

### Q-17 · ¿Mantenés "sin tope, con aviso" aunque choque con "nunca se pierde trabajo" y con la fluidez en gama media?
- bloquea: RN de límite de tamaño de lienzo
- respuesta: La app conoce el hardware donde corre. Antes de crear un lienzo muy grande, avisa. Si el lienzo pasa una línea roja que ese equipo no soportaría, no deja crearlo. · 2026-10-27

### Q-18 · Al cerrar y reabrir una obra, ¿la vista (zoom, posición, giro) vuelve como estaba?
- bloquea: RN de vista y la historia de abrir una obra
- respuesta: Sí, vuelve igual: mismo zoom, posición y giro. Mover la vista nunca entra en el historial de deshacer. · 2026-10-27

### Q-19 · ¿Hasta cuánto se puede acercar y alejar el lienzo?
- bloquea: RN de límites de zoom
- respuesta: Del 5 % al 3200 %. · 2026-10-27

### Q-20 · ¿Con qué número medimos que mover, girar y hacer zoom "no da saltos"?
- bloquea: el requisito no funcional de fluidez de la vista
- respuesta: 60 cuadros por segundo, con la obra de referencia (A4 a 300 dpi, 30 capas) en un equipo de gama media. · 2026-10-27

### Q-21 · ¿Cuál es el lienzo más chico que se puede crear?
- bloquea: el caso de tamaño inválido al crear
- respuesta: 16×16 píxeles. Un tamaño menor se rechaza al crear, avisando cuál es el mínimo. · 2026-10-27

### Q-75 · ¿Con qué resolución se crean los prearmados que no son de impresión (Full HD, 2K cuadrado, pantalla)?
- bloquea: el criterio de US-001 para esos prearmados; también el tamaño de impresión de la obra
- respuesta: 72 dpi. · 2026-10-27

### Q-76 · El prearmado "pantalla" en una tableta de 1200×2000 px que está en vertical, ¿da 1200×2000 o 2000×1200?
- bloquea: el criterio de US-001 para el prearmado de pantalla
- respuesta: Según cómo está el equipo en ese momento: en vertical 1200×2000, en horizontal 2000×1200. · 2026-10-27

### Q-77 · Para los tamaños recientes, ¿2480×3508 px a 300 dpi y 2480×3508 px a 150 dpi son el mismo tamaño o dos distintos?
- bloquea: los criterios de US-001 sobre RN-tamanos-recientes
- respuesta: Distintos: un tamaño reciente es ancho, alto y resolución. · 2026-10-27

### Q-78 · Una obra recién creada, ¿se abre lista para dibujar, con la vista ajustada?
- bloquea: el último criterio de US-001 (qué ve el ilustrador al terminar de crear)
- respuesta: Sí: se abre con la vista ajustada y lista para dibujar. · 2026-10-27

### Q-79 · Si pido 12000×16000 px y la línea roja del equipo no lo admite, ¿cómo se informa "el tamaño más grande que este equipo admite"?
- bloquea: el criterio de US-002 sobre RN-linea-roja-lienzo
- respuesta: Con la misma proporción que el pedido, por ejemplo "este equipo admite hasta 7500×10000 px". · 2026-10-27

### Q-80 · ¿La resolución acepta decimales, por ejemplo 72,5 dpi?
- bloquea: los criterios de borde de US-002 sobre RN-dpi-fuera-de-rango
- respuesta: Solo enteros, de 72 a 600. · 2026-10-27

### Q-81 · Si pido 10×10 px a 700 dpi (dos errores a la vez), ¿se informan los dos juntos o de a uno?
- bloquea: el criterio de US-002 con más de un error
- respuesta: Los dos juntos. · 2026-10-27

### Q-82 · ¿Qué tan cerca de 0°, 90°, 180° o 270° tiene que pasar el giro para pegarse?
- bloquea: los criterios de US-003 sobre RN-rotacion-con-imanes
- respuesta: A 5° o menos de uno de esos ángulos. · 2026-10-27

### Q-83 · Al pellizcar para acercar, ¿qué punto del dibujo queda quieto?
- bloquea: el criterio de US-003 sobre el zoom con gestos
- respuesta: El que está bajo el centro de los dedos. · 2026-10-27

### Q-84 · Una obra exportada y abierta en otro equipo, ¿vuelve con la vista que tenía en el primero?
- bloquea: el alcance de RN-vista-se-recuerda en US-003
- respuesta: No, abre ajustada. · 2026-10-27 — Reemplazada por la respuesta siguiente.
- contradicción: chocaba con Q-65 (F-008), que pone la vista en el archivo de proyecto. El agente recomendó "abre ajustada" sin haber leído Q-65.
- respuesta: Queda Q-65: la vista viaja con la obra y, al importarla en otro equipo, abre con la misma vista. · 2026-10-27

### Q-85 · Si el fondo ya es blanco y elijo blanco otra vez, ¿eso es un paso del historial?
- bloquea: el camino triste de US-004
- respuesta: No: elegir el mismo fondo no cambia nada y no entra en el historial. · 2026-10-27

## Notas técnicas
- Cómo se calculan el umbral de aviso y la línea roja a partir de la memoria del equipo. Para arquitectura (paso 5).
- Lo mismo pasa al agregar capas (F-003) y al importar en un celular una obra creada en la PC (F-008). Se pregunta en esas specs.

## Objeciones
- Avisé que "sin tope, con aviso" (Q-14) podía hacer que el sistema cerrara la app en un Android de 4 GB y perder trabajo. El usuario lo resolvió con un límite por equipo: aviso y línea roja (Q-17).
