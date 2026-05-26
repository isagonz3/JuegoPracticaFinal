package jueguito.juegopracticafinal.Modelo.NPC;

import jueguito.juegopracticafinal.TADs.Lista;

public class NPC {
    private final String nombre;
    private final TipoNPC tipo;
    private Lista<String> dialogos;
    private final Lista<Tradeo> tradeos;
    private int dialogoActual;

    private String sprite;
    private String infoGato;

    public NPC(String nombre, TipoNPC tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dialogos = new Lista<>();
        this.tradeos = new Lista<>();
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

    public Lista<Tradeo> getTradeos() {
        return tradeos;
    }

    public void addDialogo(String dialogo){
        this.dialogos.add(dialogo);
    }

    public void addTradeo(Tradeo tradeo){
        this.tradeos.add(tradeo);
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

    public void restartDialogo(){
        this.dialogoActual = 0;
    }

    public boolean comerciar(){
        return tipo == TipoNPC.COMERCIANTE && tradeos.getSize() > 0;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public boolean tieneInfoGato() {
        return infoGato != null && !infoGato.isEmpty();
    }

    public String getInfoGato() {
        return infoGato;
    }

    public void setInfoGato(String infoGato) {
        this.infoGato = infoGato;
    }
}
