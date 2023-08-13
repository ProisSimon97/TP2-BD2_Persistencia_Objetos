package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.tarjeta.Tarjeta;
import ar.unrn.tp.modelo.tarjeta.TarjetaCredito;

import java.time.LocalDateTime;

public class PromocionCompra extends Promocion<TarjetaCredito> {

    private Tarjeta tarjeta;
    public PromocionCompra(LocalDateTime fechaInicio, LocalDateTime fechaFin, double descuento, Tarjeta tarjeta) {
        super(fechaInicio, fechaFin, descuento);
        this.tarjeta = tarjeta;
    }

    @Override
    public double calcularDescuento(double monto, TarjetaCredito tarjeta) {
        LocalDateTime fechaActual = LocalDateTime.now();

        if (fechaActual.isAfter(fechaInicio) && fechaActual.isBefore(fechaFin) && this.tarjeta.aplica(tarjeta)) {
            return monto * descuento / 100.0;
        }

        return monto;
    }
}