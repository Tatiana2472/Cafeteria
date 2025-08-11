/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author tatia
 */
public class CalculadoraWindow extends JFrame {
    private JTextField txtDisplay;

    public CalculadoraWindow() {
        super("Calculadora");
        initComponents();
    }

    private void initComponents() {
        setSize(350, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        txtDisplay = new JTextField();
        txtDisplay.setEditable(false);
        txtDisplay.setFont(new Font("Consolas", Font.BOLD, 24));
        txtDisplay.setHorizontalAlignment(JTextField.RIGHT);
        add(txtDisplay, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(5, 4, 5, 5));

        String[] botones = {
                "7", "8", "9", "+",
                "4", "5", "6", "-",
                "1", "2", "3", "×",
                "0", ".", "÷", "MOD",
                "C", "Pot", "%", "="
        };

        for (String texto : botones) {
            JButton btn = new JButton(texto);
            btn.setFont(new Font("Consolas", Font.BOLD, 18));
            btn.addActionListener(this::botonPresionado);
            panelBotones.add(btn);
        }

        add(panelBotones, BorderLayout.CENTER);
    }

    private String operador = "";
    private double primerNumero = 0;
    private boolean nuevoNumero = true;

    private void botonPresionado(ActionEvent e) {
        String texto = e.getActionCommand();

        if ("0123456789.".contains(texto)) {
            if (nuevoNumero) {
                txtDisplay.setText(texto);
                nuevoNumero = false;
            } else {
                txtDisplay.setText(txtDisplay.getText() + texto);
            }
            return;
        }

        switch (texto) {
            case "C":
                txtDisplay.setText("");
                operador = "";
                primerNumero = 0;
                nuevoNumero = true;
                break;
            case "+":
            case "-":
            case "×":
            case "÷":
            case "MOD":
            case "Pot":
            case "%":
                if (!operador.isEmpty()) {
                    calcular();
                } else {
                    primerNumero = obtenerNumero();
                }
                operador = texto;
                nuevoNumero = true;
                break;
            case "=":
                calcular();
                operador = "";
                nuevoNumero = true;
                break;
        }
    }

    private double obtenerNumero() {
        try {
            return Double.parseDouble(txtDisplay.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    private void calcular() {
        double segundoNumero = obtenerNumero();
        double resultado = 0;

        try {
            switch (operador) {
                case "+":
                    resultado = primerNumero + segundoNumero;
                    break;
                case "-":
                    resultado = primerNumero - segundoNumero;
                    break;
                case "×":
                    resultado = primerNumero * segundoNumero;
                    break;
                case "÷":
                    if (segundoNumero == 0) {
                        JOptionPane.showMessageDialog(this, "No se puede dividir entre 0", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    resultado = primerNumero / segundoNumero;
                    break;
                case "MOD":
                    resultado = primerNumero % segundoNumero;
                    break;
                case "Pot":
                    resultado = Math.pow(primerNumero, segundoNumero);
                    break;
                case "%":
                    resultado = primerNumero * (segundoNumero / 100);
                    break;
            }
            txtDisplay.setText(String.valueOf(resultado));
            primerNumero = resultado;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en cálculo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
