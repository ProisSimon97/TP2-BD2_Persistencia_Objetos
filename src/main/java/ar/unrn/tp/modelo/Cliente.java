package ar.unrn.tp.modelo;

import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Cliente {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String dni;
    private String email;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private List<Tarjeta> tarjetas;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);

    protected Cliente() { }

    public Cliente(String nombre, String apellido, String dni, String email) {
        if (dni == null || dni.isEmpty() || nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty() ||
                !isValidEmail(email)) {
            throw new RuntimeException("Los datos proporcionados no son válidos");
        }

        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.tarjetas = new ArrayList<>();
    }
    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.matches();
    }
    public void agregarTarjeta(Tarjeta tarjeta) {
        this.tarjetas.add(tarjeta);
    }
    private Long getId() {
        return id;
    }

    private String getNombre() {
        return this.nombre;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String getApellido() {
        return this.apellido;
    }

    private void setApellido(String apellido) {
        this.apellido = apellido;
    }

    private String getDni() {
        return this.dni;
    }

    private void setDni(String dni) {
        this.dni = dni;
    }

    private String getEmail() {
        return this.email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public List<Tarjeta> tarjetas() {
        return this.tarjetas;
    }

    public void nombre(String nombre) {
        this.nombre = nombre;
    }

    public void apellido(String apellido) {
        this.apellido = apellido;
    }

    public void dni(String dni) {
        this.dni = dni;
    }

    public void email(String email) {
        this.email = email;
    }
}