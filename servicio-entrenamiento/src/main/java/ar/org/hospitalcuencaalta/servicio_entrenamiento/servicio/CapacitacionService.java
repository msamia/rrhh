package ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.Capacitacion;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio.CapacitacionRepository;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.CapacitacionDetalleMapper;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.CapacitacionMapper;
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
public class CapacitacionService {
    @Autowired
    private CapacitacionRepository repo;
    @Autowired
    private CapacitacionMapper mapper;
    @Autowired
    private CapacitacionDetalleMapper detalleMapper;
    @Autowired
    private EmpleadoRegistryRepository empleadoRegistryRepo;
    @Autowired
    private EmpleadoClient empleadoClient;
    @Autowired
    private KafkaTemplate<String, Object> kafka;

    public CapacitacionDto create(CapacitacionDto dto) {
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

        Capacitacion e = mapper.toEntity(dto);
        Capacitacion saved = repo.save(e);
        CapacitacionDto out = mapper.toDto(saved);
        // publicar evento de dominio en el tópico escuchado por servicio-consultas
        kafka.send("servicioEntrenamiento.scheduled", out);
        return out;
    }

    public List<CapacitacionDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public CapacitacionDetalleDto getDetalle(Long id) {
        return detalleMapper.toDto(repo.findById(id).orElseThrow());
    }
}

