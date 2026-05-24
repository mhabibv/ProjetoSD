package model;

import java.util.ArrayList;
import java.util.List;

// Representa um país no mapa do jogo Risk
public class Pais {
    // Nome do país
    private String nome;
    // Lista de países vizinhos que fazem fronteira com este
    private List<Pais> vizinhos;

    // Quantidade de infantaria estacionada neste país
    private int infantaria;
    // Quantidade de cavalaria estacionada neste país
    private int cavalaria;

    // Jogador que é dono deste país (null se ninguém)
    private Jogador dono;
    
    // Continente ao qual este país pertence
    private Continente continente;

    // Construtor para criar um novo país
    public Pais(String nome) {
        this.nome = nome;
        this.vizinhos = new ArrayList<>();
    }

    // Adiciona um país vizinho a este país
    public void adicionarVizinho(Pais p) {
        vizinhos.add(p);
    }

    // Getter para o nome do país
    public String getNome() {
        return nome;
    }

    // Getter para a lista de vizinhos
    public List<Pais> getVizinhos() {
        return vizinhos;
    }

    // Retorna uma string formatada com os nomes dos vizinhos e seus índices no mapa
    public String getNomesVizinhos(List<Pais> mapa) {
        String nomes = "";

        for (Pais v : vizinhos) {
            int index = mapa.indexOf(v);
            nomes += "(" + index + ") " + v.getNome() + " ";
        }

        return nomes;
    }
    
    // Getter para a quantidade de infantaria
    public int getInfantaria() {
        return infantaria;
    }

    // Setter para a quantidade de infantaria
    public void setInfantaria(int infantaria) {
        this.infantaria = infantaria;
    }

    // Getter para o dono do país
    public Jogador getDono() {
        return dono;
    }

    // Setter para o dono do país
    public void setDono(Jogador dono) {
        this.dono = dono;
    }
    
    // Getter para o continente
    public Continente getContinente() {
        return continente;
    }
    
    // Setter para o continente
    public void setContinente(Continente continente) {
        this.continente = continente;
    }
}