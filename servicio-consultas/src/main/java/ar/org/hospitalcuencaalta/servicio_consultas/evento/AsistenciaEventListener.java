package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.AsistenciaProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.AsistenciaDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.AsistenciaProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class AsistenciaEventListener {
    @Autowired
    private AsistenciaProjectionRepository repo;
    @Autowired
    private AsistenciaProjectionMapper mapper;

    @KafkaListener(topics = "servicioContrato.asistencia.created")
    public void onCreated(AsistenciaDto dto) {
        repo.save(mapper.toAsistencia(dto));
    }
}