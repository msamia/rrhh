package ar.org.hospitalcuencaalta.servicio_orquestador.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DomainEventPublisher {

    private final KafkaTemplate<String, Object> kafka;

    public DomainEventPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    public void publishEmployeeCreated(Object payload) {
        kafka.send("empleado.created", payload);
    }

    public void publishEmployeeUpdated(Object payload) {
        kafka.send("empleado.updated", payload);
    }

    public void publishEmployeeDeleted(Long id) {
        kafka.send("empleado.deleted", id);
    }

    public void publishContratoCreated(Object payload) {
        kafka.send("servicioContrato.contrato.created", payload);
    }

    public void publishContratoUpdated(Object payload) {
        kafka.send("servicioContrato.contrato.updated", payload);
    }

    public void publishContratoDeleted(Long id) {
        kafka.send("servicioContrato.contrato.deleted", id);
    }

    public void publishSagaCompleted(String sagaId) {
        kafka.send("saga.completed", sagaId, Map.of("sagaId", sagaId));
    }
}
