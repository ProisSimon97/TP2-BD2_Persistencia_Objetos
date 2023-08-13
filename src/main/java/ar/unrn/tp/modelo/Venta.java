package ar.unrn.tp.modelo;

import java.time.LocalDateTime;
import java.util.List;

public class Venta {
    private LocalDateTime fechaHora;
    private Cliente cliente;
    private List<Producto> productosComprados;
    private double montoTotal;

    public Venta(LocalDateTime fechaHora, Cliente cliente, List<Producto> productosComprados, double montoTotal) {
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.productosComprados = productosComprados;
        this.montoTotal = montoTotal;
    }
}