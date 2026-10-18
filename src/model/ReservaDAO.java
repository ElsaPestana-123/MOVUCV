package src.model;

import src.model.Reserva;
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

public class ReservaDAO{
    private static final Path RUTA_RESERVAS = Paths.get("data","reservas.txt").toAbsolutePath();

    public ReservaDAO(){      
    }

    public Reserva leerReserva(String linea){ //para leer reserva de string ahorrar codigo

        if(linea == null || linea.trim().isEmpty())
            return null;

        String[] infoReserva = linea.split("\\|");
        if(infoReserva.length != 4)
            return null;

        String idReserva = infoReserva[0].trim(); 
        String cedulaPasajero = infoReserva[1].trim();
        int idItinerario = Integer.parseInt(infoReserva[2].trim());
        int numeroAsiento = Integer.parseInt(infoReserva[3].trim());
        String fecha = infoReserva[4].trim();

        Reserva reserva = new Reserva(idReserva, cedulaPasajero, idItinerario, numeroAsiento, fecha);

        return reserva;
    }

    public boolean guardarReserva(Reserva reserva) throws IOException { //guarda la reserva al final de reservas.txt
        if(reserva == null)
            return false;

        String linea = reserva.getIdReserva() + "|" + reserva.getCedulaPasajero() + "|" + reserva.getIdItinerario() + "|" + reserva.getNumeroAsiento() + "|" + reserva.getFecha();

        try{
            Files.write(RUTA_RESERVAS, (linea + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        }   catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public List <Integer> listaAsientosOcupados(int idItinerario) throws IOException { 
        List <Integer> asientosOcupados = new LinkedList<>();
        if(!Files.exists(RUTA_RESERVAS))
            return asientosOcupados;
        
        
        try(BufferedReader lector = Files.newBufferedReader(RUTA_RESERVAS, StandardCharsets.UTF_8)){
            String linea;
            
            while((linea = lector.readLine()) != null){
                Reserva reserva = leerReserva(linea);

                if(reserva != null && reserva.getIdItinerario() == idItinerario)
                    asientosOcupados.add(reserva.getNumeroAsiento());
                
            }
        }
         catch(IOException e){
            e.printStackTrace();

        }   
        return asientosOcupados;
    }

    public boolean existeReserva(String idReserva) throws IOException {
        if(!Files.exists(RUTA_RESERVAS))
            return false;
        

        try(BufferedReader lector = Files.newBufferedReader(RUTA_RESERVAS, StandardCharsets.UTF_8)){
            String linea;

            while((linea = lector.readLine()) != null){
                Reserva reserva = leerReserva(linea);

                if(reserva != null && reserva.getIdReserva().equals(idReserva))
                    return true;
            }

        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

   public boolean usuarioConReservaenItinerario(String cedulaPasajero, int idItinerario) throws IOException {
        if(!Files.exists(RUTA_RESERVAS))
            return false;
        

        try(BufferedReader lector = Files.newBufferedReader(RUTA_RESERVAS, StandardCharsets.UTF_8)){
            String linea;

            while((linea = lector.readLine()) != null){
                Reserva reserva = leerReserva(linea);

                if(reserva != null && reserva.getCedulaPasajero().equals(cedulaPasajero) && reserva.getIdItinerario() == idItinerario)
                    return true;
            }

        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }


}
