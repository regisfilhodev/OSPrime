package br.com.os.view;

import br.com.os.dao.UsuarioDAO;
import br.com.os.model.Usuario;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class TelaFormUsuario extends JDialog {

    private JTextField txtNome, txtMatricula;
    private JPasswordField txtSenha, txtConfirmarSenha;
    private JButton btnSalvar, btnCancelar;
    private JLabel lblAvisoSenha;

    private final UsuarioDAO dao = new UsuarioDAO();
    private final Usuario usuarioExistente;
    private final Runnable aoSalvar;

    public TelaFormUsuario(JFrame parent, Usuario usuarioExistente, Runnable aoSalvar) {
        super(parent, true);
        this.usuarioExistente = usuarioExistente;
        this.aoSalvar = aoSalvar;

        setTitle(usuarioExistente == null ? "Novo Usuário" : "Editar Usuário");
        setSize(420, 480);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Cores.FUNDO);

        initComponents();

        if (usuarioExistente != null) {
            txtNome.setText(usuarioExistente.getNome());
            txtMatricula.setText(String.valueOf(usuarioExistente.getMatricula()));
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Cores.AZUL_ESCURO);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitulo = new JLabel("  " + (usuarioExistente == null ? "Novo Usuário" : "Editar Usuário"));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        add(cabecalho, BorderLayout.NORTH);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Cores.FUNDO);
        painel.setBorder(new EmptyBorder(20, 25, 10, 25));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 0, 6, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 1;

        txtNome = new JTextField();
        txtMatricula = new JTextField();
        txtSenha = new JPasswordField();
        txtConfirmarSenha = new JPasswordField();

        int row = 0;
        row = addCampo(painel, c, row, "Nome", txtNome);
        row = addCampo(painel, c, row, "Matrícula", txtMatricula);

        String rotuloSenha = (usuarioExistente == null) ? "Senha" : "Nova Senha (deixe em branco pra manter)";
        row = addCampo(painel, c, row, rotuloSenha, txtSenha);
        addCampo(painel, c, row, "Confirmar Senha", txtConfirmarSenha);

        add(painel, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        painelBotoes.setBackground(Cores.FUNDO);
        painelBotoes.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Cores.CINZA_CLARO));

        btnCancelar = new JButton("Cancelar");
        btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(Cores.AZUL_ESCURO);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSalvar.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnSalvar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private int addCampo(JPanel painel, GridBagConstraints c, int row, String rotulo, JComponent campo) {
        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(Cores.CINZA_ESCURO);

        c.gridy = row;
        painel.add(lbl, c);
        c.gridy = row + 1;
        painel.add(campo, c);
        return row + 2;
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String matriculaTexto = txtMatricula.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        if (nome.isEmpty() || matriculaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha nome e matrícula!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int matricula;
        try {
            matricula = Integer.parseInt(matriculaTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Matrícula deve ser um número!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Senha obrigatória só na criação; na edição, só valida se foi preenchida
        boolean vaiTrocarSenha = usuarioExistente == null || !senha.isEmpty();
        if (vaiTrocarSenha) {
            if (senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a senha!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!senha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            Integer idAtual = (usuarioExistente != null) ? usuarioExistente.getId() : null;
            if (dao.matriculaExiste(matricula, idAtual)) {
                JOptionPane.showMessageDialog(this, "Já existe um usuário com essa matrícula!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (usuarioExistente == null) {
                dao.cadastrar(nome, matricula, senha);
                JOptionPane.showMessageDialog(this, "Usuário criado com sucesso!");
            } else {
                Usuario u = usuarioExistente;
                u.setNome(nome);
                u.setMatricula(matricula);
                dao.atualizarDados(u);
                if (vaiTrocarSenha) {
                    dao.redefinirSenha(u.getId(), senha);
                }
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
            }

            aoSalvar.run();
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}