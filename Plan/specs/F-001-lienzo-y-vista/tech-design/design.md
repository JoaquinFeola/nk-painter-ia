# Diseño — F-001 Lienzo y vista

Cómo se resuelve F-001 dentro del componente nk-painter (`./`), con el código compartido y la
entrada de cada plataforma. Las preguntas y de dónde salió cada respuesta: `decisions.md`
(QT-58 a QT-74).

## Flujos

Crear una obra (US-001, US-002):
1. Al abrir, la app lee la memoria del equipo por la interfaz de memoria de cada plataforma y
   calcula una vez el límite del equipo: umbral de aviso y línea roja (QT-62). Si QA los fijó
   al lanzar, se usan esos (QT-63).
2. La pantalla de obra nueva muestra los prearmados, los recientes (del repositorio de
   tamaños recientes, QT-67) y el formulario personalizado. El prearmado de pantalla pide el
   tamaño actual de la pantalla a la plataforma en el momento de elegirlo (QT-60).
3. El código común pasa la medida a píxeles (QT-66) y valida el pedido entero: todos los
   motivos de rechazo juntos, incluida la línea roja con el máximo de la misma proporción
   (QT-64, QT-65). Lo que la UI deja escribir no cuenta: la validación es la del dominio.
4. Si no hay rechazo y el lienzo supera el umbral de aviso, la UI pide confirmar. Si el
   ilustrador no sigue, no se crea nada.
5. Se crea la obra en memoria: lienzo, fondo, "Capa 1" vacía y activa, vista ajustada al área
   de dibujo (RN-obra-nueva-lista). Una capa vacía no reserva mosaicos: crear no reserva
   memoria de píxeles.
6. Se registra el tamaño en los recientes y se abre la obra. La obra vive en un contenedor de
   la app, no de la pantalla (QT-72). No se guarda en disco hasta F-007.

Navegar la vista (US-003):
1. La entrada de la plataforma reparte los punteros (QT-70): el lápiz pinta; dos dedos van al
   reconocedor de gestos común. En Android, si un dedo estaba pintando, su trazo se cancela en
   el front buffer. En Windows el táctil llega por el procedimiento de ventana del módulo de
   lápiz (QT-61).
2. Por cada movimiento, el reconocedor da desplazamiento, escala y giro alrededor del centro de
   los dedos, y la vista se actualiza con zoom recortado a 5 %–3200 % (QT-69). La obra no se
   toca: la vista no pasa por el historial.
3. La pantalla redibuja la composición guardada con la transformación de la vista: en Android
   con `renderMultiBufferedLayer` del renderizador de baja demora; en Windows con el lienzo de
   Compose. Se dibujan solo los mosaicos visibles del nivel reducido que corresponde al zoom
   (QT-73). Ningún cuadro recombina capas (QT-58).
4. Al levantar los dedos, el giro se pega al ángulo recto si quedó a 5° o menos (QT-69).
5. Ajustar pone la vista en 0°, centrada y con el lienzo entero dentro del área de dibujo.

Pintar con la vista movida (lo que F-001 cambia del trazo del esqueleto):
1. Cada punto del lápiz se lleva de la pantalla al lienzo con la inversa de la vista (QT-70).
2. El trazo húmedo de Android se dibuja en el front buffer con la misma transformación.
3. Al pintar los mosaicos de la capa, se recombina cada mosaico tocado con todas las capas
   visibles y se actualizan sus niveles reducidos y su imagen en pantalla (QT-58, QT-73).

Cambiar el fondo (US-004):
1. La obra cambia su fondo si es distinto del actual; si es el mismo, no hace nada
   (RN-fondo-cambiable).
2. La pantalla dibuja el nuevo fondo debajo de la composición; no se recombina nada (QT-71).
3. Que el cambio entre en el historial llega con F-004 (QT-59).

## Dónde se cumple cada regla
| Regla | Se cumple en |
|---|---|
| RN-tamanos-prearmados | Lista de `CanvasPreset` en el código común |
| RN-prearmado-pantalla | Interfaz de pantalla de cada plataforma, leída al elegir el prearmado (QT-60) |
| RN-tamano-personalizado | Conversión de medida en el pedido de obra nueva (QT-66) |
| RN-dpi-fuera-de-rango | `Resolution`, al validar el pedido |
| RN-tamano-minimo | `CanvasSize`, al validar el pedido, sobre los píxeles ya convertidos |
| RN-tamanos-recientes | Repositorio de tamaños recientes: quita el repetido, pone el nuevo primero y guarda 5 |
| RN-obra-nueva-lista | Creación de `Artwork`: una capa vacía activa y vista ajustada |
| RN-aviso-lienzo-grande | `DeviceCanvasLimit` al validar; la UI solo pregunta lo que el dominio pidió confirmar |
| RN-linea-roja-lienzo | `DeviceCanvasLimit` al validar, con el máximo de la misma proporción (QT-65) |
| RN-errores-juntos | Validación del pedido: junta los motivos antes de responder (QT-64) |
| RN-fondo-inicial | Pedido de obra nueva: blanco si no se eligió otro |
| RN-fondo-cambiable | `Artwork` cambia el fondo solo si es distinto; el historial, con F-004 |
| RN-vista-no-cambia-obra | La vista cambia sin pasar por el historial ni por los mosaicos |
| RN-vista-se-recuerda | `Viewport` en el manifiesto (campo `view` del formato v1); guardar y reabrir llegan con F-007 y F-008 |
| RN-limites-zoom | `Viewport`: recorta el zoom en cada cambio y al ajustar |
| RN-rotacion-con-imanes | `Viewport`: imán al terminar el gesto |
| RN-zoom-centro-de-dedos | `Viewport`: escala y giro alrededor del centro de los dedos |
| RN-ajustar-vista | `Viewport` ajustado al área de dibujo |
| RN-gestos-de-vista | Reparto de punteros de cada plataforma más el reconocedor de gestos común (QT-70) |

## Datos
- **Obra** (`Artwork`), agregado: lienzo, fondo, capas (de F-003; en F-001, solo "Capa 1") y
  vista. Hasta F-007 vive solo en memoria.
- **Tamaño de lienzo** (`CanvasSize`): ancho y alto en píxeles con su resolución entera. Se
  congela al crear: F-001 no lo cambia (FT-04 queda afuera).
- **Fondo** (`CanvasBackground`): transparente o un color sRGB.
- **Vista** (`Viewport`): centro en píxeles del lienzo, zoom y giro en grados. En coordenadas
  del lienzo y no de la pantalla, como pide el formato (QT-35).
- **Límite del equipo** (`DeviceCanvasLimit`): umbral de aviso y línea roja en píxeles. Se
  calcula al abrir la app y no se guarda.
- **Tamaños recientes**: hasta 5 `CanvasSize`, en las preferencias de la app del equipo.
- **Composición guardada**: por mosaico, las capas visibles mezcladas sobre transparente, con
  sus niveles reducidos. Es derivada: se rehace desde las capas y no se guarda en disco.

## Cuando un paso falla
- Crear tiene un solo efecto, la obra en memoria; registrar el reciente va después. Si
  guardar el reciente falla, la obra queda creada y el reciente se pierde: no se avisa, porque
  no hay nada que el ilustrador pueda hacer.
- Si la plataforma no da el tamaño de la pantalla, el prearmado de pantalla no se ofrece en
  esa vez; los demás siguen.
- Si no se puede leer la memoria del equipo, no hay límite que calcular: se usan los valores
  de un Android de 4 GB (QT-62) y se registra en el log de la app.
- Gestos y ajustar no tienen efecto fuera de la vista: no hay nada que deshacer si fallan.
- Que el sistema cierre la app pierde la obra hasta F-007 (QT-72).

## Integraciones
Ninguna con sistemas externos. Usa interfaces de la plataforma ya previstas en
`Docs/Architecture/architecture.md`: memoria del equipo y entrada del lápiz, más una nueva de
tamaño de pantalla.

## Qué queda para otras features
- Deshacer el cambio de fondo, reabrir e importar la vista: F-004, F-007, F-008 (QT-59). Sus 7
  criterios quedan `blocked` hasta entonces.
- La regla de 0,3 s del trazo con un dedo y el dedo pintando en Windows: F-009.
- Dos composiciones, debajo y encima de la capa activa: decididas en el diseño de F-002 (QT-89).

## Preguntas abiertas
Ninguna. Sin verificar, con su prueba asignada en `decisions.md`: tamaño de pantalla en
píxeles físicos en Windows (QT-60), táctil real en Windows (QT-61), máximo de memoria directa
de la JVM (QT-62). Las fracciones de QT-62 se calibran en la unidad 20.
