package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDate;

public class PromocionProducto extends Promocion {

    private Marca marca;

    public PromocionProducto(LocalDate fechaInicio, LocalDate fechaFin, double descuento, Marca marca) {
        super(fechaInicio, fechaFin, descuento);
        this.marca = marca;
    }

    @Override
    public double calcularDescuento(Producto producto) {
        LocalDate fechaActual = LocalDate.now();

        if(producto.esMarca(this.marca)) {
            if (fechaActual.isAfter(fechaInicio) && fechaActual.isBefore(fechaFin)) {
                return this.descuento;
            }
        }

        return 0.0;
    }

    @Override
    public double calcularDescuento(Tarjeta tarjeta) {
        return 0;
    }
}