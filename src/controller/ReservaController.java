package src.controller;

import src.model.Reserva;
import src.model.ReservaDAO;
import java.util.List;
import javax.swing.JOptionPane;
import src._helpers.Validaciones;


public class ReservaController {
    
    private final ReservaDAO reservaDAO;

    public ReservaController() {
        this.reservaDAO = new ReservaDAO();
    }

    public boolean guardarReserva(Reserva reserva) {
        try {
            return reservaDAO.guardarReserva(reserva);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<Integer> obtenerAsientosOcupados(int idItinerario) {
        try {
            return reservaDAO.listaAsientosOcupados(idItinerario);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener los asientos ocupados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

}
