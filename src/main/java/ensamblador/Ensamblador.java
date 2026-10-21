package ensamblador;

import controlador.CompraBoletoControlador;
import modelo.ControlModeloCompra;
import modelo.FachadaModeloCompra;
import presentacion.ControlPresentacionCartelera;
import presentacion.FachadaPresentacionCartelera;
import vista.CarteleraView;

/**
 * Construye las dependencias: un solo modelo compartido por múltiples clientes.
 */
public class Ensamblador {

    private final FachadaModeloCompra modeloCompartido = new FachadaModeloCompra(new ControlModeloCompra());
    private int ventanas = 0;

    public void abrirVentana() {

        ControlPresentacionCartelera controlPresentacion = new ControlPresentacionCartelera(modeloCompartido);
        FachadaPresentacionCartelera fachadaPresentacion = new FachadaPresentacionCartelera(controlPresentacion);
        CompraBoletoControlador controlador = new CompraBoletoControlador(fachadaPresentacion);
        CarteleraView vista = new CarteleraView(controlador, this, ++ventanas, fachadaPresentacion);
        controlador.establecerVista(vista);
        vista.setVisible(true);

    }
}
