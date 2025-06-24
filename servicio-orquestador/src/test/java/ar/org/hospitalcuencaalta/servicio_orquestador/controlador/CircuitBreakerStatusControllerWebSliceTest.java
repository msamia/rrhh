package ar.org.hospitalcuencaalta.servicio_orquestador.controlador;

import ar.org.hospitalcuencaalta.servicio_orquestador.historial.CreationAction;
import ar.org.hospitalcuencaalta.servicio_orquestador.historial.CreationHistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(CircuitBreakerStatusController.class)
@Import(CreationHistory.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
class CircuitBreakerStatusControllerWebSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreationHistory history;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CircuitBreakerRegistry registry;

    @Test
    void empleadoActions_shouldReturnLoggedActions() throws Exception {
        history.add(new CreationAction("CREATE_EMPLEADO", true, null, Instant.EPOCH));
        history.add(new CreationAction("CREATE_CONTRATO", false, "boom", Instant.EPOCH.plusSeconds(1)));

        mockMvc.perform(get("/actuator/cb-state/empleado-actions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
