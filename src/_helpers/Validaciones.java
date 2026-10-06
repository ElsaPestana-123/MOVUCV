package src._helpers; 

import javax.swing.*;

public class Validaciones {

    public static boolean EsVacia(String cadena){
        return cadena == null || cadena.trim().isEmpty();
    }

    public static boolean validarCamposVacios(String nombre, String apellido, String cedula, String correo, String clave, String confirmacionClave){
        return EsVacia(nombre) || EsVacia(cedula) || EsVacia(correo) || EsVacia(clave) || EsVacia(apellido);
    }

    //validación de que el correo tenga el formato nombre@dominio.extension usando regex
    public static boolean validarFormatoCorreo(String correo){
        final String FORMATO_CORREO= "^([A-Za-z0-9.])+@([A-Za-z])+(\\.)[a-zA-Z]{2,}$"; 
        return correo != null && correo.matches(FORMATO_CORREO);
    }

    //validación de que la cédula no comience en 0 y solo contenga números de 2 a 8 digitos, usando regex
    public static boolean validarFormatoCedula(String cedula){
        final String FORMATO_CEDULA = "^[1-9]\\d{1,7}$";
        return cedula != null && cedula.matches(FORMATO_CEDULA);
    }

    //validación de que la clave contenga de 8 a 16 caracteres, usando regex
    public static boolean validarFormatoClave(String clave){
        final String FORMATO_CLAVE = "^(\\S){8,16}$";
        return clave != null && clave.matches(FORMATO_CLAVE);
    }

    //validación de nombre que solo contenga letras usando regex
    public static boolean validarFormatoNombre(String nombre){
        final String FORMATO_NOMBRE = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]{2,}$";
        return nombre != null && nombre.matches(FORMATO_NOMBRE);
    }

    //validación de datos de la unidad
   public static boolean validarFormatoPlaca(String placa){
        return placa != null && placa.matches("^01[A-Z]{2}2[A-Z]A$");
    }

   public static boolean validarFormatoModelo(String modelo){
        return modelo != null && modelo.matches(
                "^[A-ZÁÉÍÓÚÑ][a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$");
    }

   public static boolean validarFormatoEstado(String disponible){
        return disponible != null
                && disponible.matches("^(Operativo|En Mantenimiento|Inactivo)$");
    }

   public static boolean validarFormatoDatosUnidad(String placa, String modelo, int capacidad, String disponible){

        if (placa == null || modelo == null || disponible == null || capacidad <=0) 
            return false;

        return validarFormatoPlaca(placa)
                && validarFormatoModelo(modelo)
                && validarFormatoEstado(disponible);
    }

    public static void limpiarCampos( JTextField texto){

        texto.setText("");
    }

    // Validar las horas que pueden ser asignadas en el itinerario

    public static boolean validarHora (String hora){
        if (hora == null || hora.trim().isEmpty()){
            return false;
        }

        return hora.matches("^((0[5-9]|1[0-9]):[0-5][0-9]|20:00)$");

    }

    //Validar los estados del itinerario

    public static boolean validarEstado (String estado){

        if(estado == null || estado.trim().isEmpty()){
            return false;
        }
        
        estado = estado.trim();

        return estado.equalsIgnoreCase("Programado") || estado.equalsIgnoreCase("En Curso") || estado.equalsIgnoreCase("Finalizado") || estado.equalsIgnoreCase("Cancelado");
    }

    // Validar la ruta

    public static boolean validarRuta(String ruta){
        if(ruta == null || ruta.trim().isEmpty()){
            return false;
        }

        return true;
    }

    public static boolean validarFormatoItinerario(String ruta, String hora, String estado){
        return validarHora(hora) && validarEstado(estado) && validarRuta(ruta);
    }
}