package ar.unrn.tp.modelo;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Venta {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate fecha;
    @ManyToOne
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Producto> productosComprados;
    @Column(name = "monto_total")
    private double montoTotal;

    protected Venta() { }

    public Venta(LocalDate fechaHora, Cliente cliente, List<Producto> productosComprados, double montoTotal) {
        this.fecha = fechaHora;
        this.cliente = cliente;
        this.productosComprados = productosComprados;
        this.montoTotal = montoTotal;
    }
}