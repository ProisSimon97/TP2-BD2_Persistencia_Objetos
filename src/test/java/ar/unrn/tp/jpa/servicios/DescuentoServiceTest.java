package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.promocion.PromocionCompra;
import ar.unrn.tp.modelo.promocion.PromocionProducto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

public class DescuentoServiceTest {

    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static DescuentoService descuentoService;

    @Test
    public void Should_BeTrue_When_AProductDiscountIsCreated() {

        LocalDate fechaInicio = LocalDate.now().minusDays(2);
        LocalDate fechaFin = LocalDate.now().plusDays(2);
        Marca marca = new Marca("Acme");

        descuentoService = new DescuentoServiceJPA(UNIT_NAME);
        descuentoService.crearDescuento("Acme", fechaInicio, fechaFin, 0.5);

        inTransactionExecute((em) -> {

            PromocionProducto promocionProducto = em.find(PromocionProducto.class, 1L);
            assertTrue(promocionProducto.esFecha(fechaInicio, fechaFin));
            assertTrue(promocionProducto.esMarca(marca.getTipo()));
            assertTrue(promocionProducto.esDescuento(0.5));
        });
    }

    @Test
    public void Should_BeTrue_When_ASaleDiscountIsCreated() {

        LocalDate fechaInicio = LocalDate.now().minusDays(2);
        LocalDate fechaFin = LocalDate.now().plusDays(2);
        Tarjeta tarjeta = new Tarjeta(1500, "Ibiza", "759958992654");

        inTransactionExecute((em) -> {
            em.persist(tarjeta);
        });

        descuentoService = new DescuentoServiceJPA(UNIT_NAME);
        descuentoService.crearDescuentoSobreTotal("Ibiza", fechaInicio, fechaFin, 0.8);

        inTransactionExecute((em) -> {

            PromocionCompra promocionCompra = em.find(PromocionCompra.class, 1L);
            assertTrue(promocionCompra.esFecha(fechaInicio, fechaFin));
            assertTrue(promocionCompra.esTarjeta(tarjeta));
            assertTrue(promocionCompra.esDescuento(0.8));
        });
    }

    public void inTransactionExecute(Consumer<EntityManager> bloqueDeCodigo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT_NAME);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            bloqueDeCodigo.accept(em);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
