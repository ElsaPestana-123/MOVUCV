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
import java.time.LocalTime;
import java.util.Random;

public class ItinerarioDAO {

    private static final Path RUTA_ITINERARIOS = Paths.get("data","itinerario.txt").toAbsolutePath();

    public ItinerarioDAO() {
    }

    //Funcion para leer un itinerario

    public Itinerario leerItinerario(String linea){

        if(linea == null || linea.isEmpty()){
            return null;
        }

        String[] datos = linea.split("\\|");

        if(datos.length < 7){
            return null;
        }

        try{

            String ruta = datos[0].trim();
            String horaSalida = datos[1].trim();
            String placa = datos[2].trim();
            String conductor = datos[3].trim();
            String estado = datos[4].trim();

            int reservas = Integer.parseInt(datos[5].trim());
            int id = Integer.parseInt(datos[6].trim());

            if(Validaciones.validarFormatoItinerario(ruta, horaSalida,estado)){
                return new Itinerario(ruta, horaSalida, placa, conductor, estado, reservas,id);
            } else {
                return null;
            }

        } catch (NumberFormatException e) {
            return null;
        }

    }

// Método para leer las rutas desde rutas.txt
    public List<String> obtenerRutas() {
        List<String> rutas = new LinkedList<>();
        Path rutaArchivo = Paths.get("data", "rutas.txt").toAbsolutePath();
        
        if (!Files.exists(rutaArchivo)) {
            return rutas;
        }

        try (BufferedReader lector = Files.newBufferedReader(rutaArchivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                // Saltamos líneas vacías o el encabezado "nombre|tarifa"
                if (linea.isEmpty() || linea.toLowerCase().startsWith("nombre")) {
                    continue;
                }
                
                String[] datos = linea.split("\\|");
                if (datos.length >= 1) {
                    rutas.add(datos[0].trim()); // Tomamos solo el nombre de la ruta
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer rutas.txt: " + e.getMessage());
        }
        return rutas;
    }

    //Función para leer todos los itinerarios

    public List<Itinerario> listaItinerarios(){

        List<Itinerario> itinerarios = new LinkedList<>();

        if(!Files.exists(RUTA_ITINERARIOS)){
            return itinerarios;
        }

        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_ITINERARIOS, StandardCharsets.UTF_8)){
            String linea;

            while((linea = lectorArchivo.readLine()) != null){
                linea = linea.trim();

                Itinerario itinerario = leerItinerario(linea);

                if(itinerario != null){

                    itinerarios.add(itinerario);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return itinerarios;
    }

     public List<Itinerario> listaItinerariosPorConductor(String cedula){
        List<Itinerario> itinerariosAsignados = new LinkedList<>();
        if(cedula == null || cedula.trim().isEmpty()){
            return itinerariosAsignados;
        }

        for(Itinerario itinerario : listaItinerarios()){
            if(itinerario.getConductor().trim().equalsIgnoreCase(cedula.trim())){
                itinerariosAsignados.add(itinerario);
            }
        }

        return itinerariosAsignados;
    }
    //Función para verificar que un mismo autobus no sea asignado a la misma hora

    public boolean evitarConflictos(Itinerario itinerarionuevo){

        List<Itinerario> itinerarios = listaItinerarios();

        for(int i = 0; i < itinerarios.size(); i++){

            Itinerario itinerarioGuardado = itinerarios.get(i);

            String estadoGuardado = itinerarioGuardado.getEstado();

            if (estadoGuardado.equalsIgnoreCase("Cancelado") || estadoGuardado.equalsIgnoreCase("Finalizado")) {
                continue;
            }

            if(itinerarioGuardado.getHoraSalida().equals(itinerarionuevo.getHoraSalida())){
                if(itinerarioGuardado.getPlaca().equalsIgnoreCase(itinerarionuevo.getPlaca()) || itinerarioGuardado.getConductor().equalsIgnoreCase(itinerarionuevo.getConductor())){
                    return true;
                }
            }
        }

        return false;

    }

    public boolean guardarItinerario(String ruta, String horaSalida, String placa, String conductor, String estado, int reservas){

        // Rastreador 1
        if(!Validaciones.validarFormatoItinerario(ruta, horaSalida, estado)){
            System.out.println("ERROR: Validaciones rechazó el formato. Revisa la regex de la ruta o la hora.");
            return false;
        }

        int idNuevo = generarIDRandom();
        Itinerario itinerario = new Itinerario(ruta, horaSalida, placa, conductor, estado, reservas, idNuevo);

        // Rastreador 2
        if(evitarConflictos(itinerario)){
            System.out.println("ERROR: Conflicto detectado. Ese autobús o conductor ya tiene un viaje a las " + horaSalida);
            return false;
        }


        try{
            String contenidoArchivo = Files.readString(RUTA_ITINERARIOS, StandardCharsets.UTF_8);
            String inicioLinea = "";

            if((!contenidoArchivo.isEmpty()) && (!contenidoArchivo.endsWith("\n")) && (!contenidoArchivo.endsWith("\r\n"))){
                inicioLinea = System.lineSeparator();
            }

            String lineaAgregada = inicioLinea + ruta + "|" + horaSalida + "|" + placa + "|" + conductor + "|" + estado + "|" + reservas + "|" + idNuevo + System.lineSeparator();

            Files.write(RUTA_ITINERARIOS, lineaAgregada.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            
            System.out.println("ÉXITO: Se guardó correctamente en el archivo.");
            return true;

        } catch (IOException e) {
            System.out.println("ERROR DE ARCHIVO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public boolean cancelarItinerario(LocalTime horaActual){
        if(!Files.exists(RUTA_ITINERARIOS)){
            return false;
        }

        List<String> archivoActualizado = new LinkedList<>();
        boolean cancelado = false;

        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_ITINERARIOS, StandardCharsets.UTF_8)){
            String linea;

            while((linea = lectorArchivo.readLine()) != null){
                linea = linea.trim();

                if(linea.isEmpty()){
                    continue;
                }

                Itinerario itinerario = leerItinerario(linea);

                if(itinerario != null && itinerario.getEstado().equalsIgnoreCase("Programado")){
                    itinerario.setEstado("Cancelado");
                    linea = itinerario.getRuta() + "|" + itinerario.getHoraSalida() + "|" + itinerario.getPlaca()
                            + "|" + itinerario.getConductor() + "|" + itinerario.getEstado() + "|" + itinerario.getReservas();
                    cancelado = true;
                }

                archivoActualizado.add(linea);
            }
        } catch (IOException e){
            return false;
        }

        if(cancelado){
            try{
                Files.write(RUTA_ITINERARIOS, archivoActualizado, StandardCharsets.UTF_8);
                return true;
            } catch (IOException e){
                return false;
            }
        }

        return false;
    }

    public boolean cancelarItinerario(String ruta, String horaSalida, String placa){

        if(!Files.exists(RUTA_ITINERARIOS) || ruta == null || horaSalida == null || placa == null){
            return false;
        }

        List<String> archivoActualizado = new LinkedList<>();
        boolean cancelado = false;

        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_ITINERARIOS, StandardCharsets.UTF_8)){
            String linea;

            while((linea = lectorArchivo.readLine()) != null){
                linea = linea.trim();

                if(linea.isEmpty()){
                    continue;
                }

                Itinerario itinerario = leerItinerario(linea);

                if(itinerario != null
                        && itinerario.getRuta().equalsIgnoreCase(ruta.trim())
                        && itinerario.getHoraSalida().equals(horaSalida.trim())
                        && itinerario.getPlaca().equalsIgnoreCase(placa.trim())
                        && itinerario.getEstado().equalsIgnoreCase("Programado")){
                    itinerario.setEstado("Cancelado");
                    linea = itinerario.getRuta() + "|" + itinerario.getHoraSalida() + "|" + itinerario.getPlaca()
                            + "|" + itinerario.getConductor() + "|" + itinerario.getEstado() + "|" + itinerario.getReservas();
                    cancelado = true;
                }

                archivoActualizado.add(linea);
            }
        } catch (IOException e){
            return false;
        }

        if(cancelado){
            try{
                Files.write(RUTA_ITINERARIOS, archivoActualizado, StandardCharsets.UTF_8);
                return true;
            } catch (IOException e){
                return false;
            }
        }

        return false;
    }

    public boolean actualizarItinerario(String ruta, String horaSalida, String placa, String conductor, String estado){

        if(!Files.exists(RUTA_ITINERARIOS)){
            return false;
        }

        List<String> archivoActualizado = new LinkedList<>();

        boolean existe = false;

        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_ITINERARIOS, StandardCharsets.UTF_8)){

            String linea;

            ruta = ruta.trim();
            horaSalida = horaSalida.trim();

            while((linea = lectorArchivo.readLine()) != null){
                linea = linea.trim();

                if (linea.isEmpty()) {
                    continue;
                }

                Itinerario itinerario = leerItinerario(linea);

                if (itinerario != null && itinerario.getRuta().equalsIgnoreCase(ruta) && itinerario.getHoraSalida().equals(horaSalida)) {
                    
                    linea = ruta + "|" + horaSalida + "|" + placa.trim() + "|" + conductor.trim() + "|" + estado.trim() + "|" + itinerario.getReservas();
                    existe = true;
                }

                archivoActualizado.add(linea);
            }

        } catch (IOException e){
            return false;
        }

        if(existe){
            try{
                Files.write(RUTA_ITINERARIOS, archivoActualizado, StandardCharsets.UTF_8);
                return true;
            } catch (IOException e){
                return false;
            }
        }

        return false;
    }

    public Itinerario buscarItinerario(String ruta, String horaSalida){

        if(ruta == null || ruta.isEmpty() || horaSalida == null || horaSalida.isEmpty()){
            return null;
        }

        List<Itinerario> itinerarios = listaItinerarios();

        for(int i = 0; i < itinerarios.size(); i++){
            Itinerario itinerarioGuarado = itinerarios.get(i);

            if(itinerarioGuarado.getRuta().equals(ruta.trim()) && itinerarioGuarado.getHoraSalida().equals(horaSalida.trim())){
                return itinerarioGuarado;
            }
        }

        return null;
    }

    public boolean existeItinerario(int idBuscado){ // busqueda en itinerario.txt por id
         if(!Files.exists(RUTA_ITINERARIOS)){
            return false;
        }

        try (BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_ITINERARIOS, StandardCharsets.UTF_8)) {
            String linea;

            while((linea = lectorArchivo.readLine()) != null){
                linea = linea.trim();

                if (linea.isEmpty()) 
                    continue; //ignora vacias

                String[] datos = linea.split("\\|");
                if (datos.length >= 7) {

                    int idExistente = Integer.parseInt(datos[6].trim()); 

                    if (idExistente == idBuscado) 
                        return true;
                    
                    
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false; 

    }

    public int generarIDRandom(){ // función que genera numeros aleatorios para id itinerario
        Random random = new Random(); 
        int idNuevo = 1000 + random.nextInt(90000);

        while (existeItinerario(idNuevo)) {
        idNuevo = 1000 + random.nextInt(90000);
        }

        return idNuevo;
    }

    public boolean eliminarItinerario(String ruta, String horaSalida, String placa){
        if(!Files.exists(RUTA_ITINERARIOS)){
            return false;
        }

        
        boolean eliminado = false;
        List<String> archivoActualizado = new LinkedList<>();


        try (BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_ITINERARIOS, StandardCharsets.UTF_8)) {
            String linea;

            ruta = ruta.trim();
            horaSalida = horaSalida.trim();

            while((linea = lectorArchivo.readLine()) != null){
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split("\\|");
                if (datos.length >= 6) {

                    if (datos[0].equalsIgnoreCase(ruta) && datos[1].equalsIgnoreCase(horaSalida) && datos[2].equalsIgnoreCase(placa)) 
                        eliminado = true;

                     else 
                        archivoActualizado.add(linea);
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (eliminado) {
            try{
                Files.write(RUTA_ITINERARIOS, archivoActualizado, StandardCharsets.UTF_8);
                return true;

            } catch (IOException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    // ...existing code...

public int incrementarReservasSiDisponible(int idItinerario, int capacidad) throws IOException {
    return ajustarReservas(idItinerario, 1, capacidad);
}

public boolean decrementarReservas(int idItinerario) throws IOException {
    return ajustarReservas(idItinerario, -1, Integer.MAX_VALUE) == 1;
}

private int ajustarReservas(int idItinerario, int cambio, int capacidad) throws IOException {
    List<String> lineas = Files.readAllLines(RUTA_ITINERARIOS, StandardCharsets.UTF_8);

    for (int i = 0; i < lineas.size(); i++) {
        String[] datos = lineas.get(i).split("\\|", -1);

        if (datos.length < 7) {
            continue;
        }

        try {
            if (Integer.parseInt(datos[6].trim()) != idItinerario) {
                continue;
            }

            int reservasActuales = Integer.parseInt(datos[5].trim());
            if (cambio > 0 && reservasActuales >= capacidad) {
                return 0;
            }

            datos[5] = String.valueOf(Math.max(0, reservasActuales + cambio));
            lineas.set(i, String.join("|", datos));

            Files.write(RUTA_ITINERARIOS, lineas, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
            return 1;
        } catch (NumberFormatException e) {
            continue;
        }
    }

    return -1;
}
}
