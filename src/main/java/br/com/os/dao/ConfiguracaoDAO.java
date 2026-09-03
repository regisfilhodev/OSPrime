package br.com.os.dao;

import br.com.os.model.Configuracao;
import br.com.os.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracaoDAO {

    public Configuracao buscar() throws SQLException {
        String sql = "SELECT * FROM tb_configuracao WHERE id = 1";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Configuracao c = new Configuracao();
                c.setStatusPadraoOS(rs.getString("status_padrao_os"));
                c.setTiposEquipamento(rs.getString("tipos_equipamento"));
                c.setQuantidadeMinimaPadrao(rs.getInt("quantidade_minima_padrao"));
                return c;
            }
        }
        return new Configuracao(); // fallback, não deveria acontecer se o INSERT inicial rodou
    }

    public void salvar(Configuracao c) throws SQLException {
        String sql = "UPDATE tb_configuracao SET status_padrao_os = ?, tipos_equipamento = ?, "
                   + "quantidade_minima_padrao = ? WHERE id = 1";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getStatusPadraoOS());
            stmt.setString(2, c.getTiposEquipamento());
            stmt.setInt(3, c.getQuantidadeMinimaPadrao());
            stmt.executeUpdate();
        }
    }
}