package src.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.model.*;
import src._helpers.ComponentUtils;
import src.controller.HomeUsuarioController;
public class PasajeroMonedero extends JFrame {

    // colores
    private static final Color COLOR_FONDO = new Color(11, 11, 35);
    private static final Color COLOR_PANEL = new Color(19, 29, 61);
    private static final Color COLOR_BOTONES = new Color(125, 182, 245);
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150);
    private static final Color COLOR_INPUT = new Color(250, 250, 250);
    private static final Color COLOR_ROJO = new Color(220, 30, 30);

    // fuentes
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);
    private static final Font FUENTE_SALDO = new Font("SansSerif", Font.BOLD, 48);
    private static final Font FUENTE_PEQUENA_NEGRITA = new Font("SansSerif", Font.BOLD, 13);

    // variables de estado
    private HomeUsuarioController controlador;
    private Usuario usuarioLogeado;

    // variables de la interfaz
    private JLabel saldoAct;
    private JComboBox<String> comboBanco;
    private JTextField txtTelefono;
    private JTextField txtCedula;
    private JTextField txtReferencia;
    private JTextField txtMonto;
    private JButton btnConfirmar;
    private JButton btnLimpiar;
    private JLabel TextoError;

    public PasajeroMonedero(Usuario usuarioLogeado, HomeUsuarioController controladorController) {
        this.controlador = controladorController;
        this.usuarioLogeado = usuarioLogeado; 
         ComponentUtils.aplicarIcono(this);

        setTitle("MOVUCV - Mi Monedero");
        setSize(1366, 768); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO); 
        setLayout(new BorderLayout());

        add(Cabezal(), BorderLayout.NORTH);
        add(Cuerpo(), BorderLayout.CENTER);
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
            System.err.println("Error: No se encontró el logo en " + ruta);
            titulo.setText("MOVUCV"); 
        }

        JLabel sep = new JLabel("|");
        sep.setFont(FUENTE_TITULO);
        sep.setForeground(COLOR_SECUNDARIO);

        JLabel texto1 = new JLabel("Panel Pasajero");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);

        izq.add(titulo); 
        izq.add(sep); 
        izq.add(texto1); 

        JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        dere.setOpaque(false);
        dere.add(ComponentUtils.textoPresionable(usuarioLogeado.getNombre(), COLOR_TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("|", COLOR_TEXTO, FUENTE_TEXTO, null)); 
                BotonRedondeado regresarHome = new BotonRedondeado("Regresar al Home",COLOR_BOTONES);
        regresarHome.addActionListener(e -> controlador.regresarHome(this)); // le pasamos una función lamda a fin de que sea presionable pero no rediriga
        dere.add(regresarHome);
         BotonRedondeado botonCerrar = new BotonRedondeado("Cerrar Sesión",COLOR_ROJO);
        botonCerrar.addActionListener(e -> {
            controlador.cerrarSesion(this);
        });
        dere.add(botonCerrar);
     
        p.add(izq, BorderLayout.WEST);
        p.add(dere, BorderLayout.EAST);
     
        return p;
    }

    private JPanel Cuerpo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(15, 0, 10, 0));

        p.add(cuadroIzq());
        p.add(cuadroDere());

        return p;
    }

    private JPanel cuadroIzq() {
        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        p.setPreferredSize(new Dimension(620, 500));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); 
        p.setBorder(new EmptyBorder(50, 30, 30, 30));

        JLabel titulo = new JLabel("Saldo Disponible");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_SECUNDARIO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(titulo);

        p.add(Box.createRigidArea(new Dimension(0, 30)));

        saldoAct = new JLabel("Bs. 0.00"); 
        saldoAct.setFont(FUENTE_SALDO); 
        saldoAct.setForeground(COLOR_TEXTO);
        saldoAct.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(saldoAct);

        p.add(Box.createRigidArea(new Dimension(0, 60)));

        JLabel texto1 = new JLabel("Datos para Pago Móvil MOVUCV:");
        texto1.setFont(FUENTE_SUBTITULO);
        texto1.setForeground(COLOR_BOTONES);
        texto1.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto1);
        
        p.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel texto2 = new JLabel("Banco: Banco de Venezuela (0102)");
        texto2.setFont(FUENTE_TEXTO); texto2.setForeground(COLOR_TEXTO); texto2.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto2);
        
        JLabel texto3 = new JLabel("Teléfono: 0412-0000000");
        texto3.setFont(FUENTE_TEXTO); texto3.setForeground(COLOR_TEXTO); texto3.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto3);

        JLabel texto4 = new JLabel("Cédula: J-123456789");
        texto4.setFont(FUENTE_TEXTO); texto4.setForeground(COLOR_TEXTO); texto4.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto4);

        return p;
    }

    private JPanel cuadroDere() {
        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        p.setPreferredSize(new Dimension(620, 500)); 
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(15, 40, 15, 40));

        JLabel texto1 = new JLabel("Registrar Recarga");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);
        texto1.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(texto1);
        p.add(Box.createRigidArea(new Dimension(0, 15)));

        TextoError = new JLabel(" ");
        TextoError.setFont(FUENTE_SUBTITULO);
        TextoError.setForeground(COLOR_ROJO);
        TextoError.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(TextoError);
        p.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel casillas = new JPanel(new GridBagLayout());
        casillas.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 15, 0);

        c.gridx = 0; c.gridy = 0;
        JLabel lblBanco = new JLabel("Banco Emisor:");
        lblBanco.setFont(FUENTE_SUBTITULO); lblBanco.setForeground(COLOR_TEXTO);
        casillas.add(lblBanco, c);

        c.gridx = 1;
        String[] bancos = {"0001 - Banco Central de Venezuela", "0102 - Venezuela", "0104 - Venezolano de Crédito", "0105 - Mercantil", "0108 - Provincial", "0114 - Bancaribe", "0115 - Exterior", "0128 - Caroní", "0134 - Banesco", "0137 - Sofitasa", "0138 - Plaza", "0146 - Bangente", "0151 - Fondo Común", "0156 - 100% Banco", "0157 - Del Sur", "0163 - Tesoro", "0166 - Agrícola de Venezuela", "0168 - Bancrecer", "0169 - Mi Banco", "0171 - Activo", "0172 - Bancamiga", "0173 - Internacional de Desarrollo", "0174 - Banplus", "0175 - Digital de los Trabajadores", "0177 - BANFANB", "0178 - N58 Banco Digital", "0191 - Nacional de Crédito", "0601 - Instituto Municipal de Crédito Popular"};
        comboBanco = new JComboBox<>(bancos);
        comboBanco.setFont(FUENTE_TEXTO); comboBanco.setBackground(COLOR_INPUT);
        casillas.add(comboBanco, c);

        txtTelefono = new JTextField();
        crearCasillas(casillas, c, 1, "Teléfono:", txtTelefono);

        txtCedula = new JTextField();
        crearCasillas(casillas, c, 2, "Cédula/RIF:", txtCedula);

        txtReferencia = new JTextField();
        crearCasillas(casillas, c, 3, "N° Referencia:", txtReferencia);

        txtMonto = new JTextField();
        crearCasillas(casillas, c, 4, "Monto (Bs):", txtMonto);

        p.add(casillas);
        p.add(Box.createRigidArea(new Dimension(0, 20)));

        btnConfirmar = new JButton("Confirmar Pago");
        btnConfirmar.setFont(FUENTE_SUBTITULO);
        btnConfirmar.setBackground(COLOR_BOTONES);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.setMaximumSize(new Dimension(200, 45));
        p.add(btnConfirmar);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(FUENTE_SUBTITULO);
        btnLimpiar.setBackground(Color.GRAY);
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLimpiar.setMaximumSize(new Dimension(100, 30));
        p.add(btnLimpiar);

        return p;
    }

    private void crearCasillas(JPanel casillas, GridBagConstraints c, int n, String texto, JTextField entrada) {
        c.gridx = 0;
        c.gridy = n;
        JLabel texto1 = new JLabel(texto);
        texto1.setFont(FUENTE_SUBTITULO);
        texto1.setForeground(COLOR_TEXTO);
        casillas.add(texto1, c);

        c.gridx = 1;
        c.gridwidth = 1;
        entrada.setFont(FUENTE_TEXTO);
        entrada.setBackground(COLOR_INPUT);
        entrada.setBorder(new EmptyBorder(8, 10, 8, 10));
        casillas.add(entrada, c);
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

     public static class BotonRedondeado extends JButton {
        private Color colorFondo; // boton redondito de acceder

        BotonRedondeado(String texto, Color colorFondo) {
            super(texto);
            this.colorFondo = colorFondo;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setRolloverEnabled(true);

            setFont(FUENTE_PEQUENA_NEGRITA);
            setForeground(COLOR_FONDO);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(5, 20, 5, 20));

        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }


//GETS
    public JButton getBtnConfirmar() { return btnConfirmar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public String getBanco() { return (String) comboBanco.getSelectedItem(); }
    public String getBancoEmisor() { return (String) comboBanco.getSelectedItem(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getCedula() { return txtCedula.getText().trim(); }
    public String getReferencia() { return txtReferencia.getText().trim(); }
    public String getMonto() { return txtMonto.getText().trim(); }
    //SETS
    public void setSaldoActual(String nuevoSaldo) {
        saldoAct.setText("Bs. " + nuevoSaldo);
    }

    public void mostrarError(String texto) {
        TextoError.setForeground(COLOR_ROJO); // Rojo brillante
        TextoError.setText(texto);
    }

    public void mostrarExito(String texto) {
        TextoError.setForeground(new Color(46, 204, 113)); // Verde
        TextoError.setText(texto);
    }

    public void limpiarFormulario() {
        comboBanco.setSelectedIndex(0);
        txtTelefono.setText("");
        txtCedula.setText("");
        txtReferencia.setText("");
        txtMonto.setText("");
        TextoError.setText(" ");
    }
}