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
   public static boolean validarFormatoDatosUnidad(String placa, String modelo, int capacidad, String disponible){

        if (placa == null || modelo == null || disponible == null || capacidad <=0) 
            return false;

        final String FORMATO_PLACA = "^01[A-Z]{2}2[A-Z]A$";
        final String FORMATO_MODELO = "^[A-ZÁÉÍÓÚÑ][a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$";
        final String FORMATO_ESTADO = "^(Operativo|En Mantenimiento|Inactivo)$";

        if (placa.matches(FORMATO_PLACA) && modelo.matches(FORMATO_MODELO) && disponible.matches(FORMATO_ESTADO)) 
            return true;
        
        return false;
    }

    public static void limpiarCampos( JTextField texto){

        texto.setText("");
    }
}