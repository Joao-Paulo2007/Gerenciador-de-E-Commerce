package view;

import model.Login;
import model.LoginFunc;
import model.LoginGerente;
import service.CadastroService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Tela inicial: login e cadastro de Funcionário / Gerente.
 * Usa o CadastroService já existente no projeto (em memória).
 */
public class TelaLogin extends JFrame {

    private static final CadastroService CADASTRO_SERVICE = new CadastroService();
    private static boolean usuariosPadraoCriados = false;

    private final JTextField campoNome = new JTextField(16);
    private final JPasswordField campoSenha = new JPasswordField(16);
    private final JTextField campoDataNascimento = new JTextField(10);
    private final JRadioButton opcaoFuncionario = new JRadioButton("Funcionário");
    private final JRadioButton opcaoGerente = new JRadioButton("Gerente");

    public TelaLogin() {
        criarUsuariosPadraoSeNecessario();

        setTitle("Loja Pontual - Acesso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 480);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(construirCabecalho(), BorderLayout.NORTH);
        add(construirFormulario(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel construirCabecalho() {
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(Cores.PRIMARIA);
        cabecalho.setPreferredSize(new Dimension(0, 90));
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Loja Pontual");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Acesse sua conta para continuar");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(0xD5, 0xDE, 0xEA));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        cabecalho.add(Box.createVerticalGlue());
        cabecalho.add(titulo);
        cabecalho.add(subtitulo);
        cabecalho.add(Box.createVerticalGlue());
        return cabecalho;
    }

    private JPanel construirFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Cores.BRANCO);
        painel.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 4, 6, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(opcaoFuncionario);
        grupo.add(opcaoGerente);
        opcaoFuncionario.setSelected(true);
        opcaoFuncionario.setBackground(Color.WHITE);
        opcaoGerente.setBackground(Color.WHITE);

        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelTipo.setBackground(Color.WHITE);
        painelTipo.add(new JLabel("Tipo de acesso (para cadastro):"));
        painel.add(painelTipo, c);

        c.gridy++;
        JPanel painelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelRadios.setBackground(Color.WHITE);
        painelRadios.add(opcaoFuncionario);
        painelRadios.add(opcaoGerente);
        painel.add(painelRadios, c);

        c.gridy++; c.gridwidth = 2;
        painel.add(rotulo("Nome"), c);
        c.gridy++;
        painel.add(campoNome, c);

        c.gridy++;
        painel.add(rotulo("Data de nascimento (dd/mm/aaaa)"), c);
        c.gridy++;
        painel.add(campoDataNascimento, c);

        c.gridy++;
        painel.add(rotulo("Senha"), c);
        c.gridy++;
        painel.add(campoSenha, c);

        c.gridy++;
        c.insets = new Insets(18, 4, 6, 4);
        BotaoEstilizado botaoEntrar = new BotaoEstilizado("Entrar", Cores.PRIMARIA);
        botaoEntrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        botaoEntrar.addActionListener(e -> entrar());
        painel.add(botaoEntrar, c);

        c.gridy++;
        c.insets = new Insets(6, 4, 6, 4);
        BotaoEstilizado botaoCadastrar = new BotaoEstilizado("Criar novo cadastro", Cores.PRIMARIA_CLARA);
        botaoCadastrar.setFont(new Font("SansSerif", Font.BOLD, 12));
        botaoCadastrar.addActionListener(e -> cadastrar());
        painel.add(botaoCadastrar, c);

        c.gridy++;
        JLabel dica = new JLabel("<html><center>Contas de teste:<br>gerente / 1234 &nbsp;|&nbsp; funcionario / 1234</center></html>");
        dica.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dica.setForeground(new Color(0x99, 0x99, 0x99));
        dica.setHorizontalAlignment(SwingConstants.CENTER);
        painel.add(dica, c);

        return painel;
    }

    private JLabel rotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(new Color(0x55, 0x55, 0x55));
        return label;
    }

    private void entrar() {
        String nome = campoNome.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (nome.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe nome e senha.", "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Login login = CADASTRO_SERVICE.autenticar(nome, senha);

        if (login == null) {
            JOptionPane.showMessageDialog(this, "Nome ou senha inválidos.", "Falha no login",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();
        new TelaPrincipal(login);
    }

    private void cadastrar() {
        String nome = campoNome.getText().trim();
        String senha = new String(campoSenha.getPassword());
        String dataTexto = campoDataNascimento.getText().trim();

        if (nome.isEmpty() || senha.isEmpty() || dataTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha nome, data de nascimento e senha para se cadastrar.",
                    "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (CADASTRO_SERVICE.existeUsuario(nome)) {
            JOptionPane.showMessageDialog(this, "Já existe um usuário com esse nome.", "Nome em uso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(dataTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/mm/aaaa.", "Data inválida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (opcaoGerente.isSelected()) {
            LoginGerente gerente = new LoginGerente(nome, dataNascimento, senha);
            boolean sucesso = CADASTRO_SERVICE.cadastrarGerente(gerente);

            if (!sucesso) {
                JOptionPane.showMessageDialog(this, "Limite de gerentes atingido.", "Cadastro não realizado",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Gerente cadastrado! Código de acesso gerado: " + gerente.getCodigoAcesso(),
                    "Cadastro realizado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            LoginFunc funcionario = new LoginFunc(nome, dataNascimento, senha);
            CADASTRO_SERVICE.cadastrarFuncionario(funcionario);

            JOptionPane.showMessageDialog(this,
                    "Funcionário cadastrado! Matrícula gerada: " + funcionario.getCadastro(),
                    "Cadastro realizado", JOptionPane.INFORMATION_MESSAGE);
        }

        campoNome.setText("");
        campoSenha.setText("");
        campoDataNascimento.setText("");
    }

    /** Cria contas de teste na primeira vez que o arquivo de logins ainda não existe. */
    private static void criarUsuariosPadraoSeNecessario() {
        if (usuariosPadraoCriados) {
            return;
        }
        LocalDate dataPadrao = LocalDate.of(1995, 1, 1);

        if (!CADASTRO_SERVICE.existeUsuario("gerente")) {
            CADASTRO_SERVICE.cadastrarGerente(new LoginGerente("gerente", dataPadrao, "1234"));
        }
        if (!CADASTRO_SERVICE.existeUsuario("funcionario")) {
            CADASTRO_SERVICE.cadastrarFuncionario(new LoginFunc("funcionario", dataPadrao, "1234"));
        }
        usuariosPadraoCriados = true;
    }
}
