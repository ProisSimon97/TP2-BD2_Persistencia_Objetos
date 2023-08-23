package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;

import javax.persistence.*;
import java.util.List;

public class ClienteServiceJPA implements ClienteService {
    private String unit;

    public ClienteServiceJPA(String unit) {
        this.unit = unit;
    }

    @Override
    public void crearCliente(String nombre, String apellido, String dni, String email) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            TypedQuery<Long> query = em.createQuery("select count(c) from Cliente c where c.dni = :dni", Long.class);
            query.setParameter("dni", dni);
            Long cant = query.getSingleResult();

            if(cant > 0) {
                throw new RuntimeException("Ya existe un usuario con ese dni");
            }
            Cliente cliente = new Cliente(nombre, apellido, dni, email);
            em.persist(cliente);

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
    public void modificarCliente(Long idCliente, String nombre, String apellido, String dni, String email) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Cliente cliente = em.getReference(Cliente.class, idCliente);

            if(cliente == null) {
                throw new RuntimeException("El cliente solicitado no existe");
            }

            cliente.nombre(nombre);
            cliente.apellido(apellido);
            cliente.dni(dni);
            cliente.email(email);

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
    public void agregarTarjeta(Long idCliente, String nro, String nombre, double fondosDisponibles) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Cliente cliente = em.getReference(Cliente.class, idCliente);
            //mejor usar find() para consultar si existe?

            if(cliente != null) {
                Tarjeta tarjeta = new Tarjeta(fondosDisponibles, nombre, nro);
                cliente.agregarTarjeta(tarjeta);
            }

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
    public List listarTarjetas(Long idCliente) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(this.unit);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Cliente cliente = em.find(Cliente.class, idCliente);

            if(cliente == null) {
                throw new RuntimeException("No existe el cliente solicitado");
            }

            return cliente.tarjetas();
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
