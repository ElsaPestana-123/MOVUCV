package src.controller;

import src.view.AdminGestionarTarifas;

public class TarifasController {

    private final AdminGestionarTarifas vista;
    
    private int filaSeleccionada = -1;

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
            filaSeleccionada = -1;
        });

        // Seleccionar una ruta para modificar únicamente su tarifa.
        vista.getTablaRutas().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = vista.getTablaRutas().getSelectedRow();

                if (fila >= 0) {
                    filaSeleccionada = fila;
                    String tarifa = vista.getModeloTabla().getValueAt(fila, 2).toString();
                    vista.setTarifa(tarifa);
                }
            }
        });

        // Guardar la tarifa de la ruta seleccionada.
        vista.getBtnGuardar().addActionListener(e -> {
            guardarTarifa();
        });
    }

    private void guardarTarifa() {
        if (filaSeleccionada < 0) {
            vista.mostrarError("Seleccione una ruta para modificar su tarifa.");
            return;
        }
        String tarifaStr = vista.getTarifa();

        if (tarifaStr.isEmpty()) {
            vista.mostrarError("La tarifa es obligatoria.");
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

            vista.getModeloTabla().setValueAt(String.format("%.2f", tarifaBase), filaSeleccionada, 2);
            vista.mostrarExito("Tarifa actualizada correctamente.");

        } catch (NumberFormatException ex) {
            vista.mostrarError("Ingrese un monto válido en la tarifa.");
        }
    }
}