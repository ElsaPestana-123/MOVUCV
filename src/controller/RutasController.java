package src.controller;

import javax.swing.JOptionPane;
import java.util.List;
import src.model.Rutas;
import src.model.RutasDAO;
import src.view.AdminGestionarRutas;

public class RutasController {
    private final AdminGestionarRutas vista;
    private final RutasDAO rutasDAO;
    private boolean editar;
    private String nombreOriginal = "";

    public RutasController(AdminGestionarRutas vista) {
        this.vista = vista;
        this.rutasDAO = new RutasDAO();
        cargarTabla();
        iniciarEventos();
    }

    private void cargarTabla() {
        List<Rutas> rutas = rutasDAO.listaRutas();
        Object[][] datos = new Object[rutas.size()][3]; // AHORA SON 3 COLUMNAS
        for (int i = 0; i < rutas.size(); i++) {
            datos[i][0] = rutas.get(i).getNombreRuta();
            datos[i][1] = tipoVisible(rutas.get(i).getTipoRuta());
            datos[i][2] = String.valueOf(rutas.get(i).getTarifa()); // SE AGREGA LA TARIFA
        }
        vista.cargarDatosEnTabla(datos);
    }

    private void iniciarEventos() {
        vista.getBtnLimpiar().addActionListener(e -> {
            vista.limpiarFormulario();
            editar = false;
            nombreOriginal = "";
            vista.cambiarModoBoton(false);
        });
        vista.getBtnGuardar().addActionListener(e -> registrarRuta());
        vista.getBtnEliminar().addActionListener(e -> eliminarRuta());
        vista.getTablaRutas().getSelectionModel().addListSelectionListener(e -> {
           if (!e.getValueIsAdjusting() && vista.getTablaRutas().getSelectedRow() >= 0) {
            int fila = vista.getTablaRutas().getSelectedRow();
            vista.setNombreRuta(vista.getModeloTabla().getValueAt(fila, 0).toString());
            vista.setTipoRuta(vista.getModeloTabla().getValueAt(fila, 1).toString());
            vista.setTarifa(vista.getModeloTabla().getValueAt(fila, 2).toString()); // CARGA LA TARIFA AL SELECCIONAR
            vista.getBtnEliminar().setEnabled(true);
            editar = true;
            nombreOriginal = vista.getModeloTabla().getValueAt(fila, 0).toString();
            vista.cambiarModoBoton(true);
        }
        });
    }

    private void registrarRuta() {
        String nombreRuta = vista.getNombreRuta().trim();
        String tarifaStr = vista.getTarifa().trim(); // EXTRAE LA TARIFA
        
        if (nombreRuta.isEmpty() || tarifaStr.isEmpty()) {
            vista.mostrarError("Todos los campos son obligatorios.");
            return;
        }

        double tarifaBase = 0.0;
        try {
           
            tarifaBase = Double.parseDouble(tarifaStr.replace(",", "."));
            if (tarifaBase < 0) {
                vista.mostrarError("La tarifa debe ser un monto positivo.");
                return;
            }
        } catch (NumberFormatException ex) {
            vista.mostrarError("La tarifa debe ser un número válido (ej. 1.50).");
            return;
        }

        Rutas ruta = new Rutas(nombreRuta, tipoRuta(), tarifaBase);
        boolean guardada = editar
                ? rutasDAO.actualizarRuta(nombreOriginal, ruta)
                : rutasDAO.guardarRuta(ruta);
        if (!guardada) {
            vista.mostrarError(editar
                    ? "No se pudo actualizar la ruta. Verifique que el nombre no esté duplicado."
                    : "No se pudo registrar la ruta. Verifique que el nombre no esté duplicado.");
            return;
        }
        JOptionPane.showMessageDialog(vista,
                editar ? "Ruta actualizada correctamente." : "Ruta registrada exitosamente.");
        cargarTabla();
        vista.limpiarFormulario();
        editar = false;
        nombreOriginal = "";
        vista.cambiarModoBoton(false);
    }

    private void eliminarRuta() {
        int fila = vista.getTablaRutas().getSelectedRow();
        if (fila < 0) {
            vista.mostrarError("Seleccione una ruta para eliminar.");
            return;
        }

        String ruta = vista.getModeloTabla().getValueAt(fila, 0).toString();
        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de que desea eliminar la ruta " + ruta + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION && rutasDAO.eliminarRuta(ruta)) {
            JOptionPane.showMessageDialog(vista, "Ruta eliminada correctamente.");
            cargarTabla();
            vista.limpiarFormulario();
            editar = false;
            nombreOriginal = "";
            vista.cambiarModoBoton(false);
        } else if (confirmacion == JOptionPane.YES_OPTION) {
            vista.mostrarError("No se pudo eliminar la ruta seleccionada.");
        }
    }

    private Rutas.TipoRuta tipoRuta() {
        return Rutas.TipoRuta.valueOf(vista.getTipoRuta().toUpperCase());
    }

    private String tipoVisible(Rutas.TipoRuta tipo) {
        return tipo == Rutas.TipoRuta.URBANA ? "Urbana" : "Extraurbana";
    }
}
