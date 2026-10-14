package src.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.model.Usuario;
import src.controller.HomeAdminController;
import src._helpers.ComponentUtils;
public class AdminGestionarTarifas extends JFrame {

    // colores
    private static final Color COLOR_FONDO = new Color(11, 11, 35);
    private static final Color COLOR_PANEL = new Color(19, 29, 61);
    private static final Color COLOR_BOTONES = new Color(125, 182, 245);
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150);
    private static final Color COLOR_INPUT = new Color(250, 250, 250);

    // fuentes
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);

    // variables de estado
    private HomeAdminController controlador;
    private Usuario adminLogeado;

    // variables de la interfaz
    private JTable tablaRutas;
    private javax.swing.table.DefaultTableModel modeloTabla;
    private JTextField txtTarifa;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JLabel error;

    public AdminGestionarTarifas(HomeAdminController controlador, Usuario adminLogeado) {
        this.controlador = controlador;
        this.adminLogeado = adminLogeado;

        setTitle("MOVUCV - Gestión de Tarifas");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        add(Cabezal(), BorderLayout.NORTH);
        add(Cuerpo(), BorderLayout.CENTER);
        add(ComponentUtils.menuIzqu(this,controlador), BorderLayout.WEST);
        add(PieDePagina(), BorderLayout.SOUTH);
    }

    private JPanel Cabezal() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COLOR_PANEL);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izq.setOpaque(false);

        JLabel titulo = new JLabel("MOVUCV");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);

        String ruta = "res/images/logo sin fondo.png";
        java.io.File Logo = new java.io.File(ruta);

        if (Logo.exists()) {
            ImageIcon Imglogo = new ImageIcon(Logo.getAbsolutePath());
            Image LogoMejorado = Imglogo.getImage().getScaledInstance(100, 40, Image.SCALE_SMOOTH);
            titulo.setIcon(new ImageIcon(LogoMejorado));
            titulo.setIconTextGap(12);
        } else {
            titulo.setText("MOVUCV");
        }

        JLabel sep = new JLabel("|");
        sep.setFont(FUENTE_TITULO);
        sep.setForeground(COLOR_SECUNDARIO);

        JLabel texto1 = new JLabel("Panel Administrador");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);

        izq.add(titulo);
        izq.add(sep);
        izq.add(texto1);

        JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        dere.setOpaque(false);
        dere.add(ComponentUtils.textoPresionable(adminLogeado != null ? adminLogeado.getNombre() : "Admin", COLOR_TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("|", COLOR_TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("Cerrar Sesion", COLOR_TEXTO, FUENTE_TEXTO, () -> {
            if (controlador != null) controlador.cerrarSesion(this);
        }));

        p.add(izq, BorderLayout.WEST);
        p.add(dere, BorderLayout.EAST);

        return p;
    }

    private JPanel Cuerpo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(50, 0, 20, 0));

        p.add(cuadroIzq());
        p.add(cuadroDere());

        return p;
    }

    private JPanel cuadroIzq() {
        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        p.setPreferredSize(new Dimension(550, 500));
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titulo = new JLabel("Tarifas");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        p.add(titulo, BorderLayout.NORTH);

        String[] columnas = { "Nombre de la Ruta", "Tipo", "Tarifa Base (Bs)" };
        modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRutas = new JTable(modeloTabla);
        tablaRutas.setFont(FUENTE_TEXTO);
        tablaRutas.setRowHeight(30);
        tablaRutas.getTableHeader().setFont(FUENTE_SUBTITULO);
        
        JScrollPane scroll = new JScrollPane(tablaRutas);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    private JPanel cuadroDere() {
        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        p.setPreferredSize(new Dimension(450, 500));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel texto1 = new JLabel("Actualizar Tarifa");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);
        texto1.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto1);
        p.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel casillas = new JPanel(new GridBagLayout());
        casillas.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 15, 0);

        c.gridx = 0; c.gridy = 0;
        JLabel tarifa = new JLabel("Tarifa (Bs):");
        tarifa.setFont(FUENTE_SUBTITULO);
        tarifa.setForeground(COLOR_TEXTO);
        casillas.add(tarifa, c);

        c.gridx = 1;
        txtTarifa = new JTextField();
        txtTarifa.setFont(FUENTE_TEXTO);
        txtTarifa.setBackground(COLOR_INPUT);
        txtTarifa.setBorder(new EmptyBorder(8, 10, 8, 10));
        casillas.add(txtTarifa, c);

        casillas.setMaximumSize(new Dimension(450, 80));
        p.add(casillas);
        
        p.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón Guardar / Registrar
        btnGuardar = new JButton("Guardar Tarifa");
        btnGuardar.setFont(FUENTE_SUBTITULO);
        btnGuardar.setBackground(COLOR_BOTONES);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(200, 45));
        p.add(btnGuardar);
        
        p.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botón Limpiar
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(FUENTE_SUBTITULO);
        btnLimpiar.setBackground(Color.GRAY);
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLimpiar.setMaximumSize(new Dimension(100, 30));
        p.add(btnLimpiar);

        p.add(Box.createRigidArea(new Dimension(0, 15)));

  //mensajes de errores
        error = new JLabel(" ");
        error.setFont(FUENTE_TEXTO);
        error.setForeground(Color.RED);
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(error);

        p.add(Box.createVerticalGlue());

        return p;
    }

    private JPanel PieDePagina() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(COLOR_FONDO);
        p.setBorder(new EmptyBorder(1, 0, 10, 0));
        JLabel texto = new JLabel("MOVUCV | Inicio Seguro | Datos Protegidos");
        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(COLOR_SECUNDARIO);
        p.add(texto);
        return p;
    }

    //getters

    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JTable getTablaRutas() { return tablaRutas; }
    public javax.swing.table.DefaultTableModel getModeloTabla() { return modeloTabla; }

    public String getTarifa() { return txtTarifa.getText().trim(); }
//settters
    public void setTarifa(String tarifa) { txtTarifa.setText(tarifa); }

    public void cargarDatosEnTabla(Object[][] datos) {
        modeloTabla.setRowCount(0);
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    public void cambiarModoBoton(boolean actualizar) {
        if (actualizar) {
            btnGuardar.setText("Guardar Cambios");
        } else {
            btnGuardar.setText("Guardar Tarifa");
        }
    }

    public void mostrarError(String texto) {
        error.setForeground(new Color(255, 85, 85));
        error.setText(texto);
        txtTarifa.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    public void mostrarExito(String mensaje) {
        error.setForeground(new Color(46, 204, 113));
        error.setText(mensaje);
        txtTarifa.setBorder(new EmptyBorder(8, 10, 8, 10));
    }

    public void limpiarFormulario() {
        txtTarifa.setText("");
        txtTarifa.setBorder(new EmptyBorder(8, 10, 8, 10));
        error.setText(" ");
        
        cambiarModoBoton(false);
        tablaRutas.clearSelection();
    }
}