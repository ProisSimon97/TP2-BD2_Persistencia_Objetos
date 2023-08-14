package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDateTime;

public class PromocionCompra extends Promocion {

    private Tarjeta tarjeta;
    public PromocionCompra(LocalDateTime fechaInicio, LocalDateTime fechaFin, double descuento, Tarjeta tarjeta) {
        super(fechaInicio, fechaFin, descuento);
        this.tarjeta = tarjeta;
    }

    @Override
    public double calcularDescuento(Tarjeta tarjeta) {
        LocalDateTime fechaActual = LocalDateTime.now();

        if(tarjeta.aplica(this.tarjeta)) {
            if (fechaActual.isAfter(fechaInicio) && fechaActual.isBefore(fechaFin) && this.tarjeta.aplica(tarjeta)) {
                return this.descuento;
            }
        }

        return 0.0;
    }

    @Override
    public double calcularDescuento(Producto producto) {
        return 0;
    }
}