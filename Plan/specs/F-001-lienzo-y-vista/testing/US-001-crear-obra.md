# Pruebas · US-001 Crear una obra
feature: F-001 · unidad 27 · plan aprobado 2026-10-28 (QT-77)

Cada escenario se corre en las dos áreas que declara el stack, **la pantalla de Android** (emulador
`Pixel_10_Pro_XL`, manejado con `adb`: toques, texto, capturas) y **la pantalla de Windows**
(`:desktopApp:run`, manejada con capturas y mouse/teclado sintéticos desde PowerShell). No hay
API: todo escenario es de pantalla y entra por la app real.

## Cómo se observa el resultado
- **Tamaño y resolución creados.** El editor no muestra el tamaño. Se observa en el registro
  que la app guarda al crear: después de crear, se cierra la app, se vuelve a abrir y el
  primer reciente de la pantalla Obra nueva dice `ancho×alto px · dpi`. El archivo de
  recientes se lee también directo (Android: `run-as com.nkpainter cat files/recent-canvas-sizes.txt`;
  Windows: `%APPDATA%\nk-painter\recent-canvas-sizes.txt`). Además, en el editor se mide la
  proporción del lienzo en la captura.
- **Fondo.** Se lee el color de los píxeles del lienzo en la captura del editor, lejos de los
  bordes; transparente es el damero.
- **Vista ajustada.** En la captura: el lienzo entero dentro del área, centrado (márgenes
  iguales ±2 px en el eje que sobra), sin giro (bordes horizontales).

## Datos
Mecanismo (QT-78): **solo usando la app**. Ningún archivo de la app se escribe ni se borra a
mano. Los recientes se arman creando obras, y los escenarios miran los primeros puestos de la
lista, así que no necesitan empezar vacíos. Los tamaños creados quedan en los recientes de cada
equipo: se registran como datos dejados atrás en el índice. Ninguna obra real se toca: las
obras viven solo en memoria hasta F-007.

## Escenarios

### E-01 · A4 crea 2480×3508 px a 300 dpi
- **Cubre:** CA-01 (RN-tamanos-prearmados) · prioridad alta
- **Pasos:** abrir la app; elegir A4; Crear; cerrar y volver a abrir.
- **Esperado:** el editor muestra un lienzo de proporción 2480:3508; el primer reciente es
  `2480×3508 px · 300 dpi`.
- **Efectos materiales:** ninguno.

### E-02 · Full HD crea 1920×1080 px a 72 dpi
- **Cubre:** CA-02 · prioridad alta
- **Pasos:** elegir Full HD; Crear; cerrar y volver a abrir.
- **Esperado:** proporción 1920:1080; primer reciente `1920×1080 px · 72 dpi`.

### E-03 · Pantalla sigue la orientación del equipo
- **Cubre:** CA-03 (RN-prearmado-pantalla) · prioridad media
- **Datos:** el criterio da una tableta de 1200×2000; el emulador no es esa
  tableta, así que se usa la misma partición (equipo en horizontal) con las medidas reales del
  equipo, leídas antes con `adb shell wm size` (Android) o el modo del monitor (Windows).
- **Pasos Android:** girar el emulador a horizontal; elegir Pantalla; Crear; volver a abrir.
  Repetir en vertical.
- **Pasos Windows:** con el monitor como está (horizontal), elegir Pantalla; Crear; volver a abrir.
- **Esperado:** el prearmado y el reciente valen ancho×alto de la pantalla en esa orientación
  (en horizontal, el lado mayor primero), a 72 dpi.
- **Efectos materiales:** ninguno. **No se gira el monitor del usuario**: el caso vertical en
  Windows queda sin correr salvo que el usuario lo gire (hueco declarado, prioridad baja).

### E-04 · 3000×2000 px a 150 dpi se respeta
- **Cubre:** CA-04 (RN-tamano-personalizado) · prioridad alta
- **Pasos:** unidad px; Ancho 3000, Alto 2000, Resolución 150; Crear; volver a abrir.
- **Esperado:** primer reciente `3000×2000 px · 150 dpi`; proporción 3:2.

### E-05 · 100×150 mm a 300 dpi da 1181×1772 px
- **Cubre:** CA-05 · prioridad alta (borde de redondeo)
- **Pasos:** unidad mm; 100, 150, 300; leer el tamaño en px que muestra el formulario; Crear; volver a abrir.
- **Esperado:** el formulario muestra `1181×1772 px`; primer reciente `1181×1772 px · 300 dpi`.

### E-06 · 8×10 pulgadas a 72 dpi da 576×720 px
- **Cubre:** CA-06 · prioridad media
- **Pasos:** unidad pulg; 8, 10, 72; Crear; volver a abrir.
- **Esperado:** el formulario muestra `576×720 px`; primer reciente `576×720 px · 72 dpi`.

### E-07 · Recientes del más nuevo al más viejo
- **Cubre:** CA-07 (RN-tamanos-recientes) · prioridad alta
- **Datos:** los tres tamaños se crean por la app, como dice el criterio.
- **Pasos:** crear A4; volver a abrir; crear 3000×2000 px a 150 dpi; volver a abrir; crear Full HD; volver a abrir.
- **Esperado:** Recientes muestra, en orden, `1920×1080 px · 72 dpi`, `3000×2000 px · 150 dpi`,
  `2480×3508 px · 300 dpi`.

### E-08 · Solo los últimos 5
- **Cubre:** CA-08 · prioridad media
- **Datos:** se crean por la app, en este orden, 6 tamaños distintos: A4, 3000×2000 px a
  150 dpi, Full HD, A5, 2K cuadrado y 576×720 px a 72 dpi, volviendo a abrir entre cada uno.
- **Pasos:** después del 6.º, volver a abrir y leer Recientes.
- **Esperado:** se ven 5: `576×720 px · 72 dpi`, `2048×2048 px · 72 dpi`, `1748×2480 px · 300 dpi`,
  `1920×1080 px · 72 dpi`, `3000×2000 px · 150 dpi`, y `2480×3508 px · 300 dpi` (A4, el primero) ya no está.

### E-09 · Repetir un tamaño no lo duplica
- **Cubre:** CA-09 · prioridad media
- **Datos:** se crean por la app A4 y después Full HD.
- **Pasos:** crear A4; volver a abrir.
- **Esperado:** los dos primeros recientes son `2480×3508 px · 300 dpi` y `1920×1080 px · 72 dpi`,
  y A4 no aparece una segunda vez en la lista.

### E-10 · Mismo ancho y alto con otra resolución es otro reciente
- **Cubre:** CA-10 · prioridad media
- **Datos:** se crea A4 por la app.
- **Pasos:** px; 2480, 3508, 150; Crear; volver a abrir.
- **Esperado:** los dos primeros recientes son `2480×3508 px · 150 dpi` y `2480×3508 px · 300 dpi`.

### E-11 · Sin elegir fondo, blanco
- **Cubre:** CA-11 (RN-fondo-inicial) · prioridad alta
- **Pasos:** al entrar, comprobar que Blanco está elegido; A4; Crear.
- **Esperado:** el lienzo en la captura es #FFFFFF.

### E-12 · Fondo #F5E6C8
- **Cubre:** CA-12 · prioridad alta
- **Pasos:** Fondo: Color; escribir `#F5E6C8`; Crear.
- **Esperado:** el lienzo en la captura es #F5E6C8 (±1 por canal por la conversión de color de la captura).

### E-13 · Fondo transparente
- **Cubre:** CA-13 · prioridad alta
- **Pasos:** Fondo: Transparente; Crear.
- **Esperado:** el lienzo se ve en damero (dos tonos alternados), sin un color plano detrás.

### E-14 · La obra nueva se abre lista para dibujar
- **Cubre:** CA-14 (RN-obra-nueva-lista, RN-ajustar-vista) · prioridad alta
- **Pasos:** A4; Crear; captura del editor.
- **Esperado:** el lienzo entero visible, centrado, a 0°. **La parte "una sola capa vacía
  llamada Capa 1, activa" queda `blocked`**: no hay dónde verla hasta el panel de capas de
  F-003 (acordado en `ux-ui/screen-obra-nueva.md`); la cubre por ahora el test del dominio.

### Bordes que ninguna técnica genera
- **Doble toque en Crear.** E-15: tocar Crear dos veces seguidas con A4. Esperado: se abre
  una sola obra y el reciente aparece una sola vez (RN-tamanos-recientes: sin duplicados).
- **Archivo de recientes dañado.** E-16: **blocked**. La app no puede producir un archivo
  dañado, y QT-78 descarta escribirlo a mano. El estado "recientes que no se pudieron leer" lo
  cubre por ahora el test `FileRecentCanvasSizeStoreTest`, que es del build.
- **Ventana angosta.** E-17, solo Windows: achicar la ventana a menos de 600 dp de ancho.
  Esperado: regiones apiladas, Crear fijo abajo y a la vista (screen-obra-nueva, ancho angosto).
- Descartados: recurso de otro usuario (no hay usuarios), dependencia caída (no hay red),
  flujo interrumpido (la obra vive en memoria hasta F-007: cerrar la pierde por diseño).

## Cobertura
| Criterio | Escenarios | Estado |
|---|---|---|
| US-001 CA-01 | E-01 | passed |
| US-001 CA-02 | E-02 | passed |
| US-001 CA-03 | E-03 | passed (Windows en vertical sin correr) |
| US-001 CA-04 | E-04 | passed |
| US-001 CA-05 | E-05 | passed |
| US-001 CA-06 | E-06 | passed |
| US-001 CA-07 | E-07 | passed |
| US-001 CA-08 | E-08 | passed |
| US-001 CA-09 | E-09, E-15 | passed |
| US-001 CA-10 | E-10 | passed |
| US-001 CA-11 | E-11 | passed (Windows en la re-corrida, tras BUG-001) |
| US-001 CA-12 | E-12 | passed (Windows en la re-corrida, tras BUG-001) |
| US-001 CA-13 | E-13 | passed |
| US-001 CA-14 | E-14 | parte visible passed (Windows en la re-corrida); "Capa 1" blocked hasta F-003 |
| estados de pantalla | E-16 (blocked: QT-78), E-17 | E-17 passed; E-16 blocked |

## Ejecuciones

### Corrida 2026-10-28 · agente
Android: emulador Pixel_10_Pro_XL (1344×2992, 480 dpi), APK debug, manejado por `adb` y el
árbol de accesibilidad (`uiautomator`), con los elementos ubicados por su texto. Windows:
`:desktopApp:run` en un monitor de 2560×1440 al 125 %, manejado con clics y teclado sintéticos
en coordenadas leídas de la captura, porque **UI Automation no ve ningún elemento de la
ventana** (observación de accesibilidad, abajo). Datos: solo por la app (QT-78). El tamaño
creado se leyó en los recientes: en Android, en pantalla al relanzar; en Windows, en el archivo
de recientes después de cada creación, y en pantalla al relanzar en E-08. Evidencia:
`evidence/2026-10-28/android/` y `evidence/2026-10-28/windows/`.

| Escenario | Android | Windows | Visto |
|---|---|---|---|
| E-01 | passed | passed | Android: proporción 0,7066 (A4 da 0,7070); primer reciente `2480×3508 px · 300 dpi`. Windows: contorno de 677×952; reciente `2480 3508 300` |
| E-02 | passed | passed | proporción 1,773 / contorno 1482×836; reciente `1920×1080 px · 72 dpi` |
| E-03 | passed | passed (solo horizontal) | Android en horizontal: Pantalla ofrece y crea 2992×1344 a 72 dpi; en vertical, 1344×2992. Windows: 2560×1440 a 72 dpi. Monitor en vertical: **sin correr** (hueco declarado, prioridad baja) |
| E-04 | passed | passed | reciente `3000×2000 px · 150 dpi`; proporción 1,497 |
| E-05 | passed | passed | el formulario muestra `1181×1772 px`; reciente `1181×1772 px · 300 dpi` |
| E-06 | passed | passed | el formulario muestra `576×720 px`; reciente `576×720 px · 72 dpi`; proporción 0,799 |
| E-07 | passed | passed | recientes: Full HD, 3000×2000 a 150 dpi, A4, en ese orden |
| E-08 | passed | passed | 5 recientes: 576×720, 2K, A5, Full HD, 3000×2000; A4 fuera |
| E-09 | passed | passed | A4 y Full HD primeros, sin segunda A4 |
| E-10 | passed | passed | `2480×3508 px · 150 dpi` y `· 300 dpi`, los dos |
| E-11 | passed | **failed** | Android: lienzo #FFFFFF. Windows: blanco solo en un rectángulo de 403×258 arriba a la izquierda de un contorno de 677×952 → BUG-001 |
| E-12 | passed | **failed** | Android: (245, 230, 200) = #F5E6C8 en todo el lienzo. Windows: el color es el correcto pero cubre solo una parte → BUG-001 |
| E-13 | passed | passed | damero de dos tonos (#FFFFFF y #D9D9D9) en todo el lienzo, en las dos |
| E-14 | passed (parte visible) | **failed** | Android: entera, centrada (545 px arriba y abajo), a 0°. Windows: contorno entero y centrado (405 y 402 px a los lados), pero la obra no se ve entera por BUG-001. "Capa 1" activa: **blocked** en las dos, no hay panel de capas hasta F-003 |
| E-15 | passed | passed | un solo reciente A4 después del doble toque |
| E-16 | blocked | blocked | QT-78: la app no puede producir un archivo dañado |
| E-17 | — | passed | ventana de 700 px: regiones apiladas, prearmados en fila lateral, Crear fijo abajo |

**Observaciones (no son bugs: ningún criterio las cubre)**
- Accesibilidad, Windows: UI Automation no expone ningún elemento de la ventana de Compose
  (árbol vacío). Un lector de pantalla de Windows no podría recorrer la pantalla. Va a la
  pregunta abierta Q-90 (accesibilidad del producto).
- Usabilidad, Android en horizontal (teléfono): las dos columnas quedan recortadas a una franja
  de unos 600 de 1344 px de alto, con espacio vacío arriba de Crear. Recientes y Fondo quedan
  fuera de la vista hasta desplazar. `ux-ui/screen-obra-nueva.md` no define la altura angosta.
- Android: al instalar, el sistema avisa que `libnkpainter_native_memory.so` no está alineada a
  16 KB y que la app corre en modo de compatibilidad. Está fuera de esta historia; se informa
  para planificarlo.
- Android: el lienzo de 16×16 se ve a 514 px, que es el tope de zoom de 3200 % (RN-limites-zoom).
  Es coherente con la regla.
- Consola: sin errores en logcat ni en la salida de `desktopApp:run` durante la corrida.

Datos dejados atrás: los recientes de los dos equipos quedan con tamaños de prueba (ver el índice).

### Corrida 2026-10-28 (2) · agente · re-corrida en Windows tras BUG-001
Solo Windows, E-01 a E-14, con los mismos medios que la primera corrida y el arreglo de la
unidad 108. Evidencia: `evidence/2026-10-28-r2/windows/`.

| Escenario | Windows | Visto |
|---|---|---|
| E-01 | passed | contorno de 677 px de ancho; fondo blanco en todo el lienzo; reciente `2480 3508 300` |
| E-02 | passed | Full HD blanco de borde a borde (1475×831); reciente `1920 1080 72` |
| E-03 | passed | Pantalla 2560×1440; reciente `2560 1440 72` |
| E-04 | passed | reciente `3000 2000 150` |
| E-05 | passed | reciente `1181 1772 300` |
| E-06 | passed | reciente `576 720 72` |
| E-07 | passed | Full HD, 3000×2000 a 150, A4 |
| E-08 | passed | 576×720, 2K, A5, Full HD, 3000×2000; A4 fuera |
| E-09 | passed | A4, Full HD, sin segunda A4 |
| E-10 | passed | `2480 3508 150` y `2480 3508 300` |
| E-11 | passed | #FFFFFF en todo el lienzo (antes, solo 403×258) |
| E-12 | passed | (245, 230, 200) = #F5E6C8 en todo el lienzo |
| E-13 | passed | damero en todo el lienzo |
| E-14 | passed (parte visible) | entera, centrada (405 y 402 px a los lados), a 0°, con todo el fondo pintado. "Capa 1": sigue **blocked** hasta F-003 |
