package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.modelo.Carrito;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.Venta;
import ar.unrn.tp.modelo.promocion.PromocionCompra;
import ar.unrn.tp.modelo.promocion.PromocionProducto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class VentaServiceJPA implements VentaService {

    private String unit;

    public VentaServiceJPA(String unit) {
        this.unit = unit;
    }

    @Override
    public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Cliente cliente = em.find(Cliente.class, idCliente);
            if (cliente == null)
                throw new RuntimeException("No existe el cliente solicitado");

            Tarjeta tarjeta = em.find(Tarjeta.class, idTarjeta);
            if (tarjeta == null)
                throw new RuntimeException("No existe la tarjeta solicitada");

            if (!cliente.miTarjeta(tarjeta))
                throw new RuntimeException("La tarjeta no coincide con el regristro de tarjetas del cliente");

            if(productos.isEmpty()) {
                throw new RuntimeException("No hay productos para esta lista");
            }

            TypedQuery<Producto> queryProductos = em.createQuery("select p from Producto p where p.id in :productos", Producto.class);
            queryProductos.setParameter("productos", productos);
            List<Producto> productosBd = queryProductos.getResultList();

            TypedQuery<PromocionProducto> queryPromocionesProducto = em.createQuery("select p from PromocionProducto p where :now between p.fechaInicio and p.fechaFin", PromocionProducto.class);
            queryPromocionesProducto.setParameter("now", LocalDate.now());
            List<PromocionProducto> promocionesProducto = queryPromocionesProducto.getResultList();

            TypedQuery<PromocionCompra> queryPromocionesCompra = em.createQuery("select p from PromocionCompra p where :now between p.fechaInicio and p.fechaFin", PromocionCompra.class);
            queryPromocionesCompra.setParameter("now", LocalDate.now());
            PromocionCompra promocionCompra = queryPromocionesCompra.getSingleResult();

            Carrito carrito = new Carrito(cliente);

            carrito.agregarProductos(productosBd);

            Venta venta = carrito.realizarCompra(promocionesProducto, promocionCompra, tarjeta);

            em.persist(venta);

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
    public float calcularMonto(List<Long> productos, Long idTarjeta) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();

        try {
            if(productos.isEmpty()) {
                throw new RuntimeException("No hay productos para esta lista");
            }

            Tarjeta tarjeta = em.find(Tarjeta.class, idTarjeta);

            if(tarjeta == null) {
                throw new RuntimeException("La tarjeta solicitada no existe");
            }

            TypedQuery<Producto> queryProductos = em.createQuery("select p from Producto p where p.id in :productos", Producto.class);
            queryProductos.setParameter("productos", productos);
            List<Producto> productosBd = queryProductos.getResultList();

            TypedQuery<PromocionProducto> queryPromocionesProducto = em.createQuery("select p from PromocionProducto p where :now between p.fechaInicio and p.fechaFin",
                    PromocionProducto.class);
            queryPromocionesProducto.setParameter("now", LocalDate.now());
            List<PromocionProducto> promocionesProducto = queryPromocionesProducto.getResultList();

            TypedQuery<PromocionCompra> queryPromocionesCompra = em.createQuery("select p from PromocionCompra p where :now between p.fechaInicio and p.fechaFin",
                    PromocionCompra.class);
            queryPromocionesCompra.setParameter("now", LocalDate.now());
            PromocionCompra promocionCompra = queryPromocionesCompra.getSingleResult();

            Carrito carrito = new Carrito();

            productosBd.forEach(p -> {
                carrito.agregarProducto(p);
            });

            return (float) carrito.calcularMontoTotalConDescuento(promocionesProducto, promocionCompra, tarjeta);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @Override
    public List ventas() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Venta> ventas = em.createQuery("select v from Venta v", Venta.class);
            return ventas.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
