package br.com.os.view;

import br.com.os.dao.ProdutoDAO;
import br.com.os.model.Produto;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class TelaEstoque extends JFrame {

    private JTextField txtBusca;
    private JButton btnNovo, btnEditar, btnExcluir, btnBuscar;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final ProdutoDAO dao = new ProdutoDAO();

    public TelaEstoque() {
        setTitle("Estoque de Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Cores.FUNDO);

        initComponents();
        carregarTabela(null);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Cores.AZUL_ESCURO);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitulo = new JLabel("  Estoque de Produtos");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        add(cabecalho, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(10, 10));
        corpo.setBackground(Cores.FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(Cores.FUNDO);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelBusca.setBackground(Cores.FUNDO);
        txtBusca = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        estilizarBotaoSecundario(btnBuscar);
        painelBusca.add(new JLabel("Produto:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);

        btnNovo = new JButton("+ Novo Produto");
        estilizarBotaoPrimario(btnNovo);

        painelTopo.add(painelBusca, BorderLayout.WEST);
        painelTopo.add(btnNovo, BorderLayout.EAST);

        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Categoria", "Qtd.", "Qtd. Mín.", "Preço Custo", "Preço Venda"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(230, 234, 240));
        tabela.setSelectionBackground(new Color(220, 235, 255));

        // Destaca em vermelho claro as linhas com estoque baixo
        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    int qtd = Integer.parseInt(modeloTabela.getValueAt(row, 3).toString());
                    int qtdMin = Integer.parseInt(modeloTabela.getValueAt(row, 4).toString());
                    c.setBackground(qtd <= qtdMin ? new Color(255, 230, 230) : Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createLineBorder(Cores.CINZA_CLARO));

        JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        painelBotoesInferior.setBackground(Cores.FUNDO);
        btnEditar = new JButton("Editar Selecionado");
        btnExcluir = new JButton("Excluir Selecionado");
        estilizarBotaoSecundario(btnEditar);
        estilizarBotaoSecundario(btnExcluir);
        btnExcluir.setForeground(new Color(200, 40, 40));
        painelBotoesInferior.add(btnEditar);
        painelBotoesInferior.add(btnExcluir);

        JLabel lblLegenda = new JLabel("  ⚠ Linhas em vermelho: estoque igual ou abaixo do mínimo");
        lblLegenda.setForeground(Cores.CINZA_ESCURO);
        lblLegenda.setFont(new Font("SansSerif", Font.PLAIN, 11));
        painelBotoesInferior.add(lblLegenda);

        corpo.add(painelTopo, BorderLayout.NORTH);
        corpo.add(scrollTabela, BorderLayout.CENTER);
        corpo.add(painelBotoesInferior, BorderLayout.SOUTH);
        add(corpo, BorderLayout.CENTER);

        btnNovo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());
        btnBuscar.addActionListener(e -> buscar());

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

    private void abrirFormulario(Produto p) {
        TelaFormProduto form = new TelaFormProduto(this, p, () -> carregarTabela(null));
        form.setVisible(true);
    }

    private void editarSelecionado() {
        Integer id = getIdSelecionado();
        if (id == null) return;
        try {
            Produto p = dao.buscarPorId(id);
            if (p != null) abrirFormulario(p);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirSelecionado() {
        Integer id = getIdSelecionado();
        if (id == null) return;

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esse produto?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) return;

        try {
            dao.excluir(id);
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
            carregarTabela(null);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer getIdSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (Integer) modeloTabela.getValueAt(linha, 0);
    }

    private void buscar() {
        String termo = txtBusca.getText().trim();
        carregarTabela(termo.isEmpty() ? null : termo);
    }

    private void carregarTabela(String filtro) {
        try {
            List<Produto> lista = (filtro == null) ? dao.listarTodos() : dao.buscarPorNome(filtro);
            modeloTabela.setRowCount(0);
            for (Produto p : lista) {
                modeloTabela.addRow(new Object[]{
                        p.getId(), p.getNome(), p.getCategoria(),
                        p.getQuantidade(), p.getQuantidadeMinima(),
                        String.format("%.2f", p.getPrecoCusto()),
                        String.format("%.2f", p.getPrecoVenda())
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaEstoque().setVisible(true));
    }
}