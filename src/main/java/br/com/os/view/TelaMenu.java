package br.com.os.view;

import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaMenu extends JFrame {

    private static final Color COR_PRIMARIA = Cores.AZUL_CLARO;
    private static final Color COR_BARRA = Cores.AZUL_BARRA;
    private static final Color COR_TITULO = Cores.AZUL_ESCURO;
    private static final Color COR_TEXTO_SECUNDARIO = Cores.CINZA_ESCURO;
    private static final Color COR_BORDA = Cores.CINZA_CLARO;
    private static final Color COR_FUNDO = Cores.FUNDO;
    private static final Color COR_BRANCO = Cores.BRANCO;

    public TelaMenu() {
        setTitle("Sistema de Ordem de Serviço");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(criarBarraSuperior(), BorderLayout.NORTH);
        add(criarAreaConteudo(), BorderLayout.CENTER);
    }

    private JPanel criarBarraSuperior() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        JPanel faixaAba = new JPanel(new BorderLayout());
        faixaAba.setBackground(COR_BARRA);
        faixaAba.setPreferredSize(new Dimension(0, 38));

        JLabel abaMenu = new JLabel("MENU");
        abaMenu.setForeground(Color.WHITE);
        abaMenu.setFont(new Font("SansSerif", Font.BOLD, 12));
        abaMenu.setHorizontalAlignment(SwingConstants.CENTER);
        abaMenu.setBorder(new EmptyBorder(0, 22, 0, 22));

        JPanel abaAtiva = new JPanel(new BorderLayout());
        abaAtiva.setOpaque(false);
        abaAtiva.add(abaMenu, BorderLayout.CENTER);

        JPanel indicador = new JPanel();
        indicador.setBackground(COR_PRIMARIA);
        indicador.setPreferredSize(new Dimension(0, 3));
        abaAtiva.add(indicador, BorderLayout.SOUTH);

        faixaAba.add(abaAtiva, BorderLayout.WEST);
        container.add(faixaAba, BorderLayout.NORTH);

        JPanel ribbon = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ribbon.setBackground(Color.WHITE);
        ribbon.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, COR_BORDA
        ));

        ribbon.add(criarGrupo(
                "CADASTROS",
                criarBotaoRibbon("CLIENTES", "Clientes", this::abrirTelaClientes),
                criarBotaoRibbon("OS", "Ordens de Serviço", this::abrirTelaOS)
        ));

        ribbon.add(criarSeparadorGrupo());

        ribbon.add(criarGrupo(
                "RELATÓRIOS",
                criarBotaoRibbon("REL", "Relatórios", this::abrirTelaRelatorios)
        ));

        ribbon.add(criarSeparadorGrupo());

        ribbon.add(criarGrupo(
                "ESTOQUE",
                criarBotaoRibbon("EST", "Estoque", this::abrirTelaEstoque)
        ));

        ribbon.add(criarSeparadorGrupo());

        ribbon.add(criarGrupo("SISTEMA",
                criarBotaoRibbon("CONF", "Configurações", this::abrirTelaConfiguracoes),
                criarBotaoRibbon("SAIR", "Sair", this::sair)
        ));

        container.add(ribbon, BorderLayout.CENTER);
        return container;
    }

    private JPanel criarGrupo(String nomeGrupo, JPanel... botoes) {
        JPanel grupo = new JPanel(new BorderLayout());
        grupo.setBackground(Color.WHITE);
        grupo.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel painelBotoes = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                4,
                0
        ));
        painelBotoes.setBackground(Color.WHITE);

        for (JPanel botao : botoes) {
            painelBotoes.add(botao);
        }

        JLabel rotulo = new JLabel(nomeGrupo, SwingConstants.CENTER);
        rotulo.setFont(new Font("SansSerif", Font.BOLD, 9));
        rotulo.setForeground(COR_TEXTO_SECUNDARIO);
        rotulo.setBorder(new EmptyBorder(3, 0, 0, 0));

        grupo.add(painelBotoes, BorderLayout.CENTER);
        grupo.add(rotulo, BorderLayout.SOUTH);

        return grupo;
    }

    private JPanel criarSeparadorGrupo() {
        JPanel separador = new JPanel();
        separador.setPreferredSize(new Dimension(1, 62));
        separador.setBackground(COR_BORDA);
        return separador;
    }

    private JPanel criarBotaoRibbon(String icone, String texto, Runnable acao) {
        JPanel botao = new JPanel();
        botao.setLayout(new BoxLayout(botao, BoxLayout.Y_AXIS));
        botao.setPreferredSize(new Dimension(96, 64));
        botao.setMaximumSize(new Dimension(96, 64));
        botao.setMinimumSize(new Dimension(96, 64));
        botao.setBackground(Color.WHITE);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(new EmptyBorder(5, 4, 5, 4));

        IconeMenu painelIcone = new IconeMenu(icone, COR_TITULO);
        painelIcone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTexto = new JLabel(texto, SwingConstants.CENTER);
        lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblTexto.setForeground(COR_TITULO);
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTexto.setMaximumSize(new Dimension(88, 20));

        botao.add(Box.createVerticalGlue());
        botao.add(painelIcone);
        botao.add(Box.createVerticalStrut(4));
        botao.add(lblTexto);
        botao.add(Box.createVerticalGlue());

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                acao.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(Cores.AZUL_HOVER);
                painelIcone.setCor(COR_PRIMARIA);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(Color.WHITE);
                painelIcone.setCor(COR_TITULO);
            }
        };

        botao.addMouseListener(mouseAdapter);
        painelIcone.addMouseListener(mouseAdapter);
        lblTexto.addMouseListener(mouseAdapter);

        return botao;
    }

    private JPanel criarAreaConteudo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(35, 50, 25, 50));

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Bem-vindo ao Sistema OS");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 27));
        titulo.setForeground(COR_TITULO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descricao = new JLabel(
                "Acesse rapidamente as principais funções do sistema."
        );
        descricao.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descricao.setForeground(COR_TEXTO_SECUNDARIO);
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(5));
        cabecalho.add(descricao);

        painel.add(cabecalho, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 4, 18, 18));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(30, 0, 0, 0));

        cards.add(criarCard(
                "Ordens de Serviço",
                "Gerencie as ordens abertas e acompanhe os atendimentos.",
                "OS",
                this::abrirTelaOS
        ));

        cards.add(criarCard(
                "Clientes",
                "Cadastre, consulte e gerencie seus clientes.",
                "CLIENTES",
                this::abrirTelaClientes
        ));

        cards.add(criarCard(
                "Relatórios",
                "Visualize informações e indicadores do sistema.",
                "REL",
                this::abrirTelaRelatorios
        ));

        cards.add(criarCard(
                "Estoque",
                "Controle produtos, materiais e movimentações.",
                "EST",
                this::abrirTelaEstoque
        ));

        painel.add(cards, BorderLayout.CENTER);

        JLabel rodape = new JLabel(
                "Sistema de Ordem de Serviço • Versão 1.0"
        );
        rodape.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rodape.setForeground(COR_TEXTO_SECUNDARIO);
        rodape.setHorizontalAlignment(SwingConstants.CENTER);

        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarCard(
            String titulo,
            String descricao,
            String icone,
            Runnable acao
    ) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(getBackground());
                g2.fillRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        16,
                        16
                );

                g2.setColor(COR_BORDA);
                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        16,
                        16
                );

                g2.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COR_BRANCO);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        IconeMenu painelIcone = new IconeMenu(icone, COR_PRIMARIA);
        painelIcone.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        labelTitulo.setForeground(COR_TITULO);
        labelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelDescricao = new JLabel(
                "<html><div style='width:180px'>"
                + descricao
                + "</div></html>"
        );
        labelDescricao.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelDescricao.setForeground(COR_TEXTO_SECUNDARIO);
        labelDescricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel acessar = new JLabel("Acessar →");
        acessar.setFont(new Font("SansSerif", Font.BOLD, 11));
        acessar.setForeground(COR_PRIMARIA);
        acessar.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(painelIcone);
        card.add(Box.createVerticalStrut(18));
        card.add(labelTitulo);
        card.add(Box.createVerticalStrut(7));
        card.add(labelDescricao);
        card.add(Box.createVerticalGlue());
        card.add(acessar);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                acao.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(Cores.AZUL_HOVER);
                painelIcone.setCor(COR_TITULO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(COR_BRANCO);
                painelIcone.setCor(COR_PRIMARIA);
            }
        };

        card.addMouseListener(mouseAdapter);
        painelIcone.addMouseListener(mouseAdapter);
        labelTitulo.addMouseListener(mouseAdapter);
        labelDescricao.addMouseListener(mouseAdapter);
        acessar.addMouseListener(mouseAdapter);

        return card;
    }

    private void sair() {
        String[] opcoes = {
            "Mudar de usuário",
            "Sair do sistema",
            "Cancelar"
        };

        int opcao = JOptionPane.showOptionDialog(
                this,
                "O que deseja fazer?",
                "Encerrar sessão",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        switch (opcao) {
            case 0 -> {
                // Volta para a tela de login
                dispose();
                new TelaLogin().setVisible(true);
            }

            case 1 -> {
                // Encerra completamente o sistema
                System.exit(0);
            }

            case 2 -> {
                // Não faz nada
            }
        }
    }

    private void abrirTelaOS() {
        TelaOS tela = new TelaOS();
        tela.setVisible(true);
    }

    private void abrirTelaClientes() {
        JOptionPane.showMessageDialog(
                this,
                "Tela de Clientes ainda não implementada."
        );
    }

    private void abrirTelaRelatorios() {
        TelaRelatorios tela = new TelaRelatorios();
        tela.setVisible(true);
    }

    private void abrirTelaEstoque() {
        TelaEstoque tela = new TelaEstoque();
        tela.setVisible(true);
    }

    private void abrirTelaConfiguracoes() {
        TelaConfiguracoes tela = new TelaConfiguracoes(this);
        tela.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()
                -> new TelaMenu().setVisible(true)
        );
    }

    private static class IconeMenu extends JPanel {

        private final String tipo;
        private Color cor;

        public IconeMenu(String tipo, Color cor) {
            this.tipo = tipo;
            this.cor = cor;
            setPreferredSize(new Dimension(38, 38));
            setMaximumSize(new Dimension(38, 38));
            setMinimumSize(new Dimension(38, 38));
            setOpaque(false);
        }

        public void setCor(Color cor) {
            this.cor = cor;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setStroke(new BasicStroke(
                    2f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));
            g2.setColor(cor);

            int w = getWidth();
            int h = getHeight();

            switch (tipo) {
                case "OS":
                    desenharOS(g2, w, h);
                    break;
                case "CLIENTES":
                    desenharClientes(g2, w, h);
                    break;
                case "REL":
                    desenharRelatorio(g2, w, h);
                    break;
                case "EST":
                    desenharEstoque(g2, w, h);
                    break;
                case "CONF":
                    desenharConfiguracao(g2, w, h);
                    break;
                case "SAIR":
                    desenharSair(g2, w, h);
                    break;
            }

            g2.dispose();
        }

        private void desenharOS(Graphics2D g2, int w, int h) {
            g2.drawRoundRect(7, 9, 24, 20, 4, 4);
            g2.drawLine(12, 14, 26, 14);
            g2.drawLine(12, 19, 22, 19);
            g2.drawLine(12, 24, 18, 24);
        }

        private void desenharClientes(Graphics2D g2, int w, int h) {
            g2.drawOval(13, 6, 12, 12);
            g2.drawArc(8, 20, 22, 15, 0, 180);
        }

        private void desenharRelatorio(Graphics2D g2, int w, int h) {
            g2.drawLine(8, 31, 8, 10);
            g2.drawLine(8, 31, 32, 31);
            g2.drawLine(13, 26, 18, 20);
            g2.drawLine(18, 20, 23, 23);
            g2.drawLine(23, 23, 30, 13);
        }

        private void desenharEstoque(Graphics2D g2, int w, int h) {
            g2.drawRect(7, 10, 24, 20);
            g2.drawLine(7, 16, 31, 16);
            g2.drawLine(15, 10, 15, 30);
            g2.drawLine(23, 10, 23, 30);
        }

        private void desenharConfiguracao(Graphics2D g2, int w, int h) {
            g2.drawOval(11, 11, 16, 16);
            g2.drawOval(16, 16, 6, 6);

            for (int i = 0; i < 8; i++) {
                double angulo = Math.toRadians(i * 45);
                int x1 = (int) (19 + Math.cos(angulo) * 10);
                int y1 = (int) (19 + Math.sin(angulo) * 10);
                int x2 = (int) (19 + Math.cos(angulo) * 14);
                int y2 = (int) (19 + Math.sin(angulo) * 14);

                g2.drawLine(x1, y1, x2, y2);
            }
        }

        private void desenharSair(Graphics2D g2, int w, int h) {
            g2.drawRoundRect(8, 7, 16, 24, 3, 3);
            g2.drawLine(19, 19, 31, 19);
            g2.drawLine(27, 15, 31, 19);
            g2.drawLine(27, 23, 31, 19);
        }
    }
}
