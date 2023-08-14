package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDateTime;

public abstract class Promocion {
    protected LocalDateTime fechaInicio;
    protected LocalDateTime fechaFin;
    protected double descuento;

    public Promocion(LocalDateTime fechaInicio, LocalDateTime fechaFin, double descuento) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descuento = descuento;
    }

    public abstract double calcularDescuento(Tarjeta tarjeta);
    public abstract double calcularDescuento(Producto producto);
}