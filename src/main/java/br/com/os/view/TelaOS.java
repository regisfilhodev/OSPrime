package br.com.os.view;

import br.com.os.dao.OsDAO;
import br.com.os.model.OrdemServico;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class TelaOS extends JFrame {

    private static final Color COR_FUNDO = Cores.FUNDO;
    private static final Color COR_PRIMARIA = Cores.AZUL_ESCURO;
    private static final Font FONTE_TABELA = new Font("SansSerif", Font.PLAIN, 13);

    private JTextField txtBusca;
    private JButton btnNovo, btnEditar, btnExcluir, btnBuscar;
    private JTable tabelaOS;
    private DefaultTableModel modeloTabela;

    private final OsDAO dao = new OsDAO();

    public TelaOS() {
        setTitle("Ordens de Serviço");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // <- fecha só essa janela, não o programa
        setSize(950, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);

        initComponents();
        carregarTabela(null);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ---------- CABEÇALHO ----------
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(COR_PRIMARIA);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitulo = new JLabel("  Ordens de Serviço");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        add(cabecalho, BorderLayout.NORTH);

        // ---------- CORPO ----------
        JPanel corpo = new JPanel(new BorderLayout(10, 10));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Barra de busca + botão Nova OS
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(COR_FUNDO);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelBusca.setBackground(COR_FUNDO);
        txtBusca = new JTextField(20);
        txtBusca.setFont(FONTE_TABELA);
        btnBuscar = new JButton("Buscar");
        estilizarBotaoSecundario(btnBuscar);
        painelBusca.add(new JLabel("Cliente:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);

        btnNovo = new JButton("+ Nova OS");
        estilizarBotaoPrimario(btnNovo);

        painelTopo.add(painelBusca, BorderLayout.WEST);
        painelTopo.add(btnNovo, BorderLayout.EAST);

        // Tabela
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Cliente", "Telefone", "Equipamento", "Marca/Modelo", "Status", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaOS = new JTable(modeloTabela);
        tabelaOS.setFont(FONTE_TABELA);
        tabelaOS.setRowHeight(26);
        tabelaOS.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabelaOS.getTableHeader().setBackground(new Color(230, 234, 240));
        tabelaOS.setSelectionBackground(new Color(220, 235, 255));
        JScrollPane scrollTabela = new JScrollPane(tabelaOS);
        scrollTabela.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Botões inferiores
        JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        painelBotoesInferior.setBackground(COR_FUNDO);
        btnEditar = new JButton("Ver / Editar Selecionada");
        btnExcluir = new JButton("Excluir Selecionada");
        estilizarBotaoSecundario(btnEditar);
        estilizarBotaoSecundario(btnExcluir);
        btnExcluir.setForeground(new Color(200, 40, 40));
        painelBotoesInferior.add(btnEditar);
        painelBotoesInferior.add(btnExcluir);

        corpo.add(painelTopo, BorderLayout.NORTH);
        corpo.add(scrollTabela, BorderLayout.CENTER);
        corpo.add(painelBotoesInferior, BorderLayout.SOUTH);

        add(corpo, BorderLayout.CENTER);

        // ---------- EVENTOS ----------
        btnNovo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionada());
        btnExcluir.addActionListener(e -> excluirSelecionada());
        btnBuscar.addActionListener(e -> buscarOS());

        tabelaOS.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarSelecionada();
                }
            }
        });
    }

    private void estilizarBotaoPrimario(JButton botao) {
        botao.setFocusPainted(false);
        botao.setBackground(COR_PRIMARIA);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setBorder(new EmptyBorder(8, 16, 8, 16));
    }

    private void estilizarBotaoSecundario(JButton botao) {
        botao.setFocusPainted(false);
        botao.setFont(FONTE_TABELA);
        botao.setBackground(Color.WHITE);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(6, 12, 6, 12)));
    }

    private void abrirFormulario(OrdemServico os) {
        TelaFormOS form = new TelaFormOS(this, os, () -> carregarTabela(null));
        form.setVisible(true);
    }

    private void editarSelecionada() {
        Integer id = getIdSelecionado();
        if (id == null) {
            return;
        }

        try {
            OrdemServico os = dao.buscarPorId(id);
            if (os != null) {
                abrirFormulario(os);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar OS: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirSelecionada() {
        Integer id = getIdSelecionado();
        if (id == null) {
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir essa OS?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            dao.excluir(id);
            JOptionPane.showMessageDialog(this, "OS excluída com sucesso!");
            carregarTabela(null);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer getIdSelecionado() {
        int linha = tabelaOS.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma OS na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (Integer) modeloTabela.getValueAt(linha, 0);
    }

    private void buscarOS() {
        String termo = txtBusca.getText().trim();
        carregarTabela(termo.isEmpty() ? null : termo);
    }

    private void carregarTabela(String filtroCliente) {
        try {
            List<OrdemServico> lista = (filtroCliente == null)
                    ? dao.listarTodas()
                    : dao.buscarPorCliente(filtroCliente);

            modeloTabela.setRowCount(0);
            for (OrdemServico os : lista) {
                modeloTabela.addRow(new Object[]{
                    os.getId(), os.getClienteNome(), os.getClienteTelefone(),
                    os.getTipoEquipamento(), os.getMarcaModelo(), os.getStatus(),
                    String.format("%.2f", os.getValorEstimado())
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaOS().setVisible(true));
    }
}
