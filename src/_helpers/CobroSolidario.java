package src._helpers;

public class CobroSolidario {

    // 0.15 = 15%, 0.50 = 50%, 1.00 = 100%
    public static final double tarifaEstudiante = 0.15; 
    public static final double tarifaTrabajador = 0.50;
    public static final double tatifaPublicoGeneral = 1.00;

    public static double calcularMonto(double tarifaBase, char rol){
        char rolMayuscula = Character.toUpperCase(rol);

        switch (rolMayuscula) {
            case 'E':
                return tarifaBase * tarifaEstudiante;
            case 'T':
                return tarifaBase * tarifaTrabajador;
            default:
                return tarifaBase * tatifaPublicoGeneral;
        }
    }

    public static String obtenerNombreRol(char rol){
        char rolMayuscula = Character.toUpperCase(rol);

        switch (rolMayuscula) {
            case 'E':
                return "Estudiante";
            case 'T':
                return "Trabajador";
            default:
                return "Público General";
        }
    }
    
}
