package src.controller;

import src.view.PasajeroMonedero;
import src.model.Usuario;
import src.model.UsuarioDAO;
import src.model.Mondero;
import src.model.MonederoDAO;
import src._helpers.Validaciones;

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

    public String getBancoEmisor() {
    
        if (vista.getBancoEmisor() != null) {
            return vista.getBancoEmisor();
        }
        return ""; 
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
           String monto = vista.getMonto();
           String bancoEmisor = vista.getBancoEmisor();

           // Validaciones

            if(telefono.isEmpty() || cedula.isEmpty() || referencia.isEmpty() || monto.isEmpty()){
                vista.mostrarError("Todos los campos son obligatorios.");
                return;
            }

            if (!Validaciones.validarNumeroTelefono(telefono)) {

                vista.mostrarError("El número de teléfono debe tener exactamente 11 dígitos.");
                return;
            }

            if (!Validaciones.validarFormatoCedula(cedula)){
                vista.mostrarError("La Cedula debe tener entre 4 y 8 digitos");
                return;
            }

            if (!Validaciones.validarNumeroReferencia(referencia)){
                vista.mostrarError("La referencia debe tener entre 4 y 13 digitos");
                return;
            }

            try {
                float montoFloat = Float.parseFloat(monto);

                if (!Validaciones.validarMonto(montoFloat)) {
                    vista.mostrarError("El monto debe ser mayor a 0.");
                    return;
                }

                if (bancoEmisor == null || bancoEmisor.trim().isEmpty()) {
                    vista.mostrarError("Debe seleccionar un banco emisor.");
                    return;
                }

                Mondero monedero = new Mondero(bancoEmisor, telefono, cedula, referencia, montoFloat);

                boolean pagoRegistrado = MonederoDAO.guardarPago(monedero);

                if (!pagoRegistrado) {
                    vista.mostrarError("No se pudo registrar el pago");
                    return;
                }

                double nuevoSaldo = usuario.getSaldo() + montoFloat;

                boolean saldoActualizado = UsuarioDAO.actualizarSaldo(usuario.getCedula(), nuevoSaldo);

                if (saldoActualizado){

                    usuario.setSaldo(nuevoSaldo);

                    String saldoFormateado = String.format("%.2f", nuevoSaldo);
                    vista.setSaldoActual(saldoFormateado);

                    vista.limpiarFormulario();
                    vista.mostrarExito("Recarga Exitosa. Referencia #" + referencia + " procesada.");

                } else {
                    vista.mostrarError("No se pudo actualizar el saldo del usuario.");
                }
            } catch (NumberFormatException ex) {
                vista.mostrarError("El monto debe ser un número válido.");
            }
        });
    }
    
}