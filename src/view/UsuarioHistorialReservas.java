package src.view;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.controller.HomeUsuarioController;
import src.model.Reserva;
import src.model.ReservaDAO;
import src.model.Usuario;
import src._helpers.*;

public class UsuarioHistorialReservas extends JFrame {
    private static final Color FONDO = new Color(11, 11, 35);
    private static final Color PANEL = new Color(19, 29, 61);
    private static final Color AZUL = new Color(125, 182, 245);
    private static final Color TEXTO = new Color(245, 245, 245);
    private static final Color TEXTO_SUAVE = new Color(170, 178, 195);
    private static final Color SECUNDARIO = new Color(150, 150, 150);
    private static final Color ROJO = new Color(220, 30, 30);

    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);

    private final Usuario usuario;
    private final JFrame home;
    private final HomeUsuarioController controlador;
    private final ReservaDAO reservaDAO = new ReservaDAO();

    public UsuarioHistorialReservas(Usuario usuario, JFrame home, HomeUsuarioController controlador) {
        this.usuario = usuario;
        this.home = home;
        this.controlador = controlador;

        setTitle("MOVUCV - Mis Reservas");
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

        JLabel texto1 = new JLabel("Mis Reservas");
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

        HomeUsuario.BotonRedondeado cerrar = new HomeUsuario.BotonRedondeado("Cerrar sesión", ROJO);
        cerrar.addActionListener(e -> cerrarSesion());
        dere.add(cerrar);

        p.add(izq, BorderLayout.WEST);
        p.add(dere, BorderLayout.EAST);
        return p;
    }

    private JPanel construirCentro() {
        JPanel container = new JPanel(new BorderLayout(0, 18));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(26, 42, 22, 42));

        JPanel cabecera = new JPanel();
        cabecera.setOpaque(false);
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Historial de reservas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(TEXTO);

        JLabel subtitulo = new JLabel("Aquí puedes ver tus reservas activas y eliminarlas si es necesario.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitulo.setForeground(TEXTO_SUAVE);

        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(6));
        cabecera.add(subtitulo);

        JPanel listaPanel = new JPanel();
        listaPanel.setOpaque(false);
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        List<Reserva> reservas = obtenerReservasDelUsuario();
        if (reservas.isEmpty()) {
            JLabel vacio = new JLabel("Todavía no tienes reservas registradas.");
            vacio.setFont(new Font("SansSerif", Font.PLAIN, 18));
            vacio.setForeground(TEXTO_SUAVE);
            listaPanel.add(vacio);
        } else {
            for (Reserva reserva : reservas) {
                listaPanel.add(crearTarjetaReserva(reserva));
            }
        }

        JScrollPane scroll = new JScrollPane(listaPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        container.add(cabecera, BorderLayout.NORTH);
        container.add(scroll, BorderLayout.CENTER);
        return container;
    }

    private JPanel crearTarjetaReserva(Reserva reserva) {
        JPanel tarjeta = new JPanel(new BorderLayout(18, 0));
        tarjeta.setBackground(new Color(19, 29, 61));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(125, 182, 245, 90), 1),
                new EmptyBorder(16, 16, 16, 16)
        ));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Reserva " + reserva.getIdReserva());
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(TEXTO);

        JPanel datos = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        datos.setOpaque(false);
        datos.add(itemDato("Itinerario", String.valueOf(reserva.getIdItinerario())));
        datos.add(itemDato("Asiento", "#" + reserva.getNumeroAsiento()));
        datos.add(itemDato("Fecha", reserva.getFecha()));
        datos.add(itemDato("Cedula", reserva.getCedulaPasajero()));

        info.add(titulo);
        info.add(Box.createVerticalStrut(8));
        info.add(datos);

        JPanel lado = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 14));
        lado.setOpaque(false);

        HomeUsuario.BotonRedondeado eliminar = new HomeUsuario.BotonRedondeado("Eliminar", ROJO);
        eliminar.addActionListener(e -> {
            boolean ok = reservaDAO.eliminarReservaPorId(reserva.getIdReserva());
            if (ok) {
                dispose();
                new UsuarioHistorialReservas(usuario, home, controlador).setVisible(true);
            } else {
            }
        });

        lado.add(eliminar);

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

    private List<Reserva> obtenerReservasDelUsuario() {
        List<Reserva> lista = new ArrayList<>();
        Path archivo = Paths.get("data", "reservas.txt").toAbsolutePath();

        if (!Files.exists(archivo)) {
            return lista;
        }

        try (BufferedReader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            boolean primera = true;

            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }

                if (primera) {
                    primera = false;
                    if (linea.toLowerCase().startsWith("id|")) {
                        continue;
                    }
                }

                Reserva reserva = parsearReserva(linea);
                if (reserva != null && usuario != null && reserva.getCedulaPasajero().equals(usuario.getCedula())) {
                    lista.add(reserva);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private Reserva parsearReserva(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return null;
        }

        String[] datos = linea.split("\\|");
        if (datos.length != 5) {
            return null;
        }

        try {
            String idReserva = datos[0].trim();
            String cedula = datos[1].trim();
            int idItinerario = Integer.parseInt(datos[2].trim());
            int asiento = Integer.parseInt(datos[3].trim());
            String fecha = datos[4].trim();
            return new Reserva(idReserva, cedula, idItinerario, asiento, fecha);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private JPanel crearPie() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(PANEL);
        p.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel texto = new JLabel("MOVUCV | Reservas | Datos Protegidos");
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