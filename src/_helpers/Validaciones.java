package src._helpers; 

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
   public static boolean validarFormatoDatosUnidad(String placa, String modelo, int capacidad){
        boolean resultplaca = true;
        boolean resultmodelo = true;
        boolean resultcapacidad = true;

        if (placa == null || placa.length() > 7) {
            resultplaca = false;
        } else if (placa.charAt(0) != '0' || placa.charAt(1) != '1' || placa.charAt(4) != '2' || placa.charAt(6) != 'A') {
            resultplaca = false;
        }
        
        if (modelo == null || modelo.trim().isEmpty()) {
            resultmodelo = false;
        }

        if (capacidad <= 0) {
            resultcapacidad = false;
        }

        return resultplaca && resultmodelo && resultcapacidad;
    }
}