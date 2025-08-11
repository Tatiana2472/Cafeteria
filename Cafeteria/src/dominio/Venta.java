package dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una venta (cabecera) con sus valores calculados:
 * subtotal, impuestoIVA (7%), impuestoIVI (13%), descuento y total.
 */
public class Venta {
    private int id;
    private Usuario usuario;
    private LocalDateTime fechaHora;
    private double subtotal;
    private double impuestoIVA;   // IVA 7%
    private double impuestoIVI;   // IVI 13%
    private double descuento;
    private double total;
    private List<DetalleVenta> detalles;

    // Constructor completo (sin id para creación, con id para lectura)
    public Venta(Usuario usuario) {
        this.usuario = usuario;
        this.fechaHora = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }

    public Venta(int id, Usuario usuario, LocalDateTime fechaHora, double subtotal, double impuestoIva,
                 double impuestoIvi, double descuento, double total, List<DetalleVenta> detalles) {
        this.id = id;
        this.usuario = usuario;
        this.fechaHora = fechaHora;
        this.subtotal = subtotal;
        this.impuestoIVA = impuestoIva;
        this.impuestoIVI = impuestoIvi;
        this.descuento = descuento;
        this.total = total;
        this.detalles = detalles;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuestoIVA() {
        return impuestoIVA;
    }

    public void setImpuestoIVA(double impuestoIVA) {
        this.impuestoIVA = impuestoIVA;
    }

    public double getImpuestoIVI() {
        return impuestoIVI;
    }

    public void setImpuestoIVI(double impuestoIVI) {
        this.impuestoIVI = impuestoIVI;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    // Métodos funcionales

    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
    }

    public void calcularTotales() {
        subtotal = detalles.stream().mapToDouble(DetalleVenta::getTotalLinea).sum();
        impuestoIVA = subtotal * 0.07;
        impuestoIVI = subtotal * 0.13;
        // Descuento ya debe estar seteado previamente si aplica
        total = subtotal + impuestoIVA + impuestoIVI - descuento;
    }
}
    