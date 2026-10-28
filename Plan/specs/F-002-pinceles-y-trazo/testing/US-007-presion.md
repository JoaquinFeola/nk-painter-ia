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
| CA-01 a CA-08 | E-01 a E-08 | pending (Windows: agente; Android: usuario) |
| CA-09 | E-09 | android passed · windows pending |
| CA-10 | E-10 | android passed; Windows sin escenario (F-009) |
| CA-11 | E-11 | pending (Windows); Android blocked |
| CA-12 | E-12 | pending (Windows); Android blocked |

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

### Corrida 2026-10-28 · Windows · **sin correr**
E-01 a E-09, E-11 y E-12: pending. La corrida de Windows se cortó en US-005 E-02 porque alguien estaba usando la PC (ver `US-005-dibujar-con-pincel.md`).
