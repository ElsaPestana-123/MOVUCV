package test;

import org.junit.Test;
import static org.junit.Assert.*;
import src.model.Rutas;
import src.model.RutasDAO;

public class RutasTest {

    private RutasDAO ruta = new RutasDAO();

    @Test 

    public void guardarRutaTestExito(){
       
        Rutas nuevaRuta = new Rutas("UCV - Petare", Rutas.TipoRuta.URBANA, 220);
        boolean resultado = ruta.guardarRuta(nuevaRuta);

        assertTrue(resultado);
    }

    @Test 

    public void guardarRutaTestFallo(){
       
        Rutas nuevaRuta = new Rutas("UCV - Petare", Rutas.TipoRuta.URBANA, 220);
        ruta.guardarRuta(nuevaRuta);
        boolean resultado = ruta.guardarRuta(nuevaRuta);

        assertFalse(resultado);
    }

    @Test

    public void actualizarRutaTestExito(){
       
        Rutas nuevaRuta = new Rutas("UCV - Petare", Rutas.TipoRuta.URBANA, 220);
        ruta.guardarRuta(nuevaRuta);
        boolean resultado = ruta.actualizarRuta("UCV - Chacao", nuevaRuta);
        assertTrue(resultado);
    }

    @Test

    public void actualizarRutaTestFallo(){
       
        Rutas nuevaRuta = new Rutas("UCV - Petare", Rutas.TipoRuta.URBANA, 220);
        ruta.guardarRuta(nuevaRuta);
        boolean resultado = ruta.actualizarRuta("UCV - Chacao", new Rutas("UCV - Chacao", Rutas.TipoRuta.URBANA, 220));
        assertFalse(resultado);
    }

    @Test 

    public void eliminarRutaTestExito(){
       
        Rutas nuevaRuta = new Rutas("UCV - Petare", Rutas.TipoRuta.URBANA, 220);
        ruta.guardarRuta(nuevaRuta);
        boolean resultado = ruta.eliminarRuta("UCV - Petare");
        assertTrue(resultado);
    }

    @Test

    public void eliminarRutaTestFallo(){
       
        Rutas nuevaRuta = new Rutas("UCV - Petare", Rutas.TipoRuta.URBANA, 220);
        ruta.guardarRuta(nuevaRuta);
        boolean resultado = ruta.eliminarRuta("UCV - Chacao");
        assertFalse(resultado);
    }
    
}
