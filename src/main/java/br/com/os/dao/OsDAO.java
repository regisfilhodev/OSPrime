package br.com.os.dao;

import br.com.os.model.OrdemServico;
import br.com.os.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OsDAO {

    // CREATE - Salvar uma nova OS
    public void salvar(OrdemServico os) throws SQLException {
        String sql = "INSERT INTO tb_ordem_servico "
                + "(cliente_nome, cliente_telefone, tipo_equipamento, marca_modelo, "
                + "numero_serie, acessorios, defeito_relatado, laudo_tecnico, valor_estimado, status_os) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, os.getClienteNome());
            stmt.setString(2, os.getClienteTelefone());
            stmt.setString(3, os.getTipoEquipamento());
            stmt.setString(4, os.getMarcaModelo());
            stmt.setString(5, os.getNumeroSerie());
            stmt.setString(6, os.getAcessorios());
            stmt.setString(7, os.getDefeitoRelatado());
            stmt.setString(8, os.getLaudoTecnico());
            stmt.setDouble(9, os.getValorEstimado());
            stmt.setString(10, os.getStatus());

            stmt.executeUpdate();
        }
    }

    // READ - Listar todas as OS
    public List<OrdemServico> listarTodas() throws SQLException {
        List<OrdemServico> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_ordem_servico ORDER BY id DESC";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearOrdemServico(rs));
            }
        }
        return lista;
    }

    // READ - Buscar por nome do cliente (para o campo de busca)
    public List<OrdemServico> buscarPorCliente(String nome) throws SQLException {
        List<OrdemServico> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_ordem_servico WHERE cliente_nome LIKE ? ORDER BY id DESC";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrdemServico(rs));
                }
            }
        }
        return lista;
    }

    // UPDATE - Atualizar uma OS existente
    public void atualizar(OrdemServico os) throws SQLException {
        String sql = "UPDATE tb_ordem_servico SET "
                + "cliente_nome = ?, cliente_telefone = ?, tipo_equipamento = ?, marca_modelo = ?, "
                + "numero_serie = ?, acessorios = ?, defeito_relatado = ?, laudo_tecnico = ?, "
                + "valor_estimado = ?, status_os = ? WHERE id = ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, os.getClienteNome());
            stmt.setString(2, os.getClienteTelefone());
            stmt.setString(3, os.getTipoEquipamento());
            stmt.setString(4, os.getMarcaModelo());
            stmt.setString(5, os.getNumeroSerie());
            stmt.setString(6, os.getAcessorios());
            stmt.setString(7, os.getDefeitoRelatado());
            stmt.setString(8, os.getLaudoTecnico());
            stmt.setDouble(9, os.getValorEstimado());
            stmt.setString(10, os.getStatus());
            stmt.setInt(11, os.getId());

            stmt.executeUpdate();
        }
    }

    // DELETE - Excluir uma OS pelo id
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tb_ordem_servico WHERE id = ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // READ - Buscar uma OS completa pelo ID
    public OrdemServico buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_ordem_servico WHERE id = ?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearOrdemServico(rs);
                }
            }
        }
        return null;
    }

    // Método auxiliar: converte uma linha do ResultSet num objeto OrdemServico
    private OrdemServico mapearOrdemServico(ResultSet rs) throws SQLException {
        OrdemServico os = new OrdemServico();
        os.setId(rs.getInt("id"));
        os.setDataAbertura(rs.getTimestamp("data_abertura").toLocalDateTime());
        os.setClienteNome(rs.getString("cliente_nome"));
        os.setClienteTelefone(rs.getString("cliente_telefone"));
        os.setTipoEquipamento(rs.getString("tipo_equipamento"));
        os.setMarcaModelo(rs.getString("marca_modelo"));
        os.setNumeroSerie(rs.getString("numero_serie"));
        os.setAcessorios(rs.getString("acessorios"));
        os.setDefeitoRelatado(rs.getString("defeito_relatado"));
        os.setLaudoTecnico(rs.getString("laudo_tecnico"));
        os.setValorEstimado(rs.getDouble("valor_estimado"));
        os.setStatus(rs.getString("status_os"));
        return os;
    }

    // Filtro combinado: status (opcional) + período (opcional)
    public List<OrdemServico> filtrar(String status, java.time.LocalDate dataInicio, java.time.LocalDate dataFim) throws SQLException {
        List<OrdemServico> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_ordem_servico WHERE 1=1");

        if (status != null && !status.equals("TODOS")) {
            sql.append(" AND status_os = ?");
        }
        if (dataInicio != null) {
            sql.append(" AND data_abertura >= ?");
        }
        if (dataFim != null) {
            sql.append(" AND data_abertura < ?");
        }
        sql.append(" ORDER BY data_abertura DESC");

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (status != null && !status.equals("TODOS")) {
                stmt.setString(idx++, status);
            }
            if (dataInicio != null) {
                stmt.setTimestamp(idx++, java.sql.Timestamp.valueOf(dataInicio.atStartOfDay()));
            }
            if (dataFim != null) {
                stmt.setTimestamp(idx++, java.sql.Timestamp.valueOf(dataFim.plusDays(1).atStartOfDay()));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrdemServico(rs));
                }
            }
        }
        return lista;
    }
}
