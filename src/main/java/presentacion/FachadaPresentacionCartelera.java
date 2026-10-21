package presentacion;

import java.util.List;
import java.util.Set;
import modelo.Compra;
import modelo.Funcion;
import modelo.Observer;
import modelo.Pelicula;

/**
 * API de presentación utilizada por CompraBoletoControlador.
 */
public class FachadaPresentacionCartelera implements IConsultaModelo, IOperacionesModelo {

    private final ControlPresentacionCartelera control;

    public FachadaPresentacionCartelera(ControlPresentacionCartelera control) {
        this.control = control;
    }

    @Override
    public int consultarPrecioBoleto() {
        return control.consultarPrecioBoleto();
    }

    @Override
    public List<Pelicula> listarPeliculas() {
        return control.listarPeliculas();
    }

    @Override
    public List<Funcion> listarFunciones(String peliculaId) {
        return control.listarFunciones(peliculaId);
    }

    @Override
    public Set<String> asientosReservados(String funcionId) {
        return control.asientosReservados(funcionId);
    }

    @Override
    public List<Compra> listarCompras() {
        return control.listarCompras();
    }

    @Override
    public void agregarObservador(Observer observador) {
        control.agregarObservador(observador);
    }

    @Override
    public Compra confirmarCompra(String funcionId, List<String> asientos, String ultimosCuatro) {
        return control.confirmarCompra(funcionId, asientos, ultimosCuatro);
    }
}
