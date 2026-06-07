package jueguito.juegopracticafinal.Modelo.Core;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.InputStreamReader;

//Gestiona la configuración global del juego cargando parámetros desde un archivo JSON o usando valores por defecto
public class Configuracion {

    //ATRIBUTOS

    private static Configuracion INSTANCE;
    public JugadorCfg jugador = new JugadorCfg();
    public GatoCfg gato = new GatoCfg();
    public EnemigosCfg enemigos = new EnemigosCfg();
    public ObjetosCfg objetos = new ObjetosCfg();
    public TurnoCfg turno = new TurnoCfg();
    public ZonaCfg zona = new ZonaCfg();
    public UiCfg ui = new UiCfg();


    //MÉTODOS

    //Obtiene la instancia de LA configuración cargándola desde JSON si es necesario
    public static Configuracion get() {
        if (INSTANCE == null) {
            try (var is = Configuracion.class.getResourceAsStream("/datosJSON/configuracion.json")) {
                if (is != null) {
                    INSTANCE = new Gson().fromJson(new InputStreamReader(is), Configuracion.class);
                } else {
                    INSTANCE = new Configuracion();
                }
            } catch (Exception e) {
                System.err.println("[Configuracion] Error: " + e.getMessage());
                e.printStackTrace();
                INSTANCE = new Configuracion();
            }
        }
        return INSTANCE;
    }

    //Configuración del jugador
    public static class JugadorCfg {
        public int vidaMax = 20, ataqueBase = 5, defensaBase = 2, rangoMov = 50;
    }

    //Configuración del gato
    public static class GatoCfg {
        public int vida = 10, ataque = 0, defensa = 0, rangoMov = 10;
        public int[] zonasPermitidas = {2, 4, 6, 7, 8};
    }

    //Configuración de enemigos
    public static class EnemigosCfg {
        public int vidaBase = 5, ataqueBase = 2, defensaBase = 1, rangoMov = 4, min = 3, max = 6;
    }

    //Configuración de objetos
    public static class ObjetosCfg {
        public int min = 1, max = 3;
    }

    //Configuración de turnos
    public static class TurnoCfg {
        public int maxTurnos = 100;
    }

    //Configuración de zonas del mundo
    public static class ZonaCfg {
        public int zonaInicial = 0, numZonas = 10;
    }

    //Configuración de la interfaz de usuario
    public static class UiCfg {
        public int tileSize = 16, anchoVentana = 1100, altoVentana = 700;
    }
}