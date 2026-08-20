package service;

import Enum.FiltrarProdutos;
import Enum.StatusDoProduto;
import Enum.StatusEntrega;
import model.Produto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por todo o CRUD de produtos e por persistir os dados
 * em um arquivo .txt (banco de dados simples em texto).
 */
public class ProdutoService {

    private static final String CAMINHO_ARQUIVO = "data/produtos.txt";

    private final List<Produto> produtos = new ArrayList<>();
    private String imagemUrl;

    public ProdutoService() {
        carregar();
    }

    // ---------- CRUD ----------

    public Produto adicionar(String nome, FiltrarProdutos categoria, double preco, int quantidade,
                             String descricao, StatusDoProduto status, StatusEntrega statusEntrega, String imagemUrl) {

        String codigo = GeradorCodigoProduto.gerarCodigo();
        Produto produto = new Produto(codigo, nome, categoria, preco, quantidade, descricao, status,
                statusEntrega, this.imagemUrl);
        produtos.add(produto);
        salvar();
        return produto;
    }

    public boolean editar(Produto produtoEditado) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getCodigo().equals(produtoEditado.getCodigo())) {
                produtos.set(i, produtoEditado);
                salvar();
                return true;
            }
        }
        return false;
    }

    public boolean remover(String codigo) {
        boolean removeu = produtos.removeIf(p -> p.getCodigo().equals(codigo));
        if (removeu) {
            salvar();
        }
        return removeu;
    }

    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos);
    }

    public List<Produto> filtrarPorCategoria(FiltrarProdutos categoria) {
        List<Produto> resultado = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getCategoria() == categoria) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Produto> buscarPorNome(String termo) {
        List<Produto> resultado = new ArrayList<>();
        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }
        String termoBusca = termo.toLowerCase();
        for (Produto p : produtos) {
            if (p.getNome().toLowerCase().contains(termoBusca)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // ---------- Persistência em .txt ----------

    private void salvar() {
        try {
            Path pasta = Paths.get("data");
            if (!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }

            try (BufferedWriter escritor = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(CAMINHO_ARQUIVO), StandardCharsets.UTF_8))) {

                for (Produto produto : produtos) {
                    escritor.write(produto.paraLinhaArquivo());
                    escritor.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos no arquivo: " + e.getMessage());
        }
    }

    private void carregar() {
        Path caminho = Paths.get(CAMINHO_ARQUIVO);

        if (!Files.exists(caminho)) {
            return;
        }

        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(CAMINHO_ARQUIVO), StandardCharsets.UTF_8))) {

            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                try {
                    Produto produto = Produto.deLinhaArquivo(linha);
                    if (produto != null) {
                        produtos.add(produto);
                        GeradorCodigoProduto.ajustarProximoCodigo(produto.getCodigo());
                    }
                } catch (Exception e) {
                    System.err.println("Linha inválida no arquivo de produtos, ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar produtos do arquivo: " + e.getMessage());
        }
    }
}
