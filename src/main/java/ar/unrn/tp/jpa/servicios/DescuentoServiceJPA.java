package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.promocion.PromocionCompra;
import ar.unrn.tp.modelo.promocion.PromocionProducto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class DescuentoServiceJPA implements DescuentoService {

    private String unit;

    public DescuentoServiceJPA(String unit) {
        this.unit = unit;
    }
    @Override
    public void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Tarjeta tarjeta = new Tarjeta(marcaTarjeta);
            PromocionCompra promocionCompra = new PromocionCompra(fechaDesde, fechaHasta, porcentaje, tarjeta);

            em.persist(promocionCompra);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @Override
    public void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Marca marca = new Marca(marcaProducto);
            PromocionProducto promocionProducto = new PromocionProducto(fechaDesde, fechaHasta, porcentaje, marca);

            em.persist(promocionProducto);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
