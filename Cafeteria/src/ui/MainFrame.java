/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dominio.*;
import servicio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author tatia
 */
public class MainFrame extends JFrame {
    private Usuario usuario;
    private DefaultListModel<String> logListModel = new DefaultListModel<>();

    public MainFrame(Usuario usuario) {
        this.usuario = usuario;
         setTitle("Cafetería UTN - " + usuario.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearMenu();
        add(new JLabel("Bienvenido al sistema de caja.", SwingConstants.CENTER));
    }
    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
        
        // Productos
        JMenu menuProductos = new JMenu("Productos");
        JMenuItem itemGestionar = new JMenuItem("Gestionar Productos");
        itemGestionar.addActionListener(e -> abrirGestionProductos());

        // Ventas
        JMenu menuVentas = new JMenu("Ventas");
        JMenuItem itemNuevaVenta = new JMenuItem("Nueva Venta");
        itemNuevaVenta.addActionListener(e -> hacerVenta());
        JMenuItem itemHistorial = new JMenuItem("Historial de Ventas");
        itemHistorial.addActionListener(e -> verHistorialVentas());

        // Herramientas
        JMenu menuHerramientas = new JMenu("Herramientas");
        JMenuItem itemCalculadora = new JMenuItem("Calculadora");
        itemCalculadora.addActionListener(e -> abrirCalculadora());

        // Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemLogs = new JMenuItem("Ver Logs");
        itemLogs.addActionListener(e -> verLogs());

        // Añadir al menú
        menuProductos.add(itemGestionar);
        menuVentas.add(itemNuevaVenta);
        menuVentas.add(itemHistorial);
        menuHerramientas.add(itemCalculadora);
        menuAyuda.add(itemLogs);

        menuBar.add(menuArchivo);
        menuBar.add(menuProductos);
        menuVentas.addSeparator();
        menuVentas.add(itemHistorial);
        menuBar.add(menuVentas);
        menuBar.add(menuHerramientas);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);
    }
    
    private void abrirGestionProductos() {
        ProductoService service = new ProductoService();
        List<Producto> productos = (List<Producto>) service.getProductosActivos();

        String[] nombres = productos.stream()
                .map(p -> p.getNombre() + " - $" + p.getPrecioUnitario())
                .toArray(String[]::new);
        
        JList<String> lista = new JList<>(nombres);
        JScrollPane scroll = new JScrollPane(lista);

        int opcion = JOptionPane.showOptionDialog(this, scroll, "Gestionar Productos",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                new String[]{"Editar", "Cerrar"}, "Editar");

        if (opcion == 0 && lista.getSelectedIndex() != -1) {
            Producto p = productos.get(lista.getSelectedIndex());
            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", p.getNombre());
            String nuevoPrecioStr = JOptionPane.showInputDialog(this, "Nuevo precio:", p.getPrecioUnitario());
            
        if (nuevoNombre != null && nuevoPrecioStr != null) {
                try {
                    double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                    p.setNombre(nuevoNombre);
                    p.setPrecioUnitario(nuevoPrecio);
                    service.actualizarProducto(p);
                    JOptionPane.showMessageDialog(this, "Producto actualizado.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Precio inválido.");
                }
            }
        }
    }
    
    private void hacerVenta() {
        Venta venta = new Venta(usuario.getId());
        ProductoService prodService = new ProductoService();
        VentaService ventaService = new VentaService();
        List<Producto> productos = prodService.getProductosActivos();

        do {
            String[] opciones = productos.stream()
                    .map(p -> p.getNombre() + " ($" + p.getPrecioUnitario() + ")")
                    .toArray(String[]::new);

            String seleccion = (String) JOptionPane.showInputDialog(this, "Seleccione producto:",
                    "Agregar producto", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (seleccion == null) break;
            
            Producto prod = productos.get(java.util.Arrays.asList(opciones).indexOf(seleccion));
            String cantStr = JOptionPane.showInputDialog(this, "Cantidad:", "1");
            try {
                int cantidad = Integer.parseInt(cantStr);
                DetalleVenta det = new DetalleVenta(0, prod.getId(), cantidad, prod.getPrecioUnitario());
                venta.agregarDetalle(det);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
          } while (JOptionPane.showConfirmDialog(this, "¿Agregar otro producto?", "Agregar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

        if (venta.getDetalles().isEmpty()) return;

        // Calcular totales
        double subtotal = venta.getDetalles().stream().mapToDouble(DetalleVenta::getTotalLinea).sum();
        double iva = subtotal * 0.07;
        double ivi = subtotal * 0.13;
        String descStr = JOptionPane.showInputDialog(this, "Descuento ($):", "0");
        double descuento = 0;
        try { descuento = Double.parseDouble(descStr); } catch (Exception e) {}

        double total = subtotal + iva + ivi - descuento;

        venta.setSubtotal(subtotal);
        venta.setImpuestoIVA(iva);
        venta.setImpuestoIVI(ivi);
        venta.setDescuento(descuento);
        venta.setTotal(total);

        // Guardar
        ventaService.registrarVenta(venta);

        // Imprimir ticket
        imprimirTicket(venta);

        JOptionPane.showMessageDialog(this, "Venta registrada. Total: $" + total);
    }
    
    private void imprimirTicket(Venta venta) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        sb.append("CAFETERÍA UTN\n");
        sb.append("Fecha: ").append(venta.getFechaHora().format(df)).append("\n");
        sb.append("Usuario: ").append(usuario.getUsername()).append("\n\n");
        
        for (DetalleVenta d : venta.getDetalles()) {
            Producto p = new ProductoService().findById(d.getProductoId());
            sb.append(p.getNombre()).append(" x").append(d.getCantidad())
                    .append(": $").append(String.format("%.2f", d.getTotalLinea())).append("\n");
        }
        sb.append("\nSubtotal: $").append(String.format("%.2f", venta.getSubtotal())).append("\n");
        sb.append("IVA (7%): $").append(String.format("%.2f", venta.getImpuestoIVA())).append("\n");
        sb.append("IVI (13%): $").append(String.format("%.2f", venta.getImpuestoIVI())).append("\n");
        if (venta.getDescuento() > 0) {
        sb.append("Descuento: -$").append(String.format("%.2f", venta.getDescuento())).append("\n");
        }
        sb.append("Total: $").append(String.format("%.2f", venta.getTotal())).append("\n");
        sb.append("\n¡Gracias por su compra!");

        try (FileWriter fw = new FileWriter("ticket_" + venta.getId() + ".txt")) {
            fw.write(sb.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el ticket.");
        }
    }

    private void verHistorialVentas() {
        VentaService service = new VentaService();
        List<Venta> ventas = service.getVentasDelDia(usuario.getId());
        
        StringBuilder sb = new StringBuilder("Ventas del día:\n\n");
        for (Venta v : ventas) {
            sb.append("ID:").append(v.getId())
                    .append(" | Total: $").append(String.format("%.2f", v.getTotal()))
                    .append(" | ").append(v.getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .append("\n");
        }
        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString())));
    }
    
     private void abrirCalculadora() {
        JFrame calc = new JFrame("Calculadora");
        calc.setLayout(new BorderLayout());

        JTextField display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        calc.add(display, BorderLayout.NORTH);
        
        JPanel panel = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] botones = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "MOD", "POT", "%", "C"
        };
        
        for (String b : botones) {
            JButton btn = new JButton(b);
            btn.addActionListener(e -> {
                String op = btn.getText();
                String actual = display.getText();

                if (op.equals("C")) {
                    display.setText("0");
                } else if (op.equals("=")) {
                    try {
                        // Simplificado: solo una operación
                        String expr = actual.replaceAll("×", "*").replaceAll("÷", "/");
                        // Aquí deberías usar un parser real, pero por simplicidad:
                        // Este ejemplo es básico. En producción, usa un evaluador de expresiones.
                        display.setText("Error");
                    } catch (Exception ex) {
                        display.setText("Error");
                    }
                  } else if (op.equals("MOD")) {
                    display.setText(actual + " % ");
                } else if (op.equals("POT")) {
                    display.setText(actual + " ^ ");
                } else if (op.equals("%")) {
                    try {
                        double val = Double.parseDouble(actual);
                        display.setText(String.valueOf(val / 100));
                    } catch (Exception ex) {
                        display.setText("Error");
                    }
                } else {
                    if (actual.equals("0") && "0123456789".contains(op)) {
                        display.setText(op);
                    } else {
                        display.setText(actual + op);
                    }
                }
            });
            panel.add(btn);
        }

        calc.add(panel, BorderLayout.CENTER);
        calc.pack();
        calc.setLocationRelativeTo(this);
        calc.setVisible(true);
    }
     
    private void verLogs() {
        JList<String> list = new JList<>(logListModel);
        LogRepositorio repo = new LogRepositorio();
        logListModel.clear();
        repo.obtenerLogs().forEach(logListModel::addElement);

        JOptionPane.showMessageDialog(this, new JScrollPane(list), "Logs del Sistema", JOptionPane.INFORMATION_MESSAGE);
    }
}

