package src.controller;

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

    //validación de que el correo tenga el formato nombre@dominio.extension usando regex
    public boolean validarFormatoCorreo(String correo){
        final String FORMATO_CORREO= "^([A-Za-z0-9.])+@([A-Za-z])+(\\.)[a-zA-Z]{2,}$"; 
        return correo != null && correo.matches(FORMATO_CORREO);
    }

    //validación de que la cédula no comience en 0 y solo contenga números usando regex
    public boolean validarFormatoCedula(String cedula){
        final String FORMATO_CEDULA = "^[1-9]\\d{2,}$";
        return cedula != null && cedula.matches(FORMATO_CEDULA);
    }

    //validación de que la cédula vontenga números unicamente usando regex
    public boolean validarFormatoClave(String clave){
        final String FORMATO_CLAVE = "^(\\S){8,}$";
        return clave != null && clave.matches(FORMATO_CLAVE);
    }

    //validación de nombre que solo contenga letras usando regex
    public boolean validarFormatoNombre(String nombre){
        final String FORMATO_NOMBRE = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]{2,}$";
        return nombre != null && nombre.matches(FORMATO_NOMBRE);
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

    //llama a todos los métodos de validación de formato antes de registrar al usuario y devuelve un string
    public String registrarUsuario(String nombre, String apellido, String correo, String cedula, String clave, String confirmacionClave){

        if(validarCamposVacios(nombre, apellido, cedula, correo, clave, confirmacionClave)){
            return "*Faltan campos por completar.";
        }

        cedula = cedula.trim();
        correo = correo.trim();
        nombre = nombre.trim();
        apellido = apellido.trim();

        if(!validarFormatoNombre(nombre) || !validarFormatoNombre(apellido)){
            return "*Nombre y Apellido deben contener solo letras y al menos 2 caracteres.";
        }

        if(!validarFormatoCedula(cedula)){
            return "*La Cédula solo debe contener digitos y no debe empezar con 0.";
        }

        if(!validarFormatoCorreo(correo)){
            return "*El Correo debe tener el formato \"nombre@dominio.extension\"."; 
        }

        if(!validarFormatoClave(clave)){
            return "La Contraseña debe contener al menos 8 caracteres y sin espacios en blanco.";
        }

        if(!clave.equals(confirmacionClave)){
            return "*Las Contraseñas no coinciden.";
        }

        try{
            char rolUsuario = UsuarioDAO.busquedaPorCedula(cedula);
            
            if(rolUsuario == '\0'){
                return "*La Cédula no está autorizada en el sistema.";
            }

            if(UsuarioDAO.busquedaPorCorreo(correo) !=  null){
                return "*El Correo ya se encuentra registrado en el sistema.";
            }

            boolean registroExitoso = UsuarioDAO.guardarUsuario(cedula, rolUsuario, nombre.toUpperCase(), apellido.toUpperCase(), correo, clave, 0.0);

            if(registroExitoso)
                return "USUARIO REGISTRADO EXITOSAMENTE.";

            else 
                return "Error al registrar usuario. Intenta nuevamente.";


        }catch(IOException e){
            e.printStackTrace();
            return "Error al acceder al registro del sistema.";
        }
    }
    
}
