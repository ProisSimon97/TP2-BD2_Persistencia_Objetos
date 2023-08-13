package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDateTime;

public class PromocionVenta implements Promocion {
    private String medioPago;
    private double descuento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public PromocionVenta(String medioPago, double descuento, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.medioPago = medioPago;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public double calcularDescuento(double monto, Tarjeta tarjeta) {
        LocalDateTime fechaActual = LocalDateTime.now();

        if (fechaActual.isAfter(fechaInicio) && fechaActual.isBefore(fechaFin) && tarjeta.aplica(this.medioPago)) {
            return monto * descuento / 100.0;
        }

        return monto;
    }

    @Override
    public double calcularDescuento(double monto, String marca) {
        return 0;
    }
}