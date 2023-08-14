package ar.unrn.tp.modelo.tarjeta;

public class Tarjeta {
    private String numeroTarjeta;
    private String nombre;
    private boolean activa;
    private double fondosDisponibles;

    public Tarjeta(double fondosDisponibles) {
        this.activa = true;
        this.fondosDisponibles = fondosDisponibles;
    }

    public boolean estaActiva() {
        return this.activa;
    }

    public double fondos() {
        return this.fondosDisponibles;
    }

    public boolean aplica(Tarjeta tarjeta) {
        return this.equals(tarjeta);
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