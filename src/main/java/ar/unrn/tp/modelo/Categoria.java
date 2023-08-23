package ar.unrn.tp.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Categoria {
    @Id
    @GeneratedValue
    private Long id;
    private String tipo;

    protected Categoria() { }

    public Categoria(String tipo) {
        this.tipo = tipo;
    }

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private String getTipo() {
        return tipo;
    }

    private void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
