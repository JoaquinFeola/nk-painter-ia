# Análisis — F-008 Exportar e importar

Ya respondido en otras specs y válido acá: Q-04 (archivo `nombreObra.<extensión>`), Q-29 (no se importa una obra que supera la línea roja del equipo), Q-35 (el historial no viaja), Q-54 (reglas de nombre).

## Abiertas

(ninguna)

## Respondidas

### Q-59 · ¿Dónde queda el archivo exportado y cómo llega al otro equipo sin internet?
- bloquea: RN de destino de exportación
- respuesta: El archivo se puede compartir por donde el ilustrador quiera (WhatsApp, por ejemplo); en la PC se descarga y se importa en la app de PC. La app entrega el archivo; el viaje lo hace otra app o el ilustrador. · 2026-10-28

### Q-60 · ¿Qué pasa si se importa una obra cuyo nombre ya existe en la galería?
- bloquea: RN de conflicto de nombre al importar
- respuesta: Pregunta si quiere reemplazar la obra existente o cambiarle el nombre a la importada. · 2026-10-28

### Q-61 · ¿Qué pasa con un archivo de una versión más nueva de la app, o un archivo dañado?
- bloquea: los casos que salen mal de importar
- respuesta: Un archivo de una versión más nueva no se importa y se indica que hay que actualizar la app. Uno dañado o que no es de nk-painter no se importa y se dice por qué. La galería no cambia. Los archivos de versiones anteriores se importan siempre. · 2026-10-28

### Q-62 · ¿Cómo se exporta como PNG o JPEG: tamaño, calidad y transparencia?
- bloquea: RN de exportar imagen
- respuesta: Tamaño original por defecto, o de 25 % a 400 %. Calidad JPEG de 1 a 100, inicial 90. Con fondo transparente, el PNG lo conserva y el JPEG sale con fondo blanco. Solo las capas visibles. · 2026-10-28

### Q-63 · ¿Dónde va una imagen importada, en qué formatos, y qué pasa si es más grande que el lienzo?
- bloquea: RN de importar imagen
- respuesta: Dentro de una obra: va a una capa nueva sobre la activa, centrada y lista para transformar; si es más grande que el lienzo, se achica para entrar entera. Desde la galería: crea una obra nueva del tamaño de la imagen (con aviso y línea roja de F-001). Formatos PNG, JPEG y WebP. · 2026-10-28

### Q-64 · ¿Cuánto pueden tardar exportar e importar la obra de referencia?
- bloquea: los requisitos no funcionales
- respuesta: Exportar o importar el archivo de proyecto de la obra de referencia en menos de 10 s, y una imagen PNG en menos de 5 s, en gama media, con progreso visible y sin bloquear la app. · 2026-10-28

### Q-65 · ¿Qué viaja dentro del archivo de proyecto?
- bloquea: RN de contenido del archivo de proyecto y el objetivo "la obra viaja sin perder nada"
- respuesta: La obra entera y su vista: nombre, tamaño y dpi, fondo, todas las capas (también las ocultas) con orden, nombre, opacidad, recorte, bloqueo alfa y visibilidad, y la vista. No viajan el historial (Q-35) ni los ajustes de pinceles y colores. · 2026-10-28

### Q-66 · ¿Cómo entra a la app un archivo que llega por WhatsApp o se descarga en la PC?
- bloquea: RN de formas de importar
- respuesta: Importar eligiendo el archivo desde la app, y además "abrir con": tocar el archivo en WhatsApp o en el explorador (Android), o doble clic en Windows, abre nk-painter y lo importa directo, con las mismas reglas. · 2026-10-28

### Q-67 · ¿Confirmás los detalles que extendí en F-008?
- bloquea: RN-ida-y-vuelta-sin-perdidas, RN-obra-importada-en-galeria, RN-importar-imagen-formatos
- respuesta: Confirmo los tres: la obra exportada e importada en otro equipo se ve idéntica píxel por píxel; la obra importada queda en la galería fuera de carpetas y se abre sola; una imagen que no es PNG/JPEG/WebP o está dañada no se importa y se dice por qué. · 2026-10-28

## Notas técnicas
- La extensión del archivo de proyecto, su formato interno y su versión: paso 6. Tiene que poder viajar por WhatsApp como documento sin que lo modifiquen (sin verificar).
- "Abrir con" en Android y la asociación de la extensión en Windows dependen de cada plataforma. Paso 5.
- Compartir desde Android usa el menú del sistema: la app no necesita permiso de internet (objetivo Q-03). A verificar en el paso 5.

## Objeciones
