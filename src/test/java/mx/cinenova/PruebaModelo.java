package mx.cinenova;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import modelo.Compra;
import modelo.ControlModeloCompra;
import modelo.FachadaModeloCompra;

/** Pruebas autocontenidas sin JUnit; ejecutar con java mx.cinenova.PruebaModelo. */
public class PruebaModelo {
    private static void verificar(boolean condicion, String texto) {
        if (!condicion) { throw new AssertionError(texto); }
    }
    public static void main(String[] args) {
        FachadaModeloCompra modelo = new FachadaModeloCompra(new ControlModeloCompra());
        AtomicInteger notificaciones = new AtomicInteger();
        modelo.agregarObservador(notificaciones::incrementAndGet);
        verificar(modelo.listarPeliculas().size() == 3, "Tres películas");
        verificar(modelo.listarFunciones("p1").size() == 3, "Tres funciones de la primera película");
        verificar(modelo.obtenerFuncion("f2").getSala().getNumero() == 2, "Sala 2");
        verificar(modelo.obtenerFuncion("f2").getSala().getCapacidad() == 24, "24 asientos");
        verificar(!modelo.asientosReservados("f2").contains("A3"), "A3 libre al principio");
        Compra compra = modelo.confirmarCompra("f2", List.of("A3", "A4"), "4242");
        verificar(compra.getTotal() == 170, "Total 170 pesos");
        verificar(compra.getBoletos().size() == 2, "Dos boletos");
        verificar(compra.getBoletos().get(0).getFuncion().getSala().getNumero() == 2, "Sala en boleto");
        verificar(modelo.asientosReservados("f2").contains("A3"), "A3 ocupado en f2");
        verificar(!modelo.asientosReservados("f1").contains("A3"), "A3 libre en otra función");
        verificar(notificaciones.get() == 1, "Observer avisa del cambio");
        boolean error = false;
        try { modelo.confirmarCompra("f2", List.of("A3"), "4242"); }
        catch (IllegalStateException ex) { error = true; }
        verificar(error, "Se rechaza la reserva duplicada");
        verificar(modelo.listarCompras().size() == 1, "No crea compra duplicada");
        System.out.println("OK: catálogo, sala, importe, boletos, reservas por función, Observer y bloqueo de duplicados.");
    }
}
