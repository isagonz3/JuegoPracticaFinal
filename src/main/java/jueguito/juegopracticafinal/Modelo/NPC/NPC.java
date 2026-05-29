package jueguito.juegopracticafinal.Modelo.NPC;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.TADs.Lista;

//Representa un personaje no jugable del juego almacenando su nombre, tipo, diálogos y sprite asociado
public class NPC {

    //ATRIBUTOS

    private String nombre;
    private TipoNPC tipo;
    private Lista<String> dialogos;
    private int dialogoActual;
    private Partida partida;

    private String sprite;


    //CONSTRUCTORES

    //Es el constructor del NPC que inicializa su nombre, tipo y la lista vacía de diálogos
    public NPC(String nombre, TipoNPC tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dialogos = new Lista<>();
        this.dialogoActual = 0;
    }


    //Es el constructor del NPC que inicializa su nombre, tipo y una lista de diálogos personalizada
    public NPC(String nombre, TipoNPC tipo, Lista<String> dialogos) {
        this(nombre, tipo);
        this.dialogos = dialogos;
    }


    //MÉTODOS

    //Gestiona el diálogo del NPC devolviendo frases según el tipo, nombre y progreso de conversación
    public String hablar() {

        if (tipo == TipoNPC.ALDEANO) {

            if (nombre.equals("Sirena")) {
                return "Parece que para llegar a la llave vas a necesitar una barca";
            }

            if (nombre.equals("Guardia")) {
                return "Necesitas conseguir una llave para abrir el castillo";
            }

            if (dialogos.isEmpty() || dialogoActual >= dialogos.getSize()) {
                return "...";
            }

            String frase = dialogos.get(dialogoActual);
            dialogoActual++;
            return frase;
        }
        return "...";
    }


    //GETTERS Y SETTERS

    //Obtiene el nombre del NPC
    public String getNombre() {
        return nombre;
    }

    //Obtiene el tipo del NPC
    public TipoNPC getTipo() {
        return tipo;
    }

    //Asigna el sprite asociado al NPC
    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
}