package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import src._helpers.Validaciones;
import src.model.UnidadDAO;
import src.model.Unidad;

public class UnidadGestionF {

    @Test 

    public void ValidarDatosUnidad(){
        assertTrue(Validaciones.validarFormatoDatosUnidad("01AB2CA", "Encava ENT-657", 63, "Operativo"));

        assertFalse(Validaciones.validarFormatoDatosUnidad("88AD4HD", "Encava ENT-657", 63, "Operativo"));
        assertFalse(Validaciones.validarFormatoDatosUnidad("01AB2CA", "Encava ENT-657", -1, "En Mantenimiento"));
    }

    @Test 

    public void ValidarBusquedaUnidad(){

        UnidadDAO unidad = new UnidadDAO();

        Unidad unidadExistente = unidad.buscarUnidad("01AB2CA");

        assertNotNull(unidadExistente);

        assertEquals("01AB2CA", unidadExistente.getPlaca());

        Unidad unidadInexistente = unidad.buscarUnidad("88AD4HD");

        assertNull(unidadInexistente);

    }

    @Test 

    public void ValidarNoDuplicados(){

        UnidadDAO unidad = new UnidadDAO();

        boolean guardarUnidadExistente = unidad.guardarUnidad("01AB2CA", "Encava ENT-610", 32, "Operativo");

        assertFalse(guardarUnidadExistente);


    }

    @Test 

    public void ValidarActualizacionUnidad(){

        UnidadDAO unidad = new UnidadDAO();

        boolean actualizarUnidadExistente = unidad.actualizarUnidad("01AB2CA", "En Mantenimiento");

        assertTrue(actualizarUnidadExistente);

        boolean actualizarUnidadInexistente = unidad.actualizarUnidad("88AD4HD", "Operativo");

        assertFalse(actualizarUnidadInexistente);
    }

    @Test 

    public void ValidarEliminacionUnidad(){

        UnidadDAO unidad = new UnidadDAO();

        boolean eliminarUnidadExistente = unidad.eliminarUnidad("01FD2SA");

        assertTrue(eliminarUnidadExistente);

        boolean eliminarUnidadInexistente = unidad.eliminarUnidad("88AD4HD");

        assertFalse(eliminarUnidadInexistente);
    }

}
