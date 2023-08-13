package ar.unrn.tp.modelo;

import ar.unrn.tp.modelo.tarjeta.TarjetaCredito;

import java.util.List;

public class Cliente {
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private List<TarjetaCredito> tarjetas;

    public Cliente(String nombre, String apellido, String dni, String email, List<TarjetaCredito> tarjetas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.tarjetas = tarjetas;
    }
}