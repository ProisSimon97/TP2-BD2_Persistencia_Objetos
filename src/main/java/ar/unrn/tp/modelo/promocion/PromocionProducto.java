package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class PromocionProducto extends Promocion {

    private static final double SIN_DESCUENTO = 0.0;
    @Embedded
    private Marca marca;

    protected PromocionProducto() { }

    public PromocionProducto(LocalDate fechaInicio, LocalDate fechaFin, double descuento, Marca marca) {
        super(fechaInicio, fechaFin, descuento);
        this.marca = marca;
    }

    @Override
    public double calcularDescuento(Producto producto) {
        if(aplicaDescuento(producto)) {
            return this.descuento;
        }

        return SIN_DESCUENTO;
    }

    private boolean aplicaDescuento(Producto producto) {
        LocalDate fechaActual = LocalDate.now();

        if(producto.esMarca(this.marca)) {
            if (fechaActual.isAfter(fechaInicio) && fechaActual.isBefore(fechaFin)) {
                return true;
            }
        }

        return false;
    }

    public boolean esMarca(String marca) { return this.marca.getTipo().equals(marca); }

    @Override
    public double calcularDescuento(Tarjeta tarjeta) {
        return 0;
    }
}