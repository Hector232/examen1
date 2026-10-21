package presentacion;

import java.util.List;
import modelo.Compra;

/**
 * Operaciones de compra expuestas por presentación.
 */
public interface IOperacionesModelo {

    Compra confirmarCompra(String funcionId, List<String> asientos, String ultimosCuatro);
}
