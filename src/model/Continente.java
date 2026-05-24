package model;

import java.util.ArrayList;
import java.util.List;

// Representa um continente do mapa, que pode dar bônus de tropas se dominado completamente
public class Continente {
    // Nome do continente
    private String nome;
    // Bônus de tropas recebido ao dominar completamente este continente
    private int bonus;
    // Lista de países que fazem parte deste continente
    private List<Pais> paises = new ArrayList<>();

    // Construtor para criar um novo continente
    public Continente(String nome, int bonus) {
        this.nome = nome;
        this.bonus = bonus;
    }

    // Adiciona um país a este continente e vincula o continente ao país
    public void adicionarPais(Pais p) {
        paises.add(p);
        p.setContinente(this);
    }

    // Getter para a lista de países
    public List<Pais> getPaises() {
        return paises;
    }

    // Getter para o bônus deste continente
    public int getBonus() {
        return bonus;
    }

    // Getter para o nome do continente
    public String getNome() {
        return nome;
    }
}