# Análisis — F-007 Galería y autoguardado

## Abiertas

(ninguna)

## Respondidas

### Q-51 · ¿Cómo se organiza y se ordena la galería?
- bloquea: RN de carpetas y de orden de la galería
- respuesta: Miniaturas con nombre, fecha y tamaño. Carpetas de un solo nivel. Orden por última modificación (por defecto), por nombre o por fecha de creación. · 2026-10-27

### Q-52 · ¿Qué se puede hacer con una obra desde la galería, y cómo se protege el borrado?
- bloquea: RN de acciones sobre obras y RN de borrado
- respuesta: Abrir, renombrar, duplicar, mover a carpeta y borrar. Borrar pide confirmación y es definitivo: no se puede recuperar. · 2026-10-27

### Q-53 · ¿Cuándo se autoguarda, y existe además un "guardar" manual?
- bloquea: RN de autoguardado y el objetivo "nunca se pierde trabajo"
- respuesta: Se guarda después de cada paso del historial y al salir de la app o pasar a otra. No hay botón de guardar. Un indicador discreto muestra cuándo está guardando. · 2026-10-27

### Q-54 · ¿Cómo se llama una obra nueva, pueden repetirse los nombres y qué caracteres admite?
- bloquea: RN de nombre de obra (también usado como nombre del archivo exportado, Q-04)
- respuesta: Nombre inicial "Obra AAAA-MM-DD HH.MM". Dos obras no pueden llamarse igual en la misma carpeta (se ofrece agregar " (2)"). No admite \ / : * ? " < > |. Máximo 100 caracteres. · 2026-10-27

### Q-55 · ¿Qué pasa si el equipo se queda sin espacio al autoguardar, o si la app se cierra de golpe?
- bloquea: los casos que salen mal del autoguardado
- respuesta: Sin espacio: avisa que la obra no se pudo guardar, la última versión guardada queda intacta, reintenta en el próximo paso y se sigue dibujando. Cierre de golpe: al reabrir, la obra está como en el último autoguardado, sin preguntar. · 2026-10-27

### Q-56 · ¿Qué pasa al borrar una carpeta que tiene obras adentro?
- bloquea: RN de borrado de carpetas
- respuesta: Se borran con la carpeta, con una confirmación que dice cuántas obras se van a borrar para siempre. · 2026-10-27

### Q-57 · ¿Cuánto puede tardar en abrirse la obra de referencia desde la galería, y en mostrarse la galería con muchas obras?
- bloquea: los requisitos no funcionales de la galería
- respuesta: La obra de referencia se abre en menos de 3 s y una galería de 200 obras se muestra en menos de 1 s, en gama media. Autoguardar nunca frena el trazo siguiente. · 2026-10-27

### Q-58 · ¿Confirmás los detalles que extendí de Q-51 y Q-54?
- bloquea: RN-duplicar-obra, RN-carpetas-nombre, RN-nombre-caracteres (nombre vacío) y RN-orden-galeria (orden recordado)
- respuesta: Confirmo los cuatro: duplicar deja la obra en la misma carpeta con " (2)"; las carpetas siguen las mismas reglas de nombre y no se repiten en la galería; un nombre vacío no se acepta; el orden elegido se recuerda al cerrar la app. · 2026-10-27

## Notas técnicas
- Guardar después de cada paso una obra de 30 capas A4 sin frenar el trazo exige guardar solo lo que cambió y hacerlo en segundo plano, sin dejar el archivo a medio escribir si la app se cierra. Para arquitectura (paso 5).

## Objeciones
- Ofrecí una papelera de 30 días para que un borrado por error no pierda la obra, en línea con "nunca se pierde trabajo". El usuario eligió confirmación y borrado definitivo (Q-52), y también borrar las obras junto con su carpeta (Q-56). Se ejecuta así; no se reabre sin un argumento nuevo.
