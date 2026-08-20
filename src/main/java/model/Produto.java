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
    private String imagemUrl;

    public Produto(String codigo, String nome, FiltrarProdutos categoria, double preco,
                   int quantidadeEstoque, String descricao, StatusDoProduto status,
                   StatusEntrega statusEntrega, String imagemUrl) {
        this.codigo = codigo;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.descricao = descricao;
        this.status = status;
        this.statusEntrega = statusEntrega;
        this.imagemUrl = imagemUrl;
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

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    /**
     * Converte o produto em uma linha de texto para salvar no arquivo .txt
     * Formato: codigo|nome|categoria|preco|quantidade|status|statusEntrega|imagemUrl|descricao
     */
    public String paraLinhaArquivo() {
        String descricaoSegura = descricao == null ? "" : descricao.replace("\n", " ").replace("|", "/");
        String imagemUrlSegura = imagemUrl == null ? "" : imagemUrl.replace("|", "/");
        return codigo + "|" + nome + "|" + categoria.name() + "|" + preco + "|" + quantidadeEstoque
                + "|" + status.name() + "|" + statusEntrega.name() + "|" + imagemUrlSegura + "|" + descricaoSegura;
    }

    /**
     * Reconstrói um Produto a partir de uma linha salva no arquivo .txt
     * Aceita tanto o formato novo (com imagemUrl) quanto o formato antigo (sem imagemUrl),
     * para não quebrar arquivos de produtos já existentes.
     */
    public static Produto deLinhaArquivo(String linha) {
        String[] campos = linha.split("\\|", 9);

        if (campos.length < 8) {
            return null;
        }

        String codigo = campos[0];
        String nome = campos[1];
        FiltrarProdutos categoria = FiltrarProdutos.valueOf(campos[2]);
        double preco = Double.parseDouble(campos[3]);
        int quantidade = Integer.parseInt(campos[4]);
        StatusDoProduto status = StatusDoProduto.valueOf(campos[5]);
        StatusEntrega statusEntrega = StatusEntrega.valueOf(campos[6]);

        String imagemUrl;
        String descricao;

        if (campos.length == 9) {
            imagemUrl = campos[7];
            descricao = campos[8];
        } else {
            // Arquivo salvo antes de existir o campo de imagem
            imagemUrl = "";
            descricao = campos[7];
        }

        return new Produto(codigo, nome, categoria, preco, quantidade, descricao, status, statusEntrega, imagemUrl);
    }
}
