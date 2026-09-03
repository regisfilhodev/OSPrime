package br.com.os.view;

import br.com.os.dao.UsuarioDAO;
import br.com.os.model.Usuario;
import br.com.os.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class TelaLogin extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    private final UsuarioDAO dao = new UsuarioDAO();

    private static final Color FUNDO = new Color(241, 245, 249);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXTO = new Color(30, 41, 59);
    private static final Color TEXTO_SECUNDARIO = new Color(100, 116, 139);
    private static final Color BORDA = new Color(226, 232, 240);
    private static final Color PLACEHOLDER = new Color(148, 163, 184);

    public TelaLogin() {
        setTitle("Sistema OS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        criarInterface();
    }

    private void criarInterface() {
        JPanel fundo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(FUNDO);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(226, 232, 240));
                g2.fillOval(-180, -180, 450, 450);
                g2.fillOval(getWidth() - 250, getHeight() - 250, 400, 400);

                g2.dispose();
            }
        };

        setContentPane(fundo);

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(760, 440));
        container.add(criarPainelEsquerdo(), BorderLayout.WEST);
        container.add(criarCardLogin(), BorderLayout.CENTER);

        fundo.add(container);
    }

    private JPanel criarPainelEsquerdo() {
        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradiente = new GradientPaint(
                        0, 0, Cores.AZUL_ESCURO,
                        getWidth(), getHeight(), Cores.AZUL_NAVY
                );

                g2.setPaint(gradiente);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-80, getHeight() - 170, 230, 230);

                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(getWidth() - 100, -100, 180, 180);

                g2.dispose();
            }
        };

        painel.setPreferredSize(new Dimension(330, 440));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(50, 40, 40, 40));
        painel.setOpaque(false);

        JPanel logo = criarLogo();
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("<html>Bem-vindo ao<br><b>Sistema OS</b></html>");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 28));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(35, 0, 15, 0));

        JLabel descricao = new JLabel(
                "<html>Gerencie suas ordens de serviço,<br>clientes e atendimentos em um só lugar.</html>"
        );
        descricao.setForeground(new Color(219, 234, 254));
        descricao.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rodape = new JLabel("Sistema de Gestão • v1.0");
        rodape.setForeground(new Color(191, 219, 254));
        rodape.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(logo);
        painel.add(titulo);
        painel.add(descricao);
        painel.add(Box.createVerticalGlue());
        painel.add(rodape);

        return painel;
    }

    private JPanel criarLogo() {
        JPanel logo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2.setColor(Cores.AZUL_ESCURO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 19));

                FontMetrics fm = g2.getFontMetrics();
                String texto = "OS";
                int x = (getWidth() - fm.stringWidth(texto)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2.drawString(texto, x, y);
                g2.dispose();
            }
        };

        logo.setPreferredSize(new Dimension(58, 58));
        logo.setMaximumSize(new Dimension(58, 58));
        logo.setOpaque(false);

        return logo;
    }

    private JPanel criarCardLogin() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(15, 23, 42, 20));
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 28, 28);

                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 28, 28);

                g2.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(48, 55, 40, 55));
        card.setPreferredSize(new Dimension(430, 440));
        card.setOpaque(false);

        JLabel titulo = new JLabel("Entrar");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Acesse sua conta para continuar");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(TEXTO_SECUNDARIO);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMatricula = criarRotuloCampo("MATRÍCULA");
        txtLogin = criarCampo();

        JLabel lblSenha = criarRotuloCampo("SENHA");
        txtSenha = criarCampoSenha();

        btnEntrar = criarBotaoEntrar();

        JLabel rodape = new JLabel("Entre com suas credenciais para acessar o sistema");
        rodape.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rodape.setForeground(PLACEHOLDER);
        rodape.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titulo);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitulo);
        card.add(Box.createVerticalStrut(32));

        card.add(lblMatricula);
        card.add(Box.createVerticalStrut(8));
        card.add(txtLogin);
        card.add(Box.createVerticalStrut(20));

        card.add(lblSenha);
        card.add(Box.createVerticalStrut(8));
        card.add(txtSenha);
        card.add(Box.createVerticalStrut(28));

        card.add(btnEntrar);
        card.add(Box.createVerticalGlue());
        card.add(rodape);

        btnEntrar.addActionListener(e -> tentarLogin());
        txtSenha.addActionListener(e -> tentarLogin());

        txtLogin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c)
                        && c != KeyEvent.VK_BACK_SPACE
                        && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });

        return card;
    }

    private JLabel criarRotuloCampo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(TEXTO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField();

        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setForeground(TEXTO);
        campo.setCaretColor(Cores.AZUL_ESCURO);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setPreferredSize(new Dimension(0, 44));
        estilizarFoco(campo);

        return campo;
    }

    private JPasswordField criarCampoSenha() {
        JPasswordField campo = new JPasswordField();

        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setForeground(TEXTO);
        campo.setCaretColor(Cores.AZUL_ESCURO);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setPreferredSize(new Dimension(0, 44));
        estilizarFoco(campo);

        return campo;
    }

    private void estilizarFoco(JTextField campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA, 1),
                new EmptyBorder(0, 14, 0, 14)
        ));

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Cores.AZUL_CLARO, 2),
                        new EmptyBorder(0, 13, 0, 13)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDA, 1),
                        new EmptyBorder(0, 14, 0, 14)
                ));
            }
        });
    }

    private JButton criarBotaoEntrar() {
        JButton botao = new JButton("ENTRAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color cor = getModel().isRollover()
                        ? Cores.AZUL_NAVY
                        : Cores.AZUL_ESCURO;

                g2.setColor(cor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));

                FontMetrics fm = g2.getFontMetrics();
                String texto = "ENTRAR";

                int x = (getWidth() - fm.stringWidth(texto)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2.drawString(texto, x, y);
                g2.dispose();
            }
        };

        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        botao.setPreferredSize(new Dimension(0, 46));
        botao.setAlignmentX(Component.LEFT_ALIGNMENT);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return botao;
    }

    private void tentarLogin() {
        String matriculaTexto = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (matriculaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe sua matrícula.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            txtLogin.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe sua senha.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            txtSenha.requestFocus();
            return;
        }

        int matricula;

        try {
            matricula = Integer.parseInt(matriculaTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "A matrícula deve conter apenas números.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            txtLogin.requestFocus();
            return;
        }

        try {
            Usuario usuario = dao.autenticar(matricula, senha);

            if (usuario != null) {
                TelaMenu menu = new TelaMenu();
                menu.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Matrícula ou senha inválidos.",
                        "Não foi possível entrar",
                        JOptionPane.ERROR_MESSAGE
                );
                txtSenha.setText("");
                txtSenha.requestFocus();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível conectar ao banco de dados.\n\n" + e.getMessage(),
                    "Erro de conexão",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            new TelaLogin().setVisible(true);
        });
    }
}

