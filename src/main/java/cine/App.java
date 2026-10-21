package cine;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ensamblador.Ensamblador;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
                /* Se usa el aspecto predeterminado de Swing. */ }
            Ensamblador ensamblador = new Ensamblador();
            for (int i = 0; i < 2; i++) {

                ensamblador.abrirVentana();

            }

        });
    }
}
