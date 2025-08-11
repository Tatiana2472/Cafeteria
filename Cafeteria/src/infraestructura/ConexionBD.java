package infraestructura;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

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

   public static void registrarError(String mensaje, SQLException e) {
        String sql = "INSERT INTO logs (mensaje, detalle, fecha) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mensaje);
            stmt.setString(2, e.getMessage());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.err.println("No se pudo registrar el error en la base de datos: " + ex.getMessage());
        }
    }
}
