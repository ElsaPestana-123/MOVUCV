package src.controller;

import src.model.Reserva;
import src.model.ReservaDAO;
import src.model.Itinerario;
import src.model.Usuario;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import src.model.Pasaje;
import src.model.PasajeDAO;
import src.model.Rutas;
import src.model.RutasDAO;
import src.model.UsuarioDAO;
import src._helpers.CobroSolidario;

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

    public String procesarReserva(Usuario usuario, Itinerario itinerario, int numeroAsiento) {
        if (!esRutaReservable(itinerario)) 
            return "*La ruta seleccionada no se encuentra disponible para reservas.";
        if (numeroAsiento <= 0) 
            return "*Debe seleccionar un asiento válido en el mapa.";

        try {
            if (reservaDAO.usuarioConReservaenItinerario(usuario.getCedula(), itinerario.getId())) {
                return "*Usted ya cuenta con una reserva activa para este itinerario.";
            }

            if (estaAsientoOcupado(itinerario.getId(), numeroAsiento)) {
                return "*El asiento N° " + numeroAsiento + " ya se encuentra ocupado por otro pasajero.";
            }

            RutasDAO rutasDAO = new RutasDAO();
            double tarifaBase = 0.0;
            for (Rutas r : rutasDAO.listaRutas()) {
                if (r.getNombreRuta().equalsIgnoreCase(itinerario.getRuta())) {
                    tarifaBase = r.getTarifa(); break;
                }
            }

            double montoFinal = CobroSolidario.calcularMonto(tarifaBase, usuario.getRol());

            if (usuario.getSaldo() < montoFinal) {
                return "*Saldo insuficiente para realizar el pago del pasaje.";
            }

            String idReserva = generarIdReservaUnico();
            Reserva nuevaReserva = new Reserva(idReserva, usuario.getCedula(), itinerario.getId(), numeroAsiento, LocalDate.now().toString());

            if (ReservaDAO.guardarReserva(nuevaReserva)) {
                
                double nuevoSaldo = usuario.getSaldo() - montoFinal;
                usuario.setSaldo(nuevoSaldo); 
                UsuarioDAO.actualizarSaldo(usuario.getCedula(), nuevoSaldo); // Actualiza en .txt
                
                Pasaje nuevoPasaje = new Pasaje(itinerario.getRuta(), usuario.getRol(), montoFinal);
                new PasajeDAO().guardarPasaje(nuevoPasaje);

                return "EXITO|" + idReserva + "|" + tarifaBase + "|" + montoFinal;
            }

            return "*Error al registrar la reserva en el sistema.";
        } catch (Exception e) {
            e.printStackTrace();
            return "*Error al acceder a la base de datos de reservas.";
        }
    }

    public boolean guardarReserva(Reserva reserva) { 
        try {
            return ReservaDAO.guardarReserva(reserva); 
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

    public boolean cancelarReserva(String idReserva) { 
        return reservaDAO.eliminarReservaPorId(idReserva);
    }

}
