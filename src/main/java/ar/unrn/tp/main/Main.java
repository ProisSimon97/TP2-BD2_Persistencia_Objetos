package ar.unrn.tp.main;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.jpa.servicios.ClienteServiceJPA;
import ar.unrn.tp.jpa.servicios.DescuentoServiceJPA;
import ar.unrn.tp.jpa.servicios.ProductoServiceJPA;
import ar.unrn.tp.jpa.servicios.VentaServiceJPA;
import ar.unrn.tp.modelo.Categoria;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Main {
    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static EntityManagerFactory emf;
    private static ClienteService clienteService;
    private static DescuentoService descuentoService;
    private static ProductoService productoService;
    private static VentaService ventaService;

    public static void main(String[] args) {

        emf = Persistence.createEntityManagerFactory(UNIT_NAME);

        clienteService = new ClienteServiceJPA(emf);
        descuentoService = new DescuentoServiceJPA(emf);
        productoService = new ProductoServiceJPA(emf);
        ventaService = new VentaServiceJPA(emf);

        inTransactionExecute((em) -> {
            Categoria categoria = new Categoria("Ropa deportiva");
            em.persist(categoria);
        });

        clienteService.crearCliente("Simon", "Preuss", "39870345", "simon@gmail.com");
        clienteService.agregarTarjeta(2L, "7777", "Visa", 15000);

        productoService.crearProducto("777", "Remera", 15000, "Adudas", 1L);

        descuentoService.crearDescuento("Adudas", LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 0.5);
        descuentoService.crearDescuentoSobreTotal("Visa", LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 0.8);

        List<Long> productos = new ArrayList<>();
        productos.add(4L);

        ventaService.realizarVenta(2L, productos, 3L);

        productoService.modificarProducto(4L, "555", "Modificaodo", 12500, 1L);
    }

    public static void inTransactionExecute(Consumer<EntityManager> bloqueDeCodigo) {
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
