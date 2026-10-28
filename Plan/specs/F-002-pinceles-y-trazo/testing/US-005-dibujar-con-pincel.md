# Pruebas · US-005 Dibujar con un pincel
feature: F-002 · unidad 37 · plan aprobado 2026-10-28 (QT-108)

Cada escenario se corre en las dos áreas que declara el stack, por la pantalla:
- **Android**: emulador `Pixel_10_Pro_XL` por `adb` (toques, arrastres y texto con
  `adb shell input`; capturas con `screencap`). El emulador dibuja con dedo (presión completa).
- **Windows**: `:desktopApp:run`, ventana de 1500×1000 px, mouse y teclado sintéticos desde
  PowerShell (QT-79). El trazo rápido y el lápiz, con la inyección de puntero sintético de
  Windows (`InjectSyntheticPointerInput`, la misma que usa el `-PpenProbe` de la unidad 17),
  desde `tools/inject-pen.ps1`.

## Cómo se observa el resultado
- **Medidas en píxeles del lienzo**: los escenarios que miden usan un lienzo chico, que
  "Ajustar" agranda (zoom de ajuste mayor a 100 %). Se lee el zoom del ajuste a partir del
  borde del lienzo en la captura (ancho en pantalla ÷ ancho del lienzo) y se divide la medida
  en pantalla por ese zoom. Tolerancia: ±1 px del lienzo.
- **Color**: un píxel del centro de la línea en la captura, ±2 por canal por la compresión del
  color de pantalla.
- **Pincel activo**: el nombre bajo la herramienta Pincel y el pincel marcado en el panel.

## Datos
Solo usando la app (QT-78), con dos excepciones que se piden en QT-108:
- **"Primera vez en el equipo"** (E-02) necesita que no haya preferencias de dibujo. Android:
  `adb shell pm clear com.nkpainter.android` (borra los datos de la app en el emulador, que solo
  tiene datos de prueba; también los recientes). Windows: setup renombra
  `%APPDATA%\nk-painter\drawing-settings.txt` a `.qa-bak`; teardown lo devuelve.
  Scripts: `data/US-005/E-02-setup.ps1` · `data/US-005/E-02-teardown.ps1`.
- Las obras viven en memoria y se pierden al cerrar. Los tamaños creados quedan en los
  recientes (dato dejado atrás ya registrado en F-001).

Sin efectos materiales.

## Escenarios

### E-01 · El panel ofrece los 7 pinceles
- **Cubre:** CA-01 (RN-set-base) · prioridad alta
- **Pasos:** obra A4; tocar Pincel.
- **Esperado:** el panel lista exactamente Lápiz, Tinta, Rotulador, Pincel redondo, Aerógrafo,
  Pincel suave y Píxel, sin cortar ningún nombre.

### E-02 · La primera vez se dibuja con el Lápiz
- **Cubre:** CA-02 (RN-pincel-inicial) · prioridad alta
- **Setup / cleanup:** `data/US-005/E-02-setup.ps1` · `data/US-005/E-02-teardown.ps1`
- **Pasos:** abrir la app; obra 400×300 px; dibujar una línea de (50, 150) a (350, 150) sin
  elegir pincel.
- **Esperado:** la herramienta dice "Lápiz"; en el panel, Lápiz marcado y Presión en Normal; la
  línea mide 4 px (grosor de fábrica del Lápiz).

### E-03 · Al reabrir vuelve el último pincel usado
- **Cubre:** CA-03 (RN-pincel-inicial) · prioridad alta
- **Pasos:** elegir Tinta, dibujar una línea, cerrar la app (Android: `am force-stop`), abrirla y
  crear una obra.
- **Esperado:** la herramienta dice "Tinta" y la Tinta está marcada en el panel.

### E-04 · Elegir un pincel lo deja como el único activo
- **Cubre:** CA-04 (RN-un-pincel-activo) · prioridad alta
- **Pasos:** con el Lápiz activo, elegir Aerógrafo; cerrar el panel; dibujar una línea.
- **Esperado:** solo Aerógrafo marcado en el panel; la herramienta dice "Aerógrafo"; la línea
  es ancha (60 px) y de borde difuso, no la de 4 px del Lápiz.

### E-05 · El Rotulador pinta con #000000
- **Cubre:** CA-05 (RN-pinta-en-capa-activa) · prioridad alta
- **Pasos:** obra A4 con fondo blanco; Rotulador; línea de (100, 100) a (500, 100).
- **Esperado:** el píxel del centro de la línea es #000000 (±2).

### E-06 · Solo en la capa activa
- **Cubre:** CA-06 · **blocked**: hacen falta dos capas (F-003, US-009). Se corre en la QA de F-003.

### E-07 · El Píxel no suaviza los bordes
- **Cubre:** CA-07 (RN-pincel-pixel-sin-suavizado) · prioridad alta
- **Datos:** obra de 30×30 px (el ajuste la lleva a unos 3200 %; el zoom real se lee del borde).
- **Pasos:** Píxel; diagonal de (10, 10) a (20, 20).
- **Esperado:** cada píxel del lienzo en la captura es negro (±2) o del color del fondo: ninguno
  gris. Equivale a mirarla al 3200 %: el criterio pide ese zoom para ver píxeles enteros.

### E-08 · Un toque sin mover deja un punto de 20 px
- **Cubre:** CA-08 (RN-toque-deja-punto) · prioridad alta
- **Datos:** obra de 600×600 px.
- **Pasos:** Pincel redondo (grosor de fábrica 20); tocar (300, 300) y levantar.
- **Esperado:** un punto de 20 px de diámetro (±1) centrado en (300, 300) (±1).

### E-09 · Lo que sale del lienzo no se pinta, y el trazo no se corta
- **Cubre:** CA-09 (RN-trazo-recortado-al-lienzo) · prioridad alta
- **Datos:** A4 (2480×3508 px), vista ajustada: a la derecha del lienzo queda área de dibujo.
- **Pasos:** Rotulador; sin levantar, de (2300, 1000) a (2700, 1000) y de vuelta a (2300, 1200)
  (puntos de pantalla calculados con el zoom del ajuste).
- **Esperado:** se ven los dos tramos dentro del lienzo, que llegan al borde derecho; fuera del
  lienzo el área de dibujo sigue de su color, sin tinta.

### E-10 · Deshacer el trazo que salió y volvió
- **Cubre:** CA-10 · **blocked**: historial (F-004, US-014).

### E-11 · Cada trazo se deshace por separado
- **Cubre:** CA-11 · **blocked**: historial (F-004, US-014).

### E-12 · Demora de la parte de la app
- **Cubre:** CA-12 (requisito no funcional), en parte · prioridad media
- **Datos:** A4 a 300 dpi con **una** capa: la obra de referencia tiene 30 y la app no crea
  capas hasta F-003.
- **Pasos:** Lápiz; 20 trazos. Android: el registro `nkpainter-latency` (logcat). Windows:
  `-PlatencyLog` y 20 trazos con `tools/inject-pen.ps1`.
- **Esperado:** p95 de la parte de la app por debajo de 50 ms (QT-92).
- **Lo que no prueba:** la obra de 30 capas, los equipos de 4 y 8 GB (es el emulador y esta PC)
  y la demora de punta a punta (sin cámara lenta, QT-92). El criterio queda `qa —` aunque
  pase: es evidencia parcial.

### E-13 · Un círculo rápido sale continuo
- **Cubre:** CA-13 (requisito no funcional) · prioridad alta
- **Datos:** obra de 1400×1400 px.
- **Pasos:** Tinta; un círculo de 1000 px de diámetro en 0,3 s. Android: 60 movimientos del
  dedo con `adb shell sendevent` en un solo gesto. Windows: 60 puntos con
  `tools/inject-pen.ps1` (presión completa).
- **Esperado:** la línea es continua (ningún corte) y sin tramos rectos visibles en la captura.

## Cobertura
| Criterio | Escenarios | Estado |
|---|---|---|
| CA-01 | E-01 | android passed · windows pending |
| CA-02 | E-02 | passed (las dos) |
| CA-03 | E-03 | android passed · windows pending |
| CA-04 | E-04 | android passed · windows pending |
| CA-05 | E-05 | android passed · windows pending |
| CA-06 | E-06 | blocked (F-003) |
| CA-07 | E-07 | android passed · windows pending |
| CA-08 | E-08 | android passed · windows pending |
| CA-09 | E-09 | android passed · windows pending |
| CA-10 | E-10 | blocked (F-004) |
| CA-11 | E-11 | blocked (F-004) |
| CA-12 | E-12 | android passed (parcial) · windows pending |
| CA-13 | E-13 | **android failed (BUG-001)** · windows pending |

Bordes buscados: repetir el toque del panel (elegir el pincel ya activo, E-04 lo cubre de paso);
cerrar la app en medio de un trazo es de F-007; datos enormes: E-09 usa A4.

## Ejecuciones
Evidencia: `evidence/2026-10-28/android/` (prefijo `a-`) y `evidence/2026-10-28/windows/` (prefijo `w-`).
Build: el árbol de trabajo del 2026-10-28, que ya incluye las unidades 38 a 40 (US-006 y US-008).

### Corrida 2026-10-28 · Android (emulador `Pixel_10_Pro_XL`) · completa
| Escenario | Estado | Real | Evidencia |
|---|---|---|---|
| E-01 | passed | en A4, el panel lista Lápiz, Tinta, Rotulador, Pincel redondo, Aerógrafo, Pincel suave y Píxel; la fila se desplaza de costado (como dice `screen-pinceles.md`) y ningún nombre queda cortado | `a-E04-01-panel.png`, `a-E04-02-panel-fin.png` |
| E-02 | passed | tras `pm clear`: la herramienta dice "Lápiz", Lápiz y Normal marcados; línea de 3,9 px | `a-E02-02-obra.png`, `a-E02-03-linea.png`, `a-E02-04-panel.png` |
| E-03 | passed | Tinta, `force-stop`, reabrir, crear: la herramienta dice "Tinta" y es la marcada | `a-E03-01-tinta.png`, `a-E03-02-reabierta.png` |
| E-04 | passed | solo Aerógrafo marcado (también los dos chips de la parte desplazada); la línea es difusa y ancha: tinta visible en 54 px y más de 10 % de gris en 38 px, con un pincel de grosor 60 (el borde difuso se desvanece antes del diámetro nominal) | `a-E04-01-panel.png`, `a-E04-02-panel-fin.png`, `a-E04-05-linea-400.png` |
| E-05 | passed | centro de la línea #000000 en A4 | `a-E05-01-rotulador.png` |
| E-06 | blocked | dos capas (F-003) | — |
| E-07 | passed | obra 30×30 a 3207 %: 12 píxeles negros puros sobre la diagonal, ninguno intermedio | `a-E07-01-diagonal.png` |
| E-08 | passed | punto de 19,6 × 20,1 px centrado en (300, 300,2) | `a-E08-01-punto.png` |
| E-09 | passed | con la pantalla horizontal (A4 ajustado deja área a los costados): dos tramos que llegan al borde derecho; fuera del lienzo, nada | `a-E09-03-trazo.png`, `a-E09-04-zoom.png` |
| E-10, E-11 | blocked | historial (F-004) | — |
| E-12 | passed (parcial) | 20 trazos con Lápiz en A4: p95 por trazo entre 21 y 32 ms, máximo aislado 119 ms; el criterio sigue `qa —` (evidencia parcial, como dice el plan) | `a-E12-latency-log.txt` |
| E-13 | **failed** | la línea es continua (sin cortes), pero **termina con un tramo recto**: los últimos ~3 puntos antes de levantar no siguen la curva (unos 23° del círculo). Pasa igual en un círculo lento (2,1 s) → BUG-001 | `a-E13-04-circulo.png`, `a-E13-05-repeticion-y-control.png`, `a-E13-06-recorte-final-recto.png` |

- **Cómo se manejó E-13:** `sendevent` está bloqueado en el emulador (imagen de producción, sin root); el círculo se mandó por la consola del emulador (`adb emu event send`) en 8 tandas: 362 ms de bajar a levantar. El control lento fue un punto por llamada.
- **E-09 en horizontal:** en vertical el A4 ajustado ocupa todo el ancho y no queda área a la derecha; se giró el emulador (`wm user-rotation lock 1`) y se devolvió al terminar.
- **Datos:** E-02 `pm clear` (corrido; el teardown en Android no tiene nada que devolver: el emulador solo tiene datos de prueba). Quedan en los recientes 400×300, 30×30, 600×600 y 1400×1400 (dato dejado atrás, como en F-001).
- **Observaciones (no son bugs de esta historia):**
  - Dos "nk-painter no responde" (ANR) al escribir 1400 en Ancho/Alto de Obra nueva: el hilo principal esperaba al dibujado de la GPU (`HardwareRenderer.syncAndDrawFrame`) con `system_server` al 80 % en núcleo. Causa sospechada: traba gráfica del emulador. **No confirmada.** Al tercer intento entró sin problema. `a-E13-00-anr.png`, `a-E13-anr-dropbox.txt`.
  - F-001: después de girar la pantalla, "Ajustar vista" no encaja el A4 en la pantalla horizontal (queda con el zoom de la vertical); creada de nuevo en horizontal, encaja. `a-E09-01-ajustada.png`, `a-E09-02-horizontal.png`. Fuera del alcance de esta historia: se reporta.
  - Aviso del sistema al abrir: "This app isn't 16 KB compatible" (`libnkpainter_native_memory.so` y dos bibliotecas de terceros sin alinear a 16 KB). `a-E02-01-inicio.png`.
  - En la barra inferior, "Pincel redondo" bajo Goma queda pisado por la barra de gestos del sistema. `a-E02-02-obra.png`.
  - Un trazo que empieza a menos de ~50 px del borde izquierdo lo toma Android como gesto de "atrás" y la app pasa a segundo plano (conducta del sistema).

### Corrida 2026-10-28 · Windows · **interrumpida**
| Escenario | Estado | Real | Evidencia |
|---|---|---|---|
| E-02 | passed | sin `drawing-settings.txt`: la herramienta dice "Lápiz", Lápiz marcado y Normal marcado (el renglón Presión se ve en el borde inferior de la hoja); línea de 3,7 a 4,0 px | `w-E02-02-obra.png`, `w-E02-03-linea.png`, `w-E02-04-panel.png` |
| E-01, E-03 a E-13 | pending | la corrida se cortó: apareció en la obra un trazo con el Rotulador que no hizo el agente y el pincel activo cambió; alguien estaba usando la PC, y el mouse sintético (QT-79) no puede convivir con eso | `w-E02-05-panel-presion.png` |

- **Datos:** E-02 setup y teardown corridos (no había preferencias antes; quedan las que creó la app).
