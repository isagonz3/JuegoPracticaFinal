package jueguito.juegopracticafinal.Modelo.Vista;

import javafx.scene.image.Image;

public class SpriteManager {

    public Image getSprite(String ruta) {
        try {
            return new Image(getClass().getResourceAsStream(ruta));
        } catch (Exception e) {
            System.err.println("No se pudo cargar sprite: " + ruta);
            return null;
        }
    }

    // ============================================================
    // SPRITES DE ENTIDADES
    // ============================================================

    public Image getJugadorSprite() {
        return getSprite("/entidades/jugador_down00.png");
    }

    public Image getGatoSprite() {
        return getSprite("/entidades/gato_quieto.png");
    }

    public Image getEnemigoSprite() {
        return getSprite("/entidades/soldado_quieto.png");
    }

    // ============================================================
    // SPRITES DE NPC
    // ============================================================

    public Image getNPCSprite(String nombre) {

        if (nombre == null) return getSprite("/entidades/NPC_1.png");

        switch (nombre.toLowerCase()) {

            case "mercader":
                return getSprite("/entidades/NPC_1.png");

            case "sirena":
                return getSprite("/entidades/NPC_2.png");

            case "guardia":
                return getSprite("/entidades/NPC_3.png");

            default:
                return getSprite("/entidades/NPC_1.png");
        }
    }

    // ============================================================
    // SPRITES DE OBJETOS
    // ============================================================

    public Image getObjetoSprite(String nombre) {

        if (nombre == null) return getSprite("/objetos/pocionVida.png");

        switch (nombre.toLowerCase()) {

            case "espada":
                return getSprite("/objetos/espada.png");

            case "escudo":
                return getSprite("/objetos/escudo.png");

            case "llave":
                return getSprite("/objetos/llave.png");

            case "gema":
                return getSprite("/objetos/gema.png");

            case "pocion de vida":
                return getSprite("/objetos/pocionVida.png");

            case "pocion de ataque":
                return getSprite("/objetos/pocionRango.png");

            case "barca":
            case "barco":
                return getSprite("/objetos/barco.png");

            case "mapa":
                return getSprite("/objetos/mapa.png");

            default:
                return getSprite("/objetos/pocionVida.png");
        }
    }

    // ============================================================
    // SPRITES DE ZONAS
    // ============================================================

    public Image getZonaBackground(int idZona) {

        String[] zonas = {
                "0_InteriorHabitacion",
                "1_ExteriorHabitacion",
                "2_BosqueOeste",
                "3_Aldea",
                "4_ExteriorTienda",
                "5_InteriorTienda",
                "6_Lago",
                "7_BosqueEste",
                "8_ExteriorCastillo",
                "9_InteriorCastillo"
        };

        if (idZona < 0 || idZona >= zonas.length) return null;

        return getSprite("/zonas/" + zonas[idZona] + ".png");
    }
}