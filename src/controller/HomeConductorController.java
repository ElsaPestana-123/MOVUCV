package src.controller;

import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import src.model.Itinerario;
import src.model.ItinerarioDAO;
import src.model.Usuario;
import src.model.UsuarioDAO;
import src.view.HomeConductor;

public class HomeConductorController {
    public void iniciarHome(String correo){
        try{
            Usuario usuarioLogeado = UsuarioDAO.busquedaPorCorreo(correo);
            if(usuarioLogeado == null || usuarioLogeado.getRol() != 'C'){
                JOptionPane.showMessageDialog(null, "No se pudieron cargar los datos del conductor.");
                return;
            }

            List<Itinerario> itinerarios = new ItinerarioDAO()
                    .listaItinerariosPorConductor(usuarioLogeado.getCedula());
            HomeConductor vista = new HomeConductor(usuarioLogeado, itinerarios, this);
            vista.setVisible(true);
        } catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del conductor.");
            e.printStackTrace();
        }
    }

    public void cerrarSesion(JFrame ventanaActual){
        ventanaActual.dispose();
    }
}