package src.controller;

import src.model.UsuarioDAO;
import java.io.IOException;
import src.model.Usuario;
import src._helpers.Validaciones;


public class UsuarioController{

    public static char existeEnAutorizado(String cedula){
        try{
           return UsuarioDAO.busquedaPorCedula(cedula);

        } catch (IOException e){
            e.printStackTrace();
            return '\0';
        }
    }

    public static boolean existeEnRegistrados(String correo){
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

        if(Validaciones.validarCamposVacios(nombre, apellido, cedula, correo, clave, confirmacionClave)){
            return "*Faltan campos por completar.";
        }

        cedula = cedula.trim();
        correo = correo.trim();
        nombre = nombre.trim();
        apellido = apellido.trim();

        if(!Validaciones.validarFormatoNombre(nombre) || !Validaciones.validarFormatoNombre(apellido)){
            return "*Nombre y Apellido deben contener solo letras y al menos 2 caracteres.";
        }

        if(!Validaciones.validarFormatoCedula(cedula)){
            return "*La Cédula debe contener de 2 a 8 digitos y no debe empezar con 0.";
        }

        if(!Validaciones.validarFormatoCorreo(correo)){
            return "*El Correo debe tener el formato \"nombre@dominio.extension\"."; 
        }

        if(!Validaciones.validarFormatoClave(clave)){
            return "La Contraseña debe contener de 8 a 16 caracteres y sin espacios en blanco.";
        }

        if(!clave.equals(confirmacionClave)){
            return "*Las Contraseñas no coinciden.";
        }

        try{
            char rolUsuario = UsuarioDAO.busquedaPorCedula(cedula);
            
            if(rolUsuario == '\0'){
                return "*La Cédula no está autorizada en el sistema.";
            }

            if(UsuarioDAO.existeCedula(cedula)){
                return "*La Cédula ya se encuentra registrada en el sistema.";
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
