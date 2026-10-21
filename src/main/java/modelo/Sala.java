package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sala {

    private final int numero;
    private final int filas;
    private final int columnas;
    private final List<Asiento> asientos = new ArrayList<>();

    public Sala(int numero, int filas, int columnas) {
        this.numero = numero;
        this.filas = filas;
        this.columnas = columnas;
        
        
        for (int f = 0; f < filas; f++) {
            for (int c = 1; c <= columnas; c++) {
                asientos.add(new Asiento("" + (char) ('A' + f) + c));
            }
        }
        
        
    }

    public int getNumero() {
        return numero;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getCapacidad() {
        return asientos.size();
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public boolean contiene(String clave) {
        return asientos.stream().anyMatch(a -> a.getClave().equals(clave));
    }
}
