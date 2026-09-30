package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import src._helpers.Validaciones;

public class UsuarioRegistroTest {

    @Test 

    public void testValidarNombre(){
        assertTrue(Validaciones.validarFormatoNombre("Juan"));
        assertFalse(Validaciones.validarFormatoNombre("Juan123"));
        assertFalse(Validaciones.validarFormatoNombre("Juan@"));
        assertFalse(Validaciones.validarFormatoNombre(""));
        assertFalse(Validaciones.validarFormatoNombre(null));
    }

    @Test

    public void testValidarApellido(){
        assertTrue(Validaciones.validarFormatoNombre("Castillo"));
        assertFalse(Validaciones.validarFormatoNombre("Castillo123"));
        assertFalse(Validaciones.validarFormatoNombre("Castillo@"));
        assertFalse(Validaciones.validarFormatoNombre(""));
        assertFalse(Validaciones.validarFormatoNombre(null));
    }

    @Test

    public void testValidarCedula(){
        assertTrue(Validaciones.validarFormatoCedula("31380819"));
        assertFalse(Validaciones.validarFormatoCedula("1"));
        assertFalse(Validaciones.validarFormatoCedula("123456789"));
        assertFalse(Validaciones.validarFormatoCedula("01234567"));
        assertFalse(Validaciones.validarFormatoCedula(""));
        assertFalse(Validaciones.validarFormatoCedula(null));
    }
    
    @Test

    public void testValidarCorreo(){
        assertTrue(Validaciones.validarFormatoCorreo("juanCastillo@gmail.com"));
        assertFalse(Validaciones.validarFormatoCorreo("juanCastillo@gmail"));
        assertFalse(Validaciones.validarFormatoCorreo("juanCastillo.com"));
        assertFalse(Validaciones.validarFormatoCorreo(""));
        assertFalse(Validaciones.validarFormatoCorreo(null));
    }

    @Test

    public void testValidarClave(){
        assertTrue(Validaciones.validarFormatoClave("JuanCas123&"));
        assertFalse(Validaciones.validarFormatoClave("JuanCas"));
        assertFalse(Validaciones.validarFormatoClave("JuanCas12345678901234567890"));
        assertFalse(Validaciones.validarFormatoClave(""));
        assertFalse(Validaciones.validarFormatoClave(null));
    }

}
