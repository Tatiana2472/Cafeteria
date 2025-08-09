/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dominio.Usuario;
import infraestructura.ConexionBD;
import infraestructura.UsuarioRepositorio;
import utilidades.Encriptador;
/**
 *
 * @author tatia
 */
public class AuthService {
    private UsuarioRepositorio repo = new UsuarioRepositorio();

    public Usuario login(String username, String password) {
        try {
            Usuario usuario = repo.findByUsername(username);
            if (usuario != null) {
                String hashedPassword = Encriptador.hashSHA256(password);
                if (hashedPassword.equals(usuario.getPasswordHash())) {
                    ConexionBD.registrarError("Login exitoso", new Exception("Usuario: " + username));
                    return usuario;
                }
            }
            ConexionBD.registrarError("Intento de login fallido", new Exception("Usuario: " + username));
            return null;
        } catch (Exception e) {
            ConexionBD.registrarError("Error en autenticación", e);
            return null;
        }
    }
}
