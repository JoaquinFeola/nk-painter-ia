# Decisiones de diseño — F-001 Lienzo y vista

Registro append-only. Cada entrada dice de dónde salió: `usuario`, `agente, dentro del alcance`,
`investigado` (con fuente y fecha), `observado`, `propuesta` o `[TO-DEFINE]`.
La numeración sigue la serie de `Docs/Architecture/architecture-decisions.md` (QT-57 es la
última de ese log), para que ningún QT-NN del proyecto se repita.

Modo elegido: **propuestas** · 2026-10-27 (QT-74)

## 2026-10-27 · Preguntas derivadas de las reglas y las historias

Tradeoffs (se preguntan): QT-58, QT-59. Hechos (se investigan): QT-60, QT-61.
Detalles dentro del alcance (según el modo): QT-62 a QT-73.

## QT-58 · ¿Cómo se mueve la vista a 60 cuadros por segundo si combinar las 30 capas de una pantalla toma 193 ms (QT-51)?
- tipo: tradeoff (memoria contra velocidad; condiciona F-002)
- reglas: requisito no funcional de F-001, RN-vista-no-cambia-obra

## QT-59 · Los criterios que piden deshacer (F-004), reabrir (F-007) e importar (F-008), ¿esperan a esas features o se construye ahora lo mínimo?
- tipo: tradeoff de alcance
- criterios: US-003 "mover la vista no entra en el historial", "al reabrir…", "la vista viaja…"; US-004 "deshacer…", "rehacer…", "elegir el mismo fondo…", "el fondo cambiado se mantiene al reabrir"

## QT-60 · ¿Cómo se lee el tamaño en píxeles de la pantalla y su orientación actual, en Android y en Windows?
- tipo: hecho verificable
- reglas: RN-prearmado-pantalla

## QT-61 · En Windows, ¿llegan los toques de dos dedos de una pantalla táctil a la ventana, con el procedimiento de ventana que ya reemplaza el módulo de lápiz?
- tipo: hecho verificable
- reglas: RN-gestos-de-vista, RN-tactil-en-windows (F-009)

## QT-62 · ¿Con qué cuenta se sacan el umbral de aviso y la línea roja de la memoria del equipo?
- tipo: detalle dentro del alcance (QT-10 fijó el enfoque; las fracciones se calibran en la unidad 20)
- reglas: RN-aviso-lienzo-grande, RN-linea-roja-lienzo

## QT-63 · ¿Cómo fija QA el umbral de aviso y la línea roja para probar US-002?
- tipo: detalle dentro del alcance

## QT-64 · ¿En qué orden se validan el pedido y el límite del equipo, y qué se informa junto?
- tipo: detalle dentro del alcance
- reglas: RN-errores-juntos, RN-tamano-minimo, RN-dpi-fuera-de-rango, RN-linea-roja-lienzo, RN-aviso-lienzo-grande

## QT-65 · ¿Cómo se calcula el tamaño más grande con la misma proporción que se informa en la línea roja?
- tipo: detalle dentro del alcance
- reglas: RN-linea-roja-lienzo

## QT-66 · ¿Cómo se pasan milímetros y pulgadas a píxeles, y cómo se redondea un medio píxel?
- tipo: detalle dentro del alcance
- reglas: RN-tamano-personalizado

## QT-67 · ¿Dónde se guardan los tamaños recientes?
- tipo: detalle dentro del alcance
- reglas: RN-tamanos-recientes

## QT-68 · ¿Qué forma tiene la obra en el modelo de dominio y dónde se cumple cada regla?
- tipo: detalle dentro del alcance (el stack ya eligió ddd con la obra como agregado)

## QT-69 · ¿Qué es 100 % de zoom, alrededor de qué punto gira y se acerca la vista, y cuándo se aplica el imán?
- tipo: detalle dentro del alcance
- reglas: RN-limites-zoom, RN-zoom-centro-de-dedos, RN-rotacion-con-imanes, RN-ajustar-vista

## QT-70 · ¿Cómo se reparte la entrada entre pintar y mover la vista, y cómo se llevan los puntos de la pantalla al lienzo?
- tipo: detalle dentro del alcance
- reglas: RN-gestos-de-vista; RN-trazo-interrumpido-por-gesto y RN-trazo-terminado-antes-del-gesto quedan en F-009

## QT-71 · ¿El fondo entra en la composición ya calculada o se dibuja debajo?
- tipo: detalle dentro del alcance
- reglas: RN-fondo-inicial, RN-fondo-cambiable

## QT-72 · ¿Dónde vive la obra recién creada antes de que exista la galería (F-007), y qué pasa si Android recrea la pantalla al girar el equipo?
- tipo: detalle dentro del alcance
- reglas: RN-obra-nueva-lista

## QT-73 · ¿Cómo se ve el lienzo muy alejado (5 %) sin dibujar cientos de mosaicos a tamaño completo ni verse serruchado?
- tipo: detalle dentro del alcance
- reglas: RN-limites-zoom, requisito no funcional de F-001

## QT-74 · ¿Cómo se resuelven los detalles dentro del alcance (QT-62 a QT-73)?
- tipo: modo de resolución
- respuesta:
  - usuario · 2026-10-27: propuestas. El agente decide, marcado como suyo, y el usuario objeta.

## 2026-10-27 · Respuestas

**QT-58**
- propuesta: (1) una composición guardada por mosaico, que la vista solo muestra transformada; (2) dos composiciones, debajo y encima de la capa activa; (3) sin composición guardada, mezclando en la GPU cada cuadro.
- usuario · 2026-10-27: (1) una composición guardada. Cuesta una capa llena más de memoria; al pintar se recombina el mosaico tocado con todas las capas. El diseño de F-002 (unidad 33) lo mide y puede pasar a (2).

**QT-59**
- propuesta: esperar a F-004, F-007 y F-008, o construir ahora un guardado mínimo y un historial solo para el fondo.
- usuario · 2026-10-27: esperan. Los 7 criterios quedan `blocked` en el QA de los tramos 3 y 4 hasta que lleguen esas features; F-001 no se cierra antes.

**QT-60**
- investigado · 2026-10-27, `platforms/android-37.0/data/api-versions.xml` del SDK de Android instalado: `WindowManager.getMaximumWindowMetrics()` existe desde API 30; `Display.getRealSize(Point)` desde API 17, obsoleta desde API 31. Con mínimo API 29 (QT-15) se usa `getMaximumWindowMetrics().bounds` desde API 30 y `getRealSize` en API 29. Que las dos den los píxeles de la pantalla entera según la rotación del momento sale de su nombre, no de la documentación (la página de referencia no cargó): lo verifica el criterio de orientación de US-001.
- agente, dentro del alcance: en Windows, la pantalla es el monitor donde está la ventana, con su tamaño en píxeles físicos (`GraphicsDevice.getDisplayMode()` del monitor de la ventana). La Javadoc de Java 17 (https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/GraphicsDevice.html, leída 2026-10-27) no dice si da píxeles físicos o escalados con una escala de pantalla distinta de 100 %: sin verificar, se prueba en la unidad 25; si da escalados, se multiplica por la escala de la ventana.

**QT-61**
- investigado · 2026-10-27, https://learn.microsoft.com/en-us/windows/win32/inputmsg/wm-pointerdown: WM_POINTERDOWN llega a la ventana por cada contacto, también táctil (PT_TOUCH). Si la app no lo procesa y lo pasa a DefWindowProc, Windows puede convertirlo en WM_GESTURE o en mouse; consumir unos punteros y pasar otros "da un comportamiento indefinido".
- agente, dentro del alcance: el procedimiento de ventana de `desktopApp/pens`, que ya distingue PT_TOUCH, consume todos los contactos táctiles y los entrega como dedos al reconocedor de gestos común. Sin verificar con una pantalla táctil real: si la PC de QA no es táctil, los criterios de gestos quedan `blocked` en Windows (nota de US-003).

**QT-62**
- agente, dentro del alcance: presupuesto de la obra = 25 % de la memoria total del equipo en Android (`ActivityManager.MemoryInfo.totalMem`); en Windows, el menor entre el 25 % de la memoria física total y el 80 % del máximo de memoria directa de la JVM, porque ahí viven los mosaicos. Un lienzo cuesta 16 bytes por píxel: una capa llena (4), la composición con sus niveles reducidos (≈5,3, QT-73), las imágenes que se muestran (≈5,3) y margen. Línea roja = presupuesto ÷ 16, en píxeles; umbral de aviso = la mitad de la línea roja. Se calcula una vez al abrir la app, así el mismo pedido da lo mismo durante la sesión. Las fracciones (25 %, 80 %, la mitad) son provisorias: se calibran con las mediciones de la unidad 20.
- estimación, sin medir: un Android de 4 GB da línea roja de ≈67 millones de píxeles y aviso de ≈33 millones; un A4 a 300 dpi tiene 8,7 millones.
- sin verificar: que el máximo de memoria directa de la JVM sea, por defecto, el máximo de memoria de Java. Se lee en la unidad 25 y se anota acá.

**QT-63**
- agente, dentro del alcance: QA fija los dos valores como las mediciones: en Android con extras al lanzar (`--el warnPixels 40000000 --el redLinePixels 75000000`), en Windows con `-PwarnPixels=… -PredLinePixels=…` de Gradle. Sin esos valores se calculan como dice QT-62. La app no muestra ningún ajuste para esto.

**QT-64**
- agente, dentro del alcance: 1) se juntan todos los motivos de rechazo: lado menor que 16 px, resolución no entera o fuera de 72–600, y lienzo por encima de la línea roja (este último solo si el tamaño en píxeles se pudo calcular); 2) si hay alguno, se informan todos juntos y no se crea nada; 3) si no hay ninguno y el lienzo supera el umbral de aviso, se pide confirmar; 4) se crea. "Supera" es estrictamente mayor: un lienzo igual al umbral no avisa ni se rechaza (nota de US-002).

**QT-65**
- agente, dentro del alcance: ancho = parte entera de √(línea roja × ancho pedido ÷ alto pedido); alto = parte entera de ancho × alto pedido ÷ ancho pedido; si el producto todavía supera la línea roja, se resta 1 al ancho y se recalcula. Con enteros de 64 bits. 12000×16000 con línea roja de 75 millones da 7500×10000.

**QT-66**
- agente, dentro del alcance: milímetros → medida × dpi ÷ 25,4; pulgadas → medida × dpi; se redondea al entero más cercano, el medio hacia arriba. El mínimo de 16 px se valida sobre el resultado (5×5 mm a 72 dpi = 14×14 px).

**QT-67**
- agente, dentro del alcance: en un archivo de preferencias de la app en el equipo (la carpeta de datos de la app en Android y en Windows), detrás de una interfaz de repositorio común. El tamaño se registra después de crear la obra. No va en el archivo de proyecto ni viaja.

**QT-68**
- agente, dentro del alcance: la obra (`Artwork`) es el agregado, con su lienzo (`CanvasSize`, con `Resolution`), su fondo (`CanvasBackground`), sus capas y su vista (`Viewport`). Son objetos de valor `CanvasSize`, `Resolution`, `CanvasBackground`, `Viewport`, `CanvasPreset` y `DeviceCanvasLimit`. El pedido de obra nueva se valida y da "creada", "rechazada con sus motivos" o "pide confirmar". La vista vive en la obra porque viaja en el archivo, pero cambiarla no pasa por el historial.

**QT-69**
- agente, dentro del alcance: 100 % es un píxel del lienzo por píxel físico de la pantalla. Un gesto de dos dedos aplica a la vez desplazamiento, zoom y giro alrededor del centro de los dedos, así el punto del dibujo bajo ese centro queda quieto; el zoom se recorta a 5 %–3200 %. El giro es libre durante el gesto; al terminar, si queda a 5° o menos de 0°, 90°, 180° o 270°, se pega a ese ángulo. Ajustar: 0°, centro del lienzo en el centro del área de dibujo, zoom = el menor entre ancho del área ÷ ancho del lienzo y alto del área ÷ alto del lienzo, recortado a 5 %–3200 %. El margen alrededor lo decide UX.

**QT-70**
- agente, dentro del alcance: en Android, el lápiz y la goma pintan; dos dedos mueven la vista; un dedo solo pinta como hasta ahora, y si llega un segundo dedo su trazo se cancela en el front buffer antes de llegar a los mosaicos, así el gesto no deja marca. La regla de los 0,3 s (RN-trazo-interrumpido-por-gesto) llega con F-009. En Windows pintan el lápiz y el mouse, y el táctil solo mueve la vista hasta que F-009 traiga RN-dedo-pinta-sin-lapiz. Cada punto se lleva de la pantalla al lienzo con la inversa de la vista, en código común; el trazo húmedo del front buffer se dibuja con la misma transformación.

**QT-71**
- agente, dentro del alcance: el fondo no entra en la composición. La composición mezcla las capas sobre transparente y la pantalla dibuja el fondo debajo; cambiar el fondo no recombina nada. Cómo se ve un fondo transparente (por ejemplo, un damero) lo decide UX. La imagen exportada (F-008) mezcla en el mismo orden que la pantalla.

**QT-72**
- agente, dentro del alcance: hasta F-007 la obra vive solo en memoria, en un contenedor de la app que no depende de la pantalla, así sobrevive a que Android recree la Activity al girar. Si el sistema cierra la app, la obra se pierde: lo cubre el autoguardado de F-007 (RN-recuperar-tras-cierre). El tramo 3 no guarda en disco.

**QT-73**
- agente, dentro del alcance: la composición guarda niveles reducidos a la mitad, al cuarto y así hasta que la obra entra en un mosaico (≈1/3 más de memoria). La pantalla usa el nivel más cercano al zoom, así al 5 % se dibujan unos pocos mosaicos y no cientos, y el reducido se promedia en el motor en lugar de saltear píxeles. Un mosaico pintado actualiza sus niveles reducidos en la misma pasada.

## 2026-10-28 · Build (unidades 25 y 26)

**QT-62**
- observado · 2026-10-28, esta PC (31,9 GB, Windows 11, JDK 17, sin `-Xmx`): la JVM no tiene `MaxDirectMemorySize` fijado (vale 0) y el código toma el máximo de memoria de Java, 8,57 GB. Línea roja calculada: 428 448 153 px; aviso: 214 224 076 px. Que la JVM limite la memoria directa a ese mismo máximo cuando la opción vale 0 sigue sin una fuente que lo documente.

**QT-60**
- observado · 2026-10-28, emulador (x86_64, API 37): el prearmado de pantalla aparece y la pantalla de obra nueva lo ofrece. Sin verificar todavía con el equipo girado ni, en Windows, con una escala de pantalla distinta de 100 %.

## 2026-10-28 · Desde F-002

**QT-58**
- superada en parte por QT-89 (diseño de F-002): se suman dos composiciones, debajo y encima de la capa activa; la composición que se muestra sigue.
