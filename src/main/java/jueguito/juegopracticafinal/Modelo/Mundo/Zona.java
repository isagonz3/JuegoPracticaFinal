package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.TADs.Matrix;
import jueguito.juegopracticafinal.TADs.Pila;

public class Zona {

   //ATRIBUTOS

   private Matrix<Celda> matrix;
   private String nombreZona;
   private int idZona;
   private Posicion spawnJugador;
   private Lista<Posicion> spawnEnemigos;
   private Lista<Posicion> spawnObjects;
   private Lista<Posicion> spawnNPCs;
   private Lista<Celda> puertas;
   private int countTurnos;
   private boolean visitada;


   public Zona(int idZona, String nombreZona, int rows, int cols){
      this.idZona = idZona;
      this.nombreZona = nombreZona;
      this.matrix = new Matrix<>(rows, cols);
      this.countTurnos = 0;
      this.visitada = false;
      this.spawnJugador = null;
      this.spawnEnemigos = new Lista<>();
      this.spawnObjects = new Lista<>();
      this.spawnNPCs = new Lista<>();
      this.puertas = new Lista<>();

   }

   public Zona(int idZona, String nombreZona, Celda[][] celdas) {
      this.idZona = idZona;
      this.nombreZona = nombreZona;
      int rows = celdas.length;
      int cols = celdas[0].length;
      this.matrix = new Matrix<>(rows, cols);

      for (int i = 0; i < rows; i++){
         for (int j = 0; j < cols; j++) {
            Celda celda = celdas[i][j];
            celda.setRow(i);
            celda.setCol(j);
            matrix.set(i, j, celda);
         }
      }

      this.countTurnos = 0;
      this.visitada = false;
      this.spawnJugador = null;
      this.spawnEnemigos = new Lista<>();
      this.spawnObjects = new Lista<>();
      this.spawnNPCs = new Lista<>();
      this.puertas = new Lista<>();
   }


   //MÉTODOS

   public boolean esValida(int row, int col){
      return matrix.esValida(row,col);
   }

   public void addPuerta(Celda celda){
      puertas.add(celda);
   }

   public Lista<Celda> getPuertas(){
      return puertas;
   }


   //GETTERS Y SETTERS

   public int getIdZona() {
      return idZona;
   }

   public void setIdZona(int idZona) {
      this.idZona = idZona;
   }

   public String getNombreZona() {
      return nombreZona;
   }

   public void setNombreZona(String nombreZona) {
      this.nombreZona = nombreZona;
   }

   public int getRows(){
      return matrix.getNumRows();
   }

   public int getCols(){
      return matrix.getNumCols();
   }

   public int getCountTurnos(){
      return countTurnos;
   }

   public void setCountTurnos(int countTurnos){
      this.countTurnos = countTurnos;
   }

   public boolean isVisitada() {
      return visitada;
   }

   public void setVisitada(boolean visitada) {
      this.visitada = visitada;
   }

   public void setSpawnJugador(Posicion posicion) {
      this.spawnJugador = posicion;
   }

   public Posicion getSpawnJugador() {
      return spawnJugador;
   }

   public Lista<Posicion> getSpawnEnemigos() {
      return spawnEnemigos;
   }

   public Lista<Posicion> getSpawnObjetos() {
      return spawnObjects;
   }

   public Celda getCelda(int row, int col){
      return matrix.get(row, col);
   }

   //Obtiene las celdas accesibles desde una posición concreta dentro de un rango utilizando un recorrido en anchura
   public Lista<Celda> getCeldasAccesibles(int row, int col, int rango) {
      Lista<Celda> accesibles = new Lista<>();
      if(rango <= 0 || !esValida(row,col)){
         return accesibles;
      }
      boolean[][] visitadas = new boolean[matrix.getNumRows()][matrix.getNumCols()];
      Cola<int[]> cola = new Cola<>();
      cola.enqueue(new int[] {row,col,0});
      visitadas[row][col] = true;
      while(!cola.isEmpty()){
         int[] actual = cola.dequeue();
         int colaRow = actual[0];
         int colaCol = actual[1];
         int colaDist = actual[2];
         if(colaDist > 0 && colaDist <= rango){
            accesibles.add(matrix.get(colaRow,colaCol));
         }
         if(colaDist < rango){
            int[][] coordenadas = {
                    {-1,0},
                    {1,0},
                    {0,-1},
                    {0,1}
            }; //Norte, Sur, Oeste, Este
            for (int[] coordenada : coordenadas) {
               int newrow = colaRow + coordenada[0];
               int newcol = colaCol + coordenada[1];
               if (matrix.esValida(newrow, newcol) && !visitadas[newrow][newcol]) {
                  Celda celda = matrix.get(newrow, newcol);
                  if (celda != null &&
                          celda.isTransitable() &&
                          (!celda.isOcupada() || celda.tieneObjeto())) {
                     visitadas[newrow][newcol] = true;
                     cola.enqueue(
                             new int[]{
                                     newrow,
                                     newcol,
                                     colaDist + 1
                             }
                     );
                  }
               }
            }
         }
      }
      return accesibles;
   }

   public Lista<Celda> getCaminoMinimo(int startRow, int startCol, int endRow, int endCol) {
      Lista<Celda> camino = new Lista<>();
      if (!esValida(startRow, startCol) || !esValida(endRow, endCol)) {
         return camino;
      }

      int rows = matrix.getNumRows();
      int cols = matrix.getNumCols();
      boolean[][] visitadas = new boolean[rows][cols];
      int[][][] padre = new int[rows][cols][2];

      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
            padre[i][j][0] = -1;
            padre[i][j][1] = -1;
         }
      }

      Cola<int[]> cola = new Cola<>();
      cola.enqueue(new int[]{startRow, startCol});
      visitadas[startRow][startCol] = true;

      int[][] direcciones = {{-1,0}, {1,0}, {0,-1}, {0,1}};
      boolean encontrado = false;

      while (!cola.isEmpty() && !encontrado) {
         int[] actual = cola.dequeue();
         int r = actual[0];
         int c = actual[1];

         for (int[] dir : direcciones) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            if (matrix.esValida(nr, nc) && !visitadas[nr][nc]) {
               Celda celda = matrix.get(nr, nc);
               if (celda != null && celda.isTransitable()) {
                  visitadas[nr][nc] = true;
                  padre[nr][nc][0] = r;
                  padre[nr][nc][1] = c;
                  cola.enqueue(new int[]{nr, nc});

                  if (nr == endRow && nc == endCol) {
                     encontrado = true;
                     break;
                  }
               }
            }
         }
      }

      if (encontrado) {
         Pila<Celda> pila = new Pila<>();
         int r = endRow, c = endCol;
         while (r != startRow || c != startCol) {
            pila.push(matrix.get(r, c));
            int pr = padre[r][c][0];
            int pc = padre[r][c][1];
            r = pr;
            c = pc;
         }
         pila.push(matrix.get(startRow, startCol));
         while (!pila.isEmpty()) {
            camino.add(pila.pop());
         }
      }
      return camino;
   }
}