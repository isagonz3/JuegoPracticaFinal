# Documento de Requisitos del Sistema (Revisado v2)

Basado en el análisis del código fuente existente del proyecto **"A TAD of Adventure"**.

---

## Requisitos Obligatorios de Implementación

### RO-P1 — Uso obligatorio de estructuras de datos propias (TADs)

**Estado:** VIGENTE.

Todas las estructuras de datos del proyecto deben utilizar las implementaciones propias del paquete `TADs` (`Lista`, `Cola`, `Pila`, `Matrix`, `Grafo`, `Arista`, `Arbol`, etc.). Queda prohibido el uso de colecciones del paquete `java.util` (`ArrayList`, `HashMap`, `LinkedList`, `Stack`, `Queue`, etc.) para la lógica del dominio del juego.

**Excepciones permitidas:**
- `java.util.Map` / `java.util.HashMap` exclusivamente para adaptadores JSON (Gson).
- Colecciones estándar en código de pruebas (tests JUnit) si es necesario para preparar datos de prueba.

### RO-P2 — Uso obligatorio de excepciones personalizadas

**Estado:** VIGENTE.

El manejo de errores en el proyecto debe realizarse mediante las excepciones del paquete `Modelo.Excepciones` en los casos que aplique:

| Excepción | Uso obligatorio |
|-----------|----------------|
| `ErrorCargaVistaException` | Fallos al cargar archivos FXML o recursos de vista |
| `ErrorCargaJuegoException` | Fallos al cargar partidas guardadas o recursos del juego |
| `ErrorMovimientoInvalido` | Intentos de movimiento a casillas no accesibles o inválidas |
| `ErrorCasillaOcupada` | Intento de mover una entidad a una casilla ya ocupada |
| `ErrorMovimientoSinBarca` | Intento de cruzar agua sin tener la barca equipada |
| `ErrorAccesoZonaBloqueada` | Intento de acceder a una zona bloqueada sin cumplir requisitos |
| `DeshacerMovimientoInvalido` | Intento de deshacer un movimiento cuando no hay historial |

Queda prohibido lanzar `Exception` genérica o `RuntimeException` directamente cuando exista una excepción personalizada que cubra el caso.

---

## Requisitos Funcionales (Pendientes de Implementar)

### RF-P1 — Equipamiento funcional de objetos

**Estado:** ROTO. `PartidaObjetosYCombate.equiparObjeto()` elimina el objeto del inventario pero **nunca llama a `Jugador.equipar()`**, por lo que las bonificaciones de ataque/defensa/rango nunca se aplican.

**Solución:** Añadir `jugador.equipar(o, s)` antes de `partida.terminarTurnoPublic()` en `PartidaObjetosYCombate.java:249`.

---

### RF-P2 — Carga correcta de enemigos desde JSON

**Estado:** ROTO. `LoadJSON.guardarPartida()` serializa con `"-"` (guion) pero `cargarPartida()` divide con `"\\|"` (pipe). Los enemigos guardados nunca se restauran.

**Solución:** Unificar delimitador (ej. `"::"`) en ambos extremos.

---

### RF-P3 — Cálculo correcto de daño al atacar enemigos

**Estado:** BUG. `PartidaObjetosYCombate.atacarEnemigo()` aplica defensa dos veces: primero en `calcularHit()` y luego en `e.recibirAtaque()`.

**Solución:** Reemplazar `e.recibirAtaque(hit)` por `e.getEstadisticas().restarVidaDirecta(hit)`.

---

### RF-P4 — Posición del Gato como ente estático

**Estado:** PARCIAL. El gato se coloca estático en zona 8 en posición (24,30) mediante `Partida.colocarGatoFijo()`. Aunque existe configuración `gato.rangoMov`, `gato.zonasPermitidas`, `gato.probMovimiento` en `configuracion.json`, el gato se considera un **NPC estático sin movimiento** que el jugador debe encontrar e interactuar para rescatarlo.

**Solución:** Dejar el gato como ente fijo. Mover su posición y zona de aparición a `configuracion.json` en lugar de tenerlos hardcodeados en `Partida.java:475-504`. Eliminar el campo `gato.probMovimiento` de la configuración si no se va a usar.

---

### RF-P5 — Población de zonas desde configuración JSON

**Estado:** PARCIAL. `poblarZona()` y `ponerObjetos()` usan IDs de zona hardcodeados (0,1,5,6,9). `zonasSinSpawn` existe en JSON pero no se mapea en `Configuracion.ZonaCfg`. Las listas `spawnEnemigos`, `spawnObjects`, `spawnNPCs` de `Zona` nunca se cargan desde JSON.

**Solución:** Añadir campo `zonasSinSpawn` a `ZonaCfg` y usarlo. Opcionalmente cargar spawns desde `habitaciones.json`.

---

### RF-P6 — Carga de NPCs desde JSON

**Estado:** NO IMPLEMENTADO. `NPCs.json` está vacío. Los 4 NPCs (Mercader, Sirena, Guardia, Princesa) están hardcodeados en `Partida.colocarNPCsFijos()`.

**Solución:** Definir estructura JSON para NPCs (nombre, tipo, sprite, diálogos, posición, zona) y cargarla desde `MapaLoader` o `LoadJSON`.

---

### RF-P7 — Pantalla de ajustes (Settings)

**Estado:** NO IMPLEMENTADO. `EstadoJuego.SETTINGS` existe en el enum pero no hay vista FXML ni controlador asociado.

---

### RF-P8 — Apilamiento de objetos en inventario

**Estado:** NO IMPLEMENTADO. `Objeto.cantidad` existe como campo pero `Inventario.addObjeto()` siempre crea una nueva entrada sin verificar si ya existe un objeto del mismo tipo.

---

### RF-P9 — Uso de Iterador en listas

**Estado:** NO IMPLEMENTADO. `Iterador` e `IteradorSE` existen pero nunca se usan. Todas las iteraciones sobre `Lista` se hacen con bucles indexados `for (int i = 0; i < list.getSize(); i++)`.

---

### RF-P10 — Ordenación del inventario

**Estado:** NO IMPLEMENTADO. No existe ningún mecanismo de ordenación, filtrado o categorización del inventario.

---

### RF-P11 — Límite de turnos por habitación

**Estado:** NO IMPLEMENTADO. `Zona.countTurnos` se incrementa correctamente pero no hay un límite configurable que cause derrota al superarlo (funcionalidad opcional del enunciado original).

---

### RF-P13 — Implementación de Árbol (CRÍTICO)

**Estado actual:** No existe ninguna estructura de árbol en el proyecto.

#### Interfaz `Arbol<T extends Comparable<T>>`

```java
public interface Arbol<T extends Comparable<T>> {
    void insertar(T elemento);
    boolean eliminar(T elemento);
    boolean contiene(T elemento);
    T getMinimo();
    T getMaximo();
    Lista<T> inOrden();
    Lista<T> preOrden();
    Lista<T> postOrden();
    int getAltura();
    int size();
    boolean isEmpty();
    void vaciar();
}
```

#### Implementación `ArbolBinarioBusqueda<T extends Comparable<T>>`

Estructura interna:

```java
private class NodoArbol {
    T valor;
    NodoArbol izquierdo, derecho;
    NodoArbol(T valor) { this.valor = valor; }
}

private NodoArbol raiz;
private int size;
```

**Operaciones fundamentales:**

| Operación | Estrategia | Coste |
|-----------|-----------|-------|
| `insertar(t)` | Comparar: si < ir a izquierdo, si > ir a derecho, si igual no insertar | O(h) ≈ O(log n) promedio, O(n) peor |
| `eliminar(t)` | 3 casos: hoja (eliminar), 1 hijo (reemplazar), 2 hijos (reemplazar con mínimo del subárbol derecho) | O(h) |
| `contiene(t)` | Recorrer comparando | O(h) |
| `inOrden()` | Izquierdo → raíz → derecho (recursivo o con pila) | O(n) |
| `preOrden()` | Raíz → izquierdo → derecho | O(n) |
| `postOrden()` | Izquierdo → derecho → raíz | O(n) |
| `getAltura()` | 1 + max(altura(izq), altura(der)) | O(n) |
| `getMinimo()` | Navegar siempre a izquierda | O(h) |
| `getMaximo()` | Navegar siempre a derecha | O(h) |

#### Uso 1: Árbol de Decisiones para IA de Enemigos

Reemplazar la lógica actual de `moverEnemigos()` (if-else anidados con decisiones aleatorias) por un árbol de decisiones.

**Estructura del nodo de decisión:**

```java
public class NodoDecision {
    String pregunta;
    Condicion condicion;       // evalúa true/false
    NodoDecision siTrue;       // subárbol si se cumple
    NodoDecision siFalse;      // subárbol si no se cumple
    AccionEnemigo accion;      // acción a ejecutar si es nodo hoja
}

public interface Condicion {
    boolean evaluar(Enemigo e, Partida p, Zona zona);
}

public interface AccionEnemigo {
    void ejecutar(Enemigo e, Partida p, Zona zona);
}
```

**Árbol equivalente a la IA actual:**

```
                  ¿Ha sido atacado?
                 /                  \
               Sí                    No
              /                       \
     ¿Está adyacente?             ¿Jugador cerca?
     /              \             (dist < 10)
    Sí               No            /           \
    |                |           Sí             No
 [Atacar]      [50% huir]    [Perseguir]    [No moverse]
               /         \
            [Huir]    [Atacar si adyacente]
```

**Nota de diseño:** Este árbol de decisiones no encaja en `ArbolBinarioBusqueda` porque las decisiones no tienen orden natural. Se implementará como clase separada `ArbolDecision` con nodos que tengan referencias `izquierdo`/`derecho` pero sin interfaz `Comparable`.

#### Uso 2: Árbol Binario de Búsqueda para Inventario

Usar `ArbolBinarioBusqueda<Objeto>` como índice de objetos en el inventario para búsqueda O(log n).

**Modificaciones necesarias en `Objeto.java`:**

```java
public class Objeto implements Comparable<Objeto> {
    @Override
    public int compareTo(Objeto o) {
        return this.nombre.compareTo(o.nombre);
    }
}
```

**Integración en `Inventario.java`:**

```java
public class Inventario {
    private Lista<Objeto> objetos;
    private ArbolBinarioBusqueda<Objeto> arbolObjetos;  // índice por nombre

    public boolean addObjeto(Objeto o) {
        // ... lógica actual ...
        arbolObjetos.insertar(o);  // mantener índice sincronizado
    }

    public boolean removeObjeto(Objeto o) {
        // ... lógica actual ...
        arbolObjetos.eliminar(o);
    }

    public boolean contiene(Objeto o) {
        return arbolObjetos.contiene(o);  // O(log n)
    }

    public Lista<Objeto> inOrden() {
        return arbolObjetos.inOrden();  // objetos ordenados alfabéticamente
    }
}
```

**Costes comparados:**

| Operación | Lista actual | Con árbol |
|-----------|-------------|-----------|
| `contiene()` | O(n) | O(log n) |
| `insertar()` | O(1) | O(log n) |
| `eliminar()` | O(n) | O(log n) |

#### Plan de Integración del Árbol

| Paso | Acción | Archivos |
|------|--------|----------|
| 1 | Crear `Arbol<T>`, `NodoArbol<T>`, `ArbolBinarioBusqueda<T>` | `TADs/Arbol.java`, `TADs/NodoArbol.java`, `TADs/ArbolBinarioBusqueda.java` |
| 2 | Hacer que `Objeto` implemente `Comparable<Objeto>` | `Modelo/Inventario/Objeto.java` |
| 3 | Añadir `ArbolBinarioBusqueda<Objeto>` a `Inventario` como índice | `Modelo/Inventario/Inventario.java` |
| 4 | Sincronizar árbol con add/remove en `Inventario` | `Inventario.java` |
| 5 | Crear `Condicion`, `AccionEnemigo`, `NodoDecision`, `ArbolDecision` | `Modelo/NPC/` o `Modelo/Core/IA/` |
| 6 | Refactorizar `moverEnemigos()` para usar árbol de decisiones | `PartidaMovimiento.java` |
| 7 | Añadir tests: inserción/eliminación/recorridos del árbol | Nuevo `ArbolBinarioBusquedaTest.java` |
| 8 | Añadir tests del árbol de decisiones | Nuevo `ArbolDecisionTest.java` |

---

## Requisitos No Funcionales (Pendientes)

### RNF-P1 — Validación de archivos JSON de mapa

`MapaLoader.crearDesdeTexto()` no valida que todas las filas del mapa ASCII tengan la misma longitud. Una fila más corta o más larga produce un `Matrix` irregular que puede causar `NullPointerException` en tiempo de ejecución.

**Solución:** Añadir chequeo y lanzar excepción descriptiva si las filas no son uniformes.

---

### RNF-P2 — Reinicio de contador de IDs de Objeto al cargar partida

`Objeto.contador` es estático y nunca se reinicia. Al cargar partida y luego crear nuevos objetos, pueden generarse IDs duplicados.

**Solución:** Almacenar el máximo ID usado en el JSON de guardado y restaurarlo con un setter: `Objeto.setContador(int maxId)`.

---

### RNF-P3 — Visualización de celdas accesibles desactualizada al cambiar rango

El renderizado de celdas iluminadas solo se calcula al inicio del turno. Si se usa una poción que aumenta el rango de movimiento, las celdas no se recalculan hasta el turno siguiente.

**Solución:** Refrescar `getCeldasAccesibles()` tras usar un objeto que modifique el rango.

---

### RNF-P4 — Indicador visual de agua cruzable en zona 6

La zona 6 (lago) tiene 12 coordenadas específicas hardcodeadas que requieren barca. No hay ninguna indicación visual de qué celdas de agua son cruzables.

**Solución:** Marcar las celdas de agua cruzables con un tile o superposición visual diferente.

---

## Mejoras y Correcciones a Implementar

### Críticas

| ID | Descripción | Archivos afectados | Prioridad |
|----|------------|-------------------|-----------|
| C01 | **Equipar objetos no aplica bonificaciones.** `equiparObjeto()` debe llamar a `jugador.equipar(o, s)` para que los bonos de ataque/defensa/rango surtan efecto. | `PartidaObjetosYCombate.java:215-251` | **CRÍTICO** |
| C02 | **Carga de enemigos desde JSON rota.** Serialización usa `-` y deserialización espera `\|`. Unificar delimitador. | `LoadJSON.java` | **CRÍTICO** |
| C03 | **Defensa aplicada dos veces al atacar enemigos.** `calcularHit()` ya descuenta defensa, `recibirAtaque()` la descuenta de nuevo. Usar `restarVidaDirecta()`. | `PartidaObjetosYCombate.java:56`, `Estadisticas.java` | **CRÍTICO** |
| C05 | **No existe estructura Árbol.** Crear `Arbol<T>`, `ArbolBinarioBusqueda<T>`, integrar en inventario y crear árbol de decisiones para IA. | Nuevo en `TADs/`, `Objeto.java`, `Inventario.java`, `PartidaMovimiento.java` | **CRÍTICO** |

### Altas

| ID | Descripción | Prioridad |
|----|------------|-----------|
| A01 | Deshardcodificar IDs de zona (0-9) y posiciones de NPCs/Gato/Llave. Mover a `configuracion.json`. | ALTA |
| A02 | Usar `zonasSinSpawn` desde `Configuracion.ZonaCfg` en lugar de comparaciones hardcodeadas. | ALTA |
| A03 | Hacer que `usarMapa()` funcione correctamente desde zona 0 (no encuentra SALIDA porque solo existe en zona 9). | ALTA |
| A04 | Cargar NPCs desde `NPCs.json`. Archivo actualmente vacío. | ALTA |
| A05 | Refactorizar BFS inline de `Partida.usarMapa()` para usar `AlgoritmosGrafo.bfsCamino()`. | ALTA |
| A06 | Sincronizar `ArbolBinarioBusqueda<Objeto>` con las operaciones de `Inventario`. | ALTA |

### Medias

| ID | Descripción | Prioridad |
|----|------------|-----------|
| M01 | Eliminar código muerto: `EstadoJuego.estadoGlobal`, `IteradorSE` (nunca usado), valores de `TipoCelda` no utilizados (`VACIO`, `INTERACTUABLE`, `OBJETO`, `ENEMIGO`, `NPC`, `GATO`). | MEDIA |
| M02 | Decidir si eliminar o integrar `TiendaController.java` y `tienda-view.fxml` (tienda funciona mediante VBox en `main-view.fxml`). | MEDIA |
| M03 | Parametrizar posición del Gato desde `configuracion.json` en lugar de hardcodeada. | MEDIA |
| M04 | Validar consistencia de filas en mapas JSON (mismo ancho en todas). | MEDIA |
| M05 | Actualizar celdas accesibles al usar objeto que modifique el rango de movimiento. | MEDIA |
| M06 | Implementar `ArbolDecision` para la IA de enemigos y refactorizar `moverEnemigos()`. | MEDIA |

### Bajas

| ID | Descripción | Prioridad |
|----|------------|-----------|
| B01 | Reiniciar `Objeto.contador` al cargar partida almacenando el max ID en JSON. | BAJA |
| B02 | Añadir indicador visual de agua cruzable en zona 6 (diferente tile). | BAJA |
| B03 | Usar campo `Objeto.cantidad` para apilar objetos del mismo tipo en inventario. | BAJA |
| B04 | Usar `Iterador` de las listas en lugar de bucles indexados. | BAJA |
| B05 | Mostrar en UI la distancia a la salida y número mínimo de habitaciones restantes de forma permanente (ya se calcula con `usarMapa()`). | BAJA |

---

## Resumen de Estado por Área

| Área | Estado |
|------|--------|
| **TADs: Lista, Cola, Pila, Matrix** | **Completo** |
| **TADs: Grafo genérico + Algoritmos** | **PENDIENTE** — interfaz, implementación, BFS/DFS |
| **TADs: Árbol binario de búsqueda** | **PENDIENTE** — interfaz, implementación, recorridos |
| **GrafoZonas** (refactorizado) | **Funcional pero mejorable** — acoplado y sin algoritmos propios |
| **Movimiento del jugador** | **Completo** |
| **Cambio de zona** | **Completo** |
| **Combate** | **Bug: defensa aplicada dos veces** |
| **Equipamiento** | **Roto: no aplica bonificaciones** |
| **Inventario / Objetos** | **Completo** (pendiente integrar árbol como índice) |
| **Guardar / Cargar JSON** | **Bug: enemigos no se cargan por delimitador incorrecto** |
| **Interfaz gráfica (JavaFX)** | **Completo** — menú, juego, pantalla final, renderizado |
| **IA de enemigos** | **Funcional** (pendiente árbol de decisiones) |
| **IA del Gato** | **Estático (intencionado)** — no requiere movimiento |
| **Pruebas JUnit** | **~110 tests** — faltan de Grafo genérico, Árbol, movimiento, log |
| **Log del sistema** | **Completo** |
| **Configuración desde JSON** | **Parcial** — campos `zonasSinSpawn`, `inventario.*`, `gato.probMovimiento` (obsoleto), `gato.zonasPermitidas` (obsoleto) sin mapear en Java |

---

## Diagrama de Dependencias entre las Nuevas Estructuras

```
TADs/
├── Arista<N>                    (interfaz)
├── AristaImpl<N>                (implementación)
├── Grafo<N, A extends Arista<N>> (interfaz)
├── GrafoAdyacencia<N, A>        (implementación con Lista<Lista<A>>)
├── AlgoritmosGrafo              (BFS y DFS estáticos)
│
├── Arbol<T extends Comparable<T>> (interfaz)
├── NodoArbol<T>                 (nodo interno para árbol)
├── ArbolBinarioBusqueda<T>      (implementación)
│
Modelo/
├── Mundo/
│   └── GrafoZonas ──usa──> GrafoAdyacencia<Integer, AristaImpl<Integer>>
│
├── Inventario/
│   ├── Objeto implements Comparable<Objeto>
│   └── Inventario ──tiene──> ArbolBinarioBusqueda<Objeto> (índice)
│
├── Core/
│   ├── Partida ──usa──> AlgoritmosGrafo.bfsCamino() (en lugar del BFS inline)
│   └── PartidaMovimiento ──usa──> ArbolDecision (IA enemigos)
│
└── IA/ (nuevo paquete opcional)
    ├── Condicion               (interfaz funcional)
    ├── AccionEnemigo           (interfaz funcional)
    ├── NodoDecision            (nodo de árbol de decisiones)
    └── ArbolDecision           (árbol binario de decisiones sin Comparable)
```
