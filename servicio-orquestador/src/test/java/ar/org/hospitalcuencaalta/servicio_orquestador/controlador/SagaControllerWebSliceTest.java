package ar.org.hospitalcuencaalta.servicio_orquestador.controlador;

import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaStateService;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.ContratoLaboralDto;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.EmpleadoDto;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.SagaEmpleadoContratoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web‐slice test de SagaController usando @MockitoBean.
 *
 * Ahora que el controlador inyecta StateMachineFactory<Estados, Eventos>,
 * mockeamos:
 *   1) el StateMachineFactory para devolver un StateMachine simulado;
 *   2) el StateMachine en sí para stubear sus métodos.
 */
@WebMvcTest(SagaController.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
class SagaControllerWebSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Mockeamos el StateMachineFactory para que cada getStateMachine(...) devuelva el mock 'stateMachine'.
     */
    @MockitoBean
    private StateMachineFactory<Estados, Eventos> stateMachineFactory;

    /**
     * Mockeamos directamente la StateMachine que el factory devolverá.
     */
    @MockitoBean
    private StateMachine<Estados, Eventos> stateMachine;

    /**
     * Mock del servicio que persiste el estado de la saga.
     */
    @MockitoBean
    private SagaStateService sagaStateService;

    @BeforeEach
    void setup() {
        // 1) Cuando el controlador solicite una nueva state machine, devolvemos el mock:
        when(stateMachineFactory.getStateMachine()).thenReturn(stateMachine);

        // 2) Para evitar NPE en getExtendedState().getVariables().put(...):
        doReturn(new DefaultExtendedState()).when(stateMachine).getExtendedState();

        // 3) Stubear sendEvents(Flux<Message<Eventos>>) → devolvemos Flux.empty()
        doReturn(Flux.<Message<Eventos>>empty())
                .when(stateMachine)
                .sendEvents(any(Flux.class));


        // 5) Stubear getState() → un State<Estados, Eventos> cuyo getId() sea INICIO
        @SuppressWarnings("unchecked")
        State<Estados, Eventos> estadoInicio = Mockito.mock(State.class);
        Mockito.when(estadoInicio.getId()).thenReturn(Estados.INICIO);
        doReturn(estadoInicio).when(stateMachine).getState();
    }

    @Test
    void iniciarSaga_shouldSendEvent_SOLICITAR_CREAR_EMPLEADO() throws Exception {
        // 1) Construir EmpleadoDto
        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setNombre("Juan");
        empleadoDto.setApellido("Pérez");
        empleadoDto.setDocumento("12345678");

        // 2) Construir ContratoLaboralDto
        ContratoLaboralDto contratoDto = new ContratoLaboralDto();
        contratoDto.setSalario(50_000.0);
        contratoDto.setTipoContrato("planta");
        contratoDto.setRegimen("tiempo-completo");
        contratoDto.setFechaDesde(LocalDate.parse("2025-07-01"));
        contratoDto.setFechaHasta(LocalDate.parse("2026-07-01"));

        // 3) Empaquetar DTOs en SagaEmpleadoContratoRequest
        SagaEmpleadoContratoRequest request = new SagaEmpleadoContratoRequest();
        request.setEmpleado(empleadoDto);
        request.setContrato(contratoDto);
        String jsonBody = objectMapper.writeValueAsString(request);

        // 4) Ejecutar POST a /api/saga/empleado-contrato
        mockMvc.perform(post("/api/saga/empleado-contrato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        // 5) Verificamos que se llamó exactamente 1 vez a stateMachine.sendEvents(...)
        verify(stateMachine, times(1)).sendEvents(any(Flux.class));
    }

    @Test
    void iniciarSaga_shouldAcceptOldFieldNames() throws Exception {
        Map<String, Object> empleadoJson = new HashMap<>();
        empleadoJson.put("nombre", "Juan");
        empleadoJson.put("apellido", "Pérez");
        empleadoJson.put("documento", "12345678");

        Map<String, Object> contratoJson = new HashMap<>();
        contratoJson.put("salario", 50_000.0);
        contratoJson.put("fechaInicio", "2025-07-01");
        contratoJson.put("fechaFin", "2026-07-01");
        contratoJson.put("regimen","tiempo-completo");

        Map<String, Object> request = new HashMap<>();
        request.put("empleado", empleadoJson);
        request.put("contrato", contratoJson);
        String jsonBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/saga/empleado-contrato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        verify(stateMachine, times(1)).sendEvents(any(Flux.class));
    }
}
