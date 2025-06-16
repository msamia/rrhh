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

    public void publishEmployeeCreated(Long empleadoId, Object payload) {
        kafka.send("empleado.created", empleadoId.toString(), payload);
    }

    public void publishContratoCreated(Long contratoId, Object payload) {
        kafka.send("servicioContrato.contrato.created", contratoId.toString(), payload);
    }

    public void publishSagaCompleted(String sagaId) {
        kafka.send("saga.completed", sagaId, Map.of("sagaId", sagaId));
    }
}
