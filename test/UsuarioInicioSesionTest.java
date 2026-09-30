package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import java.io.IOException;

import src.model.UsuarioDAO;
import src.model.Usuario;

public class UsuarioInicioSesionTest{
    @Test 

    public void inicioSesionTest() throws IOException{

        String correo = "humbertico@gmail.com";
        String contrasena = "12345678";

        Usuario usuario = UsuarioDAO.busquedaPorCorreo(correo);

        assertNotNull(usuario);
        assertEquals(correo, usuario.getCorreo());
        assertEquals(contrasena, usuario.getClaveAcceso());

    }

    @Test 

    public void inicioSesionInexistenteTest() throws IOException{

        Usuario usuario = UsuarioDAO.busquedaPorCorreo("usuarioinexistente@gmail.com");
        assertNull(usuario);
    }  

    @Test 

    public void inicioSesionCorreoMayusculasTest() throws IOException{

       Usuario usuario = UsuarioDAO.busquedaPorCorreo("HUMBERTICO@GMAIL.COM");
       assertNotNull(usuario);
       assertEquals("12345678", usuario.getClaveAcceso());
    }

    @Test 

    public void inicioSesioncontraseñaIncorrectaTest() throws IOException{

        String correo = "humbertico@gmail.com";
        String contrasenaMala = "Pepito123&";

        Usuario usuario = UsuarioDAO.busquedaPorCorreo(correo);

        assertNotNull(usuario);
        assertNotEquals(contrasenaMala, usuario.getClaveAcceso());

    }   
    
    

}