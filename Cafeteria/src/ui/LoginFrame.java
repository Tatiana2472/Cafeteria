/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dominio.Usuario;
import servicio.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 *
 * @author tatia
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private AuthService authService;
    private int intentosFallidos = 0;

    public LoginFrame() {
        setTitle("Login - Café T&J");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        // Panel principal con color suave y padding
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 240, 245)); // Rosa pastel suave
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);

        // Etiqueta Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUsuario.setForeground(new Color(102, 51, 102)); // Morado suave
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblUsuario, gbc);

        // Campo Usuario grande, con borde redondeado
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.setPreferredSize(new Dimension(250, 35));
        txtUsername.setBackground(new Color(255, 250, 250));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(204, 153, 204), 2, true), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(txtUsername, gbc);

        // Etiqueta Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPassword.setForeground(new Color(102, 51, 102));
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(lblPassword, gbc);

        // Campo Contraseña grande, con borde redondeado
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setPreferredSize(new Dimension(250, 35));
        txtPassword.setBackground(new Color(255, 250, 250));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(204, 153, 204), 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(txtPassword, gbc);

        // Botón Login grande, redondeado y con degradado suave
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLogin.setBackground(new Color(221, 160, 221)); // Lavanda
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(200, 40));
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(153, 102, 153), 2, true));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogin.addActionListener(e -> manejarLogin());

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnLogin, gbc);

        add(panel);
    }

    private void manejarLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try {
            Usuario usuario = authService.login(username, password);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido " + usuario.getUsername());
                dispose(); // Cierra ventana
                // Aquí podrías abrir la ventana principal
            } else {
                intentosFallidos++;
                if (intentosFallidos >= 3) {
                    JOptionPane.showMessageDialog(this, "Demasiados intentos fallidos. Saliendo...");
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Intentos: " + intentosFallidos);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + ex.getMessage());
        }
    }
}
