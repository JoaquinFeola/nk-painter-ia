---
screen: Obra nueva
stories: US-001, US-002
update-when: cambia un criterio de US-001 o US-002, cambia la lista de prearmados (RN-tamanos-prearmados), llega la galería de F-007 (cambia desde dónde se llega) o el selector de color de F-005 (reemplaza el campo de color)
---

# Obra nueva

Los criterios se citan por su orden en la historia: `US-001 CA-01` es el primero de US-001.

## Tarea
El ilustrador quiere empezar una obra del tamaño justo para su destino: elige un prearmado,
un tamaño reciente o uno personalizado, y el fondo. **Acción principal: Crear.**

## Regiones
1. **Prearmados** — los tamaños con nombre.
2. **Recientes** — los últimos tamaños usados en este equipo.
3. **Personalizado** — ancho, alto, unidad y resolución. Es el formulario que se crea: elegir
   un prearmado o un reciente lo llena.
4. **Fondo** — blanco, otro color o transparente.
5. **Crear** — el resumen de errores y la acción principal.

## Elementos
| Región | Elemento | Origen |
|---|---|---|
| Prearmados | A4 · 2480×3508 px · 300 dpi | US-001 CA-01, RN-tamanos-prearmados |
| Prearmados | A5 · 1748×2480 px · 300 dpi | RN-tamanos-prearmados |
| Prearmados | Full HD · 1920×1080 px · 72 dpi | US-001 CA-02 |
| Prearmados | 2K cuadrado · 2048×2048 px · 72 dpi | RN-tamanos-prearmados |
| Prearmados | Pantalla · el ancho×alto de la pantalla en ese momento · 72 dpi; se vuelve a leer al elegirlo | US-001 CA-03, RN-prearmado-pantalla, QT-60 |
| Prearmados | el elegido se marca como herramienta activa (acento y forma) | sistema de diseño: herramienta activa |
| Recientes | hasta 5 tamaños, del más reciente al más viejo, cada uno como ancho×alto px · dpi | US-001 CA-07 a CA-10 |
| Personalizado | ancho y alto, con la unidad elegida | US-001 CA-04 a CA-06 |
| Personalizado | unidad: px, mm o pulg | US-001 CA-04 a CA-06, RN-tamano-personalizado |
| Personalizado | resolución en dpi; acepta decimales con coma o punto para poder rechazarlos | US-001 CA-04, US-002 CA-04 a CA-07 |
| Personalizado | en mm o pulg, el tamaño resultante en px, que se actualiza al escribir (por ejemplo, 1181×1772 px) | US-001 CA-05, US-002 CA-03 |
| Fondo | Blanco, elegido de entrada | US-001 CA-11, RN-fondo-inicial |
| Fondo | Color: campo `#RRGGBB` con una muestra al lado | US-001 CA-12, Q-92; lo reemplaza el selector de F-005 |
| Fondo | Transparente, con la muestra en damero | US-001 CA-13; sistema de diseño: damero |
| Crear | resumen con todos los errores juntos | US-002 CA-08, RN-errores-juntos; sistema de diseño: formularios |
| Crear | botón Crear | sistema de diseño: acción principal, el verbo |

## Mensajes de error
Junto al campo que lo causa y repetidos en el resumen. Las frases salen de los criterios y
las confirmó el usuario (Q-91).

| Motivo | Dónde | Texto | Origen |
|---|---|---|---|
| Un lado menor que 16 px | ancho o alto (el que falla) | El mínimo es 16 px por lado. | US-002 CA-01, CA-03 |
| Resolución menor que 72 o mayor que 600 | resolución | La resolución va de 72 a 600 dpi. | US-002 CA-04, CA-05 |
| Resolución con decimales | resolución | La resolución es un número entero de 72 a 600 dpi. | US-002 CA-07 |
| Por encima de la línea roja | resumen, y ancho y alto marcados | Este equipo admite hasta 7500×10000 px. (con el máximo de la misma proporción del pedido) | US-002 CA-12, QT-65 |

## Estados
| Estado | Qué se ve | Origen |
|---|---|---|
| Al entrar | A4 elegido y sus valores en Personalizado; fondo blanco elegido | Q-93, US-001 CA-11 |
| Cargando | no aplica: los prearmados están en la app y los recientes son 5 líneas en el equipo; se muestran al abrir | QT-67 |
| Sin recientes | la región Recientes no se muestra | US-001 CA-07 (solo pide mostrar los que hay) |
| Recientes que no se pudieron leer | igual que sin recientes; la pantalla sigue | `design.md`, cuando un paso falla |
| Pantalla no disponible | el prearmado Pantalla no se muestra esa vez | `design.md`, cuando un paso falla |
| Errores del pedido | cada campo con su error y el resumen con todos; no se crea nada; se conserva lo escrito | US-002 CA-01, CA-03 a CA-05, CA-07, CA-08, CA-12 |
| Aviso de lienzo grande | diálogo de aviso sobre la pantalla, con dos salidas; la obra todavía no existe | US-002 CA-09, RN-aviso-lienzo-grande; sistema de diseño: aviso |
| Aviso: sigue | se crea con el tamaño pedido y se abre el editor | US-002 CA-10 |
| Aviso: no sigue | se cierra el diálogo, vuelve al formulario como estaba, no se crea nada | US-002 CA-11 |
| Sin aviso en un equipo más grande | el mismo pedido se crea directo | US-002 CA-13 |
| Éxito | se abre el editor; no hay mensaje | US-001 CA-14; sistema de diseño: voz, éxito |
| Sin permiso | no aplica: crear una obra no pide permisos (la app no pide almacenamiento ni internet) | `android-stack.md`, convenciones |
| Datos extremos | un número de muchas cifras (999999×999999) se acepta en el campo y lo rechaza la línea roja con su máximo; los recientes nunca pasan de 5; en el ancho más chico, el prearmado más largo ("2K cuadrado · 2048×2048 px · 72 dpi") pasa a dos líneas | US-001 CA-08, US-002 CA-12 |
| Parcial | no aplica: la pantalla no muestra nada que llegue por partes | — |

El diálogo de aviso (Q-91): "Este lienzo es grande para este equipo. ¿Quieres crearlo
igual?" con **Crear igual** y **Cancelar**.

## Ancho angosto
Menos de 600 dp: las regiones se apilan en orden y la pantalla se desplaza; los prearmados son
una fila que se desplaza hacia el costado; el resumen de errores y Crear quedan fijos abajo,
siempre a la vista. Más ancho: dos columnas, Prearmados y Recientes a la izquierda,
Personalizado y Fondo a la derecha, y Crear abajo a la derecha. En Windows es la misma
pantalla dentro de la ventana de la app.

## Navegación
- Se llega desde: el arranque de la app, hasta que exista la galería de F-007 (`screen-galeria.md`
  de F-007, sin escribir).
- **Crear** sin errores ni aviso → el editor con la obra nueva (`screen-editor.md`, se diseña
  en la unidad 28; en el tramo 3 es el lienzo del esqueleto).
- **Crear igual** en el aviso → el editor. **Cancelar** → vuelve a esta pantalla.
- Con errores no se navega.

## Qué se ve en otra pantalla
- US-001 CA-14 (obra entera, centrada, a 0°, con "Capa 1" activa) se ve en el editor. La vista
  ajustada se comprueba en el tramo 3; el nombre "Capa 1" como capa activa no se ve en
  pantalla hasta el panel de capas de F-003, así que en el tramo 3 lo prueba el test del
  dominio y la parte visible queda `blocked` hasta F-003.

## Preguntas de esta pantalla
- **Q-91 · ¿Cómo se escriben los textos de error y el aviso?**
  - usuario · 2026-10-27: como los propuso el agente (tablas de arriba y el aviso con Crear
    igual / Cancelar).
- **Q-92 · ¿Cómo se elige el color de fondo antes del selector de color de F-005?**
  - usuario · 2026-10-27: campo `#RRGGBB` con muestra; F-005 lo reemplaza.
- **Q-93 · ¿Qué tamaño viene elegido al entrar?**
  - usuario · 2026-10-27: siempre A4. Recomendé el reciente más nuevo; el usuario prefirió A4.

## Pendiente
- Ninguna pregunta abierta.
