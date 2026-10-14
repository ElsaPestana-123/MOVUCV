package src.view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src._helpers.ComponentUtils;
import src.controller.HomeAdminController;
import src.model.Usuario;

public class AdminGestionarRutas extends JFrame {
    private static final Color COLOR_FONDO = new Color(11, 11, 35);
    private static final Color COLOR_PANEL = new Color(19, 29, 61);
    private static final Color COLOR_BOTONES = new Color(125, 182, 245);
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150);
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);

    private final Usuario adminLogeado;
    private final HomeAdminController controlador;
    private JTable tablaRutas;
    private javax.swing.table.DefaultTableModel modeloTabla;
    private JTextField txtNombreRuta;
    private JComboBox<String> comboTipoRuta;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JLabel error;

    public AdminGestionarRutas(HomeAdminController controlador, Usuario adminLogeado) {
        this.controlador = controlador;
        this.adminLogeado = adminLogeado;
        setTitle("MOVUCV - Gestión de Rutas");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        add(cabezal(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(ComponentUtils.menuIzqu(this, controlador), BorderLayout.WEST);
        add(pieDePagina(), BorderLayout.SOUTH);
    }

    private JPanel cabezal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izquierda.setOpaque(false);

        JLabel titulo = new JLabel("MOVUCV");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);

        File logo = new File("res/images/logo sin fondo.png");
        if (logo.exists()) {
            ImageIcon imagenLogo = new ImageIcon(logo.getAbsolutePath());
            Image imagenAjustada = imagenLogo.getImage()
                    .getScaledInstance(100, 40, Image.SCALE_SMOOTH);
            titulo.setIcon(new ImageIcon(imagenAjustada));
            titulo.setIconTextGap(12);
        }

        JLabel separador = new JLabel("|");
        separador.setFont(FUENTE_TITULO);
        separador.setForeground(COLOR_SECUNDARIO);

        JLabel nombrePanel = new JLabel("Panel Administrador");
        nombrePanel.setFont(FUENTE_TITULO);
        nombrePanel.setForeground(COLOR_TEXTO);

        izquierda.add(titulo);
        izquierda.add(separador);
        izquierda.add(nombrePanel);
        panel.add(izquierda, BorderLayout.WEST);

        panel.add(ComponentUtils.textoPresionable(
                adminLogeado != null ? adminLogeado.getNombre() : "Admin",
                COLOR_TEXTO, FUENTE_TEXTO,
                () -> controlador.cerrarSesion(this)), BorderLayout.EAST);
        return panel;
    }

    private JPanel cuerpo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        panel.setOpaque(false);
        panel.add(cuadroTabla());
        panel.add(cuadroFormulario());
        return panel;
    }

    private JPanel cuadroTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setPreferredSize(new Dimension(550, 500));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        JLabel titulo = new JLabel("Rutas");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titulo, BorderLayout.NORTH);
        modeloTabla = new javax.swing.table.DefaultTableModel(null, new String[]{"Nombre de la Ruta", "Tipo"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaRutas = new JTable(modeloTabla);
        tablaRutas.setFont(FUENTE_TEXTO);
        tablaRutas.setRowHeight(30);
        tablaRutas.getTableHeader().setFont(FUENTE_SUBTITULO);
        panel.add(new JScrollPane(tablaRutas), BorderLayout.CENTER);
        return panel;
    }

    private JPanel cuadroFormulario() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_PANEL);
        panel.setPreferredSize(new Dimension(450, 500));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        JLabel titulo = new JLabel("Datos de la Ruta");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        JLabel nombre = new JLabel("Nombre Ruta:");
        nombre.setForeground(COLOR_TEXTO);
        nombre.setFont(FUENTE_SUBTITULO);
        JLabel tipo = new JLabel("Tipo:");
        tipo.setForeground(COLOR_TEXTO);
        tipo.setFont(FUENTE_SUBTITULO);
        txtNombreRuta = new JTextField();
        comboTipoRuta = new JComboBox<>(new String[]{"Urbana", "Extraurbana"});
        configurarCampo(txtNombreRuta);
        configurarCampo(comboTipoRuta);

        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(0, 0, 15, 10);
        restricciones.anchor = GridBagConstraints.WEST;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.weightx = 0;
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        campos.add(nombre, restricciones);

        restricciones.gridx = 1;
        restricciones.insets = new Insets(0, 0, 15, 0);
        restricciones.weightx = 1;
        campos.add(txtNombreRuta, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.insets = new Insets(0, 0, 0, 10);
        restricciones.weightx = 0;
        campos.add(tipo, restricciones);

        restricciones.gridx = 1;
        restricciones.insets = new Insets(0, 0, 0, 0);
        restricciones.weightx = 1;
        campos.add(comboTipoRuta, restricciones);
        panel.add(campos);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        btnGuardar = boton("Registrar Ruta", COLOR_BOTONES);
        btnEliminar = boton("Eliminar Ruta", new Color(190, 70, 70));
        btnEliminar.setEnabled(false);
        btnLimpiar = boton("Limpiar", Color.GRAY);
        btnLimpiar.setMaximumSize(new Dimension(100, 30));
        panel.add(btnGuardar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnEliminar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnLimpiar);
        error = new JLabel(" ");
        error.setFont(FUENTE_TEXTO);
        error.setForeground(Color.RED);
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(error);
        return panel;
    }

    private void configurarCampo(JComponent campo) {
        campo.setFont(FUENTE_TEXTO);
        campo.setPreferredSize(new Dimension(190, 38));
        campo.setMinimumSize(new Dimension(190, 38));
        campo.setMaximumSize(new Dimension(190, 38));
        campo.setBackground(Color.WHITE);
    }

    private JButton boton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_SUBTITULO);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 45));
        return boton;
    }

    private JPanel pieDePagina() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        JLabel texto = new JLabel("MOVUCV | Inicio Seguro | Datos Protegidos");
        texto.setForeground(COLOR_SECUNDARIO);
        texto.setFont(FUENTE_TEXTO);
        panel.add(texto);
        return panel;
    }

    public JTable getTablaRutas() { return tablaRutas; }
    public javax.swing.table.DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public String getNombreRuta() { return txtNombreRuta.getText().trim(); }
    public String getTipoRuta() { return (String) comboTipoRuta.getSelectedItem(); }
    public void setNombreRuta(String nombre) { txtNombreRuta.setText(nombre); }
    public void setTipoRuta(String tipo) { comboTipoRuta.setSelectedItem(tipo); }

    public void cargarDatosEnTabla(Object[][] datos) {
        modeloTabla.setRowCount(0);
        for (Object[] fila : datos) modeloTabla.addRow(fila);
    }

    public void mostrarError(String texto) {
        error.setForeground(new Color(255, 85, 85));
        error.setText(texto);
    }

    public void mostrarExito(String texto) {
        error.setForeground(new Color(46, 204, 113));
        error.setText(texto);
    }

    public void cambiarModoBoton(boolean actualizar) {
        btnGuardar.setText(actualizar ? "Guardar Cambios" : "Registrar Ruta");
    }

    public void limpiarFormulario() {
        txtNombreRuta.setText("");
        comboTipoRuta.setSelectedIndex(0);
        tablaRutas.clearSelection();
        btnEliminar.setEnabled(false);
        error.setText(" ");
    }
}
