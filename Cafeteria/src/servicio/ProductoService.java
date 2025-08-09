/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dominio.Producto;
import infraestructura.ProductoRepositorio;
import java.util.List;

/**
 *
 * @author tatia
 */
public class ProductoService {
    private ProductoRepositorio repo = new ProductoRepositorio();

    public List<Producto> getProductosActivos() {
        return repo.listarActivos();
    }

    public void crearProducto(Producto producto) {
        repo.crear(producto);
    }

    public void actualizarProducto(Producto producto) {
        repo.actualizar(producto);
    }

    public Producto findById(int id) {
        return repo.findById(id);
    }
}

