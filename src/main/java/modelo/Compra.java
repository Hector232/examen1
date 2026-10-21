package modelo;

import java.util.List;

public class Compra {

    private final int folio;
    private final Funcion funcion;
    private final List<Boleto> boletos;
    private final int precioUnitario;
    private final String ultimosCuatro;

    public Compra(int folio, Funcion funcion, List<Boleto> boletos, int precioUnitario, String ultimosCuatro) {
        this.folio = folio;
        this.funcion = funcion;
        this.boletos = List.copyOf(boletos);
        this.precioUnitario = precioUnitario;
        this.ultimosCuatro = ultimosCuatro;
    }

    public int getFolio() {
        return folio;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public List<Boleto> getBoletos() {
        return boletos;
    }

    public int getPrecioUnitario() {
        return precioUnitario;
    }

    public int getTotal() {
        return precioUnitario * boletos.size();
    }

    public String getUltimosCuatro() {
        return ultimosCuatro;
    }
}
