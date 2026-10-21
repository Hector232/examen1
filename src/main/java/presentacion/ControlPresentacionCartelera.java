package presentacion;

import java.util.List;
import java.util.Set;
import modelo.Compra;
import modelo.FachadaModeloCompra;
import modelo.Funcion;
import modelo.IConsultasModelo;
import modelo.Observer;
import modelo.Pelicula;

/**
 * Puente entre la fachada de presentación y la fachada del modelo.
 */
public class ControlPresentacionCartelera {

    private final IConsultasModelo consultasModelo;
    private final modelo.IOperacionesModelo operacionesModelo;

    public ControlPresentacionCartelera(FachadaModeloCompra modelo) {
        this.consultasModelo = modelo;
        this.operacionesModelo = modelo;
    }

    public int consultarPrecioBoleto() {
        return consultasModelo.consultarPrecioBoleto();
    }

    public List<Pelicula> listarPeliculas() {
        return consultasModelo.listarPeliculas();
    }

    public List<Funcion> listarFunciones(String peliculaId) {
        return consultasModelo.listarFunciones(peliculaId);
    }

    public Set<String> asientosReservados(String funcionId) {
        return consultasModelo.asientosReservados(funcionId);
    }

    public List<Compra> listarCompras() {
        return consultasModelo.listarCompras();
    }

    public Compra confirmarCompra(String funcionId, List<String> asientos, String ultimosCuatro) {
        return operacionesModelo.confirmarCompra(funcionId, asientos, ultimosCuatro);
    }

    public void agregarObservador(Observer observador) {
        operacionesModelo.agregarObservador(observador);
    }
}
