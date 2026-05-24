# 🎀 Risk Hello Kitty - Sistemas Distribuídos (RMI)

Este projeto é uma implementação do jogo de estratégia **War/Risk** com a temática da **Hello Kitty**, desenvolvido como parte da disciplina de Sistemas Distribuídos. O jogo utiliza **Java RMI** para comunicação em rede, permitindo partidas multijogador em uma arquitetura cliente-servidor.

## 🗺️ O Mapa do Jogo

O jogo passa-se num mundo mágico dividido em dois continentes:

* **Reino Doce:** Vila do Morango, Castelo de Chocolate e Ponte Colorida.
* **Domínio Celestial:** Campo Brilhante, Ilha Flutuante e Torre Celestial.

## 📁 Estrutura do Código (`src/`)

A organização dos pacotes segue as melhores práticas de separação de responsabilidades:

* **`client`**: Contém a lógica da interface do utilizador (`Cliente.java`) e formatação visual (`Cores.java`).
* **`game`**: Contém a lógica central das regras do jogo (`Jogo.java`).
* **`model`**: Classes base que representam as entidades do jogo (`Carta.java`, `Continente.java`, `Jogador.java`, `Pais.java`).
* **`remote`**: Interfaces RMI e DTOs (Data Transfer Objects) para comunicação entre processos.
* `JogoRemote.java`: Interface que define os métodos disponíveis remotamente.
* `JogoRemoteImpl.java`: Implementação dos métodos do lado do servidor.
* `PaisDTO.java` / `CartaDTO.java`: Versões leves das classes para transporte de dados via rede.


* **`server`**: Classe principal que inicializa o servidor RMI (`Servidor.java`).

## 🛠️ Tecnologias e Requisitos

* **Java JDK 21**
* **Java RMI** (Remote Method Invocation)

## 🚀 Como Executar

### 1. Compilação

No terminal, na raiz do projeto:

```bash
javac -d bin src/**/*.java

```

### 2. Iniciar o Servidor

```bash
java -cp bin server.Servidor

```

### 3. Iniciar os Clientes

Abra um novo terminal para cada jogador:

```bash
java -cp bin client.Cliente

```

## 📜 Regras Implementadas

* **Distribuição de Tropas:** Recebimento de tropas por continente dominado e troca de cartas.
* **Ataque:** Sistema de combate via dados com conquista de territórios.
* **Movimentação:** Remanejamento estratégico de tropas entre países vizinhos.
* **Log de Ações:** Histórico em tempo real para todos os jogadores acompanharem a partida.

---
