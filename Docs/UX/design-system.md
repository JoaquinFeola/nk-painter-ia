---
update-when: cambia un rol, una convención, la marca o la voz; se escribe el tema en código (los valores `provisional` pasan al tema y salen de acá); un tema claro u otro idioma entran al producto
---

# Sistema de diseño — nk-painter

Base: Material 3 1.9.0 sobre Compose Multiplatform (QT-47), con un solo tema oscuro neutro
(Q-86). Este documento dice qué rol cumple cada cosa, cuándo se usa y su límite; los valores
viven en el tema cuando se escriba en código (unidad 24). Hasta entonces, los valores de acá
eran `provisional`; desde la unidad 25 viven en el tema.

## Dirección visual
- **El lienzo es lo único con color.** La interfaz es gris sin tinte, así no tiñe cómo el
  ilustrador ve los colores de su obra (Q-86).
- **Mucho lienzo, herramientas a mano** (Q-87): una barra fina arriba y una barra de
  herramientas siempre visibles; capas, color y pincel se abren como paneles sobre el lienzo y
  se cierran.
- **Lo audaz va en un solo lugar: el acento verde agua**, que marca la acción principal y la
  herramienta activa. Nada más en la interfaz lleva color propio, salvo aviso y peligro.
- Sin tarjetas con sombra, sin degradés, sin etiquetas en mayúsculas sobre los títulos. Los
  bordes aparecen donde separan algo que se toca de lo que no.

## Marca
- **Logo**: `Docs/UX/brand/logo.svg` (Q-88): un trazo de lápiz que dibuja una "n" y un punto
  donde el lápiz se levanta. Va en el ícono de la app, el instalador de Windows y la galería
  vacía. No va sobre el lienzo ni en las imágenes exportadas.
- **Nombre**: nk-painter, en minúsculas.
- **Tipografía**: la sans del sistema (Roboto en Android, Segoe UI en Windows), con cifras de
  ancho fijo para tamaños, zoom y grados, así los números no bailan mientras cambian.

## Paleta por rol
Los valores viven en el tema: `shared/src/commonMain/kotlin/com/nkpainter/themes/NkPainterTheme.kt`
y `NkPainterColors.kt` (unidad 25); el lienzo de Android repite los suyos en `PenCanvasView`.
Acá quedan los roles y el contraste medido.

| Rol | Contraste medido |
|---|---|
| Superficie (barras), superficie de panel, área de dibujo | — |
| Texto | 14,6:1 en barras, 12,9:1 en panel |
| Texto secundario | 7,2:1 en barras, 6,4:1 en panel |
| Acento: acción principal | 9,3:1 en barras, 7,7:1 en el área de dibujo; texto sobre acento 9,3:1 |
| Elegido (prearmado, herramienta activa): fondo verde oscuro con texto en acento | 6,5:1 |
| Aviso | 8,5:1 en panel |
| Peligro | 6,7:1 en barras, 6,0:1 en panel |
| Borde de control | 3,8:1 en barras, 3,3:1 en panel, 3,1:1 en el área |

Contraste WCAG calculado el 2026-10-27 y el 2026-10-28 con la fórmula de luminancia relativa.

## Roles
| Rol | Cuándo se usa | Su límite |
|---|---|---|
| Acción principal | lo único para lo que está esa vista o ese diálogo | una por vista; botón lleno con el acento |
| Acción secundaria | otra salida de la misma vista (cancelar, volver) | texto o borde, nunca lleno |
| Herramienta activa | la herramienta o el panel que está en uso | una a la vez; marca con el acento y con forma (fondo o subrayado), no solo color |
| Acción destructiva | quita algo que no se recupera con deshacer | siempre confirmada; el botón que confirma usa peligro y dice el verbo |
| Aviso | una consecuencia que conviene leer antes de seguir (por ejemplo, un lienzo grande para el equipo) | nunca para un éxito; ícono y texto, nunca solo color; el ilustrador decide |
| Error | algo que impidió hacer lo pedido | dice qué pasó y qué se admite; nunca culpa |
| Panel | herramientas que se abren sobre el lienzo (capas, color, pincel) | uno abierto por lado; nunca tapa todo el lienzo en tableta o PC |

## Convenciones
- **Ancho angosto** (menos de 600 dp, el ancho compacto de Material): la barra de
  herramientas va abajo y los paneles se abren como hojas desde abajo. Más ancho: la barra va
  a la izquierda y los paneles flotan junto a ella.
- **Formularios**: cada error junto al campo que lo causó y todos repetidos en un resumen
  arriba de la acción principal, a la vez; el formulario conserva lo escrito.
- **Medidas**: siempre con su unidad (px, mm, pulg, dpi, %, °). Tamaños como `2480×3508 px`,
  sin separador de miles, con el signo ×.
- **El lienzo** se dibuja con un borde fino de color borde de control, así se ve su contorno
  aunque su fondo se parezca al área de dibujo. Un fondo transparente se ve como damero claro
  (los dos grises del damero están en el tema).
- **Gestos de vista**: mientras dura un gesto, un indicador chico muestra el zoom y el giro, y
  se va al soltar.
- **Confirmaciones**: el botón dice el verbo de lo que hace ("Crear", "Borrar"), nunca
  "Aceptar".
- **Esperas**: una operación de más de medio segundo muestra progreso; si se puede medir, en
  porcentaje.

## Accesibilidad — cómo se cumple
- Contraste mínimo 4,5:1 para texto y 3:1 para controles y gráficos con significado (medido
  en la paleta de arriba).
- Ningún estado se indica solo con color: la herramienta activa y los avisos llevan forma o
  ícono.
- Blancos táctiles de 48×48 dp en Android; 24×24 px como mínimo en Windows con mouse.
- En Windows, todo control se alcanza con el teclado y el foco se ve.
- Pendiente: el requisito verificable de accesibilidad del producto no existe todavía
  (Q-90). Esta sección dice cómo se cumple; qué se promete es del producto.

## Voz
Tú, español neutro, breve (Q-89). Un texto que fija una historia se copia de la historia; la
voz sirve para escribir el resto sin preguntar.

| Caso | Cómo | Ejemplo |
|---|---|---|
| Error | qué pasó y qué se admite, sin culpar | "El mínimo es 16 px por lado." |
| Aviso | la consecuencia y la decisión | "Este lienzo es grande para este equipo y puede ir lento. ¿Quieres crearlo igual?" |
| Estado vacío | invita a la primera acción | "Aún no tienes obras. Crea una para empezar." |
| Botón | el verbo de lo que hace | "Crear", "Ajustar vista" |
| Éxito | no se anuncia si se ve en pantalla | la obra nueva se abre; no hace falta un mensaje |

## Preguntas de este documento
- **Q-86 · ¿De qué tono es la interfaz alrededor del lienzo?**
  - usuario · 2026-10-27: oscuro neutro, un solo tema.
- **Q-87 · ¿Cuánto lugar ocupan las herramientas mientras se dibuja?**
  - usuario · 2026-10-27: barras finas siempre visibles y paneles que se ocultan.
- **Q-88 · ¿Hay logo o colores de marca para nk-painter?**
  - usuario · 2026-10-27: todavía no; el agente puede crear uno. Logo y acento verde agua
    propuestos por el agente en esta unidad; el usuario puede objetarlos.
- **Q-89 · ¿Cómo le habla la app al ilustrador?**
  - usuario · 2026-10-27: tú, español neutro.
- **Q-90 · ¿Qué promete el producto en accesibilidad (requisito verificable)?** Abierta; es
  de discovery (`Docs/Product/product.md` no tiene requisito de accesibilidad). No bloquea las
  pantallas de F-001.
