# Pruebas · US-002 Un lienzo que no se puede crear, o que conviene pensar
feature: F-001 · unidad 27 · plan aprobado 2026-10-28 (QT-77)

Mismos objetivos y medios que `US-001-crear-obra.md`: la pantalla de Android (emulador por
`adb`) y la pantalla de Windows (`:desktopApp:run`, capturas y entrada sintética).

## Cómo se observa el resultado
- **"No se crea".** La app sigue en Obra nueva, con lo escrito intacto, y la lista de
  recientes queda igual que antes del intento (se compara en pantalla, antes y después).
- **"Se crea".** Se abre el editor, y al volver a abrir la app el primer reciente es el tamaño pedido.
- **El mensaje.** El texto exacto junto al campo y en el resumen, en la captura. Los textos
  son los de `ux-ui/screen-obra-nueva.md` (Q-91).
- **Umbrales.** Son datos de prueba (Notas de la historia) y se fijan al lanzar: Android
  `adb shell am start -n com.nkpainter/.android.MainActivity --el warnPixels W --el redLinePixels R`
  (la app lee el límite una vez por proceso: se cierra con `am force-stop` antes de cambiarlo);
  Windows `gradlew :desktopApp:run -PwarnPixels=W -PredLinePixels=R`.

## Datos
Solo usando la app (QT-78). Ningún escenario necesita más datos que los umbrales. "Los
recientes no cambian" se comprueba leyendo la lista en pantalla antes y después del intento.

## Escenarios
Las entradas van en px salvo que se diga otra cosa.

### E-01 · 15×500 a 72 dpi no se crea
- **Cubre:** CA-01 (RN-tamano-minimo) · prioridad alta · técnica: borde (15)
- **Esperado:** no se crea; junto a Ancho y en el resumen: "El mínimo es 16 px por lado."

### E-02 · 16×16 a 72 dpi se crea
- **Cubre:** CA-02 · prioridad alta · borde (16)
- **Esperado:** se abre el editor; primer reciente `16×16 px · 72 dpi`.

### E-03 · 5×5 mm a 72 dpi (14×14 px) no se crea
- **Cubre:** CA-03 (RN-tamano-minimo, RN-tamano-personalizado) · prioridad media
- **Esperado:** el formulario muestra `14×14 px`; no se crea; "El mínimo es 16 px por lado."

### E-04 · 1000×1000 a 71 dpi no se crea
- **Cubre:** CA-04 (RN-dpi-fuera-de-rango) · prioridad alta · borde (71)
- **Esperado:** no se crea; "La resolución va de 72 a 600 dpi."

### E-05 · 1000×1000 a 601 dpi no se crea
- **Cubre:** CA-05 · prioridad alta · borde (601)
- **Esperado:** no se crea; "La resolución va de 72 a 600 dpi."

### E-06 · 1000×1000 a 600 dpi se crea
- **Cubre:** CA-06 · prioridad alta · borde (600)
- **Esperado:** primer reciente `1000×1000 px · 600 dpi`.
- **Extra de la misma partición:** 1000×1000 a 72 dpi, el otro extremo que nombra el título
  del criterio; esperado `1000×1000 px · 72 dpi`.

### E-07 · 72,5 dpi no se crea
- **Cubre:** CA-07 · prioridad media
- **Pasos:** 1000×1000 a `72,5`; repetir con `72.5` (el campo acepta coma y punto).
- **Esperado:** las dos veces, no se crea; "La resolución es un número entero de 72 a 600 dpi."

### E-08 · 10×10 a 700 dpi: dos errores juntos
- **Cubre:** CA-08 (RN-errores-juntos) · prioridad alta · tabla de decisión: lado mal × dpi mal
- **Esperado:** no se crea; el resumen muestra a la vez "El mínimo es 16 px por lado." y
  "La resolución va de 72 a 600 dpi."; cada campo con su error.

### E-09 · 6000×8000 avisa antes de crear
- **Cubre:** CA-09 (RN-aviso-lienzo-grande) · prioridad alta
- **Umbrales:** aviso 40 000 000, línea roja 75 000 000.
- **Pasos:** 6000×8000 a 72 dpi; Crear.
- **Esperado:** el diálogo "Este lienzo es grande para este equipo. ¿Quieres crearlo igual?"
  con Crear igual y Cancelar; detrás, la obra todavía no existe (recientes sin cambios).

### E-10 · Crear igual crea 6000×8000
- **Cubre:** CA-10 · prioridad alta
- **Pasos:** desde E-09, Crear igual; volver a abrir con los mismos umbrales.
- **Esperado:** se abre el editor; primer reciente `6000×8000 px · 72 dpi`.
- **Riesgo declarado:** 48 millones de píxeles en el emulador es memoria real; si la app se
  cierra, es un hallazgo (RNF "ningún lienzo que la app deja crear la cierra"), anotado con el
  umbral forzado como causa probable, no como bug del producto sin más.

### E-11 · Cancelar no crea nada
- **Cubre:** CA-11 · prioridad alta
- **Pasos:** desde E-09, Cancelar.
- **Esperado:** se cierra el diálogo; el formulario sigue con 6000×8000 a 72 dpi; recientes sin cambios.
- **Extra:** cerrar el diálogo tocando fuera (Android) o con Esc (Windows); esperado igual que Cancelar.

### E-12 · 12000×16000 sobre la línea roja de 75 M
- **Cubre:** CA-12 (RN-linea-roja-lienzo) · prioridad alta
- **Umbrales:** aviso 40 000 000, línea roja 75 000 000.
- **Esperado:** no se crea; "Este equipo admite hasta 7500×10000 px."; Ancho y Alto marcados.
- **Borde, de las Notas:** E-12b, 7500×10000 (exactamente 75 M) no se rechaza: avisa (supera
  40 M). E-12c, 5000×8000 (exactamente 40 M) se crea sin aviso.

### E-13 · El mismo pedido en un equipo más grande se crea sin aviso
- **Cubre:** CA-13 · prioridad media
- **Umbrales:** aviso 200 000 000, línea roja 250 000 000.
- **Pasos:** 12000×16000 a 72 dpi; Crear.
- **Esperado:** se abre el editor sin aviso; primer reciente `12000×16000 px · 72 dpi`.
- **Riesgo declarado:** 192 millones de píxeles es más de lo que el emulador y esta PC
  soportan de verdad; los umbrales forzados lo dejan pasar. Si la app se cierra, se anota como
  efecto del umbral forzado y se ofrece una pregunta, no un bug.

### Bordes que ninguna técnica genera
- **Datos extremos.** E-14: 999999×999999 a 72 dpi con los umbrales reales del equipo.
  Esperado (screen-obra-nueva, estados): se acepta en el campo y la línea roja lo rechaza
  informando el máximo cuadrado.
- **Campo vacío o con letras.** E-15: Ancho vacío y Alto `abc`. No lo pide ningún criterio:
  si falla es una pregunta, no un bug. Se espera que no se cree nada y se diga "Escribe un número."
- **Doble toque en Crear igual.** E-16: tocar dos veces Crear igual. Esperado: una sola obra,
  un solo reciente.

## Cobertura
| Criterio | Escenarios | Estado |
|---|---|---|
| US-002 CA-01 | E-01 | passed |
| US-002 CA-02 | E-02 | passed |
| US-002 CA-03 | E-03 | passed |
| US-002 CA-04 | E-04 | passed |
| US-002 CA-05 | E-05 | passed |
| US-002 CA-06 | E-06 | passed |
| US-002 CA-07 | E-07 | passed |
| US-002 CA-08 | E-08 | passed |
| US-002 CA-09 | E-09 | passed |
| US-002 CA-10 | E-10, E-16 | passed |
| US-002 CA-11 | E-11 | passed |
| US-002 CA-12 | E-12, E-14 | passed |
| US-002 CA-13 | E-13 | passed |
| sin criterio (pregunta si falla) | E-15 | passed |

## Ejecuciones

### Corrida 2026-10-28 · agente
Mismo equipo y medios que la corrida de US-001. "No se crea" se comprobó de tres formas: la app
sigue en Obra nueva, lo escrito queda intacto, y los recientes son los mismos antes y después
(Android: la lista en pantalla tras relanzar; Windows: el archivo de recientes). Los umbrales se
fijaron al lanzar; en Windows la app los imprime al arrancar
(`canvas limit: warning 40000000 px, red line 75000000 px`).

| Escenario | Android | Windows | Visto |
|---|---|---|---|
| E-01 | passed | passed | "El mínimo es 16 px por lado." junto a Ancho y en el resumen |
| E-02 | passed | passed | editor; reciente `16×16 px · 72 dpi` |
| E-03 | passed | passed | el formulario muestra `14×14 px`; mínimo en Ancho y Alto. Windows: la primera corrida no valió (el clic en mm no cayó y se probó en px, `US-002-E-03-invalid-run-px.png`); se repitió en un formulario limpio y pasó |
| E-04 | passed | passed | "La resolución va de 72 a 600 dpi." |
| E-05 | passed | passed | ídem |
| E-06 | passed | passed | recientes `1000×1000 px · 600 dpi` y `· 72 dpi` |
| E-07 | passed | passed | con `72,5` y con `72.5`: "La resolución es un número entero de 72 a 600 dpi." |
| E-08 | passed | passed | el resumen muestra los dos mensajes a la vez, y cada campo el suyo |
| E-09 | passed | passed | diálogo "Este lienzo es grande para este equipo. ¿Quieres crearlo igual?" con Cancelar y Crear igual; recientes sin cambio |
| E-10 | passed | passed | editor (proporción 0,749); reciente `6000×8000 px · 72 dpi`; la app no se cerró |
| E-11 | passed | passed | Cancelar, y además tocar fuera (Android) o Esc (Windows): formulario con 6000×8000 a 72; recientes sin cambio |
| E-12 | passed | passed | "Este equipo admite hasta 7500×10000 px." en Ancho, Alto y el resumen |
| E-12b | passed | passed | 7500×10000 (justo 75 M) avisa y no se rechaza |
| E-12c | passed | passed | 5000×8000 (justo 40 M) se crea sin aviso |
| E-13 | passed | passed | 12000×16000 con 200 M / 250 M se crea sin aviso; la app no se cerró en ninguna de las dos |
| E-14 | passed | passed | 999999×999999 con los límites reales: Android "hasta 9852×9852 px", Windows "hasta 20698×20698 px" |
| E-15 | passed | passed | "Escribe un número." en cada campo; no se crea. El resumen no repite este mensaje (ningún criterio lo pide) |
| E-16 | passed | passed | doble toque en Crear igual: un solo reciente |

En Windows, los lienzos creados en E-12c y E-13 también muestran BUG-001, que es de US-001.
Consola sin errores.
