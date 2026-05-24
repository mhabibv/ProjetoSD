package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface JogoRemote extends Remote {

    String entrar(String nome) throws RemoteException;

    String atacar(String jogador, String origem, String destino) throws RemoteException;

    String mover(String jogador, String origem, String destino, int qtd) throws RemoteException;

    String proximoTurno(String jogador) throws RemoteException;

    List<PaisDTO> getMapa() throws RemoteException;

    String getJogadorAtual() throws RemoteException;

    String getEstadoJogo() throws RemoteException;

    String distribuirTropas(String jogador, String pais, int qtd) throws RemoteException;

    String getFaseAtual() throws RemoteException;

    String passarFase(String jogador) throws RemoteException;

    int getTropasRestantes(String jogador) throws RemoteException;

    String reforcar(String jogador, String pais, int qtd) throws RemoteException;

    int getTropasReforco(String jogador) throws RemoteException;
    
    String trocarCartas(String jogador, List<Integer> indices) throws RemoteException;
    
    List<CartaDTO> getCartas(String jogador) throws RemoteException;
    
    String getVencedor() throws RemoteException;
    
    List<String> getNomesJogadores() throws RemoteException;
    
    List<String> getLogAcoes() throws RemoteException;
    
}