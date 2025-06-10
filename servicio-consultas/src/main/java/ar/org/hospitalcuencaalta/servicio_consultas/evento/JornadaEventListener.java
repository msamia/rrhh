package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.JornadaProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.JornadaTrabajoDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.JornadaProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class JornadaEventListener {
    @Autowired
    private JornadaProjectionRepository repo;
    @Autowired
    private JornadaProjectionMapper mapper;

    @KafkaListener(topics = "servicioEntrenamiento.scheduled")
    public void onCreated(JornadaTrabajoDto dto) {
        repo.save(mapper.toJornada(dto));
    }

    @KafkaListener(topics = "servicioEntrenamiento.updated")
    public void onUpdated(JornadaTrabajoDto dto) {
        repo.save(mapper.toJornada(dto));
    }
}
