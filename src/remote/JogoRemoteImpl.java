package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import game.Jogo;
   

public class JogoRemoteImpl extends UnicastRemoteObject implements JogoRemote {

    private Jogo jogo;

    public JogoRemoteImpl(Jogo jogo) throws RemoteException {
        super();
        this.jogo = jogo;
    }

    @Override
    public String entrar(String nome) throws RemoteException {
        return jogo.entrar(nome);
    }

    @Override
    public String atacar(String jogador, String origem, String destino) throws RemoteException {
        return jogo.atacar(jogador, origem, destino);
    }

    @Override
    public String mover(String jogador, String origem, String destino, int qtd) throws RemoteException {
        return jogo.mover(jogador, origem, destino, qtd);
    }

    @Override
    public String proximoTurno(String jogador) throws RemoteException {
        return jogo.proximoTurno(jogador);
    }

    @Override
    public List<PaisDTO> getMapa() throws RemoteException {
        return jogo.getMapaDTO();
    }

    @Override
    public String getJogadorAtual() throws RemoteException {
        return jogo.getJogadorAtual().getNome();
    }

    @Override
    public String getEstadoJogo() throws RemoteException {
        return jogo.getEstadoJogo();
    }

    @Override
    public String distribuirTropas(String jogador, String pais, int qtd) throws RemoteException {
        return jogo.distribuirTropas(jogador, pais, qtd);
    }

    @Override
    public String getFaseAtual() throws RemoteException {
        return jogo.getFaseAtual();
    }

    @Override
    public String passarFase(String jogador) throws RemoteException {
        return jogo.passarFase(jogador);
    }

    @Override
    public int getTropasRestantes(String jogador) throws RemoteException {
        return jogo.getTropasRestantes(jogador);
    }

    @Override
    public String reforcar(String jogador, String pais, int qtd) throws RemoteException {
        return jogo.reforcar(jogador, pais, qtd);
    }

    @Override
    public int getTropasReforco(String jogador) throws RemoteException {
        return jogo.getTropasReforco(jogador);
    }

    @Override
    public String trocarCartas(String jogador, List<Integer> indices) throws RemoteException {
        return jogo.trocarCartas(jogador, indices);
    }

    @Override
    public List<CartaDTO> getCartas(String jogador) throws RemoteException {
        return jogo.getCartasDoJogador(jogador);
    }
    
    @Override
    public String getVencedor() throws RemoteException {
        return jogo.getVencedor(); 
    }
    
    @Override
    public List<String> getNomesJogadores()throws RemoteException {
    	
    	return jogo.getNomesJogadores();
    }

    @Override
    public List<String> getLogAcoes() throws RemoteException{
    	return jogo.getLogAcoes();
    }
}