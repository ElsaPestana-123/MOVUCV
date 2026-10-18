package src.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//throws IOException en los métodos delega el manejo de cualquier error al controlador que lo llamó

public class UsuarioDAO {

    private static final Path RUTA_REGISTRADOS = Paths.get("data","usuarios.txt").toAbsolutePath();
    private static final Path RUTA_AUTORIZADOS = Paths.get("data","comunidad-universitaria-ucv.txt").toAbsolutePath();

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
                    String nombre = (informacionUsuario[2].trim() + " " + informacionUsuario[3].trim()).toUpperCase();
                    String clave = informacionUsuario[5].trim();
                    double saldo = Double.parseDouble(informacionUsuario[6].trim());
                    
                    Usuario usuario = new Usuario(cedula, rol ,nombre, correo, clave, saldo);
                    return usuario;
                }
            }
        }

        return null;
    }

    public static boolean existeCedula(String cedula) throws IOException{
        if(!Files.exists(RUTA_REGISTRADOS))
            return false;

        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_REGISTRADOS, StandardCharsets.UTF_8)){
            
            String linea;
            cedula = cedula.trim();

           while((linea = lectorArchivo.readLine()) != null){
            
                linea = linea.trim();
                if(linea.isEmpty() || linea.startsWith("cedula")) 
                    continue;

                String[] informacionUsuario = linea.split("\\|");

                if(informacionUsuario[0].trim().equalsIgnoreCase(cedula))
                    return true;

            }

        } 
         return false;
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
    public static boolean actualizarSaldo(String cedula, double nuevoSaldo) {
        Path rutaUsuarios = Paths.get("data", "usuarios.txt").toAbsolutePath();
        if (!Files.exists(rutaUsuarios)) return false;

        try {
            List<String> lineas = Files.readAllLines(rutaUsuarios, java.nio.charset.StandardCharsets.UTF_8);
            boolean actualizado = false;

            for (int i = 0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.trim().isEmpty() || linea.toLowerCase().startsWith("nombre|")) continue;

                String[] datos = linea.split("\\|");
                
                if (datos.length >= 7 && datos[0].trim().equals(cedula.trim())) {
                    datos[6] = String.valueOf(nuevoSaldo);
                    
                    lineas.set(i, String.join("|", datos));
                    actualizado = true;
                    break; 
                }
            }

            if (actualizado) {
                Files.write(rutaUsuarios, lineas, java.nio.charset.StandardCharsets.UTF_8);
                return true;
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //inclusión de usuario al final del archivo usuarios.txt
    public static boolean guardarUsuario(String cedula, char rolUsuario, String nombre, String apellido, String correo, String clave, double saldo){
        if(!Files.exists(RUTA_REGISTRADOS))
            return false;

        try {
        String contenidoArchivo = Files.readString(RUTA_REGISTRADOS, StandardCharsets.UTF_8);

        String inicioLinea = "";

        //se asegura de que se agregue el usuario al inicio de la siguiente linea y no al lado del último usuario
        if ((!contenidoArchivo.isEmpty()) && (!contenidoArchivo.endsWith("\n")) && (!contenidoArchivo.endsWith("\r\n"))) {
            inicioLinea = System.lineSeparator();
        }
    
        String lineaAgregada = inicioLinea + cedula + "|" + rolUsuario + "|" + nombre + "|" + apellido + "|" + correo + "|" + clave + "|" + saldo + System.lineSeparator();
       
        Files.writeString(RUTA_REGISTRADOS,lineaAgregada, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        return true; 

        } catch(IOException e){
            e.printStackTrace();
            return false; 
        }
    }

    public static List<String> obtenerConductores() throws IOException {

        List<String> conductores = new LinkedList<>();

        if(!Files.exists(RUTA_REGISTRADOS))
            return conductores;

        try(BufferedReader lectorArchivo = Files.newBufferedReader(RUTA_REGISTRADOS, StandardCharsets.UTF_8)){
            String linea;

            while((linea = lectorArchivo.readLine()) != null){
                linea = linea.trim();
                if(linea.isEmpty() || linea.startsWith("cedula")) 
                    continue;

                String[] informacionUsuario = linea.split("\\|");

                if (informacionUsuario.length >= 2){
                    char rol = informacionUsuario[1].trim().charAt(0);
                    if(rol == 'C'){
                        conductores.add(informacionUsuario[0].trim());
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return conductores;
    } 
}    

    

