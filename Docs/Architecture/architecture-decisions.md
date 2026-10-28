# Decisiones de arquitectura — nk-painter

Registro append-only. Cada entrada dice de dónde salió: `usuario`, `agente, dentro del alcance`, `investigado` (con fuente y fecha), `propuesta` o `[TO-DEFINE]`.

## QT-01 · ¿Se aprueba el plan del tramo 1 y con qué autonomía?
- origen: usuario · 2026-10-27
- respuesta: Aprobado, frenando en cada paso.

## QT-02 · ¿Se agrupan las 28 features de la entrega 1 en 9 specs (F-001 a F-009)?
- origen: usuario · 2026-10-27
- respuesta: Sí, esas 9.

## QT-03 · ¿Las historias se escriben antes o después del stack?
- origen: usuario · 2026-10-27
- respuesta: Specs ahora; historias después del paso 5, al inicio del tramo de cada feature.

## QT-04 · ¿Existe un framework Kotlin con una sola implementación de UI para Android y Windows, y en qué estado está?
- tipo: hecho verificable
- bloquea: la elección de framework (Q-02)
- respuesta:
  - investigado: Compose Multiplatform es estable en Android y en Desktop (JVM); Web (Wasm) está en beta. Fuente: https://kotlinlang.org/docs/multiplatform/supported-platforms.html · leído 2026-10-28.
  - investigado: la última versión estable es Compose Multiplatform 1.12.2 (15 de octubre). Fuente: https://github.com/JetBrains/compose-multiplatform/releases · leído 2026-10-28. La versión de Kotlin que pide se confirma al crear el proyecto (sin verificar).
  - investigado: esa página no lista ningún destino de UI nativo de Windows (mingwX64). En Windows, la UI de Compose corre sobre la JVM.

## QT-05 · En Windows, ¿la app corre sobre la JVM o nativa, y cuánta memoria base agrega eso?
- tipo: hecho verificable + medición
- bloquea: el objetivo de memoria liviana en una PC de 8 GB (Q-05, Q-09)
- respuesta:
  - investigado: la distribución nativa de Compose arma instaladores autocontenidos con el runtime de Java recortado con jlink, y minificado con ProGuard en release. Fuente: https://kotlinlang.org/docs/multiplatform/compose-native-distribution.html · leído 2026-10-28.
  - sin verificar: la memoria base de una app Compose Desktop en Windows. Se mide en el esqueleto (observado) contra la PC de 8 GB.

## QT-06 · ¿Cómo se lee la presión y el tipo de puntero (lápiz, dedo, mouse, borrador) de una tableta o lápiz en Windows desde ese framework?
- tipo: hecho verificable; si no hay soporte, decisión de alternativa
- bloquea: RN-presion-segun-pincel (F-002), RN-rechazo-de-palma-automatico (F-009)
- respuesta:
  - investigado (rango 4): un issue del repo oficial muestra a un desarrollador que tuvo que capturar eventos nativos de Windows para leer presión e inclinación del lápiz, porque Compose Desktop no los entrega. Fuente: https://github.com/JetBrains/compose-multiplatform/issues/4934 · leído 2026-10-28.
  - inferido: Compose Desktop toma la entrada de AWT, cuyo MouseEvent no tiene presión. Para tener presión y distinguir lápiz, dedo y borrador en Windows hace falta un módulo nativo que lea la API de lápiz de Windows (Pointer API WM_POINTER o Real-Time Stylus). Blender, por ejemplo, migró a WM_POINTER para el lápiz.
  - sin verificar: el mismo issue señala que la ventana de Compose en Windows usa un HWND distinto del de los eventos. Es un riesgo para el módulo nativo y se prueba en el esqueleto.
  - propuesta: (a) Compose + módulo nativo de lápiz; (b) Compose sin presión en Windows; (c) Android en Kotlin y Windows en otra tecnología.
  - usuario · 2026-10-28: (c) Android en Kotlin, Windows en otra tecnología.
  - reemplazada por QT-22: (a) Compose + módulo nativo de lápiz.
  - contradicción (resuelta en QT-22): (c) choca con QT-11 (motor Kotlin común), con Q-02 (Kotlin con una sola implementación) y con RN-ida-y-vuelta-sin-perdidas (F-008, idéntica píxel por píxel), porque dos motores distintos no dan los mismos píxeles. Ver QT-22.

## QT-07 · En Android, ¿qué ofrece la plataforma para trazo de baja demora y para distinguir lápiz, dedo y palma?
- tipo: hecho verificable
- bloquea: la meta de 50 ms (Q-26) y el rechazo de palma (F-009)
- respuesta:
  - investigado: la biblioteca Jetpack de gráficos de baja demora (androidx.graphics:graphics-core, GLFrontBufferedRenderer) dibuja el trazo en el front buffer desde Android 10 (API 29). Sirve para trazos, no para paneo ni zoom. Fuente: https://developer.android.com/develop/ui/views/touch-and-input/stylus-input/advanced-stylus-features · leído 2026-10-28.
  - investigado: la predicción de movimiento (androidx.input:input-motionprediction) funciona desde API 19. Lo predicho se reemplaza por los datos reales. Misma fuente.
  - investigado: getToolType distingue TOOL_TYPE_STYLUS, TOOL_TYPE_ERASER y dedo. ACTION_CANCEL (API 1) y FLAG_CANCELED (API 33) marcan toques de palma a descartar. La presión va de 0 a 1 y hay que normalizarla si viene mayor. Misma fuente.

## QT-08 · ¿La meta de 50 ms se mide de punta a punta o como demora agregada por la app?
- tipo: hecho verificable (demora típica de hardware) que puede volver a discovery
- bloquea: el requisito no funcional de F-002
- respuesta:
  - agente, dentro del alcance · 2026-10-28: se mide de punta a punta (del contacto a la pintura visible) en el equipo Android de gama media de referencia, con video de alta velocidad. Además se registra la demora agregada por la app (del timestamp del evento al cuadro presentado). Si el piso del hardware de ese equipo ya supera 50 ms, se vuelve a discovery con una propuesta de cambio de la meta de Q-26. Nunca se reinterpreta en silencio.

## QT-09 · ¿Cómo se representan las capas en memoria para que la obra de referencia (≈1 GB sin comprimir) entre en un Android de 4 GB?
- tipo: decisión técnica (detalle dentro del alcance) apoyada en hechos de memoria de cada plataforma
- bloquea: RN-linea-roja-lienzo, RN-linea-roja-capas, la meta de 60 cps
- respuesta:
  - agente, dentro del alcance · 2026-10-28: cada capa se divide en mosaicos de 256×256 px RGBA de 8 bits por canal, premultiplicados. Solo existen los mosaicos con pintura. Los que no están en pantalla ni se usaron recién se comprimen en memoria, y si la obra se acerca a la línea roja van a una caché en disco. El historial guarda solo los mosaicos que cambió cada paso.
  - estimación, sin medir: una ilustración típica pinta entre el 30 % y el 50 % de cada capa, así que la obra de referencia ocuparía de 300 a 500 MB sin comprimir en lugar de ≈1 GB. Se mide en el esqueleto.

## QT-10 · ¿Cómo se conoce la memoria disponible del equipo para calcular el umbral de aviso y la línea roja?
- tipo: hecho verificable + detalle
- bloquea: RN-aviso-lienzo-grande, RN-linea-roja-lienzo (F-001), RN-capas-disponibles-visible (F-003)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: en Android, la memoria total y disponible del sistema (ActivityManager.MemoryInfo), sabiendo que desde API 26 los píxeles de bitmap viven en la memoria nativa (investigado: https://developer.android.com/topic/performance/graphics/manage-memory · leído 2026-10-28). En Windows, la memoria física total y libre del sistema y el máximo de la JVM. El presupuesto de la obra es una fracción de la memoria del equipo; el umbral de aviso es un porcentaje de ese presupuesto y la línea roja es el presupuesto. Las fracciones se calibran en el esqueleto con la obra de referencia en los dos equipos de gama media.

## QT-11 · ¿Con qué motor se pinta el lienzo: Skia en CPU, Skia en GPU u OpenGL/Vulkan propio?
- tipo: decisión con costo (tradeoff)
- bloquea: metas de 50 ms y 60 cps, modos de pincel
- respuesta:
  - usuario · 2026-10-28: motor Kotlin común sobre Skia, con capas en mosaicos; en Android, el trazo en curso va por el renderizador de baja demora. El rendimiento se mide en el esqueleto.
  - en suspenso hasta QT-22 (resuelto: vale el motor común).

## QT-12 · ¿Cómo se copia y pega una imagen con otras apps en Android y en Windows?
- tipo: hecho verificable
- bloquea: RN-pegar-de-otras-apps (F-006)
- respuesta:
  - sin verificar: el portapapeles de imágenes en Android (ClipboardManager con URI de contenido) y en la JVM (AWT DataFlavor.imageFlavor). Se prueba en el tramo de F-006.

## QT-13 · ¿Cómo se asocia la extensión del archivo de proyecto para "abrir con" en Android y doble clic en Windows?
- tipo: hecho verificable
- bloquea: RN-formas-de-importar (F-008)
- respuesta:
  - investigado (rango 4): el plugin de Compose tiene un DSL fileAssociation(mimeType, extension, description, icono) dentro de nativeDistributions, con variante para Windows. Fuente: tutorial y commits del repo compose-jb (espejo https://code.fanruan.com/github/compose-jb/) · leído 2026-10-28. Se confirma contra la versión del plugin al crear el proyecto.
  - sin verificar: en Android, abrir un archivo de extensión propia que llega desde WhatsApp. Suele llegar como application/octet-stream y un filtro por extensión no siempre aplica. Se prueba en el tramo de F-008. Si no funciona, queda importar desde la app (que también cumple RN-formas-de-importar).

## QT-14 · ¿Compartir desde Android y guardar donde el usuario elija requieren permiso de internet o de almacenamiento?
- tipo: hecho verificable
- bloquea: el objetivo "todo funciona sin conexión" (Q-03, Q-12)
- respuesta:
  - sin verificar: compartir con un Intent ACTION_SEND y FileProvider, y guardar con el selector del sistema (Storage Access Framework), no piden permiso de internet ni de almacenamiento. Se verifica en el esqueleto revisando el manifiesto final.

## QT-15 · ¿Desde qué versión de Android se da soporte?
- tipo: decisión con costo (tradeoff): equipos cubiertos vs. funciones de baja demora disponibles
- bloquea: el alcance de equipos de la entrega 1
- respuesta:
  - usuario · 2026-10-28: Android 10 (API 29) como mínimo.

## QT-16 · ¿Cómo se empaqueta e instala en Windows?
- tipo: hecho verificable + detalle (Q-11)
- bloquea: la distribución por instalador
- respuesta:
  - investigado: el plugin genera instaladores .msi y .exe en Windows, con opciones de carpeta, instalación por usuario y menú de inicio. El empaquetado tiene que correr en Windows: no hay compilación cruzada. Fuente: https://kotlinlang.org/docs/multiplatform/compose-native-distribution.html · leído 2026-10-28.

## QT-17 · ¿Qué áreas declara el stack, que después marcan la cobertura de cada criterio?
- tipo: decisión con costo (tradeoff)
- bloquea: la línea de cobertura de todas las historias
- respuesta:
  - usuario · 2026-10-28: dos áreas, `android` y `windows`. La línea de cobertura de cada criterio es `android — · windows — · qa —`.

## QT-18 · ¿Cómo se organiza el código (arquitectura) y qué modelo de dominio usa?
- tipo: decisión con costo (eje del stack)
- bloquea: el documento de stack
- respuesta:
  - investigado: la tabla de arquitectura de frontend de NZT ofrece un solo valor, `vertical-slice`; se adopta.
  - usuario · 2026-10-28: modelo de dominio `ddd`. La obra es un agregado que hace cumplir sus reglas. DDD: agregados sí · objetos de valor sí · eventos de dominio no · ids tipados no.

## QT-19 · ¿Qué niveles de test, test-first, datos de prueba y herramienta end-to-end se adoptan?
- tipo: opt-ins del stack (se preguntan siempre)
- bloquea: el documento de stack
- respuesta:
  - usuario · 2026-10-28: niveles de test: unitarios sí · integración contra el disco real sí · contenedores efímeros no · API en proceso no (no aplica).
  - usuario · 2026-10-28: test-first sí, para reglas de negocio.
  - usuario · 2026-10-28: datos de prueba generados por código, más un juego de archivos de proyecto de muestra (incluida la obra de referencia) para QA y mediciones.
  - usuario · 2026-10-28: tests end-to-end automatizados no, por ahora.

## QT-20 · Las guías de construcción de NZT no tienen tabla para Kotlin ni Compose. ¿Cómo se trabaja ese hueco?
- tipo: hueco de la guía (se declara al usuario)
- bloquea: la fase de build
- respuesta:
  - usuario · 2026-10-28: se siguen las convenciones oficiales de Kotlin y Compose; las decisiones propias quedan como convenciones de una línea en el stack. Build no tiene guía NZT específica para Kotlin, y lo dice cuando aplica.

## QT-21 · ¿Cómo se resuelven los detalles dentro del alcance (QT-08, QT-09, QT-10)?
- tipo: modo de resolución
- respuesta:
  - usuario · 2026-10-28: Propuestas. El agente decide, marcado como suyo, y el usuario objeta.

## QT-22 · ¿Cómo se concilia "Windows en otra tecnología" (QT-06) con el motor común (QT-11), Kotlin en las dos apps (Q-02) y la obra idéntica píxel por píxel entre equipos (F-008)?
- tipo: contradicción, se resuelve con el usuario
- bloquea: QT-06, QT-11, QT-17, QT-18, QT-19 y el documento de stack
- respuesta:
  - propuesta: volver a Compose + módulo nativo; dos apps con motor Kotlin compartido; dos apps separadas cambiando F-008.
  - usuario · 2026-10-28: volver a Compose Multiplatform + módulo nativo de lápiz en Windows. Un solo código Kotlin y un solo motor; cumple Q-02, QT-11 y F-008.

## QT-23 · ¿Cómo llegan los casos de uso al guardado en disco?
- tipo: eje del stack (persistencia)
- respuesta:
  - usuario · 2026-10-28: `repositories`. Una interfaz por agregado con implementación de archivos por plataforma.

## QT-24 · ¿Cómo se escribe el módulo nativo de lápiz en Windows?
- tipo: detalle dentro del alcance (QT-21: propuestas)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: primero JNA desde Kotlin contra la Pointer API de Windows (WM_POINTER: presión, tipo de puntero, borrador), sin toolchain de C++. Si JNA no puede engancharse a la ventana real de Compose (riesgo de QT-06), se pasa a una DLL C++ por JNI. Se decide con la prueba del esqueleto.

## QT-25 · ¿Qué versión de Kotlin se usa?
- tipo: hecho verificable
- respuesta:
  - sin verificar: la que declare compatible Compose Multiplatform 1.12.2. Se fija al crear el proyecto, leyéndola de su documentación de compatibilidad.

## QT-26 · ¿Qué formato de instalador de Windows se usa?
- tipo: detalle dentro del alcance (QT-21: propuestas)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: `.msi`, instalación por usuario y con menú de inicio. Se puede sumar `.exe` si algún equipo no acepta MSI.

## Unidad 6 · Formato del archivo de proyecto · 2026-10-28

Preguntas derivadas de F-008 (RN-contenido-del-archivo, RN-fuera-del-archivo, RN-ida-y-vuelta-sin-perdidas, RN-archivo-de-version-*, RN-archivo-invalido, RN-importar-falla-sin-cambios, RN-importar-sobre-linea-roja, RN-nombre-del-archivo), F-007 (autoguardado), F-004 (RN-historial-no-viaja) y QT-09 (mosaicos premultiplicados). Modo de los detalles: **propuestas** (QT-41).

## QT-27 · ¿La obra guardada en la galería y el archivo de proyecto comparten formato?
- tipo: decisión con costo (tradeoff)
- bloquea: el diseño del formato y el autoguardado de F-007 (guardar solo lo que cambió, RN-version-anterior-intacta)
- respuesta:
  - propuesta: (a) mismo contenido y mismo esquema; en la galería, desempaquetado (una carpeta por obra, un archivo por capa) para guardar solo lo que cambió; al exportar, empaquetado en un solo archivo. (b) La galería guarda directamente el archivo de proyecto, reescrito entero en cada paso. (c) Dos formatos independientes.
  - usuario · 2026-10-28: (a) mismo esquema. Galería desempaquetada, archivo de proyecto empaquetado. ADR-002.

## QT-28 · ¿Qué contenedor usa el archivo de proyecto?
- tipo: decisión con costo (tradeoff), cara de revertir: todo archivo que exista se tiene que poder leer para siempre (RN-archivo-de-version-anterior)
- bloquea: el formato
- respuesta:
  - propuesta: (a) ZIP con un manifiesto JSON y un archivo de píxeles por capa; (b) binario propio; (c) una base SQLite en un archivo.
  - usuario · 2026-10-28: (a) ZIP. ADR-002.

## QT-29 · "Versión más nueva" (RN-archivo-de-version-nueva), ¿se compara la versión de la app o la del formato?
- tipo: la letra de la regla choca con un efecto no buscado; vuelve a discovery como propuesta de cambio
- bloquea: RN-archivo-de-version-nueva y RN-archivo-de-version-anterior
- respuesta:
  - propuesta: comparar la versión del formato. Con la letra actual, un archivo de la app 1.3 que no usa nada nuevo se rechaza en la 1.2 aunque esta lo pueda leer entero.
  - usuario · 2026-10-28: se compara la versión del formato. Cambia la redacción de RN-archivo-de-version-nueva y RN-archivo-de-version-anterior en F-008 (origin QT-29).

## QT-30 · ¿Qué extensión y tipo MIME lleva el archivo de proyecto?
- tipo: decisión visible para el usuario (RN-nombre-del-archivo)
- bloquea: RN-nombre-del-archivo, la asociación de QT-13
- respuesta:
  - investigado (rango 5, sitios de extensiones): `.nkp` ya la usan los presets de Native Instruments Kontakt y otros dos programas. Fuentes: https://fileinfo.com/extension/nkp, https://filext.com/file-extension/NKP · leído 2026-10-28.
  - investigado (rango 5): no se encontró uso registrado de `.nkpaint` ni `.nkpainter`. Existe un editor de imágenes de Windows llamado NKPaint en SourceForge (https://nkpaint.sourceforge.io · leído 2026-10-28), sin extensión propia documentada.
  - usuario · 2026-10-28: `.nkpainter`.
  - agente, dentro del alcance · 2026-10-28: tipo MIME `application/vnd.nkpainter.project`, sin registrar en IANA; solo lo usan la asociación de Windows y el filtro de "abrir con" de Android (QT-13).

## QT-31 · ¿Android y la JVM leen y escriben ZIP con la biblioteca estándar, incluido ZIP64?
- tipo: hecho verificable
- bloquea: QT-28 (a)
- respuesta:
  - sin verificar: java.util.zip existe en las dos plataformas. La página de referencia de Android no se pudo leer en esta sesión (https://developer.android.com/reference/java/util/zip/ZipOutputStream). Se verifica al crear el proyecto, con un test de integración que escribe y lee un archivo del tamaño de la obra de referencia.

## QT-32 · ¿El archivo puede viajar por WhatsApp como documento sin cambios, y con qué tamaño máximo?
- tipo: hecho verificable
- bloquea: RN-destino-de-exportacion (compartir con WhatsApp)
- respuesta:
  - sin verificar (solo rango 5): varios sitios coinciden en 2 GB por documento; la ampliación desde 100 MB arrancó como prueba en Argentina en 2022. Fuentes: https://www.infobae.com/en/2022/03/23/whatsapp-increases-the-size-of-documents-that-can-be-sent-to-2-gb, https://help.chatdaddy.tech/article/file-size-limitation · leído 2026-10-28. No se encontró página oficial de WhatsApp. Se prueba en el tramo de F-008 enviando la obra de referencia.

## QT-33 · ¿El mismo motor Skia en CPU da los mismos píxeles en ARM (Android) y x86 (Windows)?
- tipo: hecho verificable, por medición
- bloquea: RN-ida-y-vuelta-sin-perdidas, en lo que se ve combinado
- respuesta:
  - sin verificar: el archivo lleva los mismos bytes de cada capa, así que las capas son idénticas. Lo que se ve combinado depende del motor; las rutas SIMD de cada procesador podrían diferir en ±1 por canal. Se mide en el esqueleto comparando la composición de la obra de referencia en los dos equipos.

## QT-34 · ¿Cómo se guardan los píxeles de cada capa?
- tipo: detalle dentro del alcance (modo sin elegir)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: un archivo por capa con el índice de sus mosaicos pintados y cada mosaico de 256×256 en RGBA de 8 bits premultiplicado, en ese orden de canales, tal como lo tiene el motor (QT-09), comprimido con deflate por el ZIP. Sin PNG: guardar los bytes del motor sin convertir alfa garantiza que la capa vuelve idéntica (RN-ida-y-vuelta-sin-perdidas) sin analizar redondeos; si el motor usa otro orden de canales, reordenar no pierde nada. Los mosaicos vacíos no se guardan.

## QT-35 · ¿Qué lleva el manifiesto y cómo se representa la vista?
- tipo: detalle dentro del alcance (modo sin elegir)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: `manifest.json` con versión del formato, versión de la app (solo informativa), nombre, ancho y alto en px, dpi, fondo (color sRGB o transparente), las capas de abajo hacia arriba (id, nombre, opacidad, visible, recorte, bloqueo alfa, cantidad de mosaicos, archivo) y la vista. La vista va en coordenadas del lienzo (centro en px, zoom, giro), no de la pantalla, para que tenga sentido en otro equipo. Nada más: ni historial, ni ajustes del equipo (RN-fuera-del-archivo), ni selección ni capa activa, que RN-contenido-del-archivo no pide.

## QT-36 · Al importar, ¿el nombre de la obra sale del manifiesto o del nombre del archivo?
- tipo: detalle dentro del alcance (RN-contenido-del-archivo ya dice que el archivo lleva el nombre)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: del manifiesto. El nombre del archivo se ignora: WhatsApp o el usuario lo pueden cambiar ("Retrato (1).nkpainter"). Un nombre del manifiesto que no cumple RN-nombre-caracteres hace inválido el archivo.

## QT-37 · ¿Cómo se detecta un archivo dañado o ajeno (RN-archivo-invalido)?
- tipo: detalle dentro del alcance (modo sin elegir)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: primero se lee la versión del formato, para que un archivo más nuevo reciba el aviso de actualizar y no el de dañado. Después es inválido si no es ZIP, si su primera entrada `mimetype` no dice `application/vnd.nkpainter.project`, si falta el manifiesto o un campo, si un tamaño o dpi viola F-001, si no tiene capas, si un mosaico cae fuera del lienzo o su cantidad no coincide, o si falla el CRC de una entrada.

## QT-38 · ¿Cómo se garantiza que una importación fallida no deje nada (RN-importar-falla-sin-cambios) y que la línea roja se controle antes de descomprimir (RN-importar-sobre-linea-roja)?
- tipo: detalle dentro del alcance (modo sin elegir)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: se lee solo el manifiesto y con él se controlan versión, validez y línea roja (tamaño, capas y mosaicos) antes de descomprimir píxeles. El conflicto de nombre se pregunta entonces. Los píxeles se desempaquetan en una carpeta temporal de la app y se validan enteros; recién al final la carpeta se mueve a la galería en un solo paso. Al reemplazar, la obra anterior se borra después de que la nueva quedó en su lugar. Las carpetas temporales que sobrevivan a un cierre de golpe se borran al abrir la app.

## QT-39 · ¿Cómo se leen los archivos de versiones anteriores (RN-archivo-de-version-anterior)?
- tipo: detalle dentro del alcance (modo sin elegir)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: la versión del formato es un entero que empieza en 1 y sube solo cuando cambia lo que se escribe. La app conserva un paso de migración por versión (1→2, 2→3…) y los aplica en cadena al importar. El juego de archivos de muestra del stack guarda uno por versión, y un test de integración los importa todos en cada build.

## QT-40 · ¿Cómo se exporta sin bloquear, con progreso y sin dejar un archivo a medio escribir en el destino?
- tipo: detalle dentro del alcance (modo sin elegir)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: se exporta en segundo plano una foto de la obra tomada al pedir exportar, para no mezclar capas de dos pasos si se sigue dibujando o se autoguarda, con progreso por capa. Se escribe primero a un temporal de la app y después se copia al destino elegido o se comparte. Si la copia falla, se borra lo que se haya escrito en el destino.

## QT-41 · ¿Cómo se resuelven los detalles de la unidad 6 (QT-34 a QT-40)?
- tipo: modo de resolución
- respuesta:
  - usuario · 2026-10-28: Propuestas. El agente decide, marcado como suyo, y el usuario objeta.

## QT-42 · ¿Cómo se organiza la obra dentro de la galería y qué se reescribe en cada autoguardado?
- tipo: pertenece al diseño técnico de F-007; queda abierta allí
- respuesta:
  - agente, dentro del alcance · 2026-10-28: la restricción que deja esta unidad es usar el mismo manifiesto y la misma codificación de mosaicos (QT-27). Cuánto se reescribe por paso, dónde vive el historial (F-004) y la miniatura se deciden en ese diseño.

## Plan de la entrega 1 · 2026-10-28

## QT-43 · ¿Se aprueba el plan de la entrega 1 (tramos 2 a 16) y con qué corte?
- tipo: aprobación del plan
- respuesta:
  - propuesta: esqueleto primero; después tramos de hasta 3 historias (14 tramos), o un tramo por feature (9 tramos).
  - usuario · 2026-10-28: aprobado, con tramos de hasta 3 historias.

## QT-44 · ¿Con qué autonomía corre el tramo 2?
- tipo: aprobación del plan
- respuesta:
  - propuesta: acompañado (frena en cada unidad), o en lote las unidades 15 a 19, frenando igual en la 20, que la corre el usuario.
  - usuario · 2026-10-28: lote de las unidades 15 a 19.

## Tramo 2 · Esqueleto · 2026-10-28

## QT-45 · ¿Qué paquetes y versiones usa el esqueleto?
- tipo: paquetes nuevos (los confirma el usuario)
- respuesta:
  - investigado: Kotlin 2.4.21 es la última estable (8 de octubre de 2026). Fuente: https://kotlinlang.org/docs/releases.html · leído 2026-10-28.
  - investigado: el plugin de Kotlin Multiplatform 2.4.20–2.4.21 admite Gradle 7.6.3–9.7.0 y Android Gradle Plugin 8.5.2–9.3.1. Fuente: https://kotlinlang.org/docs/multiplatform/multiplatform-compatibility-guide.html · leído 2026-10-28.
  - investigado: AGP 9.3 pide Gradle 9.5.0 o más y JDK 17. Fuente: https://developer.android.com/build/releases/agp-9-3-0-release-notes · leído 2026-10-28.
  - investigado: con AGP 9 el plugin multiplataforma no convive con `com.android.application` en el mismo módulo: la app de Android va en su propio módulo y el código compartido usa `com.android.kotlin.multiplatform.library`. Fuente: https://kotlinlang.org/docs/multiplatform/multiplatform-project-agp-9-migration.html · leído 2026-10-28.
  - investigado: Compose Multiplatform 1.12.2 pide Kotlin 2.1.0 o más y JDK 17 para empaquetar; corresponde a Jetpack Compose 1.12.1. Fuente: https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html · leído 2026-10-28.
  - investigado: versiones estables en Maven (Google Maven y Maven Central, leído 2026-10-28): activity-compose 1.13.0, graphics-core 1.0.4, JNA 5.19.1, kotlinx-serialization-json 1.12.0, kotlinx-coroutines 1.11.0.
  - usuario · 2026-10-28: todos. Kotlin 2.4.21, Gradle 9.5.1, AGP 9.3.1, Compose Multiplatform 1.12.2, activity-compose 1.13.0, kotlin.test, graphics-core 1.0.4, JNA 5.19.1, kotlinx-serialization-json 1.12.0, kotlinx-coroutines 1.11.0.
  - reemplaza a QT-25 (versión de Kotlin sin verificar): Kotlin 2.4.21.

## QT-46 · ¿Quién mezcla los píxeles del trazo en los mosaicos: Skia o el código Kotlin?
- tipo: detalle dentro del alcance de QT-11 (QT-21: propuestas)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: el código Kotlin común, con aritmética entera. Skia y el bitmap de cada plataforma solo muestran los mosaicos. Así el mismo trazo da los mismos bytes en ARM y en x86, que es lo que pide RN-ida-y-vuelta-sin-perdidas (reduce el riesgo de QT-33). El costo es el rendimiento en CPU con pinceles grandes: se mide en la unidad 20 (QT-09, QT-11).
  - observado · 2026-10-28: en el emulador de Android (x86_64, API 37), un trazo azul (20, 60, 200) pintado en los mosaicos y copiado a un bitmap ARGB_8888 se ve azul. El orden de bytes RGBA premultiplicado del mosaico coincide con el del bitmap, sin conversión.
  - investigado: `CanvasFrontBufferedRenderer` de graphics-core 1.0.4 pide API 29 (anotación `RequiresApi(29)` leída del binario con javap, 2026-10-28).

## QT-47 · El usuario pide usar Material en la UI. ¿Qué variante y versión, y cuándo entra?
- tipo: decisión con costo (paquete nuevo y base del sistema de diseño de la unidad 23)
- respuesta:
  - usuario · 2026-10-28: usar Material.
  - investigado: Maven Central (leído 2026-10-28). `org.jetbrains.compose.material3:material3` no tiene versión estable que acompañe a Compose 1.12: la última estable es 1.9.0 y la que JetBrains publica junto a 1.12.2 es 1.12.0-alpha03 (https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.2). `org.jetbrains.compose.material:material` (Material 2) tiene 1.12.2 estable.
  - propuesta: Material 3 1.12.0-alpha03; Material 3 1.9.0 estable; Material 2 1.12.2. Entrar con la primera pantalla (unidad 24) o ya en el esqueleto.
  - usuario · 2026-10-28: Material 3 1.9.0 estable, y entra con la primera pantalla. Recomendé 1.12.0-alpha03; el usuario prefirió la estable. Que 1.9.0 funcione con Compose 1.12.2 está sin verificar: se prueba al agregarla en la unidad 24, y si falla se vuelve a esta pregunta con el resultado.
  - observado · 2026-10-27, unidad 24: agregada en `shared` (commonMain). Gradle resuelve sus dependencias de Compose 1.9.1 a 1.12.2. En la JVM de Windows, `Material3CompatibilityTest` dibuja fuera de pantalla un botón de Material 3 con tema oscuro y pasa (`./gradlew :shared:desktopTest`). En Android compila y arma el APK de depuración; que corra en el equipo está sin verificar hasta que la unidad 25 muestre la primera pantalla en el emulador.
  - observado · 2026-10-28, unidad 25: en el emulador (x86_64, API 37) la pantalla de obra nueva se dibuja con Material 3 y su tema; botones, campos y chips responden. Queda verificada en Android.

## QT-48 · ¿Se puede leer la presión del lápiz en la ventana de Compose en Windows (riesgo de QT-06 y ADR-001)?
- tipo: hecho verificable, por prueba
- respuesta:
  - observado · 2026-10-28: sí, con lápiz sintético. La app reemplaza el procedimiento de ventana del marco y de su ventana hija con JNA; los mensajes WM_POINTER llegan a la ventana hija donde dibuja Compose (la del issue 4934), y GetPointerPenInfo da la presión. Prueba: `./gradlew :desktopApp:run -PpenProbe` inyecta con InjectSyntheticPointerInput un trazo de presión 200 a 1000 (de 1024); llegaron 41 muestras con presión de 0,195 a 0,977, y el trazo en pantalla se engrosa con la presión. Windows 11 Pro 10.0.26300, JDK 17.
  - observado: Windows manda movimientos del lápiz en el aire, sin contacto; se descartan con el indicador POINTER_FLAG_INCONTACT.
  - sin verificar: un lápiz físico, la goma del lápiz, y que las coordenadas coincidan con el lienzo con escala de pantalla distinta de 100 %. Se prueba en la unidad 20.
  - agente, dentro del alcance · 2026-10-28: queda JNA (QT-24); no hace falta la DLL en C++.

## QT-49 · ¿En qué unidad guarda el manifiesto la opacidad de una capa?
- tipo: detalle dentro del alcance (QT-41: propuestas)
- respuesta:
  - agente, dentro del alcance · 2026-10-28: entero de 0 a 100, el porcentaje que nombra RN-opacidad-capa. Reemplaza en el formato a "la unidad del modelo de dominio" (QT-35), que todavía no existía. Si F-003 necesita más precisión, sube la versión del formato.

## QT-50 · ¿Funciona el formato v1 con ZIP en disco real (QT-31)?
- tipo: hecho verificable, por prueba
- respuesta:
  - observado · 2026-10-28: en la JVM de Windows (JDK 17), `ProjectFileRoundTripTest` escribe un `.nkpainter` de tres capas en disco y lo lee con los mismos bytes por mosaico y las mismas propiedades; rechaza un formato más nuevo, un ZIP ajeno, un archivo que no es ZIP, un byte alterado dentro de una capa (por CRC) y un archivo cortado. Corre con `./gradlew :shared:desktopTest`.
  - sin verificar: el mismo código en Android (compila, pero no hay tests en el equipo: el stack no habilita tests instrumentados) y el tamaño de la obra de referencia. Las dos cosas se miden en las unidades 19 y 20.

## QT-51 · Primeras mediciones con la obra de referencia generada (unidad 19)
- tipo: hechos, por medición (no son los equipos de referencia)
- respuesta:
  - observado · 2026-10-28, esta PC (32 GB, Windows 11, JDK 17), `./gradlew :desktopApp:run -Pbenchmark`: la obra generada pinta 2017 mosaicos, el 48 % (504 MB sin comprimir, dentro de la estimación de QT-09). Proceso al arrancar sin ventana: 70 MB; con la obra en memoria: 982 MB. Exportar: 70 MB en 3,1 s; importar: 0,9 s; los píxeles importados son idénticos (resumen de capas `3d4808dd231c0609`, de la composición `2db3d05f0aa95e06`). El tamaño del archivo no representa obras reales: el relleno generado se comprime mejor que un dibujo.
  - observado · 2026-10-28, misma PC: combinar las 30 capas de una pantalla Full HD (40 mosaicos) toma 193 ms. Para 60 cuadros por segundo hay 16,7 ms por cuadro: navegar la vista no puede recombinar las capas en cada cuadro. Queda para el diseño técnico de F-001 (unidad 22), que tiene que mostrar una composición ya calculada y recombinar solo lo que cambia.
  - observado · 2026-10-28, emulador de Android (x86_64, API 37): la app suma entre el evento del lápiz y la entrega del cuadro del front buffer unos 15 ms de promedio, con p95 de 21 ms y máximo de 46 ms. Es la parte de la app; la pantalla y el digitalizador suman la suya (QT-08).
  - observado · 2026-10-28, mismo emulador: la obra de referencia no entra. Se cortó por falta de memoria con 192 MB de memoria de Java usados (el tope de la app en ese equipo, `dalvik.vm.heapgrowthlimit`), con 3,3 GB libres en el equipo. Ver QT-52.

## QT-52 · ¿Dónde viven los píxeles de los mosaicos en Android, si la memoria de Java de la app tiene tope (192 MB en el emulador)?
- tipo: decisión con costo (tradeoff); bloquea la obra de referencia en Android (RN-linea-roja-lienzo, RN-linea-roja-capas, QT-09)
- bloquea: la unidad 20 en Android
- respuesta:
  - propuesta: (a) mosaicos en memoria nativa (`ByteBuffer.allocateDirect`) en el motor común, que en Android no contaría contra el tope de Java (sin verificar: se comprueba con la misma medición); (b) `android:largeHeap="true"` (576 MB en el emulador), que depende de cada equipo y deja poco margen para el historial; (c) comprimir en memoria los mosaicos que no se ven (ya previsto en QT-09), junto con (a) o (b).
  - usuario · 2026-10-28: (a) memoria nativa. Se implementa en la unidad 103 y se verifica con la misma medición en el emulador.
  - investigado · 2026-10-27, código de AOSP (libcore, rama main: `ojluni/src/main/java/java/nio/ByteBuffer.java` y `DirectByteBuffer.java`): en Android, `allocateDirect` crea `new DirectByteBuffer.MemoryRef(capacity)`, que reserva `VMRuntime.newNonMovableArray(byte.class, capacity + 7)`: un `byte[]` fijo dentro de la memoria de Java. La premisa de (a) es falsa: cuenta contra el mismo tope. La unidad 103 no empezó; se reabre la pregunta.
  - propuesta: (a1) reservar con `malloc` en una biblioteca nativa mínima (NDK) y envolverla con `NewDirectByteBuffer` de JNI; el motor común sigue viendo un `ByteBuffer`; suma NDK y CMake al build de Android (cambio de stack) y la liberación de cada mosaico pasa a ser explícita. (a2) `android.os.SharedMemory` mapeada (`mapReadWrite`), en bloques grandes repartidos en mosaicos; sin código nativo; que no cuente contra el tope está sin verificar (se mide). (b) `largeHeap`, como antes. (c) comprimir los mosaicos que no se ven, junto con otra.
  - usuario · 2026-10-27: (a1) NDK y `malloc`. Reemplaza a (a). Se implementa en la unidad 103 y se verifica con la misma medición en el emulador.
  - observado · 2026-10-27, unidad 103, emulador (x86_64, API 37, tope de Java 192 MB): la obra de referencia entra. Tras construirla, memoria de Java 8/192 MB y memoria nativa 553 MB; exportar 70 MB en 2,1 s, importar 2,6 s, píxeles importados idénticos. Resúmenes `3d4808dd231c0609` y `2db3d05f0aa95e06`, los mismos que en Windows (QT-51): las dos plataformas pintan los mismos bytes. Combinar una pantalla Full HD: 2896 ms en el emulador.
  - observado · 2026-10-27, esta PC, con el cambio: combinar una pantalla, 260 ms (193 ms en QT-51, una corrida de cada una: no alcanza para atribuirlo al cambio). Construir 1,3 s, exportar 2,7 s, importar 1,0 s.
  - sin verificar: memoria y tiempos en una tableta real (unidad 20).

## Tramo 3 · F-001 parte 1 · 2026-10-27

## QT-57 · ¿Hasta dónde corre el tramo 3 sin frenar entre unidades?
- tipo: aprobación del plan
- respuesta:
  - propuesta: unidades 22 a 24 (hasta el fin de UX), solo la 22, o 22 a 26 (hasta el fin del build).
  - usuario · 2026-10-27: 22 a 24. Frena antes de la unidad 25.

## QT-75 · ¿Hasta dónde corre el build del tramo 3 sin frenar?
- tipo: aprobación del plan
- respuesta:
  - propuesta: 25 y 26 (fin del build), 25 a 27 (con QA), o solo 25.
  - usuario · 2026-10-27: 25 y 26. Frena antes del QA (27).

## QT-76 · ¿Hasta dónde corre el lote autónomo que arranca en el QA del tramo 3?
- tipo: aprobación del plan
- respuesta:
  - propuesta: 27 a 31 (QA del tramo 3 y todo el tramo 4: F-001 completa), solo 27 (fin del tramo 3), o 27 a 28 (QA y pantallas de US-003/US-004).
  - usuario · 2026-10-28: 27 a 31. Frena al cerrar F-001 (tras la 31).

## QT-77 · ¿Se aprueba el plan de pruebas de US-001 y US-002?
- tipo: aprobación del plan de pruebas (unidad 27)
- respuesta:
  - propuesta: aprobar `Plan/specs/F-001-lienzo-y-vista/testing/` tal como está (30 escenarios, cada uno en Android y Windows).
  - usuario · 2026-10-28: aprobado.

## QT-78 · ¿Cómo se crean los datos de prueba (los recientes)?
- tipo: mecanismo de datos de QA
- respuesta:
  - propuesta: el agente escribe el archivo de recientes de la app (emulador por `run-as`, Windows en `%APPDATA%\nk-painter`), con copia antes y restauración después; o crearlos solo usando la app (más lento); o scripts que corre el usuario.
  - usuario · 2026-10-28: solo usando la app. Sin escribir archivos de la app; E-16 (archivo dañado) queda blocked.

## QT-79 · ¿Puede el agente manejar la app de Windows con mouse y teclado sintéticos?
- tipo: efecto material de QA
- respuesta:
  - propuesta: sí, mientras corre la parte de Windows (el mouse y el teclado de la PC quedan tomados; no hay que usarla); o la corre el usuario y el agente registra la evidencia.
  - usuario · 2026-10-28: sí, la maneja el agente.

## QT-80 · ¿Se arregla BUG-001 antes de seguir con el tramo 4?
- tipo: cambio del plan tras una falla de QA (unidad 27)
- respuesta:
  - propuesta: arreglarlo ahora (build de S3 con una unidad nueva y re-corrida de E-11, E-12, E-14 en Windows), y después seguir el lote hasta la 31; o seguir con el tramo 4 y dejar el bug abierto.
  - usuario · 2026-10-28: arreglarlo ahora; las unidades 108 (arreglo) y 109 (re-corrida) entran al lote, que sigue hasta la 31.

## QT-81 · ¿Se aprueba el plan de pruebas de US-003 y US-004, con dedos inyectados?
- tipo: aprobación del plan de pruebas (unidad 31) y efecto material de QA
- respuesta:
  - propuesta: aprobar `testing/US-003-navegar-vista.md` (13 escenarios) y `testing/US-004-cambiar-fondo.md` (6), cada uno en Android y Windows; los dos dedos se inyectan con `sendevent` en el emulador y con la inyección táctil de Windows desde PowerShell (el mouse y el teclado de la PC quedan tomados mientras corre, como en QT-79). Alternativas: aprobar sin la inyección táctil de Windows (gestos blocked en Windows), o que el usuario corra los gestos y el agente registre la evidencia.
  - usuario · 2026-10-28: aprobado, con la inyección táctil en las dos plataformas.

## QT-82 · ¿Se arreglan BUG-002 y BUG-003 antes de cerrar F-001?
- tipo: alcance del cierre de F-001 (tramo 4)
- respuesta:
  - propuesta: arreglar los dos antes de cerrar (BUG-003 parece un recorte faltante del lienzo en Windows; BUG-002 pide leer el renderizador y limitar los redibujos pendientes, sin reproducción a pedido). Alternativas: arreglar solo BUG-003 y dejar BUG-002 abierto como riesgo conocido; o cerrar F-001 con los dos abiertos y arreglarlos en un tramo posterior.
  - usuario · 2026-10-28: arreglar los dos.

## QT-83 · ¿Se repiten en Windows US-003 E-02, US-004 E-03 y E-06?
- tipo: alcance de QA de F-001
- respuesta:
  - propuesta: repetirlos después de los arreglos, con la ventana de la app al frente verificada antes de cada entrada sintética y deteniendo la corrida si el foco no es la app. Alternativa: cerrar F-001 con esos tres sin correr, registrados como huecos.
  - usuario · 2026-10-28: repetir con control de foco.

## QT-84 · ¿Qué tramo corre sin detenerse ("sigamos autónomo")?
- tipo: autonomía (lote cerrado)
- respuesta:
  - propuesta: lote hasta la aceptación de F-001: arreglo de BUG-003, arreglo de BUG-002, re-corrida de QA de F-001 en las dos plataformas; para en la aceptación. Alternativa: seguir además con el análisis de F-002 (unidad 32), que igual se detiene en sus preguntas.
  - usuario · 2026-10-28: preguntó hasta dónde se puede seguir solo. Respuesta del agente: hasta F-001 listo para aceptar (la aceptación es del usuario y es el tope); se corre ese lote (unidades 110 a 112).

## QT-85 · ¿Se acepta F-001 (US-001 a US-004) y se cierra?
- tipo: aceptación de la feature (tope del lote QT-84)
- respuesta:
  - propuesta: aceptarla y cerrarla en una unidad nueva (close.md: barrer marcadores, poner al día los documentos, entrada de historial), que cierra el tramo 4. Alternativa: pedir cambios antes de cerrar.
  - usuario · 2026-10-28: la acepta. Agente: los 36 criterios con android, windows y qa ✓ pasan a [x]; el cierre no se puede hacer: QT-59 dijo que F-001 no se cierra antes de F-003/F-004/F-007/F-008, y close.md pide todos los criterios en [x]. Quedan 10 abiertos (ver QT-87).

## QT-86 · ¿Qué lote corre sin detenerse tras el cierre de F-001 ("sigamos autonomo")?
- tipo: autonomía (lote cerrado)
- respuesta:
  - propuesta: el tramo 5 entero (unidades 32 a 37: historias, diseño técnico, pantallas, US-005, US-007 y su QA); para igual en preguntas que bloqueen, fallas y pasado el 40 % de contexto. Alternativas: solo hasta el fin del análisis de F-002 (unidad 32); o hasta el fin del diseño (32 a 34), antes de escribir código.
  - usuario · 2026-10-28: el tramo 5 entero; el lote es el cierre de F-001 (unidad 113) y las unidades 32 a 37.

## QT-87 · ¿Cómo sigue F-001, con 10 criterios que no se pueden cerrar todavía?
- tipo: contradicción entre la aceptación (QT-85) y QT-59; detiene el lote QT-86
- abiertos: US-001 CA-14 (F-003); US-003 CA-10 (F-004), CA-11 (F-007), CA-12 (F-008), CA-13 (fluidez, sin construir, unidad 20 y QT-73); US-004 CA-04, CA-05, CA-06 (F-004), CA-07 (F-007); US-002 los dos de seguir/no seguir tras el aviso: qa ✓ pero sin marca de android ni windows
- respuesta:
  - propuesta: (A) respetar QT-59: F-001 queda aceptada en parte y abierta; su cierre (unidad 113) pasa al final, después de F-008, con la re-corrida de esos criterios en el QA de cada feature que los desbloquea; el lote sigue con el tramo 5. (B) cambiar QT-59: mover los criterios bloqueados a las historias de F-003, F-004, F-007 y F-008 y cerrar F-001 ahora (más trabajo de análisis ya, entrada de historial de cambio). (C) parar el lote y decidir después.
  - usuario · 2026-10-28: (A) respetar QT-59. La unidad 113 pasa al final del tramo 16; el lote sigue con 32 a 37.

## QT-88 · ¿Entran al lote las unidades 114 a 116 (historias US-006, US-007, US-008)?
- tipo: autonomía; salieron del recorte de la unidad 32 (una historia por unidad)
- respuesta:
  - propuesta: agregarlas; el lote queda 114, 115, 116, 33 a 37 y para igual en preguntas que bloqueen. Alternativa: no agregarlas y parar acá.
  - usuario · 2026-10-28: agregarlas; el lote es 114, 115, 116, 33 a 37.

## QT-108 · ¿Se aprueba el plan de pruebas de US-005 y US-007?
- tipo: aprobación del plan de pruebas (unidad 37), con dos efectos materiales de QA
- plan: `Plan/specs/F-002-pinceles-y-trazo/testing/` (US-005: 13 escenarios, 3 blocked de antemano; US-007: 12 escenarios)
- respuesta:
  - propuesta: aprobarlo como está. Incluye (1) borrar los datos de la app en el emulador con `pm clear` y apartar `drawing-settings.txt` en Windows para probar "la primera vez" (US-005 E-02), y devolverlo al terminar; (2) un lápiz sintético de Windows con presión exacta, que toma el puntero de la PC mientras corre; (3) pinceles de fábrica en lugar de los grosores del criterio hasta US-006; (4) la presión en Android la corre el usuario en su tableta (QT-90), en una corrida aparte. Alternativas: aprobarlo sin E-02 (CA-02 queda blocked); o cambiar escenarios.
  - usuario · 2026-10-28: aprobado tal cual.

## QT-109 · ¿Cuándo se corre la QA de US-005 y US-007, si el usuario está usando la PC?
- tipo: efecto material de QA (QT-79: Windows toma mouse y teclado; el emulador carga la máquina)
- respuesta:
  - propuesta: cuando el usuario termine de usar la PC; al "continuar", Android primero y Windows después. Alternativas: ahora solo Android; ahora todo.
  - usuario · 2026-10-28: cuando termine; se retoma con "continuar".

## QT-110 · ¿Qué se adelanta mientras la QA del tramo 5 espera (QT-109)?
- tipo: autonomía; cambia el orden del plan (el tramo 6 empieza antes de cerrar la QA del tramo 5)
- respuesta:
  - propuesta: la unidad 38 (pantallas de US-006 y US-008) y parar. Alternativas: además 39 y 40 (implementar, carga la CPU); o esperar.
  - usuario · 2026-10-28: unidad 38 y además 39 y 40. El tramo 5 queda en pausa con la 37 sin correr; se vuelve a ella al "continuar" cuando el usuario deja la PC.

## QT-111 · ¿Qué lote corre sin parar ahora que el usuario deja la PC ("sigamos autónomo")?
- tipo: autonomía (lote cerrado); la QA toma mouse y teclado en Windows (QT-79) y carga la máquina con el emulador
- respuesta:
  - propuesta: unidades 37 y 41 (QA de los tramos 5 y 6, Android primero y Windows después) y parar en la aceptación de F-002. Alternativas: solo la 37; o 37, 41 y además la 42 (historias de F-003, que tienen preguntas al usuario y pararían igual).
  - usuario · 2026-10-28: 37 y 41; se para en la aceptación de F-002.

## QT-112 · ¿Cómo sigue la unidad 37 después del corte de Windows y de BUG-001?
- tipo: efecto material de QA (QT-79) y un fallo que frena el lote (QT-111)
- respuesta:
  - propuesta: (1) arreglar BUG-001 en una unidad de build antes de seguir con la QA; (2) terminar la QA de Windows cuando la PC quede libre de verdad, re-corriendo US-005 E-13 en las dos áreas tras el arreglo. Alternativas: terminar primero Windows tal como está y arreglar después; o arreglar y re-correr solo E-13.
  - usuario · 2026-10-28: arreglar primero. Se agrega la unidad 117 (arreglar BUG-001) al lote, antes de terminar la 37; luego la 41; se para en la aceptación de F-002.

## QT-113 · ¿Cómo sigue el tramo 5 después de la QA con fallas (BUG-002, BUG-003)?
- tipo: cambio de plan (unidades de arreglo que el plan no tiene) y autonomía
- respuesta:
  - propuesta: agregar la unidad 118 (arreglar BUG-003, lápiz sin presión en Windows) y la 119 (arreglar BUG-002, costuras entre mosaicos), re-correr US-005 E-13 y US-007 E-12 en Windows, y recién después la 41 (QA de US-006 y US-008). Alternativas: seguir con la 41 ahora y arreglar al final del tramo 6; o arreglar solo BUG-003 y pasar BUG-002 a F-001 como deuda.
  - usuario · 2026-10-28: arreglar los dos. Lote: 118 (BUG-003), 119 (BUG-002), 120 (re-correr en Windows US-005 E-13 y US-007 E-02 y E-12) y 41; se para en la aceptación de F-002, como QT-111.

## QT-114 · ¿Qué se hace con BUG-003 si el lápiz sintético no puede simular un lápiz sin presión?
- tipo: un fallo que resultó ser de la herramienta de prueba; cambia la unidad 118 y el escenario US-007 E-12 en Windows
- respuesta:
  - propuesta: BUG-003 se cierra como no defecto (la app hace lo correcto con lo que Windows entrega); US-007 E-12 en Windows pasa a blocked con esa causa, como ya está en Android; la 118 queda dropped y el lote sigue con la 119. Alternativas: que el usuario lo pruebe con un lápiz físico sin presión, si tiene uno; o tratar presión 0 en contacto como "sin presión" (cambia cómo se ve el apoyo más leve de un lápiz real).
  - usuario · 2026-10-28: "tengo una tableta gráfica y un lápiz para probarlo". BUG-003 queda pending hasta su corrida; la 118 cierra sin cambio de código (no se encontró defecto) y la prueba del usuario va en la 120. Ojo: E-12 necesita un lápiz que no informe presión; una tableta con presión sirve para ver la presión real, no para E-12.

