package src.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import src._helpers.ComponentUtils;
import src.controller.HomeConductorController;
import src.model.Itinerario;
import src.model.Usuario;

public class HomeConductor extends JFrame {
    private static final Color COLOR_FONDO = new Color(11, 11, 35);
    private static final Color COLOR_PANEL = new Color(19, 29, 61);
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150);
    private static final Color COLOR_ROJO = new Color(220, 30, 30);
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);

    private final Usuario usuarioLogeado;
    private final List<Itinerario> itinerarios;
    private final HomeConductorController controlador;

    public HomeConductor(
            Usuario usuarioLogeado,
            List<Itinerario> itinerarios,
            HomeConductorController controlador) {
        this.usuarioLogeado = usuarioLogeado;
        this.itinerarios = itinerarios;
        this.controlador = controlador;
         ComponentUtils.aplicarIcono(this);

        setTitle("MOVUCV - Panel de Conductor");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        add(cabezal(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(pieDePagina(), BorderLayout.SOUTH);
    }

    private JPanel cabezal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel titulo = new JLabel("MOVUCV  |  Panel de Conductor");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        acciones.setOpaque(false);
        JLabel nombre = new JLabel(usuarioLogeado.getNombre());
        nombre.setFont(FUENTE_TEXTO);
        nombre.setForeground(COLOR_TEXTO);

        HomeUsuario.BotonRedondeado botonCerrar =
                new HomeUsuario.BotonRedondeado("Cerrar Sesión", COLOR_ROJO);
        botonCerrar.addActionListener(e -> controlador.cerrarSesion(this));
        acciones.add(nombre);
        acciones.add(botonCerrar);

        panel.add(titulo, BorderLayout.WEST);
        panel.add(acciones, BorderLayout.EAST);
        return panel;
    }

    private JPanel cuerpo() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(45, 60, 40, 60));

        JLabel bienvenida = new JLabel(
                "Bienvenido, " + usuarioLogeado.getNombre() + " - tus itinerarios asignados",
                SwingConstants.CENTER);
        bienvenida.setFont(new Font("SansSerif", Font.BOLD, 28));
        bienvenida.setForeground(COLOR_TEXTO);
        panel.add(bienvenida, BorderLayout.NORTH);

        String[] columnas = {"Ruta", "Hora de salida", "Placa", "Estado", "Reservas"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Itinerario itinerario : itinerarios) {
            modelo.addRow(new Object[] {
                    itinerario.getRuta(),
                    itinerario.getHoraSalida(),
                    itinerario.getPlaca(),
                    itinerario.getEstado(),
                    itinerario.getReservas()
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setFont(FUENTE_TEXTO);
        tabla.setRowHeight(34);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tabla.setFillsViewportHeight(true);
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setPreferredSize(new Dimension(1000, 400));
        desplazamiento.setBorder(BorderFactory.createEmptyBorder());
        panel.add(desplazamiento, BorderLayout.CENTER);

        if (itinerarios.isEmpty()) {
            JLabel sinItinerarios = new JLabel("No tienes itinerarios asignados.", SwingConstants.CENTER);
            sinItinerarios.setFont(FUENTE_TEXTO);
            sinItinerarios.setForeground(COLOR_SECUNDARIO);
            panel.add(sinItinerarios, BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel pieDePagina() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(6, 0, 6, 0));
        JLabel texto = new JLabel("MOVUCV | Panel de Conductor | Datos Protegidos");
        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(COLOR_TEXTO);
        panel.add(texto);
        return panel;
    }
}
