package ar.org.hospitalcuencaalta.servicio_orquestador.controlador;

import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.ContratoLaboralDto;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.EmpleadoDto;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.SagaEmpleadoContratoRequest;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.SagaStatusResponse;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaStateService;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.SagaState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaStateService;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Test de integración para SagaController:
 *   - Ahora mockeamos StateMachineFactory en lugar de StateMachine.
 *   - El factory devuelve el StateMachine simulada que stubemos a continuación.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = AutowireMode.ALL)
class SagaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Mockeamos el StateMachineFactory para que el controlador reciba un StateMachine simulado.
     */
    @MockitoBean
    private StateMachineFactory<Estados, Eventos> stateMachineFactory;

    /**
     * Mockeamos el StateMachine que el factory devolverá.
     */
    @MockitoBean
    private StateMachine<Estados, Eventos> stateMachine;

    @MockitoBean
    private SagaStateService sagaStateService;

    @BeforeEach
    void setup() {
        // 1) Cuando el controlador llame a factory.getStateMachine(UUID), devolvemos el mock stateMachine:
        when(stateMachineFactory.getStateMachine(any(String.class)))
                .thenReturn(stateMachine);

        // 2) Stubear getExtendedState() → DefaultExtendedState para evitar NPE
        doReturn(new DefaultExtendedState()).when(stateMachine).getExtendedState();

        // 3) Stubear sendEvents(Flux<Message<Eventos>>) → Flux.empty()
        doReturn(Flux.<Message<Eventos>>empty())
                .when(stateMachine)
                .sendEvents(any(Flux.class));

        // 4) Stubear getUuid() → UUID no nulo
        doReturn(UUID.randomUUID()).when(stateMachine).getUuid();

        // 5) Stubear getState() → State<Estados, Eventos> cuyo getId() sea INICIO
        @SuppressWarnings("unchecked")
        State<Estados, Eventos> estadoInicio = Mockito.mock(State.class);
        Mockito.when(estadoInicio.getId()).thenReturn(Estados.INICIO);
        doReturn(estadoInicio).when(stateMachine).getState();
    }

    @Test
    void iniciarSaga_integration_shouldReturn200AndSendEvent() throws Exception {
        // 1) Construir DTOs de ejemplo
        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setNombre("María");
        empleadoDto.setApellido("García");
        empleadoDto.setDocumento("87654321");

        ContratoLaboralDto contratoDto = new ContratoLaboralDto();
        contratoDto.setSalario(60_000.0);
        contratoDto.setFechaInicio(LocalDate.parse("2025-08-01"));
        contratoDto.setFechaFin(LocalDate.parse("2026-08-01"));

        // 2) Empaquetar en SagaEmpleadoContratoRequest
        SagaEmpleadoContratoRequest request = new SagaEmpleadoContratoRequest();
        request.setEmpleado(empleadoDto);
        request.setContrato(contratoDto);
        String jsonBody = objectMapper.writeValueAsString(request);

        // 3) Hacer POST real usando MockMvc
        mockMvc.perform(post("/api/saga/empleado-contrato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        // 4) Verificar que sendEvents(...) se llamó exactamente 1 vez
        verify(stateMachine, times(1)).sendEvents(any(Flux.class));
    }

    @Test
    void obtenerSaga_integration_shouldReturnExpectedJson() throws Exception {
        // 1) Configurar SagaState simulado
        Instant updated = Instant.parse("2025-08-01T00:00:00Z");
        SagaState sagaState = SagaState.builder()
                .sagaId("abc-123")
                .estado(Estados.FINALIZADA)
                .updatedAt(updated)
                .build();
        when(sagaStateService.findById("abc-123")).thenReturn(Optional.of(sagaState));

        // 2) JSON esperado
        SagaStatusResponse expected = SagaStatusResponse.builder()
                .sagaId("abc-123")
                .estadoActual("FINALIZADA")
                .idEmpleadoCreado(null)
                .idContratoCreado(null)
                .mensajeError(null)
                .timestampInicio(null)
                .timestampFin(updated)
                .build();
        String expectedJson = objectMapper.writeValueAsString(expected);

        // 3) Realizar GET y verificar JSON
        mockMvc.perform(get("/api/saga/empleado-contrato/{id}", "abc-123"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(sagaStateService, times(1)).findById("abc-123");
    }
}
