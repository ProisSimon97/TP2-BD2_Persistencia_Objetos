package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.time.LocalDate;

public abstract class Promocion {
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    protected double descuento;

    public Promocion(LocalDate fechaInicio, LocalDate fechaFin, double descuento) {
        if (fechaInicio.isEqual(fechaFin)) {
            throw new RuntimeException("Las fechas de inicio y fin no pueden ser iguales");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descuento = descuento;
    }

    public abstract double calcularDescuento(Tarjeta tarjeta);
    public abstract double calcularDescuento(Producto producto);
}