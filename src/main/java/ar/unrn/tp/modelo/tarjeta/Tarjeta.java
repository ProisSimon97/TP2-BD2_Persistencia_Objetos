package ar.unrn.tp.modelo.tarjeta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tarjeta {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "numero_tarjeta")
    private String numeroTarjeta;
    private String nombre;
    private boolean activa;
    @Column(name = "fondos_disponibles")
    private double fondosDisponibles;

    protected Tarjeta() { }
    public Tarjeta(double fondosDisponibles, String nombre, String numeroTarjeta) {
        this.activa = true;
        this.numeroTarjeta = numeroTarjeta;
        this.nombre = nombre;
        this.fondosDisponibles = fondosDisponibles;
    }

    public Tarjeta(String nombre) {
        this.activa = true;
        this.nombre = nombre;
    }

    public boolean estaActiva() {
        return this.activa;
    }

    public double fondos() {
        return this.fondosDisponibles;
    }

    public boolean aplica(Tarjeta tarjeta) {
        return this.nombre.equals(tarjeta.getNombre());
    }

    public void realizarPago(double monto) {
        if (!estaActiva()) {
            throw new RuntimeException("La tarjeta no está activa");
        }

        if (monto > fondos()) {
            throw new RuntimeException("Fondos insuficientes en la tarjeta");
        }

        this.fondosDisponibles -= monto;
    }
    private String getNombre() {
        return nombre;
    }

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    private void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActiva() {
        return activa;
    }

    private void setActiva(boolean activa) {
        this.activa = activa;
    }

    private double getFondosDisponibles() {
        return fondosDisponibles;
    }

    private void setFondosDisponibles(double fondosDisponibles) {
        this.fondosDisponibles = fondosDisponibles;
    }
}