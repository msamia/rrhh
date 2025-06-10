package ar.org.hospitalcuencaalta.servicio_orquestador.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {
    private final KafkaTemplate<String, Object> kafka;
    public DomainEventPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }
    public <T> void publish(String topic, T payload) {
        kafka.send(topic, payload);
    }
}
