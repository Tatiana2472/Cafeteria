/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infraestructura;

import dominio.DetalleVenta;
import dominio.Producto;
import dominio.Usuario;
import dominio.Venta;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tatia
 */
public class VentaRepositorio {
    public void guardarVenta(Venta venta) throws SQLException {
        String sqlVenta = "INSERT INTO VENTAS(user_id, fecha_hora, subtotal, impuestoIVA, impuestoIVI, descuento, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO DETALLES_VENTA(venta_id, product_id, cantidad, precio_unit, total_linea) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false);

            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getUsuario().getId());
                psVenta.setTimestamp(2, Timestamp.valueOf(venta.getFechaHora()));
                psVenta.setDouble(3, venta.getSubtotal());
                psVenta.setDouble(4, venta.getImpuestoIVA()); // corregido método
                psVenta.setDouble(5, venta.getImpuestoIVI()); // corregido método
                psVenta.setDouble(6, venta.getDescuento());
                psVenta.setDouble(7, venta.getTotal());

                psVenta.executeUpdate();

                try (ResultSet generatedKeys = psVenta.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int ventaId = generatedKeys.getInt(1);

                        try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                            for (DetalleVenta det : venta.getDetalles()) {
                                psDetalle.setInt(1, ventaId);
                                psDetalle.setInt(2, det.getProducto().getId());
                                psDetalle.setInt(3, det.getCantidad());
                                psDetalle.setDouble(4, det.getPrecioUnitario()); // corregido método
                                psDetalle.setDouble(5, det.getTotalLinea());
                                psDetalle.addBatch();
                            }
                            psDetalle.executeBatch();
                        }
                    } else {
                        throw new SQLException("Error al obtener ID de venta");
                    }
                }
            }
            con.commit();
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al guardar venta", e);
            throw e;
        }
    }

    public List<Venta> getVentasDelDia(int userId) throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM VENTAS WHERE user_id = ? AND DATE(fecha_hora) = CURRENT_DATE";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario(rs.getInt("user_id"), null, null, null, true, null); // corregido constructor

                    Venta venta = new Venta(
                            rs.getInt("id"),
                            usuario,
                            rs.getTimestamp("fecha_hora").toLocalDateTime(),
                            rs.getDouble("subtotal"),
                            rs.getDouble("impuestoIVA"),
                            rs.getDouble("impuestoIVI"),
                            rs.getDouble("descuento"),
                            rs.getDouble("total"),
                            getDetallesVenta(rs.getInt("id"))
                    );

                    ventas.add(venta);
                }
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al obtener ventas del día", e);
            throw e;
        }
        return ventas;
    }

    private List<DetalleVenta> getDetallesVenta(int ventaId) throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT d.*, p.nombre_completo, p.precio_unitario, p.activo, p.creado FROM DETALLES_VENTA d JOIN PRODUCTOS p ON d.product_id = p.id WHERE d.venta_id = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ventaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                            rs.getInt("product_id"),
                            rs.getString("nombre_completo"),
                            rs.getDouble("precio_unitario"),
                            rs.getBoolean("activo"),
                            rs.getTimestamp("creado").toLocalDateTime()
                    );

                    DetalleVenta det = new DetalleVenta(
                            rs.getInt("id"),
                            null, // no necesitas la venta aquí
                            p,
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unit"),
                            rs.getDouble("total_linea")
                    );

                    detalles.add(det);
                }
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al obtener detalles de venta", e);
            throw e;
        }
        return detalles;
    }
}