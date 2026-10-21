package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Fachada del modelo e intermediaria de las notificaciones Observer.
 */
public class FachadaModeloCompra implements IConsultasModelo, IOperacionesModelo, Observer {

    private final ControlModeloCompra control;
    private final List<Observer> observadores = new ArrayList<>();

    public FachadaModeloCompra(ControlModeloCompra control) {
        this.control = control;
        control.agregarObservador(this);
    }

    public void agregarObservador(Observer observador) {
        observadores.add(observador);
    }

    @Override
    public void actualizar() {
        for (Observer observador : List.copyOf(observadores)) {
            observador.actualizar();
        }
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
    public Funcion obtenerFuncion(String id) {
        return control.obtenerFuncion(id);
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
    public Compra confirmarCompra(String funcionId, List<String> asientos, String ultimosCuatro) {
        return control.confirmarCompra(funcionId, asientos, ultimosCuatro);
    }
}
