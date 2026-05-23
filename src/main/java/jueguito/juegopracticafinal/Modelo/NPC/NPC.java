package jueguito.juegopracticafinal.Modelo.NPC;

import jueguito.juegopracticafinal.TADs.Lista;

public class NPC {
    private String nombre;
    private TipoNPC tipo;
    private Lista<String> dialogos;
    private Lista<Tradeo> tradeos;
    private String infoGato;
    private int dialogoActual;

    public NPC(String nombre, TipoNPC tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dialogos = new Lista<>();
        this.tradeos = new Lista<>();
        this.infoGato = "";
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

    public String getInfoGato() {
        return infoGato;
    }

    public void setInfoGato(String infoGato) {
        this.infoGato = infoGato;
    }

    public void addDialogo(String dialogo){
        this.dialogos.add(dialogo);
    }

    public void addTradeo(Tradeo tradeo){
        this.tradeos.add(tradeo);
    }

    public String hablar(){
        if(dialogos.isEmpty() || dialogoActual >= dialogos.getSize()){
            return "...";
        }

        String frase = dialogos.get(dialogoActual);
        dialogoActual++;
        return frase;
    }

    public void restartDialogo(){
        this.dialogoActual = 0;
    }

    public boolean tieneInfoGato(){
        return infoGato != null && !infoGato.isEmpty();
    }

    public boolean comerciar(){
        return tipo == TipoNPC.COMERCIANTE && tradeos.getSize() > 0;
    }
}
