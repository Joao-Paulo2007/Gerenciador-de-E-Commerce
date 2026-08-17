package service;

public class GeradorCadastro {
    private static int proximoCadastro = 1;

    public static String gerarCadastro() {

        String cadastro = String.format("%06d", proximoCadastro);
        proximoCadastro++;
        return cadastro;
    }
}
