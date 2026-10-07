package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import src._helpers.Validaciones;
import src.model.ItinerarioDAO;

public class UnidadGestionItest {
    @Test 

    public void ValidarHora(){
        assertTrue(Validaciones.validarHora("08:30"));
        assertFalse(Validaciones.validarHora("04:59")); // la hora que comienza a funcionar el transporte es a partie de las 5 de la mañana
        assertFalse(Validaciones.validarHora("20:01")); // la hora en que el trasnporte deja de funcionar es a las 20:00
        assertFalse(Validaciones.validarHora("08:80")); // No existe el minuto 80
        assertFalse(Validaciones.validarHora("8:30")); // el formato correcto es 08:30
        assertFalse(Validaciones.validarHora("HH:MM")); // no debe acepetar letras
        assertFalse(Validaciones.validarHora("")); // no puede ser vacio
        assertFalse(Validaciones.validarHora(null)); // no puede ser nulo
         
    }

    @Test 

    public void ValidarEstado(){

        assertTrue(Validaciones.validarEstado("Programado"));
        assertTrue(Validaciones.validarEstado("EN CURSO"));
        assertFalse(Validaciones.validarEstado("Progrmado")); // errores tipograficos
        assertFalse(Validaciones.validarEstado("Retrasado")); // no existe el estado Retrasado
        assertFalse(Validaciones.validarEstado(""));
        assertFalse(Validaciones.validarEstado(null));

    }

    @Test 

    public void ValidarRuta(){
        assertTrue(Validaciones.validarRuta("La Guaira - UCV"));
        assertFalse(Validaciones.validarRuta(""));
        assertFalse(Validaciones.validarRuta(null));
    }

    @Test 

    public void ValidarFormato(){
        assertTrue(Validaciones.validarFormatoItinerario("Silencio - UCV", "08:30", "Programado"));
        assertFalse(Validaciones.validarFormatoItinerario("Silencio - UCV", "08:90", "Programado"));
        assertFalse(Validaciones.validarFormatoItinerario("Silencio - UCV", "08:30", "Retrasado"));
    }

    @Test 

    public void GuardarItineario(){
        ItinerarioDAO itinerario = new ItinerarioDAO();

        boolean resultado1 = itinerario.guardarItinerario("La Guaira - UCV", "07:00", "01AB2CA", "13579", "Programado", 20);

        assertTrue(resultado1);

        ItinerarioDAO itinerario2 = new ItinerarioDAO();

        boolean resultado2 = itinerario2.guardarItinerario(null, null, null, null, null, 0);

        assertFalse(resultado2);

    }

    @Test 

    public void evitarConflictos(){
        ItinerarioDAO itinerario = new ItinerarioDAO();

        boolean resultado1 = itinerario.guardarItinerario("Silencio - UCV", "07:00", "01FG2WA", "6526291", "Programado", 15);

        assertTrue(resultado1);

        ItinerarioDAO itinerario2 = new ItinerarioDAO();

        boolean resultado2 = itinerario2.guardarItinerario("Silencio - UCV", "07:00", "01FG2WA", "6526291", "Programado", 34);

        assertFalse(resultado2);
    }

    @Test 

    public void cancelarItinerario(){

        ItinerarioDAO itinerario = new ItinerarioDAO();

        itinerario.guardarItinerario("La Rinconada - UCV", "14:30", "01HR2FA", "6027522", "Programado", 0);

    }

}
