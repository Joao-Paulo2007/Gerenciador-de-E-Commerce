package service;

import java.util.Random;

public class GeradorCodigo {

    private static final String L = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random random = new Random();

    public static String gerarCodigo() {

        //Classe usada para criar o código
        StringBuilder codigo = new StringBuilder();

        //Gera 5 letras
        for (int i = 0; i < 5; i++) {
            int indice = random.nextInt(L.length());
            codigo.append(L.charAt(indice));
        }

        //Gera 5 números
        for (int i = 0; i < 5; i++) {
            codigo.append(random.nextInt(10));
        }

        //Converte o código em uma String legível
        return codigo.toString();
    }
}