# Pruebas · US-004 Cambiar el fondo
feature: F-001 · unidad 31 · plan aprobado 2026-10-28 (QT-81)

Mismos medios que `US-003-navegar-vista.md`, sin dedos: Android por `adb` (toques, texto,
capturas) y Windows con mouse y teclado sintéticos (QT-79). Datos solo usando la app (QT-78);
sin efectos materiales.

## Cómo se observa el resultado
- **El fondo**: el color del lienzo en la captura, leído en un píxel sin tinta (±2 por canal
  por la compresión del color de pantalla); transparente es el damero claro.
- **El trazo sigue igual**: el mismo trazo en el mismo lugar, comparando las capturas antes y
  después en la zona del trazo.
- **Fondo activo**: el botón Fondo con relleno verde oscuro y texto en acento mientras el panel está abierto.

## Escenarios

### E-01 · De blanco a #1E2A3A, con un trazo que no cambia
- **Cubre:** CA-01 (RN-fondo-cambiable) · prioridad alta
- **Datos:** A4 a 300 dpi, fondo blanco; un trazo pintado.
- **Pasos:** Fondo → Color → escribir `#1E2A3A`.
- **Esperado:** el fondo pasa a #1E2A3A en el momento, sin confirmar; el trazo sigue igual.

### E-02 · De blanco a transparente
- **Cubre:** CA-02 · prioridad alta
- **Pasos:** Fondo → Transparente.
- **Esperado:** el lienzo muestra el damero detrás del trazo.

### E-03 · De transparente a #F5E6C8
- **Cubre:** CA-03 · prioridad alta
- **Datos:** obra creada con fondo Transparente.
- **Pasos:** Fondo (el panel abre con Transparente elegido) → Color → `#F5E6C8`.
- **Esperado:** el fondo es #F5E6C8.

### E-04 · Elegir el mismo fondo no cambia nada en pantalla
- **Cubre:** CA-06, la parte visible (el paso en el historial es de F-004) · prioridad media
- **Pasos:** con fondo blanco, Fondo → Blanco.
- **Esperado:** la captura del lienzo es igual a la de antes.

### E-05 · Un color mal escrito no cambia el fondo y dice por qué
- **Cubre:** el panel de `screen-editor.md` (error de color, Q-92); sin criterio propio:
  si falla, es una pregunta · prioridad media · partición inválida
- **Pasos:** Fondo → Color → `#12345G`.
- **Esperado:** junto al campo, "Escribe un color como #F5E6C8."; el fondo sigue como estaba.
  Mientras se escribe `#123`, sin mensaje (propuesta del agente, unidad 30).

### E-06 · El panel se abre, se marca y se cierra
- **Cubre:** el panel de `screen-editor.md` (herramienta activa; se cierra con Fondo o tocando fuera) · prioridad media
- **Pasos:** tocar Fondo; tocar Fondo otra vez; abrir y tocar el lienzo fuera del panel.
- **Esperado:** abierto, Fondo marcado; las dos formas lo cierran; tocar fuera no pinta.
- **Ancho:** en el teléfono vertical (menos de 600 dp) el panel sale desde abajo y deja ver
  la parte de arriba del lienzo; en Windows a 1200 px flota junto a la barra, y cabe en una
  ventana de 700 px.

## Cobertura
| Criterio | Escenarios | Estado |
|---|---|---|
| CA-01 | E-01 | passed en las dos |
| CA-02 | E-02 | passed en las dos |
| CA-03 | E-03 | passed en las dos |
| CA-04 | — | blocked hasta F-004 (QT-59) |
| CA-05 | — | blocked hasta F-004 (QT-59) |
| CA-06 | E-04 (solo la parte visible) | passed en las dos; el paso en el historial, blocked hasta F-004 |
| CA-07 | — | blocked hasta F-007 (QT-59) |

## Ejecuciones

### Corrida 2026-10-28 · agente
Mismos medios que la corrida de US-003. Evidencia: `evidence/2026-10-28-u31/android/US-004-*`
y `evidence/2026-10-28-u31/windows/US-004-*`.

| Escenario | Android | Windows | Lo observado |
|---|---|---|---|
| E-01 | passed | passed | fondo (30, 42, 58) = #1E2A3A al terminar de escribir, sin confirmar; el trazo, en el mismo rectángulo antes y después |
| E-02 | passed | passed | damero: (255,255,255) y (217,217,217) alternados |
| E-03 | passed | **no corrido** | Android: el panel abre con Transparente elegido; `#F5E6C8` da (245, 230, 200). Windows: la corrida se cortó (ver US-003) |
| E-04 | passed | passed | Blanco sobre blanco: las dos capturas del lienzo son iguales píxel a píxel |
| E-05 | passed | passed | `#123`: sin mensaje y el fondo igual; `#12345G`: "Escribe un color como #F5E6C8." y el fondo igual. En Windows, el primer intento escribió `#1E2345G` por un error del script (el fondo pasó a #1E2345, que es válido); se repitió con la entrada del plan |
| E-06 | passed | passed (sin la ventana de 700 px) | Fondo marcado con relleno verde oscuro y texto en acento; se cierra con Fondo y tocando fuera, sin pintar. Android: hoja desde abajo, el lienzo de arriba visible. Windows a 1500 px: flota junto a la barra. **La ventana de 700 px quedó sin correr** |

### Corrida 2026-10-28 (2) · agente · unidad 112
Lo que quedó sin correr en Windows (QT-83), con control de foco. Evidencia:
`evidence/2026-10-28-u112/windows/US-004-*`.

| Escenario | Android | Windows | Lo observado |
|---|---|---|---|
| E-03 | — | passed | el panel abre con Transparente elegido; Color → `#F5E6C8` da (245, 230, 200) en el lienzo |
| E-06 | — | passed | ventana de 700 px: el panel sale desde abajo y cabe; Fondo lo cierra; abierto, un clic en el lienzo de arriba lo cierra sin pintar (0 píxeles de tinta). Un primer clic "fuera" cayó dentro del panel y no se contó |
