package ar.org.hospitalcuencaalta.servicio_nomina.servicio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.ConceptoLiquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.modelo.EmpleadoConcepto;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.ConceptoLiquidacionRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.EmpleadoConceptoRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.EmpleadoConceptoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoConceptoService {
    @Autowired
    private EmpleadoConceptoRepository repo;
    @Autowired
    private ConceptoLiquidacionRepository conceptoRepo;
    @Autowired
    private KafkaTemplate<String, Object> kafka;

    /**
     * Asigna un concepto a un empleado. Se declara transaccional para asegurar
     * que la búsqueda del concepto y el alta en la tabla intermedia queden en la
     * misma unidad de trabajo. Esto previene inconsistencias si en el futuro se
     * agregan otras operaciones.
     */
    @Transactional
    public EmpleadoConceptoDto asignarConcepto(Long empleadoId, Long conceptoId) {
        ConceptoLiquidacion concepto = conceptoRepo.findById(conceptoId).orElseThrow();
        EmpleadoConcepto ec = EmpleadoConcepto.builder()
                .empleadoId(empleadoId)
                .concepto(concepto)
                .build();
        EmpleadoConcepto saved = repo.save(ec);
        EmpleadoConceptoDto out = EmpleadoConceptoDto.builder()
                .id(saved.getId())
                .empleadoId(saved.getEmpleadoId())
                .conceptoId(conceptoId)
                .build();
        kafka.send("servicioNomina.empleadoConcepto.created", out);
        return out;
    }
}
