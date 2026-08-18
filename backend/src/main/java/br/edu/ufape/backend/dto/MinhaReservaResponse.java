package br.edu.ufape.backend.dto;

import br.edu.ufape.backend.model.enums.StatusReserva;
import br.edu.ufape.backend.model.enums.TipoRecurso;
import java.time.LocalDate;
import java.time.LocalTime;

public class MinhaReservaResponse { // nova DTO para não correr o risco de quebrar a que já existe

    private Long id;
    private Long resourceId;
    private String resourceNome;
    private TipoRecurso resourceTipo;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private StatusReserva status;

    public MinhaReservaResponse() {
    }

    public MinhaReservaResponse(Long id, Long resourceId, String resourceNome, TipoRecurso resourceTipo,
            LocalDate data, LocalTime horarioInicio, LocalTime horarioFim, StatusReserva status) {
        this.id = id;
        this.resourceId = resourceId;
        this.resourceNome = resourceNome;
        this.resourceTipo = resourceTipo;
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceNome() {
        return resourceNome;
    }

    public void setResourceNome(String resourceNome) {
        this.resourceNome = resourceNome;
    }

    public TipoRecurso getResourceTipo() {
        return resourceTipo;
    }

    public void setResourceTipo(TipoRecurso resourceTipo) {
        this.resourceTipo = resourceTipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }
}