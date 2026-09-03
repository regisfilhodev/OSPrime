package br.com.os.view;

import br.com.os.dao.UsuarioDAO;
import br.com.os.model.Usuario;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class TelaUsuarios extends JFrame {

    private JButton btnNovo, btnEditar, btnExcluir;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final UsuarioDAO dao = new UsuarioDAO();

    public TelaUsuarios() {
        setTitle("Gerenciar Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Cores.FUNDO);

        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Cores.AZUL_ESCURO);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitulo = new JLabel("  Gerenciar Usuários");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        add(cabecalho, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(10, 10));
        corpo.setBackground(Cores.FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelTopo.setBackground(Cores.FUNDO);
        btnNovo = new JButton("+ Novo Usuário");
        estilizarBotaoPrimario(btnNovo);
        painelTopo.add(btnNovo);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Matrícula"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(230, 234, 240));
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(Cores.CINZA_CLARO));

        JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        painelBotoesInferior.setBackground(Cores.FUNDO);
        btnEditar = new JButton("Editar Selecionado");
        btnExcluir = new JButton("Excluir Selecionado");
        estilizarBotaoSecundario(btnEditar);
        estilizarBotaoSecundario(btnExcluir);
        btnExcluir.setForeground(new Color(200, 40, 40));
        painelBotoesInferior.add(btnEditar);
        painelBotoesInferior.add(btnExcluir);

        corpo.add(painelTopo, BorderLayout.NORTH);
        corpo.add(scroll, BorderLayout.CENTER);
        corpo.add(painelBotoesInferior, BorderLayout.SOUTH);
        add(corpo, BorderLayout.CENTER);

        btnNovo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editarSelecionado();
            }
        });
    }

    private void estilizarBotaoPrimario(JButton botao) {
        botao.setFocusPainted(false);
        botao.setBackground(Cores.AZUL_ESCURO);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setBorder(new EmptyBorder(8, 16, 8, 16));
    }

    private void estilizarBotaoSecundario(JButton botao) {
        botao.setFocusPainted(false);
        botao.setBackground(Color.WHITE);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.CINZA_CLARO),
                new EmptyBorder(6, 12, 6, 12)));
    }

    private void abrirFormulario(Usuario u) {
        TelaFormUsuario form = new TelaFormUsuario(this, u, this::carregarTabela);
        form.setVisible(true);
    }

    private void editarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Usuario u = new Usuario();
        u.setId((Integer) modeloTabela.getValueAt(linha, 0));
        u.setNome((String) modeloTabela.getValueAt(linha, 1));
        u.setMatricula((Integer) modeloTabela.getValueAt(linha, 2));
        abrirFormulario(u);
    }

    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esse usuário?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            int id = (Integer) modeloTabela.getValueAt(linha, 0);
            dao.excluir(id);
            JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
            carregarTabela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        try {
            List<Usuario> lista = dao.listarTodos();
            modeloTabela.setRowCount(0);
            for (Usuario u : lista) {
                modeloTabela.addRow(new Object[]{u.getId(), u.getNome(), u.getMatricula()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}