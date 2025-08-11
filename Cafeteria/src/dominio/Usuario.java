/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import java.time.LocalDateTime;

/**
 *
 * @author Estudiante
 */
public class Usuario {
     private int id;
    private String username;
    private String passwordHash;
    private String rol;
    private boolean activo;
    private LocalDateTime creado;

    public Usuario() {}

    public Usuario(int id, String username, String passwordHash, String rol, boolean activo, LocalDateTime creado) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = activo;
        this.creado = creado;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreado() {
        return creado;
    }

    // Getters y setters
    public void setCreado(LocalDateTime creado) {    
        this.creado = creado;
    }

    public boolean validarPassword(String password) {
        String hash = utilidades.HashUtil.hashSHA256(password);
        return this.passwordHash.equalsIgnoreCase(hash);
    }
}