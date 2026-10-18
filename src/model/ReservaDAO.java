package src.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

public class ReservaDAO {
    private static final Path RUTA_RESERVAS = Paths.get("data", "reservas.txt").toAbsolutePath();

    public Reserva leerReserva(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return null;
        }

        String texto = linea.trim();

        // Ignora la cabecera del archivo
        if (texto.toLowerCase().startsWith("id|")) {
            return null;
        }

        String[] infoReserva = texto.split("\\|");
        if (infoReserva.length != 5) {
            return null;
        }

        try {
            String idReserva = infoReserva[0].trim();
            String cedulaPasajero = infoReserva[1].trim();
            int idItinerario = Integer.parseInt(infoReserva[2].trim());
            int numeroAsiento = Integer.parseInt(infoReserva[3].trim());
            String fecha = infoReserva[4].trim();

            return new Reserva(idReserva, cedulaPasajero, idItinerario, numeroAsiento, fecha);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean guardarReserva(Reserva reserva) {
        if (reserva == null) {
            return false;
        }

        try {
            Path carpeta = RUTA_RESERVAS.getParent();
            if (carpeta != null && !Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }

            // Si el archivo no existe, lo crea y escribe la cabecera
            boolean archivoExiste = Files.exists(RUTA_RESERVAS);
            String contenido = archivoExiste? Files.readString(RUTA_RESERVAS, StandardCharsets.UTF_8): "";

            try (BufferedWriter writer = Files.newBufferedWriter(
                    RUTA_RESERVAS,
                    StandardCharsets.UTF_8,
                    archivoExiste ? StandardOpenOption.APPEND : StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
            )) {
                if (contenido.isEmpty()) {
                writer.write("id|cedula|itinerarioID|asiento|fecha");
                writer.newLine();
            } else if (!contenido.endsWith("\n") && !contenido.endsWith("\r")) {
                writer.newLine();
            }

                writer.write(
                    reserva.getIdReserva() + "|" +
                    reserva.getCedulaPasajero() + "|" +
                    reserva.getIdItinerario() + "|" +
                    reserva.getNumeroAsiento() + "|" +
                    reserva.getFecha()
                );
                writer.newLine();
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> listaAsientosOcupados(int idItinerario) throws IOException {
        List<Integer> asientosOcupados = new LinkedList<>();

        if (!Files.exists(RUTA_RESERVAS)) {
            return asientosOcupados;
        }

        try (BufferedReader lector = Files.newBufferedReader(RUTA_RESERVAS, StandardCharsets.UTF_8)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }

                if (linea.toLowerCase().startsWith("id|")) {
                    continue; // ignora la cabecera
                }

                Reserva reserva = leerReserva(linea);

                if (reserva != null && reserva.getIdItinerario() == idItinerario) {
                    asientosOcupados.add(reserva.getNumeroAsiento());
                }
            }
        }

        return asientosOcupados;
    }

    public boolean existeReserva(String idReserva) throws IOException {
        if (!Files.exists(RUTA_RESERVAS)) {
            return false;
        }

        try (BufferedReader lector = Files.newBufferedReader(RUTA_RESERVAS, StandardCharsets.UTF_8)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }

                if (linea.toLowerCase().startsWith("id|")) {
                    continue;
                }

                Reserva reserva = leerReserva(linea);

                if (reserva != null && reserva.getIdReserva().equals(idReserva)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean usuarioConReservaenItinerario(String cedulaPasajero, int idItinerario) throws IOException {
        if (!Files.exists(RUTA_RESERVAS)) {
            return false;
        }

        try (BufferedReader lector = Files.newBufferedReader(RUTA_RESERVAS, StandardCharsets.UTF_8)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }

                if (linea.toLowerCase().startsWith("id|")) {
                    continue;
                }

                Reserva reserva = leerReserva(linea);

                if (reserva != null
                        && reserva.getCedulaPasajero().equals(cedulaPasajero)
                        && reserva.getIdItinerario() == idItinerario) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean eliminarReservaPorId(String idReserva) {
        if (idReserva == null || idReserva.trim().isEmpty()) {
            return false;
        }

        Path rutaOriginal = RUTA_RESERVAS;
        Path rutaTmp = Paths.get("data", "reservas_tmp.txt").toAbsolutePath();

        if (!Files.exists(rutaOriginal)) {
            return false;
        }

        try (BufferedReader lector = Files.newBufferedReader(rutaOriginal, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(rutaTmp, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = lector.readLine()) != null) {
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }

                if (primeraLinea) {
                    primeraLinea = false;
                    if (linea.toLowerCase().startsWith("id|")) {
                        writer.write(linea);
                        writer.newLine();
                        continue;
                    }
                }

                Reserva reserva = leerReserva(linea);
                if (reserva != null && reserva.getIdReserva().equals(idReserva)) {
                    continue; // la salta, la elimina
                }

                writer.write(linea);
                writer.newLine();
            }

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            Files.deleteIfExists(rutaOriginal);
            Files.move(rutaTmp, rutaOriginal, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean usuarioTieneReservaEnHorario(String cedula, Itinerario nuevo)
        throws IOException {

    if (cedula == null || nuevo == null || !Files.exists(RUTA_RESERVAS)) {
        return false;
    }

    List<Itinerario> itinerarios = new ItinerarioDAO().listaItinerarios();

    try (BufferedReader lector =
                 Files.newBufferedReader(RUTA_RESERVAS, StandardCharsets.UTF_8)) {
        String linea;

        while ((linea = lector.readLine()) != null) {
            Reserva reserva = leerReserva(linea);

            if (reserva == null
                    || !reserva.getCedulaPasajero().equals(cedula.trim())) {
                continue;
            }

            for (Itinerario existente : itinerarios) {
                if (existente.getId() == reserva.getIdItinerario()
                        && existente.getHoraSalida().equals(nuevo.getHoraSalida())) {
                    return true;
                }
            }
        }
    }

    return false;
}
}
