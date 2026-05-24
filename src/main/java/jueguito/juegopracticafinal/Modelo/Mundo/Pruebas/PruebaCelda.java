package jueguito.juegopracticafinal.Modelo.Mundo.Pruebas;

import jueguito.juegopracticafinal.Modelo.Entidades.Entidad;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Puerta;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;
import jueguito.juegopracticafinal.Modelo.NPC.NPC;

public class PruebaCelda {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS DE LA CLASE CELDA ===");

        // Crear celda
        Celda celda = new Celda(TipoCelda.SUELO);

        // Probar setters de posición
        celda.setRow(2);
        celda.setCol(3);

        System.out.println("Fila: " + celda.getRow());
        System.out.println("Columna: " + celda.getCol());

        // Probar tipo de celda
        System.out.println("Tipo de celda: " + celda.getTipoCelda());

        celda.setTipoCelda(TipoCelda.AGUA);
        System.out.println("Nuevo tipo: " + celda.getTipoCelda());

        // Probar transitabilidad
        System.out.println("¿Es transitable?: " + celda.isTransitable());

        // Probar puerta
        Puerta puerta = new Puerta();

        celda.setPuerta(puerta);

        System.out.println("¿Tiene puerta?: " + celda.tienePuerta());
        System.out.println("Puerta: " + celda.getPuerta());

        // Probar entidad
        Entidad entidad = null; // Sustituir por implementación real

        celda.setEntidad(entidad);

        System.out.println("¿Tiene entidad?: " + celda.tieneEntidad());
        System.out.println("¿Está ocupada?: " + celda.isOcupada());

        // Probar objeto
        Objeto objeto = null; // Sustituir por implementación real

        celda.setObjeto(objeto);

        System.out.println("¿Tiene objeto?: " + celda.tieneObjeto());

        // Probar NPC
        NPC npc = null; // Sustituir por implementación real

        celda.setNpc(npc);

        System.out.println("¿Tiene NPC?: " + celda.tieneNPC());

        // Crear otra celda para probar distancia y adyacencia
        Celda otraCelda = new Celda(TipoCelda.SUELO);

        otraCelda.setRow(2);
        otraCelda.setCol(4);

        System.out.println("¿Es adyacente?: " + celda.esAdyacente(otraCelda));

        System.out.println("Distancia: " + celda.getDistancia(otraCelda));

        // Limpiar celda
        celda.clear();

        System.out.println("=== DESPUÉS DE CLEAR ===");

        System.out.println("¿Tiene puerta?: " + celda.tienePuerta());
        System.out.println("¿Tiene entidad?: " + celda.tieneEntidad());
        System.out.println("¿Tiene objeto?: " + celda.tieneObjeto());
        System.out.println("¿Tiene NPC?: " + celda.tieneNPC());

        System.out.println("=== FIN DE PRUEBAS ===");
    }
}