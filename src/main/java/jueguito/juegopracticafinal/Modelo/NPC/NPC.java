package jueguito.juegopracticafinal.Modelo.NPC;

import jueguito.juegopracticafinal.TADs.Lista;

public class NPC {
    private String nombre;
    private TipoNPC tipo;
    private Lista<String> dialogos;
    private int dialogoActual;

    private String sprite;

    public NPC(String nombre, TipoNPC tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dialogos = new Lista<>();
        this.dialogoActual = 0;
    }

    public NPC(String nombre, TipoNPC tipo, Lista<String> dialogos) {
        this(nombre, tipo);
        this.dialogos = dialogos;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoNPC getTipo() {
        return tipo;
    }

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

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
}