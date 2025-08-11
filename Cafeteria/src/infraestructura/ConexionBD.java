package infraestructura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://localhost:5432/cafeteria";
    private static final String USUARIO = "postgres";
    private static final String CONTRASENA = "admi";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // Cargar driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontro el Driver de PostgreSQL", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
    
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

   public static void registrarError(String error_al_actualizar_producto, SQLException e) {
    }
}
