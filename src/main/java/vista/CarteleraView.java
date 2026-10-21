package vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import controlador.CompraBoletoControlador;
import ensamblador.Ensamblador;
import modelo.Boleto;
import modelo.Compra;
import modelo.Funcion;
import modelo.Pelicula;
import presentacion.FachadaPresentacionCartelera;
import presentacion.IConsultaModelo;

/**
 * Vista Swing del storyboard de seis pasos. No consulta el modelo directamente:
 * todas las operaciones pasan por el controlador.
 */
public class CarteleraView extends JFrame {

    private static final Color FONDO = new Color(12, 20, 36);
    private static final Color PANEL = new Color(22, 35, 56);
    private static final Color TARJETA = new Color(28, 45, 70);
    private static final Color BORDE = new Color(45, 65, 90);
    private static final Color TEXTO = new Color(234, 243, 255);
    private static final Color SECUNDARIO = new Color(157, 175, 197);
    private static final Color TURQUESA = new Color(77, 223, 218);
    private static final Color OSCURO_TURQUESA = new Color(25, 60, 72);
    private static final Color OCUPADO = new Color(90, 99, 113);
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private enum Etapa {
        CARTELERA, DETALLE, ASIENTOS, RESUMEN, PAGO, BOLETOS
    }

    private final CompraBoletoControlador controlador;
    private IConsultaModelo consultas;
    private final int cliente;
    private final JPanel izquierda = columna();
    private final JPanel derecha = columna();
    private final Set<String> seleccionados = new LinkedHashSet<>();
    private final List<Compra> misCompras = new ArrayList<>();
    private Pelicula peliculaSeleccionada;
    private Funcion funcionSeleccionada;
    private Compra compraConfirmada;
    private Etapa etapa = Etapa.CARTELERA;

    public CarteleraView(CompraBoletoControlador controlador, Ensamblador ensamblador, int cliente, FachadaPresentacionCartelera fachadaPresentacionCartelera) {
        super("Cine — Cliente " + cliente);
        this.consultas = fachadaPresentacionCartelera;
        this.controlador = controlador;
        this.cliente = cliente;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1160, 760);
        setMinimumSize(new Dimension(970, 660));
        setLocationByPlatform(true);
        construirMarco();
        actualizarContenido();
    }

    private void construirMarco() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        setContentPane(raiz);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(new Color(16, 28, 49));
        encabezado.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 22));
        JLabel logo = texto("CINE ", 23, Font.BOLD, TURQUESA);
        encabezado.add(logo, BorderLayout.WEST);

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 9, 0));
        menu.setOpaque(false);
        JButton cartelera = boton("Cartelera", TARJETA, TEXTO);
        cartelera.addActionListener(e -> reiniciarCartelera());
        menu.add(cartelera);

        JButton misBoletos = boton("Mis boletos", TARJETA, TEXTO);
        misBoletos.addActionListener(e -> mostrarMisBoletos());
        menu.add(misBoletos);

        encabezado.add(menu, BorderLayout.EAST);
        raiz.add(encabezado, BorderLayout.NORTH);

        JPanel principal = new JPanel(new BorderLayout(14, 0));
        principal.setBackground(FONDO);
        principal.setBorder(BorderFactory.createEmptyBorder(17, 22, 20, 22));
        JScrollPane scrollIzq = crearScroll(izquierda);
        scrollIzq.setPreferredSize(new Dimension(350, 580));
        principal.add(scrollIzq, BorderLayout.WEST);
        principal.add(crearScroll(derecha), BorderLayout.CENTER);
        raiz.add(principal, BorderLayout.CENTER);
    }

    private static JPanel columna() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(FONDO);
        return panel;
    }

    private static JScrollPane crearScroll(JPanel contenido) {
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(FONDO);
        scroll.getViewport().setBackground(FONDO);
        return scroll;
    }

    private static JLabel texto(String valor, int tamano, int estilo, Color color) {
        JLabel label = new JLabel(valor);
        label.setFont(new Font("SansSerif", estilo, tamano));
        label.setForeground(color);
        return label;
    }

    private static JPanel fila(Component... elementos) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        p.setOpaque(false);
        for (Component c : elementos) {
            p.add(c);
        }
        return p;
    }

    private static void agregar(JPanel destino, Component elemento, int espacioPosterior) {
        if (elemento instanceof JComponent jc) {
            jc.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        destino.add(elemento);
        if (espacioPosterior > 0) {
            destino.add(Box.createVerticalStrut(espacioPosterior));
        }
    }

    private static JPanel contenedor(Color color, int arriba, int izquierda, int abajo, int derecha) {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(color);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(arriba, izquierda, abajo, derecha));
        return panel;
    }

    private static JButton boton(String etiqueta, Color fondo, Color frente) {
        return new BotonRedondeado(etiqueta, fondo, frente);
    }

    private void actualizarContenido() {
        izquierda.removeAll();
        derecha.removeAll();
        construirIzquierda();
        construirDerecha();
        izquierda.revalidate();
        izquierda.repaint();
        derecha.revalidate();
        derecha.repaint();
    }

    private void construirIzquierda() {
        agregar(izquierda, texto("Películas en cartelera", 18, Font.BOLD, TEXTO), 12);
        for (Pelicula pelicula : consultas.listarPeliculas()) {
            agregar(izquierda, tarjetaPelicula(pelicula), 9);
        }
        if (funcionSeleccionada != null) {
            agregar(izquierda, Box.createVerticalStrut(10), 4);
            construirMapaAsientos();
        }
    }

    private JComponent tarjetaPelicula(Pelicula pelicula) {
        boolean actual = peliculaSeleccionada != null && pelicula.getId().equals(peliculaSeleccionada.getId());
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(actual ? OSCURO_TURQUESA : PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(actual ? TURQUESA : BORDE, 1),
                BorderFactory.createEmptyBorder(11, 11, 11, 10)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 91));
        panel.setPreferredSize(new Dimension(330, 88));
        JLabel inicial = texto(pelicula.getInicial(), 24, Font.BOLD, TURQUESA);
        inicial.setHorizontalAlignment(SwingConstants.CENTER);
        inicial.setOpaque(true);
        inicial.setBackground(new Color(41, 64, 89));
        inicial.setPreferredSize(new Dimension(45, 52));
        panel.add(inicial, BorderLayout.WEST);
        JPanel info = columna();
        info.setOpaque(false);
        agregar(info, texto(pelicula.getTitulo(), 14, Font.BOLD, TEXTO), 3);
        agregar(info, texto("Clasificación: " + pelicula.getClasificacion() + " · " + pelicula.getDuracion(), 11, Font.PLAIN, SECUNDARIO), 3);
        agregar(info, texto(actual ? "Película seleccionada" : "Seleccionar película →", 11, Font.PLAIN, TURQUESA), 0);
        panel.add(info, BorderLayout.CENTER);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarPelicula(pelicula);
            }
        });
        return panel;
    }

    private void seleccionarPelicula(Pelicula pelicula) {
        peliculaSeleccionada = pelicula;
        funcionSeleccionada = null;
        compraConfirmada = null;
        seleccionados.clear();
        etapa = Etapa.DETALLE;
        actualizarContenido();
    }

    private void seleccionarFuncion(Funcion funcion) {
        funcionSeleccionada = funcion;
        seleccionados.clear();
        compraConfirmada = null;
        etapa = Etapa.ASIENTOS;
        actualizarContenido();
    }

    private void construirMapaAsientos() {
        agregar(izquierda, texto("Distribución de asientos", 16, Font.BOLD, TEXTO), 4);
        agregar(izquierda, texto("Sala " + funcionSeleccionada.getSala().getNumero() + " · Selecciona tus lugares", 12, Font.PLAIN, SECUNDARIO), 12);
        JPanel pantalla = contenedor(new Color(54, 83, 111), 4, 7, 4, 7);
        JLabel leyendaPantalla = texto("PANTALLA", 10, Font.BOLD, TEXTO);
        leyendaPantalla.setAlignmentX(Component.CENTER_ALIGNMENT);
        pantalla.add(leyendaPantalla);
        pantalla.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        agregar(izquierda, pantalla, 12);

        int filas = funcionSeleccionada.getSala().getFilas();
        int columnas = funcionSeleccionada.getSala().getColumnas();
        Set<String> ocupados = consultas.asientosReservados(funcionSeleccionada.getId());
        JPanel cuadricula = new JPanel(new GridLayout(filas, columnas + 1, 5, 6));
        cuadricula.setOpaque(false);
        for (int f = 0; f < filas; f++) {
            final String letra = String.valueOf((char) ('A' + f));
            JLabel letraFila = texto(letra, 12, Font.BOLD, SECUNDARIO);
            letraFila.setHorizontalAlignment(SwingConstants.CENTER);
            cuadricula.add(letraFila);
            for (int c = 1; c <= columnas; c++) {
                String clave = letra + c;
                boolean ocupado = ocupados.contains(clave);
                boolean elegido = seleccionados.contains(clave);
                JButton asiento = boton(String.valueOf(c), ocupado ? OCUPADO : elegido ? TURQUESA : TARJETA,
                        elegido ? FONDO : TEXTO);
                asiento.setPreferredSize(new Dimension(37, 34));
                asiento.setMargin(new Insets(3, 2, 3, 2));
                asiento.setEnabled(!ocupado && etapa != Etapa.PAGO && etapa != Etapa.BOLETOS);
                asiento.setToolTipText("Asiento " + clave + (ocupado ? " — ocupado" : " — disponible"));
                asiento.addActionListener(e -> alternarAsiento(clave));
                cuadricula.add(asiento);
            }
        }
        agregar(izquierda, cuadricula, 14);
        agregar(izquierda, fila(
                texto("● Disponible", 11, Font.PLAIN, SECUNDARIO),
                texto("● Ocupado", 11, Font.PLAIN, OCUPADO),
                texto("● Seleccionado", 11, Font.BOLD, TURQUESA)), 6);
        agregar(izquierda, texto("Seleccionados: " + (seleccionados.isEmpty() ? "ninguno" : String.join(", ", seleccionados)),
                12, Font.PLAIN, TEXTO), 12);
    }

    private void alternarAsiento(String clave) {
        if (seleccionados.contains(clave)) {
            seleccionados.remove(clave);
        } else {
            seleccionados.add(clave);
        }
        etapa = seleccionados.isEmpty() ? Etapa.ASIENTOS : Etapa.RESUMEN;
        actualizarContenido();
    }

    private void construirDerecha() {
        if (peliculaSeleccionada == null) {
            JPanel bienvenida = contenedor(PANEL, 90, 35, 100, 35);
            JLabel icono = texto("▶", 48, Font.BOLD, TURQUESA);
            icono.setAlignmentX(Component.CENTER_ALIGNMENT);
            agregar(bienvenida, icono, 18);
            JLabel titulo = texto("Selecciona una película", 24, Font.BOLD, TEXTO);
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            agregar(bienvenida, titulo, 8);
            JLabel ayuda = texto("Aquí aparecerán sus detalles, los horarios y los boletos.", 13, Font.PLAIN, SECUNDARIO);
            ayuda.setAlignmentX(Component.CENTER_ALIGNMENT);
            agregar(bienvenida, ayuda, 0);
            agregar(derecha, bienvenida, 10);
            return;
        }

        agregar(derecha, crearDetalles(), 14);
        if (funcionSeleccionada == null) {
            agregar(derecha, aviso("Elige un horario para ver los asientos disponibles."), 10);
            return;
        }
        if (etapa == Etapa.ASIENTOS) {
            agregar(derecha, aviso("Ya puedes elegir tus asientos. Selecciónalos en el mapa de la izquierda."), 10);
        } else {
            agregar(derecha, crearResumen(), 12);
            if (etapa == Etapa.PAGO) {
                agregar(derecha, crearPago(), 12);
            } else if (etapa == Etapa.BOLETOS && compraConfirmada != null) {
                agregar(derecha, crearBoletos(), 12);
            } else {
                JButton pagar = boton("Realizar pago", TURQUESA, FONDO);
                pagar.addActionListener(e -> {
                    etapa = Etapa.PAGO;
                    actualizarContenido();
                });
                pagar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                agregar(derecha, pagar, 6);
                agregar(derecha, texto("Revisa el resumen antes de continuar.", 12, Font.PLAIN, SECUNDARIO), 8);
            }
        }
    }

    private JComponent crearDetalles() {
        JPanel contenido = contenedor(PANEL, 20, 20, 18, 20);
        agregar(contenido, texto("Detalles de la película", 17, Font.BOLD, TEXTO), 12);
        JPanel info = new JPanel(new BorderLayout(14, 0));
        info.setOpaque(false);
        JLabel cartel = texto(peliculaSeleccionada.getInicial(), 48, Font.BOLD, TURQUESA);
        cartel.setHorizontalAlignment(SwingConstants.CENTER);
        cartel.setPreferredSize(new Dimension(95, 100));
        cartel.setOpaque(true);
        cartel.setBackground(TARJETA);
        info.add(cartel, BorderLayout.WEST);
        JPanel detalles = new JPanel(new BorderLayout(0, 8));
        detalles.setOpaque(false);
        JPanel cabeceraDetalles = columna();
        cabeceraDetalles.setOpaque(false);
        agregar(cabeceraDetalles, texto(peliculaSeleccionada.getTitulo(), 21, Font.BOLD, TEXTO), 5);
        agregar(cabeceraDetalles, texto(peliculaSeleccionada.getClasificacion() + " · " + peliculaSeleccionada.getDuracion(),
                13, Font.BOLD, TURQUESA), 0);
        detalles.add(cabeceraDetalles, BorderLayout.NORTH);
        JTextArea sinopsis = new JTextArea(peliculaSeleccionada.getSinopsis());
        sinopsis.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sinopsis.setForeground(SECUNDARIO);
        sinopsis.setOpaque(false);
        sinopsis.setEditable(false);
        sinopsis.setLineWrap(true);
        sinopsis.setWrapStyleWord(true);
        detalles.add(sinopsis, BorderLayout.CENTER);
        info.add(detalles, BorderLayout.CENTER);
        agregar(contenido, info, 19);
        agregar(contenido, texto("Horarios disponibles · 24 OCT 2026", 13, Font.BOLD, TEXTO), 10);
        JPanel horarios = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        horarios.setOpaque(false);
        for (Funcion funcion : consultas.listarFunciones(peliculaSeleccionada.getId())) {
            boolean seleccionado = funcionSeleccionada != null && funcion.getId().equals(funcionSeleccionada.getId());
            JButton hora = boton(funcion.getHora().format(HORA), seleccionado ? TURQUESA : TARJETA,
                    seleccionado ? FONDO : TEXTO);
            hora.addActionListener(e -> seleccionarFuncion(funcion));
            horarios.add(hora);
        }
        if (funcionSeleccionada != null) {
            horarios.add(texto("Sala " + funcionSeleccionada.getSala().getNumero(), 13, Font.BOLD, TURQUESA));
        }
        agregar(contenido, horarios, 0);
        return contenido;
    }

    private JComponent aviso(String mensaje) {
        JPanel panel = contenedor(TARJETA, 45, 22, 48, 22);
        JLabel etiqueta = texto("●  " + mensaje, 14, Font.PLAIN, TEXTO);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        agregar(panel, etiqueta, 0);
        return panel;
    }

    private JComponent crearResumen() {
        JPanel panel = contenedor(PANEL, 19, 21, 18, 21);
        agregar(panel, texto("Resumen de la compra", 19, Font.BOLD, TEXTO), 11);
        resumenFila(panel, "Película", peliculaSeleccionada.getTitulo());
        resumenFila(panel, "Fecha", funcionSeleccionada.getFecha().format(FECHA));
        resumenFila(panel, "Horario", funcionSeleccionada.getHora().format(HORA));
        resumenFila(panel, "Sala", String.valueOf(funcionSeleccionada.getSala().getNumero()));
        resumenFila(panel, "Asientos", String.join(", ", seleccionados));
        resumenFila(panel, "Precio por boleto", "$" + consultas.consultarPrecioBoleto() + " MXN");
        agregar(panel, Box.createVerticalStrut(9), 0);
        JPanel total = new JPanel(new BorderLayout());
        total.setOpaque(false);
        total.add(texto("TOTAL", 15, Font.BOLD, TEXTO), BorderLayout.WEST);
        total.add(texto("$" + (seleccionados.size() * consultas.consultarPrecioBoleto()) + " MXN",
                22, Font.BOLD, TURQUESA), BorderLayout.EAST);
        agregar(panel, total, 0);
        return panel;
    }

    private void resumenFila(JPanel panel, String etiqueta, String valor) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        fila.add(texto(etiqueta + ":", 13, Font.PLAIN, SECUNDARIO), BorderLayout.WEST);
        fila.add(texto(valor, 13, Font.BOLD, TEXTO), BorderLayout.EAST);
        agregar(panel, fila, 8);
    }

    private JComponent crearPago() {
        JPanel pago = contenedor(PANEL, 18, 21, 20, 21);
        agregar(pago, texto("Número de tarjeta (solo demostración)", 15, Font.BOLD, TEXTO), 8);
        JPasswordField numero = new JPasswordField("4242 4242 4242 4242");
        numero.setEchoChar((char) 0);
        numero.setFont(new Font("SansSerif", Font.PLAIN, 17));
        numero.setForeground(TEXTO);
        numero.setBackground(FONDO);
        numero.setCaretColor(TURQUESA);
        numero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        numero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        agregar(pago, numero, 12);
        JLabel banco = texto("", 13, Font.PLAIN, SECUNDARIO);
        JLabel tipo = texto("", 13, Font.PLAIN, SECUNDARIO);
        JLabel terminacion = texto("", 13, Font.PLAIN, SECUNDARIO);
        agregar(pago, banco, 6);
        agregar(pago, tipo, 6);
        agregar(pago, terminacion, 14);
        JButton confirmar = boton("Confirmar pago (simulación)", TURQUESA, FONDO);
        confirmar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        agregar(pago, confirmar, 10);
        agregar(pago, texto("No ingreses datos de tarjetas reales. No se realiza ningún cargo.",
                11, Font.PLAIN, SECUNDARIO), 0);

        Runnable refrescar = () -> {
            char[] entrada = numero.getPassword();
            String digitos = new String(entrada).replace(" ", "");
            Arrays.fill(entrada, '\0');
            boolean valida = digitos.matches("\\d{16}");
            confirmar.setEnabled(valida);
            banco.setText(valida ? "Banco emisor: Banco de prueba" : "Ingresa 16 dígitos de prueba");
            tipo.setText(valida ? "Tipo de tarjeta: Visa simulada" : "Tipo de tarjeta: —");
            terminacion.setText(valida ? "Últimos cuatro dígitos: " + digitos.substring(12) : "Últimos cuatro dígitos: —");
        };
        numero.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refrescar.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refrescar.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refrescar.run();
            }
        });
        confirmar.addActionListener(e -> {
            char[] entrada = numero.getPassword();
            String digitos = new String(entrada).replace(" ", "");
            Arrays.fill(entrada, '\0');
            if (!digitos.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(this, "Utiliza una tarjeta de prueba de 16 dígitos.");
                return;
            }
            try {
                Compra compra = controlador.confirmarCompra(funcionSeleccionada.getId(),
                        new ArrayList<>(seleccionados), digitos.substring(12));
                compraConfirmada = compra;
                misCompras.add(compra);
                etapa = Etapa.BOLETOS;
                actualizarContenido();
            } catch (IllegalStateException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo completar la compra",
                        JOptionPane.WARNING_MESSAGE);
                actualizarDisponibilidad();
            }
        });
        refrescar.run();
        return pago;
    }

    private JComponent crearBoletos() {
        JPanel panel = contenedor(PANEL, 16, 18, 17, 18);
        agregar(panel, texto("Boletos electrónicos · Compra #" + compraConfirmada.getFolio(),
                18, Font.BOLD, TURQUESA), 10);
        for (Boleto boleto : compraConfirmada.getBoletos()) {
            JPanel tarjeta = contenedor(TARJETA, 13, 14, 12, 14);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TURQUESA),
                    BorderFactory.createEmptyBorder(12, 14, 12, 14)));
            agregar(tarjeta, texto("CINE NOVA · BOLETO " + boleto.getId(), 11, Font.BOLD, TURQUESA), 9);
            resumenFila(tarjeta, "Película", boleto.getFuncion().getPelicula().getTitulo());
            resumenFila(tarjeta, "Fecha", boleto.getFuncion().getFecha().format(FECHA));
            resumenFila(tarjeta, "Horario", boleto.getFuncion().getHora().format(HORA));
            resumenFila(tarjeta, "Sala", String.valueOf(boleto.getFuncion().getSala().getNumero()));
            resumenFila(tarjeta, "Asiento", boleto.getAsiento());
            resumenFila(tarjeta, "Pagado", "$" + boleto.getPrecio() + " MXN");
            agregar(panel, tarjeta, 10);
        }
        JButton regresar = boton("Volver a cartelera", TURQUESA, FONDO);
        regresar.addActionListener(e -> reiniciarCartelera());
        agregar(panel, regresar, 0);
        return panel;
    }

    private void mostrarMisBoletos() {
        if (misCompras.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aún no tienes boletos en esta ventana.",
                    "Mis boletos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Compra compra : misCompras) {
            Funcion funcion = compra.getFuncion();
            sb.append("Compra #").append(compra.getFolio()).append(" — ")
                    .append(funcion.getPelicula().getTitulo()).append("\n")
                    .append("Fecha: ").append(funcion.getFecha().format(FECHA))
                    .append(" | Horario: ").append(funcion.getHora().format(HORA))
                    .append(" | Sala: ").append(funcion.getSala().getNumero()).append("\n")
                    .append("Asientos: ");
            for (Boleto boleto : compra.getBoletos()) {
                sb.append(boleto.getAsiento()).append(" ");
            }
            sb.append("\nTotal: $").append(compra.getTotal()).append(" MXN\n\n");
        }
        JTextArea area = new JTextArea(sb.toString(), 13, 47);
        area.setEditable(false);
        area.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Mis boletos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void reiniciarCartelera() {
        peliculaSeleccionada = null;
        funcionSeleccionada = null;
        compraConfirmada = null;
        seleccionados.clear();
        etapa = Etapa.CARTELERA;
        actualizarContenido();
    }

    /**
     * Invocado vía Observer cuando otra ventana confirma una compra.
     */
    public void actualizarDisponibilidad() {
        if (funcionSeleccionada == null || etapa == Etapa.BOLETOS) {
            return;
        }
        Set<String> ocupados = consultas.asientosReservados(funcionSeleccionada.getId());
        boolean cambio = seleccionados.removeIf(ocupados::contains);
        if (cambio) {
            etapa = seleccionados.isEmpty() ? Etapa.ASIENTOS : Etapa.RESUMEN;
            JOptionPane.showMessageDialog(this,
                    "Otro cliente reservó uno o más asientos seleccionados.\nRevisa la nueva disponibilidad.",
                    "Asientos actualizados", JOptionPane.WARNING_MESSAGE);
        }
        actualizarContenido();
    }

    private static class BotonRedondeado extends JButton {

        private final Color fondo;
        private final Color frente;

        BotonRedondeado(String text, Color fondo, Color frente) {
            super(text);
            this.fondo = fondo;
            this.frente = frente;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setForeground(frente);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(9, 13, 9, 13));
            setPreferredSize(new Dimension(Math.max(54, getFontMetrics(getFont()).stringWidth(text) + 48), 36));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color color = fondo;
            if (!isEnabled()) {
                color = fondo;
            } else if (getModel().isPressed()) {
                color = fondo.darker();
            } else if (getModel().isRollover()) {
                color = fondo.brighter();
            }
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 13, 13);
            if (fondo.equals(TURQUESA)) {
                g2.setColor(TURQUESA.darker());
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 13, 13);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
