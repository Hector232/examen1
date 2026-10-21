package presentacion;

import java.util.List;
import java.util.Set;
import modelo.Compra;
import modelo.Funcion;
import modelo.Observer;
import modelo.Pelicula;

/**
 * Contrato que la capa de presentación ofrece al controlador.
 */
public interface IConsultaModelo {

    int consultarPrecioBoleto();

    List<Pelicula> listarPeliculas();

    List<Funcion> listarFunciones(String peliculaId);

    Set<String> asientosReservados(String funcionId);

    List<Compra> listarCompras();

    void agregarObservador(Observer observador);
}
