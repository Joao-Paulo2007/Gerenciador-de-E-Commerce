package view;

import Enum.FiltrarProdutos;
import Enum.StatusDoProduto;
import Enum.StatusEntrega;
import model.Produto;
import service.ProdutoService;

import javax.swing.*;
import java.awt.*;

/**
 * Janela de cadastro e edição de produto. Uso exclusivo do Gerente.
 */
public class InfoProduto extends JDialog {

    private final JTextField campoNome = new JTextField(18);
    private final JComboBox<FiltrarProdutos> comboCategoria = new JComboBox<>(FiltrarProdutos.values());
    private final JTextField campoPreco = new JTextField(8);
    private final JTextField campoQuantidade = new JTextField(6);
    private final JComboBox<StatusDoProduto> comboStatus = new JComboBox<>(StatusDoProduto.values());
    private final JComboBox<StatusEntrega> comboEntrega = new JComboBox<>(StatusEntrega.values());
    private final JTextField campoImagemUrl = new JTextField(18);
    private final JTextArea campoDescricao = new JTextArea(4, 20);

    private final ProdutoService produtoService;
    private final Produto produtoExistente; // null quando é um cadastro novo
    private final Runnable aoSalvar;

    public InfoProduto(Frame dono, ProdutoService produtoService, Produto produtoExistente, Runnable aoSalvar) {
        super(dono, produtoExistente == null ? "Novo Produto" : "Editar Produto", true);

        this.produtoService = produtoService;
        this.produtoExistente = produtoExistente;
        this.aoSalvar = aoSalvar;

        setLayout(new BorderLayout());
        setSize(440, 560);
        setLocationRelativeTo(dono);
        setResizable(false);

        add(construirCabecalho(), BorderLayout.NORTH);
        add(construirFormulario(), BorderLayout.CENTER);
        add(construirRodape(), BorderLayout.SOUTH);

        if (produtoExistente != null) {
            preencherComProdutoExistente();
        }
    }

    private JPanel construirCabecalho() {
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(Cores.PRIMARIA);
        cabecalho.setPreferredSize(new Dimension(0, 50));
        JLabel titulo = new JLabel(produtoExistente == null ? "Cadastrar novo produto" : "Editar produto");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        cabecalho.add(titulo);
        return cabecalho;
    }

    private JPanel construirFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Cores.BRANCO);
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 4, 6, 4);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;

        c.gridx = 0; c.gridy = linha; c.gridwidth = 2;
        formulario.add(rotulo("Nome do produto"), c);
        linha++;
        c.gridx = 0; c.gridy = linha; c.gridwidth = 2;
        formulario.add(campoNome, c);
        linha++;

        c.gridx = 0; c.gridy = linha; c.gridwidth = 1;
        formulario.add(rotulo("Categoria"), c);
        c.gridx = 1;
        formulario.add(rotulo("Preço (R$)"), c);
        linha++;
        c.gridx = 0; c.gridy = linha;
        formulario.add(comboCategoria, c);
        c.gridx = 1;
        formulario.add(campoPreco, c);
        linha++;

        c.gridx = 0; c.gridy = linha; c.gridwidth = 1;
        formulario.add(rotulo("Quantidade em estoque"), c);
        linha++;
        c.gridx = 0; c.gridy = linha;
        formulario.add(campoQuantidade, c);
        linha++;

        c.gridx = 0; c.gridy = linha; c.gridwidth = 2;
        formulario.add(rotulo("URL da imagem do produto (opcional)"), c);
        linha++;
        c.gridx = 0; c.gridy = linha; c.gridwidth = 2;
        formulario.add(campoImagemUrl, c);
        linha++;

        if (produtoExistente != null) {
            // Status e entrega só podem ser alterados na edição de um produto já existente.
            c.gridx = 0; c.gridy = linha; c.gridwidth = 1;
            formulario.add(rotulo("Status do produto"), c);
            c.gridx = 1;
            formulario.add(rotulo("Status da entrega"), c);
            linha++;
            c.gridx = 0; c.gridy = linha;
            formulario.add(comboStatus, c);
            c.gridx = 1;
            formulario.add(comboEntrega, c);
            linha++;
        } else {
            c.gridx = 0; c.gridy = linha; c.gridwidth = 2;
            JLabel avisoPadrao = new JLabel(
                    "<html>Todo produto novo entra automaticamente como <b>À Venda</b> "
                            + "e <b>Em Separação</b>. Isso só pode ser alterado depois, editando o produto.</html>");
            avisoPadrao.setFont(new Font("SansSerif", Font.ITALIC, 11));
            avisoPadrao.setForeground(new Color(0x88, 0x88, 0x88));
            formulario.add(avisoPadrao, c);
            linha++;
        }

        c.gridx = 0; c.gridy = linha; c.gridwidth = 2;
        formulario.add(rotulo("Descrição"), c);
        linha++;
        c.gridx = 0; c.gridy = linha; c.gridwidth = 2; c.fill = GridBagConstraints.BOTH; c.weighty = 1;
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        formulario.add(scrollDescricao, c);

        return formulario;
    }

    private JLabel rotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(new Color(0x44, 0x44, 0x44));
        return label;
    }

    private JPanel construirRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rodape.setBackground(Cores.BRANCO);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setFocusPainted(false);
        botaoCancelar.addActionListener(e -> dispose());

        BotaoEstilizado botaoSalvar = new BotaoEstilizado(
                produtoExistente == null ? "Cadastrar" : "Salvar alterações", Cores.PRIMARIA);
        botaoSalvar.addActionListener(e -> salvar());

        rodape.add(botaoCancelar);
        rodape.add(botaoSalvar);
        return rodape;
    }

    private void preencherComProdutoExistente() {
        campoNome.setText(produtoExistente.getNome());
        comboCategoria.setSelectedItem(produtoExistente.getCategoria());
        campoPreco.setText(String.valueOf(produtoExistente.getPreco()));
        campoQuantidade.setText(String.valueOf(produtoExistente.getQuantidadeEstoque()));
        campoImagemUrl.setText(produtoExistente.getImagemUrl());
        comboStatus.setSelectedItem(produtoExistente.getStatus());
        comboEntrega.setSelectedItem(produtoExistente.getStatusEntrega());
        campoDescricao.setText(produtoExistente.getDescricao());
    }

    private void salvar() {
        String nome = campoNome.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do produto.", "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double preco;
        int quantidade;

        try {
            preco = Double.parseDouble(campoPreco.getText().trim().replace(",", "."));
            if (preco < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um preço válido (ex: 49.90).", "Valor inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
            if (quantidade < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe uma quantidade válida (ex: 10).", "Valor inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        FiltrarProdutos categoria = (FiltrarProdutos) comboCategoria.getSelectedItem();
        String descricao = campoDescricao.getText().trim();
        String imagemUrl = campoImagemUrl.getText().trim();

        if (produtoExistente == null) {
            // Todo produto novo nasce como À Venda / Em Separação, de forma fixa.
            produtoService.adicionar(nome, categoria, preco, quantidade, descricao,
                    StatusDoProduto.AVenda, StatusEntrega.EmSeparacao, imagemUrl);
        } else {
            StatusDoProduto status = (StatusDoProduto) comboStatus.getSelectedItem();
            StatusEntrega statusEntrega = (StatusEntrega) comboEntrega.getSelectedItem();

            produtoExistente.setNome(nome);
            produtoExistente.setCategoria(categoria);
            produtoExistente.setPreco(preco);
            produtoExistente.setQuantidadeEstoque(quantidade);
            produtoExistente.setStatus(status);
            produtoExistente.setStatusEntrega(statusEntrega);
            produtoExistente.setImagemUrl(imagemUrl);
            produtoExistente.setDescricao(descricao);
            produtoService.editar(produtoExistente);
        }

        aoSalvar.run();
        dispose();
    }
}
