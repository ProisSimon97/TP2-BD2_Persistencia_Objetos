package ar.unrn.tp.modelo;

import ar.unrn.tp.modelo.tarjeta.Tarjeta;

public class Producto {
    private String codigo;
    private String descripcion;
    private Categoria categoria;
    private double precio;
    private Marca marca;

    private Producto(String codigo, String descripcion, Categoria categoria, double precio, Marca marca) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.marca = marca;
    }

    public static Producto crearProducto(Producto producto) {
        return new Producto(producto.codigo, producto.descripcion, producto.categoria, producto.precio, producto.marca);
    }

    public double getPrecio() {
        return this.precio;
    }

    public Marca getMarca() {
        return this.marca;
    }
}