package br.com.os.model;

public class Configuracao {
    private String statusPadraoOS;
    private String tiposEquipamento; // separados por vírgula
    private Integer quantidadeMinimaPadrao;

    public String getStatusPadraoOS() { return statusPadraoOS; }
    public void setStatusPadraoOS(String statusPadraoOS) { this.statusPadraoOS = statusPadraoOS; }

    public String getTiposEquipamento() { return tiposEquipamento; }
    public void setTiposEquipamento(String tiposEquipamento) { this.tiposEquipamento = tiposEquipamento; }

    public Integer getQuantidadeMinimaPadrao() { return quantidadeMinimaPadrao; }
    public void setQuantidadeMinimaPadrao(Integer quantidadeMinimaPadrao) { this.quantidadeMinimaPadrao = quantidadeMinimaPadrao; }

    public String[] getTiposEquipamentoComoArray() {
        if (tiposEquipamento == null || tiposEquipamento.trim().isEmpty()) return new String[0];
        return tiposEquipamento.split("\\s*,\\s*");
    }
}