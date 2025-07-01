package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.EmpleadoConceptoProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EmpleadoConceptoDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.EmpleadoConceptoProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoConceptoEventListener {
    @Autowired private EmpleadoConceptoProjectionRepository repo;
    @Autowired private EmpleadoConceptoProjectionMapper mapper;

    @KafkaListener(topics = "servicioNomina.empleadoConcepto.created")
    public void onCreated(EmpleadoConceptoDto dto) {
        repo.save(mapper.toEmpleadoConcepto(dto));
    }
}
