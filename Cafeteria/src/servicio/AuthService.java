/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import infraestructura.UsuarioRepositorio;
import dominio.Usuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Formatter;

/**
 *
 * @author tatia
 */
public class AuthService {
     private UsuarioRepositorio usuarioRepo;

    public AuthService() {
        this.usuarioRepo = new UsuarioRepositorio();
    }

    // Método para login: devuelve boolean si usuario y pass son correctos
    public boolean login(String username, String password) throws SQLException {
        Usuario usuario = usuarioRepo.buscarPorUsername(username);
        if (usuario == null || !usuario.isActivo()) {
            return false; // No existe o está inactivo
        }

        String hashedInput = hashPassword(password);
        return hashedInput.equals(usuario.getPasswordHash());
    }

    // Nuevo método para obtener el usuario (debes llamar este solo si login es true)
    public Usuario obtenerUsuario(String username) throws SQLException {
        return usuarioRepo.buscarPorUsername(username);
    }

    // Verifica si usuario está bloqueado (implementación según repo)
    public boolean estaBloqueado(String username) {
        try {
            return usuarioRepo.estaBloqueado(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // por seguridad, bloquea si error en consulta
        }
    }

    // Método para hash SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }

    // Convierte byte[] a string hexadecimal
    private String bytesToHex(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }
}

