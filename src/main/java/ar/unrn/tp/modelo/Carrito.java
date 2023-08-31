package ar.unrn.tp.modelo;

import ar.unrn.tp.modelo.promocion.PromocionProducto;
import ar.unrn.tp.modelo.promocion.PromocionCompra;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private Cliente cliente;
    private List<Producto> productos;
    public Carrito(Cliente cliente) {
        this.cliente = cliente;
        this.productos = new ArrayList<>();
    }

    public Carrito() {
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(Producto.crearProducto(producto));
    }

    public void agregarProductos(List<Producto> productos) {
        if (productos.isEmpty()) {
            throw new RuntimeException("La lista de productos no puede ser vacia");
        }
        this.productos.addAll(productos);
    }

    public double calcularMontoTotalConDescuento(List<PromocionProducto> promocionProducto, PromocionCompra promocionCompra, Tarjeta tarjeta) {
        double total = 0;

        for (Producto producto : productos) {

            total += producto.getPrecio();

            double descuentos = promocionProducto.stream()
                    .mapToDouble(promo -> producto.getPrecio() * promo.calcularDescuento(producto))
                    .sum();

            total -= descuentos;

        }
        return total - (total * promocionCompra.calcularDescuento(tarjeta));
    }

    public Venta realizarCompra(List <PromocionProducto> promocionProducto, PromocionCompra promocionCompra, Tarjeta tarjeta) {
        double totalCompra = calcularMontoTotalConDescuento(promocionProducto, promocionCompra, tarjeta);

        try {
            tarjeta.realizarPago(totalCompra);
            return new Venta(LocalDate.now(), this.cliente, productos, totalCompra);
        } catch(RuntimeException e) {
            throw e;
        }
    }
}