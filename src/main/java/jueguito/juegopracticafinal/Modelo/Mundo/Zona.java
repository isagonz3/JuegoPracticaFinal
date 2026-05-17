package jueguito.juegopracticafinal.Modelo.Mundo;

import jueguito.juegopracticafinal.TADs.Cola;
import jueguito.juegopracticafinal.TADs.Lista;
import jueguito.juegopracticafinal.TADs.Matrix;

public class Zona {

   private Matrix<Celda> matrix;
   private String nombreZona;
   private int idZona;
   private Lista<Posicion> spawnEnemigos;
   private Lista<Posicion> spawnObjects;
   private Lista<Posicion> spawnNPCs;
   private Lista<Celda> puertas;
   private int countTurnos;
   private boolean visitada;


   public Zona(int idZona, String nombreZona, int rows, int cols){
      this.nombreZona = nombreZona;
      this.idZona = idZona;
      this.countTurnos = 0;
      this.visitada = false;
      this.spawnEnemigos = new Lista<>();
      this.spawnObjects = new Lista<>();
      this.spawnNPCs = new Lista<>();
      this.puertas = new Lista<>();
      this.matrix = new Matrix<>(rows, cols);
   }

   public int getIdZona() {
      return idZona;
   }

   public String getNombreZona() {
      return nombreZona;
   }

   public Matrix<Celda> getMatrix() {
      return matrix;
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

   public Lista<Posicion> getSpawnEnemigos() {
      return spawnEnemigos;
   }

   public Lista<Posicion> getSpawnObjects() {
      return spawnObjects;
   }

   public Lista<Posicion> getSpawnNPCs() {
      return spawnNPCs;
   }

   public Celda getCelda(int row, int col){
      return matrix.get(row, col);
   }

   public void setCelda(int row, int col, Celda celda){
      matrix.set(row, col, celda);
   }

   public Lista<Celda> getPuertas() {
      return puertas;
   }

   public boolean esValida(int row, int col){
      return matrix.esValida(row,col);
   }

   public Lista<Celda> getCeldasVecinas(int row, int col) {
      Lista<Celda> vecinas = new Lista<>();
      int[][] coordenadas = {{-1,0}, {1,0}, {0,-1}, {0,1}}; //Norte, Sur, Oeste, Este

       for (int[] coordenada : coordenadas) {

          int newrow = row + coordenada[0];
          int newcol = col + coordenada[1];

          if (matrix.esValida(newrow, newcol)) {
             Celda celda = matrix.get(newrow, newcol);
             if (celda != null && celda.isTransitable()) {
                vecinas.add(celda);
             }
          }

       }
      return vecinas;
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
                  if (celda != null && celda.isTransitable()) {
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
}
