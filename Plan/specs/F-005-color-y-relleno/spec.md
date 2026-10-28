# F-005 · Color y relleno

Cubre FT-30, FT-31 y FT-70 del inventario (entrega 1, Q-07).

## Qué resuelve
Un ilustrador elige colores con precisión, los toma de su propia obra para mantener la
paleta, y rellena los colores base debajo del lineart de un toque, sin que el color se escape
por un hueco ni quede un halo blanco bajo la línea.

## Alcance

**Entra**
- Elegir el color actual con rueda y cuadrado, con valores RGB y HSV o con código hex.
- Los últimos 12 colores usados.
- El cuentagotas, normal y rápido.
- El balde con su referencia, tolerancia, cierre de huecos y expansión.

**Queda afuera**
- Paletas de colores guardadas: FT-32, importante (S) (Q-38).
- Balde borrador, relleno envolvente y relleno con lazo: FT-71 y FT-72, importantes (S).
- Degradados: FT-73, importante (S).
- Cambiar el color de un trazo ya hecho: se hace con bloqueo alfa (F-003).
- Que el balde respete una selección: lo define F-006 Selección y transformación.

## Reglas de negocio

### RN-un-color-actual · origin: Q-38
Hay un solo color actual, y es el que usan el pincel y el balde.

### RN-formas-de-elegir-color · origin: Q-38
El color actual se puede elegir con la rueda de tono y el cuadrado de saturación y brillo,
escribiendo sus valores RGB (0–255) o HSV, o con su código hex de 6 dígitos. Las tres formas
muestran siempre el mismo color.

### RN-hex-invalido · origin: Q-43
Si el código hex escrito no tiene 6 dígitos hexadecimales válidos, el color actual no cambia
y se indica el formato esperado (por ejemplo `#1A2B3C`).

### RN-colores-recientes · origin: Q-38, Q-43
Se recuerdan los últimos 12 colores distintos con los que se pintó o se rellenó, del más
reciente al más viejo. Elegir un color sin usarlo no lo agrega.

### RN-recientes-persisten · origin: Q-43
Los colores recientes se conservan al cerrar y reabrir la app.

### RN-cuentagotas-referencia · origin: Q-39
El cuentagotas toma por defecto el color que se ve en ese punto, combinando todas las capas
visibles y el fondo. Con la opción "solo capa activa", toma el color de la capa activa.

### RN-cuentagotas-rapido · origin: Q-39
Con el pincel activo, mantener apoyado el lápiz digital o el dedo sin moverse durante el
tiempo configurado activa el cuentagotas en ese punto, sin cambiar de herramienta.

### RN-tiempo-cuentagotas-rapido · origin: Q-39
El tiempo del cuentagotas rápido va de 0,2 a 2 segundos, y empieza en 0,5 segundos.

### RN-cuentagotas-transparente · origin: Q-42
Si el cuentagotas toca una zona totalmente transparente, el color actual no cambia.

### RN-balde-referencia · origin: Q-40
El balde decide la zona a rellenar mirando por defecto todas las capas visibles, y pinta en la
capa activa. Con la opción "solo capa activa", mira solo la capa activa.

### RN-balde-tolerancia · origin: Q-40
La tolerancia del balde va de 0 % a 100 %, y empieza en 10 %. Con 0 %, solo entran en la zona
los píxeles exactamente del mismo color que el punto tocado.

### RN-balde-cierra-huecos · origin: Q-41
El balde trata como cerrados los huecos del lineart de hasta el tamaño configurado, de 0 a 10
píxeles, que empieza en 2. Con 0 no cierra ningún hueco.

### RN-balde-expande · origin: Q-41
El relleno se expande hacia afuera de la zona encontrada la cantidad configurada, de 0 a 5
píxeles, que empieza en 1, para tapar el borde suavizado de las líneas.

### RN-balde-opaco · origin: Q-42
El balde pinta con el color actual al 100 % de opacidad.

### RN-balde-respeta-bloqueo-alfa · origin: Q-42
Si la capa activa tiene el bloqueo alfa activo, el balde solo pinta donde la capa ya tenía
pintura (RN-bloqueo-alfa, F-003).

### RN-balde-fuera-del-lienzo · origin: Q-42
Tocar con el balde fuera del lienzo no hace nada y no entra en el historial.

### RN-ajustes-de-color-persisten · origin: Q-43
La referencia del cuentagotas, el tiempo del cuentagotas rápido, y la referencia, la
tolerancia, el cierre de huecos y la expansión del balde se conservan al cerrar y reabrir la
app.

## Requisitos no funcionales
- Un relleno con el balde sobre la obra de referencia (A4 a 300 dpi, 30 capas), mirando todas
  las capas visibles, se ve terminado en menos de 1 segundo en un equipo de gama media. (Q-43)
- El color del cuentagotas rápido aparece mientras el lápiz sigue apoyado, sin esperar a
  levantarlo (Q-39).

## Historias
| Historia | Qué cubre |
|---|---|
| US-017-elegir-color | Rueda y cuadrado, RGB, HSV, hex (y hex inválido), colores recientes |
| US-018-cuentagotas | Normal y rápido, lo que se ve o la capa activa, zona transparente, tiempo ajustable |
| US-019-rellenar | Balde con referencia, tolerancia, cierre de huecos, expansión, bloqueo alfa y fuera del lienzo |

Las historias se escriben después del paso 5, al inicio del tramo de la feature (QT-03).

## Pendiente
- (ninguna pregunta abierta)
- Para arquitectura: rendimiento del balde sobre todas las capas visibles (notas técnicas de
  `analysis.md`).
