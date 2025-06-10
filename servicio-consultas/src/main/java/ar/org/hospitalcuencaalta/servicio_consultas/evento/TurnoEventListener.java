package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.TurnoProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.TurnoDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.TurnoProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class TurnoEventListener {
    @Autowired
    private TurnoProjectionRepository repo;
    @Autowired
    private TurnoProjectionMapper mapper;

    @KafkaListener(topics = "servicioEntrenamiento.turno.created")
    public void onCreated(TurnoDto dto) {
        repo.save(mapper.toTurno(dto));
    }
}
