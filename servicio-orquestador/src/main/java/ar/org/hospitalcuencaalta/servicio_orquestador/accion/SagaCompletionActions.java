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
        String operacion = machine.getExtendedState().get("operacion", String.class);
        String sagaId   = machine.getUuid().toString();

        // DTOs completos si existen
        Object empDto = machine.getExtendedState().get("empleadoDto", Object.class);
        Object conDto = machine.getExtendedState().get("contratoDto", Object.class);

        if ("CREAR".equalsIgnoreCase(operacion)) {
            if (empDto != null) publisher.publishEmployeeCreated(empDto);
            if (conDto != null) publisher.publishContratoCreated(conDto);
        } else if ("ACTUALIZAR".equalsIgnoreCase(operacion)) {
            if (empDto != null) publisher.publishEmployeeUpdated(empDto);
            if (conDto != null) publisher.publishContratoUpdated(conDto);
        } else if ("ELIMINAR".equalsIgnoreCase(operacion)) {
            if (idContrato != null) publisher.publishContratoDeleted(idContrato);
            if (idEmpleado != null) publisher.publishEmployeeDeleted(idEmpleado);
        }

        publisher.publishSagaCompleted(sagaId);
    }
}
