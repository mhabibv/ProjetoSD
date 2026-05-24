package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import game.Jogo;
import model.*;
import remote.JogoRemote;
import remote.JogoRemoteImpl;

public class Servidor {

    public static void main(String[] args) {
        try {

            Jogo jogo = new Jogo();

            // =========================
            // PAÍSES
            // =========================
            Pais p1 = new Pais("Vila do Morango");
            Pais p2 = new Pais("Castelo de Chocolate");
            Pais p3 = new Pais("Ponte Colorida");
            Pais p4 = new Pais("Campo Brilhante");
            Pais p5 = new Pais("Ilha Flutuante");
            Pais p6 = new Pais("Torre Celestial");

            jogo.adicionarPais(p1);
            jogo.adicionarPais(p2);
            jogo.adicionarPais(p3);
            jogo.adicionarPais(p4);
            jogo.adicionarPais(p5);
            jogo.adicionarPais(p6);

            // =========================
            // VIZINHOS
            // =========================
            p1.adicionarVizinho(p2);
            p2.adicionarVizinho(p1);

            p1.adicionarVizinho(p3);
            p3.adicionarVizinho(p1);

            p2.adicionarVizinho(p4);
            p4.adicionarVizinho(p2);

            p3.adicionarVizinho(p4);
            p4.adicionarVizinho(p3);

            p4.adicionarVizinho(p5);
            p5.adicionarVizinho(p4);

            p5.adicionarVizinho(p6);
            p6.adicionarVizinho(p5);

            // =========================
            // CONTINENTES 
            // =========================
            Continente c1 = new Continente("Reino Doce", 3);
            c1.adicionarPais(p1);
            c1.adicionarPais(p2);
            c1.adicionarPais(p3);

            Continente c2 = new Continente("Domínio Celestial", 2);
            c2.adicionarPais(p4);
            c2.adicionarPais(p5);
            c2.adicionarPais(p6);

            jogo.adicionarContinente(c1);
            jogo.adicionarContinente(c2);

            // =========================
            // RMI
            // =========================

            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("Registry criado na porta 1099");
            } catch (Exception e) {
                System.out.println("Registry já estava rodando.");
            }

            JogoRemote stub = new JogoRemoteImpl(jogo);

            Naming.rebind("rmi://localhost/Jogo", stub);

            System.out.println("Servidor pronto!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}