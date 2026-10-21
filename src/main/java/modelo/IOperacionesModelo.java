package modelo;

import java.util.List;

public interface IOperacionesModelo {

    Compra confirmarCompra(String funcionId, List<String> asientos, String ultimosCuatro);

    void agregarObservador (Observer observer);

}
