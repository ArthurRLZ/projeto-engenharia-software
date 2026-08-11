package br.edu.ufape.backend.dto;

import br.edu.ufape.backend.model.enums.TipoRecurso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ResourceRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @PositiveOrZero(message = "A capacidade não pode ser negativa")
    private Integer capacidade;

    @NotNull(message = "O tipo do recurso é obrigatório")
    private TipoRecurso tipo;

    private Boolean statusFuncionamento;

    public ResourceRequest() {
    }

    public ResourceRequest(String nome, String descricao, Integer capacidade, TipoRecurso tipo, Boolean statusFuncionamento) {
        this.nome = nome;
        this.descricao = descricao;
        this.capacidade = capacidade;
        this.tipo = tipo;
        this.statusFuncionamento = statusFuncionamento;
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
