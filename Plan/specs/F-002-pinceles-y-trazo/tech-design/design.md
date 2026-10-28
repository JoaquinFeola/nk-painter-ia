# Diseño — F-002 Pinceles y trazo

Cómo se resuelve F-002 dentro del componente nk-painter (`./`), sobre el motor de mosaicos del
esqueleto y la vista de F-001. Las preguntas y de dónde salió cada respuesta: `decisions.md`
(QT-89 a QT-104).

## Flujos

Dibujar un trazo (US-005, US-006, US-007):
1. La entrada de cada plataforma junta **todos** los puntos del trazo, con su presión y el tipo
   de puntero: en Android, las muestras juntadas de cada evento de movimiento; en Windows, el
   historial de puntos del mensaje de lápiz (QT-91). El mouse llega por Compose. En Windows el
   dedo sigue moviendo solo la vista (QT-102).
2. Cada punto se lleva al lienzo con la inversa de la vista (como en F-001, QT-70). Si el
   puntero no informa presión, vale 1 (RN-sin-presion).
3. El código común aplica la curva de presión elegida (QT-96) y, según lo que el pincel activo
   cambie con la presión, saca el grosor y la opacidad del punto, con mínimo de 1 px y 1 %
   (RN-presion-proporcional).
4. El estabilizador del pincel mueve el punto dibujado hacia el real (QT-95).
5. El motor estampa la punta del pincel entre el punto anterior y el actual (QT-93), sobre una
   máscara de cobertura del trazo (QT-94). Antes de tocar un mosaico por primera vez en el
   trazo, guarda cómo estaba. Lo que cae fuera del lienzo se descarta.
6. Cada mosaico tocado de la capa activa se recalcula: mosaico de antes + color actual ×
   cobertura × opacidad. Después se recompone en la composición que se muestra con lo de abajo
   y lo de arriba de la capa activa (QT-89), y se actualizan sus niveles reducidos.
7. Mientras tanto, el trazo húmedo se ve en el front buffer en Android y en el lienzo de
   Compose en Windows, como en el esqueleto. La pantalla solo muestra: no mezcla píxeles
   (QT-46).
8. Al levantar, el trazo termina en el último punto real siguiendo la curva que traía el lápiz (QT-95), se libera la máscara y el trazo
   entrega la lista de mosaicos cambiados con su estado de antes (QT-100). Hasta F-004 se
   descarta.

Un toque sin mover: los pasos 1 a 8 con un solo punto, que se estampa una vez
(RN-toque-deja-punto).

Borrar (US-008): el mismo flujo con la goma. La punta es la del pincel elegido para la goma,
con los ajustes de la goma; la presión cambia lo mismo que en ese pincel
(RN-goma-presion-como-su-pincel). En el paso 6, en lugar de sumar color, se quita alfa de la
capa activa (QT-97). El fondo no es una capa y no se toca.

Elegir pincel y ajustarlo (US-005, US-006):
1. Elegir un pincel lo deja como el único activo. Los ajustes pedidos se recortan a su rango en
   el código común y la UI muestra el valor que quedó (RN-ajuste-fuera-de-rango).
2. Al terminar cada cambio, el repositorio de ajustes guarda en el archivo de preferencias del
   equipo los ajustes de los 7 pinceles, la goma, la curva y el pincel activo (QT-98).
3. Al abrir la app, se leen. Si no existen o no se pueden leer, valen los de fábrica.

## Dónde se cumple cada regla
| Regla | Se cumple en |
|---|---|
| RN-set-base | Lista de pinceles del código común, con su punta y sus valores de fábrica |
| RN-pincel-pixel-sin-suavizado | Punta cuadrada del Píxel, alineada a la grilla; la vista sin filtrar sobre 100 % (QT-99) |
| RN-un-pincel-activo | Estado de dibujo del código común: un pincel activo |
| RN-pincel-inicial | Repositorio de ajustes: Lápiz si no hay archivo; si no, el guardado |
| RN-toque-deja-punto | Motor: un trazo de un punto se estampa una vez |
| RN-trazo-recortado-al-lienzo | Motor: las estampas fuera del lienzo no crean mosaicos; el trazo sigue siendo uno |
| RN-pinta-en-capa-activa | Motor: pinta solo en los mosaicos de la capa activa, con el color actual |
| RN-trazo-no-se-suma-a-si-mismo | Máscara de cobertura del trazo: máximo en todos, suma en el Aerógrafo (QT-94) |
| RN-rango-grosor, RN-rango-opacidad | Ajustes del pincel en el código común, que recortan |
| RN-ajuste-fuera-de-rango | Los mismos ajustes; la UI muestra el valor que devuelven |
| RN-ajustes-no-cambian-lo-pintado | El trazo toma los ajustes al empezar; lo pintado son píxeles, no trazos guardados |
| RN-ajustes-por-pincel | Ajustes por pincel en el estado de dibujo |
| RN-ajustes-persisten, RN-goma-ajustes-persisten, RN-curva-persiste | Repositorio de ajustes, en el archivo de preferencias de cada equipo (QT-98) |
| RN-presion-segun-pincel | Cada pincel declara qué le cambia la presión: grosor, opacidad, ambos o nada |
| RN-curva-presion-unica, RN-curva-tres-opciones | Una curva en el estado de dibujo, aplicada a pinceles y goma (QT-96) |
| RN-presion-proporcional | Traducción de presión del código común, con mínimo 1 px y 1 % |
| RN-sin-presion | Entrada de cada plataforma: presión 1 para dedo, mouse o lápiz digital sin presión |
| RN-estabilizador-por-pincel | Estabilizador del código común, con el nivel del pincel (QT-95) |
| RN-goma-usa-forma-de-pincel, RN-goma-forma-inicial | Ajustes de la goma: forma (Pincel redondo de fábrica), grosor, opacidad, estabilizador |
| RN-goma-presion-como-su-pincel | La goma usa lo que declara el pincel de su forma |
| RN-goma-solo-capa-activa | Motor: borra solo en la capa activa |
| RN-goma-no-borra-fondo | El fondo se dibuja debajo de la composición y no es una capa (QT-71 de F-001) |
| RN-trazo-en-historial | El trazo entrega sus mosaicos de antes (QT-100); el historial es de F-004 |

## Datos
- **Pincel** (`Brush`): nombre, punta, qué le cambia la presión, y sus valores de fábrica. Los 7
  son fijos en el código (FT-16 a FT-22 quedan afuera).
- **Ajustes de pincel**: grosor (1 a 500 px), opacidad (1 a 100 %), estabilizador (0 a 10). Uno
  por pincel y uno para la goma, que además tiene la forma.
- **Estado de dibujo**: pincel activo, ajustes, curva de presión y color actual (#000000 hasta
  F-005, Q-97). Vive en la app, no en la obra: no va en el archivo de proyecto
  (RN-fuera-del-archivo).
- **Máscara de cobertura**: 1 byte por píxel en los mosaicos que toca el trazo. Solo existe
  mientras dura el trazo.
- **Mosaicos de antes**: la copia de cada mosaico tocado, tomada al primer toque del trazo. Se
  entrega al terminar (QT-100).
- **Composiciones de abajo y de arriba** de la capa activa: derivadas, por mosaico y solo donde
  hay pintura, a resolución completa. No se guardan en disco. Suben el costo por píxel de 16 a
  24 bytes en el peor caso: el límite del equipo de F-001 se ajusta (QT-89).
- **Archivo de preferencias**: ajustes de pinceles y goma, curva y pincel activo, por equipo.

## Cuando un paso falla
- Un trazo tiene un solo efecto, los mosaicos de la capa activa; recomponer es derivado. Si la
  app se cierra en medio de un trazo, la obra se pierde igual hasta F-007 (QT-72 de F-001).
- Si guardar las preferencias falla, los ajustes siguen en la sesión y se pierden al cerrar; se
  registra en el log y no se avisa, igual que los tamaños recientes. Si no se pueden leer, valen
  los de fábrica.
- Si llega un punto con presión fuera de 0 a 1, se recorta a ese rango (QT-07 de
  `Docs/Architecture/architecture-decisions.md`: la de Android puede venir mayor que 1).
- Si se cancela un trazo (Android cancela el del dedo cuando entra un segundo dedo, QT-70), los
  mosaicos tocados vuelven a como estaban con su copia de antes, y no queda nada del trazo.

## Integraciones
Ninguna con sistemas externos. Usa la entrada del lápiz de cada plataforma, ya prevista en
`Docs/Architecture/architecture.md`, y la interfaz de preferencias de los tamaños recientes.

## Qué cambia de F-001
- La composición guardada suma lo de abajo y lo de arriba de la capa activa (QT-89).
- El límite del equipo usa 24 bytes por píxel en lugar de 16: la línea roja y el aviso bajan a
  dos tercios (QT-89). Los criterios de US-002 usan valores fijados por QA y no cambian.
- La vista muestra los píxeles sin filtrar sobre 100 % de zoom (QT-99).

## Cómo se verifica lo que no se ve en un test
- Presión en Android: el usuario corre esos escenarios en su tableta con lápiz y el agente
  registra la evidencia (QT-90). La traducción de presión tiene tests del código común.
- Demora: la parte de la app, en las dos plataformas (Windows suma el registro). La de punta a
  punta queda `blocked` sin cámara lenta (QT-92).
- Estabilizador: un recorrido tembloroso grabado que se repite con nivel 0 y 10 (QT-95).

## Qué queda para otras features
- Deshacer un trazo o un borrado: F-004 (QT-100). Pintar en otra capa que la primera: F-003.
  Pintar con un dedo en Windows: F-009 (QT-102). Elegir color: F-005.

## Preguntas abiertas
Ninguna. Las muestras juntadas de Android quedaron verificadas en su fuente (QT-107). Sin
verificar, con su prueba asignada: el costo real de las dos composiciones (QT-89, unidad 20) y la
demora de la tableta (QT-101, unidad 20). Propuesta sin decisión: construir las dos composiciones
con la segunda capa, en F-003 (QT-106).
