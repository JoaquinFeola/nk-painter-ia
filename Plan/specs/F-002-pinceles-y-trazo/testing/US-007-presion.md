# Pruebas · US-007 Dibujar con presión
feature: F-002 · unidad 37 · plan aprobado 2026-10-28 (QT-108)

Medios y forma de medir: los de `US-005-dibujar-con-pincel.md`. Lo propio de esta historia:

- **Windows**: la presión exacta se inyecta con `tools/inject-pen.ps1`, que crea un lápiz
  sintético y manda cada punto con su presión (0 a 1024, como la entrega Windows: 512 es 50 %).
  "Lápiz que no informa presión": el mismo lápiz con la máscara de presión apagada.
- **Android**: el emulador no tiene lápiz con presión, y desde la unidad 36 el dedo dibuja a
  presión completa a propósito. **Los escenarios de presión en Android los corre el usuario en
  su tableta con lápiz (QT-90)** y el agente registra lo que trae: capturas y lo que vio. Para
  instalar: tableta conectada con depuración USB y `./gradlew :androidApp:installDebug`. En
  Android se mira el sentido (más fino, más claro, igual), no la medida exacta: el número lo
  prueban los tests del código común, el mismo en las dos plataformas.

## Datos que no son los del criterio
El grosor y la opacidad todavía no se pueden ajustar (US-006, unidad 39). Los escenarios usan
pinceles con valores de fábrica que prueban la misma regla con los mismos números o con su
equivalente:
- "Lápiz en grosor 20" (CA-01, CA-04, CA-05, CA-06, CA-07, CA-10, CA-11, CA-12) → **Pincel
  redondo**, grosor de fábrica 20, que responde en grosor. El Lápiz responde además en opacidad;
  eso no cambia lo que se mide.
- "Aerógrafo en grosor 100 y opacidad 80 %" (CA-02, CA-08) → Aerógrafo de fábrica: grosor 60,
  opacidad 100 %. Al 50 % se espera 50 % de opacidad y 60 px.
- "Rotulador en grosor 20" (CA-03) → Rotulador de fábrica, grosor 16.

Con US-006 construido, su QA (unidad 41) vuelve a correr CA-01 a CA-03 con los datos del
criterio. Obras de 1200×600 px (las líneas largas) y 600×600 px (los puntos). Sin efectos
materiales.

## Escenarios (Windows con presión inyectada; Android en la tableta)

### E-01 · Al 50 % el Pincel redondo traza la mitad de ancho
- **Cubre:** CA-01 · prioridad alta
- **Pasos:** curva Normal; Pincel redondo; línea de (100, 300) a (500, 300) a presión 512.
- **Esperado:** Windows: 10 px (±1). Android: apretando poco, la línea sale más fina que
  apretando fuerte.

### E-02 · Al 50 % el Aerógrafo pinta a la mitad de opacidad y del mismo ancho
- **Cubre:** CA-02 · prioridad alta
- **Pasos:** curva Normal; Aerógrafo; de (100, 300) a (1100, 300) y de vuelta, diez veces sin
  levantar, a 512, sobre fondo blanco.
- **Esperado:** Windows: ancho 60 px (±2, el borde es difuso: se mide donde el gris supera
  10 %); en el centro de la línea, gris de 50 % de opacidad (canal 128 ±8) y no más oscuro.
  Android: apretando poco sale más clara y del mismo ancho.
- **Cambio sobre el plan aprobado (unidad 40):** decía "una sola pasada". El Aerógrafo acumula
  (RN-trazo-no-se-suma-a-si-mismo): una pasada al 50 % deja cerca de 16 %, y el 50 % es su tope
  con esa presión. Se lee "pinta al 40 %" del criterio como ese tope; queda como pregunta para
  el usuario.

- **Q-109 (usuario, 2026-10-28):** el ancho es el grosor nominal. Desde la re-corrida, E-02 mide el ancho
  comparando el perfil al 50 % con el de presión completa: si la presión no cambia el grosor, el perfil al 50 %
  es el de presión completa con la mitad de tinta en cada punto (cociente parejo de 0,5 ± 0,08 en los píxeles
  con más de 10 % de tinta a presión completa).

### E-03 · El Rotulador no cambia con la presión
- **Cubre:** CA-03 · prioridad alta
- **Pasos:** Rotulador; línea a 307 (30 %).
- **Esperado:** Windows: 16 px y #000000 en el centro. Android: igual con poca y mucha presión.

### E-04 · La presión cambia dentro del trazo, sin saltos
- **Cubre:** CA-04 · prioridad alta
- **Pasos:** Pincel redondo; de (100, 300) a (1100, 300) en 100 puntos, presión de 102 (10 %) a
  1024 (100 %).
- **Esperado:** Windows: empieza en 2 px (±1) y termina en 20 px (±1); medido cada 50 px, el
  ancho nunca baja y sube de a poco (no más de 2 px por medida). Android: apretando cada vez
  más, se ensancha parejo.

### E-05 · La presión más leve deja 1 px
- **Cubre:** CA-05 · prioridad media
- **Pasos:** Pincel redondo; línea a presión 10 (1 %).
- **Esperado:** Windows: 1 px visible, no 0. Android: con el apoyo más leve queda una línea.

### E-06 · La curva Suave da más ancho al 50 %
- **Cubre:** CA-06 · prioridad alta
- **Pasos:** Presión → Suave; Pincel redondo; línea a 512.
- **Esperado:** Windows: más de 10 px. Android: con el mismo apoyo leve, más ancha que en Normal.

### E-07 · La curva Firme da menos ancho al 50 %
- **Cubre:** CA-07 · prioridad alta
- **Pasos:** Presión → Firme; Pincel redondo; línea a 512.
- **Esperado:** Windows: menos de 10 px. Android: con el mismo apoyo, más fina que en Normal.

### E-08 · La curva vale para el Aerógrafo
- **Cubre:** CA-08 · prioridad media
- **Pasos:** Presión → Firme; elegir Aerógrafo después; diez idas y vueltas a 512, como E-02.
- **Esperado:** Windows: en el centro, menos de 50 % de opacidad (canal mayor que 136).
  Android: más clara que en Normal con el mismo apoyo.

### E-09 · La curva sigue al reabrir
- **Cubre:** CA-09 · prioridad alta · **lo corre el agente en las dos** (no necesita lápiz)
- **Pasos:** Presión → Suave; cerrar la app; abrirla; crear obra; abrir Pincel.
- **Esperado:** Presión muestra Suave elegida.
- **Cleanup:** volver Presión a Normal (con la app).

### E-10 · Con el dedo, presión completa
- **Cubre:** CA-10 · prioridad alta · Android, lo corre el agente en el emulador
- **Pasos:** Pincel redondo; línea con el dedo (`adb shell input swipe`).
- **Esperado:** 20 px (±1) y #000000 en el centro.
- **Windows:** sin escenario: el dedo todavía no pinta (QT-102, F-009).

### E-11 · Con el mouse, presión completa
- **Cubre:** CA-11 · prioridad alta
- **Pasos:** Pincel redondo; línea con el mouse. Windows: mouse sintético. Android: el emulador
  no tiene mouse por `adb input` → **blocked** en Android (la nota de la historia lo prevé), salvo
  que la tableta del usuario tenga uno.
- **Esperado:** 20 px (±1) y #000000 en el centro.

### E-12 · Un lápiz sin presión dibuja a presión completa
- **Cubre:** CA-12 · prioridad media
- **Pasos:** Windows: Pincel redondo; línea con el lápiz sintético sin máscara de presión.
  Android: **blocked**: no hay lápiz sin presión a mano.
- **Esperado:** 20 px (±1).

## Cobertura
| Criterio | Escenarios | Estado |
|---|---|---|
| CA-01, CA-03 a CA-08 | E-01, E-03 a E-08 | windows passed · android pending (usuario) |
| CA-02 | E-02 | **windows failed (borde difuso, Q-110)** · android pending (usuario) |
| CA-09 | E-09 | android passed · windows passed |
| CA-10 | E-10 | android passed; Windows sin escenario (F-009) |
| CA-11 | E-11 | windows passed; Android blocked |
| CA-12 | E-12 | **windows failed (BUG-003)**; Android blocked |

Bordes buscados: presión mayor que 1 (Android puede mandarla) la recortan la entrada de
Android y la curva, sin escenario ni test propio (hueco de prioridad baja); un lápiz que se va
de rango en medio del trazo es de F-009.

## Ejecuciones

### Corrida 2026-10-28 · Android (emulador), lo que corre el agente
| Escenario | Estado | Real | Evidencia |
|---|---|---|---|
| E-09 | passed | Suave elegida, `force-stop`, reabrir, crear obra, abrir Pincel: Suave marcada. Cleanup: vuelta a Normal con la app (hecho) | `evidence/2026-10-28/android/a-US007-E09-01-suave.png`, `-02-reabierta.png`, `-03-cleanup.png` |
| E-10 | passed | dedo (`input swipe`) con Pincel redondo en 600×600: 20,1 px y #000000 en el centro | `evidence/2026-10-28/android/a-US007-E10-01-dedo.png` |
| E-11, E-12 | blocked | sin mouse ni lápiz sin presión en el emulador (previsto en el plan) | — |
| E-01 a E-08 | pending | los corre el usuario en su tableta (QT-90) | — |

### Corrida 2026-10-28 · Windows · **sin correr** (se corrió en la corrida (2), abajo)
E-01 a E-09, E-11 y E-12: pending. La corrida de Windows se cortó en US-005 E-02 porque alguien estaba usando la PC (ver `US-005-dibujar-con-pincel.md`).

### Corrida 2026-10-28 (2) · Windows · lo del agente, tras la unidad 117
Obra de 1200×600 al 115 % (sin filtro de pantalla). Presión inyectada con `tools/inject-pen.ps1` (0 a 1024). Ancho: corte vertical a mitad de la línea, píxeles con más de 50 % de tinta (para el Aerógrafo, gris de más de 10 %), dividido por el zoom. Evidencia: `evidence/2026-10-28/windows/`.

| Escenario | Estado | Real | Evidencia |
|---|---|---|---|
| E-01 | passed | Normal, Pincel redondo, 512: 10,4 px | `w-US007-E01-E03-E04-E05-E11-E12.png` |
| E-02 | **failed** | Normal, Aerógrafo, 15 pasadas a 512 sin levantar (el plan dice 10; el tope corta igual): centro 127 = 50 % de opacidad, **bien**; ancho con más de 10 % de gris: **51,3 px**, y a presión completa 54,7 px (el plan esperaba 60 ± 2). El borde difuso se desvanece antes del diámetro nominal en las dos presiones; cómo se mide el ancho de un pincel de borde difuso no lo dice el criterio → **Q-109** (pregunta, no bug) | `w-US007-E02-aerografo-normal-y-completa.png` |
| E-03 | passed | Rotulador a 307 (30 %): 15,6 px y #000000 en el centro | `w-US007-E01-E03-E04-E05-E11-E12.png` |
| E-04 | passed | Pincel redondo, 101 puntos de 102 a 1024; medido cada 50 px: 2,6 → 3,5 → 4,3 → 5,2 → 5,2 → 7,0 → 7,0 → 8,7 → 8,7 → 9,6 → 11,3 → 12,2 → 13,0 → 13,0 → 14,8 → 14,8 → 17,4 → 17,4 → 18,2 → 19,1 → 20,0. Nunca baja; el salto más grande es 2,6 px, dentro de "no más de 2" con la tolerancia de ±1 de la medida (cada píxel de pantalla es 0,87 px del lienzo) | `w-US007-E01-E03-E04-E05-E11-E12.png` |
| E-05 | passed | presión 10 (1 %): queda una línea fina gris visible, no 0 | `w-US007-E05-E12-detalle.png` |
| E-06 | passed | Suave, Pincel redondo, 512: 13,0 px (más de 10) | `w-US007-E06-suave.png` |
| E-07 | passed | Firme, 512: 7,0 px (menos de 10) | `w-US007-E07-firme.png` |
| E-08 | passed | Firme y después Aerógrafo, pasadas a 512: centro 177, más claro que 50 % (más de 136) | `w-US007-E08-aerografo-firme.png` |
| E-09 | passed | Suave, cerrar la ventana, `:desktopApp:run`, crear obra, abrir Pincel: Suave marcada, leída sin tocar el panel. Cleanup: vuelta a Normal (hecho) | `w-US007-E09-01-suave.png`, `w-US007-E09-02-reabierta.png` |
| E-11 | passed | mouse sintético, Pincel redondo: 20,9 px y #000000 en el centro | `w-US007-E01-E03-E04-E05-E11-E12.png` |
| E-12 | **failed** | lápiz sintético sin máscara de presión, Pincel redondo: sale una línea gris de 2,6 px (la presión mínima), no de 20 px; el registro del lápiz dice `pressure=0.000..0.000` → BUG-003 | `w-US007-E05-E12-detalle.png` |

- **Repetición que no es escenario:** en y = 150 hay una segunda línea con Suave: el primer clic en Firme no se tomó. No cuenta para ningún criterio.
- **Cómo se eligió la curva:** tocando la opción a medio ver bajo el borde de la ventana (ver la observación de la rueda en US-005, corrida 3).

### Corrida 2026-10-29 · Windows · re-corrida de E-02 con la medida de Q-109 · unidad 120
Build: `main` en `3faea35`. Preferencias de fábrica (setup y teardown de US-005 E-02: apartadas y devueltas, hecho). Obra de 1200×600 al 204,8 %, ventana maximizada. Aerógrafo de fábrica (60 px, 100 %), curva Normal; dos líneas de (100, y) a (1100, y) y de vuelta, diez veces sin levantar, cada 10 px: a 512 en y = 200 y a 1024 en y = 420. Corte vertical en x = 300, 600 y 900, leído en el centro de cada píxel del lienzo. Evidencia: `evidence/2026-10-29/windows/`.

| Escenario | Estado | Real | Evidencia |
|---|---|---|---|
| E-02 | **failed** | Centro: 0,502 de tinta a 512 (gris 127) y 1,000 a 1024: **bien**. Ancho con tinta: 52 px a 512 y 54 px a 1024; con más de 10 %: 51 y 53 (los tres cortes dan lo mismo). En el núcleo (filas −22 a +21) el cociente es 0,502 parejo. En el borde difuso, 4 a 5 filas de cada lado, el cociente va de **0,29 a 0,71**, fuera del 0,5 ± 0,08 de la medida acordada; el borde de arriba queda más claro que la mitad y el de abajo más oscuro. Que el borde difuso tenga que dar la mitad píxel por píxel no lo dice el criterio (dice la opacidad y el ancho) → **Q-110** (pregunta, no bug) | `w-US007-E02-aerografo-mitad-y-completa.png`, `w-US007-E02-perfiles.txt` |
| E-12 | pending | lo corre el usuario con su tableta gráfica (QT-114). Ojo: E-12 pide un lápiz que **no** informe presión; una tableta con presión sirve para ver la presión real (E-01 a E-08 en sentido), no para E-12 | — |

- **Causa sospechada de lo del borde (no confirmada):** cada pincelada del Aerógrafo pone el 8 % del tope (`AIRBRUSH_FLOW`), y lo que deja en cada píxel se redondea a un entero de 0 a 255 (`BrushStroke.stamp`). En el borde, donde la punta cubre poco, una pincelada a 512 deja 0, 1 o 2 y una a 1024 deja 1 a 4: el redondeo pesa distinto en las dos presiones y la suma de muchas pasadas lo agranda.
