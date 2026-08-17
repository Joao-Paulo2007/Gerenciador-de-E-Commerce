package model;

import java.time.LocalDate;

public abstract class Login {

    protected String nome;
    protected LocalDate dataNascimento;
    protected String senha;

    public Login(String nome, LocalDate dataNascimento, String senha) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getSenha() {
        return senha;
    }
}