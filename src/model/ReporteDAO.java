package src.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ReporteDAO {

    private static final Path RUTA_PASAJES = Paths.get("data", "pagos_Pasaje.txt").toAbsolutePath();

    public Reporte generarReporte(){
        if (!Files.exists(RUTA_PASAJES)) return null;

        double totalPasajeEstudiantes = 0.0;
        double totalPasajeTrabajadores = 0.0;
        double totalPasajePublico = 0.0;

        Map<String, Integer> concurrenciaRutas = new HashMap<>();

        boolean registroExistente = false;

        try (BufferedReader leerPagos = Files.newBufferedReader(RUTA_PASAJES, StandardCharsets.UTF_8)) {
            String linea;

            while((linea = leerPagos.readLine()) != null) {
                String[] datos = linea.trim().split("\\|");

                if(datos.length == 3){
                    String ruta = datos[0].trim(); 
                    String indicadorRol = datos[1].trim().toUpperCase(); 
                    double montoTotal = Double.parseDouble(datos[2].trim());

                    if (indicadorRol.startsWith("E") || indicadorRol.contains("ESTUDIANTE")) {
                        totalPasajeEstudiantes += montoTotal;
                    } else if (indicadorRol.startsWith("T") || indicadorRol.contains("EMPLEADO") || indicadorRol.contains("PROFESOR")) {
                        totalPasajeTrabajadores += montoTotal;
                    } else {
                        totalPasajePublico += montoTotal; 
                    }

                    registroExistente = true;
                    concurrenciaRutas.put(ruta, concurrenciaRutas.getOrDefault(ruta, 0) + 1);


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }  

        if (!registroExistente) {
            return null;
        }

        int id = (int) (Math.random() * 900000) + 100000;
        String tipo = "Operativo";

        return new Reporte(id, tipo, totalPasajeEstudiantes, totalPasajeTrabajadores, totalPasajePublico, concurrenciaRutas);
    }

    public static boolean exportarCSV(Reporte reporte, String rutaDestino) {
        StringBuilder contenidoCSV = new StringBuilder();
        
        contenidoCSV.append("Numero de Reporte,").append(reporte.getIdReporte()).append("\n");
        contenidoCSV.append("Tipo,").append(reporte.getTipoReporte()).append("\n\n");

        contenidoCSV.append("Segmento,Ingresos Totales (Bs)\n");
        contenidoCSV.append("Estudiantes,").append(reporte.getTotalEstudiantes()).append("\n");
        contenidoCSV.append("Empleados/Profesores,").append(reporte.getTotalTrabajadores()).append("\n");
        contenidoCSV.append("Publico General,").append(reporte.getTotalPublico()).append("\n");
        contenidoCSV.append("TOTAL,").append(reporte.getTotal()).append("\n\n");

        contenidoCSV.append("Ruta,Cantidad de Pasajeros\n");
        for (Map.Entry<String, Integer> entry : reporte.getConcurrenciaRutas().entrySet()) {
            contenidoCSV.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }

        try {
            Files.writeString(Paths.get(rutaDestino), contenidoCSV.toString(), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
