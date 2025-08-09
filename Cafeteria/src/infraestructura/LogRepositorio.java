package infraestructura;

import dominio.LogEntry;
import java.sql.*;
import java.time.LocalDateTime;

public class LogRepositorio {

    public void registrar(LogEntry log) {
        String sql = "INSERT INTO LOGS (fecha_hora, nivel, evento, detalle, stacktrace) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, log.getNivel());
            stmt.setString(3, log.getEvento());
            stmt.setString(4, log.getDetalle());
            stmt.setString(5, log.getStacktrace());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

