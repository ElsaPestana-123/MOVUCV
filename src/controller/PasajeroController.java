package src.controller;

import src.view.PasajeroMonedero;
import src.model.Usuario;
import src.model.UsuarioDAO;
import java.io.IOException;

public class PasajeroController {
    
    private PasajeroMonedero vista;
    private Usuario usuario; 

    public PasajeroController(PasajeroMonedero vista, Usuario usuario) {
        this.vista = vista;
        this.usuario = usuario; 
        
        cargarSaldoInicial();
        iniciarEventos();
    }

    private void cargarSaldoInicial() {
        String saldo = String.format("%.2f", usuario.getSaldo());
        vista.setSaldoActual(saldo);
    }

    private void iniciarEventos() {

        vista.getBtnLimpiar().addActionListener(e -> {
            vista.limpiarFormulario();
        });

        // procesar pago
        vista.getBtnConfirmar().addActionListener(e -> {
            String telefono = vista.getTelefono();
            String cedula = vista.getCedula();
            String referencia = vista.getReferencia();
            String montoStr = vista.getMonto();

            // campos vacios
            if (telefono.isEmpty() || cedula.isEmpty() || referencia.isEmpty() || montoStr.isEmpty()) {
                vista.mostrarError("Complete todos los datos bancarios requeridos para procesar la recarga");
                return;
            }

            try {
                double montoRecarga = Double.parseDouble(montoStr);
                
                // monto distinto de cero
                if (montoRecarga <= 0) {
                    vista.mostrarError("El monto a recargar debe ser mayor a cero");
                    return;
                }

                // exito
                double nuevoSaldoTotal = usuario.getSaldo() + montoRecarga; 

                // guardamos en la base el nuevo saldo
                boolean guardadoExitoso = UsuarioDAO.actualizarSaldo(nuevoSaldoTotal, usuario.getCorreo()); 

                if (guardadoExitoso) {
                    // Si se guardó en el txt, actualizamos el objeto Usuario en memoria
                    usuario.setSaldo(nuevoSaldoTotal); 
                    
                    // Actualizamos la pantalla
                    String saldoFormateado = String.format("%.2f", nuevoSaldoTotal);
                    vista.setSaldoActual(saldoFormateado);
                    vista.mostrarExito("Recarga Exitosa");
                    vista.limpiarFormulario();

                } else {
                    vista.mostrarError("Error del sistema: No se pudo registrar la recarga en la base de datos.");
                }

            } catch (NumberFormatException ex) {
                vista.mostrarError("Error: El monto debe ser numérico (Ej: 50.00)");
            } catch (IOException ex) {
                vista.mostrarError("Error crítico al intentar acceder al archivo de base de datos.");
            }
        });
    }
    
}