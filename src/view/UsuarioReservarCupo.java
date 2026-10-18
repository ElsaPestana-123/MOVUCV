package src.view;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import src._helpers.CobroSolidario;
import src.model.Rutas;
import src.model.RutasDAO;
import src.model.TicketDigital;
import src.model.TicketDigitalDAO;
import src.controller.HomeUsuarioController;
import src.controller.ReservaController;
import src.model.Itinerario;
import src.model.Unidad;
import src.model.UnidadDAO;
import src.model.Usuario;

public class UsuarioReservarCupo extends JPanel {
    private static final Color FONDO = new Color(11, 11, 35);
    private static final Color PANEL = new Color(19, 29, 61);
    private static final Color PANEL_CLARO = new Color(62, 100, 170);
    private static final Color AZUL = new Color(125, 182, 245);
    private static final Color TEXTO = new Color(245, 245, 245);
    private static final Color SECUNDARIO = new Color(150, 150, 150);
    private static final Color VERDE = new Color(30, 150, 60);
    private static final Color ROJO = new Color(210, 50, 60);

    private final Usuario usuario;
    private final Itinerario itinerario;
    private final ReservaController reservaController;
    private final JFrame home;
    private final Runnable alVolver;

    private Unidad unidad;
    private int capacidad;
    private List<Integer> ocupados = new ArrayList<>();
    private int asientoSeleccionado = -1;
    private final List<AsientoBoton> botones = new ArrayList<>();

    private double tarifaBase = 0.0;
    private double montoFinal = 0.0;

    private final JPanel zonaDerecha = new JPanel(new BorderLayout());
    private JLabel lblAsiento;
    private JLabel resAsiento;
    private JButton btnConfirmar;
    private JLabel lblMensaje;
    private String mensajeError = "";

    public UsuarioReservarCupo(Usuario usuario, Itinerario itinerario, ReservaController reservaController,
            JFrame home, HomeUsuarioController controlador, Runnable alVolver) {
        this.usuario = usuario;
        this.itinerario = itinerario;
        this.reservaController = reservaController;
        this.home = home;
        this.alVolver = alVolver;

        unidad = new UnidadDAO().buscarUnidad(itinerario.getPlaca());
        capacidad = 0;
        if (unidad != null)
            capacidad = unidad.getCapacidad();
        cargarOcupados();

        RutasDAO rutasDAO = new RutasDAO();
        for (Rutas r : rutasDAO.listaRutas()) {
            if (r.getNombreRuta().equalsIgnoreCase(itinerario.getRuta())) {
                tarifaBase = r.getTarifa();
                break;
            }
        }
        montoFinal = CobroSolidario.calcularMonto(tarifaBase, usuario.getRol());

        setOpaque(false);
        setLayout(new BorderLayout(20, 0));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        add(crearPanelAsientos(), BorderLayout.WEST);

        zonaDerecha.setOpaque(false);
        add(zonaDerecha, BorderLayout.CENTER);

        mostrarFormulario();
    }

    private void cargarOcupados() {
        try {
            List<Integer> lista = reservaController.obtenerAsientosOcupados(itinerario.getId());
            if (lista == null)
                ocupados = new ArrayList<>();
            else
                ocupados = lista;
        } catch (IOException | RuntimeException e) {
            ocupados = new ArrayList<>();
        }
    }

    private String nombreUsuario() {
        if (usuario == null) return "Usuario";
        return usuario.getNombre();
    }

    private String cedulaUsuario() {
        if (usuario == null) return "";
        return usuario.getCedula();
    }

    private JPanel crearPanelAsientos() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(PANEL_CLARO);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel ruta = etiqueta(itinerario.getRuta(), new Font("SansSerif", Font.BOLD, 18), TEXTO);
        JLabel info = etiqueta("Unidad " + itinerario.getPlaca() + "  |  Salida: " + itinerario.getHoraSalida(),
                new Font("SansSerif", Font.PLAIN, 12), TEXTO);
        JLabel mapa = etiqueta("Mapa de Asientos", new Font("SansSerif", Font.PLAIN, 13), TEXTO);

        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leyenda.setOpaque(false);
        leyenda.setAlignmentX(Component.LEFT_ALIGNMENT);
        leyenda.add(itemLeyenda("Disponible", VERDE));
        leyenda.add(itemLeyenda("Ocupado", ROJO));
        leyenda.add(itemLeyenda("Seleccionado", AZUL));

        JPanel asientos = new JPanel(new GridBagLayout());
        asientos.setOpaque(false);
        asientos.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);

        for (int numero = 1; numero <= capacidad; numero++) {
            int indice = numero - 1;
            int fila = indice / 6;
            int columna = indice % 6;
            gbc.gridy = fila;
            if (columna < 3)
                gbc.gridx = columna;
            else
                gbc.gridx = columna + 1;

            AsientoBoton boton = new AsientoBoton(numero, ocupados.contains(numero));
            boton.addActionListener(e -> seleccionarAsiento(boton.numero));
            botones.add(boton);
            asientos.add(boton, gbc);
        }
        gbc.gridx = 3;
        gbc.gridy = 0;
        asientos.add(Box.createHorizontalStrut(28), gbc);

        JScrollPane scroll = new JScrollPane(asientos);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(ruta);
        p.add(Box.createVerticalStrut(4));
        p.add(info);
        p.add(Box.createVerticalStrut(10));
        p.add(mapa);
        p.add(Box.createVerticalStrut(6));
        p.add(leyenda);
        p.add(Box.createVerticalStrut(8));
        p.add(scroll);
        p.setPreferredSize(new Dimension(360, 100));
        return p;
    }

    private JLabel etiqueta(String texto, Font fuente, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente);
        l.setForeground(color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel itemLeyenda(String texto, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        item.setOpaque(false);
        JPanel cuadro = new JPanel();
        cuadro.setBackground(color);
        cuadro.setPreferredSize(new Dimension(10, 10));
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(TEXTO);
        item.add(cuadro);
        item.add(l);
        return item;
    }

    private void seleccionarAsiento(int numero) {
        asientoSeleccionado = numero;
        for (AsientoBoton b : botones)
            b.setSeleccionado(b.numero == numero);

        if (lblAsiento != null)
            lblAsiento.setText("Asiento #" + numero);
        if (resAsiento != null)
            resAsiento.setText("#" + numero);
        if (btnConfirmar != null)
            btnConfirmar.setEnabled(true);

        mensajeError = "";
        if (lblMensaje != null)
            lblMensaje.setVisible(false);
    }

    private void mostrarFormulario() {
        zonaDerecha.removeAll();

        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBackground(PANEL_CLARO);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel detalles = new JPanel();
        detalles.setOpaque(false);
        detalles.setLayout(new BoxLayout(detalles, BoxLayout.Y_AXIS));

        detalles.add(etiqueta("Detalles de Reserva", new Font("SansSerif", Font.BOLD, 24), TEXTO));
        detalles.add(Box.createVerticalStrut(10));
        detalles.add(etiqueta("Ruta seleccionada", new Font("SansSerif", Font.PLAIN, 12), TEXTO));
        detalles.add(etiqueta(itinerario.getRuta(), new Font("SansSerif", Font.BOLD, 17), TEXTO));
        detalles.add(Box.createVerticalStrut(14));

        String textoAsiento = "Sin seleccionar";
        if (asientoSeleccionado > 0)
            textoAsiento = "Asiento #" + asientoSeleccionado;
        lblAsiento = new JLabel(textoAsiento);

        JPanel fila1 = new JPanel(new GridLayout(1, 2, 12, 0));
        fila1.setOpaque(false);
        fila1.add(campo("Unidad", itinerario.getPlaca()));
        fila1.add(campo("Horario de salida", itinerario.getHoraSalida()));
        fila1.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        detalles.add(fila1);
        detalles.add(Box.createVerticalStrut(10));

        JPanel fila2 = new JPanel(new GridLayout(1, 2, 12, 0));
        fila2.setOpaque(false);
        fila2.add(campoConLabel("Asiento seleccionado", lblAsiento));
        fila2.add(campo("Conductor", itinerario.getConductor()));
        fila2.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        detalles.add(fila2);
        detalles.add(Box.createVerticalStrut(18));

        // NUEVO: Agrupamos los datos del pasajero horizontalmente para ahorrar espacio vertical
        detalles.add(etiqueta("Datos del Pasajero", new Font("SansSerif", Font.BOLD, 14), TEXTO));
        detalles.add(Box.createVerticalStrut(6));
        
        JPanel filaPasajero = new JPanel(new GridLayout(1, 2, 12, 0));
        filaPasajero.setOpaque(false);
        filaPasajero.add(campoPlano("Nombre completo", nombreUsuario()));
        filaPasajero.add(campoPlano("Cédula", cedulaUsuario()));
        filaPasajero.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaPasajero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        detalles.add(filaPasajero);
        detalles.add(Box.createVerticalStrut(14));

        // NUEVO: Desglose de pago / Cobro Solidario
        detalles.add(etiqueta("Desglose de Pago (Cobro Solidario)", new Font("SansSerif", Font.BOLD, 14), TEXTO));
        detalles.add(Box.createVerticalStrut(6));
        
        JPanel filaPago = new JPanel(new GridLayout(1, 2, 12, 0));
        filaPago.setOpaque(false);
        filaPago.add(campoPlano("Tarifa Base", "Bs. " + String.format("%.2f", tarifaBase)));
        
        String nombreRol = CobroSolidario.obtenerNombreRol(usuario.getRol());
        filaPago.add(campoPlano("A Pagar (" + nombreRol + ")", "Bs. " + String.format("%.2f", montoFinal)));
        filaPago.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaPago.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        detalles.add(filaPago);
        detalles.add(Box.createVerticalStrut(20));

        // Botones
        btnConfirmar = new BotonOscuro("Confirmar y Pagar");
        btnConfirmar.setEnabled(asientoSeleccionado > 0);
        btnConfirmar.addActionListener(e -> confirmarReserva());

        JButton btnCancelar = new BotonOscuro("Cancelar");
        btnCancelar.addActionListener(e -> volverASeleccionarRuta());

        JPanel botonesPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        botonesPanel.setOpaque(false);
        botonesPanel.add(btnConfirmar);
        botonesPanel.add(btnCancelar);
        botonesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        botonesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        detalles.add(botonesPanel);
        detalles.add(Box.createVerticalStrut(12));

        lblMensaje = new JLabel();
        lblMensaje.setOpaque(false);
        lblMensaje.setForeground(new Color(255, 100, 100)); // Rojo más claro para destacar sobre el panel azul
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMensaje.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        if (mensajeError.isEmpty()) {
            lblMensaje.setVisible(false);
        } else {
            lblMensaje.setText("¡Atención! " + mensajeError);
            lblMensaje.setVisible(true);
        }
        detalles.add(lblMensaje);

        String textoResumen = "-";
        if (asientoSeleccionado > 0)
            textoResumen = "#" + asientoSeleccionado;
        resAsiento = new JLabel(textoResumen);
        JPanel resumen = crearResumen(false, null);

        panel.add(detalles, BorderLayout.CENTER);
        panel.add(resumen, BorderLayout.EAST);

        zonaDerecha.add(panel, BorderLayout.CENTER);
        zonaDerecha.revalidate();
        zonaDerecha.repaint();
    }

    private JPanel crearResumen(boolean confirmada, String mensaje) {
        JPanel r = new JPanel();
        r.setBackground(Color.WHITE);
        r.setLayout(new BoxLayout(r, BoxLayout.Y_AXIS));
        r.setBorder(new EmptyBorder(18, 20, 18, 20));
        r.setPreferredSize(new Dimension(460, 300));

        String textoTitulo = "Resumen de Reserva";
        Color colorTitulo = FONDO;
        if (confirmada) {
            textoTitulo = "¡Reserva confirmada!";
            colorTitulo = VERDE;
        }

        JLabel titulo = new JLabel(textoTitulo, SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(colorTitulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        r.add(titulo);
        r.add(Box.createVerticalStrut(14));

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        r.add(filaResumen("Ruta", itinerario.getRuta()));
        r.add(Box.createVerticalStrut(4));
        r.add(filaResumen("Placa de unidad", itinerario.getPlaca()));
        r.add(Box.createVerticalStrut(4));
        r.add(filaResumen("Fecha", fecha));
        r.add(Box.createVerticalStrut(4));
        r.add(filaResumen("Horario", itinerario.getHoraSalida()));
        r.add(Box.createVerticalStrut(4));
        r.add(filaResumen("Pasajero", nombreUsuario()));
        r.add(Box.createVerticalStrut(4));
        r.add(filaResumen("Cédula", cedulaUsuario()));
        
        // NUEVO: Información de cobro en el ticket blanco derecho
        r.add(Box.createVerticalStrut(4));
        r.add(filaResumen("Total a Pagar", "Bs. " + String.format("%.2f", montoFinal)));

        if (confirmada) {
            r.add(Box.createVerticalStrut(4));
            r.add(filaResumen("Asiento", "#" + asientoSeleccionado));
        } else {
            JPanel fila = new JPanel(new BorderLayout());
            fila.setOpaque(false);
            JLabel k = new JLabel("Asiento");
            k.setFont(new Font("SansSerif", Font.PLAIN, 12));
            k.setForeground(FONDO);
            resAsiento.setFont(new Font("SansSerif", Font.BOLD, 12));
            resAsiento.setForeground(FONDO);
            fila.add(k, BorderLayout.WEST);
            fila.add(resAsiento, BorderLayout.EAST);
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
            r.add(fila);
        }

        if (confirmada && mensaje != null) {
            r.add(Box.createVerticalStrut(12));
            JLabel area = new JLabel(
                    "<html><div style='text-align: center;'>" + mensaje + "</div></html>",
                    SwingConstants.CENTER);
            area.setOpaque(false);
            area.setFont(new Font("SansSerif", Font.PLAIN, 12));
            area.setForeground(FONDO);
            area.setAlignmentX(Component.CENTER_ALIGNMENT);
            r.add(area);
        }
        return r;
    }

    private JPanel filaResumen(String clave, String valor) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        JLabel k = new JLabel(clave);
        k.setFont(new Font("SansSerif", Font.PLAIN, 12));
        k.setForeground(FONDO);
        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.BOLD, 12));
        v.setForeground(FONDO);
        fila.add(k, BorderLayout.WEST);
        fila.add(v, BorderLayout.EAST);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return fila;
    }

    // NUEVO: Actualizado para disparar el ModalTicketDigital y capturar saldo insuficiente
    private void confirmarReserva() {
        if (asientoSeleccionado <= 0) return;

        String respuesta = reservaController.procesarReserva(usuario, itinerario, asientoSeleccionado);
        boolean exito = respuesta != null && respuesta.startsWith("EXITO");

        if (!exito) {
            String mensaje = respuesta != null ? respuesta.substring(1) : "Error desconocido.";
            
            cargarOcupados();
            for (AsientoBoton b : botones) {
                b.setSeleccionado(false);
                b.setOcupado(ocupados.contains(b.numero));
            }
            asientoSeleccionado = -1;
            mensajeError = mensaje;
            mostrarFormulario();
            return;
        }

        // Si fue exitoso, seccionamos los datos de respuesta
        String[] datosTicket = respuesta.split("\\|");
        String idReserva = datosTicket[1];
        double tarifaBaseTicket = Double.parseDouble(datosTicket[2]);
        double montoPagado = Double.parseDouble(datosTicket[3]);

        for (AsientoBoton b : botones) {
            b.setOcupado(ocupados.contains(b.numero) || b.numero == asientoSeleccionado);
            b.setEnabled(false);
        }

        mostrarConfirmacion("Pago procesado exitosamente. Se debitó Bs. " + String.format("%.2f", montoPagado) + " de su monedero.");

        TicketDigital ticket = new TicketDigital(
                idReserva, usuario.getNombre(), usuario.getCedula(), itinerario.getRuta(), 
                itinerario.getHoraSalida(), tarifaBaseTicket, montoPagado, 
                CobroSolidario.obtenerNombreRol(usuario.getRol()));

        TicketDigitalDAO.guardarTicket(ticket);
    }

    private void mostrarConfirmacion(String mensaje) {
        zonaDerecha.removeAll();

        JPanel panel = new JPanel();
        panel.setBackground(PANEL_CLARO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 60, 20, 60));

        JLabel titulo = new JLabel("Reserva realizada con éxito");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Guarda los datos de tu reserva.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(TEXTO);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel tarjeta = crearResumen(true, mensaje);
        tarjeta.setMaximumSize(new Dimension(460, 330));
        tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnHome = new JButton("Regresar al Home");
        estilizarBoton(btnHome, AZUL, FONDO);
        btnHome.setMaximumSize(new Dimension(260, 42));
        btnHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHome.addActionListener(e -> volverAlInicio());

        panel.add(Box.createVerticalGlue());
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(sub);
        panel.add(Box.createVerticalStrut(16));
        panel.add(tarjeta);
        panel.add(Box.createVerticalStrut(16));
        panel.add(btnHome);
        panel.add(Box.createVerticalGlue());

        zonaDerecha.add(panel, BorderLayout.CENTER);
        zonaDerecha.revalidate();
        zonaDerecha.repaint();
    }

    private void volverASeleccionarRuta() {
        if (alVolver != null)
            alVolver.run();
    }

    private void volverAlInicio() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        if (ventana != null)
            ventana.dispose();
        if (home != null)
            home.setVisible(true);
    }

    private JPanel campo(String titulo, String valor) {
        return campoConLabel(titulo, new JLabel(valor));
    }

    private JPanel campoConLabel(String titulo, JLabel valor) {
        JPanel c = new JPanel();
        c.setBackground(Color.WHITE);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(6, 10, 6, 10));
        JLabel t = new JLabel(titulo);
        t.setFont(new Font("SansSerif", Font.PLAIN, 10));
        t.setForeground(SECUNDARIO);
        valor.setFont(new Font("SansSerif", Font.BOLD, 16));
        valor.setForeground(FONDO);
        c.add(t);
        c.add(valor);
        return c;
    }

    private JPanel campoPlano(String titulo, String valor) {
        JPanel c = new JPanel();
        c.setOpaque(false);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        JLabel t = new JLabel(titulo);
        t.setFont(new Font("SansSerif", Font.PLAIN, 11));
        t.setForeground(TEXTO);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel v = new JLabel(valor, SwingConstants.CENTER);
        v.setOpaque(true);
        v.setBackground(Color.WHITE);
        v.setForeground(FONDO);
        v.setFont(new Font("SansSerif", Font.PLAIN, 13));
        v.setBorder(new EmptyBorder(6, 6, 6, 6));
        v.setAlignmentX(Component.LEFT_ALIGNMENT);
        v.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        c.add(t);
        c.add(v);
        return c;
    }

    private void estilizarBoton(JButton b, Color fondo, Color texto) {
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        b.setBackground(fondo);
        b.setForeground(texto);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static class BotonOscuro extends JButton {
        BotonOscuro(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setBorder(new EmptyBorder(10, 20, 10, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color colorFondo = PANEL;
            if (!isEnabled())
                colorFondo = new Color(19, 29, 61, 140);

            g.setColor(colorFondo);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g.dispose();

            if (isEnabled())
                setForeground(TEXTO);
            else
                setForeground(SECUNDARIO);
            super.paintComponent(graphics);
        }
    }

    private static class AsientoBoton extends JButton {
        final int numero;
        private boolean ocupado;
        private boolean seleccionado;

        AsientoBoton(int numero, boolean ocupado) {
            super(String.valueOf(numero));
            this.numero = numero;
            this.ocupado = ocupado;
            setPreferredSize(new Dimension(38, 32));
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setEnabled(!ocupado);
        }

        void setOcupado(boolean ocupado) {
            this.ocupado = ocupado;
            if (ocupado)
                seleccionado = false;
            setEnabled(!ocupado);
            repaint();
        }

        void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color = VERDE;
            if (ocupado)
                color = ROJO;
            else if (seleccionado)
                color = AZUL;

            g.setColor(color);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g.dispose();

            if (seleccionado)
                setForeground(FONDO);
            else
                setForeground(Color.WHITE);
            super.paintComponent(graphics);
        }
    }
}