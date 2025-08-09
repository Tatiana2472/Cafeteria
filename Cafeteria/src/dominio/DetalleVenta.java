package dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import dominio.Venta;
/**
 * Representa una línea (detalle) de venta.
 * totalLinea = precioUnit * cantidad (se ajusta en set)
 */
public class DetalleVenta {
    private int id;
    private Venta venta; // referencia a la venta (puede ser null antes de persistir)
    private Producto producto;
    private int cantidad;
    private BigDecimal precioUnit; // precio unitario al momento de la venta
    private BigDecimal totalLinea;

    public DetalleVenta() {
        this.cantidad = 0;
        this.precioUnit = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.totalLinea = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public DetalleVenta(int id, Producto producto, int cantidad, BigDecimal precioUnit) {
        this();
        this.id = id;
        this.producto = producto;
        this.setCantidad(cantidad);
        this.setPrecioUnit(precioUnit);
        recalcularTotalLinea();
    }

    public DetalleVenta(Producto producto, int cantidad, BigDecimal precioUnit) {
        this(0, producto, cantidad, precioUnit);
    }

    private void recalcularTotalLinea() {
        BigDecimal qty = BigDecimal.valueOf(Math.max(0, this.cantidad));
        BigDecimal pu = this.precioUnit != null ? this.precioUnit : BigDecimal.ZERO;
        this.totalLinea = pu.multiply(qty).setScale(2, RoundingMode.HALF_UP);
    }

    // ----- getters / setters -----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = Math.max(0, cantidad);
        recalcularTotalLinea();
        if (this.venta != null) this.venta.recalcularTotales();
    }

    public BigDecimal getPrecioUnit() { return precioUnit; }
    public void setPrecioUnit(BigDecimal precioUnit) {
        this.precioUnit = precioUnit != null ? precioUnit.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        recalcularTotalLinea();
        if (this.venta != null) this.venta.recalcularTotales();
    }

    public BigDecimal getTotalLinea() { return totalLinea; }
    public void setTotalLinea(BigDecimal totalLinea) {
        this.totalLinea = totalLinea != null ? totalLinea.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        if (this.venta != null) this.venta.recalcularTotales();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleVenta)) return false;
        DetalleVenta that = (DetalleVenta) o;
        return id != 0 && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String prod = producto != null ? producto.getNombre() : "null";
        return "DetalleVenta{id=" + id + ", producto=" + prod + ", cantidad=" + cantidad +
                ", precioUnit=" + precioUnit + ", totalLinea=" + totalLinea + "}";
    }
}
