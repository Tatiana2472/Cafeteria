package dominio;

/**
 * Representa una línea (detalle) de venta.
 * totalLinea = precioUnit * cantidad (se ajusta en set)
 */
public class DetalleVenta {
    private int id;
    private Venta venta;
    private Producto producto;
    private int cantidad;
    private double precioUnit;
    private double totalLinea;
    private double precioUnitario;

    public DetalleVenta() {}

    public DetalleVenta(int id, Venta venta, Producto producto, int cantidad, double precioUnit, double totalLinea) {
        this.id = id;
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnit = precioUnit;
        this.totalLinea = totalLinea;
    }
    
    public DetalleVenta(Venta venta, Producto producto, int cantidad, double precioUnitario) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.totalLinea = cantidad * precioUnitario;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
    }

    public double getTotalLinea() {
        return totalLinea;
    }

    public void setTotalLinea(double totalLinea) {
        this.totalLinea = totalLinea;
    }
}