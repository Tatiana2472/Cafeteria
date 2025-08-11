/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infraestructura;


import infraestructura.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tatia
 */
public class LogRepositorio {
    public List<String> obtenerLogs() {
        List<String> logs = new ArrayList<>();
        String sql = "SELECT fecha_hora, nivel, evento, detalle FROM LOGS ORDER BY fecha_hora DESC LIMIT 50";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String linea = String.format("[%s] %s: %s - %s",
                        rs.getString("fecha_hora"),
                        rs.getString("nivel"),
                        rs.getString("evento"),
                        rs.getString("detalle"));
                logs.add(linea);
            }
        } catch (SQLException e) {
            System.err.println("No se pudieron cargar los logs: " + e.getMessage());
        }
        return logs;
    }
}
