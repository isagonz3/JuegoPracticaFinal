package jueguito.juegopracticafinal.TADs.Pruebas;

import jueguito.juegopracticafinal.TADs.Matrix;

public class PruebaMatrix {

    public static void main(String[] args) {

        System.out.println("PRUEBA MATRIX");

        Matrix<Integer> matriz = new Matrix<>(3, 3);

        // set
        matriz.set(0, 0, 10);
        matriz.set(1, 1, 20);
        matriz.set(2, 2, 30);

        // get
        System.out.println("Elemento [0][0]:");
        System.out.println(matriz.get(0, 0));

        System.out.println("Elemento [1][1]:");
        System.out.println(matriz.get(1, 1));

        System.out.println("Elemento [2][2]:");
        System.out.println(matriz.get(2, 2));

        // dimensiones
        System.out.println("Numero filas:");
        System.out.println(matriz.getNumRows());

        System.out.println("Numero columnas:");
        System.out.println(matriz.getNumCols());

        // validacion
        System.out.println("Posicion valida (1,1)?");
        System.out.println(matriz.esValida(1,1));

        System.out.println("Posicion valida (5,5)?");
        System.out.println(matriz.esValida(5,5));
    }
}