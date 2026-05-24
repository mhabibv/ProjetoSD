package remote;

import java.io.Serializable;

public class CartaDTO implements Serializable {

    public String pais;
    public String tipo;

    public CartaDTO(String pais, String tipo) {
        this.pais = pais;
        this.tipo = tipo;
    }
}