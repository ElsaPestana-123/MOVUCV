package src.model;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MonederoDAO {

    private static final Path RUTA_PAGOS = Paths.get("data", "pagos.txt").toAbsolutePath();
   
    public MonederoDAO(){

    }

    public static boolean guardarPago(Mondero mondero){

        try{
            String linea = mondero.getBancoEmisor() + "|" + mondero.getNumeroTelefono() + "|" + mondero.getCedula() + "|" + mondero.getNumeroReferencia() + "|" + mondero.getMonto();
            Files.write(RUTA_PAGOS, (linea + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (java.io.IOException e){
            return false;
        }

    }
    
}
