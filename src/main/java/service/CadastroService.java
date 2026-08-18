package service;

import model.Login;
import model.LoginGerente;
import model.LoginFunc;

import java.util.ArrayList;
import java.util.List;

public class CadastroService {

    private List<Login> logins = new ArrayList<>();

    private static final int LimiteGerentes = 3;

    public boolean cadastrarFuncionario(LoginFunc funcionario) {

        funcionario.setCadastro(GeradorCadastro.gerarCadastro());

        logins.add(funcionario);

        return true;
    }

    public boolean cadastrarGerente(LoginGerente gerente) {

        if (quantidadeGerentes() >= LimiteGerentes) {
            return false;
        }

        gerente.setCodigoAcesso(GeradorCodigo.gerarCodigo());

        logins.add(gerente);

        return true;
    }

    private int quantidadeGerentes() {

        int quantidade = 0;

        for (Login login : logins) {

            if (login instanceof LoginGerente) {
                quantidade++;
            }
        }

        return quantidade;
    }

    //Verifica o nome e a senha, retorna o login correspondente, retorna null se não encontrar nenhuma combinação válida.
    public Login autenticar(String nome, String senha){

        for (Login login : logins){
            if (login.getNome().equalsIgnoreCase(nome) && login.getSenha().equals(senha)){
                return login;
            }
        }
        return  null;
    }

    public boolean existeUsuario(String nome){

        for (Login login : logins){
            if(login.getNome().equalsIgnoreCase(nome)){
                return true;
            }
        }
        return false;
    }
}