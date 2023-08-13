package ar.unrn.tp.modelo;

import ar.unrn.tp.modelo.promocion.PromocionProducto;
import ar.unrn.tp.modelo.promocion.PromocionVenta;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private Cliente cliente;
    private List<Producto> productos;
    public Carrito(Cliente cliente) {
        this.cliente = cliente;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(Producto.crearProducto(producto));
    }

    public double calcularMontoTotalConDescuento(PromocionProducto promocionProducto, PromocionVenta promocionVenta, Tarjeta tarjeta) {
        double total = 0;

        for (Producto producto : productos) {
            total += promocionProducto.calcularDescuento(producto.getPrecio(), producto.getMarca());
        }
        return promocionVenta.calcularDescuento(total, tarjeta);
    }

    public Venta realizarPago(PromocionProducto promocionProducto, PromocionVenta promocionVenta, Tarjeta tarjeta) {

        double totalCompra = calcularMontoTotalConDescuento(promocionProducto, promocionVenta, tarjeta);

        try {

            tarjeta.realizarCompra(totalCompra);
            return new Venta(LocalDateTime.now(), this.cliente, productos, totalCompra);

        } catch(RuntimeException e) {
            throw e;
        }
    }
}