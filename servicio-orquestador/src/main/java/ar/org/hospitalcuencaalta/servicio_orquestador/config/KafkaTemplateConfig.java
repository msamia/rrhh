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
}
