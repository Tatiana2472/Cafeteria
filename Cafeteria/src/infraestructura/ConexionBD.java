package infraestructura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://localhost:5432/cafeteria";
    private static final String USUARIO = "postgres";
    private static final String CONTRASENA = "sa123456";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // Cargar driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el Driver de PostgreSQL", e);
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
