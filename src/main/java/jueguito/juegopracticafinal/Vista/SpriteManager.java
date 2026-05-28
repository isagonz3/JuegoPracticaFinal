package jueguito.juegopracticafinal.Vista;

import javafx.scene.image.Image;

//Gestiona y proporciona los sprites del juego para representar entiedades, objetos y las zonas del juego
public class SpriteManager {

    //Carga un sprite desde una ruta de los recursos y devuelve la imagen correspondiente a la ruta
    public Image getSprite(String ruta) {
        try {
            return new Image(getClass().getResourceAsStream(ruta));
        } catch (Exception e) {
            System.err.println("No se pudo cargar sprite: " + ruta);
            return null;
        }
    }


    // SPRITES DE ENTIDADES

    //Sprite del jugador
    public Image getJugadorSprite() {
        return getSprite("/entidades/jugador_down00.png");
    }

    //Sprite del gato
    public Image getGatoSprite() {
        return getSprite("/entidades/gato_quieto.png");
    }

    //Sprite del enemigo
    public Image getEnemigoSprite() {
        return getSprite("/entidades/soldado_quieto.png");
    }


    // SPRITES DE NPC

    //Sprite del npc según el nombre
    public Image getNPCSprite(String nombre) {

        if (nombre == null) return getSprite("/entidades/NPC_1.png");

        switch (nombre.toLowerCase()) {

            case "mercader":
                return getSprite("/entidades/NPC_1.png");

            case "sirena":
                return getSprite("/entidades/NPC_2.png");

            case "guardia":
                return getSprite("/entidades/NPC_3.png");

            case "princesa":
            return getSprite("/entidades/princesa.png");

            default:
                return getSprite("/entidades/NPC_1.png");
        }
    }


    // SPRITES DE OBJETOS

    //Sprite de objetos
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


    // SPRITES DE ZONAS

    //Sprite de la zona del mapa
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