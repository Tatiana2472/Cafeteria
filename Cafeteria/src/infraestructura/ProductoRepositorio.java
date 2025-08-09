/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infraestructura;

import dominio.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tatia
 */
public class ProductoRepositorio {
    public List<Producto> listarActivos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTOS WHERE activo = 1";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = new Producto(rs.getString("nombre"), rs.getDouble("precio_unitario"));
                p.setId(rs.getInt("id"));
                p.setActivo(rs.getBoolean("activo"));
                p.setCreado(rs.getTimestamp("creado").toLocalDateTime());
                productos.add(p);
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al listar productos activos", e);
        }
        return productos;
    }

    public void crear(Producto producto) {
        String sql = "INSERT INTO PRODUCTOS(nombre, precio_unitario) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioUnitario());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                producto.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al crear producto", e);
        }
    }

    public void actualizar(Producto producto) {
        String sql = "UPDATE PRODUCTOS SET nombre = ?, precio_unitario = ?, activo = ? WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioUnitario());
            pstmt.setBoolean(3, producto.isActivo());
            pstmt.setInt(4, producto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al actualizar producto", e);
        }
    }

    public Producto findById(int id) {
        String sql = "SELECT * FROM PRODUCTOS WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Producto p = new Producto(rs.getString("nombre"), rs.getDouble("precio_unitario"));
                p.setId(rs.getInt("id"));
                p.setActivo(rs.getBoolean("activo"));
                p.setCreado(rs.getTimestamp("creado").toLocalDateTime());
                return p;
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al buscar producto por ID", e);
        }
        return null;
    }
}
