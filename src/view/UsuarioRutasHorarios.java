package src.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.controller.HomeUsuarioController;
import src.model.Itinerario;
import src.model.ItinerarioDAO;
import src.model.Usuario;
import src._helpers.*;

public class UsuarioRutasHorarios extends JFrame {
    private static final Color FONDO = new Color(11, 11, 35);
    private static final Color PANEL = new Color(19, 29, 61);
    private static final Color AZUL = new Color(125, 182, 245);
    private static final Color TEXTO = new Color(245, 245, 245);
    private static final Color TEXTO_SUAVE = new Color(170, 178, 195);
    private static final Color SECUNDARIO = new Color(150, 150, 150);
    private static final Color ROJO = new Color(190, 55, 55);

    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);

    private final Usuario usuario;
    private final JFrame home;

    public UsuarioRutasHorarios(Usuario usuario, JFrame home, HomeUsuarioController controlador) {
        this.usuario = usuario;
        this.home = home;
         ComponentUtils.aplicarIcono(this);

        setTitle("MOVUCV - Rutas y Horarios");
        setSize(1366, 768);
        setMinimumSize(new Dimension(980, 620));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);

        add(crearCabecera(), BorderLayout.NORTH);
        add(construirCentro(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
    }

    private JPanel crearCabecera() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izq.setOpaque(false);

        JLabel titulo = new JLabel("MOVUCV");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(TEXTO);

        JLabel sep = new JLabel("|");
        sep.setFont(FUENTE_TITULO);
        sep.setForeground(SECUNDARIO);

        JLabel texto1 = new JLabel("Rutas y Horarios");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(TEXTO);

        izq.add(titulo);
        izq.add(sep);
        izq.add(texto1);

        JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8));
        dere.setOpaque(false);

        String nombre = (usuario == null) ? "Usuario" : usuario.getNombre();
        dere.add(ComponentUtils.textoPresionable(nombre, TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("|", TEXTO, FUENTE_TEXTO, null));

        HomeUsuario.BotonRedondeado regresarHome = new HomeUsuario.BotonRedondeado("Regresar al Home", AZUL);
        regresarHome.addActionListener(e -> volverAlInicio());
        dere.add(regresarHome);

        HomeUsuario.BotonRedondeado cerrar = new HomeUsuario.BotonRedondeado("Cerrar Sesión", ROJO);
        cerrar.addActionListener(e -> cerrarSesion());
        dere.add(cerrar);

        p.add(izq, BorderLayout.WEST);
        p.add(dere, BorderLayout.EAST);
        return p;
    }

    private JPanel construirCentro() {
        JPanel container = new JPanel(new BorderLayout(0, 18));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(26, 42, 20, 42));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setOpaque(false);
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Horarios disponibles");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(TEXTO);

        JLabel subtitulo = new JLabel("Consulta rutas, horarios de salida y estado del servicio.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitulo.setForeground(TEXTO_SUAVE);

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createVerticalStrut(6));
        tituloPanel.add(subtitulo);

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBorder(new EmptyBorder(8, 0, 0, 0));

        List<Itinerario> itinerarios = new ItinerarioDAO().listaItinerarios();
        if (itinerarios == null || itinerarios.isEmpty()) {
            JLabel vacio = new JLabel("No hay rutas disponibles por el momento.");
            vacio.setForeground(TEXTO_SUAVE);
            vacio.setFont(new Font("SansSerif", Font.PLAIN, 18));
            lista.add(vacio);
        } else {
            for (Itinerario itinerario : itinerarios) {
                lista.add(crearTarjetaRuta(itinerario));
            }
        }

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        container.add(tituloPanel, BorderLayout.NORTH);
        container.add(scroll, BorderLayout.CENTER);

        return container;
    }

    private JPanel crearTarjetaRuta(Itinerario itinerario) {
        JPanel tarjeta = new JPanel(new BorderLayout(18, 12));
        tarjeta.setBackground(new Color(19, 29, 61));
       tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(125, 182, 245, 30), 1), new EmptyBorder(8, 12, 8, 12)));

        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel ruta = new JLabel(itinerario.getRuta());
        ruta.setFont(new Font("SansSerif", Font.BOLD, 22));
        ruta.setForeground(TEXTO);

        JPanel datos = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        datos.setOpaque(false);

        datos.add(itemDato("Salida", itinerario.getHoraSalida()));
        datos.add(itemDato("Placa", itinerario.getPlaca()));
        datos.add(itemDato("Conductor", itinerario.getConductor()));
        datos.add(itemDato("Estado", itinerario.getEstado()));

        info.add(ruta);
        info.add(Box.createVerticalStrut(8));
        info.add(datos);

        JPanel lado = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        lado.setOpaque(false);
        tarjeta.add(info, BorderLayout.CENTER);
        tarjeta.add(lado, BorderLayout.EAST);

        return tarjeta;
    }

    private JPanel itemDato(String label, String valor) {
        JPanel dato = new JPanel();
        dato.setOpaque(false);
        dato.setLayout(new BoxLayout(dato, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(TEXTO_SUAVE);

        JLabel val = new JLabel(valor);
        val.setFont(new Font("SansSerif", Font.BOLD, 14));
        val.setForeground(TEXTO);

        dato.add(lbl);
        dato.add(val);
        return dato;
    }

    private JPanel crearPie() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(PANEL);
        p.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel texto = new JLabel("MOVUCV | Rutas | Datos Protegidos");
        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(TEXTO);
        p.add(texto);

        return p;
    }

    private void volverAlInicio() {
        dispose();
        if (home != null) {
            home.setVisible(true);
        }
    }

    private void cerrarSesion() {
        dispose();
        System.exit(0);
    }
}