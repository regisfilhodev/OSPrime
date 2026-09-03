package br.com.os.view;

import br.com.os.dao.OsDAO;
import br.com.os.model.OrdemServico;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class TelaFormOS extends JDialog {

    private static final Color COR_FUNDO = Cores.FUNDO;
    private static final Color COR_PRIMARIA = Cores.AZUL_ESCURO;
    private static final Font FONTE_LABEL = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONTE_CAMPO = new Font("SansSerif", Font.PLAIN, 13);

    private JTextField txtClienteNome, txtClienteTelefone, txtMarcaModelo, txtNumeroSerie, txtValor;
    private JComboBox<String> cbTipoEquipamento, cbStatus;
    private JTextArea txtDefeito, txtLaudo;
    private JButton btnSalvar, btnCancelar;

    private final OsDAO dao = new OsDAO();
    private final OrdemServico osExistente;
    private final Runnable aoSalvar;

    public TelaFormOS(JFrame parent, OrdemServico osExistente, Runnable aoSalvar) {
        super(parent, true);
        this.osExistente = osExistente;
        this.aoSalvar = aoSalvar;

        setTitle(osExistente == null ? "Nova Ordem de Serviço" : "Editar Ordem de Serviço");
        setSize(560, 700);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(COR_FUNDO);

        initComponents();

        if (osExistente != null) {
            preencherComOsExistente();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ---------- CABEÇALHO ----------
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(COR_PRIMARIA);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        cabecalho.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("  " + (osExistente == null ? "Nova Ordem de Serviço" : "Editar Ordem de Serviço"));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        add(cabecalho, BorderLayout.NORTH);

        // ---------- FORMULÁRIO ----------
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(20, 25, 10, 25));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 0, 6, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 1;

        txtClienteNome = criarCampo(20);
        txtClienteTelefone = criarCampo(15);
        cbTipoEquipamento = criarCombo(new String[]{"Notebook", "Desktop", "Impressora", "Celular", "Outro"});
        txtMarcaModelo = criarCampo(20);
        txtNumeroSerie = criarCampo(15);
        txtDefeito = criarArea();
        txtLaudo = criarArea();
        txtValor = criarCampo(10);
        cbStatus = criarCombo(new String[]{"ABERTO", "EM ANDAMENTO", "AGUARDANDO PEÇA", "FINALIZADO", "ENTREGUE"});

        int row = 0;
        row = addCampo(painel, c, row, "Nome do Cliente", txtClienteNome);
        row = addCampo(painel, c, row, "Telefone", txtClienteTelefone);
        row = addCampo(painel, c, row, "Tipo de Equipamento", cbTipoEquipamento);
        row = addCampo(painel, c, row, "Marca/Modelo", txtMarcaModelo);
        row = addCampo(painel, c, row, "Número de Série", txtNumeroSerie);
        row = addCampo(painel, c, row, "Defeito Relatado", new JScrollPane(txtDefeito));
        row = addCampo(painel, c, row, "Laudo Técnico", new JScrollPane(txtLaudo));
        row = addCampo(painel, c, row, "Valor Estimado (R$)", txtValor);
        addCampo(painel, c, row, "Status", cbStatus);

        JScrollPane scrollFormulario = new JScrollPane(painel);
        scrollFormulario.setBorder(null);
        scrollFormulario.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollFormulario, BorderLayout.CENTER);

        // ---------- RODAPÉ ----------
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusPainted(false);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBackground(COR_PRIMARIA);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSalvar.setBorder(new EmptyBorder(8, 20, 8, 20));

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private JTextField criarCampo(int colunas) {
        JTextField campo = new JTextField(colunas);
        campo.setFont(FONTE_CAMPO);
        return campo;
    }

    private JComboBox<String> criarCombo(String[] itens) {
        JComboBox<String> combo = new JComboBox<>(itens);
        combo.setFont(FONTE_CAMPO);
        return combo;
    }

    private JTextArea criarArea() {
        JTextArea area = new JTextArea(3, 20);
        area.setFont(FONTE_CAMPO);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private int addCampo(JPanel painel, GridBagConstraints c, int row, String rotulo, JComponent campo) {
        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(FONTE_LABEL);
        lbl.setForeground(new Color(90, 90, 90));

        c.gridy = row;
        painel.add(lbl, c);
        c.gridy = row + 1;
        painel.add(campo, c);
        return row + 2;
    }

    private void preencherComOsExistente() {
        txtClienteNome.setText(osExistente.getClienteNome());
        txtClienteTelefone.setText(osExistente.getClienteTelefone());
        cbTipoEquipamento.setSelectedItem(osExistente.getTipoEquipamento());
        txtMarcaModelo.setText(osExistente.getMarcaModelo());
        txtNumeroSerie.setText(osExistente.getNumeroSerie());
        txtDefeito.setText(osExistente.getDefeitoRelatado());
        txtLaudo.setText(osExistente.getLaudoTecnico());
        txtValor.setText(String.valueOf(osExistente.getValorEstimado()));
        cbStatus.setSelectedItem(osExistente.getStatus());
    }

    private void salvar() {
        if (txtClienteNome.getText().trim().isEmpty() || txtMarcaModelo.getText().trim().isEmpty()
                || txtDefeito.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha Nome do Cliente, Marca/Modelo e Defeito Relatado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            OrdemServico os = (osExistente != null) ? osExistente : new OrdemServico();
            os.setClienteNome(txtClienteNome.getText().trim());
            os.setClienteTelefone(txtClienteTelefone.getText().trim());
            os.setTipoEquipamento((String) cbTipoEquipamento.getSelectedItem());
            os.setMarcaModelo(txtMarcaModelo.getText().trim());
            os.setNumeroSerie(txtNumeroSerie.getText().trim());
            os.setDefeitoRelatado(txtDefeito.getText().trim());
            os.setLaudoTecnico(txtLaudo.getText().trim());
            os.setStatus((String) cbStatus.getSelectedItem());

            String valorTexto = txtValor.getText().replace(",", ".").trim();
            os.setValorEstimado(valorTexto.isEmpty() ? 0.0 : Double.parseDouble(valorTexto));

            if (osExistente != null) {
                dao.atualizar(os);
                JOptionPane.showMessageDialog(this, "OS atualizada com sucesso!");
            } else {
                dao.salvar(os);
                JOptionPane.showMessageDialog(this, "OS salva com sucesso!");
            }

            aoSalvar.run();
            dispose();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
