package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.EvaluacionProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EvaluacionDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.EvaluacionProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EvaluacionEventListener {
    @Autowired
    private EvaluacionProjectionRepository repo;
    @Autowired
    private EvaluacionProjectionMapper mapper;

    @KafkaListener(topics = "servicioEntrenamiento.evaluated")
    public void onCreated(EvaluacionDto dto) {
        repo.save(mapper.toEvaluacion(dto));
    }
}
