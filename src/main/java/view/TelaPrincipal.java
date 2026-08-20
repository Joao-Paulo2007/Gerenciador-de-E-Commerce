package view;

import Enum.FiltrarProdutos;
import model.Login;
import model.LoginGerente;
import model.Produto;
import service.ProdutoService;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela principal da Loja Pontual: cabeçalho, busca e abas coloridas por categoria.
 */
public class TelaPrincipal extends JFrame {

    private final Login usuarioLogado;
    private final boolean ehGerente;
    private final ProdutoService produtoService = new ProdutoService();

    private final JTabbedPane abas = new JTabbedPane();
    private final List<PainelCategoria> paineis = new ArrayList<>();
    private final JTextField campoBusca = new JTextField(22);

    public TelaPrincipal(Login usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.ehGerente = usuarioLogado instanceof LoginGerente;

        setTitle("Loja Pontual");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(construirCabecalho(), BorderLayout.NORTH);
        add(construirAbas(), BorderLayout.CENTER);

        setVisible(true);
    }

    // ---------------------------------------------------------------
    // Cabeçalho: marca, boas-vindas, busca e ação de novo produto
    // ---------------------------------------------------------------
    private JPanel construirCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Cores.PRIMARIA);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel marca = new JLabel("Loja Pontual");
        marca.setFont(new Font("SansSerif", Font.BOLD, 24));
        marca.setForeground(Color.WHITE);

        String papel = ehGerente ? "Gerente" : "Funcionário";
        JLabel boasVindas = new JLabel("Olá, " + usuarioLogado.getNome() + "  ·  " + papel);
        boasVindas.setFont(new Font("SansSerif", Font.PLAIN, 12));
        boasVindas.setForeground(new Color(0xD5, 0xDE, 0xEA));

        JPanel ladoEsquerdo = new JPanel();
        ladoEsquerdo.setOpaque(false);
        ladoEsquerdo.setLayout(new BoxLayout(ladoEsquerdo, BoxLayout.Y_AXIS));
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);
        boasVindas.setAlignmentX(Component.LEFT_ALIGNMENT);
        ladoEsquerdo.add(marca);
        ladoEsquerdo.add(boasVindas);

        JPanel ladoDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        ladoDireito.setOpaque(false);

        campoBusca.putClientProperty("JTextField.placeholderText", "Buscar produtos...");
        campoBusca.addActionListener(e -> aplicarBusca());

        BotaoEstilizado botaoBuscar = new BotaoEstilizado("Buscar", Cores.PRIMARIA_CLARA);
        botaoBuscar.addActionListener(e -> aplicarBusca());

        BotaoEstilizado botaoLimpar = new BotaoEstilizado("Limpar", new Color(0x5D, 0x6D, 0x7E));
        botaoLimpar.addActionListener(e -> {
            campoBusca.setText("");
            aplicarBusca();
        });

        ladoDireito.add(campoBusca);
        ladoDireito.add(botaoBuscar);
        ladoDireito.add(botaoLimpar);

        if (ehGerente) {
            BotaoEstilizado botaoNovoProduto = new BotaoEstilizado("+ Novo Produto", new Color(0x27, 0xAE, 0x60));
            botaoNovoProduto.addActionListener(e -> abrirDialogoNovoProduto());
            ladoDireito.add(botaoNovoProduto);
        }

        BotaoEstilizado botaoSair = new BotaoEstilizado("Sair", new Color(0xC0, 0x39, 0x2B));
        botaoSair.addActionListener(e -> {
            dispose();
            new TelaLogin();
        });
        ladoDireito.add(botaoSair);

        cabecalho.add(ladoEsquerdo, BorderLayout.WEST);
        cabecalho.add(ladoDireito, BorderLayout.EAST);
        return cabecalho;
    }

    // ---------------------------------------------------------------
    // Abas: "Todos" + uma por categoria do enum FiltrarProdutos, coloridas
    // ---------------------------------------------------------------
    private JTabbedPane construirAbas() {
        abas.setUI(new BasicTabbedPaneUI());
        abas.setFont(new Font("SansSerif", Font.BOLD, 13));
        abas.setBackground(Cores.FUNDO);

        adicionarAba("Todos", null, Cores.PRIMARIA);

        for (FiltrarProdutos categoria : FiltrarProdutos.values()) {
            adicionarAba(categoria.name(), categoria, Cores.daCategoria(categoria));
        }

        return abas;
    }

    private void adicionarAba(String titulo, FiltrarProdutos categoria, Color cor) {
        PainelCategoria painel = new PainelCategoria(
                categoria,
                produtoService,
                ehGerente,
                this::abrirDialogoEditarProduto,
                this::confirmarExclusao
        );
        paineis.add(painel);

        abas.addTab(titulo, painel);
        int indice = abas.getTabCount() - 1;

        JLabel tabLabel = new JLabel(titulo);
        tabLabel.setOpaque(true);
        tabLabel.setBackground(cor);
        tabLabel.setForeground(Cores.corDeTextoLegivel(cor));
        tabLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabLabel.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        abas.setTabComponentAt(indice, tabLabel);
    }

    // ---------------------------------------------------------------
    // Ações
    // ---------------------------------------------------------------
    private void aplicarBusca() {
        String termo = campoBusca.getText();
        for (PainelCategoria painel : paineis) {
            painel.setTermoBusca(termo);
        }
    }

    private void atualizarTodosPaineis() {
        for (PainelCategoria painel : paineis) {
            painel.atualizar();
        }
    }

    private void abrirDialogoNovoProduto() {
        new InfoProduto(this, produtoService, null, this::atualizarTodosPaineis).setVisible(true);
    }

    private void abrirDialogoEditarProduto(Produto produto) {
        if (!ehGerente) {
            return;
        }
        new InfoProduto(this, produtoService, produto, this::atualizarTodosPaineis).setVisible(true);
    }

    private void confirmarExclusao(Produto produto) {
        if (!ehGerente) {
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this,
                "Excluir o produto \"" + produto.getNome() + "\"?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            produtoService.remover(produto.getCodigo());
            atualizarTodosPaineis();
        }
    }
}
