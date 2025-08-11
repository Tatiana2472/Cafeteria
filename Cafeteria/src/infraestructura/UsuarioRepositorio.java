package infraestructura;


import dominio.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorio {  
    
   public Usuario buscarPorUsername(String username) throws SQLException {
        // Corrijo el orden de parámetros y el SQL para que el ? de activo sea boolean y esté primero
        String sql = "SELECT * FROM usuarios WHERE activo = ? AND username = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, true);          // activo = true
            ps.setString(2, username);       // username

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol"),
                        rs.getBoolean("activo"),
                        rs.getTimestamp("creado").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al buscar usuario por username", e);
            throw e;
        }
        return null;
    }

    public List<Usuario> listarUsuarios(boolean soloActivos) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        if (soloActivos) {
            sql += " WHERE activo = true";
        }

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("rol"),
                    rs.getBoolean("activo"),
                    rs.getTimestamp("creado").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al listar usuarios", e);
            throw e;
        }
        return usuarios;
    }

    public void crearUsuario(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios(username, password, rol, activo, creado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getRol());
            ps.setBoolean(4, u.isActivo());
            ps.setTimestamp(5, Timestamp.valueOf(u.getCreado()));

            ps.executeUpdate();
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al crear usuario", e);
            throw e;
        }
    }

    public void actualizarUsuario(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET password = ?, rol = ?, activo = ? WHERE username = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getPasswordHash());
            ps.setString(2, u.getRol());
            ps.setBoolean(3, u.isActivo());
            ps.setString(4, u.getUsername());

            ps.executeUpdate();
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al actualizar usuario", e);
            throw e;
        }
    }

    public boolean estaBloqueado(String username) throws SQLException {
        String sql = "SELECT bloqueado FROM usuarios WHERE username = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("bloqueado");
                }
            }
        } catch (SQLException e) {
            ConexionBD.registrarError("Error al verificar si usuario está bloqueado", e);
            throw e;
        }
        return false; // Si no existe usuario, asumimos que no está bloqueado
    }
}
