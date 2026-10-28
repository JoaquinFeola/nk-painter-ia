# F-009 · Entrada

Cubre FT-130, FT-131 y FT-132 del inventario (entrega 1, Q-07).

## Qué resuelve
Un ilustrador apoya la mano sobre la pantalla para dibujar con el lápiz digital, y no puede
tener manchas de la palma en cada trazo. Además trabaja rápido: deshace con dos dedos en la
tablet y cambia de herramienta con el teclado en la PC, sin buscar botones.

## Alcance

**Entra**
- Rechazo de palma automático al usar el lápiz digital, y apagarlo.
- El set fijo de gestos con los dedos.
- El set fijo de atajos de teclado en Windows.
- La rueda del mouse y la pantalla táctil en Windows.
- Cómo se resuelve un trazo empezado cuando llega un gesto.

**Queda afuera**
- Reasignar gestos o atajos: no en la entrega 1 (Q-69, Q-70).
- Botones del lápiz asignables y ajuste fino de la posición del lápiz: FT-134 y FT-135,
  deseables (C).
- Cursor del pincel y mira al pasar el lápiz sin tocar: FT-133, importante (S).
- Modo de interfaz mínima: FT-136, importante (S).
- Las acciones que disparan los gestos y atajos (deshacer, zoom, transformar…): son de sus
  specs. Esta spec solo dice cómo se disparan.

## Reglas de negocio

### RN-rechazo-de-palma-automatico · origin: Q-68
Desde que la app detecta un lápiz digital, los dedos y la palma dejan de pintar y solo hacen
gestos.

### RN-rechazo-de-palma-apagable · origin: Q-68
El rechazo de palma se puede apagar en los ajustes. Apagado, los dedos vuelven a pintar
aunque haya un lápiz digital.

### RN-dedo-pinta-sin-lapiz · origin: Q-68
Mientras la app no detectó ningún lápiz digital, un dedo pinta.

### RN-gesto-deshacer · origin: Q-69
Tocar el lienzo con dos dedos a la vez deshace (RN-deshacer, F-004).

### RN-gesto-rehacer · origin: Q-69
Tocar el lienzo con tres dedos a la vez rehace (RN-rehacer, F-004).

### RN-gestos-fijos · origin: Q-69
Los gestos son los de esta spec, los de vista de F-001 (RN-gestos-de-vista) y el cuentagotas
rápido de F-005 (RN-cuentagotas-rapido). No se pueden cambiar ni apagar.

### RN-trazo-interrumpido-por-gesto · origin: Q-72
Si un segundo dedo toca el lienzo en los primeros 0,3 segundos de un trazo hecho con el dedo,
el trazo se descarta sin dejar marca ni entrar en el historial, y se hace el gesto.

### RN-trazo-terminado-antes-del-gesto · origin: Q-72
Si el segundo dedo llega después de los primeros 0,3 segundos, el trazo se termina como está y
el gesto empieza.

### RN-atajos-windows · origin: Q-70
En Windows hay estos atajos de teclado, y no se pueden cambiar:

| Atajo | Acción |
|---|---|
| Ctrl+Z | Deshacer |
| Ctrl+Y o Ctrl+Shift+Z | Rehacer (Q-73) |
| B | Pincel |
| E | Goma |
| G | Balde |
| I | Cuentagotas |
| M | Selección rectangular o elíptica |
| L | Lazo |
| Ctrl+T | Transformar |
| Ctrl+C / Ctrl+X / Ctrl+V | Copiar / cortar / pegar |
| Ctrl+D | Deseleccionar |
| [ / ] | Achicar / agrandar el grosor del pincel o la goma |
| Espacio + arrastrar | Desplazar la vista |
| Ctrl+0 | Ajustar vista |
| Ctrl+Shift+N | Capa nueva |
| Ctrl+E | Combinar hacia abajo |

### RN-atajos-al-escribir · origin: Q-73
Mientras se escribe en un campo de texto, las teclas escriben y Ctrl+Z deshace el texto, no la
obra. Los atajos vuelven a funcionar al salir del campo.

### RN-atajo-sin-efecto · origin: Q-74
Un atajo cuya acción no se puede hacer en ese momento sigue la regla de esa acción. Por
ejemplo, Ctrl+E sobre la capa de más abajo sigue RN-combinar-la-de-abajo (F-003).

### RN-rueda-zoom · origin: Q-71
En Windows, la rueda del mouse, sola o con Ctrl, acerca y aleja la vista dejando fijo el punto
bajo el cursor, dentro de los límites de zoom (RN-limites-zoom, F-001).

### RN-tactil-en-windows · origin: Q-71
En un equipo Windows con pantalla táctil, los gestos y el rechazo de palma funcionan igual que
en Android.

## Requisitos no funcionales
- Un gesto de deshacer o rehacer responde con la misma demora que deshacer en F-004: menos de
  100 ms con la obra de referencia en gama media (Q-37, Q-74).

## Historias
| Historia | Qué cubre |
|---|---|
| US-033-rechazo-de-palma | Palma ignorada con lápiz, apagarlo, dedo que pinta sin lápiz, pantalla táctil en Windows |
| US-034-gestos | Dos dedos deshace, tres rehace, trazo descartado o terminado al llegar un gesto |
| US-035-atajos-de-teclado | Los atajos de Windows, escribiendo en un campo, atajo sin efecto, rueda del mouse |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- (ninguna pregunta abierta)
- Para arquitectura: distinguir lápiz, dedo y palma, y la presión en Windows (paso 5).
