package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import game.Jogo;
   
// Implementação remota da interface JogoRemote, delegando chamadas para a lógica do Jogo
public class JogoRemoteImpl extends UnicastRemoteObject implements JogoRemote {

    // Instância do jogo que será acessada remotamente
    private Jogo jogo;

    // Construtor que inicializa a classe para RMI
    public JogoRemoteImpl(Jogo jogo) throws RemoteException {
        super();
        this.jogo = jogo;
    }

    @Override
    // Delega a entrada de um novo jogador para o jogo
    public String entrar(String nome) throws RemoteException {
        return jogo.entrar(nome);
    }

    @Override
    // Delega o ataque para o jogo
    public String atacar(String jogador, String origem, String destino) throws RemoteException {
        return jogo.atacar(jogador, origem, destino);
    }

    @Override
    // Delega o movimento de tropas para o jogo
    public String mover(String jogador, String origem, String destino, int qtd) throws RemoteException {
        return jogo.mover(jogador, origem, destino, qtd);
    }

    @Override
    // Delega o próximo turno para o jogo
    public String proximoTurno(String jogador) throws RemoteException {
        return jogo.proximoTurno(jogador);
    }

    @Override
    // Delega a obtenção do mapa em formato DTO para o jogo
    public List<PaisDTO> getMapa() throws RemoteException {
        return jogo.getMapaDTO();
    }

    @Override
    // Delega a obtenção do jogador atual para o jogo
    public String getJogadorAtual() throws RemoteException {
        return jogo.getJogadorAtual().getNome();
    }

    @Override
    // Delega a obtenção do estado do jogo para o jogo
    public String getEstadoJogo() throws RemoteException {
        return jogo.getEstadoJogo();
    }

    @Override
    // Delega a distribuição de tropas iniciais para o jogo
    public String distribuirTropas(String jogador, String pais, int qtd) throws RemoteException {
        return jogo.distribuirTropas(jogador, pais, qtd);
    }

    @Override
    // Delega a obtenção da fase atual para o jogo
    public String getFaseAtual() throws RemoteException {
        return jogo.getFaseAtual();
    }

    @Override
    // Delega a mudança de fase para o jogo
    public String passarFase(String jogador) throws RemoteException {
        return jogo.passarFase(jogador);
    }

    @Override
    // Delega a obtenção de tropas restantes para o jogo
    public int getTropasRestantes(String jogador) throws RemoteException {
        return jogo.getTropasRestantes(jogador);
    }

    @Override
    // Delega o reforço de tropas para o jogo
    public String reforcar(String jogador, String pais, int qtd) throws RemoteException {
        return jogo.reforcar(jogador, pais, qtd);
    }

    @Override
    // Delega a obtenção de tropas de reforço para o jogo
    public int getTropasReforco(String jogador) throws RemoteException {
        return jogo.getTropasReforco(jogador);
    }

    @Override
    // Delega a troca de cartas para o jogo
    public String trocarCartas(String jogador, List<Integer> indices) throws RemoteException {
        return jogo.trocarCartas(jogador, indices);
    }

    @Override
    // Delega a obtenção de cartas do jogador para o jogo
    public List<CartaDTO> getCartas(String jogador) throws RemoteException {
        return jogo.getCartasDoJogador(jogador);
    }
    
    @Override
    // Delega a obtenção do vencedor para o jogo
    public String getVencedor() throws RemoteException {
        return jogo.getVencedor(); 
    }
    
    @Override
    // Delega a obtenção dos nomes dos jogadores para o jogo
    public List<String> getNomesJogadores()throws RemoteException {
    	
    	return jogo.getNomesJogadores();
    }

    @Override
    // Delega a obtenção do histórico de ações para o jogo
    public List<String> getLogAcoes() throws RemoteException{
    	return jogo.getLogAcoes();
    }
}