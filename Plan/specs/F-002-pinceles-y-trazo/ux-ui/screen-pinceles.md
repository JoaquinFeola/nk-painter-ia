---
screen: Pinceles (barra de herramientas, panel Pincel y panel Goma, dentro del Editor)
stories: US-005, US-006, US-007, US-008
update-when: cambia un criterio de US-005 a US-008; llega el selector de color (F-005), el panel de capas (F-003), deshacer y rehacer (F-004) o una herramienta nueva a la barra (F-005, F-006); se agregan los atajos o la punta goma del lápiz (F-009)
---

# Pinceles

Esta pantalla no es nueva: es lo que F-002 suma al Editor (`../../F-001-lienzo-y-vista/ux-ui/screen-editor.md`),
que sigue valiendo para la barra superior, la vista y el panel Fondo. Los criterios se citan
por su orden en la historia: `US-005 CA-01` es el primero de US-005.

## Tarea
El ilustrador tiene una obra abierta y quiere dibujar con el pincel justo para lo que está
haciendo (bocetar con el Lápiz, entintar con la Tinta, rellenar con el Pincel redondo) y
corregir con la goma sin romper lo que está bien. Elige el pincel y lo deja ajustado una vez,
y después dibuja mucho, así que elegir y ajustar tiene que estar a un toque y no estorbar al
dibujar. A veces adapta la presión a su mano. **Acción principal: dibujar sobre el lienzo.**
Ningún control de la barra o de los paneles lleva el acento de acción principal.

## Regiones
1. **Barra de herramientas** — siempre visible, a la izquierda (abajo en ancho angosto): las
   herramientas Pincel y Goma, una activa. Cada una muestra su pincel o su forma.
2. **Panel Pincel** — se abre desde Pincel, sobre el lienzo: los 7 pinceles; debajo, los
   ajustes del pincel activo; al final, la curva de presión.
3. **Panel Goma** — se abre desde Goma, en el mismo lugar que el panel Pincel: la forma de la
   goma y, debajo, sus ajustes.
4. **Área de dibujo** — la del Editor: el lápiz digital, el dedo (en Android) y el mouse
   pintan con Pincel activo y borran con Goma activa.

## Elementos
| Región | Elemento | Origen |
|---|---|---|
| Barra de herramientas | **Pincel**: herramienta con el nombre del pincel activo debajo del ícono (por ejemplo, "Lápiz") | US-005 CA-02 a CA-04, RN-un-pincel-activo; sistema de diseño: barra siempre visible |
| Barra de herramientas | **Goma**: herramienta con el nombre de su forma debajo del ícono (por ejemplo, "Pincel redondo") | US-008 CA-01, CA-02, RN-goma-usa-forma-de-pincel |
| Barra de herramientas | una sola de las dos marcada como herramienta activa (fondo y texto en acento) | sistema de diseño: herramienta activa, una a la vez |
| Barra de herramientas | tocar la herramienta que no está activa la deja activa, sin abrir su panel; tocar la activa abre o cierra su panel | sistema de diseño: panel; propuesta del agente (sin confirmar) |
| Panel Pincel | lista de los 7 pinceles en el orden del set: Lápiz, Tinta, Rotulador, Pincel redondo, Aerógrafo, Pincel suave, Píxel; cada uno con su nombre y una muestra de su trazo | US-005 CA-01, RN-set-base |
| Panel Pincel | el pincel activo, marcado como elegido (fondo y texto en acento, no solo color); uno solo | US-005 CA-04, RN-un-pincel-activo; sistema de diseño: elegido |
| Panel Pincel | elegir un pincel lo deja activo en el momento; el panel sigue abierto y los ajustes de abajo pasan a ser los de ese pincel | US-005 CA-04, US-006 CA-11, RN-ajustes-por-pincel |
| Panel Pincel | **Grosor**: deslizador de 1 a 500 y campo numérico con `px` | US-006 CA-01, CA-06, CA-07, RN-rango-grosor; sistema de diseño: medidas con su unidad, cifras de ancho fijo |
| Panel Pincel | **Opacidad**: deslizador de 1 a 100 y campo numérico con `%` | US-006 CA-02, CA-06, CA-08, RN-rango-opacidad; sistema de diseño: medidas |
| Panel Pincel | **Estabilizador**: deslizador de 0 a 10 en pasos enteros y campo numérico; 0 se lee "0 (apagado)" | US-006 CA-09, RN-estabilizador-por-pincel |
| Panel Pincel | un valor escrito fuera de rango, al confirmarlo (Enter o salir del campo), queda en el límite más cercano y el campo y el deslizador muestran ese valor; no hay mensaje de error | US-006 CA-07, CA-08, CA-09, RN-ajuste-fuera-de-rango; sistema de diseño: voz (no es un error: el ajuste se hizo) |
| Panel Pincel | un campo vacío o con algo que no es número vuelve al valor que tenía al confirmarlo | propuesta del agente (sin confirmar); ningún criterio lo fija |
| Panel Pincel | un ajuste nuevo vale desde el trazo siguiente | US-006 CA-13, RN-ajustes-no-cambian-lo-pintado |
| Panel Pincel | **Presión**: Suave, Normal y Firme, una elegida; vale para todos los pinceles y la goma | US-007 CA-06 a CA-09, US-008 CA-07, RN-curva-tres-opciones, RN-curva-presion-unica |
| Panel Goma | **Forma**: los mismos 7 pinceles, en el mismo orden, con la forma elegida marcada; es la forma de la goma, no cambia el pincel activo | US-008 CA-01, CA-02, RN-goma-usa-forma-de-pincel, RN-goma-forma-inicial |
| Panel Goma | **Grosor**, **Opacidad** y **Estabilizador** de la goma, con los mismos controles y las mismas reglas que en el panel Pincel | US-008 CA-03, CA-04, RN-goma-usa-forma-de-pincel |
| Panel Goma | sin Presión: la curva se elige en el panel Pincel y vale también para la goma | RN-curva-presion-unica; propuesta del agente (sin confirmar) que no se repita el control |
| Paneles | se cierran tocando su herramienta de nuevo o fuera del panel; tocar fuera del panel no pinta ni borra; hay uno abierto a la vez | sistema de diseño: panel (uno por lado); propuesta del agente (sin confirmar) que el toque que cierra no pinte |
| Área de dibujo | con Pincel activo y el panel cerrado, dibujar con el lápiz digital, el mouse o (en Android) un dedo pinta con el pincel activo, sus ajustes y el color actual | US-005 CA-04 a CA-08, US-006 CA-01 a CA-05, US-007 CA-10 a CA-12 |
| Área de dibujo | con Goma activa, el mismo gesto borra en la capa activa con la forma y los ajustes de la goma; donde no queda pintura se ve el fondo | US-008 CA-02, CA-03, CA-08, CA-09, RN-goma-no-borra-fondo |
| Área de dibujo | dos dedos siguen moviendo la vista y nunca pintan ni borran | `screen-editor.md`, US-003 |

## Estados
| Estado | Qué se ve | Origen |
|---|---|---|
| Primera vez en el equipo | Pincel activo con "Lápiz"; Goma con "Pincel redondo"; en el panel Pincel, el Lápiz elegido con sus valores de fábrica y Presión en Normal | US-005 CA-02, US-008 CA-01, RN-pincel-inicial, RN-goma-forma-inicial; valores de fábrica y curva inicial (spec, *propuestos*) |
| Al reabrir la app | el último pincel usado con sus ajustes, la goma con su forma y sus ajustes, y la última curva elegida | US-005 CA-03, US-006 CA-12, US-007 CA-09, US-008 CA-04 |
| Herramienta activa al reabrir | Pincel | propuesta del agente (sin confirmar): ninguna regla pide recordar la herramienta; abrir con la goma lista sorprende al primer trazo |
| Panel abierto | el panel sobre el lienzo, sin taparlo entero; su herramienta sigue marcada; el lienzo se ve | sistema de diseño: panel |
| Moviendo un deslizador | el campo muestra el valor mientras se mueve; el ajuste se guarda al soltar (QT-98) | US-006 CA-12; design.md, QT-98 |
| Dibujando o borrando | el panel cerrado; el trazo sigue al lápiz; la barra no cambia | US-005 CA-12 |
| Cargando | no aplica: los pinceles son fijos y los ajustes se leen al abrir la app, antes de mostrar el editor | design.md, elegir pincel paso 3 |
| Vacío | no aplica: el set siempre tiene los 7 pinceles, y la goma siempre tiene forma | RN-set-base, RN-goma-forma-inicial |
| Error | no se muestra: si las preferencias no se pueden leer, valen las de fábrica y queda en el log (como los tamaños recientes); un valor fuera de rango no es un error | design.md, cuando un paso falla; RN-ajuste-fuera-de-rango |
| Sin permiso | no aplica: elegir y ajustar no pide permisos | — |
| Sin lápiz digital | igual que con lápiz: Presión se puede elegir aunque el dedo y el mouse pinten y borren a presión completa | US-007 CA-10, CA-11, RN-sin-presion |
| Éxito | el trazo o el borrado aparece; no hay mensaje | sistema de diseño: voz, éxito |
| Datos extremos | "Pincel redondo", el nombre más largo, entra entero en la barra (en dos líneas si hace falta) y en los paneles; el campo de Grosor entra con `500 px` sin moverse al cambiar de cifra; con grosor 500 el trazo puede ser más ancho que lo visible del lienzo y se pinta igual | RN-set-base, RN-rango-grosor; sistema de diseño: blancos de 48 dp, cifras de ancho fijo |

## Ancho angosto
Menos de 600 dp: la barra de herramientas va abajo y los paneles se abren como hojas desde
abajo. En el panel Pincel, la lista de pinceles va en una fila que se desplaza de costado; los
tres ajustes y Presión van debajo, uno por renglón, y la hoja se desplaza si no entra; la parte
de arriba del lienzo queda visible. El panel Goma, igual, con la fila de formas. Más ancho: la
barra a la izquierda y el panel flota junto a ella, con los pinceles en columna y los ajustes
debajo. En Windows, los paneles caben en la ventana de 700 px de ancho que se probó en el
Editor. La barra superior del Editor no cambia.

## Plataforma
- En Windows, el dedo no pinta ni borra en F-002: mueve la vista (QT-102 del diseño).
  US-007 CA-10 queda `blocked` en Windows hasta F-009.
- En Windows, las herramientas, cada pincel y forma, cada deslizador, cada campo y cada opción
  de Presión se alcanzan con el teclado, con el foco visible; las flechas mueven el deslizador
  de a un paso (sistema de diseño, accesibilidad). Los atajos son de F-009.
- La punta goma del lápiz digital no cambia de herramienta en F-002: es de F-009.

## Navegación
- Se llega desde: el Editor (`../../F-001-lienzo-y-vista/ux-ui/screen-editor.md`); los paneles
  se abren ahí mismo.
- Nada navega: elegir herramienta, pincel, forma, ajuste o curva cambia el estado de dibujo en
  esta pantalla.

## Lo que esta pantalla todavía no tiene
| Criterio | Por qué no está acá | Hasta cuándo |
|---|---|---|
| US-005 CA-05 · el color actual | el color es fijo hasta F-005 (#000000, Q-97); no hay selector | F-005 |
| US-005 CA-06, US-008 CA-10 · solo en la capa activa | elegir capa es del panel de capas | blocked hasta F-003 |
| US-005 CA-10, CA-11, US-008 CA-11 · deshacer | deshacer es de F-004 | blocked hasta F-004 |
| US-005 CA-12, CA-13 · demora y trazo continuo | no son elementos: son cómo responde el área de dibujo, y se miden | los mide QA (QT-92) |
| US-005 CA-07 · Píxel sin suavizado | se ve al acercar la vista (QT-99), sin control propio | — |
| US-006 CA-03 a CA-05, CA-10 · cómo se suma un trazo, el Aerógrafo y el estabilizador | no son elementos: es cómo pinta el pincel con sus ajustes | QA (unidad 41) |
| US-007 CA-01 a CA-05, US-008 CA-05 a CA-07 · cómo la presión cambia el trazo y el borrado | no son elementos: es cómo pinta o borra la forma elegida | QA; Android en la tableta del usuario (QT-90) |

## Pendiente
- Ninguna pregunta abierta. Propuestas del agente, sin decisión del usuario: el nombre del
  pincel o la forma en cada herramienta; el panel sigue abierto al elegir; el toque que cierra
  el panel no pinta; Presión dentro del panel Pincel y no repetida en el de Goma; la muestra
  del trazo de cada pincel; tocar la herramienta inactiva la activa sin abrir su panel; un campo
  inválido vuelve a su valor; la app abre con Pincel activo.
