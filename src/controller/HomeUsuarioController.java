package src.controller;

import java.io.IOException;
import javax.swing.JFrame;
import src.model.Usuario;
import src.model.UsuarioDAO;
import src.view.HomeUsuario;
import src.view.PasajeroMonedero;

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

    public void abrirMonedero(JFrame ventanaActual) {
        ventanaActual.dispose();
        PasajeroMonedero vistaMonedero = new PasajeroMonedero(usuarioLogeado, this);
        new PasajeroController(vistaMonedero, usuarioLogeado);
        vistaMonedero.setVisible(true);
    }

    public void regresarHome(JFrame ventanaActual) {
        ventanaActual.dispose();
        HomeUsuario vistaHome = new HomeUsuario(usuarioLogeado, this);
        vistaHome.setVisible(true);
    }

    
}
