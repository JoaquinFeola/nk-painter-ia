# US-008 · Borrar
feature: F-002

Como ilustrador, quiero borrar con la forma de pincel que más me sirva para cada corrección,
sin tocar otras capas ni el fondo, para corregir sin miedo a romper lo que está bien.

## Criterios

- [ ] **La primera vez la goma borra con la forma del Pincel redondo.** (RN-goma-forma-inicial)
  Cuando abro la app por primera vez y borro sobre una línea negra sin haber elegido forma
  para la goma, el borrado tiene la forma del Pincel redondo.
  android ✓ · windows ✓ · qa —

- [ ] **La goma borra con la forma elegida.** (RN-goma-usa-forma-de-pincel)
  Cuando elijo la forma Píxel para la goma, en grosor 1, y borro en diagonal de (10, 10) a
  (20, 20) sobre una zona pintada de #000000, cada píxel de la diagonal queda borrado entero
  o intacto: ninguno queda a medio borrar.
  android ✓ · windows ✓ · qa —

- [ ] **La goma tiene grosor y opacidad propios.** (RN-goma-usa-forma-de-pincel)
  Cuando pongo la goma en grosor 30 y opacidad 50 %, con el Pincel redondo en grosor 10, y
  borro sobre una zona pintada de #000000 opaco, el borrado mide 30 px y la pintura queda al
  50 %.
  android ✓ · windows ✓ · qa —

- [ ] **Los ajustes de la goma siguen al reabrir la app.** (RN-goma-ajustes-persisten)
  Cuando pongo la goma con forma de Aerógrafo, grosor 80, opacidad 60 % y estabilizador 2,
  cierro la app y la vuelvo a abrir, la goma tiene esa forma y esos valores.
  android ✓ · windows ✓ · qa —

- [ ] **La goma responde a la presión como su pincel.** (RN-goma-presion-como-su-pincel, RN-presion-proporcional)
  Cuando, con la curva normal y la goma con forma de Aerógrafo en opacidad 100 %, borro al
  50 % de presión sobre pintura #000000 opaca, la pintura queda al 50 %.
  android ✓ · windows ✓ · qa —

- [ ] **Una goma con forma que no responde a la presión borra igual.** (RN-goma-presion-como-su-pincel)
  Cuando, con la goma con forma de Rotulador en opacidad 100 %, borro al 30 % de presión
  sobre pintura #000000 opaca, la pintura desaparece por completo.
  android ✓ · windows ✓ · qa —

- [ ] **La curva de presión vale también para la goma.** (RN-curva-presion-unica)
  Cuando elijo la curva firme y borro al 50 % de presión con la goma con forma de Aerógrafo
  en opacidad 100 %, sobre pintura #000000 opaca, queda más del 50 % de la pintura.
  android ✓ · windows ✓ · qa —

- [ ] **Borrar deja ver el fondo, y el fondo no cambia.** (RN-goma-no-borra-fondo)
  Cuando borro una línea negra en una obra con fondo blanco, en su lugar se ve #FFFFFF y el
  fondo de la obra sigue siendo blanco.
  android ✓ · windows ✓ · qa —

- [ ] **Borrar donde solo hay fondo no cambia nada.** (RN-goma-no-borra-fondo)
  Cuando borro en una zona sin pintura de una obra con fondo #F5E6C8, la zona sigue
  #F5E6C8.
  android ✓ · windows ✓ · qa —

- [ ] **La goma borra solo en la capa activa.** (RN-goma-solo-capa-activa)
  Cuando, con la Capa 2 activa y una línea negra en la Capa 1 y otra en la Capa 2 en el mismo
  lugar, borro sobre ellas, se quita solo la línea de la Capa 2.
  android — · windows — · qa —

- [ ] **Cada borrado se deshace por separado.** (RN-trazo-en-historial)
  Cuando borro dos veces sobre una línea y deshago una vez, vuelve solo lo que quitó el
  segundo borrado.
  android — · windows — · qa —

## Notas
- Casos que salen mal: borrar donde solo hay fondo, y borrar sin querer en la capa de abajo
  (que no pasa).
- Dependen de otras features, como en F-001 (QT-59): "solo en la capa activa" necesita varias
  capas (F-003, US-009); deshacer necesita el historial (F-004, US-014). Hasta entonces
  quedan `blocked` en QA.
- Construido en la unidad 40. Sin marca de área: "solo en la capa activa" (F-003) y deshacer
  (F-004). Los controles de la herramienta Goma y su panel no los alcanza un test: QA (unidad 41).
- La presión del emulador llega al 100 %: vale la nota de US-007 sobre la QA de Android.
