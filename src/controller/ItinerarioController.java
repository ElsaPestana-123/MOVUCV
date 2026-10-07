package src.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import src._helpers.Validaciones;
import src.model.Itinerario;
import src.model.ItinerarioDAO;
import src.model.Unidad;
import src.model.UnidadDAO;
import src.model.UsuarioDAO;
import src.view.AdminGestionarItinerario;

public class ItinerarioController {

    private ItinerarioDAO itinerarioDAO;
    private UnidadDAO unidadDAO;
    private AdminGestionarItinerario vista;
    private boolean Editar = false;

    private String rutaOriginal = "";
    private String horaOriginal = "";
    private String estadoSeleccionado = "Programado";

    public ItinerarioController(AdminGestionarItinerario vista){
        this.vista = vista;
        this.itinerarioDAO = new ItinerarioDAO();
        this.unidadDAO = new UnidadDAO();

        cargarDesplegables();
        cargarTabla();
        inicioEventos();
    }

    private void cargarDesplegables(){
        
        List<Unidad> unidades = unidadDAO.listaUnidades();
        List<String> placas = new LinkedList<>();

        for(int i = 0; i < unidades.size(); i++){
            Unidad unidad = unidades.get(i);
            if(unidad.getDisponible().equalsIgnoreCase("Operativo")){
                placas.add(unidad.getPlaca());
            }
        }
        vista.cargarUnidadesEnCombo(placas.toArray(new String[0]));

        try {
            List<String> listaConductores = UsuarioDAO.obtenerConductores();
            vista.cargarConductores(listaConductores.toArray(new String[0]));
        } catch (IOException e) {
            vista.mostrarError("Error al cargar la lista de conductores desde el archivo.");
        }
    }

    private void cargarTabla(){
        List<Itinerario> itinerarios = itinerarioDAO.listaItinerarios();
        Object[][] lista = new Object[itinerarios.size()][6];

        for (int i = 0; i < itinerarios.size(); i++){
            Itinerario itinerario = itinerarios.get(i);
            
            lista[i][0] = itinerario.getRuta();
            lista[i][1] = itinerario.getConductor();
            lista[i][2] = itinerario.getPlaca();
            lista[i][3] = itinerario.getHoraSalida();
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
            estadoSeleccionado = "Programado";
        });

        vista.getTablaUnidades().getSelectionModel().addListSelectionListener( e -> {
            if (!e.getValueIsAdjusting()){
                int fila = vista.getTablaUnidades().getSelectedRow();

                if(fila >= 0){
                    rutaOriginal = vista.getTabla().getValueAt(fila, 0).toString();
                    String conductor = vista.getTabla().getValueAt(fila, 1).toString();
                    String placa = vista.getTabla().getValueAt(fila, 2).toString();
                    horaOriginal = vista.getTabla().getValueAt(fila, 3).toString();
                    estadoSeleccionado = vista.getTabla().getValueAt(fila, 4).toString();

                    vista.setRuta(rutaOriginal);
                    vista.setConductor(conductor);
                    vista.setUnidad(placa);
                    vista.setHora(horaOriginal);

                    vista.cambiarModoBoton(true);
                    Editar = true;
                }
            }
        });

        vista.getBtnProgramar().addActionListener(e -> procesarFormulario());
        vista.getBtnCancelar().addActionListener(e -> cancelarItinerarioSeleccionado());
        vista.getBtnEliminar().addActionListener(e -> eliminarItinerarioSeleccionado());
    }

    private void cancelarItinerarioSeleccionado(){
        int fila = vista.getTablaUnidades().getSelectedRow();

        if(fila < 0){
            vista.mostrarError("Seleccione un itinerario para cancelarlo");
            return;
        }

        String ruta = vista.getTabla().getValueAt(fila, 0).toString();
        String horaSalida = vista.getTabla().getValueAt(fila, 3).toString();
        String placa = vista.getTabla().getValueAt(fila, 2).toString();

        boolean cancelado = itinerarioDAO.cancelarItinerario(ruta, horaSalida, placa);

        if(cancelado){
            JOptionPane.showMessageDialog(vista, "Itinerario cancelado correctamente");
            cargarTabla();
            vista.limpiarFormulario();
            vista.cambiarModoBoton(false);
            vista.getTablaUnidades().clearSelection();
            Editar = false;
            estadoSeleccionado = "Programado";
        } else {
            vista.mostrarError("El itinerario seleccionado no se puede cancelar");
        }
    }

    private void eliminarItinerarioSeleccionado(){
        int fila = vista.getTablaUnidades().getSelectedRow();

        if(fila < 0){
            vista.mostrarError("Seleccione un itinerario para eliminarlo");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar el itinerario seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if(confirmacion != JOptionPane.YES_OPTION){
            return;
        }

        String ruta = vista.getTabla().getValueAt(fila, 0).toString();
        String horaSalida = vista.getTabla().getValueAt(fila, 3).toString();
        String placa = vista.getTabla().getValueAt(fila, 2).toString();

        boolean eliminado = itinerarioDAO.eliminarItinerario(ruta, horaSalida, placa);

        if(eliminado){
            JOptionPane.showMessageDialog(vista, "Itinerario eliminado correctamente");
            cargarTabla();
            vista.limpiarFormulario();
            vista.cambiarModoBoton(false);
            vista.getTablaUnidades().clearSelection();
            Editar = false;
            estadoSeleccionado = "Programado";
        } else {
            vista.mostrarError("No se pudo eliminar el itinerario seleccionado");
        }
    }

    private void mostrarErrorValidacion(String mensaje){
        vista.mostrarError(mensaje);
        JOptionPane.showMessageDialog(vista, mensaje, "Error de validación", JOptionPane.ERROR_MESSAGE);
    }

    private void procesarFormulario(){
        vista.limpiarError();

        String ruta = vista.getRuta();
        String horaSalida = vista.getHora();
        String placa = vista.getUnidad();
        String conductor = vista.getConductor();

        if(ruta == null || ruta.trim().isEmpty() || horaSalida == null || horaSalida.trim().isEmpty()){
            mostrarErrorValidacion("La ruta y la hora de salida son obligatorias");
            return;
        }

        if(placa == null || placa.trim().isEmpty() || conductor == null || conductor.trim().isEmpty()){
            mostrarErrorValidacion("Todos los campos son obligatorios");
            return;
        }

        if(!Validaciones.validarHora(horaSalida)){
            mostrarErrorValidacion("La hora de salida no es válida. Use el formato HH:MM entre 05:00 y 20:00");
            return;
        }

        if(!Validaciones.validarRuta(ruta)){
            vista.mostrarError("La ruta no tiene un formato válido");
            return;
        }

        if(Editar){
            boolean actualizado = itinerarioDAO.actualizarItinerario(ruta, horaSalida, placa, conductor, estadoSeleccionado);

            if(actualizado){
                JOptionPane.showMessageDialog(vista, "Itinerario actualizado correctamente");
                cargarTabla();
                vista.limpiarFormulario();
                vista.cambiarModoBoton(false);
                vista.getTablaUnidades().clearSelection();
                Editar = false;
                estadoSeleccionado = "Programado";
            } else {
                vista.mostrarError("Error al actualizar el itinerario");
            }
        } else {
            try{
                char rolUsuario = UsuarioDAO.busquedaPorCedula(conductor);

                if(rolUsuario != 'C'){
                    vista.mostrarError("La cedula no pertenece a un conductor");
                    return;
                }
            } catch (IOException e){
                vista.mostrarError("Error al verificar el conductor");
                return;
            }

            boolean registrado = itinerarioDAO.guardarItinerario(ruta, horaSalida, placa, conductor, "Programado", 0);

            if(registrado){
                JOptionPane.showMessageDialog(vista, "Itinerario programado correctamente");
                cargarTabla();
                vista.limpiarFormulario();
            } else {
                vista.mostrarError("No se pudo programar el itinerario");
            }
        }
    }
}
