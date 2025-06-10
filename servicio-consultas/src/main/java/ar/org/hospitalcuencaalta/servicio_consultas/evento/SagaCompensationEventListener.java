package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.comunes.evento.SagaCompensationEvent;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.CapacitacionProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.ContratoProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.EmpleadoProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.LiquidacionProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SagaCompensationEventListener {
    @Autowired
    private EmpleadoProjectionRepository empRepo;
    @Autowired private ContratoProjectionRepository conRepo;
    @Autowired private CapacitacionProjectionRepository capRepo;
    @Autowired private LiquidacionProjectionRepository liqRepo;

    @KafkaListener(
            topics = "${spring.kafka.topic.saga.compensated}",
            containerFactory = "sagaCompensationKafkaListenerContainerFactory"
    )
    public void onSagaCompensated(SagaCompensationEvent evt) {
        // Actualizar o eliminar proyecciones según evt.get*Id()
    }
}
