package ar.org.hospitalcuencaalta.servicio_orquestador.accion;

import ar.org.hospitalcuencaalta.servicio_orquestador.config.DomainEventPublisher;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SagaCompletionActions {

    private final DomainEventPublisher publisher;

    public SagaCompletionActions(DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void onSagaCompleted(StateContext<Estados, Eventos> context) {
        StateMachine<Estados, Eventos> machine = context.getStateMachine();

        Long idEmpleado = machine.getExtendedState().get("idEmpleado", Long.class);
        Long idContrato = machine.getExtendedState().get("idContrato", Long.class);
        String sagaId   = machine.getUuid().toString();

        // Publicar sólo al finalizar satisfactoriamente
        publisher.publishEmployeeCreated(idEmpleado, Map.of("empleadoId", idEmpleado));
        publisher.publishContratoCreated(idContrato, Map.of("contratoId", idContrato));
        publisher.publishSagaCompleted(sagaId);
    }
}
