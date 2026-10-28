# F-003 · Capas

Cubre FT-40, FT-41, FT-42, FT-43 y FT-44 del inventario (entrega 1, Q-07).

## Qué resuelve
Un ilustrador separa boceto, lineart, colores base y sombras en capas para corregir una parte
sin arruinar las demás, y colorea dentro del lineart sin salirse. Sin capas no se puede
terminar una obra profesional.

## Alcance

**Entra**
- Agregar, borrar, duplicar, reordenar, ocultar, mostrar y renombrar capas, y elegir la capa
  activa.
- Opacidad por capa.
- Combinar una capa hacia abajo y aplanar la obra.
- Recorte y bloqueo alfa.
- No dejar pasar la memoria del equipo al agregar o duplicar capas.

**Queda afuera**
- Modos de fusión de capa: FT-45, importante (S) (Q-07). En la entrega 1, todas las capas se
  combinan en modo normal.
- Carpetas, bloqueo de capa y capa de referencia: FT-46 a FT-48, importantes (S) (Q-07).
- Capas de ajuste, vectoriales y 3D: FT-49 a FT-51, deseables (C) o fuera de alcance (W).
- Importar una foto como capa nueva: FT-52, importante (S). Importar una imagen al lienzo es
  de F-008.
- Exportar una capa sola como PNG: FT-116, deseable (C).
- Deshacer y rehacer: el historial es de F-004. Esta spec solo dice qué acciones entran en él.
- Importar una obra que supera la línea roja del equipo: la regla es de F-008 (Q-29).

## Reglas de negocio

### RN-obra-nueva-una-capa · origin: Q-28
Una obra nueva empieza con una capa vacía llamada "Capa 1", sobre el fondo del lienzo, y es la
capa activa.

### RN-minimo-una-capa · origin: Q-28
La obra tiene siempre al menos una capa. Si se intenta borrar la única capa, no se borra y se
indica que se puede vaciar.

### RN-una-capa-activa · origin: Q-33
Hay exactamente una capa activa por vez.

### RN-capa-nueva-sobre-activa · origin: Q-33
Una capa nueva o duplicada se ubica justo encima de la capa activa y pasa a ser la activa.

### RN-orden-de-apilado · origin: Q-33
Las capas se ven apiladas en su orden: cada una tapa a las de abajo según su opacidad, y todas
tapan el fondo del lienzo.

### RN-opacidad-capa · origin: Q-33
La opacidad de una capa va de 0 % a 100 % y afecta a toda la capa, sin cambiar lo que tiene
pintado.

### RN-capa-oculta-no-se-pinta · origin: Q-30
Si se pinta, se borra o se rellena sobre una capa oculta, no cambia nada y se avisa que la
capa está oculta.

### RN-umbral-capas · origin: Q-29
Cuando agregar o duplicar una capa lleva la obra por encima del umbral de aviso del equipo
donde corre la app, se avisa y el ilustrador decide si sigue.

### RN-linea-roja-capas · origin: Q-29
Si agregar o duplicar una capa llevaría la obra por encima de la línea roja del equipo donde
corre la app, la capa no se crea, y se indica que hay que combinar o borrar capas para seguir.

### RN-capas-disponibles-visible · origin: Q-29
El ilustrador puede ver en todo momento cuántas capas más admite la obra en el equipo donde
corre la app.

### RN-recorte-base · origin: Q-31
Una capa con recorte solo se ve donde su base tiene pintura. La base es la primera capa sin
recorte debajo de ella, así que varias capas seguidas con recorte comparten la misma base.

### RN-recorte-base-oculta · origin: Q-31
Mientras la base está oculta, las capas recortadas a ella tampoco se ven.

### RN-bloqueo-alfa · origin: Q-33
Mientras una capa tiene el bloqueo alfa activo, pintar en ella solo cambia los píxeles que ya
tenían pintura, sin cambiar su transparencia, y borrar no deja zonas transparentes.

### RN-combinar-conserva-aspecto · origin: Q-33
Combinar una capa hacia abajo da una sola capa que se ve igual que se veían las dos juntas.

### RN-combinar-la-de-abajo · origin: Q-33
La capa que está más abajo no se puede combinar hacia abajo.

### RN-aplanar-descarta-ocultas · origin: Q-32
Aplanar junta todas las capas visibles en una sola y descarta las ocultas. Si hay capas
ocultas, antes de aplanar se avisa cuántas se van a perder y el ilustrador decide si sigue.

### RN-acciones-de-capa-en-historial · origin: Q-33
Agregar, borrar, duplicar, reordenar, ocultar, mostrar, renombrar, cambiar la opacidad,
activar el recorte o el bloqueo alfa, combinar y aplanar entran en el historial.

## Requisitos no funcionales
- Con la obra de referencia (A4 a 300 dpi, 30 capas), ocultar, mostrar o cambiar la opacidad
  de una capa se ve en pantalla al instante, a 60 cuadros por segundo mientras se arrastra la
  opacidad (Q-20, Q-10).
- Ninguna capa que la app deja crear hace que el sistema la cierre por falta de memoria en el
  equipo donde se creó (Q-29, objetivo "nunca se pierde trabajo").

## Historias
| Historia | Qué cubre |
|---|---|
| US-009-gestionar-capas | Agregar, borrar (y no borrar la única), duplicar, reordenar, renombrar, elegir la activa |
| US-010-ver-y-ocultar | Ocultar, mostrar y opacidad de capa, y no pintar en una capa oculta |
| US-011-limite-de-capas | Umbral, línea roja y capas disponibles en el equipo actual |
| US-012-recorte-y-bloqueo-alfa | Recorte con varias capas a una base, base oculta, bloqueo alfa |
| US-013-combinar-y-aplanar | Combinar hacia abajo conservando el aspecto, aplanar descartando ocultas con aviso |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- (ninguna pregunta abierta)
- Para arquitectura: cómo estimar cuántas capas más admite el equipo (notas técnicas de
  `analysis.md`).
