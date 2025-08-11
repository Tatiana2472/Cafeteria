/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dominio.*;
import infraestructura.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author tatia
 */
public class VentaService {
   private static final double IVA = 0.07;
    private static final double IVI = 0.13;

    public int registrarVenta(Venta venta) throws SQLException {
        Connection conn = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDetalle = null;
        ResultSet rsKeys = null;

        String sqlVenta = "INSERT INTO VENTAS(user_id, fecha_hora, subtotal, impuestoIVA, impuestoIVI, descuento, total) VALUES (?, NOW(), ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO DETALLES_VENTA(venta_id, product_id, cantidad, precio_unit, total_linea) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = ConexionBD.getConexion();
            conn.setAutoCommit(false);

            psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, venta.getUsuario().getId());
            psVenta.setDouble(2, venta.getSubtotal());
            psVenta.setDouble(3, venta.getImpuestoIVA());
            psVenta.setDouble(4, venta.getImpuestoIVI());
            psVenta.setDouble(5, venta.getDescuento());
            psVenta.setDouble(6, venta.getTotal());
            psVenta.executeUpdate();

            rsKeys = psVenta.getGeneratedKeys();
            if (rsKeys.next()) {
                int ventaId = rsKeys.getInt(1);

                psDetalle = conn.prepareStatement(sqlDetalle);

                for (DetalleVenta d : venta.getDetalles()) {
                    psDetalle.setInt(1, ventaId);
                    psDetalle.setInt(2, d.getProducto().getId());
                    psDetalle.setInt(3, d.getCantidad());
                    psDetalle.setDouble(4, d.getPrecioUnitario()); // Revisa que sea getPrecioUnitario()
                    psDetalle.setDouble(5, d.getTotalLinea());
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
                conn.commit();

                logEvento("INFO", "Venta registrada", "ID Venta: " + ventaId);

                return ventaId;
            } else {
                conn.rollback();
                throw new SQLException("No se pudo obtener ID de venta.");
            }
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            logEvento("ERROR", "Error al registrar venta", e.getMessage());
            throw e;
        } finally {
            if (rsKeys != null) rsKeys.close();
            if (psDetalle != null) psDetalle.close();
            if (psVenta != null) psVenta.close();
            if (conn != null) conn.close();
        }
    }

    public List<Venta> listarVentasDelDia(LocalDate fecha) throws SQLException {
        List<Venta> ventas = new ArrayList<>();

        String sql = "SELECT * FROM VENTAS WHERE DATE(fecha_hora) = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Crea Usuario solo con ID para evitar errores
                Usuario u = new Usuario(rs.getInt("user_id"), "", "", "", true, LocalDateTime.now());
                
                Venta v = new Venta(u);  // Constructor con Usuario
                v.setId(rs.getInt("id"));
                v.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                v.setSubtotal(rs.getDouble("subtotal"));
                v.setImpuestoIVA(rs.getDouble("impuestoIVA"));
                v.setImpuestoIVI(rs.getDouble("impuestoIVI"));
                v.setDescuento(rs.getDouble("descuento"));
                v.setTotal(rs.getDouble("total"));

                // Si quieres, carga detalles aquí

                ventas.add(v);
            }
        }
        return ventas;
    }

    private void logEvento(String nivel, String evento, String detalle) {
        try (Connection conn = ConexionBD.getConexion()) {
            String sql = "INSERT INTO LOGS(fecha_hora, nivel, evento, detalle) VALUES(NOW(), ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nivel);
            ps.setString(2, evento);
            ps.setString(3, detalle);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

