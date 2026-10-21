package controlador;

import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;
import modelo.Compra;
import modelo.Funcion;
import modelo.Pelicula;
import presentacion.FachadaPresentacionCartelera;
import presentacion.IConsultaModelo;
import presentacion.IOperacionesModelo;
import vista.CarteleraView;

/**
 * Recibe acciones de CarteleraView y delega en presentación.
 */
public class CompraBoletoControlador {

    private final IConsultaModelo consultaModelo;
    private final IOperacionesModelo presentacion;

    public CompraBoletoControlador(FachadaPresentacionCartelera presentacion) {
        this.presentacion = presentacion;
        this.consultaModelo = presentacion;
    }

    public void establecerVista(CarteleraView vista) {

        consultaModelo.agregarObservador(() -> SwingUtilities.invokeLater(vista::actualizarDisponibilidad));
    }

    public Compra confirmarCompra(String funcionId, List<String> asientos, String ultimosCuatro) {
        return presentacion.confirmarCompra(funcionId, asientos, ultimosCuatro);
    }
}
