package remote;

import java.io.Serializable;
import java.util.List;

// DTO (Data Transfer Object) para transferir dados de País via RMI
public class PaisDTO implements Serializable {
    // Nome do país
    public String nome;
    // Nome do jogador que é dono deste país
    public String dono;
    // Quantidade de tropas estacionadas neste país
    public int tropas;
    // Lista com nomes dos países vizinhos
    public List<String> vizinhos;
    // Nome do continente ao qual este país pertence
    public String continente;

    // Construtor para criar um DTO de país com todos os dados
    public PaisDTO(String nome, String dono, int tropas, List<String> vizinhos, String continente) {
        this.nome = nome;
        this.dono = dono;
        this.tropas = tropas;
        this.vizinhos = vizinhos;
        this.continente = continente;
    }
}

