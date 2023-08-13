package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDateTime;

public class PromocionProducto implements Promocion {
    private Marca marca;
    private double descuento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public PromocionProducto(Marca marca, double descuento, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.marca = marca;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public double calcularDescuento(double monto, Tarjeta tarjeta) {
        return 0;
    }

    @Override
    public double calcularDescuento(double monto, String marca) {
        LocalDateTime fechaActual = LocalDateTime.now();

        if (fechaActual.isAfter(fechaInicio) && fechaActual.isBefore(fechaFin) && aplica(marca)) {
            return monto * descuento / 100.0;
        }

        return monto;
    }

    public boolean aplica(String marca) {
        if(this.getMarca().equals(marca))
            return true;

        return false;
    }

    private String getMarca() {
        return this.marca.getTipo();
    }
}