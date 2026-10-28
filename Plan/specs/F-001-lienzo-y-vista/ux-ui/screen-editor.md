---
screen: Editor
stories: US-003, US-004 (y US-001 CA-14, que se ve acá)
update-when: cambia un criterio de US-003 o US-004; llega la barra de herramientas (F-002), el panel de capas (F-003), deshacer y rehacer (F-004), el selector de color (F-005, reemplaza el campo de color del panel Fondo) o la salida a la galería (F-007)
---

# Editor

Los criterios se citan por su orden en la historia: `US-003 CA-01` es el primero de US-003.

## Tarea
El ilustrador tiene una obra abierta y quiere mirarla como mira una hoja de papel: acercar
el detalle, alejarse para ver la composición, girarla para que el trazo le quede cómodo, y
volver a verla entera. A veces, además, prueba el dibujo sobre otro fondo. **Acción
principal: dibujar sobre el lienzo**, que es de F-002. En F-001 el lienzo es lo que ocupa la
pantalla, y la vista se mueve con dos dedos. Ninguna acción de la barra compite con el lienzo,
así que ninguna lleva el acento.

## Regiones
1. **Barra superior** — fina, siempre visible: las acciones sobre la vista y la obra.
2. **Área de dibujo** — el lienzo, a través de la vista, con su fondo y su borde.
3. **Indicador de vista** — sobre el área, solo mientras dura un gesto.
4. **Panel Fondo** — se abre desde la barra, sobre el lienzo, y se cierra.

La barra de herramientas (sistema de diseño: "una barra de herramientas siempre visible") no
se dibuja en F-001. Llega con F-002: la herramienta Pincel y su panel están en
`../../F-002-pinceles-y-trazo/ux-ui/screen-pinceles.md`.

## Elementos
| Región | Elemento | Origen |
|---|---|---|
| Barra superior | **Ajustar vista**: acción secundaria con ese texto | US-003 CA-08, RN-ajustar-vista; voz del sistema de diseño: botón con el verbo ("Ajustar vista") |
| Barra superior | **Fondo**: abre el panel Fondo; mientras está abierto se marca como herramienta activa (acento y forma) | US-004 CA-01 a CA-03; sistema de diseño: panel, herramienta activa |
| Área de dibujo | el lienzo con su fondo (color o damero) y su borde fino, dibujado a través de la vista | US-001 CA-11 a CA-14; sistema de diseño: el lienzo |
| Área de dibujo | dos dedos: pellizcar acerca o aleja, arrastrar desplaza, girar gira; nunca pintan | US-003 CA-01, CA-04, CA-05, CA-09, RN-gestos-de-vista |
| Área de dibujo | al pellizcar, el punto bajo el centro de los dedos se queda bajo los dedos | US-003 CA-01, RN-zoom-centro-de-dedos |
| Área de dibujo | el zoom se detiene en 5 % y en 3200 %: el gesto sigue y la vista no cambia | US-003 CA-02, CA-03, RN-limites-zoom |
| Área de dibujo | al soltar un giro a 5° o menos de 0°, 90°, 180° o 270°, la vista se pega a ese ángulo | US-003 CA-06, CA-07, RN-rotacion-con-imanes |
| Indicador de vista | zoom y giro mientras dura el gesto, como `250 % · 45°`, con cifras de ancho fijo; se va al soltar | sistema de diseño: gestos de vista, tipografía, medidas |
| Panel Fondo | **Blanco**, **Color** y **Transparente**, uno elegido: el fondo actual de la obra | US-004 CA-01 a CA-03; mismas opciones que la región Fondo de `screen-obra-nueva.md` |
| Panel Fondo | con Color: campo `#RRGGBB` con una muestra al lado | US-004 CA-01, CA-03; Q-92 (lo reemplaza el selector de F-005) |
| Panel Fondo | elegir Blanco o Transparente, o escribir un color válido, cambia el fondo en el momento; no hay botón de confirmar | US-004 CA-01 a CA-03; sistema de diseño: panel (herramientas que se abren y se cierran) |
| Panel Fondo | un color mal escrito: "Escribe un color como #F5E6C8." junto al campo; el fondo no cambia | mismo mensaje que el campo de color de Obra nueva (Q-92) |
| Panel Fondo | se cierra tocando Fondo de nuevo o fuera del panel | sistema de diseño: panel |

## Estados
| Estado | Qué se ve | Origen |
|---|---|---|
| Al abrir una obra nueva | el lienzo entero, centrado, a 0°; panel cerrado; sin indicador | US-001 CA-14, RN-obra-nueva-lista |
| Cargando | no aplica en F-001: la obra se crea en memoria y se muestra al instante. Abrir una obra guardada es de F-007, que define su espera | design.md, flujo de creación |
| Vacío | no aplica: el editor no existe sin una obra abierta | `NkPainterApp` abre el editor solo con obra |
| Error | no aplica: mover la vista y cambiar el fondo no tienen cómo fallar (design.md: "gestos y ajustar no tienen efecto fuera de la vista") | design.md |
| Parcial | no aplica: la obra se muestra entera de una vez | — |
| Sin permiso | no aplica: el editor no pide permisos | `android-stack.md`, convenciones |
| Gesto en curso | el indicador muestra zoom y giro; el lienzo sigue a los dedos | sistema de diseño: gestos de vista |
| En un límite de zoom | el indicador se queda en `3200 %` o `5 %` mientras los dedos siguen | US-003 CA-02, CA-03 |
| Giro pegado | al soltar a 86°, el lienzo queda a 90° y el indicador se va | US-003 CA-06 |
| Panel Fondo abierto | el panel sobre el lienzo; Fondo marcado como activo; el lienzo sigue visible y muestra cada cambio | sistema de diseño: panel ("nunca tapa todo el lienzo en tableta o PC") |
| Mismo fondo elegido | nada cambia en pantalla | US-004 CA-06 (que tampoco deja un paso en el historial: F-004) |
| Éxito | el cambio se ve en el lienzo; no hay mensaje | sistema de diseño: voz, éxito |
| Datos extremos | un lienzo de 16×16 px: al ajustar se ve a 3200 %, más chico que el área, centrado (el zoom no pasa del límite). Uno de 20698×20698 px: al alejar se detiene en 5 %. Un giro de 359° se muestra `359°`, no `-1°` | RN-limites-zoom, RN-ajustar-vista; observado en QA de US-002 (16×16 a 514 px) |

## Ancho angosto
Menos de 600 dp: la barra superior se queda arriba, con Ajustar vista y Fondo. El panel Fondo
se abre como hoja desde abajo y deja visible la parte de arriba del lienzo. Más ancho: el panel
flota junto a la barra. En Windows, el panel cabe en una ventana de 700 px de ancho, el mismo
mínimo que se probó en Obra nueva.

## Lo que esta pantalla todavía no tiene
| Criterio | Por qué no está acá | Hasta cuándo |
|---|---|---|
| US-003 CA-10 · deshacer no toca la vista | deshacer es de F-004 | blocked hasta F-004 (QT-59) |
| US-003 CA-11 · la vista vuelve al reabrir | reabrir es de F-007 | blocked hasta F-007 (QT-59) |
| US-003 CA-12 · la vista viaja al importar | importar es de F-008 | blocked hasta F-008 (QT-59) |
| US-003 CA-13 · 60 cuadros por segundo | no es un elemento: es cómo responde esta pantalla, y se mide (unidad 20) | lo mide QA |
| US-004 CA-04, CA-05 · deshacer y rehacer el fondo | F-004 | blocked hasta F-004 (QT-59) |
| US-004 CA-06 · el mismo fondo no deja un paso | la parte visible está arriba; el paso en el historial es de F-004 | blocked hasta F-004 |
| US-004 CA-07 · el fondo se mantiene al reabrir | F-007 | blocked hasta F-007 |
| US-001 CA-14 · "Capa 1" activa a la vista | panel de capas de F-003 | blocked hasta F-003 |

## Plataforma
- En Windows, los gestos de vista necesitan una pantalla táctil. En una PC sin táctil, los
  criterios de gestos quedan `blocked` hasta tener una (nota de US-003). La rueda del mouse y
  espacio con arrastre son de F-009.
- Ajustar vista y Fondo se alcanzan con el teclado en Windows, con el foco visible (sistema de
  diseño, accesibilidad).

## Navegación
- Se llega desde: Obra nueva, con Crear o Crear igual (`screen-obra-nueva.md`).
- Ajustar vista y Fondo no navegan: cambian la vista o el fondo en esta pantalla.
- No hay salida del editor en F-001: volver a la galería llega con F-007 (`screen-galeria.md`,
  sin escribir). Hasta entonces se sale cerrando la app.

## Pendiente
- Ninguna pregunta abierta. Propuestas del agente en esta pantalla, sin decisión del usuario:
  Ajustar vista y Fondo en la barra superior; el fondo cambia sin confirmar; el indicador como
  `250 % · 45°` con el giro de 0° a 359°.
