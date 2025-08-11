/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dominio.DetalleVenta;
import dominio.Producto;
import dominio.Usuario;
import dominio.Venta;
import servicio.TicketPDF;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author tatia
 */
public class MainFrame extends JFrame {

    private JLabel lblSubtotal, lblIVA, lblIVI, lblDescuento, lblTotal;
    private Venta venta;
    private JTable tablaDetalles;
    private DetalleTableModel detalleTableModel;

    public MainFrame(Usuario usuario) {
        super("Sistema Cafetería");

        this.venta = new Venta(usuario);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Panel etiquetas de totales
        JPanel panelTotales = new JPanel(new GridLayout(5, 2, 10, 10));
        panelTotales.add(new JLabel("Subtotal:"));
        lblSubtotal = new JLabel("$0.00");
        panelTotales.add(lblSubtotal);

        panelTotales.add(new JLabel("IVA (7%):"));
        lblIVA = new JLabel("$0.00");
        panelTotales.add(lblIVA);

        panelTotales.add(new JLabel("IVI (13%):"));
        lblIVI = new JLabel("$0.00");
        panelTotales.add(lblIVI);

        panelTotales.add(new JLabel("Descuento:"));
        lblDescuento = new JLabel("$0.00");
        panelTotales.add(lblDescuento);

        panelTotales.add(new JLabel("Total:"));
        lblTotal = new JLabel("$0.00");
        panelTotales.add(lblTotal);

        // Tabla detalles
        detalleTableModel = new DetalleTableModel(venta.getDetalles());
        tablaDetalles = new JTable(detalleTableModel);
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);

        // Botones
        JButton btnAgregarProducto = new JButton("Agregar Producto Ejemplo");
        btnAgregarProducto.addActionListener(e -> agregarProductoEjemplo());

        JButton btnGenerarPDF = new JButton("Generar Ticket PDF");
        btnGenerarPDF.addActionListener(e -> generarTicketPDF());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregarProducto);
        panelBotones.add(btnGenerarPDF);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(panelTotales, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void agregarProductoEjemplo() {
        // Crear producto real, con constructor correcto
        Producto prod = new Producto(1, "Café Expreso", 1200.50, true, null);
        // Construir DetalleVenta con constructor real (verifica en dominio)
        DetalleVenta detalle = new DetalleVenta(venta, prod, 1, prod.getPrecioUnitario());
        venta.agregarDetalle(detalle);

        venta.calcularTotales();
        detalleTableModel.fireTableDataChanged();
        actualizarLabelsTotales();
    }

    private void actualizarLabelsTotales() {
        lblSubtotal.setText(FormateoUtil.formatearMoneda(venta.getSubtotal()));
        lblIVA.setText(FormateoUtil.formatearMoneda(venta.getImpuestoIVA()));
        lblIVI.setText(FormateoUtil.formatearMoneda(venta.getImpuestoIVI()));
        lblDescuento.setText(FormateoUtil.formatearMoneda(venta.getDescuento()));
        lblTotal.setText(FormateoUtil.formatearMoneda(venta.getTotal()));
    }

    private void generarTicketPDF() {
        try {
            if (venta.getDetalles().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay productos en la venta para generar ticket.");
                return;
            }
            if (!Files.exists(Paths.get("tickets"))) {
                Files.createDirectory(Paths.get("tickets"));
            }
            String ruta = "tickets/ticket_" + venta.getUsuario().getId() + "_" + System.currentTimeMillis() + ".pdf";

            // Aquí llamas al método estático correcto para generar PDF
            TicketPDF.generarTicketPDF(venta, ruta);

            JOptionPane.showMessageDialog(this, "Ticket PDF generado en: " + ruta);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generando PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Formateo moneda
    public static class FormateoUtil {
        public static String formatearMoneda(double valor) {
            java.text.NumberFormat formato = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("es", "CR"));
            return formato.format(valor);
        }
    }

    // Modelo tabla para mostrar detalles (usa tus clases dominio.DetalleVenta)
    public static class DetalleTableModel extends AbstractTableModel {

        private final List<DetalleVenta> detalles;
        private final String[] columnas = {"Producto", "Cantidad", "Precio Unitario", "Total Línea"};

        public DetalleTableModel(List<DetalleVenta> detalles) {
            this.detalles = detalles;
        }

        @Override
        public int getRowCount() {
            return detalles.size();
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnas[col];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            DetalleVenta d = detalles.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> d.getProducto().toString(); // para pruebas
                case 1 -> d.getCantidad();
                case 2 -> FormateoUtil.formatearMoneda(d.getPrecioUnitario());
                case 3 -> FormateoUtil.formatearMoneda(d.getTotalLinea());
                default -> "";
            };
        }
    }

    public static void main(String[] args) {
    Usuario u = new Usuario(1, "Tati", "email@correo.com", "hashedpassword", true, LocalDateTime.now());
    SwingUtilities.invokeLater(() -> new MainFrame(u));
    }
}