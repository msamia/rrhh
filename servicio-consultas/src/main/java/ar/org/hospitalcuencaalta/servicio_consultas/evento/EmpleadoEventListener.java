package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EmpleadoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.EmpleadoProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EmpleadoDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.EmpleadoProjectionMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class EmpleadoEventListener {
    @Autowired private EmpleadoProjectionRepository repo;
    @Autowired private EmpleadoProjectionMapper mapper;

    @KafkaListener(topics = "empleado.created")
    public void onCreated(EmpleadoDto dto) {
        repo.save(mapper.toEmpleado(dto));
    }

    @KafkaListener(topics = "empleado.updated")
    public void onUpdated(EmpleadoDto dto) {
        repo.save(mapper.toEmpleado(dto));
    }

    @KafkaListener(topics = "empleado.deleted")
    public void onDeleted(Long id) {
        repo.deleteById(id);
    }
}
