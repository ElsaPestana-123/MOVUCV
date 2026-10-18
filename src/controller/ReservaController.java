package src.controller;

import src.model.Reserva;
import src.model.ReservaDAO;
import src.model.Itinerario;
import src.model.Usuario;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;


public class ReservaController {
    
    private final ReservaDAO reservaDAO;

    public ReservaController() {
        this.reservaDAO = new ReservaDAO();
    }

    private String generarIdReservaUnico() throws IOException {
        Random random = new Random();
        String nuevoId = "RES-" + (10000 + random.nextInt(90000));

        while (reservaDAO.existeReserva(nuevoId)) {
            nuevoId = "RES-" + (10000 + random.nextInt(90000));
        }
        
        return nuevoId;
    }

    private boolean esRutaReservable(Itinerario itinerario) {
        return itinerario != null && "Programado".equalsIgnoreCase(itinerario.getEstado());
    }

    public String procesarReserva(Usuario usuarioLogueado, Itinerario itinerario, int numeroAsiento) { //verifica formato y guarda reservas
        if (!esRutaReservable(itinerario)) 
            return "*La ruta seleccionada no se encuentra disponible para reservas.";
        

        if (numeroAsiento <= 0) 
            return "*Debe seleccionar un asiento válido en el mapa.";
        

        try {
            if (reservaDAO.usuarioConReservaenItinerario(usuarioLogueado.getCedula(), itinerario.getId())) {
                return "*Usted ya cuenta con una reserva activa para este itinerario.";
            }

            if (estaAsientoOcupado(itinerario.getId(), numeroAsiento)) {
                return "*El asiento N° " + numeroAsiento + " ya se encuentra ocupado por otro pasajero.";
            }

            Reserva nuevaReserva = new Reserva(generarIdReservaUnico(), usuarioLogueado.getCedula(), itinerario.getId(), numeroAsiento, LocalDate.now().toString());

            if (ReservaDAO.guardarReserva(nuevaReserva)) 
                return "Reserva confirmada exitosamente. Proceda al pago";
            

            return "*Error al registrar la reserva en el sistema.";
        } catch (IOException e) {
            e.printStackTrace();
            return "*Error al acceder a la base de datos de reservas.";
        }
    }

    public boolean guardarReserva(Reserva reserva) { //llama a guardar reserva en DAO
        try {
            return reservaDAO.guardarReserva(reserva); 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> obtenerAsientosOcupados(int idItinerario) throws IOException { //para obtener coupados en itinerario
        return reservaDAO.listaAsientosOcupados(idItinerario);
    }

    public boolean estaAsientoOcupado(int idItinerario, int numeroAsiento) throws IOException { // para revisar si un asiento esta ocupado en itinerario
        return obtenerAsientosOcupados(idItinerario).contains(numeroAsiento);
    }

    public boolean cancelarReserva(String idReserva) { //mouski herramienta para más tarde 
    return reservaDAO.eliminarReservaPorId(idReserva);
    }

}
