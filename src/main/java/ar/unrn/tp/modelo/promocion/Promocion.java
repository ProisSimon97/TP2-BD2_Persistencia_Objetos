package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Promocion {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "fecha_inicio")
    protected LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    protected LocalDate fechaFin;
    protected double descuento;

    protected Promocion() { }

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