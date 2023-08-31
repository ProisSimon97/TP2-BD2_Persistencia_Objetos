package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.modelo.Cliente;
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

public class ClienteServiceTest {
    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static ClienteService clienteService;
    private EntityManagerFactory emf;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(UNIT_NAME);

        clienteService = new ClienteServiceJPA(emf);
        clienteService.crearCliente("Simon", "Preuss", "39870345", "simon@gmail.com");
    }

    @Test
    public void Should_BeTrue_When_AClientIsCreated() {
        inTransactionExecute((em) -> {
            Cliente cliente = em.find(Cliente.class, 1L);

            assertTrue(cliente.esNombre("Simon"));
            assertTrue(cliente.esApellido("Preuss"));
            assertTrue(cliente.esDni("39870345"));
            assertTrue(cliente.esEmail("simon@gmail.com"));

        });
    }

    @Test
    public void Should_BeTrue_When_AClientIsModified() {
        clienteService.modificarCliente(1L, "Saimon", "Pelotto", "39870344", "simonpreuss@gmail.com");

        inTransactionExecute(
                (em) -> {
                    Cliente cliente = em.find(Cliente.class, 1L);

                    assertTrue(cliente.esNombre("Saimon"));
                    assertTrue(cliente.esApellido("Pelotto"));
                    assertTrue(cliente.esDni("39870344"));
                    assertTrue(cliente.esEmail("simonpreuss@gmail.com"));
                }
        );
    }

    @Test
    public void Should_BeTrue_When_ACardIsAdded() {
        clienteService.agregarTarjeta(1L, "123456", "MemeCard", 150000);

        inTransactionExecute(
                (em) -> {
                    Cliente cliente = em.find(Cliente.class, 1L);
                    assertTrue(cliente.misTarjetas() > 0);
                }
        );
    }

    @Test
    public void Should_BeTrue_When_ClientCardListIsNotEmpty() {
        clienteService.agregarTarjeta(1L, "123456", "MemeCard", 150000);

        inTransactionExecute(
                (em) -> {
                    List<Tarjeta> tarjetas = clienteService.listarTarjetas(1L);
                    assertTrue(!tarjetas.isEmpty());
                }
        );
    }

    @Test
    public void Should_ThrowRuntimeException_When_ClientDontExist() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            clienteService.modificarCliente(5L,"Simo", "Preus", "39870345", "simon@gmail.com");
        });

        Assertions.assertTrue(exception.getMessage().contains("No existe el cliente solicitado"));
    }

    @Test
    public void Should_ThrowRuntimeException_When_ClientDniAlreadyExists() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            clienteService.crearCliente("Simon", "Preuss", "39870345", "simon@gmail.com");
        });

        Assertions.assertTrue(exception.getMessage().contains("Ya existe un usuario con ese dni"));
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
