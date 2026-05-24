package model;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private String nome;

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    
    private List<Carta> cartas = new ArrayList<>();

    public void adicionarCarta(Carta c) { cartas.add(c); }
    public List<Carta> getCartas() { return cartas; }
    public void limparCartas() { cartas.clear(); }
    
}