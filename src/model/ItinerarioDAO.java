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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

        if(datos.length != 6){
            return null;
        }

        try{

            String ruta = datos[0].trim();
            String horaSalida = datos[1].trim();
            String placa = datos[2].trim();
            String conductor = datos[3].trim();
            String estado = datos[4].trim();

            int reservas = Integer.parseInt(datos[5].trim());

            if(Validaciones.validarFormatoItinerario(ruta, horaSalida,estado)){
                return new Itinerario(ruta, horaSalida, placa, conductor, estado, reservas);
            } else {
                return null;
            }

        } catch (NumberFormatException e) {
            return null;
        }

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

        Itinerario itinerario = new Itinerario(ruta, horaSalida, placa, conductor, estado, reservas);

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

            String lineaAgregada = inicioLinea + ruta + "|" + horaSalida + "|" + placa + "|" + conductor + "|" + estado + "|" + reservas + System.lineSeparator();

            Files.write(RUTA_ITINERARIOS, lineaAgregada.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            
            System.out.println("ÉXITO: Se guardó correctamente en el archivo.");
            return true;

        } catch (IOException e) {
            System.out.println("ERROR DE ARCHIVO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelarItinerario(LocalTime horaActual){

        if(!Files.exists(RUTA_ITINERARIOS)){
            return false;
        }

        List<String> archivoActualizado = new LinkedList<>();
        boolean existe = false;
        
        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_ITINERARIOS, StandardCharsets.UTF_8)){

            String linea;

            while((linea = lectorArchivo.readLine()) != null){
                linea = linea.trim();

                if(linea.isEmpty()){
                    continue;
                }

                Itinerario itinerario = leerItinerario(linea);

                if(itinerario != null && itinerario.getEstado().equalsIgnoreCase("Programado") && itinerario.getReservas() == 0){

                    LocalTime horaSalida = LocalTime.parse(itinerario.getHoraSalida(),DateTimeFormatter.ofPattern("HH:mm"));

                    long minutosDiferencia = ChronoUnit.MINUTES.between(horaActual, horaSalida);

                    if(minutosDiferencia <= 30){
                        itinerario.setEstado("Cancelado");

                        linea = itinerario.getRuta() + "|" + itinerario.getHoraSalida() + "|" + itinerario.getPlaca() + "|" + itinerario.getConductor() + "|" + itinerario.getEstado() + "|" + itinerario.getReservas();
                        existe = true;
                    }
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
}
