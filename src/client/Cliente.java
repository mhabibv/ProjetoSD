package client;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import remote.JogoRemote;
import remote.PaisDTO;
import remote.CartaDTO;


public class Cliente {

    public static void main(String[] args) {
        try {
            JogoRemote jogo = (JogoRemote) Naming.lookup("rmi://localhost/Jogo");
            Scanner sc = new Scanner(System.in);

            System.out.println("=== BEM-VINDO AO RISK RMI ===");
            System.out.println("Digite seu nome para entrar:");
            String meuNome = sc.nextLine();

            String respostaEntrada = jogo.entrar(meuNome);
            

            if (!respostaEntrada.equals("OK")) {
                System.out.println("Servidor diz: " + respostaEntrada);
                return;
            }

            String ultimoTurno = "";
            String ultimoEstado = "";

            while (true) {
            	
            	String vencedor = jogo.getVencedor();
            	
                if (vencedor != null) {
                    System.out.println("\n====================================");
                    System.out.println("   JOGO ENCERRADO! VITORIA DE: " + vencedor);
                    System.out.println("====================================");
                    break; // Sai do loop e encerra o cliente
                }
                
                String estado = jogo.getEstadoJogo();
                String jogadorAtual = jogo.getJogadorAtual();
                String fase = jogo.getFaseAtual();
                List<PaisDTO> mapa = jogo.getMapa();
                
             // No topo do while(true), após pegar o mapa:
                List<String> logs = jogo.getLogAcoes(); // (Precisa adicionar no JogoRemote/Impl)


                // 1. LÓGICA DE ESPERA
                if (estado.equals("AGUARDANDO_JOGADORES")) {
                    if (!ultimoEstado.equals(estado)) {
                        System.out.println("Aguardando outros jogadores entrarem...");
                        ultimoEstado = estado;
                    }
                    Thread.sleep(1500);
                    continue;
                }

                // 2. VERIFICAÇÃO DE TURNO
                if (!jogadorAtual.equals(meuNome)) {
                    if (!ultimoTurno.equals(jogadorAtual)) {
                        System.out.println("\n--- Turno de " + jogadorAtual + " (" + estado + " / " + fase + ") ---");
                        ultimoTurno = jogadorAtual;
                    }
                    System.out.print(".");
                    Thread.sleep(2000);
                    continue;
                }

                // 3. SEU TURNO - EXECUÇÃO DE AÇÕES
                System.out.println("\n******************************");
                System.out.println("SUA VEZ! Fase: " + fase);
                System.out.println("******************************");

                if (estado.equals("DISTRIBUINDO_TROPAS")) {
                    executarDistribuicao(sc, jogo, mapa, meuNome);
                } else if (estado.equals("EM_JOGO")) {
                	// "Limpa" o console no Windows e Linux/Mac (subindo o scroll)
                	System.out.print("\033[H\033[2J");
                	System.out.flush();
                	mostrarMapa(mapa, meuNome);
                	

                    System.out.println(Cores.CIANO + "\n--- ÚLTIMAS AÇÕES DO MUNDO ---" + Cores.RESET);
                    for (String log : logs) {
                        System.out.println(" > " + log);
                    }
                    System.out.println(Cores.CIANO + "------------------------------" + Cores.RESET);
                    
                    switch (fase) {
                        case "REFORCO":
                            executarReforco(sc, jogo, mapa, meuNome);
                            break;
                        case "ATAQUE":
                            executarAtaque(sc, jogo, mapa, meuNome);
                            break;
                        case "MOVIMENTO":
                            executarMovimento(sc, jogo, mapa, meuNome);
                            break;
                    }
                }
                
                ultimoTurno = jogadorAtual;
                ultimoEstado = estado;
            }

        } catch (Exception e) {
            System.err.println("Erro na conexão com o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================
    // DISTRIBUIÇÃO INICIAL
    // =========================
    private static void executarDistribuicao(Scanner sc, JogoRemote jogo, List<PaisDTO> mapa, String meuNome) throws Exception {
        int restantes = jogo.getTropasRestantes(meuNome);
        System.out.println("Tropas para distribuir: " + restantes);
        
        List<PaisDTO> meus = filtrarPaisesDoJogador(mapa, meuNome);
        mostrarLista(meus, "SEUS PAÍSES");

        int index = escolherComCancelamento(sc, meus.size());
        if (index == -1) return;

        System.out.println("Quantidade:");
        int qtd = lerNumero(sc);

        // O servidor valida se o jogador tem essa quantidade
        String result = jogo.distribuirTropas(meuNome, meus.get(index).nome, qtd);
        System.out.println("Servidor: " + result);
    }

    // =========================
    // FASE DE REFORÇO
    // =========================
    private static void executarReforco(Scanner sc, JogoRemote jogo, List<PaisDTO> mapa, String meuNome) throws Exception {
        int restantes = jogo.getTropasReforco(meuNome);
        List<CartaDTO> cartas = jogo.getCartas(meuNome);

        System.out.println("--- REFORÇO ---");
        System.out.println("Tropas disponíveis: " + restantes);
        mostrarCartasLocal(cartas);

        System.out.println("1 - Colocar tropas no mapa");
        if (cartas.size() >= 3) System.out.println("2 - Trocar conjunto de 3 cartas");
        System.out.println("0 - Pular/Aguardar");

        int opcao = lerOpcaoMenu(sc, 0, 2);

        if (opcao == 1) {
            List<PaisDTO> meus = filtrarPaisesDoJogador(mapa, meuNome);
            mostrarLista(meus, "ONDE REFORÇAR?");
            int idx = escolherComCancelamento(sc, meus.size());
            if (idx == -1) return;
            
            System.out.println("Quantidade:");
            int qtd = lerNumero(sc);
            System.out.println("Servidor: " + jogo.reforcar(meuNome, meus.get(idx).nome, qtd));
        } else if (opcao == 2 && cartas.size() >= 3) {
            executarTrocaCartas(sc, jogo, meuNome);
        }
    }

    // =========================
    // FASE DE ATAQUE
    // =========================
    private static void executarAtaque(Scanner sc, JogoRemote jogo, List<PaisDTO> mapa, String meuNome) throws Exception {
        System.out.println("--- ATAQUE ---");
        System.out.println("1 - Atacar!");
        System.out.println("2 - Finalizar Ataques (Passar para Movimento)");

        int opcao = lerOpcaoMenu(sc, 1, 2);

        if (opcao == 2) {
            System.out.println("Servidor: " + jogo.passarFase(meuNome));
            return;
        }

        List<PaisDTO> atacantes = filtrarPaisesAtacantes(mapa, meuNome);
        if (atacantes.isEmpty()) {
            System.out.println("Você não tem países com mais de 1 tropa para atacar.");
            return;
        }

        mostrarLista(atacantes, "ESCOLHA A ORIGEM");
        int idxO = escolherComCancelamento(sc, atacantes.size());
        if (idxO == -1) return;
        PaisDTO origem = atacantes.get(idxO);

        List<PaisDTO> alvos = filtrarAlvos(mapa, origem, meuNome);
        mostrarLista(alvos, "ESCOLHA O ALVO (Vizinhos inimigos)");
        int idxD = escolherComCancelamento(sc, alvos.size());
        if (idxD == -1) return;
        
        System.out.println("Atacando...");
        String res = (jogo.atacar(meuNome, origem.nome, alvos.get(idxD).nome));
        System.out.println("\n" + res);
        
        // Limpa qualquer sobra de Enter do buffer
        sc.nextLine(); 
        
        System.out.println("\nPressione ENTER para continuar...");
        sc.nextLine(); 
    }

    // =========================
    // FASE DE MOVIMENTO
    // =========================
    private static void executarMovimento(Scanner sc, JogoRemote jogo, List<PaisDTO> mapa, String meuNome) throws Exception {
        System.out.println("--- MOVIMENTO ---");
        System.out.println("1 - Mover tropas");
        System.out.println("2 - Encerrar Turno");

        int opcao = lerOpcaoMenu(sc, 1, 2);

        if (opcao == 2) {
            System.out.println("Servidor: " + jogo.proximoTurno(meuNome));
            return;
        }

        List<PaisDTO> meus = filtrarPaisesAtacantes(mapa, meuNome);
        mostrarLista(meus, "MOVER DE ONDE?");
        int idxO = escolherComCancelamento(sc, meus.size());
        if (idxO == -1) return;
        PaisDTO origem = meus.get(idxO);

        List<PaisDTO> destinos = filtrarDestinos(mapa, origem, meuNome);
        mostrarLista(destinos, "PARA ONDE? (Seus vizinhos)");
        int idxD = escolherComCancelamento(sc, destinos.size());
        if (idxD == -1) return;

        System.out.println("Quantidade para mover:");
        int qtd = lerNumero(sc);
        System.out.println("Servidor: " + jogo.mover(meuNome, origem.nome, destinos.get(idxD).nome, qtd));
    }

    // =========================
    // AUXILIARES DE INPUT / FILTRO
    // =========================
    private static void executarTrocaCartas(Scanner sc, JogoRemote jogo, String meuNome) throws Exception {
        System.out.println("\n--- TROCA DE CARTAS ---");
        System.out.println("Digite os 3 índices das cartas separados por espaço (ex: 0 1 2):");
        
        // Limpa o buffer antes de ler a linha (importante se veio de um nextInt anterior)
        sc.nextLine(); 
        
        String linha = sc.nextLine();
        String[] partes = linha.trim().split("\\s+"); // Divide a linha por espaços

        if (partes.length != 3) {
            System.out.println("Erro: Você precisa digitar exatamente 3 números.");
            return;
        }

        try {
            List<Integer> indices = new ArrayList<>();
            for (String parte : partes) {
                indices.add(Integer.parseInt(parte));
            }

            // Envia para o servidor.h             O SERVIDOR vai validar se os índices existem 
            // e se a combinação de símbolos é válida.
            System.out.println("Servidor: " + jogo.trocarCartas(meuNome, indices));
            
            System.out.println("Pressione ENTER para continuar...");
            sc.nextLine();

        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas números inteiros.");
        }
    }

    private static void mostrarMapa(List<PaisDTO> mapa, String meuNome) {
        System.out.println(Cores.BRANCO_NEGRITO + "\n--- MAPA ATUAL ---" + Cores.RESET);
        
        for (int i = 0; i < mapa.size(); i++) {
            PaisDTO p = mapa.get(i);
            
            // Define a cor do dono: Verde para você, Vermelho para os outros, Branco se for ninguém
            String corDono;
            if (p.dono.equals(meuNome)) {
                corDono = Cores.VERDE;
            } else if (p.dono.equals("Ninguém")) {
                corDono = Cores.RESET;
            } else {
                corDono = Cores.VERMELHO;
            }

            // Cor para destacar quando um país tem muitas tropas (ex: mais de 5)
            String corTropas = (p.tropas > 5) ? Cores.AMARELO : Cores.RESET;

            // Montando o printf com as cores incorporadas
            // %s recebe a cor, %-20s o texto, e o Cores.RESET limpa para a próxima coluna
            System.out.printf("[%d] %s%-20s%s | Dono: %s%-10s%s | Tropas: %s%d%s | (%s)\n", 
                i, 
                Cores.CIANO, p.nome, Cores.RESET, 
                corDono, p.dono, Cores.RESET, 
                corTropas, p.tropas, Cores.RESET, 
                p.continente);
        }
        System.out.println(Cores.BRANCO_NEGRITO + "------------------" + Cores.RESET);
    }

    private static void mostrarLista(List<PaisDTO> lista, String titulo) {
        System.out.println("\n>>> " + titulo);
        if (lista.isEmpty()) System.out.println("(Vazia)");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(i + " - " + lista.get(i).nome + " (" + lista.get(i).tropas + " tropas)");
        }
    }

    private static void mostrarCartasLocal(List<CartaDTO> cartas) {
        System.out.print("Suas cartas: ");
        if (cartas.isEmpty()) System.out.println("[Nenhuma]");
        else {
            for (int i = 0; i < cartas.size(); i++) {
                System.out.print("[" + i + ":" + cartas.get(i).tipo + "] ");
            }
            System.out.println();
        }
    }

    private static int escolherComCancelamento(Scanner sc, int max) {
        System.out.println("Escolha o índice (ou -1 para cancelar):");
        while (true) {
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                if (v == -1) return -1;
                if (v >= 0 && v < max) return v;
            } else sc.next();
            System.out.println("Índice inválido!");
        }
    }

    private static int lerOpcaoMenu(Scanner sc, int min, int max) {
        while (true) {
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                if (v >= min && v <= max) return v;
            } else sc.next();
            System.out.println("Opção inválida!");
        }
    }

    private static int lerNumero(Scanner sc) {
        while (true) {
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                if (v > 0) return v;
            } else sc.next();
            System.out.println("Digite um número positivo!");
        }
    }

    private static List<PaisDTO> filtrarPaisesDoJogador(List<PaisDTO> mapa, String jogador) {
        List<PaisDTO> l = new ArrayList<>();
        for (PaisDTO p : mapa) if (p.dono.equals(jogador)) l.add(p);
        return l;
    }

    private static List<PaisDTO> filtrarPaisesAtacantes(List<PaisDTO> mapa, String jogador) {
        List<PaisDTO> l = new ArrayList<>();
        for (PaisDTO p : mapa) if (p.dono.equals(jogador) && p.tropas > 1) l.add(p);
        return l;
    }

    private static List<PaisDTO> filtrarAlvos(List<PaisDTO> mapa, PaisDTO origem, String jogador) {
        List<PaisDTO> l = new ArrayList<>();
        for (PaisDTO p : mapa) if (origem.vizinhos.contains(p.nome) && !p.dono.equals(jogador)) l.add(p);
        return l;
    }

    private static List<PaisDTO> filtrarDestinos(List<PaisDTO> mapa, PaisDTO origem, String jogador) {
        List<PaisDTO> l = new ArrayList<>();
        for (PaisDTO p : mapa) if (origem.vizinhos.contains(p.nome) && p.dono.equals(jogador)) l.add(p);
        return l;
    }
}