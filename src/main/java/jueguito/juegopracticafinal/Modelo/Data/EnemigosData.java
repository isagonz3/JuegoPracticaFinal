package jueguito.juegopracticafinal.Modelo.Data;

public final class EnemigosData {
    public static final int VIDA_BASE = 5;
    public static final int ATAQUE_BASE = 2;
    public static final int DEFENSA_BASE = 1;
    public static final int RANGO_MOV_E = 4;

    public static int getVidaRandom() { return VIDA_BASE + (int)(Math.random() * 10); }
    public static int getAtaqueRandom() { return ATAQUE_BASE + (int)(Math.random() * 3); }
    public static int getDefensaRandom() { return DEFENSA_BASE + (int)(Math.random() * 2); }
}
