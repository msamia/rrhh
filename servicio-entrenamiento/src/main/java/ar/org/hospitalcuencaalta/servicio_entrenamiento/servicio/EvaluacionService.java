package ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EvaluacionDesempeno;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio.EvaluacionDesempenoRepository;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.EvaluacionDetalleMapper;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.EvaluacionMapper;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio.EmpleadoRegistryRepository;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.feign.EmpleadoClient;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EmpleadoRegistryDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EmpleadoRegistry;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluacionService {
    @Autowired
    private EvaluacionDesempenoRepository repo;
    @Autowired
    private EvaluacionMapper mapper;
    @Autowired
    private EvaluacionDetalleMapper detalleMapper;
    @Autowired
    private EmpleadoRegistryRepository empleadoRegistryRepo;
    @Autowired
    private EmpleadoClient empleadoClient;
    @Autowired
    private KafkaTemplate<String, Object> kafka;

    /**
     * Creación de una evaluación de desempeño. Se declara @Transactional para
     * que todas las verificaciones y la persistencia ocurran juntas. También
     * permite publicar el evento solo tras confirmar el commit.
     */
    @Transactional
    public EvaluacionDto create(EvaluacionDto dto) {
        EmpleadoRegistryDto emp;
        try {
            emp = empleadoClient.getById(dto.getEmpleadoId());
        } catch (FeignException.NotFound nf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Empleado con id=" + dto.getEmpleadoId() + " no existe");
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Error al validar empleado", ex);
        }

        if (!empleadoRegistryRepo.existsByIdAndDocumento(emp.getId(), emp.getDocumento())) {
            empleadoRegistryRepo.save(EmpleadoRegistry.builder()
                    .id(emp.getId())
                    .documento(emp.getDocumento())
                    .nombre(emp.getNombre())
                    .apellido(emp.getApellido())
                    .build());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Empleado no sincronizado aun");
        }
        if (dto.getEvaluadorId() != null) {
            EmpleadoRegistryDto evaluador;
            try {
                evaluador = empleadoClient.getById(dto.getEvaluadorId());
            } catch (FeignException.NotFound nf) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Empleado con id=" + dto.getEvaluadorId() + " no existe");
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Error al validar empleado", ex);
            }

            if (!empleadoRegistryRepo.existsByIdAndDocumento(evaluador.getId(), evaluador.getDocumento())) {
                empleadoRegistryRepo.save(EmpleadoRegistry.builder()
                        .id(evaluador.getId())
                        .documento(evaluador.getDocumento())
                        .nombre(evaluador.getNombre())
                        .apellido(evaluador.getApellido())
                        .build());
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Empleado no sincronizado aun");
            }
        }

        EvaluacionDesempeno e = mapper.toEntity(dto);
        EvaluacionDesempeno saved = repo.save(e);
        EvaluacionDto out = mapper.toDto(saved);
        // publicar evento de evaluación en el tópico consumido por servicio-consultas
        kafka.send("servicioEntrenamiento.evaluated", out);
        return out;
    }

    public List<EvaluacionDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public EvaluacionDetalleDto getDetalle(Long id) {
        return detalleMapper.toDto(repo.findById(id).orElseThrow());
    }
}

