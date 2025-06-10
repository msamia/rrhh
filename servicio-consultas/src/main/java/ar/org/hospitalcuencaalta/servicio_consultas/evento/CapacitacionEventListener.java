package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.CapacitacionProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.CapacitacionDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.CapacitacionProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CapacitacionEventListener {
    @Autowired
    private CapacitacionProjectionRepository repo;
    @Autowired
    private CapacitacionProjectionMapper mapper;

    @KafkaListener(topics = "servicioEntrenamiento.scheduled")
    public void onCreated(CapacitacionDto dto) {
        repo.save(mapper.toCapacitacion(dto));
    }
}