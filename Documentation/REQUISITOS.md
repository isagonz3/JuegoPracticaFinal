# REQUISITOS PROYECTO FINAL

## REQUISITOS FUNCIONALES

### RF1: Sistema de habitaciones:
+ Zonas creadas por matrices de diferentes dimensiones
+ Cada zona se encuentra conectada a otra
+ El jugador comienza la partida desde el interior de la casa del jugador
+ La salida se encuentra en el Interior del Castillo (fin del juego)

### RF2: Jugador:
+ Puede moverse dentro de la zona/matriz
+ Puede moverse entre las zonas/matrices (grafo)
+ Características: 
    + Puntos de vida
    + Puntos de ataque
    + Puntos de defensa
    + Velocidad/rango de movimiento
+ Cuenta con un inventario que le permite:
  + Coger objetos
  + Usar objetos
  + Equipar objetos

### RF3: Sistema de turnos:
+ 1 movimiento + 1 acción MÁXIMO por turno
+ Orden de turnos: 
  JUGADOR --> ENEMIGO --> GATO
+ Límite de turnos fijo: maxTurnos
+ Cuando el jugador entre en una zona por primera vez, se añade 1 turno extra a su contador de turnos

### RF4: Movimiento de Jugador:
+ El movimiento diagonal no está permitido, debe usar MÍNIMO 1 punto de movimiento por eje cartesiano
+ Las celdas disponibles en el rango de movimiento del jugador se marcan/iluminan
+ Las celdas a las que el jugador puede desplazarse se calculan mediante BFS en la matriz

### RF5: Enemigos:
+ Se mueven hacia el jugador para atacarle (BFS)
+ Una vez se encuentran en una celda adyacente al jugador, atacan
+ FÓRMULA DE DAÑO:
    ****DAÑO = max(0, ATAQUE*(RANDOM*2) - DEFENSA)**

### RF6: Gato:
+ OBJETIVO DEL JUEGO: el jugador debe encontrar al gato y llevarlo al castillo antes de llegar al límite de turnos
+ El gato no puede ser atacado ni dañado
+ Se mueve aleatoriamente en el mapa
+ Una vez el jugador lo encuentra, el gato caminará junto a él (respetando el orden de turnos)

### RF7: NPCs:
+ NPC Neutral: solo aporta diálogos
+ NPC Comerciante: permite al jugador hacer trueques con él
+ NPC Servicial: aporta pistas sobre el paradero del gato

### RF8: Objetos:

### RF9: Trampas:

### RF10: Condiciones Victoria:
+ El gato debe acompañar al jugador hasta el interior del castillo

### RF11: Condiciones Derrota:
+ La vida del jugador es <= 0
+ El jugador ha superado el límite de turnos máximo

### RF12: Persistencia de datos JSON:
+ Permitir guardar el estado completo de la partida
+ Permitir cargar una partida previamente guardada
+ Guardar la configuración inicial del juego

### RF13: Interfaz gráfica JavaFX:
+ Habitaciones/zonas mediante GridPane
+ Panel de información del estado del jugador (vida restante, puntos de ataque...)
+ Panel de acciones disponibles
+ Log de acciones
+ Contador de turnos 

### RF14: Log del sistema:
+ Registrar todas las acciones del juego
+ Mostrar el log al final de la partida


## REQUISITOS NO FUNCIONALES

### RNF1: Rendimiento
+ BFS en matriz
+ BFS en grafo 

### RNF2: Estructuras de datos propias
+ Implementar: Lista enlazada, Pila, Cola, Lista circular, Árbol, Grafo, Matriz
+ PROHIBIDO: ArrayList, HashMap, LinkedList, java.util.collections

### RNF3: Manejo de errores
+ Movimiento inválido → excepción personalizada
+ Archivo JSON no existe → mensaje de error
+ JSON corrupto → captura y recuperación

### RNF4: Separación de responsabilidades
+ Modelo (lógica): sin dependencias de JavaFX
+ Vista: JavaFX + FXML
+ Controlador: coordina modelo y vista

### RNF5: Documentación
+ UML: casos de uso, clases, secuencia, estados, actividad
+ Justificación de cada estructura de datos usada
+ Diario de uso de IA (prompts, resultados, crítica)

### RNF6: Testing
+ JUnit para todas las clases no visuales
+ Tests de estructuras de datos
+ Tests de lógica de juego
+ Tests de guardar/cargar JSON
