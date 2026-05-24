package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

// Interface remota para o jogo Risk, permitindo acesso via RMI
public interface JogoRemote extends Remote {

    // Registra um novo jogador no jogo
    String entrar(String nome) throws RemoteException;

    // Realiza um ataque de um país para outro
    String atacar(String jogador, String origem, String destino) throws RemoteException;

    // Move tropas de um país vizinho para outro
    String mover(String jogador, String origem, String destino, int qtd) throws RemoteException;

    // Passa para o próximo turno
    String proximoTurno(String jogador) throws RemoteException;

    // Retorna o mapa atual como DTOs
    List<PaisDTO> getMapa() throws RemoteException;

    // Retorna o nome do jogador atual
    String getJogadorAtual() throws RemoteException;

    // Retorna o estado do jogo (AGUARDANDO_JOGADORES, DISTRIBUINDO_TROPAS, EM_JOGO)
    String getEstadoJogo() throws RemoteException;

    // Distribui tropas inicialmente em um país
    String distribuirTropas(String jogador, String pais, int qtd) throws RemoteException;

    // Retorna a fase atual do turno (REFORCO, ATAQUE, MOVIMENTO)
    String getFaseAtual() throws RemoteException;

    // Passa para a próxima fase do turno
    String passarFase(String jogador) throws RemoteException;

    // Retorna a quantidade de tropas que faltam distribuir inicialmente
    int getTropasRestantes(String jogador) throws RemoteException;

    // Reforça um país com tropas na fase de reforço
    String reforcar(String jogador, String pais, int qtd) throws RemoteException;

    // Retorna a quantidade de tropas de reforço disponíveis
    int getTropasReforco(String jogador) throws RemoteException;
    
    // Troca 3 cartas por um bônus de tropas
    String trocarCartas(String jogador, List<Integer> indices) throws RemoteException;
    
    // Retorna as cartas que o jogador possui
    List<CartaDTO> getCartas(String jogador) throws RemoteException;
    
    // Retorna o nome do vencedor, ou null se o jogo ainda está em andamento
    String getVencedor() throws RemoteException;
    
    // Retorna a lista de nomes de todos os jogadores
    List<String> getNomesJogadores() throws RemoteException;
    
    // Retorna o histórico de ações realizadas no jogo
    List<String> getLogAcoes() throws RemoteException;
    
}