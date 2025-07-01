package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.ConceptoLiquidacionProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.EmpleadoConceptoProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.EmpleadoProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EmpleadoConceptoDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.EmpleadoConceptoProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoConceptoEventListener {
    @Autowired private EmpleadoConceptoProjectionRepository repo;
    @Autowired private EmpleadoConceptoProjectionMapper mapper;
    @Autowired private EmpleadoProjectionRepository empleadoRepo;
    @Autowired private ConceptoLiquidacionProjectionRepository conceptoRepo;

    @KafkaListener(topics = "servicioNomina.empleadoConcepto.created")
    public void onCreated(EmpleadoConceptoDto dto) {
        var ec = mapper.toEmpleadoConcepto(dto);
        ec.setEmpleado(empleadoRepo.getReferenceById(dto.getEmpleadoId()));
        ec.setConcepto(conceptoRepo.getReferenceById(dto.getConceptoId()));
        repo.save(ec);
    }
}
