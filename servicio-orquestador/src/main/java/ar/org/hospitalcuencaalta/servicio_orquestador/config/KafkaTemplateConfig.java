package ar.org.hospitalcuencaalta.servicio_orquestador.config;

import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.CompensacionDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaTemplateConfig {

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public KafkaTemplate<String, CompensacionDto> compensacionKafkaTemplate(
            ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate(producerFactory);
    }

    /**
     * Generic {@link KafkaTemplate} bean used by components that publish
     * different types of domain events. We expose it explicitly because the
     * custom {@code compensacionKafkaTemplate} bean prevents Spring Boot's
     * auto-configuration from creating the default template.
     */
    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate(producerFactory);
    }
}
