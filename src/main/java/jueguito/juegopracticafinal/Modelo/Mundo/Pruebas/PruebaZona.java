package jueguito.juegopracticafinal.Modelo.Mundo.Pruebas;

import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Lista;

public class PruebaZona {

    public static void main(String[] args) {

        System.out.println("=== PRUEBAS ZONA ===");

        // Crear zona
        Zona zona = new Zona(0, "ZonaTest", 5, 5);

        System.out.println("Zona creada: " + zona.getNombreZona());
        System.out.println("ID: " + zona.getIdZona());
        System.out.println("Rows: " + zona.getRows());
        System.out.println("Cols: " + zona.getCols());

        // Crear celdas
        Celda c1 = new Celda(TipoCelda.SUELO);
        Celda c2 = new Celda(TipoCelda.AGUA);
        Celda c3 = new Celda(TipoCelda.SUELO);

        // Colocar celdas en la matriz
        zona.setCelda(2, 2, c1);
        zona.setCelda(2, 3, c2);
        zona.setCelda(3, 2, c3);

        // Probar getCelda
        System.out.println("\nCelda (2,2): " + zona.getCelda(2, 2).getTipoCelda());

        // Probar vecinas transitables
        Lista<Celda> vecinas = zona.getCeldasVecinas(2, 2);

        System.out.println("\nVecinas transitables de (2,2):");
        for (int i = 0; i < vecinas.getSize(); i++) {
            Celda c = vecinas.get(i);
            System.out.println("- " + c.getTipoCelda());
        }

        // Probar accesibles con rango BFS
        Lista<Celda> accesibles = zona.getCeldasAccesibles(2, 2, 2);

        System.out.println("\nCeldas accesibles (rango 2):");
        for (int i = 0; i < accesibles.getSize(); i++) {
            Celda c = accesibles.get(i);
            System.out.println("- " + c.getTipoCelda());
        }

        // Probar spawn jugador
        Posicion spawn = new Posicion(1, 1);
        zona.setSpawnJugador(spawn);

        System.out.println("\nSpawn jugador: " + zona.getSpawnJugador());

        // Probar celdas accesibles totales
        System.out.println("Celdas transitables totales: " + zona.numCeldasAccesibles());

        // Probar puertas
        zona.addPuerta(c1);
        zona.addPuerta(c3);

        System.out.println("\nPuertas en zona:");
        for (int i = 0; i < zona.getPuertas().getSize(); i++) {
            System.out.println("- puerta celda tipo: " +
                    zona.getPuertas().get(i).getTipoCelda());
        }

        System.out.println("\n=== FIN PRUEBAS ===");
    }
}