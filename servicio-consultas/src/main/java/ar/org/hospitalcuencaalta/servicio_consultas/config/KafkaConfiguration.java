package ar.org.hospitalcuencaalta.servicio_consultas.config;

import ar.org.hospitalcuencaalta.comunes.evento.SagaCompensationEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Bean
    public KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry() {
        return new KafkaListenerEndpointRegistry();
    }

    @Bean
    public NewTopic empleadoCreatedTopic() {
        return TopicBuilder.name("empleado.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic empleadoUpdatedTopic() {
        return TopicBuilder.name("empleado.updated").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic empleadoDeletedTopic() {
        return TopicBuilder.name("empleado.deleted").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic contratoCreatedTopic() {
        return TopicBuilder.name("servicioContrato.contrato.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic contratoUpdatedTopic() {
        return TopicBuilder.name("servicioContrato.contrato.updated").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic contratoDeletedTopic() {
        return TopicBuilder.name("servicioContrato.contrato.deleted").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic asistenciaCreatedTopic() {
        return TopicBuilder.name("servicioContrato.asistencia.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic licenciaCreatedTopic() {
        return TopicBuilder.name("servicioContrato.licencia.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic vacacionCreatedTopic() {
        return TopicBuilder.name("servicioContrato.vacacion.created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic entrenamientoScheduledTopic() {
        return TopicBuilder.name("servicioEntrenamiento.scheduled").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic entrenamientoUpdatedTopic() {
        return TopicBuilder.name("servicioEntrenamiento.updated").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic entrenamientoEvaluatedTopic() {
        return TopicBuilder.name("servicioEntrenamiento.evaluated").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic turnoCreatedTopic() {
        return TopicBuilder.name("servicioEntrenamiento.turno.created").partitions(1).replicas(1).build();
    }



    @Bean
    public NewTopic nominaGeneratedTopic() {
        return TopicBuilder.name("servicioNomina.nomina.generated").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic conceptoAddedTopic() {
        return TopicBuilder.name("servicioNomina.added").partitions(1).replicas(1).build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SagaCompensationEvent>
    sagaCompensationKafkaListenerContainerFactory(
            ConsumerFactory<String, SagaCompensationEvent> cf) {
        ConcurrentKafkaListenerContainerFactory<String, SagaCompensationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        return factory;
    }
}