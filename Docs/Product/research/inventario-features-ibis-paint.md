# Inventario de features de ibis Paint — priorizado

> Investigación de referencia para la definición de producto (paso 2). Describe qué hace
> ibis Paint, **no** qué va a hacer nk-painter: la prioridad de cada fila es una
> **propuesta** hasta que el usuario la confirme en la definición de producto.
> Leído el 2026-10-27.

## Fuentes

| # | Fuente | Rango | Qué aporta |
|---|---|---|---|
| S1 | [App Store — ibis Paint X](https://apps.apple.com/app/450722833) | oficial (ficha del desarrollador) | Cifras: +130.000 pinceles, +30.000 materiales, +3.000 fuentes, 46 tramas, 86 filtros, 27 modos de fusión; vector, ventana de referencia, reglas, animación, timelapse, novedades recientes |
| S2 | [ibispaint.com — índice de lecciones / Configuración](https://ibispaint.com/lecture/index.jsp?no=81) | oficial (manual) | Índice de 201 lecciones: lista completa de herramientas, reglas, filtros y funciones; detalle de presión, lápiz, rechazo de palma |
| S3 | [ibispaint.com — Modos de fusión](https://ibispaint.com/lecture/index.jsp?no=83) | oficial (manual) | Lista de los 28 modos de fusión con su nombre |
| S4 | [ibispaint.com — Parámetros de pincel](https://ibispaint.com/lecture/index.jsp?no=118) | oficial (manual) | Todos los parámetros del motor de pinceles y sus 4 tipos |

- **Discrepancia:** S1 dice 27 modos de fusión y S3 lista 28. Probablemente S3 cuenta
  también *Normal*. No cambia ninguna decisión.
- **Discrepancia:** fuentes no oficiales citan 47.000 o 105.000 pinceles. Tomo la cifra de S1
  porque es la vigente. Tampoco cambia nada: nk-painter no va a competir en cantidad de
  pinceles prearmados, sino en un motor de pinceles configurable.
- **Sin verificar:** si las funciones "Smart" e IA (quitar fondo, acuarela inteligente,
  ampliar con IA, AI Disturbance) se procesan en servidor. Las marco fuera de alcance porque
  se pagan con créditos y dependen de una cuenta online, algo que S1 sí documenta.

## Criterio de prioridad (propuesto)

| Nivel | Significa | Por qué entra ahí |
|---|---|---|
| **M — Imprescindible** | Sin esto no se puede terminar una ilustración | Es la base que necesita cualquier persona que dibuja digitalmente |
| **S — Importante** | Un ilustrador profesional lo extraña la primera semana | Diferencia una app de "garabatos" de una herramienta de trabajo |
| **C — Deseable** | Suma, pero hay forma de trabajar sin esto | Se puede agregar sin rehacer lo anterior |
| **W — Fuera de alcance** | No entra en este producto | Requiere internet, es modelo de negocio de ibis o es un producto aparte |

También se ordena **por dependencia**: los filtros, la selección y la animación se apoyan en
las capas, y todo se apoya en el lienzo y el pincel.

## Inventario

### 1. Lienzo y vista
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-01 | Crear lienzo con tamaño y resolución (dpi) | Tamaños prearmados y personalizados (S2 #2, #67) | **M** |
| FT-02 | Zoom, desplazamiento y rotación del lienzo con gestos | Pellizcar, girar, dos dedos (S2 #194, #195) | **M** |
| FT-03 | Espejar la vista horizontalmente | Para revisar proporciones (S2 #9) | **S** |
| FT-04 | Cambiar el tamaño del lienzo, recortar y redimensionar la imagen | (S2 #64, #65, #68) | **S** |
| FT-05 | Color de fondo del lienzo / fondo transparente | (S2 #184, #57) | **M** |
| FT-06 | Cuadrícula sobre el lienzo | (S2 #54) | **C** |
| FT-07 | Papeles de lienzo (textura de papel de fondo) | (S2 #53) | **C** |
| FT-08 | Modo de color del lienzo (RGB / escala de grises / CMYK) | (S2 #69, #56) | **C** |
| FT-09 | Ventana de imagen de referencia | Imagen flotante para copiar (S1, S2 #37) | **S** |

### 2. Pinceles y motor de pinceles
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-10 | Pincel con grosor y opacidad | (S4 Básico) | **M** |
| FT-11 | Sensibilidad a la presión del lápiz, con curva ajustable | Presión → grosor y opacidad; curva editable (S2 Configuración, S4 Dinámica) | **M** |
| FT-12 | Estabilizador de trazo | Suaviza el temblor de la mano (S2 #39) | **M** |
| FT-13 | Set base de pinceles: lápiz, tinta/pluma, aerógrafo, marcador, acuarela, difuminado | Tipos de S1 | **M** |
| FT-14 | Goma de borrar (usa cualquier pincel como goma) | (S2 #7) | **M** |
| FT-15 | Difuminar (smudge) y desenfocar con pincel | (S4 Textura) | **S** |
| FT-16 | Parámetros de punta: patrón, espaciado, ángulo, rotación según el trazo, aspecto, antialias | (S4 Forma) | **S** |
| FT-17 | Inicio/fin del trazo: grosor, opacidad y desvanecido | (S4 Desvanecido) | **S** |
| FT-18 | Variación aleatoria: posición, grosor, opacidad, ángulo, dispersión de partículas | (S4 Jitter) | **C** |
| FT-19 | Tipos de pincel: Mono, Agua (mezcla de color), Doble (contorno/sombra), Color | (S4 Tipo) | **S** Mono y Agua · **C** Doble y Color |
| FT-20 | Textura del pincel (escala, ángulo, inversión) | (S4 Textura) | **C** |
| FT-21 | Dinámica por velocidad del trazo | (S4 Dinámica) | **C** |
| FT-22 | Pinceles personalizados: crear, editar patrón, guardar | (S2 #31, #32, #51) | **S** |
| FT-23 | Exportar/importar pinceles como archivo | (S2 #33, #35) | **C** |
| FT-24 | Modo de fusión propio del pincel | (S4 Configuración) | **C** |
| FT-25 | Pincel favorito / historial de pinceles recientes | (S2 #36) | **C** |
| FT-26 | Predicción de trazo (menos demora entre el lápiz y la línea) | (S2 Configuración) | **S** — es parte de "que se sienta fluida" |

### 3. Color
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-30 | Selector de color (rueda/cuadrado HSV + valores numéricos + hex) | (S2 #10) | **M** |
| FT-31 | Cuentagotas (incluido el rápido, manteniendo presionado) | (S2 #14, #15) | **M** |
| FT-32 | Paleta de colores guardada / colores recientes | (S2 #10) | **S** |

### 4. Capas
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-40 | Capas: agregar, borrar, duplicar, reordenar, ocultar, renombrar | (S2 #5, #45) | **M** |
| FT-41 | Opacidad por capa | (S2 #187) | **M** |
| FT-42 | Combinar capas / aplanar la imagen | (S2 #18) | **M** |
| FT-43 | Recorte (clipping) a la capa de abajo | (S2 #12, #40) | **M** — es la forma estándar de pintar dentro del lineart |
| FT-44 | Bloqueo alfa (pintar solo sobre lo ya pintado) | (S2 #41) | **M** |
| FT-45 | Modos de fusión de capa (28) | Lista completa en S3 | **S** el grupo principal (Normal, Multiplicar, Trama, Superponer, Luz suave, Luz fuerte, Sobreexponer color, Subexponer color, Agregar, Diferencia, Color, Luminosidad) · **C** el resto |
| FT-46 | Carpetas de capas, moverlas y transformarlas juntas | (S2 #47, #48) | **S** |
| FT-47 | Bloquear capa | (S1 novedades) | **S** |
| FT-48 | Capa de referencia para relleno y selección | (S2 #52) | **S** |
| FT-49 | Capas de ajuste (filtro no destructivo) | (S2 #174) | **C** |
| FT-50 | Capa vectorial | (S2 #50) | **C** |
| FT-51 | Capa 3D | (S1 novedades) | **W** — es un producto aparte |
| FT-52 | Agregar capa desde el lienzo / importar foto como capa | (S2 #49) | **S** |

### 5. Selección y transformación
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-60 | Selección rectangular, elíptica y con lazo | (S2 #8) | **M** |
| FT-61 | Varita mágica / selección por rango de color | (S2 #93) | **S** |
| FT-62 | Expandir, contraer e invertir la selección | (S2 #94) | **S** |
| FT-63 | Cortar, copiar y pegar | (S2 #73) | **M** |
| FT-64 | Transformar: mover, escalar, rotar, voltear | (S2 #188) | **M** |
| FT-65 | Transformar en perspectiva (distorsión libre) | (S2 #60) | **S** |
| FT-66 | Deformación con malla | (S2 #61) | **C** |
| FT-67 | Capa de selección (pintar la selección como máscara) | (S2 #43) | **C** |
| FT-68 | Licuar (pincel que empuja píxeles) | (S2 #95) | **C** |

### 6. Relleno y degradados
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-70 | Balde de relleno con tolerancia, considerando huecos del lineart | (S2 #11, #186) | **M** |
| FT-71 | Balde borrador / relleno envolvente | (S2 #58, #59) | **S** |
| FT-72 | Relleno con lazo / borrador con lazo | (S2 #96, #97) | **S** |
| FT-73 | Degradado lineal y radial | (S2 #13, #138, #139) | **S** |

### 7. Reglas y ayudas de dibujo
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-80 | Regla recta, circular y elíptica | (S2 #74–#76) | **S** |
| FT-81 | Simetría: espejo y caleidoscopio | (S2 #78, #79) | **S** — muy usada para personajes y mandalas |
| FT-82 | Regla radial | (S2 #77) | **C** |
| FT-83 | Reglas de arreglo y arreglo en perspectiva (perspectiva de 1/2/3 puntos) | (S2 #80, #81) | **C** |
| FT-84 | Formas: línea, rectángulo, círculo, elipse, polígono, Bézier, polilínea | (S2 #82–#89) | **S** |
| FT-85 | Formas extra: rectángulo redondeado, flecha, globo de diálogo | (S2 #90–#92) | **C** |

### 8. Filtros (86 en ibis Paint)
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-90 | Ajuste de color: brillo/contraste, tono/saturación/luminosidad, balance de color, curva de tonos, niveles, invertir, escala de grises, posterizar | (S2 #99–#113) | **S** |
| FT-91 | Desenfoque: gaussiano, de movimiento, radial; enfoque (máscara de enfoque); mosaico | (S2 #115–#121) | **S** |
| FT-92 | Extraer lineart (convertir un dibujo escaneado en línea con transparencia) | (S2 #103) | **S** — clave para quien dibuja en papel y colorea digital |
| FT-93 | Mapa de degradado, reemplazar color, cambiar color del dibujo | (S2 #106, #112, #114) | **C** |
| FT-94 | Estilo: contorno, resplandor, sombra paralela, bisel, relieve | (S2 #123–#137) | **C** |
| FT-95 | Dibujo: líneas de velocidad, líneas radiales (estilo manga), nubes, ruido | (S2 #140–#143, #151) | **C** |
| FT-96 | Artísticos/pixelar/transformar: aberración cromática, glitch, bloom, pixelado, ojo de pez, onda, remolino, coordenadas polares, marcos | (S2 #147–#172) | **C** |
| FT-97 | Filtros animados (lluvia y similares), código QR | (S2 #144, #173) | **W** |

### 9. Texto, materiales y manga
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-100 | Herramienta de texto (horizontal/vertical, fuente, tamaño, contorno) | (S2 #62) | **S** — limitado a las fuentes instaladas en el dispositivo |
| FT-101 | Tramas (screentones) | 46 en S1 (S2 #42) | **C** |
| FT-102 | Divisor de viñetas para manga/cómic | (S2 #63) | **C** |
| FT-103 | Manuscrito de manga con márgenes de impresión | (S2 #66, #178–#181) | **C** |
| FT-104 | Biblioteca de materiales (+30.000 en S1, descargados) | (S2 #72) | **W** como catálogo online · **C** como materiales propios importados por el usuario |
| FT-105 | Pluma de copia (clonar) | (S2 #98) | **C** |

### 10. Historial, archivos y exportación
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-110 | Deshacer/rehacer con historial largo | (S2 #7) | **M** |
| FT-111 | Galería local de obras, carpetas, ordenar | (S2 #29, #30) | **M** |
| FT-112 | Guardado automático local | (S2) | **M** — perder un dibujo es la peor falla posible |
| FT-113 | **Exportar e importar el proyecto completo como archivo propio** (capas incluidas) para seguirlo en otro dispositivo | ibis usa su formato IPV y la nube (S2 #28, #182) | **M** — requisito del usuario |
| FT-114 | Exportar imagen PNG (incluido transparente) y JPEG | (S2 #55, #57) | **M** |
| FT-115 | Exportar/importar PSD con capas | (S2 #192) | **S** — interoperabilidad con Photoshop, Krita y Clip Studio |
| FT-116 | Exportar una capa como PNG transparente | (S2 #44) | **C** |
| FT-117 | Exportar en CMYK para imprenta | (S2 #56) | **C** |
| FT-118 | Importar foto o imagen al lienzo | (S2 #49) | **M** |
| FT-119 | Firma del artista | (S2 #19) | **C** |

### 11. Grabación y animación
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-120 | Grabar el proceso y exportarlo como timelapse | (S1) | **C** — local, sin subirlo a ningún lado |
| FT-121 | Animación cuadro por cuadro con papel cebolla | (S2 #175) | **C** — es casi un producto aparte |
| FT-122 | Modo video con pistas de audio | (S2 #176, #177) | **W** |

### 12. Entrada, interfaz y configuración
| ID | Feature | Detalle (fuente) | Prioridad |
|---|---|---|---|
| FT-130 | Rechazo de palma | (S2 #197) | **M** en Android con lápiz |
| FT-131 | Gestos (dos dedos deshace, tres dedos rehace, etc.) | (S2 #194) | **M** |
| FT-132 | Atajos de teclado en Windows | (S2 #195) | **M** en Windows |
| FT-133 | Cursor del pincel y mira al pasar el lápiz sin tocar | (S2 #196, Configuración) | **S** |
| FT-134 | Botones del lápiz asignables | (S2 Configuración) | **C** |
| FT-135 | Ajuste fino de la posición de dibujo respecto del lápiz | (S2 Configuración) | **C** |
| FT-136 | Modo de pantalla completa / interfaz mínima para dibujar | (S2 #190) | **S** |

### 13. Fuera de alcance por la regla 100% offline o por ser el negocio de ibis
| ID | Feature | Por qué queda afuera |
|---|---|---|
| FT-140 | Nube y sincronización entre dispositivos (S2 #27) | Requiere internet. Se reemplaza por FT-113, el archivo de proyecto portable |
| FT-141 | Galería online, publicar, subir pinceles (S2 #21, #34, #193) | Requiere internet |
| FT-142 | Cuenta ibisAccount, códigos promocionales (S2 #198–#201) | Requiere internet y no hay cuentas |
| FT-143 | Funciones Smart/IA con créditos: quitar fondo, acuarela, ampliar con IA, AI Disturbance (S2 #70, #71, #145, #146) | Se pagan con créditos online (S1); si procesan en servidor está sin verificar |
| FT-144 | Membresía Premium, publicidad, desbloqueos pagos (S1, S2 #24–#26) | Es el modelo de negocio de ibis, no una herramienta |
| FT-145 | Protección de obras con Yuify (S1 novedades) | Servicio de terceros online |

## Resumen para decidir

| Nivel | Cantidad | Qué cubre |
|---|---|---|
| **M** | 28 | Lienzo, pincel con presión y estabilizador, goma, color, capas con recorte y bloqueo alfa, selección, transformación, balde, deshacer, galería, autoguardado, **archivo de proyecto portable**, exportar PNG/JPEG, importar imagen, gestos/atajos, rechazo de palma |
| **S** | 31 | Motor de pinceles configurable, pinceles propios, modos de fusión principales, carpetas, reglas y simetría, formas, filtros de ajuste y desenfoque, extraer lineart, texto, PSD, perspectiva |
| **C** | 33 | Filtros de estilo y artísticos, tramas y manga, capas vectoriales y de ajuste, timelapse, animación, CMYK |
| **W** | 9 | Todo lo online, la IA con créditos, el 3D, el video con audio, los filtros animados |

Recuento por script sobre las 101 filas. Las filas con prioridad partida (FT-19, FT-45, FT-104) cuentan en su nivel
más alto.

**Lectura propuesta para la entrega 1:** los **M** forman una app con la que un ilustrador ya
puede terminar y exportar una obra, y llevarla del celular a la PC. Los **S** son el camino
para que la use un profesional. La decisión es del paso 2.
