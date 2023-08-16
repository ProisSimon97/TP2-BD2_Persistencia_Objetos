package ar.unrn.tp.modelo.tarjeta;

public class Tarjeta {
    private String numeroTarjeta;
    private String nombre;
    private boolean activa;
    private double fondosDisponibles;

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
    public String getNombre() {
        return nombre;
    }
}