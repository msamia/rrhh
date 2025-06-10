package ar.org.hospitalcuencaalta.servicio_empleado.servicio;

import ar.org.hospitalcuencaalta.servicio_empleado.evento.EmpleadoEventPublisher;
import ar.org.hospitalcuencaalta.servicio_empleado.excepcion.ResourceNotFoundException;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Empleado;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.EmpleadoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.EmpleadoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private final EmpleadoMapper mapper;
    private final EmpleadoEventPublisher publisher;

    public EmpleadoDto create(EmpleadoDto dto) {
        Empleado entidad = mapper.toEntity(dto);
        Empleado guardado = repo.save(entidad);
        EmpleadoDto out = mapper.toDto(guardado);
        publisher.publishCreated(out);
        return out;
    }

    public List<EmpleadoDto> findAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public EmpleadoDto findById(Long id) {
        Empleado entidad = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", id));
        return mapper.toDto(entidad);
    }

    public EmpleadoDto update(Long id, EmpleadoDto dto) {
        dto.setId(id);
        Empleado entidad = mapper.toEntity(dto);
        Empleado actualizado = repo.save(entidad);
        EmpleadoDto out = mapper.toDto(actualizado);
        publisher.publishUpdated(out);
        return out;
    }

    public void delete(Long id) {
        Empleado entidad = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", id));
        EmpleadoDto dto = mapper.toDto(entidad);     // mapeo previo
        repo.delete(entidad);
        publisher.publishDeleted(dto);               // envío del DTO
    }

}
