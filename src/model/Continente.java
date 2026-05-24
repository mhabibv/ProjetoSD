package model;

import java.util.ArrayList;
import java.util.List;

public class Continente {
    private String nome;
    private int bonus;
    private List<Pais> paises = new ArrayList<>();

    public Continente(String nome, int bonus) {
        this.nome = nome;
        this.bonus = bonus;
    }

    public void adicionarPais(Pais p) {
        paises.add(p);
        p.setContinente(this);
    }

    public List<Pais> getPaises() {
        return paises;
    }

    public int getBonus() {
        return bonus;
    }

    public String getNome() {
        return nome;
    }
}