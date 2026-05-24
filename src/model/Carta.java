package model;

// Representa uma carta do jogo Risk, que pode ser trocada por bônus de tropas
public class Carta {

    // Enum com os 3 tipos de carta possíveis
    public enum Tipo {
        INFANTARIA,
        CAVALARIA,
        ARTILHARIA
    }

    // País associado à carta
    private String pais;
    // Tipo da carta (INFANTARIA, CAVALARIA ou ARTILHARIA)
    private Tipo tipo;

    // Construtor para criar uma nova carta
    public Carta(String pais, Tipo tipo) {
        this.pais = pais;
        this.tipo = tipo;
    }

    // Getter para o país associado
    public String getPais() {
        return pais;
    }

    // Getter para o tipo da carta
    public Tipo getTipo() {
        return tipo;
    }
}