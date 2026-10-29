# Pruebas · US-008 Borrar
feature: F-002 · unidad 41 · plan aprobado 2026-10-29 (QT-115)

Medios, forma de medir y datos: los de `US-006-ajustar-pincel.md`. Lo propio de esta historia:
- **Presión**: en Windows se inyecta con `tools/inject-pen.ps1`. En Android la corre el usuario en su
  tableta (QT-90): se mira el sentido (borra menos, borra todo), no la medida.
- **Zona pintada de #000000 opaco**: Rotulador de fábrica en Grosor 100, pasadas que se pisan sobre la
  zona que el escenario nombra.
- **Fondo de color** (E-09): la opción Color de Obra nueva con #F5E6C8.

## Escenarios

### E-01 · La primera vez la goma borra con la forma del Pincel redondo
- **Cubre:** CA-01 (RN-goma-forma-inicial) · prioridad alta
- **Setup / cleanup:** preferencias de fábrica (`data/US-005/E-02-setup.ps1` · `E-02-teardown.ps1`; Android `pm clear`).
- **Pasos:** obra 600×600; zona negra alrededor de (300, 300); Goma sin elegir forma; un toque en (300, 300).
- **Esperado:** la herramienta Goma dice "Pincel redondo". El hueco es un círculo de borde duro, de
  20 px (±1), el grosor de fábrica de esa forma.

### E-02 · La goma borra con la forma elegida
- **Cubre:** CA-02 (RN-goma-usa-forma-de-pincel) · prioridad alta
- **Pasos:** obra 30×30 pintada entera de negro; Goma con forma Píxel, Grosor 1; de (10, 10) a (20, 20).
- **Esperado:** cada píxel queda borrado entero (#FFFFFF) o intacto (#000000); ninguno intermedio.

### E-03 · La goma tiene grosor y opacidad propios
- **Cubre:** CA-03 (RN-goma-usa-forma-de-pincel) · prioridad alta
- **Pasos:** Pincel redondo en Grosor 10; obra 1200×600 con una franja negra de 100 px en y = 300; Goma
  con forma Pincel redondo, Grosor 30, Opacidad 50; una pasada vertical de (600, 150) a (600, 450).
- **Esperado:** el borrado mide 30 px (±1) y en su centro la pintura queda al 50 %: #808080 (±1).

### E-04 · Los ajustes de la goma siguen al reabrir la app
- **Cubre:** CA-04 (RN-goma-ajustes-persisten) · prioridad alta
- **Pasos:** Goma: forma Aerógrafo, Grosor 80, Opacidad 60, Estabilizador 2; cerrar; abrir; crear obra;
  abrir el panel Goma.
- **Esperado:** forma Aerógrafo, 80, 60 % y 2, sin tocar nada.

### E-05 · La goma responde a la presión como su pincel
- **Cubre:** CA-05 (RN-goma-presion-como-su-pincel, RN-presion-proporcional) · prioridad alta
- **Pasos:** curva Normal; Goma con forma Aerógrafo, Opacidad 100; sobre la franja negra, idas y vueltas
  sin levantar a presión 512.
- **Esperado:** Windows: en el centro la pintura queda al 50 % (canal 128 ±8, con la medida de Q-110).
  Android (usuario): apretando poco, borra menos que apretando fuerte.

### E-06 · Una goma con forma que no responde a la presión borra igual
- **Cubre:** CA-06 (RN-goma-presion-como-su-pincel) · prioridad alta
- **Pasos:** Goma con forma Rotulador, Opacidad 100; una pasada a presión 307 (30 %).
- **Esperado:** Windows: donde pasó queda #FFFFFF. Android (usuario): borra todo con poca y con mucha presión.

### E-07 · La curva de presión vale también para la goma
- **Cubre:** CA-07 (RN-curva-presion-unica) · prioridad alta
- **Pasos:** Presión Firme (en el panel Pincel); Goma con forma Aerógrafo, Opacidad 100; idas y vueltas a 512.
- **Esperado:** Windows: queda más del 50 % de la pintura (canal del centro menor que 120).
  Android (usuario): con el mismo apoyo leve, borra menos que en Normal.
- **Cleanup:** volver a Normal.

### E-08 · Borrar deja ver el fondo, y el fondo no cambia
- **Cubre:** CA-08 (RN-goma-no-borra-fondo) · prioridad alta
- **Pasos:** obra 1200×600, fondo blanco; línea negra; borrarla entera con la goma de fábrica.
- **Esperado:** donde estaba la línea se ve #FFFFFF; el menú Fondo sigue en Blanco.

### E-09 · Borrar donde solo hay fondo no cambia nada
- **Cubre:** CA-09 (RN-goma-no-borra-fondo) · prioridad media
- **Pasos:** obra 600×600 con fondo #F5E6C8; borrar en una zona sin pintura.
- **Esperado:** la zona sigue #F5E6C8 (±1).

### E-10 · La goma borra solo en la capa activa
- **Cubre:** CA-10 · **blocked**: hacen falta varias capas (F-003, US-009).

### E-11 · Cada borrado se deshace por separado
- **Cubre:** CA-11 · **blocked**: hace falta el historial (F-004, US-014).

## Bordes buscados
- Borrar sin querer en la capa de abajo: es CA-10, blocked.
- Goma con grosor 500 más ancha que lo visible: lo mismo que el pincel (US-005 E-09). Sin escenario
  propio, prioridad baja.
- Borrar sobre lo transparente de una obra con fondo transparente: lo fija F-001 (cuadriculado), no un
  criterio de acá; sin escenario.

## Cobertura
| Criterio | Escenarios | Estado |
|---|---|---|
| CA-01 | E-01 | windows passed · android pending |
| CA-02 | E-02 | windows passed · android pending |
| CA-03 | E-03 | windows passed · android pending |
| CA-04 | E-04 | windows passed · android pending |
| CA-05 | E-05 | windows passed · android pending (usuario) |
| CA-06 | E-06 | windows passed · android pending (usuario) |
| CA-07 | E-07 | windows passed · android pending (usuario) |
| CA-08 | E-08 | windows passed · android pending |
| CA-09 | E-09 | windows passed · android pending |
| CA-10 | E-10 | blocked (F-003) |
| CA-11 | E-11 | blocked (F-004) |

## Ejecuciones

### Corrida 2026-10-29 · Windows · completa (unidad 41)
Mismos medios que US-006, corrida del mismo día. Evidencia: `evidence/2026-10-29/windows/`.

| Escenario | Estado | Real | Evidencia |
|---|---|---|---|
| E-01 | passed | preferencias de fábrica: la Goma dice "Pincel redondo"; un toque sobre la zona negra deja un círculo de borde duro de 19 px blancos más dos de borde que suman 1: 20 px | `w-US008-E01-goma-pincel-redondo.png`, `w-US008-E01-hueco.png` |
| E-02 | passed | Píxel, Grosor 1, diagonal sobre negro: cada píxel queda #FFFFFF o #000000, ninguno intermedio (los intermedios están solo en el borde de la franja, que es del Rotulador) | `w-US008-E02-diagonal-pixel.png` |
| E-03 | passed | Pincel redondo en 10; Goma Pincel redondo 30 y 50 %: el borrado mide 29 px a #808080 más dos de borde que suman 1: 30 px; centro #808080 | `w-US006-E10-US008-E03-a-E08.png` |
| E-04 | passed | Goma Aerógrafo 80, 60 %, 2; cerrar, abrir, crear obra: la herramienta dice "Aerógrafo" y el panel muestra 80, 60 % y 2 | `w-US006-E12-US008-E04-reabrir.png` |
| E-05 | passed | Goma Aerógrafo 100 %, curva Normal, diez idas y vueltas a 512: centro 128 (la pintura al 50 %) | `w-US006-E10-US008-E03-a-E08.png` |
| E-06 | passed | Goma Rotulador 100 %, una pasada a 307: donde pasó, #FFFFFF | ídem |
| E-07 | passed | Firme, Goma Aerógrafo, diez idas y vueltas a 512: centro 78 (queda más del 50 %). Cleanup: vuelta a Normal (hecho) | ídem |
| E-08 | passed | línea negra borrada con la goma: #FFFFFF en todo el largo; el menú Fondo sigue en Blanco | ídem, `w-US008-E08-fondo-blanco.png` |
| E-09 | passed | obra 600×600 con fondo #F5E6C8: borrar donde no hay pintura deja #F5E6C8 (245, 230, 200), igual que antes | `w-US008-E09-fondo-F5E6C8.png` |
| E-10, E-11 | blocked | capas (F-003) e historial (F-004) | — |
