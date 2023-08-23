package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;

import javax.persistence.*;
import java.util.List;

public class ProductoServiceJPA implements ProductoService {

    private String unit;

    public ProductoServiceJPA(String unit) {
        this.unit = unit;
    }
    @Override
    public void crearProducto(String codigo, String descripcion, double precio, String marca, Long idCategoria) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Categoria categoria = em.find(Categoria.class, idCategoria);

            if(categoria == null) {
                throw new RuntimeException("La categoria solicitada no existe");
            }

            Marca marcaProducto = new Marca(marca);
            Producto producto = new Producto(codigo, descripcion, categoria, precio, marcaProducto);
            em.persist(producto);

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
    public void modificarProducto(Long idProducto, String codigo, String descripcion, double precio, Long idCategoria) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Categoria categoria = em.find(Categoria.class, idCategoria);
            Producto producto = em.getReference(Producto.class, idProducto);

            if(producto == null) {
                throw new RuntimeException("El producto solicitado no existe");
            }

            if(categoria == null) {
                throw new RuntimeException("La categoria solicitada no existe");
            }

            producto.codigo(codigo);
            producto.descripcion(descripcion);
            producto.precio(precio);
            producto.categoria(categoria);

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
    public List listarProductos() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            TypedQuery<Producto> q = em.createQuery("select p from Producto p", Producto.class);
            return q.getResultList();

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
