package dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa una venta (cabecera) con sus valores calculados:
 * subtotal, impuestoIVA (7%), impuestoIVI (13%), descuento y total.
 */
public class Venta {
    public static final BigDecimal IVA_RATE = new BigDecimal("0.07");
    public static final BigDecimal IVI_RATE = new BigDecimal("0.13");

    private int id;
    private Usuario usuario; // quien registró la venta
    private LocalDateTime fechaHora;
    private BigDecimal subtotal;
    private BigDecimal impuestoIva;
    private BigDecimal impuestoIvi;
    private BigDecimal descuento;
    private BigDecimal total;
    private List<DetalleVenta> detalles;

    public Venta() {
        this.fechaHora = LocalDateTime.now();
        this.subtotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.impuestoIva = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.impuestoIvi = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.descuento = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.detalles = new ArrayList<>();
    }

    public Venta(int id, Usuario usuario) {
        this();
        this.id = id;
        this.usuario = usuario;
    }

    // ----- operaciones sobre detalles -----
    public void addDetalle(DetalleVenta detalle) {
        if (detalle == null) return;
        detalle.setVenta(this);
        if (this.detalles == null) this.detalles = new ArrayList<>();
        this.detalles.add(detalle);
        recalcularTotales();
    }

    public void removeDetalle(DetalleVenta detalle) {
        if (detalle == null || this.detalles == null) return;
        this.detalles.remove(detalle);
        detalle.setVenta(null);
        recalcularTotales();
    }

    public void clearDetalles() {
        if (this.detalles != null) {
            for (DetalleVenta d : this.detalles) d.setVenta(null);
            this.detalles.clear();
        }
        recalcularTotales();
    }

    // recalcula subtotal, impuestos y total a partir de los detalles y posible descuento
    public void recalcularTotales() {
        BigDecimal sub = BigDecimal.ZERO;
        if (this.detalles != null) {
            for (DetalleVenta d : this.detalles) {
                BigDecimal linea = d.getTotalLinea() != null ? d.getTotalLinea() : BigDecimal.ZERO;
                sub = sub.add(linea);
            }
        }
        this.subtotal = sub.setScale(2, RoundingMode.HALF_UP);

        this.impuestoIva = this.subtotal.multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
        this.impuestoIvi = this.subtotal.multiply(IVI_RATE).setScale(2, RoundingMode.HALF_UP);

        BigDecimal desc = this.descuento != null ? this.descuento : BigDecimal.ZERO;
        BigDecimal tot = this.subtotal.add(this.impuestoIva).add(this.impuestoIvi).subtract(desc);
        this.total = tot.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    // permite fijar descuento y recalcula totales
    public void aplicarDescuento(BigDecimal descuento) {
        this.descuento = descuento != null ? descuento.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        recalcularTotales();
    }

    // ----- getters / setters -----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getImpuestoIva() { return impuestoIva; }
    public BigDecimal getImpuestoIvi() { return impuestoIvi; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento != null ? descuento.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO; recalcularTotales(); }

    public BigDecimal getTotal() { return total; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        if (this.detalles == null) this.detalles = new ArrayList<>();
        // asegurar que la referencia a la venta esté presente
        for (DetalleVenta d : this.detalles) d.setVenta(this);
        recalcularTotales();
    }

    // equals/hashCode basados en id cuando exista
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venta)) return false;
        Venta venta = (Venta) o;
        return id != 0 && id == venta.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Venta{id=" + id + ", usuario=" + (usuario != null ? usuario.getUsername() : "null") +
                ", fechaHora=" + fechaHora + ", subtotal=" + subtotal +
                ", iva=" + impuestoIva + ", ivi=" + impuestoIvi + ", descuento=" + descuento + ", total=" + total + "}";
    }
}
