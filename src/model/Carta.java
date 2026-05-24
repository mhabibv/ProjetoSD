package model;

public class Carta {

    public enum Tipo {
        INFANTARIA,
        CAVALARIA,
        ARTILHARIA
    }

    private String pais;
    private Tipo tipo;

    public Carta(String pais, Tipo tipo) {
        this.pais = pais;
        this.tipo = tipo;
    }

    public String getPais() {
        return pais;
    }

    public Tipo getTipo() {
        return tipo;
    }
}