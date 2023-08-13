package ar.unrn.tp.modelo.tarjeta;

import ar.unrn.tp.modelo.tarjeta.Tarjeta;

public class TarjetaCredito implements Tarjeta {
    private String numeroTarjeta;
    private String nombre;
    private boolean activa;
    private double fondosDisponibles;

    public TarjetaCredito(double fondosDisponibles) {
        this.activa = true;
        this.fondosDisponibles = fondosDisponibles;
    }

    @Override
    public boolean estaActiva() {
        return this.activa;
    }

    @Override
    public double fondos() {
        return this.fondosDisponibles;
    }

    public boolean aplica(Tarjeta tarjeta) {
        return this.nombre.equals(tarjeta.getNombre());
    }
    @Override
    public void realizarPago(double monto) {
        if (!estaActiva()) {
            throw new RuntimeException("La tarjeta no está activa");
        }

        if (monto > fondos()) {
            throw new RuntimeException("Fondos insuficientes en la tarjeta");
        }

        this.fondosDisponibles -= monto;
    }
    @Override
    public String getNombre() {
        return nombre;
    }
}