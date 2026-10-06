package src.controller;

import java.util.List;
import javax.swing.JOptionPane;
import src._helpers.Validaciones;
import src.model.Unidad;
import src.model.UnidadDAO;
import src.view.AdminGestionarFlota;


public class AdminController {

    private final AdminGestionarFlota vista;
    private final UnidadDAO unidadDAO;
    private boolean Editar = false;
    private String placaOriginal = "";

    public AdminController(AdminGestionarFlota vista) {
        this.vista = vista;
        this.unidadDAO = new UnidadDAO();
        cargarTabla();
        inicioEventos();
    }

    private void cargarTabla() {
        List<Unidad> listaUnidades = unidadDAO.listaUnidades();
        Object[][] listaCompleta = new Object[listaUnidades.size()][4];

        for (int i = 0; i < listaUnidades.size(); i++) {
            Unidad u = listaUnidades.get(i);
            listaCompleta[i][0] = u.getPlaca();
            listaCompleta[i][1] = u.getModelo();
            listaCompleta[i][2] = String.valueOf(u.getCapacidad());
            listaCompleta[i][3] = u.getDisponible();
        }

        vista.cargarDatosEnTabla(listaCompleta);
    }

    // Metodos para los eventos de la vista.
    private void inicioEventos() {
        vista.getBtnLimpiar().addActionListener(e -> {
            vista.limpiarFormulario();
            vista.habilitarCampos();
            vista.getBtnEliminar().setEnabled(false);
            vista.cambiarModoBoton(false);
            vista.getTablaUnidades().clearSelection();
            Editar = false;
        });

        vista.getTablaUnidades().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = vista.getTablaUnidades().getSelectedRow();

                if (fila >= 0) {
                    placaOriginal = vista.getModeloTabla().getValueAt(fila, 0).toString(); //para recordar placa original en caso de cambio
                    String placa = vista.getModeloTabla().getValueAt(fila, 0).toString();
                    String modelo = vista.getModeloTabla().getValueAt(fila, 1).toString();
                    String capacidad = vista.getModeloTabla().getValueAt(fila, 2).toString();
                    String estado = vista.getModeloTabla().getValueAt(fila, 3).toString();

                    vista.setPlaca(placa);
                    vista.setModelo(modelo);
                    vista.setCapacidad(capacidad);
                    vista.setEstado(estado);
                    vista.cambiarModoBoton(true);
                    vista.deshabilitarCampos();
                    vista.getBtnEliminar().setEnabled(true);
                    Editar = true;
                }
            }
        });

        vista.getBtnRegistrar().addActionListener(e -> {
            procesarFormulario();
        });

        vista.getBtnEliminar().addActionListener(e -> {
            eliminarUnidadSeleccionada();
        });
    }

    private void eliminarUnidadSeleccionada() {
        if (placaOriginal.isEmpty()) {
            vista.mostrarError("<html><body>Seleccione una unidad para eliminar.</html></body>");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de que desea eliminar la unidad con placa "
                        + placaOriginal + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (unidadDAO.eliminarUnidad(placaOriginal)) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Unidad eliminada correctamente.");
            cargarTabla();
            vista.limpiarFormulario();
            vista.habilitarCampos();
            vista.getBtnEliminar().setEnabled(false);
            vista.cambiarModoBoton(false);
            vista.getTablaUnidades().clearSelection();
            Editar = false;
            placaOriginal = "";
        } else {
            vista.mostrarError(
                    "<html><body>No se pudo eliminar la unidad seleccionada.</html></body>");
        }
    }

    private void procesarFormulario(){
        vista.limpiarError();

        String placa = vista.getPlaca();
        String modelo = vista.getModelo();
        String capacidad = vista.getCapacidad();
        String disponible = vista.getEstado();

        if(placa.isEmpty() || modelo.isEmpty() || capacidad.isEmpty()){
            vista.mostrarError("<html><body>Todos los campos son obligatorios.<html><body>");
            return;
        }
        if (!Validaciones.validarFormatoPlaca(placa)) {
            vista.mostrarError(
                    "<html><body>La placa debe tener el formato 01XX2XA, "
                    + "donde X es una letra del abecedario.</html></body>");
            return;
        }

        if (!Validaciones.validarFormatoModelo(modelo)) {
            vista.mostrarError(
                    "<html><body>El modelo solo debe contener letras, "
                    + "números, espacios o guiones, y comenzar con una "
                    + "letra mayúscula.</html></body>");
            return;
        }

        int capacidadInt;
        try{
            capacidadInt = Integer.parseInt(capacidad);
        } catch (NumberFormatException ex){
            vista.mostrarError("<html><body>La capacidad debe ser un número entero.</html></body>");
            return;
        }

        if (capacidadInt <= 0) {
            vista.mostrarError(
                    "<html><body>La capacidad debe ser mayor que cero.</html></body>");
            return;
        }

        if(Editar){
            boolean actualizado = unidadDAO.actualizarUnidad(placaOriginal,modelo,capacidadInt,disponible);

            if(actualizado){
                JOptionPane.showMessageDialog(vista, "<html><body>Datos de la Unidad actualizados correctamente.<html><body>");
                cargarTabla();
                vista.limpiarFormulario();
                vista.habilitarCampos();
                vista.getBtnEliminar().setEnabled(false);
                vista.cambiarModoBoton(false);
                vista.getTablaUnidades().clearSelection();
                Editar = false;
                placaOriginal = "";
                placaOriginal = "";
            } else {
                vista.mostrarError("<html><body>No se logro encontrar la unidad para actualizar o hubo un error en la actualización.<html><body>");
            }
        } else {

            boolean registrado = unidadDAO.guardarUnidad(placa, modelo, capacidadInt, disponible);

            if(registrado){
                JOptionPane.showMessageDialog(vista, "<html><body>Unidad registrada exitosamente.<html><body>");
                cargarTabla();
                vista.limpiarFormulario();
            } else {
                vista.mostrarError("<html><body>No se pudo registrar la unidad. Verifique que la placa no esté duplicada.<html><body>");
            }
        }


    }


}