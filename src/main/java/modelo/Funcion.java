package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Funcion {

    private final String id;
    private final Pelicula pelicula;
    private final Sala sala;
    private final LocalDate fecha;
    private final LocalTime hora;
    private final Set<String> asientosReservados = new HashSet<>();

    public Funcion(String id, Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public Set<String> getAsientosReservados() {
        return Collections.unmodifiableSet(asientosReservados);
    }

    boolean estaReservado(String clave) {
        return asientosReservados.contains(clave);
    }

    void reservar(String clave) {
        asientosReservados.add(clave);
    }

    void agregarOcupadosDeDemostracion(String... claves) {
        Collections.addAll(asientosReservados, claves);
    }
}
