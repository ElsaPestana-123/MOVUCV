package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import src._helpers.Validaciones;
import src.model.Mondero;
import src.model.MonederoDAO;

public class MonederoTest {

    @Test

    public void ValidarNumeroTelefono(){

        assertTrue(Validaciones.validarNumeroTelefono("04168765439"));
        assertFalse(Validaciones.validarNumeroTelefono("0416876543"));
        assertFalse(Validaciones.validarNumeroTelefono("041687654390"));
        assertFalse(Validaciones.validarNumeroTelefono("0416876543a"));
        assertFalse(Validaciones.validarNumeroTelefono(""));
        assertFalse(Validaciones.validarNumeroTelefono(null));
    }
    
    @Test 

    public void ValidarCedula(){

        assertTrue(Validaciones.validarFormatoCedula("12345678")); // 8 digitos de cedula
        assertTrue(Validaciones.validarFormatoCedula("1234")); // 4 digitos de cedula 
        assertFalse(Validaciones.validarFormatoCedula("01234567")); // Error No puede empezar en 0 la cedula
        assertFalse(Validaciones.validarFormatoCedula("123456789")); // No puede tener mas de 8 digitos
        assertFalse(Validaciones.validarFormatoCedula("123")); // No puede tener menos de 4 digitos 
        assertFalse(Validaciones.validarFormatoCedula("Hola")); // No puede ser letras 
        assertFalse(Validaciones.validarFormatoCedula("")); // No puede ser vacio
        assertFalse(Validaciones.validarFormatoCedula(null)); // No puede ser nulo
    }

    @Test 

    public void ValidarNumeroReferencia(){

        assertTrue(Validaciones.validarNumeroReferencia("0102456734592")); // la referencia tiene 13 digitos numericos
        assertTrue(Validaciones.validarNumeroReferencia("734592")); // pueden ser los 6 ultimos digitos de la referencia
        assertTrue(Validaciones.validarNumeroReferencia("4592")); // pueden los 4 ultimos digitos de la referencia 
        assertFalse(Validaciones.validarNumeroReferencia("01024567345923")); // No puede ser mas de 13 digitos
        assertFalse(Validaciones.validarNumeroReferencia("592")); // No puede ser menos de 4 digitos
        assertFalse(Validaciones.validarNumeroReferencia("abcdefghijkl")); // No puede ser letras
        assertFalse(Validaciones.validarNumeroReferencia("")); // No puede ser vacio 
        assertFalse(Validaciones.validarNumeroReferencia(null)); // No puede ser nulo
    }

    @Test 

    public void ValidarMonto(){

        assertTrue(Validaciones.validarMonto((float) 400.7)); // el monto es número mayor a 0
        assertFalse(Validaciones.validarMonto((float) 0.0)); // el monto no puede ser 0
        assertFalse(Validaciones.validarMonto((float) -1.5)); // el monto no puede ser un número negativo

    }

    @Test 

    public void guardarPago(){

        Mondero pago = new Mondero("0102 - Venezuela","04145674391", "12345678", "8765", 400);

        boolean guardado = MonederoDAO.guardarPago(pago);

        assertTrue(guardado);
    }
}
