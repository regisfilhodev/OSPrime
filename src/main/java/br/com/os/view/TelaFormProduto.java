package br.com.os.view;

import br.com.os.dao.ProdutoDAO;
import br.com.os.model.Produto;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class TelaFormProduto extends JDialog {

    private JTextField txtNome, txtCategoria, txtQuantidade, txtQuantidadeMinima, txtPrecoCusto, txtPrecoVenda;
    private JButton btnSalvar, btnCancelar;

    private final ProdutoDAO dao = new ProdutoDAO();
    private final Produto produtoExistente;
    private final Runnable aoSalvar;

    public TelaFormProduto(JFrame parent, Produto produtoExistente, Runnable aoSalvar) {
        super(parent, true);
        this.produtoExistente = produtoExistente;
        this.aoSalvar = aoSalvar;

        setTitle(produtoExistente == null ? "Novo Produto" : "Editar Produto");
        setSize(490, 640);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Cores.FUNDO);

        initComponents();

        if (produtoExistente != null) {
            preencherComExistente();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Cores.AZUL_ESCURO);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitulo = new JLabel("  " + (produtoExistente == null ? "Novo Produto" : "Editar Produto"));
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
        txtCategoria = new JTextField();
        txtQuantidade = new JTextField();
        txtQuantidadeMinima = new JTextField();
        txtPrecoCusto = new JTextField();
        txtPrecoVenda = new JTextField();

        int row = 0;
        row = addCampo(painel, c, row, "Nome do Produto", txtNome);
        row = addCampo(painel, c, row, "Categoria", txtCategoria);
        row = addCampo(painel, c, row, "Quantidade em Estoque", txtQuantidade);
        row = addCampo(painel, c, row, "Quantidade Mínima (alerta)", txtQuantidadeMinima);
        row = addCampo(painel, c, row, "Preço de Custo (R$)", txtPrecoCusto);
        addCampo(painel, c, row, "Preço de Venda (R$)", txtPrecoVenda);

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
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(Cores.CINZA_ESCURO);

        c.gridy = row;
        painel.add(lbl, c);
        c.gridy = row + 1;
        painel.add(campo, c);
        return row + 2;
    }

    private void preencherComExistente() {
        txtNome.setText(produtoExistente.getNome());
        txtCategoria.setText(produtoExistente.getCategoria());
        txtQuantidade.setText(String.valueOf(produtoExistente.getQuantidade()));
        txtQuantidadeMinima.setText(String.valueOf(produtoExistente.getQuantidadeMinima()));
        txtPrecoCusto.setText(String.valueOf(produtoExistente.getPrecoCusto()));
        txtPrecoVenda.setText(String.valueOf(produtoExistente.getPrecoVenda()));
    }

    private void salvar() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome do produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Produto p = (produtoExistente != null) ? produtoExistente : new Produto();
            p.setNome(txtNome.getText().trim());
            p.setCategoria(txtCategoria.getText().trim());
            p.setQuantidade(parseIntOuZero(txtQuantidade.getText()));
            p.setQuantidadeMinima(parseIntOuZero(txtQuantidadeMinima.getText()));
            p.setPrecoCusto(parseDoubleOuZero(txtPrecoCusto.getText()));
            p.setPrecoVenda(parseDoubleOuZero(txtPrecoVenda.getText()));

            if (produtoExistente != null) {
                dao.atualizar(p);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            } else {
                dao.salvar(p);
                JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
            }

            aoSalvar.run();
            dispose();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parseIntOuZero(String texto) {
        texto = texto.trim();
        return texto.isEmpty() ? 0 : Integer.parseInt(texto);
    }

    private double parseDoubleOuZero(String texto) {
        texto = texto.replace(",", ".").trim();
        return texto.isEmpty() ? 0.0 : Double.parseDouble(texto);
    }
}