package src.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO {

    private static final Path RUTA_REGISTRADOS = Paths.get("data","usuarios.txt").toAbsolutePath();
    private static final Path RUTA_AUTORIZADOS = Paths.get("data","cedulas-autorizadas.txt").toAbsolutePath();
    

    //buscar por correo en registrados
    public static Usuario busquedaPorCorreo(String correo){
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

        } catch (IOException e){
            // aca igualmente deberia haber un mensaje de error o retornar algo
        }

        return null;
    }

    //búsqueda por cedula en usuarios-autorizados que devuelve el rol
    public static char busquedaPorCedula(String cedula){

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

        } catch (IOException e){
            // aca deberiamos retornar algo de error de lectura de archivo para el controlador
        }

        return '\0';
    }

    public static boolean actualizarSaldo(double saldo, String correo){

        if(!Files.exists(RUTA_REGISTRADOS))
            return false;

        List<String> archivoActualizado = new ArrayList<>();
        boolean encontrado = false;
        
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
                    encontrado = true;
                }

                archivoActualizado.add(linea);
                
            }
        
        } catch(IOException e){
                return false;
        }

        if(encontrado){ // solo si encontramos reescribimos
            try{
                Files.write(RUTA_REGISTRADOS, archivoActualizado, StandardCharsets.UTF_8);
                return true;

            } catch(IOException e){
                return false;
            }
        }

        return false;
    }
   



    /*public static void main (String args[]){
    //String cedulaPrueba = "24680";
    //char rolEncontrado = busquedaPorCedula(cedulaPrueba);
    //boolean x = actualizarSaldo(9999999999.99, "quintero@miempresa.com");

    /*if (rolEncontrado != '\0') 
        System.out.println(rolEncontrado);

    else 
        System.out.println(-31);
        
    }*/
    
}