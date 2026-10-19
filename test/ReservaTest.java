package test;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.List;
import src.model.Reserva;
import src.model.ReservaDAO;

public class ReservaTest {

    private ReservaDAO reservaDAO = new ReservaDAO();
    private String idReservaPrueba = "RES-TEST-001";


    @Test
    public void guardarReservaTestExito() {
        Reserva nuevaReserva = new Reserva(idReservaPrueba, "777777", 18313, 20, "2026-10-18");

        boolean resultado = ReservaDAO.guardarReserva(nuevaReserva);
        assertTrue(resultado);
    }

    @Test
    public void guardarReservaTestFallo() {
        boolean resultado = ReservaDAO.guardarReserva(null);

        assertFalse(resultado);
    }

    @Test
    public void listaAsientosOcupados() throws IOException {
        List<Integer> ocupados = reservaDAO.listaAsientosOcupados(18313);
        
        assertTrue(ocupados.size() >= 3);
        assertTrue(ocupados.contains(3));
        assertTrue(ocupados.contains(18));
        assertTrue(ocupados.contains(20));
    }

    @Test
    public void existeReserva_IDExistente() throws IOException {
        assertTrue(reservaDAO.existeReserva("RES-72410"));
    }

    @Test
    public void existeReserva_IDFalso() throws IOException {
        assertFalse(reservaDAO.existeReserva("RES-999"));
    }

    @Test
    public void UsuarioYaReservo() throws IOException {
        boolean yaReservo = reservaDAO.usuarioConReservaenItinerario("777777", 18313);
        
        assertTrue(yaReservo);
    }

    @Test
    public void UsuarioNuevo() throws IOException {
        boolean yaReservo = reservaDAO.usuarioConReservaenItinerario("999999", 18313);
        
        assertFalse(yaReservo);
    }
}