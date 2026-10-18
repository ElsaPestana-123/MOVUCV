package src.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PasajeDAO {

    private static final Path RUTA_PASAJES = Paths.get("data", "pagos_Pasaje.txt").toAbsolutePath();

    public boolean guardarPasaje(Pasaje pasaje){

        if(pasaje == null || pasaje.getRuta() == null || pasaje.getRuta().trim().isEmpty()){
            return false;
        }

        try {
            String linea = pasaje.getRuta().trim() + "|" + Character.toUpperCase(pasaje.getRol()) + "|" + pasaje.getMontoPagado() + System.lineSeparator();
            Files.writeString(RUTA_PASAJES, linea, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
    }
    
}
