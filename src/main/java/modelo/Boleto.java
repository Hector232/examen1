package modelo;

public class Boleto {

    private final String id;
    private Funcion funcion;
    private String asiento;
    private int precio;

    public Boleto(String codigo, Funcion funcion, String asiento, int precio) {
        this.id = codigo;
        this.funcion = funcion;
        this.asiento = asiento;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public String getAsiento() {
        return asiento;
    }

    public int getPrecio() {
        return precio;

    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

}
