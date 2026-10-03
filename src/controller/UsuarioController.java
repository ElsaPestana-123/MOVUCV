package src.controller;

import src.model.UsuarioDAO;
import java.io.IOException;
import src.model.Usuario;
import src._helpers.Validaciones;
import javax.swing.JFrame;
import src.view.MOVUCVInicioApp; 
import src.view.MOVUCVRegistroApp;


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

    //validaciones respecto a formato de campos antes de hacer llamar a búsquedaPorCedula()

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
            return "*La Contraseña debe contener de 8 a 16 caracteres y sin espacios en blanco.";
        }

        if(!clave.equals(confirmacionClave)){
            return "*Las Contraseñas no coinciden.";
        }

        try{
            char rolUsuario = UsuarioDAO.busquedaPorCedula(cedula); 
            
            if(rolUsuario == '\0'){ //si no está en comunidad-universitaria entomces se registra por defecto como público general 
                rolUsuario = 'P'; 
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

    //valida el formato de los campos y la existencia del usuario en usuarios.txt, en caso exitoso devuelve en string el rol del usuario
    public String iniciarSesion(String correo, String clave){

        if(Validaciones.EsVacia(clave)|| Validaciones.EsVacia(correo)){
            return "*Faltan campos por completar.";
        }

        correo = correo.trim();

        if(!Validaciones.validarFormatoCorreo(correo)){
            return "*El Correo debe tener el formato \"nombre@dominio.extension\"."; 
        }

       try{

        Usuario usuarioBuscado = UsuarioDAO.busquedaPorCorreo(correo);

        if(usuarioBuscado == null){
            return "*Usuario no registrado, por favor registrarse.";
        }

        if(!usuarioBuscado.getClaveAcceso().equals(clave)){
            return "*Contraseña incorrecta.";
        }

        return String.valueOf(usuarioBuscado.getRol()); // en caso de exito retorna en string el rol del usuario

       } catch(IOException e){
        e.printStackTrace();
        return "*Error al acceder al registro del sistema.";
       }
    }

    public static void llamarRegistro(JFrame ventana){ // para en el inicio general llamar a registro en texto del cabezal
        ventana.dispose();

        MOVUCVRegistroApp vista = new MOVUCVRegistroApp();
        vista.setVisible(true);
    }

    public static void llamarInicio(JFrame ventana){ // para en el registro llamar a inicio en texto del cabezal
        ventana.dispose();

        MOVUCVInicioApp vista = new MOVUCVInicioApp();
        vista.setVisible(true);
    }
    
}
