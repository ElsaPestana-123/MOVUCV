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

public class RutasDAO {
    private static final Path RUTA_RUTAS = Paths.get("data", "rutas.txt").toAbsolutePath();

    public Rutas leerRuta(String linea) {
        if (linea == null || linea.trim().isEmpty()
                || linea.toLowerCase().startsWith("nombre")) {
            return null;
        }

        String[] datos = linea.split("\\|");
        if (datos.length < 2) {
            return null;
        }

        try {
            String nombre = datos[0].trim();
            Rutas.TipoRuta tipo = Rutas.TipoRuta.valueOf(datos[1].trim().toUpperCase());
            double tarifa = 0.0;

            if (datos.length >= 3) {
                try {
                    tarifa = Double.parseDouble(datos[2].trim());
                } catch (NumberFormatException e) { tarifa = 0.0; }
            }

            if (nombre.isEmpty()) {
                return null;
            }
            return new Rutas(nombre, tipo, tarifa);

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Rutas> listaRutas() {
        List<Rutas> rutas = new LinkedList<>();
        if (!Files.exists(RUTA_RUTAS)) {
            return rutas;
        }

        try (BufferedReader lector = Files.newBufferedReader(RUTA_RUTAS, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                Rutas ruta = leerRuta(linea.trim());
                if (ruta != null) {
                    rutas.add(ruta);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer rutas.txt: " + e.getMessage());
        }
        return rutas;
    }

    public List<String> obtenerNombresRutas() {
        List<String> nombres = new LinkedList<>();
        for (Rutas ruta : listaRutas()) {
            nombres.add(ruta.getNombreRuta());
        }
        return nombres;
    }

    public boolean existeRuta(String nombreRuta) {
        if (nombreRuta == null || nombreRuta.trim().isEmpty()) {
            return false;
        }
        for (Rutas ruta : listaRutas()) {
            if (ruta.getNombreRuta().equalsIgnoreCase(nombreRuta.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean guardarRuta(Rutas ruta) {
        if (ruta == null || ruta.getNombreRuta() == null
                || ruta.getNombreRuta().trim().isEmpty()
                || ruta.getTipoRuta() == null || existeRuta(ruta.getNombreRuta())) {
            return false;
        }

        try {
            String contenido = Files.exists(RUTA_RUTAS) ? Files.readString(RUTA_RUTAS, StandardCharsets.UTF_8) : "";
            String separador = contenido.isEmpty() || contenido.endsWith("\n") || contenido.endsWith("\r\n") ? "" : System.lineSeparator();

            String linea = separador + ruta.getNombreRuta().trim() + "|" + ruta.getTipoRuta().name().toLowerCase() + "|" + ruta.getTarifa() + System.lineSeparator();

            Files.writeString(RUTA_RUTAS, linea, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean actualizarRuta(String nombreOriginal, Rutas rutaActualizada) {
        if (!Files.exists(RUTA_RUTAS) || nombreOriginal == null || rutaActualizada == null
                || nombreOriginal.trim().isEmpty()
                || rutaActualizada.getNombreRuta() == null
                || rutaActualizada.getNombreRuta().trim().isEmpty()
                || rutaActualizada.getTipoRuta() == null) {
            return false;
        }

        List<String> lineas = new LinkedList<>();
        boolean encontrada = false;
        try (BufferedReader lector = Files.newBufferedReader(RUTA_RUTAS, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 2 && datos[0].trim().equalsIgnoreCase(nombreOriginal.trim())) {
                    lineas.add(rutaActualizada.getNombreRuta().trim() + "|"
                            + rutaActualizada.getTipoRuta().name().toLowerCase() + "|" + rutaActualizada.getTarifa());
                    encontrada = true;
                } else {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (!encontrada || (!nombreOriginal.trim().equalsIgnoreCase(rutaActualizada.getNombreRuta().trim())
                && existeRuta(rutaActualizada.getNombreRuta()))) {
            return false;
        }

        try {
            Files.write(RUTA_RUTAS, lineas, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean eliminarRuta(String nombreRuta) {
        if (!Files.exists(RUTA_RUTAS) || nombreRuta == null || nombreRuta.trim().isEmpty()) {
            return false;
        }

        List<String> lineas = new LinkedList<>();
        boolean encontrada = false;
        try (BufferedReader lector = Files.newBufferedReader(RUTA_RUTAS, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 2 && datos[0].trim().equalsIgnoreCase(nombreRuta.trim())) {
                    encontrada = true;
                } else {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (!encontrada) {
            return false;
        }
        try {
            Files.write(RUTA_RUTAS, lineas, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
