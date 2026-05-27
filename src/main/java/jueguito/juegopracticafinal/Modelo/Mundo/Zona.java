package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.TADs.Matrix;

public class Zona {
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

   public boolean esValida(int row, int col){
      return matrix.esValida(row,col);
   }

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
            int[][] coordenadas = {{-1,0}, {1,0}, {0,-1}, {0,1}}; //Norte, Sur, Oeste, Este
            for (int[] coordenada : coordenadas) {
               int newrow = colaRow + coordenada[0];
               int newcol = colaCol + coordenada[1];

               if (matrix.esValida(newrow, newcol) && !visitadas[newrow][newcol]) {
                  Celda celda = matrix.get(newrow, newcol);

                  if (celda != null && celda.isTransitable() && (!celda.isOcupada() || celda.tieneObjeto())) {
                     visitadas[newrow][newcol] = true;
                     cola.enqueue(new int[]{newrow, newcol, colaDist + 1});
                  }
               }
            }
         }
      }
      return accesibles;
   }

   public int numCeldasAccesibles(){
      int num = 0;
      for(int i = 0; i < matrix.getNumRows(); i++){
         for(int j = 0; j < matrix.getNumCols(); j++){
            if(matrix.get(i,j).isTransitable()){
               num++;
            }
         }
      }
      return num;
   }

   public void addPuerta(Celda celda){
      puertas.add(celda);
   }

   public Lista<Celda> getCaminoMinimo(int origenRow, int origenCol,int destinoRow, int destinoCol){
      if(!esValida(origenRow,origenCol) || !esValida(destinoRow,destinoCol)){
         return new Lista<>();
      }

      int rows = matrix.getNumRows();
      int cols = matrix.getNumCols();

      boolean[][] visitadas = new boolean[rows][cols];
      int[][] raizRow = new int[rows][cols];
      int[][] raizCol = new int[rows][cols];

      Cola<int[]> cola = new Cola<>();

      for(int i = 0; i < rows; i++){
         for(int j = 0; j < cols; j++){
            raizRow[i][j] = raizCol[i][j] = -1;
         }
      }

      cola.enqueue(new int[] {origenRow, origenCol});
      visitadas[origenRow][origenCol] = true;
      int[][] coordenadas = {{-1,0}, {1,0}, {0,-1}, {0,1}}; //Norte, Sur, Oeste, Este

      while(!cola.isEmpty()){
         int[] actual = cola.dequeue();
         int row = actual[0];
         int col = actual[1];

         if(row == destinoRow && col == destinoCol){
            Lista<Celda> camino = new Lista<>();
            int newRow = destinoRow;
            int newCol = destinoCol;

            while(newRow != -1){
               camino.addFirst(matrix.get(newRow,newCol));
               int r = raizRow[newRow][newCol];
               int c = raizCol[newRow][newCol];
               newRow = r;
               newCol = c;
            }
            return camino;
         }
         for(int[] dir : coordenadas){
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if(esValida(newRow,newCol) && !visitadas[newRow][newCol]){
               Celda celda = matrix.get(newRow, newCol);
               if(celda != null && celda.isTransitable()){
                   visitadas[newRow][newCol] = true;
                   raizRow[newRow][newCol] = row;
                   raizCol[newRow][newCol] = col;
                  cola.enqueue(new int[]{newRow, newCol});
               }
            }
         }
      }
      return new Lista<>();
   }
}