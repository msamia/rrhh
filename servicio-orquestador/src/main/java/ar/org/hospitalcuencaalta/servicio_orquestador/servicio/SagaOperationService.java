package ar.org.hospitalcuencaalta.servicio_orquestador.servicio;

import ar.org.hospitalcuencaalta.servicio_orquestador.operacion.SagaOperation;
import ar.org.hospitalcuencaalta.servicio_orquestador.repositorio.SagaOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.statemachine.StateMachine;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SagaOperationService {
    private final SagaOperationRepository repository;

    @Transactional
    public void record(StateMachine<?, ?> sm, String step, boolean success, String error) {
        Long sagaId = null;
        if (sm != null) {
            Object sagaIdObj = sm.getExtendedState().getVariables().get("sagaDbId");
            if (sagaIdObj instanceof Number n) {
                sagaId = n.longValue();
            } else if (sagaIdObj instanceof String s) {
                try { sagaId = Long.valueOf(s); } catch (NumberFormatException ignored) {}
            }
        }
        recordBySagaId(sagaId, step, success, error);
    }

    @Transactional
    public void recordBySagaId(Long sagaId, String step, boolean success, String error) {
        SagaOperation op = SagaOperation.builder()
                .sagaId(sagaId)
                .step(step)
                .success(success)
                .error(error)
                .timestamp(Instant.now())
                .build();
        repository.save(op);
    }

    public List<SagaOperation> findAll() { return repository.findAll(); }

    public List<SagaOperation> findBySagaId(Long sagaId) {
        return repository.findBySagaIdOrderByTimestampAsc(sagaId);
    }
}
