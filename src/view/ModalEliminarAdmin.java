package src.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import src._helpers.ComponentUtils;
import src.controller.UsuarioController;
import src.model.Usuario;

public class ModalEliminarAdmin extends JDialog {

    private static final Color COLOR_FONDO = new Color(11, 11, 35);
    private static final Color COLOR_PANEL = new Color(19, 29, 61);
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);
    private static final Color COLOR_AZUL = new Color(125, 182, 245);
    private static final Color COLOR_ROJO = new Color(220, 30, 30);

    private final UsuarioController controlador;
    private final DefaultListModel<Usuario> modeloAdministradores = new DefaultListModel<>();
    private final JList<Usuario> listaAdministradores = new JList<>(modeloAdministradores);
    private final JButton botonEliminar = new JButton("Eliminar seleccionados");
    private final JLabel estado = new JLabel(" ");

    public ModalEliminarAdmin(JFrame parent, UsuarioController controlador) {
        super(parent, "Eliminar Administradores", true);
        this.controlador = controlador;
        ComponentUtils.aplicarIcono(this);

        setSize(540, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel titulo = new JLabel("Selecciona los administradores que deseas eliminar");
        titulo.setForeground(COLOR_TEXTO);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        listaAdministradores.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaAdministradores.setBackground(COLOR_PANEL);
        listaAdministradores.setForeground(COLOR_TEXTO);
        listaAdministradores.setSelectionBackground(COLOR_AZUL);
        listaAdministradores.setSelectionForeground(COLOR_FONDO);
        listaAdministradores.setBorder(new EmptyBorder(8, 8, 8, 8));
        listaAdministradores.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Usuario administrador = (Usuario) value;
                setText(administrador.getNombre() + "  |  " + administrador.getCorreo());
                return this;
            }
        });

        JScrollPane desplazamiento = new JScrollPane(listaAdministradores);
        desplazamiento.setBorder(BorderFactory.createLineBorder(COLOR_AZUL));
        panel.add(desplazamiento, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout(8, 8));
        pie.setOpaque(false);
        estado.setForeground(COLOR_TEXTO);
        pie.add(estado, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        botones.setOpaque(false);
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBackground(COLOR_PANEL);
        botonCancelar.setForeground(COLOR_TEXTO);
        botonCancelar.setFocusPainted(false);
        botonCancelar.addActionListener(e -> dispose());

        botonEliminar.setBackground(COLOR_ROJO);
        botonEliminar.setForeground(Color.WHITE);
        botonEliminar.setFocusPainted(false);
        botonEliminar.addActionListener(this::eliminarSeleccionados);

        botones.add(botonCancelar);
        botones.add(botonEliminar);
        pie.add(botones, BorderLayout.EAST);
        panel.add(pie, BorderLayout.SOUTH);

        add(panel);
        cargarAdministradores();
    }

    private void cargarAdministradores() {
        modeloAdministradores.clear();
        try {
            for (Usuario administrador : controlador.obtenerAdministradores())
                modeloAdministradores.addElement(administrador);

            boolean hayAdministradores = !modeloAdministradores.isEmpty();
            botonEliminar.setEnabled(hayAdministradores);
            estado.setText(hayAdministradores ? "Puedes seleccionar varios administradores." : "No hay administradores registrados.");
        } catch (IOException e) {
            botonEliminar.setEnabled(false);
            estado.setText("No se pudo cargar la lista de administradores.");
        }
    }

    private void eliminarSeleccionados(ActionEvent evento) {
        List<Usuario> seleccionados = listaAdministradores.getSelectedValuesList();
        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un administrador.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar " + seleccionados.size() + " administrador(es)?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION)
            return;

        List<String> cedulas = new ArrayList<>();
        for (Usuario administrador : seleccionados)
            cedulas.add(administrador.getCedula());

        try {
            int eliminados = controlador.eliminarAdministradores(cedulas);
            JOptionPane.showMessageDialog(this, eliminados + " administrador(es) eliminado(s).");
            cargarAdministradores();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron eliminar los administradores.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}