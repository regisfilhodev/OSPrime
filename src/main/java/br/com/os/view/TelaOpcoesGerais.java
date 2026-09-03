package br.com.os.view;

import br.com.os.dao.ConfiguracaoDAO;
import br.com.os.model.Configuracao;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class TelaOpcoesGerais extends JFrame {

    private JComboBox<String> cbStatusPadrao;
    private JTextArea txtTiposEquipamento;
    private JTextField txtQuantidadeMinima;
    private JButton btnSalvar;

    private final ConfiguracaoDAO dao = new ConfiguracaoDAO();

    public TelaOpcoesGerais() {
        setTitle("Opções Gerais do Sistema");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Cores.FUNDO);

        initComponents();
        carregarConfiguracao();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Cores.AZUL_ESCURO);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitulo = new JLabel("  Opções Gerais do Sistema");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        add(cabecalho, BorderLayout.NORTH);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Cores.FUNDO);
        painel.setBorder(new EmptyBorder(25, 30, 10, 30));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 0, 6, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 1;

        cbStatusPadrao = new JComboBox<>(new String[]{"ABERTO", "EM ANDAMENTO", "AGUARDANDO PEÇA", "FINALIZADO", "ENTREGUE"});

        txtTiposEquipamento = new JTextArea(4, 20);
        txtTiposEquipamento.setLineWrap(true);
        txtTiposEquipamento.setWrapStyleWord(true);

        txtQuantidadeMinima = new JTextField();

        int row = 0;
        row = addCampo(painel, c, row, "Status padrão para OS novas", cbStatusPadrao);
        row = addCampo(painel, c, row, "Tipos de equipamento (separados por vírgula)", new JScrollPane(txtTiposEquipamento));
        addCampo(painel, c, row, "Quantidade mínima padrão (produtos novos no estoque)", txtQuantidadeMinima);

        add(painel, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        painelBotoes.setBackground(Cores.FUNDO);
        painelBotoes.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Cores.CINZA_CLARO));

        btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setBackground(Cores.AZUL_ESCURO);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSalvar.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnSalvar.setFocusPainted(false);

        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
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

    private void carregarConfiguracao() {
        try {
            Configuracao cfg = dao.buscar();
            cbStatusPadrao.setSelectedItem(cfg.getStatusPadraoOS());
            txtTiposEquipamento.setText(cfg.getTiposEquipamento());
            txtQuantidadeMinima.setText(String.valueOf(cfg.getQuantidadeMinimaPadrao()));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvar() {
        try {
            Configuracao cfg = new Configuracao();
            cfg.setStatusPadraoOS((String) cbStatusPadrao.getSelectedItem());
            cfg.setTiposEquipamento(txtTiposEquipamento.getText().trim());

            String qtdTexto = txtQuantidadeMinima.getText().trim();
            cfg.setQuantidadeMinimaPadrao(qtdTexto.isEmpty() ? 0 : Integer.parseInt(qtdTexto));

            dao.salvar(cfg);
            JOptionPane.showMessageDialog(this, "Configurações salvas com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade mínima deve ser um número!", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}