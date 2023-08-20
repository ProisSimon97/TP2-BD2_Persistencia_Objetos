package ar.unrn.tp.modelo;
import ar.unrn.tp.modelo.promocion.PromocionCompra;
import ar.unrn.tp.modelo.promocion.PromocionProducto;
import ar.unrn.tp.modelo.tarjeta.Tarjeta;
import ar.unrn.tp.modelo.Marca;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class Tests {

    @Test
    public void Should_BeEquals_When_PromoDatesAreInvalid() {

        Cliente cliente = new Cliente("Juan", "Gomez", "41012565", "juan@gmail.com");
        Tarjeta tarjetaCliente = new Tarjeta(1500000, "Z", "1");
        cliente.agregarTarjeta(tarjetaCliente);

        Marca marca = new Marca("Matel");
        Tarjeta tarjeta = new Tarjeta("Z");

        Producto p1 = new Producto("1", "Auto de juguete", new Categoria("Juguete"), 15000, marca);
        Producto p2 = new Producto("12", "Muñeco de accion", new Categoria("Juguete"), 10000, marca);

        Carrito carrito = new Carrito(cliente);

        carrito.agregarProducto(p1);
        carrito.agregarProducto(p2);

        PromocionProducto promoProducto = new PromocionProducto(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1), 0.05, marca);
        PromocionCompra promoCompra = new PromocionCompra(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1), 0.08, tarjeta);

        Assertions.assertEquals(carrito.calcularMontoTotalConDescuento(promoProducto, promoCompra, tarjetaCliente), 25000);
    }

    @Test
    public void Should_BeEquals_When_ProductPromoDatesAreValid() {

        Cliente cliente = new Cliente("Juan", "Gomez", "41012565", "juan@gmail.com");
        Tarjeta tarjetaCliente = new Tarjeta(1500000, "Z", "1");
        cliente.agregarTarjeta(tarjetaCliente);

        Marca marca = new Marca("Acme");
        Tarjeta tarjeta = new Tarjeta("Z");

        Producto p1 = new Producto("1", "Auto de juguete", new Categoria("Juguete"), 15000, marca);
        Producto p2 = new Producto("12", "Muñeco de accion", new Categoria("Juguete"), 10000, marca);

        Carrito carrito = new Carrito(cliente);

        carrito.agregarProducto(p1);
        carrito.agregarProducto(p2);

        PromocionProducto promoProducto = new PromocionProducto(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3), 0.05, marca);
        PromocionCompra promoCompra = new PromocionCompra(LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), 0.08, tarjeta);

        Assertions.assertEquals(carrito.calcularMontoTotalConDescuento(promoProducto, promoCompra, tarjetaCliente), 23750);
    }

    @Test
    public void Should_BeEquals_When_SalePromoDatesAreValid() {

        Cliente cliente = new Cliente("Juan", "Gomez", "41012565", "juan@gmail.com");
        Tarjeta tarjetaCliente = new Tarjeta(1500000, "Z", "1");
        cliente.agregarTarjeta(tarjetaCliente);

        Marca marca = new Marca("Acme");
        Tarjeta tarjeta = new Tarjeta("Z");

        Producto p1 = new Producto("1", "Auto de juguete", new Categoria("Juguete"), 15000, marca);
        Producto p2 = new Producto("12", "Muñeco de accion", new Categoria("Juguete"), 10000, marca);

        Carrito carrito = new Carrito(cliente);

        carrito.agregarProducto(p1);
        carrito.agregarProducto(p2);

        PromocionProducto promoProducto = new PromocionProducto(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1), 0.05, marca);
        PromocionCompra promoCompra = new PromocionCompra(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3), 0.08, tarjeta);

        Assertions.assertEquals(carrito.calcularMontoTotalConDescuento(promoProducto, promoCompra, tarjetaCliente), 23000);
    }

    @Test
    public void Should_BeEquals_When_SaleAndProductPromosDatesAreValid() {

        Cliente cliente = new Cliente("Juan", "Gomez", "41012565", "juan@gmail.com");
        Tarjeta tarjetaCliente = new Tarjeta(1500000, "MemeCard", "1");
        cliente.agregarTarjeta(tarjetaCliente);

        Marca marca = new Marca("Comarca");
        Tarjeta tarjeta = new Tarjeta("MemeCard");

        Producto p1 = new Producto("1", "Auto de juguete", new Categoria("Juguete"), 15000, marca);
        Producto p2 = new Producto("12", "Muñeco de accion", new Categoria("Juguete"), 10000, marca);

        Carrito carrito = new Carrito(cliente);

        carrito.agregarProducto(p1);
        carrito.agregarProducto(p2);

        PromocionProducto promoProducto = new PromocionProducto(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3), 0.05, marca);
        PromocionCompra promoCompra = new PromocionCompra(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3), 0.08, tarjeta);

        Assertions.assertEquals(carrito.calcularMontoTotalConDescuento(promoProducto, promoCompra, tarjetaCliente), 21850);
    }

    @Test
    public void Should_BeTrue_When_ASaleIsMade() {

        Cliente cliente = new Cliente("Juan", "Gomez", "41012565", "juan@gmail.com");
        Tarjeta tarjetaCliente = new Tarjeta(15000000, "MemeCard", "1");
        cliente.agregarTarjeta(tarjetaCliente);

        Marca marca = new Marca("Comarca");
        Tarjeta tarjeta = new Tarjeta("MemeCard");

        Producto p1 = new Producto("1", "Auto de juguete", new Categoria("Juguete"), 15000, marca);
        Producto p2 = new Producto("12", "Muñeco de accion", new Categoria("Juguete"), 10000, marca);

        Carrito carrito = new Carrito(cliente);

        carrito.agregarProducto(p1);
        carrito.agregarProducto(p2);

        PromocionProducto promoProducto = new PromocionProducto(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3), 0.05, marca);
        PromocionCompra promoCompra = new PromocionCompra(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3), 0.08, tarjeta);

        Tienda tienda = new Tienda();
        tienda.agregarVenta(carrito.realizarCompra(promoProducto, promoCompra, tarjetaCliente));

        Assertions.assertTrue(tienda.estadoDeVentas());
    }
    @Test
    public void Should_ThrowRuntimeException_When_ProductIsInvalid() {

        Marca marca = new Marca("Matel");

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            new Producto("1", null, new Categoria("Juguete"), 15000, marca);
        });

        Assertions.assertEquals("Los datos proporcionados no son válidos", exception.getMessage());
    }

    @Test
    public void Should_ThrowRuntimeException_When_ClientIsInvalid() {

        Cliente cliente = new Cliente("Juan", "Gomez", "41012565", "juan@gmail.com");
        Tarjeta tarjetaCliente = new Tarjeta(15000000, "MemeCard", "1");
        cliente.agregarTarjeta(tarjetaCliente);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            new Cliente("Juan", "Pereira", "39870", "juanGmail.com");
        });

        Assertions.assertEquals("Los datos proporcionados no son válidos", exception.getMessage());
    }

    @Test
    public void Should_ThrowRuntimeException_When_PromoDatesAreInvalid() {

        Marca marca = new Marca("Comarca");

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            new PromocionProducto(LocalDate.now().minusDays(3), LocalDate.now().minusDays(3), 0.05, marca);
        });

        Assertions.assertEquals("Las fechas de inicio y fin no pueden ser iguales", exception.getMessage());
    }

}
