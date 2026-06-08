# LOG DE CONSULTA — Estilización CSS de vistas del juego

**Proyecto:** `JuegoPracticaFinal` — "A TAD of Adventure"
**Lenguaje:** Java 25 + JavaFX 21.0.6, Maven
**Fecha:** 2026-05-28

---

## Problema inicial
El usuario pidió mejorar el aspecto visual de 4 vistas (main-view, menu-view, end-view, tienda-view) para que se vean más como un videojuego, usando CSS stylesheets.

## Análisis realizado

### Estado actual del styling
- **No existe ningún archivo CSS** en el proyecto (glob `**/*.css` = vacío)
- Todo el estilo es inline en FXML con `style=""` o programático en Java con `setTextFill()`
- 4 vistas FXML en `src/main/resources/jueguito/juegopracticafinal/`
- Ventana: 960×576 px fijos

### Vistas y su estilo actual

| Vista | Archivo | Estilo actual |
|---|---|---|
| Menú | `menu-view.fxml` | Label título: `font-size: 24; font-weight: bold` + `<Font name="Roboto Light" size="12">` (parcialmente contradictorio). 3 botones sin estilo. |
| Juego | `main-view.fxml` | Múltiples inline styles: zonaLabel (18px bold), tiendaPanel (fondo rgba negro 0.7), labels de sección (bold), turnoLabel (14px). Sin coherencia visual. |
| Final | `end-view.fxml` | resultadoLabel (24px bold), logFinal (monospace). Layout con coordenadas absolutas (AnchorPane). Colores rojo/verde puestos desde `EndController.java` con `setTextFill()`. |
| Tienda | `tienda-view.fxml` | Título (18px bold). Sin más estilo. |

### Paleta de colores propuesta (fantasía oscura / RPG)

| Elemento | Color |
|---|---|
| Fondo general | `#1a1a2e` (azul noche) |
| Títulos/acentos | `#ffd700` (dorado) |
| Paneles laterales | `rgba(15, 52, 96, 0.85)` |
| Botones | fondo `#16213e`, borde dorado `#ffd700` |
| Texto secundario | `#e0e0e0` |
| Salud/combate | `#e74c3c` (rojo) |
| Victoria | `#2ecc71` (verde) |

## Plan de modificación acordado

### 1. Crear archivo CSS
`src/main/resources/jueguito/juegopracticafinal/game-style.css`

Clases CSS a definir:
- `.game-root` — fondo oscuro base
- `.game-title` — texto grande dorado con glow (título menú + resultado final)
- `.zone-label` — nombre de zona destacado
- `.section-label` — encabezados de sección con línea dorada inferior
- `.game-button` — botón oscuro con borde dorado, hover glow
- `.game-panel` — panel semitransparente con borde dorado
- `.game-text-area` — área de texto estilo terminal
- `.game-progress-bar` — barra de vida roja sobre track oscuro
- `.game-list-view` — lista oscura con borde dorado
- `.shop-overlay` — superposición de tienda
- `.victory` / `.defeat` — colores para resultado final

### 2. Modificar FXML (4 archivos)

| FXML | Cambios |
|---|---|
| `menu-view.fxml` | Quitar `style=""` inline. Añadir `styleClass="game-title"` al Label título y `styleClass="game-button"` a los 3 botones. VBox raíz con clase `menu-root`. |
| `main-view.fxml` | Quitar todos los `style=""` inline. Asignar clases: zonaLabel → `zone-label`, labels de sección → `section-label`, botones → `game-button`, TextAreas → `game-text-area`, ProgressBar → `game-progress-bar`, ListView → `game-list-view`, tiendaPanel → `shop-overlay`. |
| `end-view.fxml` | Quitar `style=""` inline. Cambiar de AnchorPane (coordenadas absolutas) a VBox centrado. resultadoLabel → `game-title`, logFinal → `game-text-area`, volverBtn → `game-button`. |
| `tienda-view.fxml` | Quitar `style=""` inline. Título → `section-label`, botones → `game-button`. |

### 3. Modificar Java (2 archivos)

| Archivo | Cambio |
|---|---|
| `JueguitoFX.java` | En los 4 métodos que crean Scene (`start`, `irAJuego`, `irAEnd`, `volverAlMenu`), agregar `scene.getStylesheets().add(getClass().getResource("/jueguito/juegopracticafinal/game-style.css").toExternalForm())`. |
| `EndController.java` | Reemplazar `resultadoLabel.setTextFill(Color.GREEN/RED)` por `resultadoLabel.getStyleClass().add("victory")` / `.add("defeat")`. Eliminar import `javafx.scene.paint.Color`. |

### 4. Mejora de layout en end-view.fxml
Cambiar de `AnchorPane` (con `layoutX`/`layoutY` absolutos) a `VBox` centrado con `alignment="CENTER"` y `spacing="20"`, consistente con el menú.

## Archivos a modificar

| Archivo | Cambio |
|---|---|
| `src/main/resources/jueguito/juegopracticafinal/game-style.css` | **CREAR** — archivo CSS con todas las clases de estilo |
| `src/main/resources/jueguito/juegopracticafinal/menu-view.fxml` | Reemplazar inline styles por styleClass |
| `src/main/resources/jueguito/juegopracticafinal/main-view.fxml` | Reemplazar inline styles por styleClass |
| `src/main/resources/jueguito/juegopracticafinal/end-view.fxml` | Reemplazar inline styles + cambiar layout a VBox centrado |
| `src/main/resources/jueguito/juegopracticafinal/tienda-view.fxml` | Reemplazar inline styles por styleClass |
| `src/main/java/jueguito/juegopracticafinal/App/JueguitoFX.java` | Agregar stylesheet a las 4 escenas |
| `src/main/java/jueguito/juegopracticafinal/Controladores/EndController.java` | Cambiar setTextFill por styleClass |

---

## Segunda iteración — Paleta Lospec500 + fuente pixelada (2026-06-08)

### Solicitud
El usuario pidió mejorar los estilos con fuente pixelada y colores verdes basados en la paleta **Lospec500**.

### Decisiones tomadas

| Pregunta | Respuesta |
|---|---|
| ¿Fuente pixelada? | **Press Start 2P** (Google Fonts, OFL) |
| ¿Fondo general? | **Claro** (`#f6e8e0` — warm white) en lugar de oscuro |
| ¿Acento tienda? | **Oro** (`#dab163` / `#f7f3b7`) |

### Archivos modificados

| Archivo | Cambio |
|---|---|
| `game-style.css` | Reescritura completa con paleta verde Lospec500 |
| `PressStart2P-Regular.ttf` | **AGREGADO** (118 KB) a resources |
| `JueguitoFX.java` | `Font.loadFont()` para Press Start 2P |

### Mapa de colores Lospec500 → CSS

```
Lospec500   → Uso CSS
#f6e8e0       Fondo general (warm white)
#ffffff       Fondos de texto/listas
#1e4044       Texto body (dark teal)
#006554       Botones, labels (deep green)
#26854c       Títulos, hover (green)
#62a477       Bordes (muted green)
#d3eed3       Panel lateral (pale green)
#5ab552       Barra de progreso (medium green)
#9de64e       Victoria (bright green)
#ec273f       Derrota (red)
#dab163       Tienda botones (gold)
#f7f3b7       Texto sobre fondo oscuro (pale yellow)
```

### Tamaños para Press Start 2P
Fuente pixel chunk, tamaños reducidos: título 24px, botones 8px, labels 8-10px.

---

## Tercera iteración — Reemplazo de fuente: Press Start 2P → VT323 (2026-06-08)

### Solicitud
El usuario preguntó por fuentes más legibles manteniendo estilo pixelado. Se analizaron:

| Fuente | Mono | Acentos español | Legibilidad | JavaFX |
|---|---|---|---|---|
| **VT323** | ✅ | ✅ | Muy alta | ✅ TTF estático |
| Pixelify Sans | ❌ | ✅ | Alta | ✅ TTF estático |
| Silkscreen | ✅ | ✅ | Media | ✅ TTF estático |
| DotGothic16 | ✅ | Parcial | Media-alta | ✅ TTF estático |

**Decisión:** Se reemplazó Press Start 2P por **VT323** (terminal font, más legible, monospaced, soporta español).

### Archivos modificados

| Archivo | Cambio |
|---|---|
| `game-style.css` | `@font-face` → VT323, todos los `font-family` actualizados, tamaños aumentados |
| `PressStart2P-Regular.ttf` | **ELIMINADO** |
| `VT323-Regular.ttf` | **AGREGADO** (153 KB) |
| `JueguitoFX.java` | `Font.loadFont()` actualizado a VT323 |

### Tamaños para VT323 (iteración 1)
Título 32px, zona 14px, sección 12px, label 11px, botón juego 10px, botón menú 18px, text area 11px, fin 20px, victoria/derrota 13px.

---

## Cuarta iteración — Aumento de tamaño de fuente (2026-06-08)

### Solicitud
El usuario pidió aumentar los tamaños de fuente para mejorar legibilidad.

### Tamaños finales

| Elemento | Tamaño final |
|---|---|
| Título (`game-title`) | **38px** |
| Zona label (`zone-label`) | **18px** |
| Section label (`section-label`) | **15px** |
| Game label (`game-label`) | **14px** |
| Game button (`game-button`) | **13px** |
| Menu button (`menu-button`) | **24px** |
| Text area (`game-text-area`) | **14px** |
| End screen (`end-root`) | **26px** |
| Victoria / Derrota | **16px** |

### Archivos modificados
Solo `game-style.css` (6 edits de `-fx-font-size`).
