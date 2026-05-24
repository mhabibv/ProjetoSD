package remote;

import java.io.Serializable;

// DTO (Data Transfer Object) para transferir dados de Carta via RMI
public class CartaDTO implements Serializable {

    // País associado à carta
    public String pais;
    // Tipo da carta (INFANTARIA, CAVALARIA, ARTILHARIA)
    public String tipo;

    // Construtor para criar um DTO de carta
    public CartaDTO(String pais, String tipo) {
        this.pais = pais;
        this.tipo = tipo;
    }
}