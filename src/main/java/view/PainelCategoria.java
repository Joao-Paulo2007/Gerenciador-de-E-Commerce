package view;

import Enum.FiltrarProdutos;
import Enum.StatusDoProduto;
import Enum.StatusEntrega;
import model.Produto;
import service.ProdutoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Painel exibido dentro de cada aba (uma categoria, ou "Todos").
 * Mostra os filtros de status/entrega e a grade de cards de produto.
 */
public class PainelCategoria extends JPanel {

    private final FiltrarProdutos categoria; // null = todas as categorias
    private final ProdutoService produtoService;
    private final boolean ehGerente;
    private final Consumer<Produto> aoEditarProduto;
    private final Consumer<Produto> aoExcluirProduto;

    private final JPanel grade = new JPanel(new WrapLayout(FlowLayout.LEFT, 14, 14));
    private final JComboBox<String> comboStatus;
    private final JComboBox<String> comboEntrega;
    private final JLabel rotuloContagem = new JLabel();

    private String termoBuscaAtual = "";

    public PainelCategoria(FiltrarProdutos categoria, ProdutoService produtoService, boolean ehGerente,
                           Consumer<Produto> aoEditarProduto, Consumer<Produto> aoExcluirProduto) {

        this.categoria = categoria;
        this.produtoService = produtoService;
        this.ehGerente = ehGerente;
        this.aoEditarProduto = aoEditarProduto;
        this.aoExcluirProduto = aoExcluirProduto;

        setLayout(new BorderLayout());
        setBackground(Cores.FUNDO);

        // ----- Barra de filtros locais -----
        JPanel barraFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        barraFiltros.setBackground(Cores.FUNDO);

        comboStatus = new JComboBox<>(new String[]{"Todos os status", "À Venda", "Vendido", "Pendente"});
        comboEntrega = new JComboBox<>(new String[]{"Toda entrega", "Não Enviado", "Em Separação", "Enviado",
                "Em Trânsito", "Entregue", "Cancelada"});

        comboStatus.addActionListener(e -> atualizar());
        comboEntrega.addActionListener(e -> atualizar());

        rotuloContagem.setFont(new Font("SansSerif", Font.ITALIC, 12));
        rotuloContagem.setForeground(new Color(0x77, 0x77, 0x77));

        barraFiltros.add(new JLabel("Filtrar por:"));
        barraFiltros.add(comboStatus);
        barraFiltros.add(comboEntrega);
        barraFiltros.add(Box.createHorizontalStrut(12));
        barraFiltros.add(rotuloContagem);

        grade.setBackground(Cores.FUNDO);
        grade.setBorder(BorderFactory.createEmptyBorder(4, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(grade);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(Cores.FUNDO);

        add(barraFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        atualizar();
    }

    public void setTermoBusca(String termo) {
        this.termoBuscaAtual = termo == null ? "" : termo.trim();
        atualizar();
    }

    /** Recarrega a lista de produtos aplicando categoria + busca + filtros locais. */
    public void atualizar() {
        grade.removeAll();

        List<Produto> produtos = termoBuscaAtual.isEmpty()
                ? produtoService.listarTodos()
                : produtoService.buscarPorNome(termoBuscaAtual);

        int totalExibido = 0;

        for (Produto produto : produtos) {

            if (categoria != null && produto.getCategoria() != categoria) {
                continue;
            }

            if (!passaFiltroStatus(produto.getStatus())) {
                continue;
            }

            if (!passaFiltroEntrega(produto.getStatusEntrega())) {
                continue;
            }

            CardProduto card = new CardProduto(
                    produto,
                    ehGerente,
                    () -> aoEditarProduto.accept(produto),
                    () -> aoExcluirProduto.accept(produto)
            );
            grade.add(card);
            totalExibido++;
        }

        if (totalExibido == 0) {
            JLabel vazio = new JLabel("Nenhum produto encontrado.");
            vazio.setFont(new Font("SansSerif", Font.ITALIC, 13));
            vazio.setForeground(new Color(0x99, 0x99, 0x99));
            grade.add(vazio);
        }

        rotuloContagem.setText(totalExibido + " produto(s) encontrado(s)");

        grade.revalidate();
        grade.repaint();
    }

    private boolean passaFiltroStatus(StatusDoProduto status) {
        String selecionado = (String) comboStatus.getSelectedItem();
        if (selecionado == null || selecionado.equals("Todos os status")) {
            return true;
        }
        return Cores.textoStatus(status).equals(selecionado);
    }

    private boolean passaFiltroEntrega(StatusEntrega statusEntrega) {
        String selecionado = (String) comboEntrega.getSelectedItem();
        if (selecionado == null || selecionado.equals("Toda entrega")) {
            return true;
        }
        return Cores.textoEntrega(statusEntrega).equals(selecionado);
    }
}
