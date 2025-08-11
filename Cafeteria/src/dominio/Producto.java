/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Estudiante
 */
public class Producto {
    private int id;
    private String nombreCompleto;
    private double precioUnitario;
    private boolean activo;
    private LocalDateTime creado;

    public Producto(int id, String nombreCompleto, double precioUnitario, boolean activo, LocalDateTime creado) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.precioUnitario = precioUnitario;
        this.activo = activo;
        this.creado = creado;
    }

    // Para crear nuevos productos sin id ni creado
    public Producto(String nombreCompleto, double precioUnitario) {
        this.nombreCompleto = nombreCompleto;
        this.precioUnitario = precioUnitario;
        this.activo = true;
        this.creado = LocalDateTime.now();
    }

    // Getters y setters...

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getCreado() {
        return creado;
    }
}

