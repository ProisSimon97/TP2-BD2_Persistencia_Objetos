package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDateTime;

public class PromocionProducto extends Promocion<Marca> {

    private Marca marca;

    public PromocionProducto(LocalDateTime fechaInicio, LocalDateTime fechaFin, double descuento, Marca marca) {
        super(fechaInicio, fechaFin, descuento);
        this.marca = marca;
    }

    @Override
    public double calcularDescuento(double monto, Marca marca) {
        LocalDateTime fechaActual = LocalDateTime.now();

        if (fechaActual.isAfter(fechaInicio) && fechaActual.isBefore(fechaFin) && aplica(marca)) {
            return monto * descuento / 100.0;
        }

        return monto;
    }

    public boolean aplica(Marca marca) {
        if(this.marca.getTipo().equals(marca.getTipo()))
            return true;

        return false;
    }
}