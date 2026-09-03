package br.com.os.model;

import java.time.LocalDateTime;

public class Produto {
    private Integer id;
    private String nome;
    private String categoria;
    private Integer quantidade;
    private Integer quantidadeMinima;
    private Double precoCusto;
    private Double precoVenda;
    private LocalDateTime dataCadastro;

    public Produto() {
        this.quantidade = 0;
        this.quantidadeMinima = 0;
        this.precoCusto = 0.0;
        this.precoVenda = 0.0;
    }

    public boolean isEstoqueBaixo() {
        return quantidade != null && quantidadeMinima != null && quantidade <= quantidadeMinima;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Integer getQuantidadeMinima() { return quantidadeMinima; }
    public void setQuantidadeMinima(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }

    public Double getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(Double precoCusto) { this.precoCusto = precoCusto; }

    public Double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(Double precoVenda) { this.precoVenda = precoVenda; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}