/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import dominio.Producto;
import infraestructura.ConexionBD;
import infraestructura.ProductoRepositorio;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author tatia
 */
public class ProductoService {
    private ProductoRepositorio repo;

    public ProductoService() {
        repo = new ProductoRepositorio();
    }

    public List<Producto> getProductosActivos() throws SQLException {
        return repo.listarProductos(true);
    }

    public List<Producto> getTodosProductos() throws SQLException {
        return repo.listarProductos(false);
    }

    public void crearProducto(Producto p) throws SQLException {
        repo.crearProducto(p);
    }

    public void actualizarProducto(Producto p) throws SQLException {
        repo.actualizarProducto(p);
    }

    public void activarProducto(int id) throws SQLException {
        repo.activarProducto(id);
    }

    public void inactivarProducto(int id) throws SQLException {
        repo.inactivarProducto(id);
    }

    public Producto findById(int id) throws SQLException {
        return repo.buscarPorId(id);
    }
}

