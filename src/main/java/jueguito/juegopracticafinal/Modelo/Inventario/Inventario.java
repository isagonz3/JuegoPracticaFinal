package jueguito.juegopracticafinal.Modelo.Inventario;

import jueguito.juegopracticafinal.TADs.Lista;

public class Inventario {
    private Lista<Objeto> objetos;
    private int sizeInventario;

    public Inventario(){
        this.objetos = new Lista<>();
        this.sizeInventario = 10;
    }

    public Inventario(int size){
        this.objetos = new Lista<>();
        this.sizeInventario = size;
    }

    public boolean addObjeto(Objeto objeto){

        if(objetos.getSize() >= sizeInventario){
            return false;
        }

        // SOLO STACK PARA GEMA
        if(objeto.getNombre().equalsIgnoreCase("gema")){

            for(int i = 0; i < objetos.getSize(); i++){
                Objeto o = objetos.get(i);

                if(o.getNombre().equalsIgnoreCase("gema")){
                    o.incrementarCantidad();
                    return true;
                }
            }
        }

        objetos.add(objeto);
        return true;
    }

    public boolean removeObjeto(Objeto objeto){
        return objetos.delete(objeto) != null;
    }

    public boolean containsObjeto(String nombre){
        for(int i = 0; i < objetos.getSize(); i++){
            if(objetos.get(i).getNombre().equals(nombre)){
                return true;
            }
        }
        return false;
    }

    public Objeto getObjeto(int index){
        return objetos.get(index);
    }

    public Objeto findObjeto(String nombre){
        for(int i = 0; i < objetos.getSize(); i++){
            Objeto objeto = objetos.get(i);
            if(objeto.getNombre().equals(nombre)){
                return objeto;
            }
        }
        return null;
    }

    public int size(){
        return objetos.getSize();
    }

    public boolean inventarioLleno(){
        return objetos.getSize() >= sizeInventario;
    }

    public boolean inventarioVacio(){
        return objetos.isEmpty();
    }

    public Lista<Objeto> getObjetos(){
        return objetos;
    }

    public void setSizeInventario(int size){
        this.sizeInventario = size;
    }

    public int getSizeInventario(){
        return sizeInventario;
    }
}
