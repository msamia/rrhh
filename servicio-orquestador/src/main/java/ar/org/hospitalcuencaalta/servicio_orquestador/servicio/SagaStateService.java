package ar.org.hospitalcuencaalta.servicio_orquestador.servicio;

import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.SagaState;
import ar.org.hospitalcuencaalta.servicio_orquestador.repositorio.SagaStateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SagaStateService {

    private final SagaStateRepository repository;
    private final ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(StateMachine<Estados, Eventos> stateMachine) {
        Object sagaIdRaw = stateMachine.getExtendedState().getVariables().get("sagaDbId");
        Long sagaId = null;
        if (sagaIdRaw instanceof Number) {
            sagaId = ((Number) sagaIdRaw).longValue();
        } else if (sagaIdRaw instanceof String) {
            try {
                sagaId = Long.valueOf((String) sagaIdRaw);
            } catch (NumberFormatException ignored) {
                sagaId = null;
            }
        }

        SagaState state = null;
        if (sagaId != null) {
            state = repository.findById(sagaId).orElse(null);
        }
        if (state == null) {
            state = new SagaState();
        }

        state.setEstado(stateMachine.getState().getId());
        try {
            String json = objectMapper.writeValueAsString(stateMachine.getExtendedState().getVariables());
            state.setExtendedState(json);
        } catch (JsonProcessingException e) {
            state.setExtendedState(null);
        }
        state.setUpdatedAt(Instant.now());

        state = repository.saveAndFlush(state);

        stateMachine.getExtendedState().getVariables().put("sagaDbId", state.getSagaId());
    }

    public Optional<SagaState> findById(Long sagaId) {
        return repository.findById(sagaId);
    }
}
