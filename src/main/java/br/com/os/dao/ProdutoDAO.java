package br.com.os.dao;

import br.com.os.model.Produto;
import br.com.os.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void salvar(Produto p) throws SQLException {
        String sql = "INSERT INTO tb_produto (nome, categoria, quantidade, quantidade_minima, preco_custo, preco_venda) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCategoria());
            stmt.setInt(3, p.getQuantidade());
            stmt.setInt(4, p.getQuantidadeMinima());
            stmt.setDouble(5, p.getPrecoCusto());
            stmt.setDouble(6, p.getPrecoVenda());

            stmt.executeUpdate();
        }
    }

    public void atualizar(Produto p) throws SQLException {
        String sql = "UPDATE tb_produto SET nome = ?, categoria = ?, quantidade = ?, "
                   + "quantidade_minima = ?, preco_custo = ?, preco_venda = ? WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCategoria());
            stmt.setInt(3, p.getQuantidade());
            stmt.setInt(4, p.getQuantidadeMinima());
            stmt.setDouble(5, p.getPrecoCusto());
            stmt.setDouble(6, p.getPrecoVenda());
            stmt.setInt(7, p.getId());

            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tb_produto WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_produto ORDER BY nome";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Produto> buscarPorNome(String nome) throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_produto WHERE nome LIKE ? ORDER BY nome";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_produto WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setCategoria(rs.getString("categoria"));
        p.setQuantidade(rs.getInt("quantidade"));
        p.setQuantidadeMinima(rs.getInt("quantidade_minima"));
        p.setPrecoCusto(rs.getDouble("preco_custo"));
        p.setPrecoVenda(rs.getDouble("preco_venda"));
        p.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
        return p;
    }
}