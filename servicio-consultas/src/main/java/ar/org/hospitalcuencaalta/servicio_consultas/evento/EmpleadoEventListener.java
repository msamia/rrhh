package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EmpleadoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.*;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EmpleadoDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.EmpleadoProjectionMapper;
import java.util.Map;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class EmpleadoEventListener {
    @Autowired private EmpleadoProjectionRepository repo;
    @Autowired private ContratoProjectionRepository contratoRepo;
    @Autowired private JornadaProjectionRepository jornadaRepo;
    @Autowired private TurnoProjectionRepository turnoRepo;
    @Autowired private AsistenciaProjectionRepository asistenciaRepo;
    @Autowired private CapacitacionProjectionRepository capacitacionRepo;
    @Autowired private LicenciaProjectionRepository licenciaRepo;
    @Autowired private VacacionProjectionRepository vacacionRepo;
    @Autowired private EvaluacionProjectionRepository evaluacionRepo;
    @Autowired private ConceptoLiquidacionProjectionRepository conceptoRepo;
    @Autowired private LiquidacionProjectionRepository liquidacionRepo;
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
    public void onDeleted(Object payload) {
        Long id = null;
        if (payload instanceof Number n) {
            id = n.longValue();
        } else if (payload instanceof EmpleadoDto dto) {
            id = dto.getId();
        } else if (payload instanceof Map<?, ?> map) {
            Object value = map.get("id");
            if (value instanceof Number n) {
                id = n.longValue();
            }
        }
        if (id != null) {
            // Eliminar dependencias para respetar las claves foráneas
            asistenciaRepo.deleteByEmpleado_Id(id);
            turnoRepo.deleteByJornada_Contrato_Empleado_Id(id);
            jornadaRepo.deleteByContrato_Empleado_Id(id);
            capacitacionRepo.deleteByEmpleado_Id(id);
            licenciaRepo.deleteByEmpleado_Id(id);
            vacacionRepo.deleteByEmpleado_Id(id);
            evaluacionRepo.deleteByEmpleado_Id(id);
            evaluacionRepo.deleteByEvaluador_Id(id);
            conceptoRepo.deleteByLiquidacion_Empleado_Id(id);
            liquidacionRepo.deleteByEmpleado_Id(id);
            contratoRepo.deleteByEmpleado_Id(id);
            repo.deleteById(id);
        }
    }
}
