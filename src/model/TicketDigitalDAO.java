package src.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TicketDigitalDAO {

    private static final Path RUTA_TICKETS = Paths.get("data", "tickets.txt").toAbsolutePath();

    public static boolean guardarTicket(TicketDigital ticket) {

        try {

            String contenido = ticket.getIdReserva() + "|" + ticket.getNombrePasajero() + "|" + ticket.getCedula() + "|" + ticket.getRuta() + "|" + ticket.getHoraSalida() + "|" + ticket.getTarifaBase() + "|" + ticket.getMontoPagado() + "|" + ticket.getRolPasajero() + "\n";
            Files.write(RUTA_TICKETS, contenido.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
