package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.modelo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VentaServiceTest {

    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static VentaService ventaService;
    private static ClienteService clienteService;
    private static DescuentoService descuentoService;
    private static ProductoService productoService;

    public void setUp() {

        clienteService = new ClienteServiceJPA(UNIT_NAME);
        productoService = new ProductoServiceJPA(UNIT_NAME);
        descuentoService = new DescuentoServiceJPA(UNIT_NAME);

        inTransactionExecute((em) -> {
            Categoria categoria = new Categoria("Ropa deportiva");
            em.persist(categoria);
        });

        clienteService.crearCliente("Simon", "Preuss", "39870345", "simon@gmail.com");

        productoService.crearProducto("777", "Remera Crossfit", 15000, "Adudas", 1L);
        productoService.crearProducto("555", "Pantalon corto", 11000, "Adudas", 1L);

        clienteService.agregarTarjeta(2L, "34595465465454", "Visa", 150000);

        descuentoService.crearDescuentoSobreTotal("Visa", LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 0.8);
        descuentoService.crearDescuento("Adudas", LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 0.5);
    }

    @Test
    public void Should_BeTrue_When_ASaleIsMade() {
        setUp();

        productoService = new ProductoServiceJPA(UNIT_NAME);
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.id());
        });
        
        ventaService = new VentaServiceJPA(UNIT_NAME);
        ventaService.realizarVenta(2L, idsProductos, 5L);

        inTransactionExecute((em) -> {
            Venta venta = em.find(Venta.class, 8L);
            assertNotNull(venta);
        });
    }

    @Test
    public void Should_ThrowRuntimeException_When_ASaleIsMadeWithoutAValidClient() {
        setUp();

        productoService = new ProductoServiceJPA(UNIT_NAME);
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.id());
        });

        ventaService = new VentaServiceJPA(UNIT_NAME);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.realizarVenta(65L, idsProductos, 5L);
        });

        Assertions.assertTrue(exception.getMessage().contains("No existe el cliente solicitado"));
    }

    @Test
    public void Should_ThrowRuntimeException_When_ASaleIsMadeWithoutAValidCard() {
        setUp();

        productoService = new ProductoServiceJPA(UNIT_NAME);
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.id());
        });

        ventaService = new VentaServiceJPA(UNIT_NAME);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.realizarVenta(2L, idsProductos, 15L);
        });

        Assertions.assertTrue(exception.getMessage().contains("No existe la tarjeta solicitada"));
    }

    @Test
    public void Should_ThrowRuntimeException_When_ASaleIsMadeWithoutAProductList() {
        setUp();

        List<Long> idsProductos = new ArrayList<>();

        ventaService = new VentaServiceJPA(UNIT_NAME);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.realizarVenta(2L, idsProductos, 5L);
        });

        Assertions.assertTrue(exception.getMessage().contains("No hay productos para esta lista"));
    }

    @Test
    public void Should_BeTrue_When_TryCalculateAmount() {
        setUp();

        productoService = new ProductoServiceJPA(UNIT_NAME);
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.id());
        });

        ventaService = new VentaServiceJPA(UNIT_NAME);

        Assertions.assertEquals(ventaService.calcularMonto(idsProductos, 5L), 5200);
    }

    @Test
    public void Should_ThrowRuntimeException_When_TryCalculateAmountWithoutAProductList() {
        setUp();

        List<Long> idsProductos = new ArrayList<>();

        ventaService = new VentaServiceJPA(UNIT_NAME);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.calcularMonto(idsProductos, 5L);
        });

        Assertions.assertTrue(exception.getMessage().contains("No hay productos para esta lista"));
    }

    @Test
    public void Should_ThrowRuntimeException_When_TryCalculateAmountWithoutAValidCard() {
        setUp();

        productoService = new ProductoServiceJPA(UNIT_NAME);
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.id());
        });

        ventaService = new VentaServiceJPA(UNIT_NAME);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.calcularMonto(idsProductos, 15L);
        });

        Assertions.assertTrue(exception.getMessage().contains("La tarjeta solicitada no existe"));
    }

    @Test
    public void Should_BeTrue_When_SalesListIsNotEmpty() {
        setUp();

        productoService = new ProductoServiceJPA(UNIT_NAME);
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.id());
        });

        ventaService = new VentaServiceJPA(UNIT_NAME);
        ventaService.realizarVenta(2L, idsProductos, 5L);

        List<Venta> ventas = ventaService.ventas();

        assertTrue(!ventas.isEmpty());
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
