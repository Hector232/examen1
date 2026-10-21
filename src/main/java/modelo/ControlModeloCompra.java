package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mantiene el estado compartido; cada Funcion tiene SUS PROPIOS asientos
 * ocupados.
 */
public class ControlModeloCompra {

    public static final int PRECIO_BOLETO = 85;
    private final List<Pelicula> peliculas = new ArrayList<>();
    private final List<Funcion> funciones = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final SujetoObservado sujeto = new SujetoObservado();
    private int consecutivo = 1;

    public ControlModeloCompra() {
        cargarDatosDeDemostracion();
    }

    public void agregarObservador(Observer o) {
        sujeto.agregarObservador(o);
    }

    public int consultarPrecioBoleto() {
        return PRECIO_BOLETO;
    }

    public List<Pelicula> listarPeliculas() {
        return List.copyOf(peliculas);
    }

    public List<Funcion> listarFunciones(String peliculaId) {
        return funciones.stream()
                .filter(f -> f.getPelicula().getId().equals(peliculaId))
                .sorted(Comparator.comparing(Funcion::getHora))
                .collect(Collectors.toUnmodifiableList());
    }

    public Funcion obtenerFuncion(String id) {
        return funciones.stream().
                filter(f -> f.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Función no encontrada."));
    }

    public Set<String> asientosReservados(String funcionId) {
        return Set.copyOf(obtenerFuncion(funcionId).getAsientosReservados());
    }

    public List<Compra> listarCompras() {
        return List.copyOf(compras);
    }

    public Compra confirmarCompra(String funcionId, List<String> asientos, String ultimosCuatro) {
        Compra compra;
        synchronized (this) {
            Funcion funcion = obtenerFuncion(funcionId);
            if (asientos == null || asientos.isEmpty()) {
                throw new IllegalArgumentException("Selecciona al menos un asiento.");
            }
            if (!ultimosCuatro.matches("\\d{4}")) {
                throw new IllegalArgumentException("La tarjeta de demostración es inválida.");
            }
            Set<String> unicos = new LinkedHashSet<>(asientos);
            if (unicos.size() != asientos.size()) {
                throw new IllegalArgumentException("Se repitió un asiento.");
            }
            for (String asiento : unicos) {
                if (!funcion.getSala().contiene(asiento)) {
                    throw new IllegalArgumentException("El asiento " + asiento + " no existe en la sala.");
                }
                if (funcion.estaReservado(asiento)) {
                    throw new IllegalStateException("El asiento " + asiento + " ya fue reservado por otro cliente.");
                }
            }
            // La validación y la reserva se realizan juntas, antes de notificar a las vistas.
            for (String asiento : unicos) {
                funcion.reservar(asiento);
            }
            
            int folio = consecutivo++;
            List<Boleto> boletos = new ArrayList<>();
            int indice = 1;
            for (String asiento : unicos) {
                boletos.add(new Boleto(String.format("CN-%04d-%02d", folio, indice++),
                        funcion, asiento, PRECIO_BOLETO));
            }
            compra = new Compra(folio, funcion, boletos, PRECIO_BOLETO, ultimosCuatro);
            compras.add(compra);
        }
        sujeto.notificarObservadores();
        return compra;
    }

    private void cargarDatosDeDemostracion() {
        Pelicula jumanji = new Pelicula("p1", "jumanji", "B15", "2 h 05 min",
                "un tablero magico o algo así no me acuerdo", "J");
        Pelicula pokemon = new Pelicula("p2", "Pokemon", "A", "1 h 45 min",
                "mounstros de bolsillo", "P");
        Pelicula espia = new Pelicula("p3", "007", "AA", "1 h 30 min",
                "un espia", "0");

        peliculas.addAll(List.of(jumanji, pokemon, espia));

        Sala sala1 = new Sala(1, 4, 6);
        Sala sala2 = new Sala(2, 4, 6);
        Sala sala3 = new Sala(3, 4, 6);

        LocalDate fecha = LocalDate.of(2026, 10, 24);

        funciones.add(new Funcion("f1", jumanji, sala2, fecha, LocalTime.of(16, 20)));
        funciones.add(new Funcion("f2", jumanji, sala2, fecha, LocalTime.of(19, 0)));
        funciones.add(new Funcion("f3", jumanji, sala2, fecha, LocalTime.of(21, 40)));
        funciones.add(new Funcion("f4", pokemon, sala1, fecha, LocalTime.of(17, 30)));
        funciones.add(new Funcion("f5", pokemon, sala1, fecha, LocalTime.of(20, 0)));
        funciones.add(new Funcion("f6", espia, sala3, fecha, LocalTime.of(16, 0)));
        funciones.add(new Funcion("f7", espia, sala3, fecha, LocalTime.of(18, 30)));

        obtenerFuncion("f1").agregarOcupadosDeDemostracion("B2", "D5");
        obtenerFuncion("f2").agregarOcupadosDeDemostracion("A6", "B3", "C5", "D2");
        obtenerFuncion("f3").agregarOcupadosDeDemostracion("A1", "D5");
        obtenerFuncion("f4").agregarOcupadosDeDemostracion("A4", "B4");
        obtenerFuncion("f5").agregarOcupadosDeDemostracion("C4");
        obtenerFuncion("f6").agregarOcupadosDeDemostracion("D2");
    }
}
