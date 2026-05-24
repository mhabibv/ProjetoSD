package client;

// Classe utilitária para cores ANSI no console
public class Cores {
    // Reset/limpeza de formatação
    public static final String RESET = "\u001B[0m";
    // Cores básicas para output no console
    public static final String VERMELHO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARELO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    public static final String ROXO = "\u001B[35m";
    public static final String CIANO = "\u001B[36m";
    // Texto branco com negrito
    public static final String BRANCO_NEGRITO = "\033[1;37m";
}
