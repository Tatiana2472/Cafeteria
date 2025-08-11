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
    private int userId;
    private LocalDateTime fechaHora;
    private double subtotal;
    private double impuestoIVA;
    private double impuestoIVI;
    private double descuento;
    private double total;
    private List<DetalleVenta> detalles;

   public Venta(int userId) {
        this.userId = userId;
        this.fechaHora = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.subtotal = 0;
        this.impuestoIVA = 0;
        this.impuestoIVI = 0;
        this.descuento = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public void agregarDetalle(DetalleVenta det) {
    if (det == null) {
        throw new IllegalArgumentException("El detalle no puede ser nulo");
    }
    this.detalles.add(det);

    // Recalcular valores
    recalcularTotales();
}

private void recalcularTotales() {
    subtotal = 0;
    for (DetalleVenta d : detalles) {
        subtotal += d.getTotalLinea();  // suma total de cada detalle
    }

    impuestoIVA = subtotal * 0.07;   // 7% IVA
    impuestoIVI = subtotal * 0.13;   // 13% IVI

    total = subtotal + impuestoIVA + impuestoIVI;

    if (descuento > 0) {
        total -= descuento;  // el descuento es monto fijo, según tu UI
        }
    }
}
    