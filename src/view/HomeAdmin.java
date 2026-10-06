package src.view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.model.Usuario;
import src.model.Unidad;
import src.model.UnidadDAO;
import src.model.Itinerario;
import src.model.ItinerarioDAO;
import src.model.UsuarioDAO;
import src._helpers.ComponentUtils;
import src._helpers.ComponentUtils.Tarjeta;
import src.controller.HomeAdminController;

public class HomeAdmin extends JFrame {
    private Usuario adminLogeado;
    private HomeAdminController controlador;
 
    //colores
    private static final Color COLOR_FONDO = new Color(11, 11, 35);       // Azul muy oscuro (Fondo general y Cabecera)
    private static final Color COLOR_PANEL = new Color(19, 29, 61);       // Azul oscuro (Para ambas tarjetas)
    private static final Color COLOR_BOTONES = new Color(125, 182, 245);   // Azul claro (Botones y enlaces)
    private static final Color COLOR_TEXTO = new Color(245, 245, 245);    // Blanco humo (Textos principales)
    private static final Color COLOR_SECUNDARIO = new Color(150, 150, 150); // Gris (Textos secundarios, separadores)
    private static final Color COLOR_VERDE = new Color(30, 150, 60);
    private static final Color COLOR_NARANJA = new Color(235, 140, 0);
    private static final Color COLOR_ROJO = new Color(220, 30, 30);
 
    // fuentes
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);
    private static final Font FUENTE_TARJETA_TITULO = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FUENTE_PEQUENA_NEGRITA = new Font("SansSerif", Font.BOLD, 13);

    public HomeAdmin(Usuario usuarioadminLogeado, HomeAdminController controlador){

        this.adminLogeado = usuarioadminLogeado;
        this.controlador = controlador;

        setTitle("MOVUCV - Panel de Administrador");
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
 
        // parte izquierda logo, MOVUCV, separador y tipo de panel 
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izq.setOpaque(false);

        JLabel titulo = new JLabel("MOVUCV");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);
 
        // carga de logito
        String ruta = "res/images/logo sin fondo.png";
        File archivoLogo = new File(ruta);
 
        if (archivoLogo.exists()) {
            ImageIcon Imglogo = new ImageIcon(archivoLogo.getAbsolutePath());
            Image LogoMejorado = Imglogo.getImage().getScaledInstance(100, 40, Image.SCALE_SMOOTH);
            titulo.setIcon(new ImageIcon(LogoMejorado));
            titulo.setIconTextGap(12);

        } else 
            System.err.println("Error: No se encontró el logo en " + ruta);
        
 
        JLabel sep = new JLabel("|"); 
        sep.setFont(FUENTE_TITULO);
        sep.setForeground(COLOR_SECUNDARIO);
 
        JLabel texto1 = new JLabel("Panel Administrador");
        texto1.setFont(FUENTE_TITULO);
        texto1.setForeground(COLOR_TEXTO);
 
        izq.add(titulo); // agregar el titulo a la izquierda
        izq.add(sep); // agregar el separador
        izq.add(texto1); // agregamos el texto de inicio
 
        // parte derecha 
        JPanel dere = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        dere.setOpaque(false);
        dere.add(ComponentUtils.textoPresionable(adminLogeado.getNombre(), COLOR_TEXTO, FUENTE_TEXTO, null));
        dere.add(ComponentUtils.textoPresionable("|", COLOR_TEXTO, FUENTE_TEXTO, null)); 
        dere.add(ComponentUtils.textoPresionable("Cerrar Sesion", COLOR_TEXTO, FUENTE_TEXTO, () -> {controlador.cerrarSesion(this);})); 
 
        p.add(izq, BorderLayout.WEST);
        p.add(dere, BorderLayout.EAST);
 
        return p;
    }

    private JPanel Cuerpo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
 
       p.add(ComponentUtils.menuIzqu(this,controlador), BorderLayout.WEST);
       p.add(dashboard(), BorderLayout.CENTER);
        return p;
    }

    private JPanel PieDePagina() { // pie de pagina por defecto en todas las ventanas
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        p.setBackground(COLOR_PANEL);
        p.setBorder(new EmptyBorder(6, 0, 6, 0));
        JLabel texto = new JLabel("MOVUCV | Inicio | Datos Protegidos"); 

        texto.setFont(FUENTE_TEXTO);
        texto.setForeground(COLOR_TEXTO);
        p.add(texto);

        return p;
    }

    //para las tarjetas que aparecen tipo mosaico
     private JPanel dashboard() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 20, 10, 20));

         JPanel arriba = new JPanel();
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));
        arriba.setOpaque(false);
 
        JLabel titulo = new JLabel("Dashboard General \n"); // para lo de arribita el titulo
        titulo.setFont(FUENTE_TARJETA_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        arriba.add(titulo);

       arriba.add(Box.createRigidArea(new Dimension(0, 15))); // ESPACIO    

        JPanel filaArriba = new JPanel(new GridLayout(1, 4, 20, 0));
        filaArriba.setOpaque(false);
        filaArriba.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaArriba.add(miniTarjeta("Unidades Activas", String.valueOf(contarUnidadesActivas()), COLOR_VERDE, COLOR_FONDO));
        filaArriba.add(miniTarjeta("Rutas Operativas", String.valueOf(contarItinerariosPorEstado("Programado")), COLOR_BOTONES, COLOR_FONDO));
        filaArriba.add(miniTarjeta("Pasajeros Hoy", String.valueOf(contarPasajerosProgramados()), COLOR_NARANJA, COLOR_FONDO));
        filaArriba.add(miniTarjeta("Conductores", String.valueOf(contarConductores()), COLOR_ROJO, COLOR_NARANJA));
        arriba.add(filaArriba);

         arriba.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel filaAbajo = new JPanel(new GridLayout(1, 3, 20, 0));
        filaAbajo.setOpaque(false);
        filaAbajo.add(miniTarjeta("Viajes Completados", String.valueOf(contarItinerariosPorEstado("Finalizado")), COLOR_VERDE, COLOR_FONDO));
        filaAbajo.add(miniTarjeta("Tiempo Promedio de Ruta", "", COLOR_BOTONES, COLOR_FONDO));
        filaAbajo.add(miniTarjeta("Incidencias Reportadas", "", COLOR_NARANJA, COLOR_FONDO));

        p.add(arriba, BorderLayout.NORTH); // tarjetas de arribita en norte
        p.add(listasDatos(), BorderLayout.CENTER);
        p.add(filaAbajo, BorderLayout.SOUTH); // tarjetas de abajo en sur

        return p;
     }

    private JPanel listasDatos() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(25, 0, 25, 0));

        DefaultListModel<String> unidades = new DefaultListModel<>();
        for (Unidad unidad : new UnidadDAO().listaUnidades()) {
            if ("Operativo".equalsIgnoreCase(unidad.getDisponible())) {
                unidades.addElement(unidad.getPlaca() + " | " + unidad.getModelo()
                        + " | " + unidad.getDisponible());
            }
        }

        DefaultListModel<String> rutas = new DefaultListModel<>();
        DefaultListModel<String> conductores = new DefaultListModel<>();
        for (Itinerario itinerario : new ItinerarioDAO().listaItinerarios()) {
            if ("Programado".equalsIgnoreCase(itinerario.getEstado())
                    || "En Curso".equalsIgnoreCase(itinerario.getEstado())) {
                rutas.addElement(itinerario.getRuta() + " | " + itinerario.getHoraSalida()
                        + " | " + itinerario.getEstado());

                String conductor = "Cédula: " + itinerario.getConductor();
                if (!conductores.contains(conductor)) {
                    conductores.addElement(conductor);
                }
            }
        }

        panel.add(panelLista("Unidades", unidades));
        panel.add(panelLista("Rutas", rutas));
        panel.add(panelLista("Conductores", conductores));
        return panel;
    }

    private JPanel panelLista(String titulo, DefaultListModel<String> datos) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(COLOR_PANEL); 
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BOTONES, 1),
                new EmptyBorder(15, 15, 15, 15))); 

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(FUENTE_TARJETA_TITULO); 
        etiqueta.setForeground(COLOR_TEXTO); 

        JList<String> lista = new JList<>(datos);
        // CAMBIO AQUÍ: Font.BOLD para la negrita
        lista.setFont(new Font(FUENTE_TEXTO.getFamily(), Font.BOLD, 14));
        // CAMBIO AQUÍ: COLOR_TEXTO para que sea blanco humo
        lista.setForeground(COLOR_TEXTO); 
        lista.setBackground(COLOR_PANEL);
        lista.setFixedCellHeight(35); 

        // Renderizador personalizado 
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(0, 10, 0, 10)); 
                
                if (isSelected) {
                    label.setBackground(COLOR_FONDO); 
                    label.setForeground(COLOR_BOTONES); 
                } else {
                    label.setBackground(COLOR_PANEL);
                    // CAMBIO AQUÍ: COLOR_TEXTO para mantener el blanco cuando no está seleccionado
                    label.setForeground(COLOR_TEXTO);
                }
                return label;
            }
        });

        panel.add(etiqueta, BorderLayout.NORTH);
        
        JScrollPane desplazamiento = new JScrollPane(lista);
        desplazamiento.setBackground(COLOR_PANEL);
        desplazamiento.getViewport().setBackground(COLOR_PANEL);
        desplazamiento.setBorder(BorderFactory.createEmptyBorder()); 
        
        desplazamiento.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        panel.add(desplazamiento, BorderLayout.CENTER);
        return panel;
    }


    private int contarUnidadesActivas() {
        int activas = 0;
        for (Unidad unidad : new UnidadDAO().listaUnidades()) {
            if ("Operativo".equalsIgnoreCase(unidad.getDisponible())) {
                activas++;
            }
        }
        return activas;
    }

    private int contarItinerariosPorEstado(String estado) {
        int cantidad = 0;
        for (Itinerario itinerario : new ItinerarioDAO().listaItinerarios()) {
            if (estado.equalsIgnoreCase(itinerario.getEstado())) {
                cantidad++;
            }
        }
        return cantidad;
    }

    private int contarPasajerosProgramados() {
        int pasajeros = 0;
        for (Itinerario itinerario : new ItinerarioDAO().listaItinerarios()) {
            if ("Programado".equalsIgnoreCase(itinerario.getEstado())) {
                pasajeros += itinerario.getReservas();
            }
        }
        return pasajeros;
    }

    private int contarConductores() {
        try {
            List<String> conductores = UsuarioDAO.obtenerConductores();
            return conductores.size();
        } catch (IOException e) {
            return 0;
        }
    }

    // clase para las mini tarjetas del mosaico
     private JPanel miniTarjeta(String titulo, String detalles, Color colorLinea, Color colorDetalle) {
        Tarjeta t = new Tarjeta(Color.WHITE, colorLinea);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.setBorder(new EmptyBorder(14, 15, 10, 15));
 
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_PEQUENA_NEGRITA);
        lblTitulo.setForeground(COLOR_FONDO);

        
        JLabel lblDetalle = new JLabel(detalles); //para el texto en la minitarjeta
        lblDetalle.setFont(FUENTE_TEXTO);
        lblDetalle.setForeground(colorDetalle);
 
        t.add(lblTitulo);
        t.add(lblDetalle);
        return t;
    }


}
