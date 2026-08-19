package br.edu.ufape.backend.controller;

import br.edu.ufape.backend.dto.AvailabilityRequest;
import br.edu.ufape.backend.dto.AvailabilityResponse;
import br.edu.ufape.backend.dto.ResourceRequest;
import br.edu.ufape.backend.dto.ResourceResponse;
import br.edu.ufape.backend.model.enums.TipoRecurso;
import br.edu.ufape.backend.service.ResourceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.*;

class ResourceControllerTest {
    @Mock
    ResourceService resourceService;
    @InjectMocks
    ResourceController resourceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarRecurso() {
        when(resourceService.criarRecurso(any(ResourceRequest.class))).thenReturn(new ResourceResponse(Long.valueOf(1), "nome", "descricao", Integer.valueOf(0), TipoRecurso.LABORATORIO, Boolean.TRUE));

        ResponseEntity<ResourceResponse> result = resourceController.criarRecurso(new ResourceRequest("nome", "descricao", Integer.valueOf(0), TipoRecurso.LABORATORIO, Boolean.TRUE));
        Assertions.assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testListarRecursos() {
        when(resourceService.listarRecursos()).thenReturn(List.of(new ResourceResponse(Long.valueOf(1), "nome", "descricao", Integer.valueOf(0), TipoRecurso.LABORATORIO, Boolean.TRUE)));

        ResponseEntity<List<ResourceResponse>> result = resourceController.listarRecursos();
        Assertions.assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testConsultarDisponibilidade() {
        when(resourceService.consultarDisponibilidade(any(AvailabilityRequest.class))).thenReturn(List.of(new AvailabilityResponse(Long.valueOf(1), "nome", TipoRecurso.LABORATORIO, "descricao", Integer.valueOf(0), true)));

        ResponseEntity<List<AvailabilityResponse>> result = resourceController.consultarDisponibilidade(LocalDate.of(2026, Month.AUGUST, 18), LocalTime.of(21, 4, 15), LocalTime.of(21, 4, 15));
        Assertions.assertTrue(result.getStatusCode().is2xxSuccessful());
    }
}
