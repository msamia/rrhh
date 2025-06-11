package ar.org.hospitalcuencaalta.servicio_orquestador.servicio;

import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.SagaState;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.repositorio.SagaStateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SagaStateService {

    private static final Logger log = LoggerFactory.getLogger(SagaStateService.class);

    private final SagaStateRepository repository;
    private final ObjectMapper objectMapper;

    public void save(StateMachine<Estados, Eventos> stateMachine) {
        String sagaId = stateMachine.getUuid().toString();
        SagaState state = repository.findById(sagaId).orElseGet(() -> SagaState.builder()
                .sagaId(sagaId)
                .build());
        state.setEstado(stateMachine.getState().getId());
        try {
            String json = objectMapper.writeValueAsString(stateMachine.getExtendedState().getVariables());
            state.setExtendedState(json);
        } catch (JsonProcessingException e) {
            log.warn("[SagaStateService] No se pudo convertir el estado extendido a JSON para saga '{}': {}", sagaId, e.toString());
            state.setExtendedState(null);
        }
        state.setUpdatedAt(Instant.now());
        repository.save(state);
    }

    public Optional<SagaState> findById(String sagaId) {
        return repository.findById(sagaId);
    }
}
