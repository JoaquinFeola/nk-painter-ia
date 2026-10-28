# Decisiones de diseño — F-002 Pinceles y trazo

Registro append-only. Cada entrada dice de dónde salió: `usuario`, `agente, dentro del alcance`,
`investigado` (con fuente y fecha), `observado`, `propuesta` o `[TO-DEFINE]`.
La numeración sigue la serie del proyecto (QT-88 es la última de
`Docs/Architecture/architecture-decisions.md`).

Modo elegido: **propuestas** · 2026-10-28 (QT-103)

## 2026-10-28 · Preguntas derivadas de las reglas y las historias

Salieron de RN-set-base, RN-pincel-pixel-sin-suavizado, RN-pinta-en-capa-activa,
RN-toque-deja-punto, RN-trazo-recortado-al-lienzo, los ajustes y su persistencia, la presión y
sus curvas, el estabilizador, las reglas de la goma, RN-trazo-en-historial, los dos requisitos
no funcionales y QT-58 de F-001 (la composición guardada que este diseño tenía que medir).

Decisiones con costo (se preguntan siempre):
- **QT-89 · ¿Se queda la composición guardada única (QT-58) o se pasa a dos composiciones, debajo y encima de la capa activa?**
- **QT-90 · ¿Cómo se prueba la presión en Android, si el emulador la manda siempre al 100 %?**

Hechos verificables (se investigan, no se preguntan):
- **QT-91 · ¿Cómo llegan todos los puntos de un trazo rápido en cada plataforma, para que no se pierdan?**
- **QT-92 · ¿Qué parte de la demora de 50 ms se puede medir con los equipos del usuario?**

Detalles dentro del alcance (según el modo):
- **QT-93 · ¿Cómo se representa cada pincel del set y cómo deja pintura?**
- **QT-94 · ¿Cómo se aplica la opacidad dentro de un trazo?**
- **QT-95 · ¿Cómo funciona el estabilizador de 0 a 10?**
- **QT-96 · ¿Qué hacen las curvas suave, normal y firme?**
- **QT-97 · ¿Cómo borra la goma sin tocar el fondo?**
- **QT-98 · ¿Dónde se guardan los ajustes de pinceles, goma, curva y pincel activo?**
- **QT-99 · ¿Cómo se ve el pincel Píxel sin suavizado al acercar la vista?**
- **QT-100 · ¿Qué deja F-002 preparado para el historial de F-004?**
- **QT-101 · ¿Hace falta predicción de trazo (FT-26) para la meta de demora?**
- **QT-102 · En Windows, ¿el dedo pinta en F-002?**

Pregunta para discovery (se registra en `analysis.md` como Q-107): un trazo semitransparente
que se cruza consigo mismo, ¿se oscurece donde se superpone? QT-94 depende de ella.

Modo de los detalles (QT-93 a QT-102):
- **QT-103 · ¿Cómo se resuelven los detalles dentro del alcance?**

## 2026-10-28 · Respuestas

**QT-103**
- usuario · 2026-10-28: propuestas. El agente decide, marcado como suyo, y el usuario objeta.

**QT-89**
- propuesta: (1) seguir con una composición y medir en la unidad 35; (2) dos composiciones guardadas, debajo y encima de la capa activa; (3) una, aceptando la demora con muchas capas. Medido (QT-51, QT-52): recombinar 30 capas cuesta 5 a 6,5 ms por mosaico en esta PC y 72 ms en el emulador.
- usuario · 2026-10-28: (2) dos composiciones. Supera en parte a QT-58 de F-001: la composición que se muestra sigue (ningún cuadro recombina capas), y se le suman lo de abajo y lo de arriba de la capa activa.
- agente, dentro del alcance: lo de abajo y lo de arriba van a resolución completa, sin niveles reducidos; al pintar, cada mosaico tocado se recompone con tres mezclas (abajo, capa activa, arriba) en la composición que se muestra y sus niveles reducidos. Las dos solo guardan mosaicos donde hay pintura, como las capas. En el peor caso (pintura en todo el lienzo arriba y abajo de la capa activa) el costo por píxel pasa de 16 a 24 bytes (QT-62 de F-001: +4 de abajo, +4 de arriba), y la línea roja y el umbral de aviso bajan a dos tercios. La pregunta estimaba 21 bytes y un 25 % menos: la cuenta con QT-62 da 24 y un 33 % menos; se le informa al usuario. Estimación sin medir; se calibra con la unidad 20. Cambiar de capa activa (F-003) rehace las dos composiciones una vez.

**QT-90**
- propuesta: (a) el usuario corre los escenarios de presión en su Android con lápiz y el agente registra la evidencia; (b) inyectar presión en el emulador, sin verificar que la acepte; (c) solo tests automáticos, QA de Android `blocked`.
- usuario · 2026-10-28: (a) en su tableta, más los tests automáticos de la traducción de presión.

**QT-91**
- investigado: en Windows, cuando la app no procesa los mensajes de puntero tan rápido como llegan, los movimientos del lápiz se juntan en uno; `GetPointerPenInfoHistory` devuelve todos los puntos juntados en el mensaje actual, del más nuevo al más viejo, con su presión; solo sirve para el mensaje recién leído por el mismo hilo. Desde Windows 8. Fuente: https://learn.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getpointerpeninfohistory · leído 2026-10-28.
- sin verificar en su fuente en esta sesión: en Android, un `MotionEvent` de movimiento trae las muestras anteriores juntadas (`getHistorySize`, `getHistoricalX`, `getHistoricalPressure`), de la más vieja a la más nueva. La página de referencia no se pudo leer entera; el build (unidad 35) lo confirma en https://developer.android.com/reference/android/view/MotionEvent antes de usarlo.
- agente, dentro del alcance: las dos plataformas entregan al motor común todos los puntos, en orden y con su presión; el motor interpola entre puntos (QT-93), así que ningún tramo queda recto por falta de muestras.

**QT-92**
- investigado (del propio proyecto): QT-53 y QT-54 de `Docs/Architecture/architecture-decisions.md`. Sin cámara lenta, la demora de punta a punta no se mide; se mide la parte de la app en Android (`nkpainter-latency`, p95 de 20 trazos por debajo de 50 ms). En Windows no hay registro de demora (M-06 `blocked`).
- agente, dentro del alcance: la unidad 35 suma en Windows el mismo registro de la parte de la app (del mensaje de puntero al cuadro). El criterio de 50 ms de US-005 se verifica con la parte de la app en las dos plataformas; la de punta a punta queda `blocked` mientras no haya cámara lenta, y el reporte lo dice.

**QT-93**
- agente, dentro del alcance: cada pincel es una punta que se estampa a lo largo del trazo, cada 10 % de su diámetro (nunca más de 1 px), interpolando posición y presión entre puntos; aritmética entera en el código común, como el pincel redondo del esqueleto (QT-46). Puntas: redonda dura (Tinta, Rotulador, Pincel redondo), redonda suave con caída gradual (Aerógrafo, Pincel suave), redonda dura con un grano fijo en coordenadas del lienzo (Lápiz, igual en ARM y x86) y cuadrada alineada a la grilla, sin bordes suavizados (Píxel). Un toque sin moverse estampa una vez (RN-toque-deja-punto). Lo que cae fuera del lienzo no crea mosaicos y no se guarda; el trazo sigue siendo uno (RN-trazo-recortado-al-lienzo). Cuánta caída y cuánto grano: valores de fábrica que se calibran al construir.

**QT-94**
- agente, dentro del alcance, según Q-107: cada trazo lleva una máscara de cobertura en los mosaicos que toca (1 byte por píxel, solo mientras dura). Cada estampa sube la cobertura al máximo entre la que había y la suya, así que el trazo no se oscurece al cruzarse; el Aerógrafo suma la suya en vez de tomar el máximo, hasta el tope. El mosaico de la capa se calcula como el mosaico de antes del trazo más el color actual por la cobertura por la opacidad. Por eso el motor guarda el mosaico de antes del primer toque de cada trazo (sirve también a F-004, QT-100).

**QT-95**
- agente, dentro del alcance: el estabilizador sigue al punto real con un retraso: en cada muestra, el punto dibujado avanza 1 / (1 + nivel) del camino hasta el punto real (nivel 0: va igual). Al levantar, el trazo termina en el último punto real. Es entero y determinista, así que el mismo recorrido da los mismos píxeles (el criterio del temblor de US-006 se prueba repitiendo un recorrido grabado).

**QT-96**
- agente, dentro del alcance: normal usa la presión tal como llega; suave la eleva a 0,6; firme, a 1,7. Con 50 % dan 66 % y 31 %, que cumplen los criterios de US-007. Los exponentes son valores de fábrica que se calibran al construir.

**QT-97**
- agente, dentro del alcance: la goma usa la misma máscara de cobertura que el pincel elegido para ella, y en lugar de sumar color quita alfa de la capa activa (en premultiplicado, escala los cuatro canales por 1 − cobertura × opacidad). El fondo no es una capa: se dibuja debajo de la composición (QT-71 de F-001), así que la goma no tiene cómo tocarlo (RN-goma-no-borra-fondo).

**QT-98**
- agente, dentro del alcance: ajustes de cada pincel, de la goma, la curva y el pincel activo en un archivo de preferencias de la app en cada equipo, con el mismo mecanismo que los tamaños recientes (una interfaz en el código común y su archivo en el código de las dos plataformas). Se guardan al terminar cada cambio, no en cada movimiento de un deslizador. No van en el archivo de proyecto (RN-fuera-del-archivo). Si el archivo falta o no se puede leer, vuelven los valores de fábrica y se registra en el log.

**QT-99**
- agente, dentro del alcance: con zoom por encima de 100 %, la vista muestra los píxeles sin filtrar (cada píxel del lienzo como un cuadrado); por debajo, filtrado como hasta ahora. Vale para toda la obra, no solo para el Píxel, y cambia cómo F-001 dibuja la vista en las dos plataformas.

**QT-100**
- agente, dentro del alcance: cada trazo (de pincel o de goma) termina entregando la lista de mosaicos que cambió con su estado de antes (QT-94). F-002 la descarta; F-004 la guarda como un paso del historial (RN-trazo-en-historial, QT-09). Los criterios de deshacer quedan `blocked` hasta F-004.

**QT-101**
- agente, dentro del alcance: sin predicción de trazo por ahora. La parte de la app medida en el emulador es de 15 ms de promedio y p95 de 21 ms (QT-51). Se revisa con M-05 de la unidad 20: si el p95 de la tableta pasa de 50 ms, vuelve como pregunta.

**QT-102**
- agente, dentro del alcance: no. Como dijo el diseño de F-001, en Windows el dedo solo mueve la vista, y pintar con un dedo es de F-009 (regla de 0,3 s). El criterio "con el dedo" de US-007 queda `blocked` en Windows hasta F-009. El mouse sí pinta en Windows, a presión completa (RN-sin-presion).

**QT-104 · Con la cuenta corregida (24 bytes por píxel y línea roja a dos tercios, no 21 y 25 %), ¿se mantiene QT-89?**
- propuesta: mantener dos composiciones (la demora al asentar el trazo pesa más que el tamaño máximo del lienzo); o volver a una y medir en la unidad 35.
- usuario · 2026-10-28: mantener dos composiciones.

## 2026-10-28 · Al construir US-005 (unidad 35)

**QT-105 · ¿Qué espaciado de estampas toma QT-93 ("cada 10 % del diámetro, nunca más de 1 px")?**
- agente, dentro del alcance: 10 % del diámetro con un piso de 0,5 px. Leído al pie de la letra (nunca más de 1 px entre estampas), un pincel de 500 px estamparía 500 veces su área por cada 500 px de trazo; con la máscara de cobertura (QT-94) estampar más seguido no cambia el resultado, solo el costo. Se calibra con la medición de la unidad 20.

**QT-106 · ¿Cuándo se construyen las composiciones de abajo y de arriba (QT-89)?**
- propuesta: en F-003 (US-009), cuando exista una segunda capa. Con una sola capa no hay nada abajo ni arriba de la activa, y el código no tendría ningún criterio que lo ejercite. Mientras tanto la pantalla muestra los mosaicos de la capa activa como hasta ahora. Sin decisión del usuario.

**QT-107 · ¿Cómo llegan en Android las muestras juntadas (QT-91 sin verificar)?**
- investigado: "To process all coordinates in the batch in time order, first consume the historical coordinates then consume the current coordinates." Fuente: comentario de clase de `MotionEvent.java`, sección Batching, https://raw.githubusercontent.com/aosp-mirror/platform_frameworks_base/main/core/java/android/view/MotionEvent.java · leído 2026-10-28. Es lo que ya hacía el esqueleto; queda verificado en su fuente, no en un equipo.
- investigado: en Windows, `GetPointerPenInfoHistory` deja en `entriesCount` el total disponible aunque supere el búfer, y lista del más nuevo al más viejo. Fuente: https://learn.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getpointerpeninfohistory · leído 2026-10-28. Construido; sin probar con un lápiz físico.
