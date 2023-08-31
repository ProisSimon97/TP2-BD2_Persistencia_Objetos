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
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.promocion.PromocionCompra;
import ar.unrn.tp.modelo.promocion.PromocionProducto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;
import com.objectdb.o.UNI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class Main {
    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static ClienteService clienteService;
    private static DescuentoService descuentoService;
    private static ProductoService productoService;
    private static VentaService ventaService;

    public static void main(String[] args) {

        clienteService = new ClienteServiceJPA(UNIT_NAME);
        descuentoService = new DescuentoServiceJPA(UNIT_NAME);
        productoService = new ProductoServiceJPA(UNIT_NAME);
        ventaService = new VentaServiceJPA(UNIT_NAME);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT_NAME);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        Cliente cliente = new Cliente("Simon", "Preuss", "39870345", "simon@gmail.com");
        Tarjeta tarjeta = new Tarjeta(1500000, "Visa", "34595465465454");
        cliente.agregarTarjeta(tarjeta);

        Marca marca = new Marca("Adudas");
        Categoria categoria = new Categoria("Ropa deportiva");
        Producto producto1 = new Producto("777", "Remera crossfit", categoria, 15000, marca);
        Producto producto2 = new Producto("555", "Pantalon largo", categoria, 15000, marca);

        PromocionProducto promocionProducto = new PromocionProducto(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 0.5, marca);
        PromocionCompra promocionCompra = new PromocionCompra(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 0.8, tarjeta);

        try {
            tx.begin();


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen())
                em.close();
            if (emf != null)
                emf.close();
        }
    }
}
