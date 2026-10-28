# Análisis — nk-painter (producto)

## Abiertas

(ninguna)

## Respondidas

### Q-01 · ¿Qué producto es?
- bloquea: todo
- respuesta: Una app de dibujo digital para quien se dedica al diseño o al dibujo, con las herramientas de ibis Paint. No una copia exacta. · 2026-10-27

### Q-02 · ¿En qué plataformas?
- bloquea: el alcance y las restricciones
- respuesta: App nativa de Android y app de Windows, en Kotlin. Buscar un framework con una sola implementación para las dos. · 2026-10-27

### Q-03 · ¿Necesita internet?
- bloquea: el alcance (nube, cuentas, galería online)
- respuesta: No. 100% offline, todo se guarda localmente. · 2026-10-27

### Q-04 · ¿Cómo se pasa un dibujo de un dispositivo a otro?
- bloquea: el módulo de archivos y el objetivo de portabilidad
- respuesta: Se exporta el dibujo como un archivo `nombreDibujo.<extensión>`, que se importa en el celular o en la computadora para seguir dibujándolo. · 2026-10-27

### Q-05 · ¿Qué exigencias de rendimiento tiene?
- bloquea: los requisitos no funcionales del producto
- respuesta: Optimizada: no tiene que consumir demasiada RAM y tiene que sentirse fluida. · 2026-10-27

### Q-06 · ¿Qué features entran y en qué orden?
- bloquea: el alcance y la entrega 1
- respuesta: Todas las de ibis Paint como meta, investigadas y priorizadas antes de escribir código (`Docs/Product/research/inventario-features-ibis-paint.md`). · 2026-10-27

### Q-07 · ¿Qué entra en la entrega 1?
- bloquea: la entrega 1 y los tramos del plan
- respuesta: Las 28 features imprescindibles (M) del inventario. Las importantes (S) se encaran después, solo con aprobación explícita del usuario. · 2026-10-27

### Q-08 · ¿Para quién es la app, en primer lugar?
- bloquea: la tabla de usuarios y los objetivos de la definición de producto
- respuesta: Ilustradores profesionales y semiprofesionales. · 2026-10-27

### Q-09 · ¿En qué dispositivo tiene que sentirse fluida como mínimo?
- bloquea: cómo se mide el objetivo "fluida y liviana" (el "se sabe por")
- respuesta: Gama media actual: Android con 4 GB de RAM y PC de oficina con 8 GB. · 2026-10-27

### Q-10 · ¿Cuál es la obra más pesada que tiene que soportar sin trabarse?
- bloquea: el límite de tamaño de lienzo y de cantidad de capas, y el presupuesto de memoria
- respuesta: A4 a 300 dpi (3508×2480 px) con unas 30 capas. · 2026-10-27

### Q-11 · ¿Cómo llega la app a quienes la usan?
- bloquea: el alcance (tienda, instalador, uso personal) y las restricciones de la entrega 1
- respuesta: Instalable a mano por ahora: APK en Android e instalador en Windows. Las tiendas se deciden después. · 2026-10-27

### Q-12 · ¿Confirmás lo que propuse en la definición de producto?
- bloquea: dar por acordados los objetivos, sus mediciones y los usuarios de product.md
- respuesta: Confirmo todo: los objetivos "nunca se pierde trabajo" y "todo funciona sin conexión", cómo se mide la fluidez (son metas a medir, no medidas), el "hoy no puede" de cada usuario y dejar afuera iOS, macOS y web. · 2026-10-27

## Notas técnicas
- Framework Kotlin único para Android y Windows, y la presión del lápiz en Windows. Para arquitectura (paso 5).
- Formato del archivo de proyecto: capas, metadatos y versión. Para arquitectura (paso 6).
- Q-09 y Q-10 juntas: 30 capas A4 sin comprimir ocuparían alrededor de 1 GB (3508 × 2480 × 4 bytes ≈ 34,8 MB por capa). En un Android de 4 GB eso choca con "poca RAM". Arquitectura tiene que decidir cómo guardar las capas en memoria (por ejemplo solo las zonas pintadas, o comprimidas). No se investiga acá.

## Objeciones
