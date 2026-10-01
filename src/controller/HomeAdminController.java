package src.controller;

import java.io.IOException;
import javax.swing.JFrame;
import src.model.Usuario;
import src.model.UsuarioDAO;
import src.view.HomeAdmin;
import src.controller.AdminController;  
import src.view.AdminGestionarFlota;
import src.view.AdminGestionarItinerario;


public class HomeAdminController {
    private Usuario usuarioLogeado;

    public void iniciarHome(String correo) {
        try{

        this.usuarioLogeado = UsuarioDAO.busquedaPorCorreo(correo);

        } catch (IOException e){
            e.printStackTrace(); 
            System.err.println("*Error al cargar los datos del usuario.");
        }

        HomeAdmin vistaa = new HomeAdmin(usuarioLogeado, this);
        vistaa.setVisible(true); 

    }
    
    public void cerrarSesion(JFrame ventanaActual) {
        ventanaActual.dispose();
    }

    public void llamarGestionarF(JFrame ventanaActual){ //llamada de redirección a Gestionar Flota
        ventanaActual.dispose();

        AdminGestionarFlota vistaFlota = new AdminGestionarFlota();
        AdminController flotaController = new AdminController(vistaFlota);
        vistaFlota.setVisible(true);

    }
}
