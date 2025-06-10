package ar.org.hospitalcuencaalta.comunes.config.kafka;

import ar.org.hospitalcuencaalta.comunes.evento.SagaCompensationEvent;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración común de Kafka para producción, consumo y creación automática de topics.
 */
@Configuration
@EnableKafka
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class CommonKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:}")
    private String consumerGroupId;

    // ---------------- Producer ----------------

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // ---------------- Admin / Topics ----------------

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    // Topics for domain events
    @Bean
    public NewTopic empleadoCreatedTopic() {
        return new NewTopic("servicio-empleado.created", 3, (short) 1);
    }

    @Bean
    public NewTopic empleadoUpdatedTopic() {
        return new NewTopic("servicio-empleado.updated", 3, (short) 1);
    }

    @Bean
    public NewTopic empleadoDeletedTopic() {
        return new NewTopic("servicio-empleado.deleted", 3, (short) 1);
    }

    @Bean
    public NewTopic contratoCreatedTopic() {
        return new NewTopic("servicio-contrato.created", 3, (short) 1);
    }

    @Bean
    public NewTopic contratoUpdatedTopic() {
        return new NewTopic("servicio-contrato.updated", 3, (short) 1);
    }

    @Bean
    public NewTopic contratoDeletedTopic() {
        return new NewTopic("servicio-contrato.deleted", 3, (short) 1);
    }

    @Bean
    public NewTopic entrenamientoCreatedTopic() {
        return new NewTopic("servicio-entrenamiento.created", 3, (short) 1);
    }

    @Bean
    public NewTopic entrenamientoUpdatedTopic() {
        return new NewTopic("servicio-entrenamiento.updated", 3, (short) 1);
    }

    @Bean
    public NewTopic entrenamientoDeletedTopic() {
        return new NewTopic("servicio-entrenamiento.deleted", 3, (short) 1);
    }

    @Bean
    public NewTopic nominaCreatedTopic() {
        return new NewTopic("servicio-nomina.created", 3, (short) 1);
    }

    @Bean
    public NewTopic nominaDeletedTopic() {
        return new NewTopic("servicio-nomina.deleted", 3, (short) 1);
    }

    // Compensation topic
    @Bean
    public NewTopic sagaCompensatedTopic() {
        return new NewTopic("saga.compensated", 3, (short) 1);
    }

// ---------------- Consumer for SagaCompensationEvent ----------------

    @Bean
    public ConsumerFactory<String, SagaCompensationEvent> sagaCompensationConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // JsonDeserializer configuration
        JsonDeserializer<SagaCompensationEvent> deserializer =
                new JsonDeserializer<>(SagaCompensationEvent.class, false);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SagaCompensationEvent>
    sagaCompensationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SagaCompensationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(sagaCompensationConsumerFactory());
        return factory;
    }
}
