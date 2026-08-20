package service;

import model.Login;
import model.LoginFunc;
import model.LoginGerente;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LoginRepository {

    private static final String CAMINHO_ARQUIVO = "data/logins.txt";
    private static final String LINHA_SEPARADORA = "===========";
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public List<Login> carregarTodos() {
        List<Login> logins = new ArrayList<>();
        Path caminho = Paths.get(CAMINHO_ARQUIVO);

        if (!Files.exists(caminho)) {
            return logins;
        }

        String secaoAtual = "";

        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(CAMINHO_ARQUIVO), StandardCharsets.UTF_8))) {

            String linha;
            while ((linha = leitor.readLine()) != null) {
                String linhaLimpa = linha.trim();

                if (linhaLimpa.isEmpty() || linhaLimpa.startsWith("=")) {
                    continue;
                }

                if (linhaLimpa.equalsIgnoreCase("Funcionário") || linhaLimpa.equalsIgnoreCase("Funcionario")) {
                    secaoAtual = "FUNCIONARIO";
                    continue;
                }

                if (linhaLimpa.equalsIgnoreCase("Gerente")) {
                    secaoAtual = "GERENTE";
                    continue;
                }

                if (!linhaLimpa.startsWith("N")) {
                    continue;
                }

                try {
                    Login login = interpretarLinha(linhaLimpa, secaoAtual);
                    if (login != null) {
                        logins.add(login);
                    }
                } catch (Exception e) {
                    System.err.println("Linha inválida no arquivo de logins, ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar logins do arquivo: " + e.getMessage());
        }

        return logins;
    }

    private Login interpretarLinha(String linha, String secao) {
        // Formato: N°1/Nome/DataNascimento/Senha
        String[] partes = linha.split("/", 4);

        if (partes.length < 4) {
            return null;
        }

        String nome = partes[1].trim();
        LocalDate dataNascimento = LocalDate.parse(partes[2].trim(), FORMATO_DATA);
        String senha = partes[3].trim();

        if ("GERENTE".equals(secao)) {
            LoginGerente gerente = new LoginGerente(nome, dataNascimento, senha);
            gerente.setCodigoAcesso(GeradorCodigo.gerarCodigo());
            return gerente;
        } else {
            LoginFunc funcionario = new LoginFunc(nome, dataNascimento, senha);
            funcionario.setCadastro(GeradorCadastro.gerarCadastro());
            return funcionario;
        }
    }

    public void salvar(List<Login> logins) {
        List<LoginFunc> funcionarios = new ArrayList<>();
        List<LoginGerente> gerentes = new ArrayList<>();

        for (Login login : logins) {
            if (login instanceof LoginGerente) {
                gerentes.add((LoginGerente) login);
            } else if (login instanceof LoginFunc) {
                funcionarios.add((LoginFunc) login);
            }
        }

        try {
            Path pasta = Paths.get("data");
            if (!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }

            try (BufferedWriter escritor = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(CAMINHO_ARQUIVO), StandardCharsets.UTF_8))) {

                escritor.write(LINHA_SEPARADORA);
                escritor.newLine();
                escritor.write("Funcionário");
                escritor.newLine();
                escritor.write(LINHA_SEPARADORA);
                escritor.newLine();

                int contador = 1;
                for (LoginFunc funcionario : funcionarios) {
                    escritor.write("N°" + contador + "/" + funcionario.getNome() + "/"
                            + funcionario.getDataNascimento().format(FORMATO_DATA) + "/" + funcionario.getSenha());
                    escritor.newLine();
                    contador++;
                }

                escritor.newLine();
                escritor.write(LINHA_SEPARADORA);
                escritor.newLine();
                escritor.write("Gerente");
                escritor.newLine();
                escritor.write(LINHA_SEPARADORA);
                escritor.newLine();

                contador = 1;
                for (LoginGerente gerente : gerentes) {
                    escritor.write("N°" + contador + "/" + gerente.getNome() + "/"
                            + gerente.getDataNascimento().format(FORMATO_DATA) + "/" + gerente.getSenha());
                    escritor.newLine();
                    contador++;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar logins no arquivo: " + e.getMessage());
        }
    }
}
