package model;

import java.time.LocalDate;

public class LoginGerente extends Login {

    private String codigoAcesso;

    public LoginGerente(String nome, LocalDate dataNascimento, String senha) {
        super(nome, dataNascimento, senha);
    }

    public String getCodigoAcesso() {
        return codigoAcesso;
    }

    public void setCodigoAcesso(String codigoAcesso) {
        this.codigoAcesso = codigoAcesso;
    }
}