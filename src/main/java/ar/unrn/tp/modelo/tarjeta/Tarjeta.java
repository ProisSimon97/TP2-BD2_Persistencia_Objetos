package ar.unrn.tp.modelo.tarjeta;

public interface Tarjeta {
    boolean estaActiva();
    double fondos();
    void realizarPago(double monto);
    boolean aplica(Tarjeta tarjeta);
    String getNombre();
}

