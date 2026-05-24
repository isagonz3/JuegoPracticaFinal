package jueguito.juegopracticafinal.Modelo.Turno;

import jueguito.juegopracticafinal.Modelo.Core.Partida;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.Celda;
import jueguito.juegopracticafinal.Modelo.Mundo.Posicion;
import jueguito.juegopracticafinal.Modelo.Mundo.TipoCelda;
import jueguito.juegopracticafinal.Modelo.Mundo.Zona;
import jueguito.juegopracticafinal.TADs.Lista;

import static jueguito.juegopracticafinal.Modelo.Data.ObjetosData.OBJETOS_MAX;
import static jueguito.juegopracticafinal.Modelo.Data.ObjetosData.OBJETOS_MIN;

public class ObjetoManager {
    private Partida partida;

    public ObjetoManager(Partida partida){
        this.partida = partida;
    }

    public void ponerObjetos(Zona zona){
        if(zona.getIdZona() == 0 || zona.getIdZona() == 5 || zona.getIdZona() == 9){
            return;
        }

        Lista<Posicion> spawns = zona.getSpawnObjetos();
        if(spawns.getSize() > 0){
            for(int i = 0; i < spawns.getSize(); i++){
                Posicion posicion = spawns.get(i);
                if(!zona.esValida(posicion.getRow(),posicion.getCol())){
                    continue;
                }

                Celda celda = zona.getCelda(posicion.getRow(),posicion.getCol());
                if(!celda.isOcupada()){
                    celda.setObjeto(crearObjetoRandom());
                }
            }
            return;
        }

        int numObjetos = OBJETOS_MIN + (int)(Math.random() * (OBJETOS_MAX - OBJETOS_MIN + 1));
        for(int i = 0; i < numObjetos; i++){
            for(int intentos = 0; intentos < 50; intentos++){
                int row = (int)(Math.random() * zona.getRows());
                int col = (int)(Math.random() * zona.getCols());
                Celda celda = zona.getCelda(row, col);
                if (celda != null && celda.isTransitable() && !celda.isOcupada()
                        && celda.getTipoCelda() != TipoCelda.PUERTA && !celda.tieneObjeto()) {
                    celda.setObjeto(crearObjetoRandom());
                    break;
                }
            }
        }
    }

    private Objeto crearObjetoRandom(){
        int r = (int)(Math.random() * 5);
        return switch (r) {
            case 0 -> {
                Objeto o = new Objeto("Pocion de vida", TipoObjeto.CONSUMIBLE,
                        0, 0, 5, 0, 1, "Cura 5 de vida");
                yield o;
            }
            case 1 -> {
                Objeto o = new Objeto("Pocion de ataque", TipoObjeto.CONSUMIBLE,
                        2, 0, 0, 5, 1, "+2 ataque +5 rango");
                yield o;
            }
            case 2 -> {
                Objeto o = new Objeto("Pocion de defensa", TipoObjeto.CONSUMIBLE,
                        0, 1, 0, 0, 1, "+1 defensa");
                yield o;
            }
            case 3 -> {
                Objeto o = new Objeto("Espada", TipoObjeto.EQUIPABLE,
                        3, 0, 0, 0, 99, "Espada basica");
                o.setSlot(SlotEquipable.ARMA);
                yield o;
            }
            default -> {
                Objeto o = new Objeto("Escudo", TipoObjeto.EQUIPABLE,
                        0, 2, 0, 0, 99, "Escudo basico");
                o.setSlot(SlotEquipable.ESCUDO);
                yield o;
            }
        };
    }
}
