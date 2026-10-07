package src.view;

import src.controller.UsuarioController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModalRegistroAdmin extends JDialog {

    private JTextField txtNombre, txtApellido, txtCedula, txtCorreo;
    private JPasswordField txtClave, txtConfirmar;
    private UsuarioController controlador;

    private static final Color COLOR_FONDO = new Color(11, 11, 35);
    private static final Color COLOR_PANEL = new Color(19, 29, 61);
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);
    private static final Color COLOR_VERDE = new Color(30, 150, 60);

    public ModalRegistroAdmin(JFrame parent, UsuarioController controlador) {
        super(parent, "Registrar Nuevo Administrador", true); // true = modal (bloquea el fondo)
        this.controlador = controlador;

        setSize(400, 500);
        setLocationRelativeTo(parent); 
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new GridLayout(7, 2, 10, 20));
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));

        txtNombre = crearCampoTexto();
        txtApellido = crearCampoTexto();
        txtCedula = crearCampoTexto();
        txtCorreo = crearCampoTexto();
        txtClave = crearCampoOculto();
        txtConfirmar = crearCampoOculto();

        agregarFila(panelPrincipal, "Nombre:", txtNombre);
        agregarFila(panelPrincipal, "Apellido:", txtApellido);
        agregarFila(panelPrincipal, "Cédula:", txtCedula);
        agregarFila(panelPrincipal, "Correo:", txtCorreo);
        agregarFila(panelPrincipal, "Contraseña:", txtClave);
        agregarFila(panelPrincipal, "Confirmar Clave:", txtConfirmar);

        JButton btnRegistrar = new JButton("Registrar Admin");
        btnRegistrar.setBackground(COLOR_VERDE);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> registrar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(COLOR_PANEL);
        btnCancelar.setForeground(COLOR_TEXTO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose()); // Cierra la ventana

        panelPrincipal.add(btnCancelar);
        panelPrincipal.add(btnRegistrar);

        add(panelPrincipal);
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setBackground(COLOR_PANEL);
        campo.setForeground(COLOR_TEXTO);
        campo.setCaretColor(COLOR_TEXTO); // Color del cursor titilante
        campo.setBorder(BorderFactory.createLineBorder(new Color(125, 182, 245)));
        return campo;
    }

    private JPasswordField crearCampoOculto() {
        JPasswordField campo = new JPasswordField();
        campo.setBackground(COLOR_PANEL);
        campo.setForeground(COLOR_TEXTO);
        campo.setCaretColor(COLOR_TEXTO);
        campo.setBorder(BorderFactory.createLineBorder(new Color(125, 182, 245)));
        return campo;
    }

    private void agregarFila(JPanel panel, String textoLabel, JComponent campo) {
        JLabel label = new JLabel(textoLabel);
        label.setForeground(COLOR_TEXTO);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(label);
        panel.add(campo);
    }

    private void registrar() {
   
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String cedula = txtCedula.getText();
        String correo = txtCorreo.getText();
        String clave = new String(txtClave.getPassword());
        String confirmar = new String(txtConfirmar.getPassword());

        String mensaje = controlador.registrarAdmin(nombre, apellido, correo, cedula, clave, confirmar);

        JOptionPane.showMessageDialog(this, mensaje);

        if (mensaje.equals("ADMINISTRADOR REGISTRADO EXITOSAMENTE.")) {
            dispose();
        }
    }
}