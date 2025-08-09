/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infraestructura;

import dominio.DetalleVenta;
import dominio.Venta;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tatia
 */
public class VentaRepositorio {
    public void guardar(Venta venta) {
        String sqlVenta = "INSERT INTO VENTAS(user_id, subtotal, impuestoIVA, impuestoIVI, descuento, total) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, venta.getUserId());
                pstmt.setDouble(2, venta.getSubtotal());
                pstmt.setDouble(3, venta.getImpuestoIVA());
                pstmt.setDouble(4, venta.getImpuestoIVI());
                pstmt.setDouble(5, venta.getDescuento());
                pstmt.setDouble(6, venta.getTotal());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    venta.setId(rs.getInt(1));

                    // Guardar detalles
                    String sqlDetalle = "INSERT INTO DETALLES_VENTA(venta_id, product_id, cantidad, precio_unit, total_linea) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmtDet = conn.prepareStatement(sqlDetalle)) {
                        for (DetalleVenta det : venta.getDetalles()) {
                            pstmtDet.setInt(1, venta.getId());
                            pstmtDet.setInt(2, det.getProductoId());
                            pstmtDet.setInt(3, det.getCantidad());
                            pstmtDet.setDouble(4, det.getPrecioUnit());
                            pstmtDet.setDouble(5, det.getTotalLinea());
                            pstmtDet.addBatch();
                        }
                        pstmtDet.executeBatch();
                    }
                    conn.commit();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al guardar venta", e);
        }
    }

    public List<Venta> listarVentasDelDia(int userId) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM VENTAS WHERE user_id = ? AND DATE(fecha_hora) = DATE('now')";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Venta v = new Venta(rs.getInt("user_id"));
                v.setId(rs.getInt("id"));
                v.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                v.setSubtotal(rs.getDouble("subtotal"));
                v.setImpuestoIVA(rs.getDouble("impuestoIVA"));
                v.setImpuestoIVI(rs.getDouble("impuestoIVI"));
                v.setDescuento(rs.getDouble("descuento"));
                v.setTotal(rs.getDouble("total"));
                ventas.add(v);
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al listar ventas del día", e);
        }
        return ventas;
    }

    public List<Object[]> reporteProductosVendidos() {
        List<Object[]> reporte = new ArrayList<>();
        String sql = """
            SELECT p.nombre, SUM(d.cantidad) as total_vendido
            FROM DETALLES_VENTA d
            JOIN PRODUCTOS p ON d.product_id = p.id
            GROUP BY p.id, p.nombre
            ORDER BY total_vendido DESC
            """;
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reporte.add(new Object[]{rs.getString("nombre"), rs.getInt("total_vendido")});
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error en reporte de productos vendidos", e);
        }
        return reporte;
    }
}
