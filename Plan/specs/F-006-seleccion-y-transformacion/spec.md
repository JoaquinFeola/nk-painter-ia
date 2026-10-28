# F-006 · Selección y transformación

Cubre FT-60, FT-63 y FT-64 del inventario (entrega 1, Q-07).

## Qué resuelve
Un ilustrador corrige proporciones moviendo, agrandando o girando una parte del dibujo, pinta
solo dentro de una zona sin salirse, y reutiliza partes copiándolas, también entre obras o
desde una imagen de referencia de otra app.

## Alcance

**Entra**
- Seleccionar con rectángulo, elipse y lazo, sumando o restando a la selección, y
  deseleccionar.
- Que la selección limite al pincel, la goma y el balde.
- Cortar, copiar y pegar, entre obras de la app y desde otras apps.
- Mover, escalar, rotar y voltear lo seleccionado o la capa entera, con confirmar y cancelar.

**Queda afuera**
- Varita mágica y selección por color: FT-61, importante (S) (Q-07).
- Invertir, expandir y contraer la selección: FT-62, importante (S) (Q-44).
- Transformar en perspectiva, deformar con malla y licuar: FT-65, FT-66 y FT-68 (S y C).
- Capa de selección (pintar la selección como máscara): FT-67, deseable (C).
- Pegar con un atajo de teclado: el atajo es de F-009 Entrada; la acción de pegar es de acá.
- Importar una imagen desde un archivo: es de F-008.

## Reglas de negocio

### RN-formas-de-seleccion · origin: Q-44
Se selecciona con rectángulo, elipse o lazo. El lazo se cierra solo al levantar el lápiz
digital, uniendo el último punto con el primero.

### RN-modos-de-seleccion · origin: Q-44
Cada forma nueva puede reemplazar la selección actual, sumarse a ella o restarse de ella.

### RN-deseleccionar · origin: Q-44, Q-36
Deseleccionar deja la obra sin selección, y entra en el historial.

### RN-seleccion-limita · origin: Q-45
Mientras hay una selección, el pincel, la goma y el balde solo cambian píxeles dentro de ella.

### RN-seleccion-es-de-la-obra · origin: Q-45
La selección pertenece a la obra, no a una capa: cambiar la capa activa no la cambia.

### RN-borde-suavizado · origin: Q-45
El borde de la selección es suavizado: lo que se pinta, copia o transforma en el borde pasa
gradualmente, sin escalones.

### RN-cambios-de-seleccion-en-historial · origin: Q-36
Cada cambio de la selección es un paso del historial (RN-que-entra-en-historial, F-004).

### RN-cortar-copiar · origin: Q-48
Cortar y copiar actúan sobre lo seleccionado de la capa activa, o sobre toda la capa activa si
no hay selección. Cortar además lo quita de la capa.

### RN-copiar-zona-vacia · origin: Q-48
Si lo que se copia o se corta no tiene pintura, el portapapeles no cambia y se avisa que no
había nada para copiar.

### RN-pegar-como-capa · origin: Q-46
Pegar crea una capa nueva encima de la capa activa, que pasa a ser la activa, y deja lo pegado
listo para transformar.

### RN-posicion-al-pegar · origin: Q-46
Lo copiado en la app se pega en la misma posición del lienzo de donde se copió. Una imagen que
viene de otra app se pega centrada en la vista.

### RN-portapapeles-entre-obras · origin: Q-46
Lo copiado en una obra se puede pegar en otra obra de la app.

### RN-pegar-de-otras-apps · origin: Q-46
Se puede pegar una imagen copiada en otra app del mismo equipo.

### RN-pegar-sin-imagen · origin: Q-49
Si el portapapeles no tiene una imagen, pegar no crea ninguna capa y se avisa que no hay una
imagen para pegar.

### RN-pegar-en-linea-roja · origin: Q-48
Si pegar llevaría la obra por encima de la línea roja de capas del equipo, no se pega y se
avisa, igual que al agregar una capa (RN-linea-roja-capas, F-003).

### RN-que-se-transforma · origin: Q-47
Transformar actúa sobre lo seleccionado de la capa activa, o sobre toda la capa activa si no
hay selección.

### RN-escala-proporcional · origin: Q-47
Escalar mantiene las proporciones salvo que el ilustrador libere esa opción.

### RN-rotacion-con-iman · origin: Q-47
Rotar es libre y se pega a cada múltiplo de 15° cuando el giro pasa cerca de uno.

### RN-voltear · origin: Q-47
Se puede voltear horizontal y verticalmente.

### RN-calidad-de-transformacion · origin: Q-47
Al transformar se elige calidad suave o "píxel exacto". Con "píxel exacto", los píxeles se
conservan enteros, sin mezclar colores en los bordes.

### RN-ajustar-sin-perder-calidad · origin: Q-47
Hasta confirmar, se puede escalar, rotar y mover varias veces, y el resultado se calcula
siempre desde el contenido original: achicar y volver a agrandar no lo pone borroso.

### RN-cancelar-transformacion · origin: Q-47
Cancelar una transformación deja la capa exactamente como estaba antes de empezarla.

### RN-confirmar-un-paso · origin: Q-47
Confirmar una transformación es un solo paso del historial, por más ajustes que se hayan
hecho antes.

### RN-fuera-del-lienzo-se-pierde · origin: Q-48
Al confirmar una transformación, lo que queda fuera del lienzo se pierde. Se recupera
deshaciendo.

## Requisitos no funcionales
- Mientras se arrastra una transformación sobre una capa de la obra de referencia (A4 a
  300 dpi), la vista previa se mueve al menos a 30 cuadros por segundo en un equipo de gama
  media (Q-50).

## Historias
| Historia | Qué cubre |
|---|---|
| US-020-seleccionar | Rectángulo, elipse y lazo; nueva, sumar y restar; deseleccionar; que limite pincel, goma y balde; que siga al cambiar de capa |
| US-021-copiar-y-pegar | Cortar, copiar y pegar como capa nueva; entre obras y desde otras apps; zona vacía; sin imagen; línea roja |
| US-022-transformar | Mover, escalar, rotar y voltear; calidad; ajustar sin perder calidad; cancelar; confirmar como un paso; lo que queda fuera del lienzo |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- (ninguna pregunta abierta)
- Para arquitectura: portapapeles de imágenes por plataforma y transformar desde el original
  (notas técnicas de `analysis.md`).
