package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.VacacionProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.VacacionDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.VacacionProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class VacacionEventListener {
    @Autowired
    private VacacionProjectionRepository repo;
    @Autowired
    private VacacionProjectionMapper mapper;

    @KafkaListener(topics = "servicioContrato.vacacion.created")
    public void onCreated(VacacionDto dto) {
        repo.save(mapper.toVacacion(dto));
    }
}
