package br.com.os.dao;

import br.com.os.model.Usuario;
import br.com.os.util.Conexao;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public void cadastrar(String nome, int matricula, String senhaPura) throws SQLException {
        String hash = BCrypt.hashpw(senhaPura, BCrypt.gensalt());
        String sql = "INSERT INTO tb_usuario (nome, matricula, senha_hash) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, matricula);
            stmt.setString(3, hash);
            stmt.executeUpdate();
        }
    }

    public Usuario autenticar(int matricula, String senhaDigitada) throws SQLException {
        String sql = "SELECT * FROM tb_usuario WHERE matricula = ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, matricula);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashSalvo = rs.getString("senha_hash");

                    if (BCrypt.checkpw(senhaDigitada, hashSalvo)) {
                        Usuario u = new Usuario();
                        u.setId(rs.getInt("id"));
                        u.setNome(rs.getString("nome"));
                        u.setMatricula(rs.getInt("matricula"));
                        u.setSenhaHash(hashSalvo);
                        return u;
                    }
                }
            }
        }
        return null;
    }

    public java.util.List<Usuario> listarTodos() throws SQLException {
        java.util.List<Usuario> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM tb_usuario ORDER BY nome";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setMatricula(rs.getInt("matricula"));
                lista.add(u);
            }
        }
        return lista;
    }

// Atualiza nome/matrícula, sem mexer na senha
    public void atualizarDados(Usuario u) throws SQLException {
        String sql = "UPDATE tb_usuario SET nome = ?, matricula = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNome());
            stmt.setInt(2, u.getMatricula());
            stmt.setInt(3, u.getId());
            stmt.executeUpdate();
        }
    }

// Troca só a senha (redefinir)
    public void redefinirSenha(int id, String novaSenhaPura) throws SQLException {
        String hash = BCrypt.hashpw(novaSenhaPura, BCrypt.gensalt());
        String sql = "UPDATE tb_usuario SET senha_hash = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hash);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tb_usuario WHERE id = ?";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

// Verifica se já existe alguém com essa matrícula (pra validar duplicidade)
    public boolean matriculaExiste(int matricula, Integer idIgnorar) throws SQLException {
        String sql = idIgnorar == null
                ? "SELECT COUNT(*) FROM tb_usuario WHERE matricula = ?"
                : "SELECT COUNT(*) FROM tb_usuario WHERE matricula = ? AND id != ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matricula);
            if (idIgnorar != null) {
                stmt.setInt(2, idIgnorar);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
