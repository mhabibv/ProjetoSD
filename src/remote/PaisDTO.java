package remote;

import java.io.Serializable;
import java.util.List;

public class PaisDTO implements Serializable {
    public String nome;
    public String dono;
    public int tropas;
    public List<String> vizinhos;
    public String continente;

    public PaisDTO(String nome, String dono, int tropas, List<String> vizinhos, String continente) {
        this.nome = nome;
        this.dono = dono;
        this.tropas = tropas;
        this.vizinhos = vizinhos;
        this.continente = continente;
    }
}

