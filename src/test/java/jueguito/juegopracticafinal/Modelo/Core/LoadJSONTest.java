package jueguito.juegopracticafinal.Modelo.Core;

import jueguito.juegopracticafinal.Modelo.Entidades.Estadisticas;
import jueguito.juegopracticafinal.Modelo.Entidades.Gato;
import jueguito.juegopracticafinal.Modelo.Entidades.Jugador;
import jueguito.juegopracticafinal.Modelo.Inventario.Inventario;
import jueguito.juegopracticafinal.Modelo.Inventario.Objeto;
import jueguito.juegopracticafinal.Modelo.Inventario.SlotEquipable;
import jueguito.juegopracticafinal.Modelo.Inventario.TipoObjeto;
import jueguito.juegopracticafinal.Modelo.Mundo.*;
import jueguito.juegopracticafinal.Modelo.Turno.EstadoJuego;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LoadJSONTest {

    private Zona zonaPrueba(int id){
        Celda[][] celdas = new Celda[3][3];

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                celdas[i][j]=new Celda(TipoCelda.SUELO);
            }
        }

        return new Zona(id, "Z"+id, celdas);
    }


    @Test
    void guardarYCargarPartidaBasica(){
        try{
            File temp=File.createTempFile("partida", ".json");

            GrafoZonas grafo=new GrafoZonas();
            Zona z=zonaPrueba(0);
            grafo.addZona(z);

            Partida original=new Partida(grafo);

            Jugador j=new Jugador("Test",new Estadisticas(10,5,5,4),new Posicion(0,0));

            original.setJugador(j);
            z.getCelda(0,0).setEntidad(j);

            Gato g = new Gato("Michi", new Estadisticas(10,0,0,4));
            original.setGato(g);

            original.setZonaActual(z);
            original.setIdZonaActual(0);
            original.setEstadoActual(EstadoJuego.EN_CURSO);

            LoadJSON.guardarPartida(original, temp.getAbsolutePath());

            Partida cargada=LoadJSON.cargarPartida(temp.getAbsolutePath(), grafo);

            assertNotNull(cargada);
            assertEquals("Test", cargada.getJugador().getNombre());
            assertEquals(0, cargada.getIdZonaActual());
            assertEquals(EstadoJuego.EN_CURSO, cargada.getEstadoActual());

        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    void cargarPartidaRutaInvalidaDevuelveNull() {
        GrafoZonas grafo = new GrafoZonas();
        Zona z = zonaPrueba(0);
        grafo.addZona(z);

        Partida resultado = LoadJSON.cargarPartida("/ruta/inexistente/partida.json", grafo);
        assertNull(resultado);
    }

    @Test
    void guardarYCargarMantieneInventario() {
        try {
            File temp=File.createTempFile("partida_inv", ".json");

            GrafoZonas grafo=new GrafoZonas();
            Zona z=new Zona(0, "Z0", new Celda[][]{
                    {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)},
                    {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
            });
            grafo.addZona(z);

            Partida p=new Partida(grafo);
            Jugador j=new Jugador("Test",new Estadisticas(10, 5, 5, 4), new Posicion(0, 0));
            p.setJugador(j);
            z.getCelda(0, 0).setEntidad(j);

            Objeto espada = new Objeto("Espada",TipoObjeto.EQUIPABLE,2, 1, 0, 0,3,null);
            espada.setSlot(SlotEquipable.ARMA); // slot correcto
            j.getInventario().addObjeto(espada);

            Gato g=new Gato("PUTOGATO", new Estadisticas(10, 0, 0, 4));
            p.setGato(g);

            p.setZonaActual(z);
            p.setIdZonaActual(0);
            p.setEstadoActual(EstadoJuego.EN_CURSO);

            LoadJSON.guardarPartida(p, temp.getAbsolutePath());

            Partida cargada=LoadJSON.cargarPartida(temp.getAbsolutePath(), grafo);
            assertNotNull(cargada);

            Jugador jc=cargada.getJugador();

            assertTrue(jc.getInventario().size() >= 1);

            Objeto o1=jc.getInventario().getObjeto(0);
            assertEquals("Espada",o1.getNombre());
            assertEquals(TipoObjeto.EQUIPABLE,o1.getTipo());
            assertEquals(2,o1.getAtaqueBonus());
            assertEquals(3,o1.getUsosRestantes());
            if (o1.getSlot()!=null) {
                assertEquals(SlotEquipable.ARMA, o1.getSlot());
            }

        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void guardarYCargarMantieneEquipamiento() {
        try {
            File temp = File.createTempFile("partida_equip", ".json");

            GrafoZonas grafo = new GrafoZonas();
            Zona z = new Zona(0, "Z0", new Celda[][]{
                    {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)},
                    {new Celda(TipoCelda.SUELO), new Celda(TipoCelda.SUELO)}
            });
            grafo.addZona(z);

            Partida p = new Partida(grafo);
            Jugador j = new Jugador("Test", new Estadisticas(10, 5, 2, 4), new Posicion(0, 0));
            p.setJugador(j);
            z.getCelda(0, 0).setEntidad(j);

            // Crear y equipar ARMA (ataque +3)
            Objeto espada = new Objeto("Espada", TipoObjeto.EQUIPABLE, 3, 0, 0, 0, 99, "Espada");
            espada.setSlot(SlotEquipable.ARMA);
            j.getInventario().addObjeto(espada);

            // Crear y equipar ESCUDO (defensa +3)
            Objeto escudo = new Objeto("Escudo", TipoObjeto.EQUIPABLE, 0, 3, 0, 0, 99, "Escudo");
            escudo.setSlot(SlotEquipable.ESCUDO);
            j.getInventario().addObjeto(escudo);

            // Equipar ambos
            j.equipar(espada, SlotEquipable.ARMA);
            j.equipar(escudo, SlotEquipable.ESCUDO);

            // Stats del jugador antes de guardar
            int atqAntes = j.getAtaqueTotal();
            int defAntes = j.getDefensaTotal();

            Gato g = new Gato("Gatiko", new Estadisticas(10, 0, 0, 4));
            p.setGato(g);
            p.setZonaActual(z);
            p.setIdZonaActual(0);
            p.setEstadoActual(EstadoJuego.EN_CURSO);

            LoadJSON.guardarPartida(p, temp.getAbsolutePath());

            Partida cargada = LoadJSON.cargarPartida(temp.getAbsolutePath(), grafo);
            assertNotNull(cargada);

            Jugador jc = cargada.getJugador();
            Inventario inv = jc.getInventario();

            // Verificar que los objetos equipados existen
            Objeto equipArma = jc.getEquipamiento()[SlotEquipable.ARMA.ordinal()];
            Objeto equipEscudo = jc.getEquipamiento()[SlotEquipable.ESCUDO.ordinal()];

            assertNotNull(equipArma, "Slot ARMA debe tener objeto");
            assertNotNull(equipEscudo, "Slot ESCUDO debe tener objeto");

            assertEquals("Espada", equipArma.getNombre());
            assertEquals(SlotEquipable.ARMA, equipArma.getSlot());
            assertEquals(3, equipArma.getAtaqueBonus());

            assertEquals("Escudo", equipEscudo.getNombre());
            assertEquals(SlotEquipable.ESCUDO, equipEscudo.getSlot());
            assertEquals(3, equipEscudo.getDefensaBonus());

            // Verificar que los objetos equipados están en objetosEquipados
            assertTrue(inv.getObjetosEquipados().contains(equipArma));
            assertTrue(inv.getObjetosEquipados().contains(equipEscudo));

            // Verificar que los bonuses se aplicaron correctamente (ini 5atk/2def + 3atk/3def = 8/5)
            assertEquals(atqAntes, jc.getAtaqueTotal());
            assertEquals(defAntes, jc.getDefensaTotal());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

