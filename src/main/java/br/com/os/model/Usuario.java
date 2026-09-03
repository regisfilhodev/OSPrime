package br.com.os.model;

public class Usuario {
    private Integer id;
    private String nome;
    private Integer matricula;
    private String senhaHash;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getMatricula() { return matricula; }
    public void setMatricula(Integer matricula) { this.matricula = matricula; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
}