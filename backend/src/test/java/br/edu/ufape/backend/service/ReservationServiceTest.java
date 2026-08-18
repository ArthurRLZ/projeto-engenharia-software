package br.edu.ufape.backend.service;

import br.edu.ufape.backend.dto.MinhaReservaResponse;
import br.edu.ufape.backend.model.Reservation;
import br.edu.ufape.backend.model.Resource;
import br.edu.ufape.backend.model.User;
import br.edu.ufape.backend.model.enums.Role;
import br.edu.ufape.backend.model.enums.StatusReserva;
import br.edu.ufape.backend.model.enums.TipoRecurso;
import br.edu.ufape.backend.repository.ReservationRepository;
import br.edu.ufape.backend.repository.ResourceRepository;
import br.edu.ufape.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    private User usuarioLogado;
    private Resource resource;

    @BeforeEach
    void setUp() {
        usuarioLogado = User.builder()
                .id(1L)
                .nome("Joao Teste")
                .email("joao@ufape.br")
                .role(Role.USER)
                .build();

        resource = Resource.builder()
                .id(10L)
                .nome("Laboratório A")
                .tipo(TipoRecurso.LABORATORIO)
                .build();

        // simula que o SecurityContextHolder devolve o usuario logado com email
        // "joao@ufape.br"
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("joao@ufape.br");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        securityContextHolderMock = mockStatic(SecurityContextHolder.class);
        securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);

        when(userRepository.findByEmail("joao@ufape.br")).thenReturn(Optional.of(usuarioLogado));
    }

    @AfterEach
    void tearDown() {
        securityContextHolderMock.close();
    }

    @Test
    @DisplayName("Deve listar apenas as reservas do usuário autenticado, já traduzidas")
    void deveListarReservasDoUsuarioLogado() {
        Reservation reserva = Reservation.builder()
                .id(100L)
                .user(usuarioLogado)
                .resource(resource)
                .data(LocalDate.of(2026, 9, 1))
                .horarioInicio(LocalTime.of(10, 0))
                .horarioFim(LocalTime.of(11, 0))
                .status(StatusReserva.PENDENTE)
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Reservation> paginaMock = new PageImpl<>(List.of(reserva), pageable, 1);

        when(reservationRepository.findByUserOrderByDataDescHorarioInicioDesc(usuarioLogado, pageable))
                .thenReturn(paginaMock);

        Page<MinhaReservaResponse> resultado = reservationService.listarMinhasReservas(pageable);

        assertThat(resultado.getTotalElements()).isEqualTo(1);
        MinhaReservaResponse item = resultado.getContent().get(0);
        assertThat(item.getId()).isEqualTo(100L);
        assertThat(item.getResourceNome()).isEqualTo("Laboratório A");
        assertThat(item.getStatus()).isEqualTo(StatusReserva.PENDENTE);

        // garante que a busca foi feita filtrando pelo usuario logado, nunca por outro
        verify(reservationRepository).findByUserOrderByDataDescHorarioInicioDesc(usuarioLogado, pageable);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando o usuário não tem nenhuma reserva")
    void deveRetornarPaginaVazia_quandoSemReservas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Reservation> paginaVazia = new PageImpl<>(List.of(), pageable, 0);

        when(reservationRepository.findByUserOrderByDataDescHorarioInicioDesc(usuarioLogado, pageable))
                .thenReturn(paginaVazia);

        Page<MinhaReservaResponse> resultado = reservationService.listarMinhasReservas(pageable);

        assertThat(resultado.getTotalElements()).isEqualTo(0);
        assertThat(resultado.getContent()).isEmpty();
    }
}