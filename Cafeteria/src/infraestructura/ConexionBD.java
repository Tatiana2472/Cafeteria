package infraestructura;

import java.io.PrintWriter;
import java.io.StringWriter;
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
            // Carga driver JDBC PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error cargando driver JDBC: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para obtener conexión
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    // Registra errores en un log simple (puedes cambiar a logger real)
    public static void registrarError(String mensaje, Exception e) {
        System.err.println("[" + LocalDateTime.now() + "] ERROR: " + mensaje);
        if (e != null) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
        }
    }
}

