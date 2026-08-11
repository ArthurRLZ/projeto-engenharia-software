package br.edu.ufape.backend.dto;

import br.edu.ufape.backend.model.enums.TipoRecurso;

public class ResourceResponse {

    private Long id;
    private String nome;
    private String descricao;
    private Integer capacidade;
    private TipoRecurso tipo;
    private Boolean statusFuncionamento;

    public ResourceResponse() {
    }

    public ResourceResponse(Long id, String nome, String descricao, Integer capacidade, TipoRecurso tipo, Boolean statusFuncionamento) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.capacidade = capacidade;
        this.tipo = tipo;
        this.statusFuncionamento = statusFuncionamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public TipoRecurso getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecurso tipo) {
        this.tipo = tipo;
    }

    public Boolean getStatusFuncionamento() {
        return statusFuncionamento;
    }

    public void setStatusFuncionamento(Boolean statusFuncionamento) {
        this.statusFuncionamento = statusFuncionamento;
    }
}