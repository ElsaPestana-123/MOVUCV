package src.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import src._helpers.Validaciones;

public class UnidadDAO{

    private static final Path RUTA_UNIDADES = Paths.get("data","unidades.txt").toAbsolutePath();  
    
    // Función para leer una unidad desde el archivo deunidades
    // y devolver el objeto Unidad correspondiente.

    public Unidad leerUnidad(String archivo){
        if(archivo == null || archivo.isEmpty()){
            return null;
        }

        String[] datos = archivo.split("\\|");
        if (datos.length != 4) {
           return null;
        }
        
        try {
            String placa = datos[0].trim();
            String modelo = datos[1].trim();

            int capacidad = Integer.parseInt(datos[2].trim());

            String disponible = datos[3].trim();

            if (Validaciones.validarFormatoDatosUnidad(placa, modelo, capacidad, disponible)) {
               return new Unidad(placa, modelo, capacidad, disponible);
            } else {
              return null;
           }
            
        } catch (NumberFormatException e) {
            return null;
        }

    }

    //Función para leer todas la unidades que se encuentran
    //en el archivo de unidades y ponerlas en una lista de unidades

    public List<Unidad> listaUnidades(){

        List<Unidad> unidades = new LinkedList<>();

        if(!Files.exists(RUTA_UNIDADES)) {
            return unidades;
        }

         try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_UNIDADES, StandardCharsets.UTF_8)){
            String linea;

            while((linea = lectorArchivo.readLine()) != null){

                linea = linea.trim();

                Unidad unidad = leerUnidad(linea);

                if(unidad != null){

                    unidades.add(unidad);

                }
            }
        } catch (IOException e) {
            
        }

        return unidades;
    }

    //Función para verificar si una unidad ya existe en el archivo de unidades

    public boolean existeUnidad(Unidad unidadNueva){

        List<Unidad> unidades = listaUnidades();

        for(int i = 0; i < unidades.size(); i++){

            Unidad unidadArchivo = unidades.get(i);

            if(unidadArchivo.getPlaca().equals(unidadNueva.getPlaca())){

                return true;

            }
        }
        return false;
    }

    //Función para buscar una unidad en el archivo
    //de unidades y devolver el objeto unidad correspondiente

    public Unidad buscarUnidad(String placa){
        if (placa == null || placa.isEmpty()) {
            return null;
        }

        List<Unidad> unidades = listaUnidades();

        for (int i = 0; i < unidades.size(); i++){

            Unidad unidadGuardada = unidades.get(i);

            if(unidadGuardada.getPlaca().equals(placa.trim())){

                return unidadGuardada;

            }
        }
        return null;
    }

    //Función para actualizar la disponibilidad de una unidad en el archivo de unidades

    public boolean actualizarUnidad(String placaVieja, String placa, String disponible){

        if (!Files.exists(RUTA_UNIDADES) || placa == null) {
            return false;
        }

        List<String> archivoActualizado = new LinkedList<>();
        boolean existe = false;

        try (BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_UNIDADES, StandardCharsets.UTF_8)) {
            String linea;
            placa = placa.trim();

            while((linea = lectorArchivo.readLine()) != null){

                linea = linea.trim();

                if (linea.isEmpty() || linea.startsWith("placa")) {
                    archivoActualizado.add(linea);
                    continue;
                }



                String[] datos = linea.split("\\|");

                if(datos.length == 4 && datos[0].trim().equalsIgnoreCase(placaVieja)){
                    datos[0] = placa.trim();
                    datos[3] = disponible.trim();
                    linea = String.join("|", datos);
                    existe = true;

                }

                archivoActualizado.add(linea);
            }


        } catch (IOException e) {
            return false;
        }    

        if(existe){
            
            try {
                Files.write(RUTA_UNIDADES, archivoActualizado, StandardCharsets.UTF_8);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        return false;

    }

    //Función para eliminar una unidad del archivo de unidades

    public boolean eliminarUnidad(String placa){

        if (!Files.exists(RUTA_UNIDADES) || placa == null) {
            return false;
        }

        List<String> archivoActualizado = new LinkedList<>();
        boolean existe = false;

        try (BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_UNIDADES, StandardCharsets.UTF_8)) {
            String linea;
            placa = placa.trim();

            while((linea = lectorArchivo.readLine()) != null){

                linea = linea.trim();

                if (linea.isEmpty() || linea.startsWith("placa")) {
                    archivoActualizado.add(linea);
                    continue;
                }

                String[] datos = linea.split("\\|");

                if(datos.length == 4 && datos[0].trim().equalsIgnoreCase(placa)){

                    existe = true;

                } else {
                    archivoActualizado.add(linea);

                }

            }


        } catch (IOException e) {
            return false;
        }    

        if(existe){
            
            try {
                Files.write(RUTA_UNIDADES, archivoActualizado, StandardCharsets.UTF_8);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        return false;

    }

    // Función para guardar una unidad en el archivo de unidades

    public boolean guardarUnidad(String placa, String modelo, int capacidad, String disponible){

        if (!Validaciones.validarFormatoDatosUnidad(placa, modelo, capacidad, disponible)) {
            return false;
        }

        Unidad unidad = new Unidad(placa, modelo, capacidad, disponible);

        if(!unidad.validarDatos()){
            return false;
        }

        if(existeUnidad(unidad)){
            return false;
        }

        try {
            String contenidoArchivo = Files.readString(RUTA_UNIDADES, StandardCharsets.UTF_8);
            String inicioLinea = "";

            if ((!contenidoArchivo.isEmpty()) && (!contenidoArchivo.endsWith("\n")) && (!contenidoArchivo.endsWith("\r\n"))) {
                inicioLinea = System.lineSeparator();
            }

            String lineaAgregada = inicioLinea + placa + "|" + modelo + "|" + capacidad + "|" + disponible + System.lineSeparator();

            Files.writeString(RUTA_UNIDADES, lineaAgregada, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;

        } catch (IOException e) {
            return false;
        }
    }
        
}