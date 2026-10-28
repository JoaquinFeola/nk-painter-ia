# Análisis — F-009 Entrada

Ya respondido en otras specs y válido acá: gestos de vista con dos dedos (F-001, Q-16, Q-33), cuentagotas rápido al mantener apoyado (F-005, Q-39).

## Abiertas

(ninguna)

## Respondidas

### Q-68 · ¿Cómo funciona el rechazo de palma?
- bloquea: RN de rechazo de palma
- respuesta: Automático: apenas la app detecta un lápiz digital, los dedos y la palma dejan de pintar y solo hacen gestos. Se puede apagar en ajustes. Sin lápiz, el dedo pinta. · 2026-10-28

### Q-69 · ¿Qué gestos con los dedos hay, y se pueden cambiar?
- bloquea: RN de gestos
- respuesta: Set fijo: tocar con 2 dedos deshace y con 3 dedos rehace; pellizcar, arrastrar y girar con 2 dedos mueven la vista; mantener apoyado activa el cuentagotas. No se cambian en la entrega 1. · 2026-10-28

### Q-70 · ¿Qué atajos de teclado hay en Windows, y se pueden cambiar?
- bloquea: RN de atajos
- respuesta: Set fijo tipo Photoshop: Ctrl+Z / Ctrl+Y deshacer y rehacer; B pincel, E goma, G balde, I cuentagotas, M selección, L lazo, Ctrl+T transformar; Ctrl+C/X/V; Ctrl+D deseleccionar; [ y ] grosor; Espacio+arrastrar mover; Ctrl+0 ajustar vista; Ctrl+Shift+N capa nueva; Ctrl+E combinar. No se cambian en la entrega 1. · 2026-10-28

### Q-71 · En Windows, ¿qué hace la rueda del mouse, y la pantalla táctil usa los mismos gestos que Android?
- bloquea: RN de mouse y pantalla táctil en Windows
- respuesta: La rueda acerca y aleja centrado en el cursor, y Ctrl+rueda también. En PCs con pantalla táctil, los gestos y el rechazo de palma son los mismos que en Android. · 2026-10-28

### Q-72 · Si mientras se dibuja con un dedo apoya un segundo dedo, ¿qué pasa con el trazo empezado?
- bloquea: el caso que sale mal entre trazo y gesto
- respuesta: Si el segundo dedo llega en los primeros 0,3 s del trazo, el trazo se descarta sin dejar marca ni entrar en el historial, y se hace el gesto. Pasado ese tiempo, el trazo se termina como está y el gesto empieza. · 2026-10-28

### Q-73 · Mientras se escribe en un campo de texto, ¿los atajos actúan sobre el texto o sobre la obra?
- bloquea: el caso borde de atajos mientras se escribe
- respuesta: Sobre el texto: las teclas escriben y Ctrl+Z deshace el texto; los atajos vuelven al salir del campo. Además, Ctrl+Shift+Z también rehace. · 2026-10-28

### Q-74 · ¿Confirmás los dos detalles que extendí en F-009?
- bloquea: RN-atajo-sin-efecto y el requisito de demora de los gestos
- respuesta: Confirmo los dos: un atajo cuya acción no se puede hacer sigue la regla de esa acción; el gesto de deshacer o rehacer responde en menos de 100 ms, como deshacer en F-004. · 2026-10-28

## Notas técnicas
- Distinguir lápiz digital, dedo y palma, y leer la presión de una tableta gráfica en Windows, depende de lo que expone cada plataforma y el framework. Paso 5 (riesgo ya anotado en el plan).

## Objeciones
