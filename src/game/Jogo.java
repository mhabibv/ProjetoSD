package game;

import model.*;
import remote.CartaDTO;
import remote.PaisDTO;
import java.util.*;

public class Jogo {
	
	private String vencedor = null;
	private List<String> logAcoes = new ArrayList<>();

    private List<Pais> mapa = new ArrayList<>();
    private List<Jogador> jogadores = new ArrayList<>();
    private List<Continente> continentes = new ArrayList<>();

    private int turnoAtual = 0;

    private enum EstadoJogo {
        AGUARDANDO_JOGADORES,
        DISTRIBUINDO_TROPAS,
        EM_JOGO
    }

    private enum FaseTurno {
        REFORCO,
        ATAQUE,
        MOVIMENTO
    }

    private EstadoJogo estado = EstadoJogo.AGUARDANDO_JOGADORES;
    private FaseTurno faseAtual = FaseTurno.REFORCO;

    private boolean conquistouNoTurno = false;
    private int maxJogadores = 2;

    private Map<String, Integer> tropasParaDistribuir = new HashMap<>();
    private Map<String, Integer> tropasReforco = new HashMap<>();

    // =========================
    // UTILITÁRIOS
    // =========================

    public synchronized Jogador getJogadorAtual() {
        return jogadores.get(turnoAtual);
    }

    private Pais buscarPais(String nome) {
        for (Pais p : mapa) {
            if (p.getNome().equalsIgnoreCase(nome)) return p;
        }
        return null;
    }

    private int calcularReforco(Jogador j) {
        int territorios = 0;
        for (Pais p : mapa) {
            if (p.getDono() == j) territorios++;
        }
        int base = Math.max(3, territorios / 2);
        
        int bonusC = 0;
        for (Continente c : continentes) {
            boolean domina = true;
            for (Pais p : c.getPaises()) {
                if (p.getDono() != j) { domina = false; break; }
            }
            if (domina) bonusC += c.getBonus();
        }
        return base + bonusC;
    }
    
    private void verificarVitoria() {
        Jogador donoUnico = null;
        
        for (Pais p : mapa) {
            if (donoUnico == null) {
                donoUnico = p.getDono();
            } else if (p.getDono() != donoUnico) {
                // Se encontrarmos dois donos diferentes no mapa, ninguém venceu ainda
                return; 
            }
        }
        
        // Se o código chegou aqui, todos os países têm o mesmo dono!
        this.vencedor = donoUnico.getNome();
    }

    // =========================
    // ENTRADA E SETUP
    // =========================

    public synchronized String entrar(String nome) {
        if (estado != EstadoJogo.AGUARDANDO_JOGADORES) return "Erro: Jogo em andamento.";
        for (Jogador j : jogadores) {
            if (j.getNome().equalsIgnoreCase(nome)) return "Erro: Nome já existe.";
        }
        jogadores.add(new Jogador(nome));
        registrarAcao(nome + " entrou no jogo!");
        if (jogadores.size() == maxJogadores) iniciarJogo();
        return "OK";
    }

    private void iniciarJogo() {
        Collections.shuffle(mapa);
        for (int i = 0; i < mapa.size(); i++) {
            Pais p = mapa.get(i);
            Jogador j = jogadores.get(i % jogadores.size());
            p.setDono(j);
            p.setInfantaria(1);
        }
        for (Jogador j : jogadores) {
            tropasParaDistribuir.put(j.getNome(), 5);
        }
        estado = EstadoJogo.DISTRIBUINDO_TROPAS;
        registrarAcao("O mapa foi distribuído! Comecem a ocupação.");
    }

    // =========================
    // DISTRIBUIÇÃO INICIAL
    // =========================

    public synchronized String distribuirTropas(String jogador, String nomePais, int qtd) {
        if (estado != EstadoJogo.DISTRIBUINDO_TROPAS) return "Erro: Não é fase de distribuição.";
        if (!getJogadorAtual().getNome().equals(jogador)) return "Erro: Não é seu turno.";

        Pais p = buscarPais(nomePais);
        if (p == null) return "Erro: País inválido.";
        if (!p.getDono().getNome().equals(jogador)) return "Erro: O país não é seu.";

        int restantes = tropasParaDistribuir.getOrDefault(jogador, 0);
        if (qtd <= 0 || qtd > restantes) return "Erro: Quantidade inválida (Disponível: " + restantes + ").";

        p.setInfantaria(p.getInfantaria() + qtd);
        tropasParaDistribuir.put(jogador, restantes - qtd);

        if (tropasParaDistribuir.get(jogador) == 0) {
            turnoAtual = (turnoAtual + 1) % jogadores.size();
            
            // Verifica se todos terminaram a distribuição
            boolean todosTerminaram = tropasParaDistribuir.values().stream().allMatch(v -> v == 0);
            if (todosTerminaram) {
                estado = EstadoJogo.EM_JOGO;
                faseAtual = FaseTurno.REFORCO;
                turnoAtual = 0; // Começa pelo primeiro
                int r = calcularReforco(getJogadorAtual());
                tropasReforco.put(getJogadorAtual().getNome(), r);
            }
        }
        return "OK: Tropas posicionadas.";
    }

    // =========================
    // REFORÇO
    // =========================

    public synchronized String reforcar(String jogador, String nomePais, int qtd) {
        if (faseAtual != FaseTurno.REFORCO) return "Erro: Não é fase de reforço.";
        if (!getJogadorAtual().getNome().equals(jogador)) return "Erro: Não é seu turno.";

        Pais p = buscarPais(nomePais);
        int restantes = tropasReforco.getOrDefault(jogador, 0);

        if (p == null || !p.getDono().getNome().equals(jogador)) return "Erro: Território inválido.";
        if (qtd <= 0 || qtd > restantes) return "Erro: Tropas insuficientes.";

        p.setInfantaria(p.getInfantaria() + qtd);
        tropasReforco.put(jogador, restantes - qtd);
        
        registrarAcao(jogador + " reforçou " + nomePais + " com " + qtd + " tropas!");

        
        if (tropasReforco.get(jogador) == 0) {
            faseAtual = FaseTurno.ATAQUE;
            return "OK: Reforço finalizado. Fase de ATAQUE iniciada.";
        }
        return "OK: " + tropasReforco.get(jogador) + " restantes.";
        
    }

    // =========================
    // ATAQUE (Soberano)
    // =========================

    public synchronized String atacar(String jogadorNome, String origemNome, String destinoNome) {
        // 1. Validações de Regra de Negócio
        if (faseAtual != FaseTurno.ATAQUE) return "Erro: Não é fase de ataque.";
        if (!getJogadorAtual().getNome().equals(jogadorNome)) return "Erro: Não é seu turno.";

        Pais o = buscarPais(origemNome);
        Pais d = buscarPais(destinoNome);

        if (o == null || d == null) return "Erro: País inválido.";
        if (o.getDono() != getJogadorAtual()) return "Erro: Origem não é sua.";
        if (d.getDono() == getJogadorAtual()) return "Erro: Destino já é seu.";
        if (!o.getVizinhos().contains(d)) return "Erro: Não são vizinhos.";
        if (o.getInfantaria() <= 1) return "Erro: Tropas insuficientes para atacar (mínimo 2).";

        // 2. Lógica de Dados
        Random r = new Random();
        int atk = r.nextInt(6) + 1;
        int def = r.nextInt(6) + 1;

        // String base para o log (o que os outros verão) e para o result (o que o atacante verá)
        String dadosFormatados = "[" + atk + " vs " + def + "]";
        String logBase = jogadorNome + " atacou " + destinoNome + " " + dadosFormatados;

        if (atk > def) {
            // Sucesso no ataque
            d.setInfantaria(d.getInfantaria() - 1);

            if (d.getInfantaria() <= 0) {
                // Lógica de Ocupação/Conquista
                d.setDono(getJogadorAtual());
                d.setInfantaria(1);
                o.setInfantaria(o.getInfantaria() - 1);
                conquistouNoTurno = true;

                // Log de destaque para conquista
                registrarAcao("*** " + jogadorNome + " CONQUISTOU " + destinoNome + "! ***");
                
                verificarVitoria();
                return "Atk: " + atk + " vs Def: " + def + "\nTERRITÓRIO CONQUISTADO!";
            } else {
                // Apenas venceu o embate mas o país ainda resiste
                registrarAcao(logBase + " -> Vitória!");
                return "Atk: " + atk + " vs Def: " + def + "\nVitoria no embate!";
            }
        } else {
            // Derrota no ataque
            o.setInfantaria(o.getInfantaria() - 1);
            
            registrarAcao(logBase + " -> Derrota!");
            return "Atk: " + atk + " vs Def: " + def + "\nDerrota no embate! Perdeu 1 tropa.";
        }
    }

    // =========================
    // MOVIMENTO
    // =========================

    public synchronized String mover(String jogador, String origem, String destino, int qtd) {
        if (faseAtual != FaseTurno.MOVIMENTO) return "Erro: Não é fase de movimento.";
        if (!getJogadorAtual().getNome().equals(jogador)) return "Erro: Não é seu turno.";

        Pais o = buscarPais(origem);
        Pais d = buscarPais(destino);

        if (o == null || d == null) return "Erro: País inválido.";
        if (o.getDono() != getJogadorAtual() || d.getDono() != getJogadorAtual()) return "Erro: Ambos países devem ser seus.";
        if (!o.getVizinhos().contains(d)) return "Erro: Não são vizinhos.";
        if (qtd <= 0 || o.getInfantaria() <= qtd) return "Erro: Quantidade inválida (deve sobrar 1 na origem).";

        o.setInfantaria(o.getInfantaria() - qtd);
        d.setInfantaria(d.getInfantaria() + qtd);
        registrarAcao(jogador + " moveu " + qtd + " tropas de " + origem + "para " + destino +"!");
        return "OK: Tropas movidas.";
    }

    // =========================
    // CARTAS E TROCA
    // =========================

    public synchronized String trocarCartas(String jogadorNome, List<Integer> indices) {
        if (faseAtual != FaseTurno.REFORCO) return "Erro: Troca apenas na fase de reforço.";
        Jogador j = getJogadorAtual();
        if (!j.getNome().equals(jogadorNome)) return "Erro: Não é seu turno.";

        List<Carta> cartas = j.getCartas();
        if (indices.size() != 3) return "Erro: Selecione exatamente 3 cartas.";
        
        List<Carta> selecionadas = new ArrayList<>();
        for (int i : indices) {
            if (i < 0 || i >= cartas.size()) return "Erro: Índice de carta inválido.";
            selecionadas.add(cartas.get(i));
        }

        if (!combinacaoValida(selecionadas)) return "Erro: Combinação de símbolos inválida.";

        cartas.removeAll(selecionadas);
        int bonus = 6; // Pode ser progressivo se desejar
        int atual = tropasReforco.getOrDefault(jogadorNome, 0);
        tropasReforco.put(jogadorNome, atual + bonus);

        return "OK: Troca realizada! +" + bonus + " tropas.";
    }

    private boolean combinacaoValida(List<Carta> selecionadas) {
        Set<Carta.Tipo> tipos = new HashSet<>();
        for (Carta c : selecionadas) tipos.add(c.getTipo());
        return tipos.size() == 1 || tipos.size() == 3;
    }

    // =========================
    // FLUXO DE TURNOS
    // =========================

    public synchronized String passarFase(String jogador) {
        if (!getJogadorAtual().getNome().equals(jogador)) return "Erro: Não é seu turno.";
        
        if (faseAtual == FaseTurno.ATAQUE) {
            faseAtual = FaseTurno.MOVIMENTO;
            return "OK: Iniciando fase de MOVIMENTO.";
        }
        return "Erro: Não pode pular esta fase desta forma.";
    }

    public synchronized String proximoTurno(String jogador) {
    	
    	if (vencedor != null) return "Erro: O jogo já acabou! Vencedor: " + vencedor;
    	
        Jogador atual = getJogadorAtual();
        
        // 1. Validações
        if (!atual.getNome().equals(jogador)) return "Erro: Não é seu turno.";
        if (atual.getCartas().size() >= 5) return "Erro: Você tem 5 cartas. Deve trocar obrigatoriamente.";

        // 2. Entrega de carta se conquistou algo (antes de mudar o turno)
        if (conquistouNoTurno) {
            Carta.Tipo t = Carta.Tipo.values()[new Random().nextInt(3)];
            atual.adicionarCarta(new Carta("Bônus de Conquista", t));
        } 
        
        // 3. Resetar flags do turno que está acabando
        conquistouNoTurno = false;

        // 4. Lógica para encontrar o próximo jogador vivo
        // O do-while garante que se um jogador perdeu todos os países, ele será pulado.
        do {
            turnoAtual = (turnoAtual + 1) % jogadores.size();
        } while (contarPaises(getJogadorAtual()) == 0);
        
        // 5. Configurar o início do turno do novo jogador
        faseAtual = FaseTurno.REFORCO;
        
        Jogador proximo = getJogadorAtual();
        int r = calcularReforco(proximo);
        tropasReforco.put(proximo.getNome(), r);
        registrarAcao("--- Turno de " + proximo.getNome() + " começou ---");
        return "OK: Turno de " + proximo.getNome();
    }

    private int contarPaises(Jogador j) {
        int total = 0;
        for (Pais p : mapa) {
            if (p.getDono() != null && p.getDono().equals(j)) total++;
        }
        return total;
    }

    // =========================
    // MÉTODOS REMOTOS (GETTERS)
    // =========================

    public synchronized List<PaisDTO> getMapaDTO() {
        List<PaisDTO> lista = new ArrayList<>();
        for (Pais p : mapa) {
            List<String> vNomes = new ArrayList<>();
            for (Pais v : p.getVizinhos()) vNomes.add(v.getNome());

            // Verificação de segurança para o dono
            String nomeDono = (p.getDono() != null) ? p.getDono().getNome() : "Ninguém";

            lista.add(new PaisDTO(
                p.getNome(), 
                nomeDono, 
                p.getInfantaria(), 
                vNomes, 
                p.getContinente() != null ? p.getContinente().getNome() : "Nenhum"
            ));
        }
        return lista;
    }

    public String getEstadoJogo() { return estado.name(); }
    public String getFaseAtual() { return faseAtual.name(); }
    public int getTropasRestantes(String j) { return tropasParaDistribuir.getOrDefault(j, 0); }
    public int getTropasReforco(String j) { return tropasReforco.getOrDefault(j, 0); }
    public List<Jogador> getJogadores() { return jogadores; }
    
    public synchronized List<CartaDTO> getCartasDoJogador(String nome) {
        List<CartaDTO> lista = new ArrayList<>();
        for (Jogador j : jogadores) {
            if (j.getNome().equals(nome)) {
                for (Carta c : j.getCartas()) {
                    lista.add(new CartaDTO(c.getPais(), c.getTipo().name()));
                }
            }
        }
        return lista;
    }
    public String getVencedor() {
        return vencedor;
    }
    
    public List<String> getNomesJogadores() {
        List<String> nomes = new ArrayList<>();
        for (Jogador j : jogadores) nomes.add(j.getNome());
        return nomes;
    }

    public void adicionarPais(Pais p) { mapa.add(p); }
    public void adicionarContinente(Continente c) { continentes.add(c); }
    
	 // Método para adicionar ao log e manter apenas as últimas 5-10 mensagens
	 private void registrarAcao(String msg) {
	     logAcoes.add(0, msg); // Adiciona no topo
	     if (logAcoes.size() > 8) logAcoes.remove(8); // Mantém o log curto
	 }
	
	 // Getter para o RMI
	 public List<String> getLogAcoes() {
	     return logAcoes;
	 }
}