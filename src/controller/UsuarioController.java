package src.controller;

/*import java.util.regex.Matcher;
import java.util.regex.Pattern;*/

import src.model.UsuarioDAO;
import java.io.IOException;
import src.model.Usuario;


public class UsuarioController{

    public boolean EsVacia(String cadena){
        return cadena == null || cadena.trim().isEmpty();
    }

    public boolean validarCamposVacios(String nombre, String apellido, String cedula, String correo, String clave, String confirmacionClave){
        return EsVacia(nombre) || EsVacia(cedula) || EsVacia(correo) || EsVacia(clave) || EsVacia(apellido);
    }

    public boolean validarFormatoCorreo(String correo){
        final String FORMATO_CORREO= "^([A-Za-z0-9.])+@([A-Za-z])+(\\.)[a-zA-Z]{2,}$"; 
        return correo != null && correo.matches(FORMATO_CORREO);
    }

    public boolean validarFormatoCedula(String cedula){
        final String FORMATO_CEDULA = "^[1-9]\\d{2,}$";
        return cedula != null && cedula.matches(FORMATO_CEDULA);
    }

    public char existeEnAutorizado(String cedula){
        try{
           return UsuarioDAO.busquedaPorCedula(cedula);

        } catch (IOException e){
            e.printStackTrace();
            return '\0';
        }
    }

    public boolean existeEnRegistrados(String correo){
        try{
            Usuario usuario = UsuarioDAO.busquedaPorCorreo(correo); 
            return usuario != null;

        } catch(IOException e){
            e.printStackTrace();
            return false; 
        }
    }


    
    //validar autorizados

    //validar duplicados

    //registro exitoso 
    
}
