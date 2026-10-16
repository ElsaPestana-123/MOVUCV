package src.controller;

import java.io.IOException;
import javax.swing.JFrame;
import src.model.Usuario;
import src.model.UsuarioDAO;
import src.view.HomeAdmin; 
import src.view.AdminGestionarFlota;
import src.view.AdminGestionarItinerario;
import src.view.AdminReporte;
import src.view.AdminGestionarRutas;

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
    
    public void cerrarSesion(JFrame ventanaActual) { // cierra sesion en el texto clickleable en el cabezal
        ventanaActual.dispose();
    }

    public void llamarGestionarF(JFrame ventanaActual){ //llamada de redirección a Gestionar Flota
        ventanaActual.dispose();

        AdminGestionarFlota vistaFlota = new AdminGestionarFlota(this, this.usuarioLogeado);
        new AdminController(vistaFlota);
        vistaFlota.setVisible(true);
    }
    
    public void llamarRutas(JFrame ventanaActual){
        ventanaActual.dispose();

        AdminGestionarRutas vistaRutas = new AdminGestionarRutas(this, this.usuarioLogeado);
        new RutasController(vistaRutas);
        vistaRutas.setVisible(true);
    }

    public void llamarGestionarI(JFrame ventanaActual){ //llamada de redirección a Gestionar itinerario
        ventanaActual.dispose();

        AdminGestionarItinerario vistaItinerario = new AdminGestionarItinerario(this, this.usuarioLogeado);
        new ItinerarioController(vistaItinerario);
        vistaItinerario.setVisible(true);
    }

    public void regresarDashboard(JFrame ventanaActual) { // para regresar a dashboaard
        ventanaActual.dispose();

        HomeAdmin dashboard = new HomeAdmin(this.usuarioLogeado, this);
        dashboard.setVisible(true);
    }

    public void llamarReportes(JFrame ventanaActual) { 
        ventanaActual.dispose();

        AdminReporte vistaReportes = new AdminReporte(this, this.usuarioLogeado);
        vistaReportes.setVisible(true);
    }
}
