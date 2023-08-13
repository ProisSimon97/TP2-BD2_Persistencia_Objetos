package ar.unrn.tp.modelo.promocion;

import java.time.LocalDateTime;

public abstract class Promocion<T> {
    protected LocalDateTime fechaInicio;
    protected LocalDateTime fechaFin;
    protected double descuento;

    public Promocion(LocalDateTime fechaInicio, LocalDateTime fechaFin, double descuento) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descuento = descuento;
    }

    public abstract double calcularDescuento(double monto, T objeto);
}