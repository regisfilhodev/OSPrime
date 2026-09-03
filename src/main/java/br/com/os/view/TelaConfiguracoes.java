package br.com.os.view;

import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaConfiguracoes extends JFrame {

    public TelaConfiguracoes(TelaMenu aThis) {
        setTitle("Configurações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Cores.FUNDO);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(Cores.AZUL_ESCURO);
        cabecalho.setPreferredSize(new Dimension(0, 60));
        JLabel lblTitulo = new JLabel("  Configurações");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        cabecalho.add(lblTitulo, BorderLayout.CENTER);
        add(cabecalho, BorderLayout.NORTH);

        JPanel painel = new JPanel(new GridLayout(2, 1, 15, 15));
        painel.setBackground(Cores.FUNDO);
        painel.setBorder(new EmptyBorder(30, 40, 30, 40));

        painel.add(criarOpcao("👤", "Gerenciar Usuários", "Criar, editar e excluir contas de acesso", () -> {
            TelaUsuarios tela = new TelaUsuarios();
            tela.setVisible(true);
        }));

        painel.add(criarOpcao("⚙", "Opções Gerais", "Status padrão, tipos de equipamento e mais", () -> {
            TelaOpcoesGerais tela = new TelaOpcoesGerais();
            tela.setVisible(true);
        }));

        add(painel, BorderLayout.CENTER);
    }

    private JPanel criarOpcao(String icone, String titulo, String descricao, Runnable acao) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.CINZA_CLARO),
                new EmptyBorder(15, 20, 15, 20)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblIcone = new JLabel(icone);
        lblIcone.setFont(new Font("SansSerif", Font.PLAIN, 28));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setForeground(Cores.AZUL_ESCURO);

        JLabel lblDescricao = new JLabel(descricao);
        lblDescricao.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDescricao.setForeground(Cores.CINZA_ESCURO);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(lblTitulo);
        textos.add(Box.createVerticalStrut(3));
        textos.add(lblDescricao);

        card.add(lblIcone, BorderLayout.WEST);
        card.add(textos, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) { acao.run(); }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(235, 245, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }
}