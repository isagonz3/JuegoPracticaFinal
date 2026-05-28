# LOG DE CONSULTA — Optimización de coste operacional

**Proyecto:** `JuegoPracticaFinal` — "A TAD of Adventure"
**Lenguaje:** Java 25 + JavaFX 21.0.6, Maven
**Líneas totales:** ~4.350 en 57 clases

---

## Problema inicial
El usuario preguntó cómo mejorar el coste operacional del código.

## Análisis realizado
Se exploró el código completo identificando los cuellos de botella:

| # | Problema | Archivo | Impacto |
|---|---|---|---|
| 1 | 900 `WritableImage` creados por render | `MapaRenderer.java:79-85` | Crítico |
| 2 | Múltiples escaneos completos de cuadrícula por turno | `PartidaObjetosYCombate.java`, `PartidaMovimiento.java` | Crítico |
| 3 | `Lista.get(i)` en bucles → O(n²) | 13 archivos, ~30 bucles | Alto |
| 4 | BFS ejecutado en cada pulsación WASD | `PartidaMovimiento.java:135` | Alto |
| 5 | `SpriteManager` sin caché de imágenes | `SpriteManager.java` | Medio |
| 6 | Rutas absolutas del sistema de archivos | `MapaLoader.java`, `Configuracion.java` | Bajo |

## Plan de modificación acordado

Se priorizaron **3 problemas graves** con cambios localizados usando solo TADs del proyecto (sin `java.util.*`):

### 1. Cache de tiles en `MapaRenderer` con `Lista<Matrix<Image>>`
- Precarga todos los tiles de una zona la primera vez que se renderiza
- Reutiliza los tiles cacheados en renders posteriores
- Pasa de 900 `WritableImage` nuevos por render a **0 nuevos**

### 2. Iterador en `Lista` + conversión de bucles
- Añadir método `iterador()` y clase interna `IteradorLista` en `Lista.java`
- Convertir ~30 bucles que usan `get(i)` de O(n²) a O(n)
- Interfaz `Iterador<T>` ya existe en el proyecto

### 3. BFS condicional en `moverJugador()`
- Para movimientos adyacentes (WASD/flechas), validar en O(1) en vez de ejecutar BFS
- BFS solo se ejecuta para clics en destino lejano

## Archivos a modificar

| Archivo | Cambio |
|---|---|
| `MapaRenderer.java` | Añadir `tileCache: Lista<Matrix<Image>>`, método `getTile()`, 2 bucles → iterador |
| `Lista.java` | Añadir `iterador()` + clase `IteradorLista` |
| `UIRenderer.java` | 4 bucles → iterador |
| `EndController.java` | 1 bucle → iterador |
| `LoadJSON.java` | 2 bucles → iterador |
| `Inventario.java` | 4 bucles → iterador |
| `Partida.java` | 3 bucles → iterador |
| `PartidaMovimiento.java` | BFS condicional + 1 bucle → iterador |
| `GrafoZonas.java` | 3 bucles → iterador |
| `MenuController.java` | 1 bucle → iterador |
| `PartidaObjetosYCombate.java` | 3 bucles → iterador |
| `SpriteManager.java` | Cache con `Lista<Image>` |

## Mejora estimada

| Operación | Antes | Después |
|---|---|---|
| Render (zona 30×30) | 900 `WritableImage` nuevos | 0 nuevos (caché) |
| `getCeldasAccesibles(n=200)` | 20.100 recorridos nodos | 200 (~100x) |
| Movimiento WASD | ~900 celdas BFS exploradas | 0 (~instantáneo) |
| `UIRenderer.actualizarLog(n=100)` | 5.050 recorridos | 100 (~50x) |
| `LoadJSON.guardarLog(n=100)` | 5.050 recorridos | 100 (~50x) |
| `Inventario.contiene(n=30)` | 465 recorridos | 30 (~15x) |
| `SpriteManager.getJugadorSprite()` | 1 `Image` + 1 stream nuevos | 0 nuevos (caché) |
