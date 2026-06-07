package jueguito.juegopracticafinal.Modelo.Inventario;

import jueguito.juegopracticafinal.TADs.ArbolBinarioDeBusqueda;
import jueguito.juegopracticafinal.TADs.InterfazIterador;
import jueguito.juegopracticafinal.TADs.Lista;

public class Inventario {

    //ATRIBUTOS

    private Lista<Objeto> objetos;
    private int sizeInventario;
    private ArbolBinarioDeBusqueda<Objeto> arbolObjetos;

    private Lista<Objeto> objetosUsados;
    private Lista<Objeto> objetosEquipados;


    public Inventario() {
        this.objetos = new Lista<>();
        this.objetosUsados = new Lista<>();
        this.objetosEquipados = new Lista<>();
        this.sizeInventario = 10;
        this.arbolObjetos = new ArbolBinarioDeBusqueda<>();
    }

    public Inventario(int size) {
        this.objetos = new Lista<>();
        this.objetosUsados = new Lista<>();
        this.objetosEquipados = new Lista<>();
        this.sizeInventario = size;
        this.arbolObjetos = new ArbolBinarioDeBusqueda<>();
    }


    //MÉTODOS

    //Añade un objeto al inventario si no se ha alcanzado el tamaño máximo permitido
    public boolean addObjeto(Objeto objeto) {

        if (objetos.getSize() >= sizeInventario) return false;
        objetos.add(objeto);
        arbolObjetos.add(objeto);
        return true;
    }

    //Elimina un objeto del inventario si existe y devuelve si la operación fue exitosa
    public boolean removeObjeto(Objeto objeto) {
        if (objetos.delete(objeto) != null) {
            arbolObjetos.delete(objeto);
            return true;
        }
        return false;
    }

    //Devuelve la cantidad actual de objetos en el inventario
    public int size() {
        return objetos.getSize();
    }

    //Comprueba si el inventario ha alcanzado su capacidad máxima
    public boolean inventarioLleno() {
        return objetos.getSize() >= sizeInventario;
    }

    //Comprueba si el inventario contiene un objeto con el mismo identificador
    public boolean contiene(Objeto objeto) {
        return arbolObjetos.contains(objeto);
    }

    public Lista<Objeto> inOrden() {
        return arbolObjetos.inOrden();
    }

    //Equipa un objeto si cumple las condiciones de tipo y compatibilidad con otros objetos equipados
    public boolean equipar(Objeto o) {

        if (o == null || o.getSlot() == null) return false;

        // máximo 2 equipados
        if (objetosEquipados.getSize() >= 2) return false;

        // solo 1 por tipo (ARMA / ESCUDO)
        InterfazIterador<Objeto> it = objetosEquipados.iterador();
        while (it.hasNext()) {
            if (it.next().getSlot() == o.getSlot()) return false;
        }

        objetosEquipados.add(o);
        return true;
    }


    //Actualiza el estado de los objetos en uso y equipados, eliminando aquellos cuya duración haya expirado
    public void avanzarTurno() {

        // OBJETOS USADOS (2 turnos)
        InterfazIterador<Objeto> itU = objetosUsados.iterador();
        while (itU.hasNext()) {
            Objeto o = itU.next();
            o.incrementarTurno();
            if (o.getTurnosActivos() >= o.getDuracionTurnos()) {
                objetosUsados.delete(o);
                // reinicia iteración tras delete
                itU = objetosUsados.iterador();
            }
        }

        // OBJETOS EQUIPADOS (5 turnos)
        InterfazIterador<Objeto> itE = objetosEquipados.iterador();
        while (itE.hasNext()) {
            Objeto o = itE.next();
            o.incrementarTurno();
            if (o.getTurnosActivos() >= o.getDuracionTurnos()) {
                objetosEquipados.delete(o);
                itE = objetosEquipados.iterador();
            }
        }
    }


    //GETTERS Y SETTERS

    public Lista<Objeto> getObjetosUsados() {
        return objetosUsados;
    }

    public Lista<Objeto> getObjetosEquipados() {
        return objetosEquipados;
    }

    public Lista<Objeto> getObjetos() {
        return objetos;
    }

    public Objeto getObjeto(int index) {
        return objetos.get(index);
    }
}
