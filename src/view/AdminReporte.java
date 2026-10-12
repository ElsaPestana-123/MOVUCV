package src.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import src._helpers.ComponentUtils;
import src.controller.HomeAdminController;
import src.controller.ReporteController;
import src.model.Reporte;
import src.model.Usuario;

public class AdminReporte extends JFrame {

    private static final Color COLOR_FONDO = new Color(11, 11, 35);
    private static final Color COLOR_PANEL = new Color(19, 29, 61);
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);
    private static final Color COLOR_DATOS_FONDO = Color.WHITE;
    private static final Color COLOR_DATOS_TEXTO = new Color(11, 11, 35);
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150);
    private static final Color COLOR_VERDE = new Color(30, 150, 60);
    private static final Color COLOR_BOTONES = new Color(125, 182, 245);

    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);

    private final ReporteController reporteController;
    private final HomeAdminController controlador;
    private final Usuario adminLogeado;
    private JLabel lblEstudiantes;
    private JLabel lblTrabajadores;
    private JLabel lblPublico;
    private JLabel lblTotal;
    private JLabel lblTituloReporte;
    private Reporte reporteActual;

    public AdminReporte(HomeAdminController controlador, Usuario adminLogeado) {
        this.controlador = controlador;
        this.adminLogeado = adminLogeado;
        this.reporteController = new ReporteController();

        setTitle("MOVUCV - Panel de Administrador - Gestionar Reportes");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        add(cabezal(), BorderLayout.NORTH);
        add(ComponentUtils.menuIzqu(this, controlador), BorderLayout.WEST);
        add(cuerpo(), BorderLayout.CENTER);
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
        File archivoLogo = new File("res/images/logo sin fondo.png");
        if (archivoLogo.exists()) {
            Image logo = new ImageIcon(archivoLogo.getAbsolutePath()).getImage()
                    .getScaledInstance(100, 40, Image.SCALE_SMOOTH);
            titulo.setIcon(new ImageIcon(logo));
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

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        derecha.setOpaque(false);
        derecha.add(ComponentUtils.textoPresionable(adminLogeado.getNombre(), COLOR_TEXTO, FUENTE_TEXTO, null));
        derecha.add(ComponentUtils.textoPresionable("|", COLOR_TEXTO, FUENTE_TEXTO, null));
        derecha.add(ComponentUtils.textoPresionable("Cerrar Sesión", COLOR_TEXTO, FUENTE_TEXTO,
                () -> controlador.cerrarSesion(this)));

        panel.add(izquierda, BorderLayout.WEST);
        panel.add(derecha, BorderLayout.EAST);
        return panel;
    }

    private JPanel cuerpo() {
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        cuerpo.setBorder(new EmptyBorder(30, 70, 20, 70));

        JLabel titulo = new JLabel("Gestionar Reportes");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        cuerpo.add(titulo, BorderLayout.NORTH);
        cuerpo.add(panelReporte(), BorderLayout.CENTER);
        return cuerpo;
    }

    private JPanel panelReporte() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel superior = new JPanel();
        superior.setOpaque(false);
        superior.setLayout(new BoxLayout(superior, BoxLayout.Y_AXIS));

        JButton btnGenerar = crearBoton("Generar Reporte", COLOR_BOTONES, Color.BLACK);
        btnGenerar.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTituloReporte = crearEtiqueta("Presione 'Generar Reporte' para iniciar");
        lblTituloReporte.setForeground(COLOR_BOTONES);
        lblTituloReporte.setAlignmentX(Component.CENTER_ALIGNMENT);
        superior.add(btnGenerar);
        superior.add(Box.createRigidArea(new Dimension(0, 15)));
        superior.add(lblTituloReporte);
        panel.add(superior, BorderLayout.NORTH);

        JPanel resultados = new JPanel(new GridLayout(4, 2, 20, 25));
        resultados.setBackground(COLOR_DATOS_FONDO);
        resultados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BOTONES, 1),
                new EmptyBorder(30, 30, 30, 30)));
        resultados.add(crearEtiquetaDatos("Estudiantes:"));
        lblEstudiantes = crearEtiquetaDatos("Bs. 0.00");
        resultados.add(lblEstudiantes);
        resultados.add(crearEtiquetaDatos("Trabajadores / Empleados:"));
        lblTrabajadores = crearEtiquetaDatos("Bs. 0.00");
        resultados.add(lblTrabajadores);
        resultados.add(crearEtiquetaDatos("Público General:"));
        lblPublico = crearEtiquetaDatos("Bs. 0.00");
        resultados.add(lblPublico);
        resultados.add(crearEtiquetaDatos("TOTAL RECAUDADO:"));
        lblTotal = crearEtiquetaDatos("Bs. 0.00");
        resultados.add(lblTotal);
        panel.add(resultados, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        inferior.setOpaque(false);
        JButton btnExportar = crearBoton("Exportar CSV", COLOR_VERDE, Color.WHITE);
        btnExportar.setEnabled(false);
        JButton btnLimpiar = crearBoton("Limpiar", Color.GRAY, Color.WHITE);
        inferior.add(btnExportar);
        inferior.add(btnLimpiar);
        panel.add(inferior, BorderLayout.SOUTH);

        btnGenerar.addActionListener(e -> {
            reporteActual = reporteController.procesarReporte();
            if (reporteActual == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron registros de operaciones ni ingresos.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                btnExportar.setEnabled(false);
                mostrarValores("Bs. 0.00");
                lblTituloReporte.setText("Sin datos para mostrar");
                return;
            }

            lblTituloReporte.setText("Reporte " + reporteActual.getTipoReporte()
                    + " | Número: " + reporteActual.getIdReporte());
            lblEstudiantes.setText(String.format("Bs. %.2f", reporteActual.getTotalEstudiantes()));
            lblTrabajadores.setText(String.format("Bs. %.2f", reporteActual.getTotalTrabajadores()));
            lblPublico.setText(String.format("Bs. %.2f", reporteActual.getTotalPublico()));
            lblTotal.setText(String.format("Bs. %.2f", reporteActual.getTotal()));
            btnExportar.setEnabled(true);
        });

        btnExportar.addActionListener(e -> {
            if (reporteController.manejarExportacion(reporteActual)) {
                JOptionPane.showMessageDialog(this, "Data estructurada exportada exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnLimpiar.addActionListener(e -> {
            reporteActual = null;
            btnExportar.setEnabled(false);
            mostrarValores("Bs. 0.00");
            lblTituloReporte.setText("Presione 'Generar Reporte' para iniciar");
        });
        return panel;
    }

    private void mostrarValores(String valor) {
        lblEstudiantes.setText(valor);
        lblTrabajadores.setText(valor);
        lblPublico.setText(valor);
        lblTotal.setText(valor);
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_SUBTITULO);
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(250, 45));
        return boton;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(COLOR_TEXTO);
        etiqueta.setFont(FUENTE_SUBTITULO);
        return etiqueta;
    }

    private JLabel crearEtiquetaDatos(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(COLOR_DATOS_TEXTO);
        etiqueta.setFont(FUENTE_SUBTITULO);
        return etiqueta;
    }

    private JPanel pieDePagina() {
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pie.setBackground(COLOR_FONDO);
        pie.setBorder(new EmptyBorder(1, 0, 10, 0));
        JLabel texto = new JLabel("MOVUCV | Inicio Seguro | Datos Protegidos");
        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(COLOR_SECUNDARIO);
        pie.add(texto);
        return pie;
    }
}
