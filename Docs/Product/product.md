# Producto — nk-painter

App de dibujo digital para ilustradores, con las herramientas de ibis Paint como meta
(Q-01, Q-06). Funciona en Android y Windows (Q-02), 100% sin internet (Q-03).

## Objetivos

- **Un ilustrador termina una obra completa sin salir de la app.** Hoy las
  apps móviles gratuitas dependen de cuentas, nube o publicidad, y las de escritorio no
  corren en el celular. Se sabe por: una ilustración A4 se boceta, se entinta, se colorea en
  capas y se exporta como PNG sin usar otra app. (Q-01, Q-08)
- **La obra viaja entre el celular y la PC sin perder nada.** Hoy pasar una obra con capas
  entre dispositivos exige la nube del fabricante. Se sabe por: un archivo exportado en
  Android se abre en Windows (y al revés) con las mismas capas, el mismo orden, opacidad y
  recortes, y se puede seguir dibujando. (Q-04)
- **Se siente fluida y liviana en un equipo de gama media.** Se sabe por *(metas sin medir todavía, Q-12)*: el trazo sigue al lápiz sin demora que el
  ilustrador note; mover, hacer zoom y girar el lienzo se ve sin saltos; una obra A4 a
  300 dpi con 30 capas se abre y se edita en un Android de 4 GB y en una PC de 8 GB sin que
  el sistema cierre la app. (Q-05, Q-09, Q-10)
- **Nunca se pierde trabajo.** (Q-12) Se sabe por: si la app se cierra de golpe, al
  reabrirla la obra está como estaba, salvo como mucho el último trazo. (Q-03)
- **Todo funciona sin conexión.** Se sabe por: con el dispositivo en modo avión, todas las
  funciones andan, y la app no pide permiso de acceso a internet. (Q-03)

## Usuarios

| Quién | Qué necesita | Qué no puede hacer hoy |
|---|---|---|
| Ilustrador pro o semi-pro, en tablet o celular Android con lápiz (Q-08) | Dibujar con presión del lápiz, capas y sin distracciones, en cualquier lugar | Llevar la obra con capas a la PC sin pasar por la nube de un fabricante |
| El mismo ilustrador en Windows, con tableta gráfica o lápiz (Q-02, Q-08) | Seguir la obra en pantalla grande, con atajos de teclado | Abrir en el escritorio la obra móvil con sus capas, en una app con las mismas herramientas |

## Módulos

- **Lienzo y vista**: crear la obra, moverse por ella, cambiar el fondo.
- **Pinceles**: dibujar y borrar con presión, estabilizador y un set base de pinceles.
- **Color**: elegir colores y tomarlos del lienzo.
- **Capas**: organizar la obra en capas, con opacidad, recorte y bloqueo alfa.
- **Selección y transformación**: delimitar una zona, copiarla o moverla, escalarla, rotarla.
- **Relleno**: pintar zonas cerradas de un toque.
- **Historial**: deshacer y rehacer.
- **Galería y archivos**: la colección local de obras, el autoguardado, el archivo de proyecto
  portable, la exportación a imagen y la importación.
- **Entrada**: gestos, atajos de teclado y rechazo de palma.

Más adelante, fuera de la entrega 1: reglas y simetría, filtros, texto, manga, animación.

## Restricciones

- Android y Windows, escrita en Kotlin, con una sola implementación compartida si hay un
  framework que lo permita (Q-02). La elección la verifica arquitectura.
- Sin internet: nada se descarga, se sube ni se sincroniza (Q-03).
- Guardado solo local, en el dispositivo (Q-03).
- Liviana en memoria y fluida en gama media: Android de 4 GB y PC de 8 GB (Q-05, Q-09).
- Obra máxima de referencia: A4 a 300 dpi (3508×2480 px) con unas 30 capas (Q-10).
- Por ahora se instala a mano: APK en Android e instalador en Windows (Q-11).
- No usa el nombre, el logo ni los recursos de ibis Paint (pinceles, materiales, fuentes).
  Toma sus funcionalidades como referencia, no su marca (Q-01).

## Entrega 1

- **Para:** que un ilustrador termine una obra y la lleve entre el celular y la PC, de forma
  fluida, sin internet y sin perder trabajo. Son todos los objetivos.
- **Entra:** las 28 features imprescindibles (M) del inventario
  (`Docs/Product/research/inventario-features-ibis-paint.md`): FT-01, 02, 05, 10–14, 30, 31,
  40–44, 60, 63, 64, 70, 110–114, 118, 130–132 (Q-07).
- **Para aprender:** nada que obligue a esperar. Las features importantes (S) empiezan solo
  con aprobación explícita del usuario después de esta entrega (Q-07).
- **Se sabe por:** una obra A4 con capas, empezada en Android, se termina en Windows y se
  exporta como PNG, con los "se sabe por" de fluidez y de no perder trabajo cumplidos en
  equipos de gama media.

## Alcance

**Entra**
- Las features imprescindibles en Android y Windows, con la misma obra abriéndose en los dos.
- El archivo de proyecto propio, con capas, para exportar e importar entre dispositivos (Q-04).
- Exportar como PNG y JPEG, e importar una imagen al lienzo.

**Queda afuera**
- Nube, sincronización, cuentas, galería online y publicar obras: todo eso requiere internet
  (Q-03). La portabilidad la da el archivo de proyecto (Q-04).
- Funciones de IA con créditos, membresías, publicidad y compras: son el modelo de negocio de
  ibis Paint, no herramientas de dibujo (inventario, grupo 13).
- Capas 3D, video con audio y filtros animados: quedan en "fuera de alcance" del inventario.
- Play Store y Microsoft Store: no por ahora (Q-11).
- Las features importantes (S) y deseables (C) no entran en la entrega 1. Quedan en el
  inventario y esperan aprobación (Q-07).
- Otras plataformas (iOS, macOS, web): no se pidieron (Q-02, Q-12).

## Pendiente
- (ninguna pregunta abierta)
- Las prioridades S y C del inventario son una propuesta: se confirman cuando se planifique
  la entrega siguiente.
