package model;

import java.util.ArrayList;
import java.util.List;

public class Pais {
    private String nome;
    private List<Pais> vizinhos;

    private int infantaria;
    private int cavalaria;

    private Jogador dono;
    
    private Continente continente;

    public Pais(String nome) {
        this.nome = nome;
        this.vizinhos = new ArrayList<>();
    }

    public void adicionarVizinho(Pais p) {
        vizinhos.add(p);
    }

    public String getNome() {
        return nome;
    }

    public List<Pais> getVizinhos() {
        return vizinhos;
    }

    public String getNomesVizinhos(List<Pais> mapa) {
        String nomes = "";

        for (Pais v : vizinhos) {
            int index = mapa.indexOf(v);
            nomes += "(" + index + ") " + v.getNome() + " ";
        }

        return nomes;
    }
    
    public int getInfantaria() {
        return infantaria;
    }

    public void setInfantaria(int infantaria) {
        this.infantaria = infantaria;
    }

    public Jogador getDono() {
        return dono;
    }

    public void setDono(Jogador dono) {
        this.dono = dono;
    }
    
    public Continente getContinente() {
        return continente;
    }
    
    public void setContinente(Continente continente) {
        this.continente = continente;
    }
}