package src.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//throws IOException en los métodos delega el manejo de cualquier error al controlador que lo llamó

public class UsuarioDAO {

    private static final Path RUTA_REGISTRADOS = Paths.get("data","usuarios.txt").toAbsolutePath();
    private static final Path RUTA_AUTORIZADOS = Paths.get("data","cedulas-autorizadas.txt").toAbsolutePath();

    public UsuarioDAO(){      
    }
    
    //búsqueda por correo en registrados
    public static Usuario busquedaPorCorreo(String correo) throws IOException { 
        if(!Files.exists(RUTA_REGISTRADOS))
            return null;
        
        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_REGISTRADOS, StandardCharsets.UTF_8)){
            
            String linea;
            correo = correo.trim();


            while((linea = lectorArchivo.readLine()) != null){

                linea = linea.trim();
                if(linea.isEmpty() || linea.startsWith("cedula")) 
                    continue;

                String[] informacionUsuario = linea.split("\\|");

                if(informacionUsuario[4].trim().equalsIgnoreCase(correo)){

                    String cedula = informacionUsuario[0].trim();
                    char rol = informacionUsuario[1].trim().charAt(0);
                    String nombre = informacionUsuario[2].trim() + " " + informacionUsuario[3].trim();
                    String clave = informacionUsuario[5].trim();
                    double saldo = Double.parseDouble(informacionUsuario[6].trim());
                    
                    Usuario usuario = new Usuario(cedula, rol ,nombre, correo, clave, saldo);
                    return usuario;
                }
            }
        }

        return null;
    }

    //búsqueda por cedula en usuarios-autorizados que devuelve el rol
    public static char busquedaPorCedula(String cedula) throws IOException{

        if(!Files.exists(RUTA_AUTORIZADOS))
            return '\0';
        
        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_AUTORIZADOS, StandardCharsets.UTF_8)){
            
            String linea;
            cedula = cedula.trim();

           while((linea = lectorArchivo.readLine()) != null){
            
                linea = linea.trim();
                if(linea.isEmpty() || linea.startsWith("cedula")) 
                    continue;

                String[] informacionUsuario = linea.split("\\|");

                if(informacionUsuario[0].trim().equalsIgnoreCase(cedula))
                    return informacionUsuario[1].trim().charAt(0);

            }

        } 
        
        return '\0';
    }

    //búsqueda en usuarios y modifica el archivo reescribiendolo
    public static boolean actualizarSaldo(double saldo, String correo) throws IOException{

        if(!Files.exists(RUTA_REGISTRADOS))
            return false;

        List<String> archivoActualizado = new ArrayList<>();
        boolean existe = false;
        
        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_REGISTRADOS, StandardCharsets.UTF_8)){

            String linea;
            correo = correo.trim();


            while((linea = lectorArchivo.readLine()) != null){

                linea = linea.trim();
                if(linea.isEmpty() || linea.startsWith("cedula")){
                    archivoActualizado.add(linea);
                    continue;
                }

                String[] informacionUsuario = linea.split("\\|");

                if(informacionUsuario[4].trim().equalsIgnoreCase(correo)){

                    informacionUsuario[6] = String.valueOf(saldo);
                    linea = String.join("|", informacionUsuario);
                    existe = true;
                }

                archivoActualizado.add(linea);
                
            }
        
        } 

        if(existe){ // solo si encontramos al usuario, reescribimos
            try{
                Files.write(RUTA_REGISTRADOS, archivoActualizado, StandardCharsets.UTF_8);
                return true;

            } catch(IOException e){
                return false;
            }
        }

        return false;
    }
   
}
