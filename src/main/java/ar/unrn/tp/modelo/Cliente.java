package ar.unrn.tp.modelo;

import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cliente {
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private List<Tarjeta> tarjetas;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);

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
}