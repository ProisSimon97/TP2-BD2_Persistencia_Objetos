package ar.unrn.tp.modelo.tarjeta;

public interface Tarjeta {
    boolean estaActiva();
    double fondos();
    void realizarCompra(double monto);
    boolean aplica(String medioPago);
}
