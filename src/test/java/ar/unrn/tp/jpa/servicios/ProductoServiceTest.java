package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

public class ProductoServiceTest {
    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static ProductoService productoService;

    @Test
    public void Should_BeTrue_When_AProductIsCreated() {
        Categoria categoria = new Categoria("Deportes");

        inTransactionExecute((em) -> {
            em.persist(categoria);
        });

        productoService = new ProductoServiceJPA(UNIT_NAME);
        productoService.crearProducto("777", "Producto de prueba", 55, "Acme", 1L);

        inTransactionExecute((em) -> {
            Producto producto = em.find(Producto.class, 1L);

            assertTrue(producto.esCodigo("777"));
            assertTrue(producto.esDescripcion("Producto de prueba"));
            assertTrue(producto.esCategoria(categoria));
            assertTrue(producto.esMarca("Acme"));
        });
    }

    @Test
    public void Should_ThrowRuntimeException_When_CategoryIsInvalid() {
        Categoria categoria = new Categoria("Deportes");

        inTransactionExecute((em) -> {
            em.persist(categoria);
        });

        productoService = new ProductoServiceJPA(UNIT_NAME);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            productoService.crearProducto("777", "Producto de prueba", 55, "Acme", 55L);
        });

        Assertions.assertTrue(exception.getMessage().contains("La categoria solicitada no existe"));
    }

    @Test
    public void Should_BeTrue_When_AProductIsModified() {
        Categoria categoria = new Categoria("Deportes");
        Categoria categoria2 = new Categoria("Ropa");

        inTransactionExecute((em) -> {
            em.persist(categoria);
            em.persist(categoria2);
        });

        productoService = new ProductoServiceJPA(UNIT_NAME);
        productoService.crearProducto("777", "Producto de prueba", 55, "Acme", 1L);
        productoService.modificarProducto(1L, "555", "Prueba modificada", 60, 2L);

        inTransactionExecute((em) -> {
            Producto producto = em.find(Producto.class, 1L);

            assertTrue(producto.esCodigo("555"));
            assertTrue(producto.esDescripcion("Prueba modificada"));
            assertTrue(producto.esCategoria(categoria2));
            assertTrue(producto.esMarca("Acme"));
        });
    }

    @Test
    public void Should_ThrowRuntimeException_When_ProductIsInvalid() {
        Categoria categoria = new Categoria("Deportes");

        inTransactionExecute((em) -> {
            em.persist(categoria);
        });

        productoService = new ProductoServiceJPA(UNIT_NAME);
        productoService.crearProducto("777", "Producto de prueba", 55, "Acme", 1L);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            productoService.modificarProducto(312L, "555", "Prueba modificada", 60, 1L);
        });

        Assertions.assertTrue(exception.getMessage().contains("El producto solicitado no existe"));
    }

    @Test
    public void Should_BeTrue_When_ProductListIsNotEmpty() {
        Categoria categoria = new Categoria("Deportes");

        inTransactionExecute((em) -> {
            em.persist(categoria);
        });

        productoService = new ProductoServiceJPA(UNIT_NAME);
        productoService.crearProducto("777", "Producto de prueba", 55, "Acme", 1L);
        productoService.crearProducto("888", "Producto de prueba 2", 75, "Raft", 1L);

        inTransactionExecute(
                (em) -> {
                    List<Tarjeta> productos = productoService.listarProductos();
                    assertTrue(!productos.isEmpty());
                }
        );
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
