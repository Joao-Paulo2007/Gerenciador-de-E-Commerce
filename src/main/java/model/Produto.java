package model;

import Enum.FiltrarProdutos;
import Enum.StatusDoProduto;
import Enum.StatusEntrega;

public class Produto {

    private String codigo;
    private String nome;
    private FiltrarProdutos categoria;
    private double preco;
    private int quantidadeEstoque;
    private String descricao;
    private StatusDoProduto status;
    private StatusEntrega statusEntrega;

    public Produto(String codigo, String nome, FiltrarProdutos categoria, double preco,
                   int quantidadeEstoque, String descricao, StatusDoProduto status,
                   StatusEntrega statusEntrega) {
        this.codigo = codigo;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.descricao = descricao;
        this.status = status;
        this.statusEntrega = statusEntrega;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public FiltrarProdutos getCategoria() {
        return categoria;
    }

    public void setCategoria(FiltrarProdutos categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusDoProduto getStatus() {
        return status;
    }

    public void setStatus(StatusDoProduto status) {
        this.status = status;
    }

    public StatusEntrega getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(StatusEntrega statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

    public String salvarArquivo(){
        String descricaoSegura = descricao == null ? "" : descricao.replace("\n", " ").replace("|", "/");
        return codigo + "|" + nome + "|" + categoria.name() + "|" + preco + "|" + quantidadeEstoque + "|" + status.name() + "|" + statusEntrega.name() + "|" + descricaoSegura;
    }

    public static Produto ReloadArquivo(String linha){
        String[] campos = linha.split("\\|", 8);

        if (campos.length < 8){
            return null;
        }

        String codigo = campos[0];
        String nome = campos[1];
        FiltrarProdutos categoria = FiltrarProdutos.valueOf(campos[2]);
        double preco = Double.parseDouble(campos[3]);
        int quantidade = Integer.parseInt(campos[4]);
        StatusDoProduto status = StatusDoProduto.valueOf(campos[5]);
        StatusEntrega statusEntrega = StatusEntrega.valueOf(campos[6]);
        String descricao = campos[7];

        return new Produto(codigo, nome, categoria, preco, quantidade, descricao, status, statusEntrega);
    }

}