package infraestructura;

import dominio.Usuario;
import java.sql.*;

public class UsuarioRepositorio {

   public Usuario findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPasswordHash(rs.getString("password_hash"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setActivo(rs.getBoolean("activo"));
                    usuario.setCreado(rs.getTimestamp("creado").toLocalDateTime());
                    return usuario;
                }
            }
        }
        return null; // No encontrado
    }

    public Usuario buscarPorUsernameYPassword(String username, String passwordHash) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password_hash = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPasswordHash(rs.getString("password_hash"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setActivo(rs.getBoolean("activo"));
                    usuario.setCreado(rs.getTimestamp("creado").toLocalDateTime());
                    return usuario;
                }
            }
        }
        return null;
    }
}
