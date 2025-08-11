/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dominio.Usuario;
import servicio.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public LoginFrame() {
        super("Login - Cafetería");

        authService = new AuthService();

        // Configuración básica ventana
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel para campos
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelCampos.add(new JLabel("Usuario:"));
        txtUsername = new JTextField();
        panelCampos.add(txtUsername);

        panelCampos.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelCampos.add(txtPassword);

        add(panelCampos, BorderLayout.CENTER);

        // Panel para botón
        JPanel panelBoton = new JPanel();
        btnLogin = new JButton("Ingresar");
        panelBoton.add(btnLogin);
        add(panelBoton, BorderLayout.SOUTH);

        // Acción botón login
        btnLogin.addActionListener(e -> realizarLogin());

        // Enter para login desde contraseña
        txtPassword.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (authService.estaBloqueado(username)) {
                JOptionPane.showMessageDialog(this, "Usuario bloqueado. Contacte al administrador.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loginExitoso = authService.login(username, password);

            if (loginExitoso) {
                Usuario usuario = authService.obtenerUsuario(username);
                JOptionPane.showMessageDialog(this, "¡Bienvenido, " + usuario.getUsername() + "!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                // Aquí puedes abrir la ventana principal o hacer lo que sigue en tu app
                this.dispose(); // cerrar login
                // new MainFrame(usuario).setVisible(true); // ejemplo de abrir siguiente ventana
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
