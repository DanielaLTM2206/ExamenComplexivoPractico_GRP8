package ec.edu.monster;

import ec.edu.monster.proxy.Celular;
import ec.edu.monster.proxy.CuotaAmortizacion;
import ec.edu.monster.proxy.Factura;
import ec.edu.monster.proxy.WSComercializadora;
import ec.edu.monster.proxy.WSComercializadora_Service;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente Escritorio Java Swing - Grupo 08
 * Comercializadora de Celulares & BanQuito
 * Integración con Servicios Web SOAP JAX-WS (Jakarta EE 10 / Payara 7)
 * 
 * Estilo visual institucional: Inspirado en Banco Pichincha / Monster
 * (Azul Marino #0F265C, Amarillo Institucional #FFDD00, Fondo Suave #F4F5F7).
 */
public class Celulares_CliEsc_GR8 extends JFrame {

    private WSComercializadora servicio;

    // Paleta de Colores Institucional (Idéntica a la Aplicación Web)
    public static final Color COLOR_AZUL = new Color(15, 38, 92);         // #0F265C Azul marino principal
    public static final Color COLOR_AZUL_CLARO = new Color(29, 58, 122);  // #1D3A7A Azul medio
    public static final Color COLOR_AZUL_OSCURO = new Color(10, 25, 65);  // #0A1941
    public static final Color COLOR_AMARILLO = new Color(255, 221, 0);    // #FFDD00 Amarillo institucional
    public static final Color COLOR_AMARILLO_OSC = new Color(230, 199, 0);// #E6C700
    public static final Color COLOR_AMARILLO_SUAVE = new Color(255, 251, 224); // #FFFBE0
    public static final Color COLOR_FONDO = new Color(244, 245, 247);     // #F4F5F7
    public static final Color COLOR_BLANCO = Color.WHITE;
    public static final Color COLOR_TEXTO = new Color(31, 41, 55);        // #1F2937
    public static final Color COLOR_TEXTO_SUAVE = new Color(107, 114, 128);// #6B7280
    public static final Color COLOR_EXITO = new Color(46, 125, 50);       // #2E7D32 Verde éxito
    public static final Color COLOR_EXITO_BG = new Color(230, 244, 234);  // #E6F4EA
    public static final Color COLOR_ERROR = new Color(198, 40, 40);       // #C62828 Rojo peligro
    public static final Color COLOR_ERROR_BG = new Color(254, 242, 242);  // #FEF2F2
    public static final Color COLOR_BORDE = new Color(226, 229, 235);     // #E2E5EB

    // Tipos de diálogo modal estilizado
    public static final int TIPO_EXITO = 1;
    public static final int TIPO_ERROR = 2;
    public static final int TIPO_AVISO = 3;
    public static final int TIPO_INFO = 4;

    // Contenedor principal de pestañas
    private JTabbedPane pestanas;

    // Componentes Pestaña Catálogo
    private JTable tablaCatalogo;
    private DefaultTableModel modeloCatalogo;
    private JCheckBox chkSoloActivos;

    // Directorio oficial de clientes registrados en el Core BanQuito
    public static final Map<String, String> CLIENTES_BANQUITO = new LinkedHashMap<>();
    static {
        CLIENTES_BANQUITO.put("1710034065", "Carlos Andrés Andrade Mora");
        CLIENTES_BANQUITO.put("1720045671", "María Fernanda López Ruiz");
        CLIENTES_BANQUITO.put("1705678918", "Jorge Luis Paredes Vega");
        CLIENTES_BANQUITO.put("1712345675", "Luis Alberto Morales Paz");
        CLIENTES_BANQUITO.put("1709876542", "Pedro José Vásquez Lima");
        CLIENTES_BANQUITO.put("1715555551", "Ana Gabriela Torres Silva");
        CLIENTES_BANQUITO.put("1718765439", "Diego Sebastián Salazar Ríos");
        CLIENTES_BANQUITO.put("1723456784", "Valeria Cedeño Bravo");
    }

    // Directorio activo del sistema (BanQuito + Clientes nuevos registrados en Comercializadora)
    public static final Map<String, String> CLIENTES_SISTEMA = new LinkedHashMap<>(CLIENTES_BANQUITO);

    // Componentes Pestaña Facturación
    private JComboBox<String> cboClientesSugeridos;
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JComboBox<CelularItem> cboCelulares;
    private JSpinner spnCantidad;
    private JRadioButton rdoEfectivo;
    private JRadioButton rdoCredito;
    private JComboBox<Integer> cboCuotas;
    private JLabel lblSubtotal;
    private JLabel lblDescuento;
    private JLabel lblTotal;
    private JLabel lblEstadoBanQuito;
    private JTextArea txtFacturaEmitida;
    private JButton btnFacturar;

    // Componentes Pestaña Amortización
    private JTextField txtNumFacturaBuscar;
    private JTable tablaAmortizacion;
    private DefaultTableModel modeloAmortizacion;
    private JPanel pnlDatosCliente;
    private JLabel lblFacturaInfo;
    private JLabel lblClienteInfo;
    private JLabel lblCedulaInfo;
    private JLabel lblFechaInfo;
    private JLabel lblCreditoInfo;
    private JLabel lblMontoInfo;
    private JLabel lblPlazoInfo;
    private JLabel lblEstadoCredito;
    private JPanel pnlTotalesAmortizacion;
    private JLabel lblTotalPagadoInfo;
    private JLabel lblTotalInteresInfo;
    private JLabel lblTotalCapitalInfo;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}
            new Celulares_CliEsc_GR8().setVisible(true);
        });
    }

    public Celulares_CliEsc_GR8() {
        setTitle("Comercializadora de Celulares & BanQuito - Cliente Escritorio Java Swing (Grupo 08)");
        setSize(1180, 860);
        setMinimumSize(new Dimension(1060, 760));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);

        conectarServicioSOAP();
        construirInterfaz();
        cargarCatalogo();
    }

    private void conectarServicioSOAP() {
        try {
            URL wsdlUrl = new URL("http://localhost:8080/WS_Comercializadora_SOAP/WSComercializadora?wsdl");
            WSComercializadora_Service factory = new WSComercializadora_Service(wsdlUrl);
            servicio = factory.getWSComercializadoraPort();
        } catch (Exception e) {
            mostrarDialogoModal(this, "Error de Conexión SOAP",
                    "No se pudo conectar al servicio web SOAP de la Comercializadora:\n" + e.getMessage() +
                    "\nAsegúrese de que Payara Server 7 esté iniciado en el puerto 8080.", TIPO_ERROR);
        }
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout());

        // 1. BANNER CABECERA INSTITUCIONAL CON SULLIVAN / MONSTER (Estilo Web)
        JPanel pnlHeader = crearPanelCabecera();
        add(pnlHeader, BorderLayout.NORTH);

        // 2. PESTAÑAS PRINCIPALES ESTILIZADAS CON NAVEGACIÓN WEB
        pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pestanas.setBackground(COLOR_FONDO);
        pestanas.setOpaque(true);

        pestanas.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                tabInsets = new Insets(10, 20, 10, 20);
                selectedTabPadInsets = new Insets(2, 2, 2, 2);
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isSelected) {
                    g2.setColor(COLOR_AMARILLO);
                    g2.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 10, 10);
                    g2.setColor(COLOR_AMARILLO_OSC);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(x + 2, y + 2, w - 4, h - 2, 10, 10);
                } else {
                    g2.setColor(new Color(230, 235, 245));
                    g2.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 10, 10);
                    g2.setColor(COLOR_BORDE);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(x + 2, y + 2, w - 4, h - 2, 10, 10);
                }
                g2.dispose();
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                g2.setColor(isSelected ? COLOR_AZUL : new Color(75, 85, 105));
                g2.drawString(title, textRect.x, textRect.y + metrics.getAscent());
                g2.dispose();
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(COLOR_BORDE);
                g2.drawRect(0, 0, pestanas.getWidth() - 1, pestanas.getHeight() - 1);
                g2.dispose();
            }

            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {}
        });

        pestanas.addTab("  Catálogo de Celulares (CRUD)  ", crearPanelCatalogo());
        pestanas.addTab("  Caja y Facturación (Efectivo / Crédito)  ", crearPanelFacturacion());
        pestanas.addTab("  Consulta Tabla de Amortización  ", crearPanelAmortizacion());

        add(pestanas, BorderLayout.CENTER);

        // 3. BARRA DE ESTADO INFERIOR ELEGANTE
        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.setBackground(COLOR_BLANCO);
        pnlStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_AMARILLO),
                new EmptyBorder(8, 18, 8, 18)
        ));

        JLabel lblStatus = new JLabel("● Servicio SOAP: Conectado (http://localhost:8080/WS_Comercializadora_SOAP/WSComercializadora)");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblStatus.setForeground(COLOR_EXITO);

        JLabel lblGrupo = new JLabel("ESPE - Arquitectura de Software  |  NRC 36883 - Grupo 08");
        lblGrupo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblGrupo.setForeground(COLOR_AZUL);

        pnlStatus.add(lblStatus, BorderLayout.WEST);
        pnlStatus.add(lblGrupo, BorderLayout.EAST);
        add(pnlStatus, BorderLayout.SOUTH);
    }

    private JPanel crearPanelCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_AZUL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 5, 0, COLOR_AMARILLO),
                new EmptyBorder(12, 20, 12, 20)
        ));

        // Imagen de Sullivan en la ventanilla con marco dorado (portada.jpg)
        JLabel lblImagen = new JLabel();
        try {
            URL imgUrl = getClass().getResource("/img/portada.jpg");
            if (imgUrl != null) {
                ImageIcon original = new ImageIcon(imgUrl);
                Image imgEscalada = original.getImage().getScaledInstance(215, 118, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(imgEscalada));
                lblImagen.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_AMARILLO, 2, true),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)
                ));
            }
        } catch (Exception ignored) {}

        // Panel de Textos del Banner con tipografía moderna y llamativa
        JPanel pnlTexto = new JPanel(new GridLayout(3, 1, 0, 3));
        pnlTexto.setOpaque(false);
        pnlTexto.setBorder(new EmptyBorder(4, 20, 4, 8));

        JLabel lblTitulo = new JLabel("COMERCIALIZADORA DE TELÉFONOS CELULARES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        lblTitulo.setForeground(COLOR_BLANCO);

        JLabel lblSub = new JLabel("Integración con Core Bancario BanQuito mediante Servicios Web SOAP JAX-WS (Jakarta EE 10)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(205, 220, 245));

        // Franja de beneficios estilizada
        JLabel lblPromocion = new JLabel("★ Venta al contado con 42% de descuento inmediato  |  Crédito directo de 3 a 18 meses al 16.5% anual");
        lblPromocion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPromocion.setForeground(COLOR_AMARILLO);

        pnlTexto.add(lblTitulo);
        pnlTexto.add(lblSub);
        pnlTexto.add(lblPromocion);

        header.add(lblImagen, BorderLayout.WEST);
        header.add(pnlTexto, BorderLayout.CENTER);
        return header;
    }

    // -------------------------------------------------------------------------
    // PESTAÑA 1: CATÁLOGO DE CELULARES (CRUD)
    // -------------------------------------------------------------------------
    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setBackground(COLOR_FONDO);

        // Barra de Herramientas Estilizada con botones llenos de color
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        toolbar.setBackground(COLOR_BLANCO);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));

        chkSoloActivos = new JCheckBox("Mostrar solo celulares activos para la venta", true);
        chkSoloActivos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chkSoloActivos.setForeground(COLOR_TEXTO);
        chkSoloActivos.setOpaque(false);
        chkSoloActivos.addActionListener(e -> cargarCatalogo());

        JButton btnRefrescar = crearBoton("↻ Actualizar", COLOR_AZUL, COLOR_BLANCO, COLOR_AZUL_CLARO);
        btnRefrescar.addActionListener(e -> cargarCatalogo());

        JButton btnNuevo = crearBoton("+ Nuevo Celular", COLOR_AMARILLO, COLOR_AZUL, COLOR_AMARILLO_OSC);
        btnNuevo.addActionListener(e -> abrirDialogoNuevoCelular());

        JButton btnEditar = crearBoton("✎ Modificar Celular", COLOR_AZUL_CLARO, COLOR_BLANCO, COLOR_AZUL);
        btnEditar.addActionListener(e -> abrirDialogoEditarCelular());

        JButton btnEliminar = crearBoton("✖ Eliminar (Borrado Lógico)", COLOR_ERROR_BG, COLOR_ERROR, new Color(248, 113, 113));
        btnEliminar.addActionListener(e -> ejecutarEliminarCelular());

        toolbar.add(chkSoloActivos);
        toolbar.add(Box.createHorizontalStrut(15));
        toolbar.add(btnRefrescar);
        toolbar.add(btnNuevo);
        toolbar.add(btnEditar);
        toolbar.add(btnEliminar);

        panel.add(toolbar, BorderLayout.NORTH);

        // Tabla con diseño moderno: Encabezado Azul + Filas Cebradas + Badges de Estado
        modeloCatalogo = new DefaultTableModel(new String[]{"ID", "Marca", "Modelo", "Precio ($)", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCatalogo = new JTable(modeloCatalogo);
        tablaCatalogo.setRowHeight(34);
        tablaCatalogo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCatalogo.setSelectionBackground(COLOR_AMARILLO_SUAVE);
        tablaCatalogo.setSelectionForeground(COLOR_AZUL);
        tablaCatalogo.setGridColor(new Color(235, 238, 243));

        JTableHeader header = tablaCatalogo.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                l.setBackground(COLOR_AZUL);
                l.setForeground(COLOR_BLANCO);
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                l.setHorizontalAlignment(col == 0 || col == 4 ? JLabel.CENTER : (col == 3 ? JLabel.RIGHT : JLabel.LEFT));
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 4, 0, COLOR_AMARILLO),
                        new EmptyBorder(6, 12, 6, 12)
                ));
                l.setOpaque(true);
                return l;
            }
        });

        tablaCatalogo.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Color rowBg = isSelected ? COLOR_AMARILLO_SUAVE : (row % 2 == 0 ? COLOR_BLANCO : new Color(248, 250, 253));

                // Columna Estado con BADGE PÍLDORA (Verde para ACTIVO, Gris para DESACTIVADO)
                if (col == 4) {
                    JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
                    badgePanel.setOpaque(true);
                    badgePanel.setBackground(rowBg);

                    String estado = String.valueOf(value);
                    boolean activo = "ACTIVO".equalsIgnoreCase(estado);

                    JLabel badge = new JLabel(activo ? " ●  ACTIVO " : " ○  DESACTIVADO ");
                    badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    badge.setOpaque(true);

                    if (activo) {
                        badge.setBackground(COLOR_EXITO_BG);
                        badge.setForeground(COLOR_EXITO);
                        badge.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(167, 243, 208), 1, true),
                                new EmptyBorder(2, 10, 2, 10)
                        ));
                    } else {
                        badge.setBackground(new Color(241, 243, 246));
                        badge.setForeground(COLOR_TEXTO_SUAVE);
                        badge.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                                new EmptyBorder(2, 10, 2, 10)
                        ));
                    }
                    badgePanel.add(badge);
                    return badgePanel;
                }

                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                l.setBorder(new EmptyBorder(4, 12, 4, 12));
                l.setBackground(rowBg);
                l.setForeground(isSelected ? COLOR_AZUL : COLOR_TEXTO);

                if (col == 0) {
                    l.setHorizontalAlignment(JLabel.CENTER);
                    l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    l.setForeground(new Color(100, 110, 130));
                } else if (col == 1) {
                    l.setHorizontalAlignment(JLabel.LEFT);
                    l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    if (!isSelected) l.setForeground(COLOR_AZUL);
                } else if (col == 3) {
                    l.setHorizontalAlignment(JLabel.RIGHT);
                    l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    if (!isSelected) l.setForeground(COLOR_AZUL);
                } else {
                    l.setHorizontalAlignment(JLabel.LEFT);
                }
                return l;
            }
        });

        tablaCatalogo.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaCatalogo.getColumnModel().getColumn(1).setPreferredWidth(130);
        tablaCatalogo.getColumnModel().getColumn(2).setPreferredWidth(280);
        tablaCatalogo.getColumnModel().getColumn(3).setPreferredWidth(130);
        tablaCatalogo.getColumnModel().getColumn(4).setPreferredWidth(140);

        JScrollPane scroll = new JScrollPane(tablaCatalogo);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1, true));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void cargarCatalogo() {
        if (servicio == null) return;
        modeloCatalogo.setRowCount(0);
        boolean soloActivos = chkSoloActivos.isSelected();

        SwingWorker<List<Celular>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Celular> doInBackground() throws Exception {
                return servicio.listarCelulares(soloActivos);
            }

            @Override
            protected void done() {
                try {
                    List<Celular> lista = get();
                    if (lista != null) {
                        for (Celular c : lista) {
                            String estado = c.isActivo() ? "ACTIVO" : "INACTIVO";
                            modeloCatalogo.addRow(new Object[]{
                                    c.getCodCelular(),
                                    c.getMarca(),
                                    c.getModelo(),
                                    String.format("%.2f", c.getPrecio()),
                                    estado
                            });
                        }
                    }
                    actualizarComboCelulares();
                } catch (Exception e) {
                    mostrarDialogoModal(Celulares_CliEsc_GR8.this, "Error SOAP",
                            "Error al consultar catálogo: " + e.getMessage(), TIPO_ERROR);
                }
            }
        };
        worker.execute();
    }

    private JTextField crearCampoTextoFormulario(String textoInicial) {
        JTextField txt = new JTextField(textoInicial);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setPreferredSize(new Dimension(250, 36));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_AZUL, 1, true),
                        new EmptyBorder(6, 12, 6, 12)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                        new EmptyBorder(6, 12, 6, 12)
                ));
            }
        });
        return txt;
    }

    private JPanel crearHeaderModal(JDialog dialog, String titulo, String subtitulo) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_AZUL);
        header.setBorder(new EmptyBorder(14, 20, 14, 16));

        JPanel pnlTitulos = new JPanel(new GridLayout(subtitulo != null ? 2 : 1, 1, 0, 2));
        pnlTitulos.setOpaque(false);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTit.setForeground(COLOR_BLANCO);
        pnlTitulos.add(lblTit);

        if (subtitulo != null) {
            JLabel lblSub = new JLabel(subtitulo);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblSub.setForeground(new Color(205, 220, 245));
            pnlTitulos.add(lblSub);
        }

        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCerrar.setForeground(new Color(230, 235, 245));
        btnCerrar.setOpaque(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dialog.dispose());

        header.add(pnlTitulos, BorderLayout.CENTER);
        header.add(btnCerrar, BorderLayout.EAST);

        JPanel contenedorHeader = new JPanel(new BorderLayout());
        contenedorHeader.add(header, BorderLayout.CENTER);
        JPanel barraAmarilla = new JPanel();
        barraAmarilla.setPreferredSize(new Dimension(10, 3));
        barraAmarilla.setBackground(COLOR_AMARILLO);
        contenedorHeader.add(barraAmarilla, BorderLayout.SOUTH);

        return contenedorHeader;
    }

    private void abrirDialogoNuevoCelular() {
        JDialog dialog = new JDialog(this, "Registrar Nuevo Celular en Catálogo", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BLANCO);
        root.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));

        root.add(crearHeaderModal(dialog, "+ Registrar Nuevo Celular en Catálogo", "Comercializadora de Celulares - Integración SOAP Grupo 08"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(22, 26, 18, 26));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMarca = crearCampoTextoFormulario("");
        JTextField txtModelo = crearCampoTextoFormulario("");
        JTextField txtPrecio = crearCampoTextoFormulario("");

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMarca = new JLabel("Marca del Celular:");
        lblMarca.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMarca.setForeground(COLOR_AZUL);
        form.add(lblMarca, gbc);
        gbc.gridx = 1;
        form.add(txtMarca, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblModelo = new JLabel("Modelo del Equipo:");
        lblModelo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblModelo.setForeground(COLOR_AZUL);
        form.add(lblModelo, gbc);
        gbc.gridx = 1;
        form.add(txtModelo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblPrecio = new JLabel("Precio de Venta ($):");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrecio.setForeground(COLOR_AZUL);
        form.add(lblPrecio, gbc);
        gbc.gridx = 1;
        form.add(txtPrecio, gbc);

        root.add(form, BorderLayout.CENTER);

        // Barra inferior de botones
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 14));
        footer.setBackground(new Color(248, 250, 252));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE));

        JButton btnCancelar = crearBoton("Cancelar", COLOR_BLANCO, COLOR_TEXTO, COLOR_BORDE);
        btnCancelar.setPreferredSize(new Dimension(105, 36));
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnGuardar = crearBoton("Guardar en Catálogo", COLOR_AMARILLO, COLOR_AZUL, COLOR_AMARILLO_OSC);
        btnGuardar.setPreferredSize(new Dimension(175, 36));
        btnGuardar.addActionListener(e -> {
            try {
                String marca = txtMarca.getText().trim();
                String modelo = txtModelo.getText().trim();
                String precioStr = txtPrecio.getText().trim().replace(",", ".");

                if (marca.isEmpty() || modelo.isEmpty() || precioStr.isEmpty()) {
                    mostrarDialogoModal(dialog, "Campos Incompletos",
                            "Debe completar la marca, modelo y precio de venta del celular.", TIPO_AVISO);
                    return;
                }

                BigDecimal precio;
                try {
                    precio = new BigDecimal(precioStr);
                    if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                        mostrarDialogoModal(dialog, "Precio Inválido",
                                "El precio debe ser un número positivo mayor a cero.", TIPO_AVISO);
                        return;
                    }
                } catch (NumberFormatException nfe) {
                    mostrarDialogoModal(dialog, "Precio Numérico Requerido",
                            "Ingrese un valor numérico válido para el precio (ejemplo: 499.99).", TIPO_AVISO);
                    return;
                }

                servicio.crearCelular(marca, modelo, precio);
                dialog.dispose();
                mostrarDialogoModal(Celulares_CliEsc_GR8.this, "¡Celular Registrado!",
                        "El equipo <b>" + marca + " " + modelo + "</b> ($" + String.format("%.2f", precio) +
                        ") fue agregado exitosamente al catálogo comercial.", TIPO_EXITO);
                cargarCatalogo();
                actualizarComboCelulares();
            } catch (Exception ex) {
                mostrarDialogoModal(dialog, "Error al Crear Celular", ex.getMessage(), TIPO_ERROR);
            }
        });

        footer.add(btnCancelar);
        footer.add(btnGuardar);
        root.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void abrirDialogoEditarCelular() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila < 0) {
            mostrarDialogoModal(this, "Selección Requerida",
                    "Seleccione un celular de la tabla para modificar sus especificaciones.", TIPO_AVISO);
            return;
        }

        int id = (int) modeloCatalogo.getValueAt(fila, 0);
        String marcaActual = (String) modeloCatalogo.getValueAt(fila, 1);
        String modeloActual = (String) modeloCatalogo.getValueAt(fila, 2);
        String precioActual = String.valueOf(modeloCatalogo.getValueAt(fila, 3)).replace("$", "").trim();
        boolean activoActual = "ACTIVO".equalsIgnoreCase(String.valueOf(modeloCatalogo.getValueAt(fila, 4)));

        JDialog dialog = new JDialog(this, "Modificar Celular (ID " + id + ")", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BLANCO);
        root.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));

        root.add(crearHeaderModal(dialog, "✎ Modificar Celular (ID #" + id + ")", "Actualización de catálogo y estado comercial"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(22, 26, 18, 26));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMarca = crearCampoTextoFormulario(marcaActual);
        JTextField txtModelo = crearCampoTextoFormulario(modeloActual);
        JTextField txtPrecio = crearCampoTextoFormulario(precioActual.replace(",", "."));
        JCheckBox chkActivo = new JCheckBox("Activo para la venta en el catálogo", activoActual);
        chkActivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkActivo.setForeground(COLOR_AZUL);
        chkActivo.setOpaque(false);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMarca = new JLabel("Marca del Celular:");
        lblMarca.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMarca.setForeground(COLOR_AZUL);
        form.add(lblMarca, gbc);
        gbc.gridx = 1;
        form.add(txtMarca, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblModelo = new JLabel("Modelo del Equipo:");
        lblModelo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblModelo.setForeground(COLOR_AZUL);
        form.add(lblModelo, gbc);
        gbc.gridx = 1;
        form.add(txtModelo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblPrecio = new JLabel("Precio de Venta ($):");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrecio.setForeground(COLOR_AZUL);
        form.add(lblPrecio, gbc);
        gbc.gridx = 1;
        form.add(txtPrecio, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel lblEstado = new JLabel("Estado Comercial:");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstado.setForeground(COLOR_AZUL);
        form.add(lblEstado, gbc);
        gbc.gridx = 1;
        form.add(chkActivo, gbc);

        root.add(form, BorderLayout.CENTER);

        // Barra inferior de botones
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 14));
        footer.setBackground(new Color(248, 250, 252));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE));

        JButton btnCancelar = crearBoton("Cancelar", COLOR_BLANCO, COLOR_TEXTO, COLOR_BORDE);
        btnCancelar.setPreferredSize(new Dimension(105, 36));
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnGuardar = crearBoton("Guardar Cambios", COLOR_AMARILLO, COLOR_AZUL, COLOR_AMARILLO_OSC);
        btnGuardar.setPreferredSize(new Dimension(160, 36));
        btnGuardar.addActionListener(e -> {
            try {
                String marca = txtMarca.getText().trim();
                String modelo = txtModelo.getText().trim();
                String precioStr = txtPrecio.getText().trim().replace(",", ".");
                boolean activo = chkActivo.isSelected();

                if (marca.isEmpty() || modelo.isEmpty() || precioStr.isEmpty()) {
                    mostrarDialogoModal(dialog, "Campos Incompletos",
                            "Debe completar la marca, modelo y precio del celular.", TIPO_AVISO);
                    return;
                }

                BigDecimal precio;
                try {
                    precio = new BigDecimal(precioStr);
                    if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                        mostrarDialogoModal(dialog, "Precio Inválido",
                                "El precio debe ser un número positivo mayor a cero.", TIPO_AVISO);
                        return;
                    }
                } catch (NumberFormatException nfe) {
                    mostrarDialogoModal(dialog, "Precio Numérico Requerido",
                            "Ingrese un valor numérico válido para el precio.", TIPO_AVISO);
                    return;
                }

                servicio.actualizarCelular(id, marca, modelo, precio, activo);
                dialog.dispose();
                mostrarDialogoModal(Celulares_CliEsc_GR8.this, "¡Actualización Exitosa!",
                        "El celular <b>" + marca + " " + modelo + "</b> (ID #" + id + ") fue actualizado correctamente en la base de datos.", TIPO_EXITO);
                cargarCatalogo();
                actualizarComboCelulares();
            } catch (Exception ex) {
                mostrarDialogoModal(dialog, "Error al Modificar", ex.getMessage(), TIPO_ERROR);
            }
        });

        footer.add(btnCancelar);
        footer.add(btnGuardar);
        root.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void ejecutarEliminarCelular() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila < 0) {
            mostrarDialogoModal(this, "Selección Requerida",
                    "Seleccione un celular de la tabla para dar de baja.", TIPO_AVISO);
            return;
        }

        int id = (int) modeloCatalogo.getValueAt(fila, 0);
        String desc = modeloCatalogo.getValueAt(fila, 1) + " " + modeloCatalogo.getValueAt(fila, 2);

        boolean confirm = mostrarDialogoConfirmacion(this, "Confirmar Baja de Celular",
                "¿Está seguro de dar de baja el siguiente celular?\n" +
                "<b>" + desc + "</b> (ID #" + id + ")\n\n" +
                "<b>Mecanismo de Protección (Borrado Lógico):</b>\n" +
                "• Si el celular ya posee facturas emitidas, se aplicará <b>Borrado Lógico</b> (cambiará a inactivo para no romper el historial de ventas).\n" +
                "• Si nunca se ha vendido, se eliminará físicamente.");

        if (confirm) {
            try {
                boolean ok = servicio.eliminarCelular(id);
                if (ok) {
                    mostrarDialogoModal(this, "Operación Exitosa",
                            "¡Operación completada con éxito!<br>Se aplicó el proceso de baja (borrado lógico referencial si tenía ventas, o eliminación física si era nuevo).", TIPO_EXITO);
                    cargarCatalogo();
                    actualizarComboCelulares();
                }
            } catch (Exception e) {
                mostrarDialogoModal(this, "Error al Eliminar", e.getMessage(), TIPO_ERROR);
            }
        }
    }

    // -------------------------------------------------------------------------
    // PESTAÑA 2: CAJA Y FACTURACIÓN (EFECTIVO Y CRÉDITO DIRECTO)
    // -------------------------------------------------------------------------
    private JPanel crearPanelFacturacion() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 16, 16));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setBackground(COLOR_FONDO);

        // PANEL IZQUIERDO: FORMULARIO DE CAJA
        JPanel pnlForm = new JPanel(new BorderLayout(12, 12));
        pnlForm.setBackground(COLOR_BLANCO);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBackground(COLOR_BLANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        // Selector Rápido de Clientes Oficiales BanQuito
        JLabel lblSel = new JLabel("Cliente Sugerido:");
        lblSel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSel.setForeground(COLOR_AZUL);
        campos.add(lblSel, gbc);
        gbc.gridx = 1;
        cboClientesSugeridos = new JComboBox<>();
        cboClientesSugeridos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboClientesSugeridos.addItem("-- Seleccionar cliente registrado en BanQuito --");
        for (Map.Entry<String, String> entry : CLIENTES_SISTEMA.entrySet()) {
            cboClientesSugeridos.addItem(entry.getKey() + " - " + entry.getValue());
        }
        cboClientesSugeridos.addActionListener(e -> {
            int idx = cboClientesSugeridos.getSelectedIndex();
            if (idx > 0) {
                String item = (String) cboClientesSugeridos.getSelectedItem();
                if (item != null && item.contains(" - ")) {
                    String[] parts = item.split(" - ");
                    txtCedula.setText(parts[0].trim());
                    autoCompletarPorCedula(parts[0].trim());
                }
            }
        });
        campos.add(cboClientesSugeridos, gbc);

        // Cédula con botón de validación inmediata
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblCed = new JLabel("Cédula Cliente:");
        lblCed.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCed.setForeground(COLOR_AZUL);
        campos.add(lblCed, gbc);
        gbc.gridx = 1;

        JPanel pnlCedula = new JPanel(new BorderLayout(6, 0));
        pnlCedula.setBackground(COLOR_BLANCO);

        txtCedula = new JTextField("1710034065", 14);
        txtCedula.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtCedula.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { reaccionar(); }
            @Override
            public void removeUpdate(DocumentEvent e) { reaccionar(); }
            @Override
            public void changedUpdate(DocumentEvent e) { reaccionar(); }
            private void reaccionar() {
                SwingUtilities.invokeLater(() -> autoCompletarPorCedula(txtCedula.getText().trim()));
            }
        });
        txtCedula.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                autoCompletarPorCedula(txtCedula.getText().trim());
            }
        });

        JButton btnValidarCed = new JButton("Validar C.I.");
        btnValidarCed.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnValidarCed.setBackground(COLOR_AZUL);
        btnValidarCed.setForeground(COLOR_BLANCO);
        btnValidarCed.setFocusPainted(false);
        btnValidarCed.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnValidarCed.setToolTipText("Consultar cédula en el Core BanQuito y validar dígito verificador");
        btnValidarCed.addActionListener(e -> autoCompletarPorCedula(txtCedula.getText().trim()));

        pnlCedula.add(txtCedula, BorderLayout.CENTER);
        pnlCedula.add(btnValidarCed, BorderLayout.EAST);
        campos.add(pnlCedula, gbc);

        // Nombre (Protegido para clientes registrados, editable solo si es cliente nuevo)
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblNom = new JLabel("Nombre Completo:");
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNom.setForeground(COLOR_AZUL);
        campos.add(lblNom, gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField("Carlos Andrés Andrade Mora", 14);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNombre.setEditable(false);
        txtNombre.setBackground(new Color(240, 245, 252));
        txtNombre.setToolTipText("Nombre protegido según la base de datos oficial");
        campos.add(txtNombre, gbc);

        // Celular
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblProd = new JLabel("Teléfono Celular:");
        lblProd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProd.setForeground(COLOR_AZUL);
        campos.add(lblProd, gbc);
        gbc.gridx = 1;
        cboCelulares = new JComboBox<>();
        cboCelulares.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboCelulares.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) recalcularTotales();
        });
        campos.add(cboCelulares, gbc);

        // Cantidad
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCant.setForeground(COLOR_AZUL);
        campos.add(lblCant, gbc);
        gbc.gridx = 1;
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spnCantidad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        spnCantidad.addChangeListener(e -> recalcularTotales());
        campos.add(spnCantidad, gbc);

        // Forma de Pago
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblFp = new JLabel("Forma de Pago:");
        lblFp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFp.setForeground(COLOR_AZUL);
        campos.add(lblFp, gbc);
        gbc.gridx = 1;
        JPanel pnlFormaPago = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlFormaPago.setBackground(COLOR_BLANCO);
        rdoEfectivo = new JRadioButton("Efectivo (42% Desc.)", true);
        rdoCredito = new JRadioButton("Crédito BanQuito", false);
        rdoEfectivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rdoCredito.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rdoEfectivo.setForeground(COLOR_EXITO);
        rdoCredito.setForeground(COLOR_AZUL);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdoEfectivo);
        bg.add(rdoCredito);
        pnlFormaPago.add(rdoEfectivo);
        pnlFormaPago.add(rdoCredito);
        campos.add(pnlFormaPago, gbc);

        rdoEfectivo.addActionListener(e -> alternarFormaPago());
        rdoCredito.addActionListener(e -> alternarFormaPago());

        // Plazo de cuotas
        gbc.gridx = 0; gbc.gridy++;
        JLabel lblPlazo = new JLabel("Plazo Cuotas:");
        lblPlazo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPlazo.setForeground(COLOR_AZUL);
        campos.add(lblPlazo, gbc);
        gbc.gridx = 1;
        cboCuotas = new JComboBox<>(new Integer[]{3, 6, 9, 12, 15, 18});
        cboCuotas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cboCuotas.setEnabled(false);
        campos.add(cboCuotas, gbc);

        // Estado evaluación BanQuito
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        lblEstadoBanQuito = new JLabel("● Pago en efectivo: Aplica 42% de descuento inmediato.");
        lblEstadoBanQuito.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstadoBanQuito.setForeground(COLOR_EXITO);
        campos.add(lblEstadoBanQuito, gbc);

        // Panel de Totales con Tarjeta Destacada (Estilo Web)
        JPanel pnlTotales = new JPanel(new BorderLayout(8, 8));
        pnlTotales.setBackground(COLOR_BLANCO);
        pnlTotales.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JPanel pnlDetalleTotales = new JPanel(new GridLayout(2, 2, 6, 6));
        pnlDetalleTotales.setOpaque(false);

        JLabel lblSubTit = new JLabel("Subtotal Catálogo:");
        lblSubTit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubTit.setForeground(COLOR_TEXTO_SUAVE);

        lblSubtotal = new JLabel("$ 0.00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSubtotal.setForeground(COLOR_TEXTO);

        JLabel lblDescTit = new JLabel("Descuento (42% Efectivo):");
        lblDescTit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescTit.setForeground(COLOR_TEXTO_SUAVE);

        lblDescuento = new JLabel("-$ 0.00");
        lblDescuento.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDescuento.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDescuento.setForeground(COLOR_EXITO);

        pnlDetalleTotales.add(lblSubTit);
        pnlDetalleTotales.add(lblSubtotal);
        pnlDetalleTotales.add(lblDescTit);
        pnlDetalleTotales.add(lblDescuento);

        // Banner Azul con Total Dorado idéntico a la Web (.resumen .total)
        JPanel pnlTotalBanner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_AZUL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        pnlTotalBanner.setOpaque(false);
        pnlTotalBanner.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel lblTotalTexto = new JLabel("TOTAL A PAGAR:");
        lblTotalTexto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalTexto.setForeground(COLOR_BLANCO);

        lblTotal = new JLabel("$ 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(COLOR_AMARILLO);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        pnlTotalBanner.add(lblTotalTexto, BorderLayout.WEST);
        pnlTotalBanner.add(lblTotal, BorderLayout.EAST);

        pnlTotales.add(pnlDetalleTotales, BorderLayout.NORTH);
        pnlTotales.add(pnlTotalBanner, BorderLayout.SOUTH);

        // Botón principal de Facturación con color Amarillo Oficial
        btnFacturar = crearBoton("EMITIR FACTURA Y PROCESAR VENTA", COLOR_AMARILLO, COLOR_AZUL, COLOR_AMARILLO_OSC);
        btnFacturar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFacturar.setPreferredSize(new Dimension(300, 48));
        btnFacturar.addActionListener(e -> emitirFactura());

        pnlForm.add(campos, BorderLayout.NORTH);
        pnlForm.add(pnlTotales, BorderLayout.CENTER);
        pnlForm.add(btnFacturar, BorderLayout.SOUTH);

        // PANEL DERECHO: VISTA PREVIA DEL COMPROBANTE ELECTRÓNICO
        JPanel pnlPreview = new JPanel(new BorderLayout(8, 8));
        pnlPreview.setBackground(COLOR_BLANCO);
        pnlPreview.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));

        // Cabecera estilizada del comprobante
        JPanel pnlCompHeader = new JPanel(new BorderLayout());
        pnlCompHeader.setBackground(COLOR_AZUL);
        pnlCompHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_AMARILLO),
                new EmptyBorder(8, 12, 8, 12)
        ));
        JLabel lblTituloComp = new JLabel("COMPROBANTE ELECTRÓNICO DE FACTURA");
        lblTituloComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTituloComp.setForeground(COLOR_BLANCO);
        pnlCompHeader.add(lblTituloComp, BorderLayout.WEST);

        txtFacturaEmitida = new JTextArea();
        txtFacturaEmitida.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtFacturaEmitida.setEditable(false);
        txtFacturaEmitida.setBackground(new Color(253, 254, 255));
        txtFacturaEmitida.setBorder(new EmptyBorder(12, 12, 12, 12));
        txtFacturaEmitida.setText(
                "=================================================================\n" +
                "           COMERCIALIZADORA DE CELULARES - GRUPO 08              \n" +
                "               RUC: 1799999999001  -  Quito, Ecuador             \n" +
                "=================================================================\n" +
                " Seleccione un equipo del catálogo y presione 'EMITIR FACTURA'   \n" +
                " para procesar la transacción y obtener el comprobante oficial.  \n" +
                "=================================================================\n"
        );

        pnlPreview.add(pnlCompHeader, BorderLayout.NORTH);
        pnlPreview.add(new JScrollPane(txtFacturaEmitida), BorderLayout.CENTER);

        panel.add(pnlForm);
        panel.add(pnlPreview);
        return panel;
    }

    private void alternarFormaPago() {
        boolean esCredito = rdoCredito.isSelected();
        cboCuotas.setEnabled(esCredito);
        autoCompletarPorCedula(txtCedula.getText().trim());
        recalcularTotales();
    }

    private void recalcularTotales() {
        CelularItem item = (CelularItem) cboCelulares.getSelectedItem();
        if (item == null) return;

        int cantidad = (int) spnCantidad.getValue();
        BigDecimal precio = item.celular.getPrecio();
        BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
        lblSubtotal.setText("$ " + subtotal);

        if (rdoEfectivo.isSelected()) {
            BigDecimal descuento = subtotal.multiply(new BigDecimal("0.42")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal total = subtotal.subtract(descuento).setScale(2, RoundingMode.HALF_UP);
            lblDescuento.setText("-$ " + descuento);
            lblTotal.setText("$ " + total);
        } else {
            lblDescuento.setText("-$ 0.00");
            lblTotal.setText("$ " + subtotal);
        }
    }

    private void actualizarComboCelulares() {
        cboCelulares.removeAllItems();
        if (servicio == null) return;
        try {
            List<Celular> lista = servicio.listarCelulares(true);
            if (lista != null) {
                for (Celular c : lista) {
                    cboCelulares.addItem(new CelularItem(c));
                }
            }
            recalcularTotales();
        } catch (Exception ignored) {}
    }

    private void autoCompletarPorCedula(String cedula) {
        if (cedula == null) cedula = "";
        cedula = cedula.trim();

        if (cedula.length() == 10) {
            if (CLIENTES_SISTEMA.containsKey(cedula)) {
                String nombreOficial = CLIENTES_SISTEMA.get(cedula);
                txtNombre.setText(nombreOficial);
                txtNombre.setEditable(false); // BLOQUEADO: Nombre protegido contra incongruencias
                txtNombre.setBackground(new Color(240, 245, 252));
                txtNombre.setToolTipText("Nombre verificado y protegido en la base de datos oficial");

                boolean esBanQuito = CLIENTES_BANQUITO.containsKey(cedula);
                if (esBanQuito) {
                    lblEstadoBanQuito.setText("🔒 Cliente verificado en BanQuito: " + nombreOficial + " (Nombre protegido)");
                    lblEstadoBanQuito.setForeground(COLOR_EXITO);
                } else {
                    lblEstadoBanQuito.setText("🔒 Cliente registrado en Comercializadora: " + nombreOficial + " (Nombre protegido)");
                    lblEstadoBanQuito.setForeground(new Color(20, 100, 180));
                }
            } else {
                // Cédula de 10 dígitos pero no encontrada en el sistema
                if (!esCedulaValida(cedula)) {
                    txtNombre.setText("");
                    txtNombre.setEditable(false);
                    txtNombre.setBackground(new Color(255, 242, 242));
                    lblEstadoBanQuito.setText("✕ Cédula inválida: Dígito verificador o provincia incorrecta (Módulo 10).");
                    lblEstadoBanQuito.setForeground(COLOR_ERROR);
                } else {
                    // Cédula válida ecuatoriana pero NO registrada -> Habilitar para registrar nuevo cliente
                    txtNombre.setEditable(true);
                    txtNombre.setBackground(COLOR_BLANCO);
                    txtNombre.setToolTipText("Ingrese el nombre completo del nuevo cliente a registrar");
                    if (txtNombre.getText().trim().isEmpty() || CLIENTES_SISTEMA.containsValue(txtNombre.getText().trim())) {
                        txtNombre.setText("");
                    }
                    if (rdoCredito.isSelected()) {
                        lblEstadoBanQuito.setText("⚠ Cédula no registrada en BanQuito. Las compras a crédito requieren cuenta bancaria (RN-01).");
                        lblEstadoBanQuito.setForeground(COLOR_ERROR);
                    } else {
                        lblEstadoBanQuito.setText("📝 Cédula nueva no registrada. Ingrese el Nombre Completo para registrar al cliente.");
                        lblEstadoBanQuito.setForeground(new Color(20, 100, 180));
                    }
                }
            }
        } else if (cedula.length() > 10) {
            txtNombre.setEditable(false);
            txtNombre.setBackground(new Color(255, 242, 242));
            lblEstadoBanQuito.setText("✕ Cédula supera los 10 dígitos ecuatorianos.");
            lblEstadoBanQuito.setForeground(COLOR_ERROR);
        } else {
            // Menos de 10 dígitos
            txtNombre.setEditable(false);
            txtNombre.setBackground(new Color(248, 249, 250));
            lblEstadoBanQuito.setText("● Ingrese los 10 dígitos de la cédula para validar identidad y datos del cliente.");
            lblEstadoBanQuito.setForeground(new Color(110, 110, 110));
        }
    }

    public static boolean esCedulaValida(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) return false;
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if ((provincia < 1 || provincia > 24) && provincia != 30) return false;
        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito >= 6) return false;

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            int producto = digito * coeficientes[i];
            if (producto >= 10) producto -= 9;
            suma += producto;
        }
        int digitoVerificador = (suma % 10 == 0) ? 0 : 10 - (suma % 10);
        return digitoVerificador == Character.getNumericValue(cedula.charAt(9));
    }

    private void emitirFactura() {
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        CelularItem item = (CelularItem) cboCelulares.getSelectedItem();

        if (cedula.isEmpty() || item == null) {
            mostrarDialogoModal(this, "Datos Incompletos",
                    "Ingrese la cédula del cliente y seleccione un teléfono del catálogo.", TIPO_AVISO);
            return;
        }

        // 1. Validación matemática oficial de cédula ecuatoriana (Módulo 10)
        if (!esCedulaValida(cedula)) {
            mostrarDialogoModal(this, "Cédula Ecuatoriana Inválida",
                    "La cédula <b>" + cedula + "</b> no cumple con el algoritmo oficial de verificación (Módulo 10 del Registro Civil).<br>" +
                    "Por favor verifique los 10 dígitos ingresados.", TIPO_ERROR);
            return;
        }

        // 2. Validación y consistencia de identidad
        if (CLIENTES_SISTEMA.containsKey(cedula)) {
            String nombreOficial = CLIENTES_SISTEMA.get(cedula);
            if (!nombreOficial.equalsIgnoreCase(nombre)) {
                // Inconsistencia de identidad: Cédula pertenece a otra persona
                txtNombre.setText(nombreOficial);
                txtNombre.setEditable(false);
                txtNombre.setBackground(new Color(240, 245, 252));
                mostrarDialogoModal(this, "Inconsistencia de Identidad Detectada",
                        "La cédula <b>" + cedula + "</b> pertenece legalmente en los registros a:<br>" +
                        "<b style='color:#0F265C; font-size:14px;'>" + nombreOficial + "</b>.<br><br>" +
                        "No se permite facturar con un nombre diferente (<i>" + (nombre.isEmpty() ? "[Vacío]" : nombre) + "</i>).<br><br>" +
                        "<b>Acción tomada:</b> El campo de nombre ha sido corregido automáticamente a <b>" + nombreOficial + "</b>.<br>" +
                        "Presione 'EMITIR FACTURA' nuevamente para procesar la transacción legalmente.",
                        TIPO_AVISO);
                return;
            }
            nombre = nombreOficial;
        } else {
            // Cédula no registrada previamente
            if (nombre.isEmpty()) {
                txtNombre.setEditable(true);
                txtNombre.requestFocus();
                mostrarDialogoModal(this, "Registro de Nuevo Cliente Requerido",
                        "La cédula <b>" + cedula + "</b> no se encuentra registrada en la base de datos.<br><br>" +
                        "Por favor ingrese el <b>Nombre Completo</b> en el campo correspondiente para registrar al nuevo cliente y emitir la factura.",
                        TIPO_AVISO);
                return;
            }

            if (rdoCredito.isSelected()) {
                mostrarDialogoModal(this, "Crédito No Autorizado (RN-01)",
                        "La cédula <b>" + cedula + "</b> (" + nombre + ") no se encuentra registrada en el Core Bancario BanQuito.<br><br>" +
                        "<b>Regla de Negocio RN-01:</b> Para solicitar financiamiento a crédito directo (3 a 18 meses), el solicitante debe poseer una cuenta bancaria activa en BanQuito.<br><br>" +
                        "<i>Para clientes nuevos no bancarizados, la compra debe procesarse al CONTADO (Efectivo con 42% de descuento directo).</i>",
                        TIPO_ERROR);
                return;
            }
        }

        int codCelular = item.celular.getCodCelular();
        int cantidad = (int) spnCantidad.getValue();
        btnFacturar.setEnabled(false);

        final String finalCedula = cedula;
        final String finalNombre = nombre;

        SwingWorker<Factura, Void> worker = new SwingWorker<>() {
            @Override
            protected Factura doInBackground() throws Exception {
                if (rdoEfectivo.isSelected()) {
                    return servicio.facturarEfectivo(finalCedula, finalNombre, codCelular, cantidad);
                } else {
                    int cuotas = (Integer) cboCuotas.getSelectedItem();
                    return servicio.facturarCredito(finalCedula, finalNombre, codCelular, cuotas);
                }
            }

            @Override
            protected void done() {
                btnFacturar.setEnabled(true);
                try {
                    Factura f = get();
                    if (f != null) {
                        // Registrar cliente nuevo en el directorio activo si no existía
                        boolean esNuevo = !CLIENTES_SISTEMA.containsKey(finalCedula);
                        if (esNuevo) {
                            CLIENTES_SISTEMA.put(finalCedula, finalNombre);
                            cboClientesSugeridos.addItem(finalCedula + " - " + finalNombre + " (Nuevo)");
                        }

                        StringBuilder sb = new StringBuilder();
                        sb.append("=================================================================\n");
                        sb.append("           COMERCIALIZADORA DE TELÉFONOS CELULARES               \n");
                        sb.append("         INTEGRACIÓN SOAP BANQUITO - GRUPO 08 (ESPE)             \n");
                        sb.append("=================================================================\n");
                        sb.append(String.format("FACTURA N°:            %06d\n", f.getNumFactura()));
                        sb.append(String.format("FECHA DE EMISIÓN:      %s\n", f.getFecha()));
                        sb.append(String.format("CLIENTE:               %s\n", f.getNombre()));
                        sb.append(String.format("CÉDULA / RUC:          %s\n", f.getCedula()));
                        sb.append(String.format("MODALIDAD DE PAGO:     %s\n", f.getFormaPago()));
                        if (esNuevo) {
                            sb.append("ESTADO CLIENTE:        NUEVO CLIENTE REGISTRADO EN COMERCIALIZADORA\n");
                        }
                        sb.append("-----------------------------------------------------------------\n");
                        sb.append(String.format("EQUIPO:                %s %s\n", item.celular.getMarca(), item.celular.getModelo()));
                        sb.append(String.format("CANTIDAD:              %d\n", cantidad));
                        sb.append(String.format("PRECIO UNITARIO:       $ %.2f\n", item.celular.getPrecio()));
                        sb.append(String.format("SUBTOTAL:              $ %.2f\n", f.getSubtotal()));
                        sb.append(String.format("DESCUENTO:             $ %.2f\n", f.getDescuento()));
                        sb.append(String.format("TOTAL CANCELADO:       $ %.2f\n", f.getTotal()));
                        sb.append("-----------------------------------------------------------------\n");

                        boolean esCredito = f.getCodCredito() != null && f.getCodCredito() > 0;
                        if (esCredito) {
                            sb.append("--- DATOS DEL CRÉDITO DIRECTO APROBADO EN BANQUITO ---\n");
                            sb.append(String.format("CÓDIGO CRÉDITO BANCO:  %d\n", f.getCodCredito()));
                            sb.append(String.format("PLAZO FINANCIADO:      %d meses (Tasa 16.5%% anual)\n", cboCuotas.getSelectedItem()));
                            sb.append("ESTADO FINANCIERO:     APROBADO POR CORE BANCARIO BANQUITO\n");
                            txtNumFacturaBuscar.setText(String.valueOf(f.getNumFactura()));
                        } else {
                            sb.append("BENEFICIO APLICADO:    42% DE DESCUENTO DIRECTO POR PAGO AL CONTADO\n");
                        }
                        sb.append("=================================================================\n");
                        sb.append("          ¡GRACIAS POR SU COMPRA! - SISTEMA GRUPO 08             \n");
                        sb.append("=================================================================\n");

                        txtFacturaEmitida.setText(sb.toString());

                        // Diálogo modal estilizado con diseño web institucional
                        if (esCredito) {
                            mostrarDialogoModal(Celulares_CliEsc_GR8.this, "¡Venta a Crédito Aprobada!",
                                    "Factura N° " + f.getNumFactura() + " emitida con éxito.<br>" +
                                    "Crédito Directo BanQuito N° <b>" + f.getCodCredito() + "</b> aprobado para <b>" + f.getNombre() + "</b>.<br><br>" +
                                    "<i>Se ha cargado el número de factura en la pestaña 'Consulta Tabla de Amortización' para consultar sus cuotas.</i>",
                                    TIPO_EXITO);
                        } else if (esNuevo) {
                            mostrarDialogoModal(Celulares_CliEsc_GR8.this, "¡Cliente Registrado y Venta Exitosa!",
                                    "Factura N° <b>" + f.getNumFactura() + "</b> emitida con éxito.<br><br>" +
                                    "Se registró satisfactoriamente al nuevo cliente <b>" + f.getNombre() + "</b> (C.I. " + f.getCedula() + ") en la Comercializadora.<br><br>" +
                                    "<b>Beneficio aplicado:</b> 42% de descuento directo inmediato al contado.",
                                    TIPO_EXITO);
                        } else {
                            mostrarDialogoModal(Celulares_CliEsc_GR8.this, "¡Venta al Contado Exitosa!",
                                    "Factura N° " + f.getNumFactura() + " emitida con éxito para <b>" + f.getNombre() + "</b>.<br>" +
                                    "Beneficio aplicado: <b>42% de descuento directo inmediato</b>.",
                                    TIPO_EXITO);
                        }
                    }
                } catch (Exception e) {
                    mostrarDialogoModal(Celulares_CliEsc_GR8.this, "Error en Facturación",
                            "No se pudo procesar la venta:\n" + e.getMessage(), TIPO_ERROR);
                }
            }
        };
        worker.execute();
    }

    // -------------------------------------------------------------------------
    // PESTAÑA 3: CONSULTA TABLA DE AMORTIZACIÓN (CON DATOS CLIENTE Y FACTURA)
    // -------------------------------------------------------------------------
    private JPanel crearPanelAmortizacion() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setBackground(COLOR_FONDO);

        // 1. BARRA SUPERIOR DE BÚSQUEDA POR NÚMERO DE FACTURA (Estilo Web)
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        bar.setBackground(COLOR_BLANCO);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));

        JLabel lblFacturaBuscar = new JLabel("N° de Factura:");
        lblFacturaBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFacturaBuscar.setForeground(COLOR_AZUL);

        txtNumFacturaBuscar = new JTextField("4", 8);
        txtNumFacturaBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton btnConsultar = crearBoton("🔍 Consultar Tabla de Amortización", COLOR_AZUL, COLOR_BLANCO, COLOR_AZUL_CLARO);
        btnConsultar.addActionListener(e -> consultarAmortizacion());

        JLabel lblHint = new JLabel("  (Ingrese el número de la factura a crédito para visualizar los datos del cliente y sus cuotas)");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(COLOR_TEXTO_SUAVE);

        bar.add(lblFacturaBuscar);
        bar.add(txtNumFacturaBuscar);
        bar.add(btnConsultar);
        bar.add(lblHint);

        // 2. TARJETA ELEGANTE CON LOS DATOS DEL CLIENTE, CRÉDITO Y FECHA (.datos-cliente Web)
        pnlDatosCliente = new JPanel(new GridLayout(2, 4, 14, 10));
        pnlDatosCliente.setBackground(COLOR_BLANCO);
        pnlDatosCliente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, COLOR_AMARILLO),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                        new EmptyBorder(12, 16, 12, 16)
                )
        ));

        lblFacturaInfo = new JLabel("-");
        lblClienteInfo = new JLabel("-");
        lblCedulaInfo = new JLabel("-");
        lblFechaInfo = new JLabel("-");
        lblCreditoInfo = new JLabel("-");
        lblMontoInfo = new JLabel("-");
        lblPlazoInfo = new JLabel("-");
        lblEstadoCredito = new JLabel("-");

        pnlDatosCliente.add(crearDatoItem("N° FACTURA", lblFacturaInfo));
        pnlDatosCliente.add(crearDatoItem("CLIENTE TITULAR", lblClienteInfo));
        pnlDatosCliente.add(crearDatoItem("CÉDULA / RUC", lblCedulaInfo));
        pnlDatosCliente.add(crearDatoItem("FECHA DE EMISIÓN", lblFechaInfo));
        pnlDatosCliente.add(crearDatoItem("CRÉDITO BANQUITO", lblCreditoInfo));
        pnlDatosCliente.add(crearDatoItem("MONTO FINANCIADO", lblMontoInfo));
        pnlDatosCliente.add(crearDatoItem("PLAZO / CONDICIONES", lblPlazoInfo));
        pnlDatosCliente.add(crearDatoItem("ESTADO BANCARIO", lblEstadoCredito));

        JPanel pnlNorte = new JPanel(new BorderLayout(0, 10));
        pnlNorte.setOpaque(false);
        pnlNorte.add(bar, BorderLayout.NORTH);
        pnlNorte.add(pnlDatosCliente, BorderLayout.CENTER);
        panel.add(pnlNorte, BorderLayout.NORTH);

        // 3. TABLA DE AMORTIZACIÓN CON ENCABEZADO AZUL Y FILAS CEBRADAS
        modeloAmortizacion = new DefaultTableModel(
                new String[]{"Cuota N°", "Valor Cuota ($)", "Interés Pagado ($)", "Capital Pagado ($)", "Saldo Pendiente ($)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaAmortizacion = new JTable(modeloAmortizacion);
        tablaAmortizacion.setRowHeight(32);
        tablaAmortizacion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaAmortizacion.setSelectionBackground(COLOR_AMARILLO_SUAVE);
        tablaAmortizacion.setSelectionForeground(COLOR_AZUL);
        tablaAmortizacion.setGridColor(new Color(235, 238, 243));

        JTableHeader header = tablaAmortizacion.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                l.setBackground(COLOR_AZUL);
                l.setForeground(COLOR_BLANCO);
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                l.setHorizontalAlignment(col == 0 ? JLabel.CENTER : JLabel.RIGHT);
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 4, 0, COLOR_AMARILLO),
                        new EmptyBorder(6, 12, 6, 12)
                ));
                l.setOpaque(true);
                return l;
            }
        });

        tablaAmortizacion.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                l.setBorder(new EmptyBorder(4, 12, 4, 12));

                if (isSelected) {
                    l.setBackground(COLOR_AMARILLO_SUAVE);
                    l.setForeground(COLOR_AZUL);
                } else {
                    l.setBackground(row % 2 == 0 ? COLOR_BLANCO : new Color(248, 250, 253));
                    l.setForeground(COLOR_TEXTO);
                }

                if (col == 0) {
                    l.setHorizontalAlignment(JLabel.CENTER);
                    l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    l.setForeground(COLOR_AZUL);
                } else {
                    l.setHorizontalAlignment(JLabel.RIGHT);
                    l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                }
                return l;
            }
        });

        JScrollPane scroll = new JScrollPane(tablaAmortizacion);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1, true));
        panel.add(scroll, BorderLayout.CENTER);

        // 4. BARRA DE TOTALES DEL CRÉDITO (Footer como tfoot en la Web)
        pnlTotalesAmortizacion = new JPanel(new GridLayout(1, 3, 16, 0));
        pnlTotalesAmortizacion.setBackground(COLOR_BLANCO);
        pnlTotalesAmortizacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_AZUL),
                new EmptyBorder(10, 16, 10, 16)
        ));

        lblTotalPagadoInfo = new JLabel("Total Pagado: $ 0.00");
        lblTotalPagadoInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalPagadoInfo.setForeground(COLOR_AZUL);
        lblTotalPagadoInfo.setHorizontalAlignment(SwingConstants.LEFT);

        lblTotalInteresInfo = new JLabel("Total Intereses: $ 0.00");
        lblTotalInteresInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalInteresInfo.setForeground(new Color(180, 83, 9));
        lblTotalInteresInfo.setHorizontalAlignment(SwingConstants.CENTER);

        lblTotalCapitalInfo = new JLabel("Capital Amortizado: $ 0.00");
        lblTotalCapitalInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCapitalInfo.setForeground(COLOR_EXITO);
        lblTotalCapitalInfo.setHorizontalAlignment(SwingConstants.RIGHT);

        pnlTotalesAmortizacion.add(lblTotalPagadoInfo);
        pnlTotalesAmortizacion.add(lblTotalInteresInfo);
        pnlTotalesAmortizacion.add(lblTotalCapitalInfo);

        panel.add(pnlTotalesAmortizacion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearDatoItem(String etiqueta, JLabel lblValor) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);

        JLabel lblEt = new JLabel(etiqueta);
        lblEt.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblEt.setForeground(COLOR_TEXTO_SUAVE);

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblValor.setForeground(COLOR_AZUL);

        p.add(lblEt, BorderLayout.NORTH);
        p.add(lblValor, BorderLayout.CENTER);
        return p;
    }

    private void consultarAmortizacion() {
        if (servicio == null) return;
        String texto = txtNumFacturaBuscar.getText().trim();
        if (texto.isEmpty()) {
            mostrarDialogoModal(this, "Validación Requerida",
                    "Ingrese el número de la factura que desea consultar.", TIPO_AVISO);
            return;
        }

        try {
            int numFactura = Integer.parseInt(texto);
            modeloAmortizacion.setRowCount(0);

            SwingWorker<List<CuotaAmortizacion>, Void> worker = new SwingWorker<>() {
                private Factura factura;
                private BigDecimal totalPagado = BigDecimal.ZERO;
                private BigDecimal totalInteres = BigDecimal.ZERO;
                private BigDecimal totalCapital = BigDecimal.ZERO;
                private boolean esEfectivo = false;

                @Override
                protected List<CuotaAmortizacion> doInBackground() throws Exception {
                    factura = servicio.obtenerFactura(numFactura);
                    if (factura == null) {
                        throw new Exception("No se encontró en el sistema ninguna factura con el N° " + numFactura);
                    }
                    if (!"CREDITO".equalsIgnoreCase(factura.getFormaPago())) {
                        esEfectivo = true;
                        return null;
                    }
                    return servicio.consultarAmortizacion(numFactura);
                }

                @Override
                protected void done() {
                    try {
                        List<CuotaAmortizacion> cuotas = get();

                        // Caso 1: La factura fue cancelada al contado en efectivo
                        if (esEfectivo) {
                            lblFacturaInfo.setText("N° " + String.format("%06d", factura.getNumFactura()));
                            lblClienteInfo.setText(factura.getNombre());
                            lblCedulaInfo.setText(factura.getCedula());
                            lblFechaInfo.setText(factura.getFecha() != null ? factura.getFecha() : "Registrada");
                            lblCreditoInfo.setText("PAGO AL CONTADO");
                            lblMontoInfo.setText(String.format("$ %.2f", factura.getTotal()));
                            lblPlazoInfo.setText("Descuento 42% Aplicado");
                            lblEstadoCredito.setText("● PAGADA EN EFECTIVO");
                            lblEstadoCredito.setForeground(COLOR_EXITO);

                            lblTotalPagadoInfo.setText(String.format("Total Cancelado: $ %.2f", factura.getTotal()));
                            lblTotalInteresInfo.setText("Intereses: $ 0.00 (Sin crédito)");
                            lblTotalCapitalInfo.setText(String.format("Descuento Directo: -$ %.2f", factura.getDescuento()));

                            mostrarDialogoModal(Celulares_CliEsc_GR8.this, "Factura al Contado (Efectivo)",
                                    "La Factura N° <b>" + numFactura + "</b> a nombre de <b>" + factura.getNombre() + "</b> fue emitida en modalidad <b>EFECTIVO</b> con 42% de descuento directo.<br><br>" +
                                    "<b>Las ventas al contado no generan deuda bancaria ni tabla de amortización.</b><br><br>" +
                                    "<i>Para visualizar cuotas de amortización, ingrese una factura a crédito (por ejemplo: Factura N° <b>5</b>, <b>1</b> o <b>2</b>).</i>",
                                    TIPO_INFO);
                            return;
                        }

                        // Caso 2: Factura a crédito con tabla de amortización en BanQuito
                        if (cuotas != null && !cuotas.isEmpty()) {
                            for (CuotaAmortizacion c : cuotas) {
                                totalPagado = totalPagado.add(c.getValorCuota());
                                totalInteres = totalInteres.add(c.getInteres());
                                totalCapital = totalCapital.add(c.getCapital());

                                modeloAmortizacion.addRow(new Object[]{
                                        c.getNumCuota(),
                                        String.format("%.2f", c.getValorCuota()),
                                        String.format("%.2f", c.getInteres()),
                                        String.format("%.2f", c.getCapital()),
                                        String.format("%.2f", c.getSaldo())
                                });
                            }

                            // Actualizar los datos del cliente, crédito y fecha (Igual a la Web)
                            lblFacturaInfo.setText("N° " + String.format("%06d", factura.getNumFactura()));
                            lblClienteInfo.setText(factura.getNombre());
                            lblCedulaInfo.setText(factura.getCedula());
                            lblFechaInfo.setText(factura.getFecha() != null ? factura.getFecha() : "Registrada");
                            lblCreditoInfo.setText("Crédito BanQuito N° " + factura.getCodCredito());
                            lblMontoInfo.setText(String.format("$ %.2f", factura.getTotal()));
                            lblPlazoInfo.setText(cuotas.size() + " meses · 16.5% anual");
                            lblEstadoCredito.setText("● APROBADO CORE BANQUITO");
                            lblEstadoCredito.setForeground(COLOR_EXITO);

                            // Actualizar la barra inferior de totales
                            lblTotalPagadoInfo.setText(String.format("Total Pagado: $ %.2f", totalPagado));
                            lblTotalInteresInfo.setText(String.format("Total Intereses: $ %.2f", totalInteres));
                            lblTotalCapitalInfo.setText(String.format("Capital Financiado: $ %.2f", totalCapital));
                        } else {
                            mostrarDialogoModal(Celulares_CliEsc_GR8.this, "Sin Amortización",
                                    "No se encontraron cuotas para la factura N° " + numFactura, TIPO_INFO);
                        }
                    } catch (Exception e) {
                        String msg = e.getMessage();
                        if (msg != null && msg.contains("Exception: ")) {
                            msg = msg.substring(msg.indexOf("Exception: ") + 11);
                        }
                        mostrarDialogoModal(Celulares_CliEsc_GR8.this, "Consulta de Amortización",
                                msg != null ? msg : "Error desconocido al consultar factura.", TIPO_ERROR);
                    }
                }
            };
            worker.execute();
        } catch (NumberFormatException e) {
            mostrarDialogoModal(this, "Número Inválido",
                    "El número de factura debe ser un número entero válido.", TIPO_AVISO);
        }
    }

    // -------------------------------------------------------------------------
    // DIÁLOGOS MODALES ESTILIZADOS INSTITUCIONALES (Reemplazo de JOptionPane)
    // -------------------------------------------------------------------------
    public static void mostrarDialogoModal(Window parent, String titulo, String mensaje, int tipo) {
        JDialog dialog = new JDialog(parent, titulo, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(COLOR_BLANCO);

        Color accentColor;
        Color iconBg;
        Color iconFg;

        switch (tipo) {
            case TIPO_EXITO:
                accentColor = COLOR_EXITO;
                iconBg = COLOR_EXITO_BG;
                iconFg = COLOR_EXITO;
                break;
            case TIPO_ERROR:
                accentColor = COLOR_ERROR;
                iconBg = COLOR_ERROR_BG;
                iconFg = COLOR_ERROR;
                break;
            case TIPO_AVISO:
                accentColor = new Color(217, 119, 6);
                iconBg = new Color(254, 243, 199);
                iconFg = new Color(180, 83, 9);
                break;
            default:
                accentColor = COLOR_AZUL;
                iconBg = new Color(225, 235, 255);
                iconFg = COLOR_AZUL;
                break;
        }

        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createMatteBorder(5, 0, 0, 0, accentColor)
        ));

        JPanel body = new JPanel(new BorderLayout(16, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(22, 24, 18, 24));

        // Contenedor que fija el tamaño del icono a 44x44 y previene estiramiento en el BorderLayout
        JPanel pnlIconWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlIconWrapper.setOpaque(false);

        JLabel lblIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Círculo de fondo perfecto
                g2.setColor(iconBg);
                g2.fillOval(2, 2, 40, 40);
                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(2, 2, 40, 40);

                // Dibujar icono vectorial nítido sin problemas de fuentes Unicode
                int cx = 22;
                int cy = 22;
                g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (tipo == TIPO_EXITO) {
                    g2.setColor(COLOR_EXITO);
                    g2.drawLine(cx - 7, cy, cx - 2, cy + 6);
                    g2.drawLine(cx - 2, cy + 6, cx + 8, cy - 5);
                } else if (tipo == TIPO_ERROR) {
                    g2.setColor(COLOR_ERROR);
                    g2.drawLine(cx - 6, cy - 6, cx + 6, cy + 6);
                    g2.drawLine(cx + 6, cy - 6, cx - 6, cy + 6);
                } else if (tipo == TIPO_AVISO) {
                    g2.setColor(new Color(180, 83, 9));
                    g2.drawLine(cx, cy - 8, cx, cy + 2);
                    g2.fillOval(cx - 2, cy + 6, 4, 4);
                } else {
                    g2.setColor(COLOR_AZUL);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = (44 - fm.stringWidth("i")) / 2;
                    int ty = (44 - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("i", tx, ty);
                }
                g2.dispose();
            }
        };
        lblIcon.setPreferredSize(new Dimension(44, 44));
        lblIcon.setMinimumSize(new Dimension(44, 44));
        lblIcon.setMaximumSize(new Dimension(44, 44));
        pnlIconWrapper.add(lblIcon);

        JPanel pnlMsg = new JPanel(new BorderLayout(0, 6));
        pnlMsg.setOpaque(false);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTit.setForeground(COLOR_AZUL);

        String textoMsg = mensaje != null ? mensaje : "";
        if (textoMsg.contains("Exception: ")) {
            textoMsg = textoMsg.substring(textoMsg.indexOf("Exception: ") + 11);
        }

        JLabel lblDesc = new JLabel("<html><body style='width: 330px; font-family: Segoe UI; font-size: 11px;'>" +
                textoMsg.replace("\n", "<br>") + "</body></html>");
        lblDesc.setForeground(COLOR_TEXTO);

        pnlMsg.add(lblTit, BorderLayout.NORTH);
        pnlMsg.add(lblDesc, BorderLayout.CENTER);

        body.add(pnlIconWrapper, BorderLayout.WEST);
        body.add(pnlMsg, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(240, 242, 246)));

        JButton btnOk = crearBoton("Aceptar", COLOR_AMARILLO, COLOR_AZUL, COLOR_AMARILLO_OSC);
        btnOk.setPreferredSize(new Dimension(110, 36));
        btnOk.addActionListener(e -> dialog.dispose());

        actions.add(btnOk);

        content.add(body, BorderLayout.CENTER);
        content.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        btnOk.requestFocusInWindow();
        dialog.setVisible(true);
    }

    public static boolean mostrarDialogoConfirmacion(Window parent, String titulo, String mensaje) {
        final boolean[] resultado = new boolean[]{false};
        JDialog dialog = new JDialog(parent, titulo, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(COLOR_BLANCO);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createMatteBorder(5, 0, 0, 0, COLOR_AMARILLO)
        ));

        JPanel body = new JPanel(new BorderLayout(16, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(22, 24, 18, 24));

        JPanel pnlIconWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlIconWrapper.setOpaque(false);

        JLabel lblIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_AMARILLO_SUAVE);
                g2.fillOval(2, 2, 40, 40);
                g2.setColor(COLOR_AMARILLO_OSC);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(2, 2, 40, 40);

                g2.setColor(COLOR_AZUL);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (44 - fm.stringWidth("?")) / 2;
                int ty = (44 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString("?", tx, ty);
                g2.dispose();
            }
        };
        lblIcon.setPreferredSize(new Dimension(44, 44));
        lblIcon.setMinimumSize(new Dimension(44, 44));
        lblIcon.setMaximumSize(new Dimension(44, 44));
        pnlIconWrapper.add(lblIcon);

        JPanel pnlMsg = new JPanel(new BorderLayout(0, 6));
        pnlMsg.setOpaque(false);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTit.setForeground(COLOR_AZUL);

        JLabel lblDesc = new JLabel("<html><body style='width: 320px; font-family: Segoe UI; font-size: 11px;'>" +
                mensaje.replace("\n", "<br>") + "</body></html>");
        lblDesc.setForeground(COLOR_TEXTO);

        pnlMsg.add(lblTit, BorderLayout.NORTH);
        pnlMsg.add(lblDesc, BorderLayout.CENTER);

        body.add(pnlIconWrapper, BorderLayout.WEST);
        body.add(pnlMsg, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(240, 242, 246)));

        JButton btnCancelar = crearBoton("Cancelar", COLOR_BLANCO, COLOR_TEXTO, COLOR_BORDE);
        btnCancelar.setPreferredSize(new Dimension(100, 36));
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnAceptar = crearBoton("Confirmar", COLOR_AMARILLO, COLOR_AZUL, COLOR_AMARILLO_OSC);
        btnAceptar.setPreferredSize(new Dimension(110, 36));
        btnAceptar.addActionListener(e -> {
            resultado[0] = true;
            dialog.dispose();
        });

        actions.add(btnCancelar);
        actions.add(btnAceptar);

        content.add(body, BorderLayout.CENTER);
        content.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return resultado[0];
    }

    // Método de utilidad para crear botones modernos redondeados con colores corporativos sólidos
    public static JButton crearBoton(String texto, Color bg, Color fg, Color borde) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Color currentColor = bg;
                if (!isEnabled()) {
                    currentColor = new Color(229, 231, 235);
                } else if (getModel().isPressed()) {
                    currentColor = bg.darker();
                } else if (getModel().isRollover()) {
                    if (bg.equals(COLOR_AMARILLO)) {
                        currentColor = COLOR_AMARILLO_OSC;
                    } else if (bg.equals(COLOR_BLANCO)) {
                        currentColor = new Color(238, 243, 252);
                    } else {
                        currentColor = bg.brighter();
                    }
                }

                g2.setColor(currentColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                Color borderCol = borde != null ? borde : currentColor;
                if (!isEnabled()) {
                    borderCol = new Color(209, 213, 219);
                }
                g2.setColor(borderCol);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);

                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                Color textCol = isEnabled() ? fg : new Color(156, 163, 175);
                g2.setColor(textCol);
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        return btn;
    }

    // Clase auxiliar para el ComboBox de celulares
    private static class CelularItem {
        final Celular celular;
        CelularItem(Celular c) { this.celular = c; }
        @Override
        public String toString() {
            return celular.getMarca() + " " + celular.getModelo() + " ($ " + celular.getPrecio() + ")";
        }
    }
}
