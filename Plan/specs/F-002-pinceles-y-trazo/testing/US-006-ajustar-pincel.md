# Pruebas · US-006 Ajustar un pincel
feature: F-002 · unidad 41 · plan aprobado 2026-10-29 (QT-115)

Medios, forma de medir y tolerancias: los de `US-005-dibujar-con-pincel.md`, con estos cambios:
- **Windows**: ventana maximizada (2560×1440). En 1500×1000 la app queda en 1500×1462 y tapa el botón Crear
  (corrida del 2026-10-29). Los valores se escriben en el campo y se confirman con Enter. Trazos con
  `tools/inject-pen.ps1` a presión completa, o con mouse sintético.
- **Android**: emulador `Pixel_10_Pro_XL` por `adb`. Los trazos de varios puntos van por la consola del
  emulador, como US-005 E-13 (`tools/emu-circle.py`).
- **Color**: un píxel del centro, con la captura leída en el centro de cada píxel del lienzo (zoom de
  más de 100 %, sin filtro). Tolerancia: la del criterio, ±1 por canal.
- Cada escenario corre en las dos áreas, por la pantalla.

## Datos
- **Preferencias de fábrica al empezar**: setup y teardown compartidos con US-005 E-02 (`data/US-005/`).
  En Windows se apartan y se devuelven las preferencias del usuario. En Android, `pm clear` (el
  emulador solo tiene datos de prueba, QT-108).
- **Obras**: 600×600 (puntos), 1200×600 (líneas) y 1200×1000 (temblor), fondo blanco, creadas con la app
  (QT-78). Quedan en los recientes (dato dejado atrás, como en F-001).
- Sin efectos materiales.

## Escenarios

### E-01 · El grosor elegido es el que pinta
- **Cubre:** CA-01 (RN-rango-grosor) · prioridad alta
- **Pasos:** obra 600×600; Pincel redondo, Grosor 40; un toque en (300, 300) sin moverse.
- **Esperado:** un punto de 40 px de diámetro (±1).

### E-02 · La opacidad elegida es la que pinta
- **Cubre:** CA-02 (RN-rango-opacidad) · prioridad alta
- **Pasos:** obra 1200×600; Rotulador, Opacidad 50; línea de (100, 300) a (1100, 300).
- **Esperado:** el centro de la línea es #808080 (±1 por canal).

### E-03 · Un trazo que se cruza consigo mismo no se oscurece
- **Cubre:** CA-03 (RN-trazo-no-se-suma-a-si-mismo) · prioridad alta
- **Pasos:** Rotulador, Opacidad 50; sin levantar: (200, 300) → (800, 300) → (500, 100) → (500, 500).
- **Esperado:** el cruce en (500, 300) es #808080 (±1), igual que el resto de la línea.

### E-04 · Dos trazos que se cruzan sí se suman
- **Cubre:** CA-04 (RN-trazo-no-se-suma-a-si-mismo) · prioridad alta
- **Pasos:** Rotulador, Opacidad 50; una línea horizontal y, después de levantar, una vertical que la cruza.
- **Esperado:** el cruce es #404040 (±1).

### E-05 · El Aerógrafo acumula y no pasa su opacidad
- **Cubre:** CA-05 (RN-trazo-no-se-suma-a-si-mismo) · prioridad alta
- **Pasos:** Aerógrafo, Opacidad 60. Una sola pasada de (100, 150) a (1100, 150). Después, en y = 400,
  diez idas y vueltas sin levantar.
- **Esperado:** el centro de la línea de diez pasadas es más oscuro que el de la de una pasada, y no
  más oscuro que el 60 % (canal 102 o más, ±1).

### E-06 · Se aceptan los extremos de los rangos
- **Cubre:** CA-06 (RN-rango-grosor, RN-rango-opacidad) · prioridad alta
- **Pasos:** Pincel redondo: Grosor 1 y Opacidad 1; después, Grosor 500 y Opacidad 100. Además, el
  deslizador de Grosor llevado a cada punta: Windows con las flechas, Android arrastrando.
- **Esperado:** cada valor queda tal cual, en el campo y en el deslizador.

### E-07 · Un grosor fuera de rango queda en el límite
- **Cubre:** CA-07 (RN-ajuste-fuera-de-rango) · prioridad alta
- **Pasos:** Pincel redondo: escribir 800 y confirmar; después escribir 0 y confirmar.
- **Esperado:** queda en 500 y se ve 500; después queda en 1 y se ve 1. Sin mensaje de error.

### E-08 · Una opacidad fuera de rango queda en el límite
- **Cubre:** CA-08 (RN-ajuste-fuera-de-rango) · prioridad media
- **Pasos:** Rotulador: escribir 150 en Opacidad y confirmar.
- **Esperado:** queda en 100 y se ve 100 %.

### E-09 · El estabilizador va de 0 a 10
- **Cubre:** CA-09 (RN-estabilizador-por-pincel) · prioridad media
- **Pasos:** Lápiz: Estabilizador 10; después escribir 11 y confirmar. En Windows, además, la flecha
  derecha con el deslizador en 10.
- **Esperado:** queda en 10 en los dos casos.

### E-10 · Un estabilizador alto alisa el temblor
- **Cubre:** CA-10 (RN-estabilizador-por-pincel) · prioridad alta
- **Datos:** obra 1200×1000. El mismo recorrido, de (100, y) a (1100, y), con un vaivén de ±6 px cada
  20 px (51 puntos), igual para los dos trazos.
- **Pasos:** Tinta con Estabilizador 0, en y = 300; Tinta con Estabilizador 10, en y = 700.
- **Esperado:** el centro de la línea con 10 se aparta de la recta, en promedio y en máximo, menos que
  la de 0.

### E-11 · Cada pincel recuerda sus ajustes
- **Cubre:** CA-11 (RN-ajustes-por-pincel) · prioridad alta
- **Pasos:** Tinta: Grosor 8, Estabilizador 3; Aerógrafo: Grosor 120; volver a la Tinta; volver al Aerógrafo.
- **Esperado:** la Tinta en 8 y 3; el Aerógrafo en 120.

### E-12 · Los ajustes siguen al reabrir la app
- **Cubre:** CA-12 (RN-ajustes-persisten) · prioridad alta
- **Pasos:** Lápiz: Grosor 12, Opacidad 70; cerrar la app (Windows: cerrar la ventana; Android:
  `force-stop`); abrir; crear obra; abrir el panel.
- **Esperado:** el Lápiz en 12 y 70 %, sin tocar nada.

### E-13 · Cambiar un ajuste no toca lo ya pintado
- **Cubre:** CA-13 (RN-ajustes-no-cambian-lo-pintado) · prioridad alta
- **Pasos:** obra 1200×600; Rotulador, Grosor 10, línea en y = 200; Grosor 50, línea en y = 400.
- **Esperado:** la primera sigue de 10 px (±1) y la segunda sale de 50 px (±1).

## Bordes buscados
- Campo vacío o con letras: lo fija una propuesta de UX sin confirmar, ningún criterio. Se mira de paso
  en E-07 y, si falla, va como pregunta, no como bug.
- Mover el deslizador sin soltar y cerrar la app: QT-98 dice que se guarda al soltar. No tiene criterio,
  queda sin escenario (prioridad baja).
- Obra enorme: lo cubre US-005 E-09 con A4.

## Cobertura
| Criterio | Escenarios | Estado |
|---|---|---|
| CA-01 | E-01 | windows passed · android pending |
| CA-02 | E-02 | windows passed · android pending |
| CA-03 | E-03 | windows passed · android pending |
| CA-04 | E-04 | windows passed · android pending |
| CA-05 | E-05 | windows passed · android pending |
| CA-06 | E-06 | windows passed · android pending |
| CA-07 | E-07 | windows passed · android pending |
| CA-08 | E-08 | windows passed · android pending |
| CA-09 | E-09 | windows passed · android pending |
| CA-10 | E-10 | **windows failed (Q-111)** · android pending |
| CA-11 | E-11 | windows passed · android pending |
| CA-12 | E-12 | windows passed · android pending |
| CA-13 | E-13 | windows passed · android pending |

## Ejecuciones

### Corrida 2026-10-29 · Windows · completa (unidad 41)
Build: `main` en `b3ef7a4`. `:desktopApp:run -PpenLog`, ventana maximizada; trazos con `tools/inject-pen.ps1` a presión completa; campos escritos y confirmados con Enter; deslizadores arrastrados con el mouse sintético (las flechas sintéticas trababan la app: `SendKeys` tardaba minutos por tecla; **no confirmado** si pasa con un teclado real). Medida en el centro de cada píxel del lienzo, zoom de más de 100 %. Evidencia: `evidence/2026-10-29/windows/`.

| Escenario | Estado | Real | Evidencia |
|---|---|---|---|
| E-01 | passed | obra 600×600 al 215 %: punto de 39 px negros más dos píxeles de borde que suman 1: 40 px en las dos direcciones | `w-US006-E01-punto-40.png`, `w-US006-E01-panel.png` |
| E-02 | passed | obra 1200×600 al 205 %: el centro de la línea es #7F7F7F a lo largo de toda la línea (#808080 ± 1) | `w-US006-E02-E03-E04-E13-rotulador.png` |
| E-03 | passed | el cruce del trazo consigo mismo es #7F7F7F, igual que el resto | ídem |
| E-04 | passed | el cruce de dos trazos es #3F3F3F (#404040 ± 1) | ídem |
| E-05 | passed | una pasada: centro 195; diez idas y vueltas: centro 102 (el 60 % justo), más oscuro que una pasada y no más | `w-US006-E05-US007-criterio.png` |
| E-06 | passed | campos: 1 y 1 %, después 500 y 100 %; deslizador de Grosor arrastrado a cada punta: 1 y 500 | `w-US006-E06-a-E11-campos.png` |
| E-07 | passed | 800 → queda y se ve 500; 0 → queda y se ve 1; sin mensaje | ídem |
| E-08 | passed | Rotulador, 150 → queda y se ve 100 % | ídem |
| E-09 | passed | Lápiz, 10 → 10; 11 → 10; arrastrar el deslizador más allá de la punta → 10. Las flechas no se pudieron probar (ver arriba) | ídem |
| E-10 | **failed** | obra 1200×1000 al 129 %, Tinta 8 px, vaivén de ±6 px cada 20 px. Apartamiento del centro de la línea respecto de la recta, cada 5 px: con 0, promedio 3,0 y máximo 6,1; con 10, promedio **1,3** (alisa) pero máximo **10,2**. Con 10 la línea es lisa hasta x ≈ 980 y en los últimos ~120 px vuelve a tener todo el vaivén, y más: picos de −8,3 y −7,6 contra ±6 de la mano. Coincide con el repaso del final que agregó la unidad 117 (BUG-001), que repasa los puntos reales que el estabilizador venía atrasando (**sospecha, no confirmada en el código**). El criterio dice "se aparta menos", sin decir si en promedio o en todo el trazo → **Q-111** | `w-US006-E10-US008-E03-a-E08.png`, `w-US006-E10-final-estab10.png` |
| E-11 | passed | Tinta 8 y 3; Aerógrafo 120; vuelta a la Tinta: 8 y 3; al Aerógrafo: 120 | `w-US006-E06-a-E11-campos.png` |
| E-12 | passed | Lápiz 12 y 70 %; cerrar la ventana, `:desktopApp:run`, crear obra, abrir Pincel: 12 y 70 % sin tocar nada | `w-US006-E12-US008-E04-reabrir.png` |
| E-13 | passed | Rotulador 10 y después 50: la primera mide 11,0 px a lo largo de toda la línea y la segunda 50,9 px | `w-US006-E02-E03-E04-E13-rotulador.png` |

- **Datos:** setup y teardown de US-005 E-02: preferencias apartadas y devueltas (hecho; quedaron con la Tinta en 7 y la goma en 52, como estaban). Quedan en los recientes 600×600, 1200×600 y 1200×1000 (dato dejado atrás, como en F-001).
- **Observación:** las líneas del Rotulador miden 1 px más que su grosor (11,0 y 50,9; US-005 E-08 dio 20,2 en un punto). Dentro de la tolerancia. **Causa sospechada, no confirmada:** el borde suavizado de cada pincelada se une con el de la siguiente y queda un poco más oscuro que el de una sola.
- **Observación:** al tocar la herramienta que no está activa, se activa sin abrir su panel (como dice la pantalla). El primer toque en Goma justo después de cerrar el panel Pincel no se tomó una vez; **no confirmado** que pase con un mouse real.
- **Cambio sobre el plan:** US-008 E-02 se corrió en una franja negra de la obra de 1200×1000, no en una obra de 30×30: la regla y la medida son las mismas.
