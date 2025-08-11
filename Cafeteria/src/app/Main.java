package app;

import javax.swing.SwingUtilities;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
         // Ejecutar la UI en el hilo de eventos (buena práctica)
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}