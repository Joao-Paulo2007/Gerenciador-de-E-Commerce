package view;

import model.Produto;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Card visual de um produto, exibido dentro das abas da tela principal.
 */
public class CardProduto extends JPanel {

    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public CardProduto(Produto produto, boolean ehGerente, Runnable aoEditar, Runnable aoExcluir) {

        setLayout(new BorderLayout(8, 8));
        setPreferredSize(new Dimension(230, ehGerente ? 365 : 330));
        setBackground(Cores.BRANCO);

        Color corCategoria = Cores.daCategoria(produto.getCategoria());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 6, 0, 0, corCategoria),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Cores.BORDA, 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10))));

        // ----- Imagem do produto (carregada da URL, se houver) -----
        JLabel rotuloImagem = criarRotuloImagem(produto);

        // ----- Topo: categoria + nome -----
        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));

        JLabel rotuloCategoria = new JLabel(produto.getCategoria().name().toUpperCase());
        rotuloCategoria.setFont(new Font("SansSerif", Font.BOLD, 10));
        rotuloCategoria.setForeground(corCategoria);
        rotuloCategoria.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rotuloNome = new JLabel("<html><b>" + produto.getNome() + "</b></html>");
        rotuloNome.setFont(new Font("SansSerif", Font.PLAIN, 15));
        rotuloNome.setForeground(Cores.TEXTO);
        rotuloNome.setAlignmentX(Component.LEFT_ALIGNMENT);

        topo.add(rotuloImagem);
        topo.add(Box.createVerticalStrut(6));
        topo.add(rotuloCategoria);
        topo.add(Box.createVerticalStrut(3));
        topo.add(rotuloNome);

        // ----- Centro: descrição + preço -----
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        String descricao = produto.getDescricao() == null || produto.getDescricao().isBlank()
                ? "Sem descrição."
                : produto.getDescricao();
        JTextArea areaDescricao = new JTextArea(descricao);
        areaDescricao.setFont(new Font("SansSerif", Font.PLAIN, 11));
        areaDescricao.setForeground(new Color(0x55, 0x55, 0x55));
        areaDescricao.setLineWrap(true);
        areaDescricao.setWrapStyleWord(true);
        areaDescricao.setEditable(false);
        areaDescricao.setOpaque(false);
        areaDescricao.setRows(3);
        areaDescricao.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaDescricao.setMaximumSize(new Dimension(210, 55));

        JLabel rotuloPreco = new JLabel(FORMATO_MOEDA.format(produto.getPreco()));
        rotuloPreco.setFont(new Font("SansSerif", Font.BOLD, 16));
        rotuloPreco.setForeground(Cores.PRIMARIA);
        rotuloPreco.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rotuloEstoque = new JLabel("Estoque: " + produto.getQuantidadeEstoque() + " un.");
        rotuloEstoque.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rotuloEstoque.setForeground(new Color(0x77, 0x77, 0x77));
        rotuloEstoque.setAlignmentX(Component.LEFT_ALIGNMENT);

        centro.add(areaDescricao);
        centro.add(Box.createVerticalStrut(6));
        centro.add(rotuloPreco);
        centro.add(rotuloEstoque);

        // ----- Badges de status -----
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        badges.setOpaque(false);
        badges.add(criarBadge(Cores.textoStatus(produto.getStatus()), Cores.doStatus(produto.getStatus())));
        badges.add(criarBadge(Cores.textoEntrega(produto.getStatusEntrega()), Cores.daEntrega(produto.getStatusEntrega())));

        JPanel meio = new JPanel();
        meio.setOpaque(false);
        meio.setLayout(new BoxLayout(meio, BoxLayout.Y_AXIS));
        meio.add(centro);
        meio.add(badges);

        add(topo, BorderLayout.NORTH);
        add(meio, BorderLayout.CENTER);

        // ----- Ações (somente gerente) -----
        if (ehGerente) {
            JPanel acoes = new JPanel(new GridLayout(1, 2, 6, 0));
            acoes.setOpaque(false);

            BotaoEstilizado botaoEditar = new BotaoEstilizado("Editar", Cores.PRIMARIA_CLARA);
            botaoEditar.setFont(new Font("SansSerif", Font.BOLD, 11));
            botaoEditar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            botaoEditar.addActionListener(e -> aoEditar.run());

            BotaoEstilizado botaoExcluir = new BotaoEstilizado("Excluir", new Color(0xC0, 0x39, 0x2B));
            botaoExcluir.setFont(new Font("SansSerif", Font.BOLD, 11));
            botaoExcluir.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            botaoExcluir.addActionListener(e -> aoExcluir.run());

            acoes.add(botaoEditar);
            acoes.add(botaoExcluir);
            add(acoes, BorderLayout.SOUTH);
        }
    }

    private JLabel criarRotuloImagem(Produto produto) {
        JLabel rotuloImagem = new JLabel();
        rotuloImagem.setHorizontalAlignment(SwingConstants.CENTER);
        rotuloImagem.setAlignmentX(Component.LEFT_ALIGNMENT);
        rotuloImagem.setPreferredSize(new Dimension(206, 100));
        rotuloImagem.setMaximumSize(new Dimension(206, 100));
        rotuloImagem.setOpaque(true);
        rotuloImagem.setBackground(new Color(0xF0, 0xF2, 0xF5));
        rotuloImagem.setBorder(BorderFactory.createLineBorder(Cores.BORDA, 1));
        rotuloImagem.setFont(new Font("SansSerif", Font.ITALIC, 11));
        rotuloImagem.setForeground(new Color(0xAA, 0xAA, 0xAA));

        String url = produto.getImagemUrl();

        if (url == null || url.isBlank()) {
            rotuloImagem.setText("Sem imagem");
        } else {
            rotuloImagem.setText("Carregando imagem...");
            carregarImagemAssincrona(url, rotuloImagem);
        }

        return rotuloImagem;
    }

    private void carregarImagemAssincrona(String url, JLabel rotuloImagem) {
        SwingWorker<ImageIcon, Void> tarefa = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    Image imagem = new ImageIcon(new URL(url)).getImage();
                    Image imagemRedimensionada = imagem.getScaledInstance(206, 100, Image.SCALE_SMOOTH);
                    return new ImageIcon(imagemRedimensionada);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icone = get();
                    if (icone != null) {
                        rotuloImagem.setIcon(icone);
                        rotuloImagem.setText(null);
                    } else {
                        rotuloImagem.setText("Imagem indisponível");
                    }
                } catch (Exception e) {
                    rotuloImagem.setText("Imagem indisponível");
                }
            }
        };
        tarefa.execute();
    }

    private JLabel criarBadge(String texto, Color cor) {
        JLabel badge = new JLabel(texto);
        badge.setOpaque(true);
        badge.setBackground(cor);
        badge.setForeground(Cores.corDeTextoLegivel(cor));
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setBorder(BorderFactory.createEmptyBorder(2, 7, 2, 7));
        return badge;
    }

}
