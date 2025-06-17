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

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SagaStateService {

    private final SagaStateRepository repository;
    private final ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public void save(StateMachine<Estados, Eventos> stateMachine) {
        UUID sagaId = stateMachine.getUuid();
        boolean created = false;
        SagaState state = repository.findById(sagaId).orElse(null);
        if (state == null) {
            state = SagaState.builder()
                    .sagaId(sagaId)
                    .build();
            entityManager.persist(state);
            created = true;
        }
        state.setEstado(stateMachine.getState().getId());
        try {
            String json = objectMapper.writeValueAsString(stateMachine.getExtendedState().getVariables());
            state.setExtendedState(json);
        } catch (JsonProcessingException e) {
           // log.warn("[SagaStateService] No se pudo convertir el estado extendido a JSON para saga '{}': {}", sagaId, e.toString());

            state.setExtendedState(null);
        }
        state.setUpdatedAt(Instant.now());
        // Persistir inmediatamente para que las consultas subsecuentes puedan
        // obtener el estado actualizado sin depender de la sincronización de
        // la transacción.
        if (created) {
            entityManager.flush();
        } else {
            repository.saveAndFlush(state);
        }
    }

    public Optional<SagaState> findById(UUID sagaId) {
        return repository.findById(sagaId);
    }
}
