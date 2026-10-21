package modelo;

import java.util.ArrayList;
import java.util.List;

public class SujetoObservado {

    private final List<Observer> observadores = new ArrayList<>();

    public void agregarObservador(Observer observer) {
        observadores.add(observer);
    }

    public void notificarObservadores() {
        for (Observer observer : List.copyOf(observadores)) {
            observer.actualizar();
        }
    }
}
