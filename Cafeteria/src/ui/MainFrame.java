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
        List<Producto> productos = service.getProductosActivos();

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
                

