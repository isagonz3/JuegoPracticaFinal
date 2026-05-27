package jueguito.juegopracticafinal.Modelo.Core;

import com.google.gson.Gson;

import java.io.FileReader;

public class Configuracion {
    private static Configuracion INSTANCE;

    public JugadorCfg jugador = new JugadorCfg();
    public GatoCfg gato = new GatoCfg();
    public EnemigosCfg enemigos = new EnemigosCfg();
    public ObjetosCfg objetos = new ObjetosCfg();
    public TurnoCfg turno = new TurnoCfg();
    public InventarioCfg inventario = new InventarioCfg();
    public ZonaCfg zona = new ZonaCfg();
    public UiCfg ui = new UiCfg();

    public static Configuracion get() {
        if (INSTANCE == null) {
            try (FileReader r = new FileReader("src/main/resources/datosJSON/configuracion.json")) {
                INSTANCE = new Gson().fromJson(r, Configuracion.class);
            } catch (Exception e) {
                INSTANCE = new Configuracion();
            }
        }
        return INSTANCE;
    }

    public static class JugadorCfg {
        public int vidaMax = 20, ataqueBase = 5, defensaBase = 2, rangoMov = 50;
    }
    public static class GatoCfg {
        public int vida = 10, ataque = 0, defensa = 0, rangoMov = 10;
        public double probMovimiento = 0.3;
        public int[] zonasPermitidas = {2, 4, 6, 7, 8};
    }
    public static class EnemigosCfg {
        public int vidaBase = 5, ataqueBase = 2, defensaBase = 1, rangoMov = 4, min = 3, max = 6;
    }
    public static class ObjetosCfg {
        public int min = 1, max = 3;
    }
    public static class TurnoCfg {
        public int maxTurnos = 100;
    }
    public static class InventarioCfg {
        public int maxItems = 10, slotsEquip = 2;
    }
    public static class ZonaCfg {
        public int[] zonasSinSpawn = {0, 5};
        public int zonaCastillo = 9, zonaInicial = 0, numZonas = 10;
    }
    public static class UiCfg {
        public int tileSize = 16, anchoVentana = 960, altoVentana = 576;
    }
}
