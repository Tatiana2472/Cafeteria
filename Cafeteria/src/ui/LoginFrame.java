/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;


import servicio.AuthService;
import dominio.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author tatia
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private int intentos = 0;

    public LoginFrame() {
        setTitle("Login - Cafetería Josss y Tati");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Panel central
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario:", SwingConstants.RIGHT));
        txtUsuario = new JTextField();
        panel.add(new JLabel("Contraseña:", SwingConstants.RIGHT));
        txtPassword = new JPasswordField();

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(this::onLogin);

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onLogin(null);
                }
            }
        });
        
        panel.add(new JLabel());
        panel.add(btnLogin);

        add(panel, BorderLayout.CENTER);

        // Mensaje de intentos
        JLabel lblIntentos = new JLabel("Intentos: 0/3", SwingConstants.CENTER);
        add(lblIntentos, BorderLayout.SOUTH);
        
        // Actualizar intentos
        btnLogin.addActionListener(e -> {
            intentos++;
            lblIntentos.setText("Intentos: " + intentos + "/3");
            if (intentos >= 3) {
                btnLogin.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Demasiados intentos fallidos.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void onLogin(ActionEvent e) {
        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AuthService auth = new AuthService();
        Usuario user = auth.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido, " + user.getUsername() + "!");
            new MainFrame(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de login", JOptionPane.ERROR_MESSAGE);
        }
    }
}
