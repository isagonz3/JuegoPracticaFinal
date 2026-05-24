package jueguito.juegopracticafinal.Modelo.Turno;

public enum EstadoJuego {
    EN_CURSO, VICTORIA, DERROTA, MENU, SETTINGS;


    private static EstadoJuego estadoGlobal = MENU;
    public static EstadoJuego getEstadoGlobal() {
        return estadoGlobal;
    }
    public static void setEstadoGlobal(EstadoJuego estado) {
        estadoGlobal = estado;
    }
}