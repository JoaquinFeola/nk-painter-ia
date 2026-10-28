# Análisis — F-005 Color y relleno

## Abiertas

(ninguna)

## Respondidas

### Q-38 · ¿Cómo se elige un color, y se recuerdan los últimos usados?
- bloquea: RN del selector de color y de colores recientes
- respuesta: Rueda de tono con cuadrado de saturación y brillo, valores RGB y HSV editables, código hex y los últimos 12 colores usados al pintar. Las paletas guardadas (FT-32) quedan para después. · 2026-10-27

### Q-39 · ¿De dónde toma el color el cuentagotas, y cómo se activa el cuentagotas rápido?
- bloquea: RN del cuentagotas
- respuesta: Por defecto toma el color que se ve (todas las capas visibles y el fondo); se puede cambiar a "solo capa activa". Mantener apoyado el lápiz o el dedo sin moverse 0,5 s con el pincel activa el cuentagotas rápido; el tiempo se ajusta de 0,2 a 2 s. · 2026-10-27

### Q-40 · ¿Qué mira el balde para decidir la zona a pintar, y cómo se regula la tolerancia?
- bloquea: RN de referencia y tolerancia del balde
- respuesta: Por defecto el balde busca la zona mirando todas las capas visibles, pero pinta en la capa activa; se puede cambiar a "solo capa activa". Tolerancia de 0 a 100 %, inicial 10 %. · 2026-10-27

### Q-41 · ¿Qué hace el balde con los huecos chicos del lineart y con el borde suavizado de las líneas?
- bloquea: RN de cierre de huecos y de expansión del relleno
- respuesta: Cierra huecos del lineart de hasta N píxeles (ajustable de 0 a 10, inicial 2) y expande el relleno de 0 a 5 píxeles (inicial 1) para que no quede halo bajo el borde suavizado. · 2026-10-27

### Q-42 · ¿Confirmás lo que propuse para casos borde de F-005?
- bloquea: RN-balde-opaco, RN-cuentagotas-transparente, RN-balde-fuera-del-lienzo
- respuesta: Confirmo los cuatro: el balde pinta con el color actual al 100 % de opacidad; si el cuentagotas toca una zona totalmente transparente, el color actual no cambia; tocar con el balde fuera del lienzo no hace nada; el balde respeta el bloqueo alfa de la capa activa. · 2026-10-27

### Q-43 · ¿Confirmás los detalles que agregué en F-005?
- bloquea: RN-hex-invalido, RN-colores-recientes, RN-recientes-persisten, RN-ajustes-de-color-persisten y el requisito de demora del balde
- respuesta: Confirmo los cuatro: un hex inválido no cambia el color y muestra el formato esperado; elegir un color sin pintar con él no lo agrega a los recientes; los recientes y los ajustes del cuentagotas y del balde se conservan al cerrar la app; un relleno con el balde sobre la obra de referencia termina en menos de 1 segundo en gama media. · 2026-10-27

## Notas técnicas
- Un balde que mira todas las capas visibles de la obra de referencia y cierra huecos tiene que responder rápido en un Android de 4 GB. Para arquitectura (paso 5).

## Objeciones
