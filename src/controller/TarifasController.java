package src.controller;

import javax.swing.JOptionPane;
import src.view.AdminGestionarTarifas;

public class TarifasController {

    private final AdminGestionarTarifas vista;
    
    // Variables para controlar si estamos creando o editando
    private boolean modoEditar = false;
    private String rutaOriginal = ""; 

    // Aquí iría tu DAO futuro, ej: private RutaDAO rutaDAO;

    public TarifasController(AdminGestionarTarifas vista) {
        this.vista = vista;
        // this.rutaDAO = new RutaDAO();
        
        cargarTabla();
        iniciarEventos();
    }

    private void cargarTabla() {
        // Simulación de los datos que traerá tu DAO
        // Cuando lo tengas será: List<Ruta> lista = rutaDAO.obtenerRutas();
        Object[][] datosSimulados = {
            {"Plaza Venezuela - UCV", "Urbana", "15.00"},
            {"La Bandera - UCV", "Urbana", "15.00"},
            {"Maracay - UCV", "Extraurbana", "80.00"}
        };
        vista.cargarDatosEnTabla(datosSimulados);
    }

    private void iniciarEventos() {
        
        // Evento 1: Limpiar
        vista.getBtnLimpiar().addActionListener(e -> {
            vista.limpiarFormulario();
            modoEditar = false;
            rutaOriginal = "";
        });

        // Evento 2: Seleccionar fila de la tabla para EDITAR
        vista.getTablaRutas().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = vista.getTablaRutas().getSelectedRow();

                if (fila >= 0) {
                    // Extraemos los datos de la tabla
                    rutaOriginal = vista.getModeloTabla().getValueAt(fila, 0).toString();
                    String tipo = vista.getModeloTabla().getValueAt(fila, 1).toString();
                    String tarifa = vista.getModeloTabla().getValueAt(fila, 2).toString();

                    // Llenamos el formulario
                    vista.setNombreRuta(rutaOriginal);
                    vista.setTipoRuta(tipo);
                    vista.setTarifa(tarifa);
                    
                    // Activamos modo edición
                    vista.cambiarModoBoton(true);
                    vista.getBtnEliminar().setEnabled(true);
                    modoEditar = true;
                }
            }
        });

        // Evento 3: Guardar o Actualizar
        vista.getBtnGuardar().addActionListener(e -> {
            procesarGuardado();
        });

        // Evento 4: Eliminar
        vista.getBtnEliminar().addActionListener(e -> {
            eliminarRutaSeleccionada();
        });
    }

    private void procesarGuardado() {
        String nombreRuta = vista.getNombreRuta();
        String tipo = vista.getTipoRuta();
        String tarifaStr = vista.getTarifa();

        // Validación de campos vacíos
        if (nombreRuta.isEmpty() || tarifaStr.isEmpty()) {
            vista.mostrarError("Todos los campos son obligatorios.");
            return;
        }

        try {
            tarifaStr = tarifaStr.replace(",", ".");
            double tarifaBase = Double.parseDouble(tarifaStr);

            // Validación de la tarifa
            if (tarifaBase <= 0) {
                vista.mostrarError("La tarifa debe ser mayor a cero.");
                return;
            }

            if (modoEditar) {
                // AQUÍ VA LA LÓGICA DAO PARA ACTUALIZAR
                // boolean actualizado = rutaDAO.actualizarRuta(rutaOriginal, nombreRuta, tipo, tarifaBase);
                
                vista.mostrarExito("Ruta y tarifa actualizadas correctamente.");
                JOptionPane.showMessageDialog(vista, "Datos actualizados para la ruta:\n" + nombreRuta);
            } else {
                // AQUÍ VA LA LÓGICA DAO PARA REGISTRAR NUEVA
                // boolean registrado = rutaDAO.registrarRuta(nombreRuta, tipo, tarifaBase);
                
                vista.mostrarExito("Nueva ruta registrada exitosamente.");
                JOptionPane.showMessageDialog(vista, "Ruta registrada exitosamente:\n" + nombreRuta);
            }

            // Recargamos la tabla y reiniciamos la vista
            cargarTabla(); 
            vista.limpiarFormulario();
            modoEditar = false;
            rutaOriginal = "";

        } catch (NumberFormatException ex) {
            vista.mostrarError("Ingrese un monto válido en la tarifa.");
        }
    }

    private void eliminarRutaSeleccionada() {
        if (rutaOriginal.isEmpty()) {
            vista.mostrarError("Seleccione una ruta para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de que desea eliminar la ruta " + rutaOriginal + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            
            // AQUÍ VA LA LÓGICA DAO PARA ELIMINAR
            // boolean eliminado = rutaDAO.eliminarRuta(rutaOriginal);
            
            JOptionPane.showMessageDialog(vista, "Ruta eliminada correctamente.");
            
            cargarTabla();
            vista.limpiarFormulario();
            modoEditar = false;
            rutaOriginal = "";
        }
    }
}