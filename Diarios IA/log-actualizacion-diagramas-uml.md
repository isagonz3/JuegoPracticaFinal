# LOG DE CONSULTA — Actualización de diagramas UML

**Proyecto:** `JuegoPracticaFinal` — "A TAD of Adventure"
**Lenguaje:** Java 25 + JavaFX 21.0.6, Maven
**Fecha:** 2026-06-07

---

## Problema inicial

El usuario solicitó crear los diagramas UML faltantes y actualizar los existentes para que reflejen el estado actual del código tras múltiples cambios: nuevos TADs (Grafo, ArbolBinarioDeBusqueda, Pila, Cola, Iterador), sistema de deshacer movimiento, cambios en GrafoZonas, Inventario, Matrix, Objeto (Comparable), EstadoJuego, y controladores.

## Trabajo realizado

### Diagramas creados (2 nuevos)

| Archivo | Tipo | Descripción |
|---|---|---|
| `UMLs/estados_juego.puml` | State diagram | Máquina de estados: `[*]→MENU→EN_CURSO→VICTORIA/DERROTA→MENU` |
| `UMLs/actividad_turno_jugador.puml` | Activity diagram | Flujo completo de un turno: mover (repeat loop) → elegir acción (fork atacar/usar/equipar/interactuar) → terminarTurno → moverEnemigos → atacarJugador → checkDerrota → checkVictoria → iniciarTurno |

### Diagramas actualizados (4 archivos)

| Archivo | Cambios principales |
|---|---|
| `clases_completo.puml` | Añadidos rectángulos **TADs** (14 clases/interfaces: InterfazGrafo, Grafo, Arista, ArbolBinarioDeBusqueda, NodoArbol, Lista, Cola, Pila, Matrix, Iterador, ElementoDobleEnlazada, ElementoSimpleEnlazada, InterfazArbol, InterfazIterador) y **Excepciones** (8 clases). GrafoZonas delega a `Grafo<Integer, Arista<Integer>>`. LogMovimiento con `Pila<Posicion>`. Inventario con `ArbolBinarioDeBusqueda<Objeto>`. Objeto y Puerta como `Comparable`. EstadoJuego sin SETTINGS. JuegoController con `deshacerMovimientoBtn` y `onDeshacerMovimiento()`. MapaRenderer con `StackPane` y `cacheCeldas`. |
| `clases_modelo.puml` | Mismos cambios que completo pero solo capa modelo (sin controladores ni vista). MapaLoader como clase con métodos estáticos. |
| `clases_control.puml` | JuegoController con nuevos atributos: `deshacerMovimientoBtn`, `entregarGatoBtn`, `StackPane`, `objetosUsadosArea`, `objetosEquipadosArea`. Nuevo método: `onDeshacerMovimiento()`. |
| `clases_vista.puml` | MapaRenderer con `StackPane mapaStackPane` y `Lista<Matrix<Image>> cacheCeldas` (del refactor de rendimiento anterior). |

### Diagramas verificados sin cambios (9 archivos)

| Archivo | veredicto |
|---|---|
| `casos_uso.puml` | Sigue preciso |
| `secuencia_turnos.puml` | Correcto (incluye checkVictoria/checkDerrota) |
| `secuencia_movimiento.puml` | Correcto (incluye cambio de zona con puerta, BFS, recogerObjeto) |
| `secuencia_ataque.puml` | Correcto (incluye huida enemigo 50%) |
| `secuencia_JSON.puml` | Correcto (guardar + cargar con Gson) |
| `secuencia_inicio_partida.puml` | Correcto |
| `secuencia_objetos.puml` | Correcto (usar + equipar + mapa) |
| `secuencia_NPCs.puml` | Correcto (hablar + comerciar + gato) |
| `secuencia_fin_partida.puml` | Correcto (victoria/derrota + volver menú) |

## Verificación

Los 15 diagramas `.puml` se compilaron a PNG con PlantUML v1.2025.0 sin errores. Todos los PNG existen y se muestran correctamente.

| Estadística | Valor |
|---|---|
| Archivos .puml | 15 |
| Archivos .png | 15 |
| PNG más grande | `clases_completo.png` (981 KB) |
| PNG más pequeño | `secuencia_inicio_partida.png` (24 KB) |

## Archivos modificados

| Archivo | Acción |
|---|---|
| `UMLs/estados_juego.puml` | **CREAR** |
| `UMLs/actividad_turno_jugador.puml` | **CREAR** |
| `UMLs/clases_completo.puml` | **REEMPLAZAR** |
| `UMLs/clases_modelo.puml` | **REEMPLAZAR** |
| `UMLs/clases_control.puml` | **REEMPLAZAR** |
| `UMLs/clases_vista.puml` | **REEMPLAZAR** |
