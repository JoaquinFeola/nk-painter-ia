# Pruebas · US-003 Navegar la vista
feature: F-001 · unidad 31 · plan aprobado 2026-10-28 (QT-81)

Cada escenario se corre en las dos áreas que declara el stack:
- **Android**: emulador `Pixel_10_Pro_XL` por `adb`. Los dos dedos se inyectan con
  `adb shell sendevent` sobre la pantalla táctil virtual del emulador (protocolo multitáctil
  de Linux, dos contactos); `adb shell input` no tiene dos dedos. Capturas con `screencap`.
- **Windows**: `:desktopApp:run`. Esta PC no tiene pantalla táctil, así que los dos dedos se
  inyectan con la inyección táctil de Windows (`InitializeTouchInjection` + `InjectTouchInput`
  de user32, desde PowerShell); el resto, mouse y teclado sintéticos como en QT-79. Si
  Windows no entrega esos toques a la app, los escenarios de gestos quedan `blocked` con esa
  causa (la nota de la historia ya lo prevé) y lo que no es gesto sigue.

## Cómo se observa el resultado
- **Zoom y giro**: el indicador `NNN % · NN°` mientras los dedos siguen apoyados (se captura
  con los dedos abajo: la inyección deja los contactos quietos entre pasos).
- **Lo que queda bajo los dedos**: una marca pintada antes (un punto con el mouse o el dedo)
  y su posición en las capturas antes y después, en píxeles de pantalla, con ±3 px de
  tolerancia por redondeo.
- **El giro al soltar**: el borde del lienzo en la captura (a 90° sus lados quedan derechos;
  a 80°, inclinados).
- **Nada pintado**: el lienzo en la captura sin ningún píxel del color de tinta.

## Datos
Solo usando la app (QT-78): cada escenario crea su obra desde Obra nueva. Las obras viven en
memoria y se pierden al cerrar; los tamaños creados quedan en los recientes, como en las
corridas anteriores (dato dejado atrás, ya registrado en el índice). Sin efectos materiales.

## Escenarios

### E-01 · Pellizcar abriendo deja quieto lo que está bajo los dedos
- **Cubre:** CA-01 (RN-zoom-centro-de-dedos) · prioridad alta
- **Datos:** A4 a 300 dpi (2480×3508 px), fondo blanco.
- **Pasos:** pintar un punto cerca del centro del área; apoyar dos dedos a 100 px a cada lado
  del punto; abrirlos hasta 300 px a cada lado, en 10 pasos.
- **Esperado:** el indicador sube de zoom (×3 del inicial); el punto sigue bajo el centro de los dedos.
- **Nota:** el píxel exacto (1200, 800) a 100 % lo prueba `ViewportGestureTest` (build); en
  pantalla se observa la regla con una marca visible, al zoom que dé el ajuste.

### E-02 · No se acerca más allá del 3200 %
- **Cubre:** CA-02 (RN-limites-zoom) · prioridad alta · borde
- **Datos:** 16×16 px a 72 dpi: al abrir, el ajuste lo muestra a 3200 % (estado "datos extremos").
- **Pasos:** pellizcar abriendo; capturar con los dedos abajo.
- **Esperado:** el indicador dice `3200 %` y el lienzo no cambia de tamaño.

### E-03 · No se aleja más allá del 5 %
- **Cubre:** CA-03 (RN-limites-zoom) · prioridad alta · borde
- **Datos:** A4 a 300 dpi.
- **Pasos:** pellizcar cerrando de 800 a 100 px de separación (factor 8, pasa del límite);
  soltar; pellizcar cerrando otra vez.
- **Esperado:** el primer gesto termina en `5 %`; en el segundo el indicador sigue en `5 %`.

### E-04 · Arrastrar con dos dedos desplaza la vista
- **Cubre:** CA-04 (RN-gestos-de-vista) · prioridad alta
- **Datos:** A4 a 300 dpi, vista a 0°, con un punto pintado.
- **Pasos:** dos dedos a 200 px de distancia; moverlos juntos 300 px a la derecha en 10 pasos.
- **Esperado:** el punto se movió 300 px a la derecha en la pantalla; el zoom no cambió.

### E-05 · Girar con dos dedos gira la vista
- **Cubre:** CA-05 (RN-gestos-de-vista, RN-rotacion-con-imanes) · prioridad alta
- **Pasos:** dos dedos a 300 px uno del otro; girarlos 30° alrededor de su centro; capturar
  con los dedos abajo; soltar.
- **Esperado:** el indicador dice `30°`; al soltar el lienzo queda girado 30° (30° está lejos del imán).

### E-06 · Cerca de un ángulo recto, el giro se pega
- **Cubre:** CA-06 (RN-rotacion-con-imanes) · prioridad alta · borde (5° del imán)
- **Pasos:** girar 86°; capturar con los dedos abajo; soltar.
- **Esperado:** con los dedos abajo, `86°`; al soltar, el lienzo queda a 90°.

### E-07 · Lejos de un ángulo recto, el giro queda donde lo dejé
- **Cubre:** CA-07 (RN-rotacion-con-imanes) · prioridad alta · borde (10° del imán)
- **Pasos:** girar 80°; soltar; empezar otro gesto sin girar para leer el indicador.
- **Esperado:** el lienzo queda a 80°; el indicador del segundo gesto dice `80°`.

### E-08 · Ajustar devuelve el lienzo entero, centrado y derecho
- **Cubre:** CA-08 (RN-ajustar-vista) · prioridad alta
- **Pasos:** desde A4, acercar hasta ~400 %, girar ~37° y arrastrar hacia una esquina; tocar
  **Ajustar vista**.
- **Esperado:** el lienzo entero a la vista, centrado, a 0°, con el mismo tamaño que al crear la obra.

### E-09 · Los gestos de vista nunca pintan
- **Cubre:** CA-09 (RN-gestos-de-vista) · prioridad alta
- **Pasos:** sobre "Capa 1" vacía: pellizcar, arrastrar y girar con dos dedos; Ajustar vista.
  En Android, además: apoyar un dedo, moverlo 100 px (empieza un trazo), apoyar el segundo y
  mover los dos; soltar.
- **Esperado:** el lienzo sin ningún píxel de tinta. En Android, el trazo del primer dedo
  tampoco queda (se cancela al llegar el segundo, QT-70).

## Casos que ningún criterio pide, buscados igual
- **E-10 · El indicador dice 359°, no -1°**: girar 1° en sentido antihorario desde 0°, con los
  dedos abajo. Esperado `359°` (screen-editor, datos extremos). Prioridad baja.
- **E-11 · Un tercer dedo no cambia nada**: con dos dedos girando, apoyar un tercero y
  moverlo. Esperado: la vista sigue a los dos primeros. Solo Android. Prioridad baja.
- **E-12 · Pintar con la vista girada cae bajo el lápiz**: girar 90° y acercar; pintar un trazo
  horizontal en la pantalla con el mouse (Windows) o un dedo (Android). Esperado: la tinta
  aparece bajo el puntero. Es el flujo "pintar con la vista movida" del diseño, sin criterio:
  si falla, es una pregunta, no un bug. En Windows, además, cubre que el trazo no quede
  corrido por la barra superior.
- **E-13 · Girar el equipo con la vista sin tocar**: en Android, rotar el emulador a
  horizontal. Esperado: el lienzo entero sigue a la vista (la vista se re-ajusta mientras no
  se haya movido). Prioridad baja.
- Descartados: recurso de otro usuario (no hay usuarios), concurrencia (un solo usuario),
  dependencia caída (no hay red).

## Cobertura
| Criterio | Escenarios | Estado |
|---|---|---|
| CA-01 | E-01 | passed en las dos (última corrida: 2026-10-28 (2), tras BUG-002) |
| CA-02 | E-02 | passed en las dos |
| CA-03 | E-03 | passed en las dos |
| CA-04 | E-04 | passed en las dos |
| CA-05 | E-05, E-10 | passed en las dos |
| CA-06 | E-06 | passed en las dos |
| CA-07 | E-07 | passed en las dos |
| CA-08 | E-08 | passed en las dos (BUG-003 verificado en la corrida (2)) |
| CA-09 | E-09 | passed en las dos |
| CA-10 | — | blocked hasta F-004 (QT-59) |
| CA-11 | — | blocked hasta F-007 (QT-59) |
| CA-12 | — | blocked hasta F-008 (QT-59) |
| CA-13 | — | **no scenario**: los niveles reducidos de QT-73 no están construidos (unidad 29) y la medición de fluidez es de la unidad 20 |

## Ejecuciones

### Corrida 2026-10-28 · agente
Android: emulador `Pixel_10_Pro_XL`; los dos dedos, por la consola del emulador
(`adb emu event send`, multitáctil con presión), porque `sendevent` lo bloquea SELinux sin
root. Windows: ventana de 1500×1000 px, dedos por `InjectTouchInput` (QT-81), mouse y
teclado sintéticos (QT-79). Datos: solo por la app (QT-78). Evidencia:
`evidence/2026-10-28-u31/android/` y `evidence/2026-10-28-u31/windows/`. Las medidas salen
de las capturas: posición de la tinta y rectángulo blanco del lienzo.

| Escenario | Android | Windows | Lo observado |
|---|---|---|---|
| E-01 | **failed**, luego passed | passed | 1.ª corrida en Android: indicador `163 %`, lienzo congelado a ≈1,2× (BUG-002). 2.ª, en app reabierta: el punto queda en (971,6; 1649,7), antes (971,6; 1649,6), y crece ×3. Windows: (741,6; 560,2) → (742,3; 560,8), crece ×3; el lienzo tapa la barra (BUG-003) |
| E-02 | passed | **no corrido** | Android: 16×16 abre a `3200 %` (512 px); pellizcando sigue en `3200 %` y del mismo tamaño. Windows: la corrida se cortó antes (ver abajo) |
| E-03 | passed | passed | Android: el primer gesto termina en `7 %` (el ajuste inicial es 54 %, ×1/8; el plan suponía llegar a 5 %), el segundo en `5 %` y el tercero sigue en `5 %`, lienzo de 122 px de ancho. Windows: `5 %` y `5 %` |
| E-04 | passed | passed | el punto se mueve 300,1 px (Android) y 300,5 px (Windows) a la derecha |
| E-05 | passed | passed | `30°` con los dedos abajo; al soltar el lienzo sigue inclinado |
| E-06 | passed | passed | `86°` con los dedos abajo; al soltar, los lados del lienzo quedan derechos (90°) |
| E-07 | passed | passed | al soltar, inclinado; un segundo gesto lee `80°` |
| E-08 | passed | passed | desde `401 % · 37°` (Android) y `408 % · 37°` (Windows), movida: Ajustar vista deja el mismo rectángulo que al crear |
| E-09 | passed | passed | ningún píxel de tinta nuevo; en Android, tampoco el trazo del primer dedo |
| E-10 | passed | passed | `359°` |
| E-11 | passed | — | el tercer dedo no mueve el lienzo |
| E-12 | passed | passed | la tinta cae bajo el puntero con la vista a 90° (Android con dedo, Windows con mouse, sin corrimiento por la barra) |
| E-13 | passed | — | horizontal: el lienzo entero se re-ajusta, centrado |

**Corrida cortada en Windows.** Al reabrir la app para E-02, `SetForegroundWindow` no la trajo
al frente porque el usuario estaba usando el navegador. Los clics, las teclas y un pellizco
fueron a esa otra ventana. Se cortó la entrada sintética, se borraron las dos capturas de esa
ventana y quedaron sin correr en Windows: US-003 E-02 y, de US-004, E-03 y el ancho de 700 px de E-06.

### Corrida 2026-10-28 (2) · agente · unidad 112, tras BUG-002 y BUG-003
Re-corrida de lo que falló o no se corrió (QT-82, QT-83), con el arreglo de los dos bugs.
Mismos medios; en Windows, antes de cada clic, tecla o toque se comprueba que la app esté al
frente y, si no, se corta sin mandar nada (QT-83): no hizo falta cortar. Evidencia:
`evidence/2026-10-28-u112/`.

| Escenario | Android | Windows | Lo observado |
|---|---|---|---|
| E-01 | passed | passed | Android: `163 %` con los dedos abajo y el lienzo dibujado a ese zoom (la tinta ×3). **BUG-002:** 30 ciclos seguidos de pellizco (10 y 25 pasos), Ajustar vista y un trazo cada 5: en todos la pantalla siguió a la vista y Ajustar la devolvió. El log sigue mostrando `BufferPool: Waiting for buffer` 6 veces, pero cada espera terminó. Windows: `76 %`, la barra con Ajustar vista y Fondo visible encima del lienzo (BUG-003) |
| E-02 | — | passed | 16×16 a 72 dpi: abre a 512 px; pellizcando, `3200 %` y el mismo tamaño |
| E-05 | — | passed | girado y desbordando, la barra sigue visible (BUG-003) |

**Observaciones (no son bugs: ningún criterio las pide)**
- **Pregunta, Windows:** si los dos dedos se apoyan y quedan quietos (o se mueven menos de un
  par de píxeles) un momento, la app nunca entra en modo gesto, aunque después se muevan. Si
  se mueven enseguida, funciona. Causa sospechada: el "mantener presionado" de Windows se queda
  con el toque. **Sin confirmar, y solo visto con toque sintético.** Queda para F-009 o para una
  pantalla táctil real.
- **Android:** con el gesto de navegación del sistema, el área de dibujo y la hoja del panel
  Fondo llegan debajo de la barra de inicio: el borde de abajo del lienzo en horizontal y el
  mensaje de error del panel quedan debajo del indicador de inicio.
- **Android:** Atrás en el editor cierra la pantalla y deja ver otra app; al volver, la obra
  sigue abierta (QT-72). La salida del editor es de F-007.
