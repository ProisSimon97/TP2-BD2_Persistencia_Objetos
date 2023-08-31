package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private EntityManagerFactory emf;
    private Categoria categoria;
    private Categoria categoria2;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(UNIT_NAME);

        categoria = new Categoria("Deportes");
        categoria2 = new Categoria("Ropa de cama");


        inTransactionExecute((em) -> {
            em.persist(categoria);
            em.persist(categoria2);
        });

        productoService = new ProductoServiceJPA(emf);
        productoService.crearProducto("777", "Producto de prueba", 55, "Acme", 1L);
        productoService.crearProducto("888", "Producto de prueba 2", 75, "Raft", 1L);
    }

    @Test
    public void Should_BeTrue_When_AProductIsCreated() {
        inTransactionExecute((em) -> {
            Producto producto = em.find(Producto.class, 3L);

            assertTrue(producto.esCodigo("777"));
            assertTrue(producto.esDescripcion("Producto de prueba"));
            assertTrue(producto.esCategoria(categoria));
            assertTrue(producto.esMarca("Acme"));
        });
    }

    @Test
    public void Should_ThrowRuntimeException_When_CategoryIsInvalid() {
     RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            productoService.crearProducto("777", "Producto de prueba", 55, "Acme", 55L);
        });

        Assertions.assertTrue(exception.getMessage().contains("La categoria solicitada no existe"));
    }

    @Test
    public void Should_BeTrue_When_AProductIsModified() {
       productoService.modificarProducto(3L, "555", "Prueba modificada", 60, 2L);

        inTransactionExecute((em) -> {
            Producto producto = em.find(Producto.class, 3L);

            assertTrue(producto.esCodigo("555"));
            assertTrue(producto.esDescripcion("Prueba modificada"));
            assertTrue(producto.esCategoria(categoria2));
            assertTrue(producto.esMarca("Acme"));
        });
    }

    @Test
    public void Should_ThrowRuntimeException_When_ProductIsInvalid() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            productoService.modificarProducto(312L, "555", "Prueba modificada", 60, 1L);
        });

        Assertions.assertTrue(exception.getMessage().contains("El producto solicitado no existe"));
    }

    @Test
    public void Should_BeTrue_When_ProductListIsNotEmpty() {
       inTransactionExecute(
                (em) -> {
                    List<Tarjeta> productos = productoService.listarProductos();
                    assertTrue(!productos.isEmpty());
                }
        );
    }

    public void inTransactionExecute(Consumer<EntityManager> bloqueDeCodigo) {
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

    @AfterEach
    public void tearDown() {
        emf.close();
    }
}
