/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dominio.Venta;
import infraestructura.VentaRepositorio;
import java.util.List;
/**
 *
 * @author tatia
 */
public class VentaService {
    private VentaRepositorio repo = new VentaRepositorio();

    public void registrarVenta(Venta venta) {
        repo.guardar(venta);
    }

    public List<Venta> getVentasDelDia(int userId) {
        return repo.listarVentasDelDia(userId);
    }

    public List<Object[]> getReporteProductosVendidos() {
        return repo.reporteProductosVendidos();
    }
}

