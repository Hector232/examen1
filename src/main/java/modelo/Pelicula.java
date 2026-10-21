package modelo;

public class Pelicula {

    private final String id;
    private String titulo;
    private String clasificacion;
    private String duracion;
    private String sinopsis;
    private String inicial;

    public Pelicula(String id, String titulo, String clasificacion, String duracion, String sinopsis, String inicial) {
        this.id = id;
        this.titulo = titulo;
        this.clasificacion = clasificacion;
        this.duracion = duracion;
        this.sinopsis = sinopsis;
        this.inicial = inicial;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getInicial() {
        return inicial;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public void setInicial(String inicial) {
        this.inicial = inicial;
    }

}
