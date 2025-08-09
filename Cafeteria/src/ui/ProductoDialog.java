/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dominio.Producto;
import servicio.ProductoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author tatia
 */
public class ProductoDialog extends JDialog {
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private Producto producto;
    private boolean guardado = false;
     private ProductoService productoService = new ProductoService();

    /**
     * Constructor para CREAR un nuevo producto
     */
    public ProductoDialog(JFrame parent) {
        super(parent, "Nuevo Producto", true);
        initComponents();
        setupLayout();
        addEventHandlers();
        setLocationRelativeTo(parent);
        setResizable(false);
        pack();
    }
    
    /**
     * Constructor para EDITAR un producto existente
     */
    public ProductoDialog(JFrame parent, Producto producto) {
        this(parent);
        this.producto = producto;
        setTitle("Editar Producto");
        txtNombre.setText(producto.getNombre());
        txtPrecio.setText(String.valueOf(producto.getPrecioUnitario()));
    }
    
    private void initComponents() {
        txtNombre = new JTextField(20);
        txtPrecio = new JTextField(20);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Panel de campos
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelCampos.add(new JLabel("Nombre del producto:", SwingConstants.RIGHT));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Precio unitario:", SwingConstants.RIGHT));
        panelCampos.add(txtPrecio);
        
        add(panelCampos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // Tamaño mínimo
        setSize(400, 180);
    }
    
    private void addEventHandlers() {
        // Guardar
        btnGuardar.addActionListener((ActionEvent e) -> {
            if (validarCampos()) {
                if (producto == null) {
                    producto = new Producto(txtNombre.getText().trim(), Double.parseDouble(txtPrecio.getText().trim()));
                    productoService.crearProducto(producto);
                } else {
                    producto.setNombre(txtNombre.getText().trim());
                    producto.setPrecioUnitario(Double.parseDouble(txtPrecio.getText().trim()));
                    productoService.actualizarProducto(producto);
                }
                guardado = true;
                JOptionPane.showMessageDialog(this, "Producto guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        
        // Cancelar
        btnCancelar.addActionListener((ActionEvent e) -> dispose());

        // Cerrar ventana sin guardar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        
        // Permitir Enter en los campos
        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    btnGuardar.doClick();
                }
            }
        });
    }
    
    private boolean validarCampos() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }
        
        if (precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El precio es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
            txtPrecio.requestFocus();
            return false;
        }

        try {
           double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0.", "Error", JOptionPane.WARNING_MESSAGE);
                txtPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error", JOptionPane.WARNING_MESSAGE);
            txtPrecio.requestFocus();
            return false;
        }
        
         return true;
    }

    /**
     * Indica si el producto fue guardado correctamente
     */
    public boolean isGuardado() {
        return guardado;
    }
    
    /**
     * Devuelve el producto guardado (solo si isGuardado() == true)
     */
    public Producto getProducto() {
        return producto;
    }
}
