package src.controller;

import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import src.model.Itinerario;
import src.model.ItinerarioDAO;
import src.model.Unidad;
import src.model.UnidadDAO;
import src.model.UsuarioDAO;
import src.controller.AdminController;
import src.view.AdminGestionarItinerario;

public class ItinerarioController {

    private ItinerarioDAO itinerarioDAO;
    private UnidadDAO unidadDAO;
    private AdminGestionarItinerario vista;

    private boolean Editar = false;

    public ItinerarioController(AdminGestionarItinerario vista){
        this.vista = vista;
        this.itinerarioDAO = new ItinerarioDAO();
        this.unidadDAO = new UnidadDAO();

        cargarTabla();
        inicioEventos();
    }

    private void cargarTabla(){
        List<Itinerario> itinerarios = itinerarioDAO.listaItinerarios();

        Object[][] lista = new Object[itinerarios.size()][6];

        for (int i = 0; i < itinerarios.size(); i++){
            Itinerario itinerario = itinerarios.get(i);
            lista[i][0] = itinerario.getRuta();
            lista[i][1] = itinerario.getHoraSalida();
            lista[i][2] = itinerario.getPlaca();
            lista[i][3] = itinerario.getConductor();
            lista[i][4] = itinerario.getEstado();
            lista[i][5] = String.valueOf(itinerario.getReservas());
        }

        vista.cargarDatosEnTabla(lista);

    }

    private void inicioEventos(){
        vista.getBtnLimpiarButton().addActionListener(e ->{
            vista.limpiarFormulario();
            vista.cambiarModoBoton(false);
            vista.getTablaUnidades().clearSelection();
            Editar = false;
        });

        vista.getTablaUnidades().getSelectionModel().addListSelectionListener( e -> {
            if (!e.getValueIsAdjusting()){
                int fila = vista.getTablaUnidades().getSelectedRow();

                if(fila >= 0){
                    vista.setRuta(vista.getTabla().getValueAt(fila,0).toString());
                    vista.setHora(vista.getTabla().getValueAt(fila,1).toString());
                    vista.setUnidad(vista.getTabla().getValueAt(fila, 2).toString());
                    vista.setConductor(vista.getTabla().getValueAt(fila,3).toString());
                    //vista.setEstado(vista.getTabla().getValueAt(fila,4).toString());
                    vista.cambiarModoBoton(true);
                    Editar = true;
                }
            }
        });

        vista.getBtnProgramar().addActionListener(e -> procesarFormulario());
    }

    private void procesarFormulario(){
        vista.limpiarError();

        String ruta = vista.getRuta();
        String horaSalida = vista.getHora();
        // String placa = vista.getPlaca();
        String conductor = vista.getConductor();
        // String estado = vista.getEstado();
    }
}
