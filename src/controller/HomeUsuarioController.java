package src.controller;

import java.io.IOException;
import javax.swing.JFrame;
import src.model.Usuario;
import src.model.UsuarioDAO;
import src.view.HomeUsuario;

public class HomeUsuarioController {
    private Usuario usuarioLogeado;

    public void iniciarHome(String correo) {
        try{

        this.usuarioLogeado = UsuarioDAO.busquedaPorCorreo(correo);

        } catch (IOException e){
            e.printStackTrace(); 
            System.err.println("*Error al cargar los datos del usuario.");
        }

        HomeUsuario vista = new HomeUsuario(usuarioLogeado, this);
        vista.setVisible(true); 

    }
    
    public void cerrarSesion(JFrame ventanaActual) {
        ventanaActual.dispose();
    }

}
