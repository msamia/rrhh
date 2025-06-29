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

    public EvaluacionDto create(EvaluacionDto dto) {
        if (!empleadoRegistryRepo.existsById(dto.getEmpleadoId())) {
            try {
                EmpleadoRegistryDto emp = empleadoClient.getById(dto.getEmpleadoId());
                empleadoRegistryRepo.save(EmpleadoRegistry.builder()
                        .id(emp.getId())
                        .legajo(emp.getLegajo())
                        .nombre(emp.getNombre())
                        .apellido(emp.getApellido())
                        .build());
            } catch (FeignException.NotFound nf) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Empleado con id=" + dto.getEmpleadoId() + " no existe");
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Error al validar empleado", ex);
            }
        }
        if (dto.getEvaluadorId() != null && !empleadoRegistryRepo.existsById(dto.getEvaluadorId())) {
            try {
                EmpleadoRegistryDto emp = empleadoClient.getById(dto.getEvaluadorId());
                empleadoRegistryRepo.save(EmpleadoRegistry.builder()
                        .id(emp.getId())
                        .legajo(emp.getLegajo())
                        .nombre(emp.getNombre())
                        .apellido(emp.getApellido())
                        .build());
            } catch (FeignException.NotFound nf) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Empleado con id=" + dto.getEvaluadorId() + " no existe");
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Error al validar empleado", ex);
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

