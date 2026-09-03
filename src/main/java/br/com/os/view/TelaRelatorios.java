package br.com.os.view;

import br.com.os.dao.OsDAO;
import br.com.os.model.OrdemServico;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaRelatorios extends JFrame {

    private static final Color COR_FUNDO = Cores.FUNDO;
    private static final Color COR_PRIMARIA = Cores.AZUL_ESCURO;

    private JComboBox<String> cbStatus;
    private JTextField txtDataInicio, txtDataFim;
    private JButton btnFiltrar, btnLimparFiltro;
    private JTable tabelaResultado;
    private DefaultTableModel modeloTabela;

    private JLabel lblTotalOS, lblValorTotal, lblTicketMedio;

    private final OsDAO dao = new OsDAO();
    private final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaRelatorios() {
        setTitle("Relatórios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);

        initComponents();
        aplicarFiltro();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ---------- CABEÇALHO ----------
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(COR_PRIMARIA);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitulo = new JLabel("  Relatórios de Ordens de Serviço");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        add(cabecalho, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(10, 10));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 15, 20));

        corpo.add(criarPainelFiltros(), BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);
        corpo.add(criarPainelResumo(), BorderLayout.SOUTH);

        add(corpo, BorderLayout.CENTER);
    }

    // ---------- FILTROS ----------
    private JPanel criarPainelFiltros() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.CINZA_CLARO),
                new EmptyBorder(10, 15, 10, 15)));

        cbStatus = new JComboBox<>(new String[]{"TODOS", "ABERTO", "EM ANDAMENTO", "AGUARDANDO PEÇA", "FINALIZADO", "ENTREGUE"});

        txtDataInicio = new JTextField(8);
        txtDataFim = new JTextField(8);
        txtDataInicio.setToolTipText("dd/MM/aaaa");
        txtDataFim.setToolTipText("dd/MM/aaaa");

        btnFiltrar = new JButton("Filtrar");
        estilizarBotaoPrimario(btnFiltrar);

        btnLimparFiltro = new JButton("Limpar");
        estilizarBotaoSecundario(btnLimparFiltro);

        painel.add(new JLabel("Status:"));
        painel.add(cbStatus);
        painel.add(new JLabel("   De:"));
        painel.add(txtDataInicio);
        painel.add(new JLabel("Até:"));
        painel.add(txtDataFim);
        painel.add(btnFiltrar);
        painel.add(btnLimparFiltro);

        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnLimparFiltro.addActionListener(e -> limparFiltro());

        return painel;
    }

    // ---------- TABELA ----------
    private JScrollPane criarPainelTabela() {
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Data", "Cliente", "Equipamento", "Status", "Valor (R$)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaResultado = new JTable(modeloTabela);
        tabelaResultado.setRowHeight(26);
        tabelaResultado.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabelaResultado.getTableHeader().setBackground(new Color(230, 234, 240));

        JScrollPane scroll = new JScrollPane(tabelaResultado);
        scroll.setBorder(BorderFactory.createLineBorder(Cores.CINZA_CLARO));
        return scroll;
    }

    // ---------- RESUMO ----------
    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 15, 0));
        painel.setBorder(new EmptyBorder(15, 0, 0, 0));
        painel.setBackground(COR_FUNDO);

        lblTotalOS = new JLabel();
        lblValorTotal = new JLabel();
        lblTicketMedio = new JLabel();

        painel.add(criarCardResumo("Total de OS", lblTotalOS));
        painel.add(criarCardResumo("Valor Total", lblValorTotal));
        painel.add(criarCardResumo("Ticket Médio", lblTicketMedio));

        return painel;
    }

    private JPanel criarCardResumo(String rotulo, JLabel valor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.CINZA_CLARO),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel lblRotulo = new JLabel(rotulo);
        lblRotulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblRotulo.setForeground(Cores.CINZA_ESCURO);

        valor.setFont(new Font("SansSerif", Font.BOLD, 22));
        valor.setForeground(COR_PRIMARIA);

        card.add(lblRotulo, BorderLayout.NORTH);
        card.add(valor, BorderLayout.CENTER);
        return card;
    }

    // ---------- LÓGICA ----------
    private void aplicarFiltro() {
        try {
            String status = (String) cbStatus.getSelectedItem();
            LocalDate dataInicio = parseDataOuNulo(txtDataInicio.getText());
            LocalDate dataFim = parseDataOuNulo(txtDataFim.getText());

            List<OrdemServico> lista = dao.filtrar(status, dataInicio, dataFim);
            preencherTabela(lista);
            preencherResumo(lista);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use o formato dd/MM/aaaa.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limparFiltro() {
        cbStatus.setSelectedIndex(0);
        txtDataInicio.setText("");
        txtDataFim.setText("");
        aplicarFiltro();
    }

    private LocalDate parseDataOuNulo(String texto) {
        texto = texto.trim();
        if (texto.isEmpty()) return null;
        return LocalDate.parse(texto, formatoData);
    }

    private void preencherTabela(List<OrdemServico> lista) {
        modeloTabela.setRowCount(0);
        for (OrdemServico os : lista) {
            modeloTabela.addRow(new Object[]{
                    os.getId(),
                    os.getDataAbertura().format(formatoData),
                    os.getClienteNome(),
                    os.getTipoEquipamento(),
                    os.getStatus(),
                    String.format("%.2f", os.getValorEstimado())
            });
        }
    }

    private void preencherResumo(List<OrdemServico> lista) {
        int total = lista.size();
        double valorTotal = lista.stream().mapToDouble(OrdemServico::getValorEstimado).sum();
        double ticketMedio = total > 0 ? valorTotal / total : 0.0;

        lblTotalOS.setText(String.valueOf(total));
        lblValorTotal.setText(String.format("R$ %.2f", valorTotal));
        lblTicketMedio.setText(String.format("R$ %.2f", ticketMedio));
    }

    // ---------- ESTILO ----------
    private void estilizarBotaoPrimario(JButton botao) {
        botao.setFocusPainted(false);
        botao.setBackground(COR_PRIMARIA);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setBorder(new EmptyBorder(6, 14, 6, 14));
    }

    private void estilizarBotaoSecundario(JButton botao) {
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.PLAIN, 12));
        botao.setBackground(Color.WHITE);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.CINZA_CLARO),
                new EmptyBorder(5, 12, 5, 12)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaRelatorios().setVisible(true));
    }
}