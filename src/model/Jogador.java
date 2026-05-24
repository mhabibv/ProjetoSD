package model;

import java.util.ArrayList;
import java.util.List;

// Representa um jogador no jogo de Risk
public class Jogador {
    // Nome do jogador
    private String nome;

    // Construtor para criar um novo jogador
    public Jogador(String nome) {
        this.nome = nome;
    }

    // Getter para o nome do jogador
    public String getNome() {
        return nome;
    }
    
    // Lista de cartas que o jogador possui
    private List<Carta> cartas = new ArrayList<>();

    // Adiciona uma carta à lista do jogador
    public void adicionarCarta(Carta c) { cartas.add(c); }
    // Getter para as cartas do jogador
    public List<Carta> getCartas() { return cartas; }
    // Limpa todas as cartas do jogador (após trocar por tropas)
    public void limparCartas() { cartas.clear(); }
    
}