/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infraestructura;

import dominio.Producto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tatia
 */
public class ProductoRepositorio {
   public List<Producto> listarProductos(boolean soloActivos) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTOS";
        if (soloActivos) {
            sql += " WHERE activo = 1";
        }

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre_completo"),
                        rs.getDouble("precio_unitario"),
                        rs.getBoolean("activo"),
                        rs.getTimestamp("creado").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al listar productos", e);
            throw e;
        }

        return productos;
    }

    public void crearProducto(Producto p) throws SQLException {
        String sql = "INSERT INTO PRODUCTOS(nombre_completo, precio_unitario, activo, creado) VALUES (?, ?, 1, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombreCompleto());
            ps.setDouble(2, p.getPrecioUnitario());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            ps.executeUpdate();
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al crear producto", e);
            throw e;
        }
    }

    public void actualizarProducto(Producto p) throws SQLException {
        String sql = "UPDATE PRODUCTOS SET nombre_completo = ?, precio_unitario = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombreCompleto());
            ps.setDouble(2, p.getPrecioUnitario());
            ps.setInt(3, p.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al actualizar producto", e);
            throw e;
        }
    }

    public void activarProducto(int id) throws SQLException {
        cambiarEstadoProducto(id, true);
    }

    public void inactivarProducto(int id) throws SQLException {
        cambiarEstadoProducto(id, false);
    }

    private void cambiarEstadoProducto(int id, boolean activo) throws SQLException {
        String sql = "UPDATE PRODUCTOS SET activo = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setInt(2, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al cambiar estado producto", e);
            throw e;
        }
    }

    public Producto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM PRODUCTOS WHERE id = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre_completo"),
                            rs.getDouble("precio_unitario"),
                            rs.getBoolean("activo"),
                            rs.getTimestamp("creado").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al buscar producto por ID", e);
            throw e;
        }
        return null;
    }
}