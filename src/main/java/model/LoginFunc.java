package model;

import java.time.LocalDate;

public class LoginFunc extends Login {

    private String cadastro;

    public LoginFunc(String nome, LocalDate dataNascimento, String senha) {
        super(nome, dataNascimento, senha);

    }

    public String getCadastro(){
        return cadastro;
    }

    public void setCadastro(String cadastro) {
        this.cadastro = cadastro;
    }
}