package modelo;

import java.util.List;
import java.util.Set;

public interface IConsultasModelo {

    int consultarPrecioBoleto();

    List<Pelicula> listarPeliculas();

    List<Funcion> listarFunciones(String peliculaId);

    Funcion obtenerFuncion(String id);

    Set<String> asientosReservados(String funcionId);

    List<Compra> listarCompras();
}
