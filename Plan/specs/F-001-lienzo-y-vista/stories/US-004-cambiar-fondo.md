# US-004 · Cambiar el fondo
feature: F-001

Como ilustrador, quiero cambiar el fondo de una obra que ya empecé, y volver atrás si no me
gusta, para probar cómo queda el dibujo sobre otro color o exportarlo sin fondo.

## Criterios

- [x] **El fondo se cambia a otro color.** (RN-fondo-cambiable)
  Cuando cambio a #1E2A3A el fondo de una obra con fondo blanco y un trazo en "Capa 1", el
  fondo es #1E2A3A y el trazo sigue igual.
  android ✓ · windows ✓ · qa ✓

- [x] **El fondo se cambia a transparente.** (RN-fondo-cambiable)
  Cuando cambio a transparente el fondo de una obra con fondo blanco, el lienzo no tiene
  color detrás de las capas.
  android ✓ · windows ✓ · qa ✓

- [x] **Un fondo transparente se cambia a un color.** (RN-fondo-cambiable)
  Cuando cambio a #F5E6C8 el fondo de una obra creada con fondo transparente, el fondo es
  #F5E6C8.
  android ✓ · windows ✓ · qa ✓

- [ ] **Deshacer devuelve el fondo anterior.** (RN-fondo-cambiable)
  Cuando deshago justo después de cambiar el fondo de blanco a #1E2A3A, el fondo vuelve a
  ser blanco.
  android — · windows — · qa —

- [ ] **Rehacer vuelve a aplicar el cambio.** (RN-fondo-cambiable)
  Cuando rehago después de haber deshecho el cambio de blanco a #1E2A3A, el fondo vuelve a
  ser #1E2A3A.
  android — · windows — · qa —

- [ ] **Elegir el mismo fondo no agrega un paso.** (RN-fondo-cambiable)
  Cuando, después de pintar un trazo en una obra con fondo blanco, elijo blanco otra vez y
  deshago una vez, se quita el trazo y el fondo sigue blanco.
  android — · windows — · qa —

- [ ] **El fondo cambiado se mantiene al reabrir.** (RN-fondo-cambiable, RN-contenido-del-archivo de F-008)
  Cuando cierro una obra a la que le cambié el fondo a #1E2A3A y la vuelvo a abrir, el fondo
  es #1E2A3A.
  android — · windows — · qa —

## Notas
- Camino triste: elegir el fondo que ya tiene no cambia nada ni deja un paso vacío en el
  historial (Q-85).
- Deshacer y rehacer son de F-004, y reabrir necesita F-007. Pasa lo mismo que en US-003: el
  diseño técnico (unidad 22) decide si esos criterios esperan a esas features o si alcanza
  con lo mínimo de cada una.
- Que el PNG exportado conserve la transparencia es de F-008 (US-031).
