# ADR-001 · Compose Multiplatform con un módulo nativo de lápiz en Windows
status: accepted · 2026-10-28 · decided by: el usuario (QT-06, QT-22)

## Qué forzó esto
El producto tiene que correr en Android y en Windows, escrito en Kotlin y con una sola
implementación (Q-02). La presión del lápiz cambia el trazo en las dos plataformas (F-002), y
una obra que viaja entre equipos se tiene que ver idéntica píxel por píxel (F-008, Q-67).
Compose Multiplatform es estable en Android y en escritorio, pero en Windows corre sobre la
JVM y no entrega la presión del lápiz (QT-04, QT-06).

## Opciones
| Opción | Costo |
|---|---|
| Compose Multiplatform y un módulo nativo que lee el lápiz de Windows | Código nativo que mantener; riesgo de ventanas en Windows a probar; la JVM agrega memoria base en Windows, sin medir todavía |
| Compose Multiplatform sin presión en Windows | Rompe RN-presion-segun-pincel en Windows: hay que cambiar F-002 |
| Android en Kotlin y Windows en otra tecnología (C#/WinUI o C++) | Dos apps y dos motores: rompe Q-02, el motor común de QT-11 y la obra idéntica píxel por píxel de F-008, y duplica el trabajo |
| Dos apps con el motor Kotlin compilado como DLL nativa para una UI de Windows en C# | Dos interfaces que construir; Kotlin nativo con Skia en Windows sin verificar |

## Decisión
Compose Multiplatform para Android y Windows, con un solo motor de pintura en Kotlin (QT-11) y
un módulo nativo chico que lee la presión y el tipo de puntero del lápiz de Windows (QT-24).

El usuario eligió primero "Windows en otra tecnología". Le planteé que chocaba con QT-11, Q-02
y F-008, y eligió volver a esta opción (QT-22).

## Qué aceptamos
- En Windows la app carga un runtime de Java. Su memoria base no está medida: se mide en el
  esqueleto contra la PC de 8 GB, y si no entra en el objetivo de memoria se vuelve a discovery.
- Mantener código nativo de Windows para el lápiz, fuera del código compartido.
- El riesgo de que la ventana de Compose en Windows no deje engancharse a sus eventos
  (issue 4934 del repo oficial). Si la prueba del esqueleto falla, esta decisión se reabre con
  ese resultado.
- El instalador de Windows se arma en Windows: no hay compilación cruzada.
- Revertirla significa reescribir la UI de Windows en otra tecnología y duplicar el motor.

## Reemplaza a
Nada.
