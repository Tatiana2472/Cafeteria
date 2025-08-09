package app;

import infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = ConexionBD.obtenerConexion()) {
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a PostgreSQL");
            } else {
                System.out.println("❌ No se pudo establecer conexión");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar a la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
