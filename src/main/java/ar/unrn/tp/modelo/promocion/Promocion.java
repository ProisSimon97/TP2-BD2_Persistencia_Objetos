package ar.unrn.tp.modelo.promocion;

import ar.unrn.tp.modelo.tarjeta.Tarjeta;

public interface Promocion {
    double calcularDescuento(double monto, Tarjeta tarjeta);
    double calcularDescuento(double monto, String marca);
}
