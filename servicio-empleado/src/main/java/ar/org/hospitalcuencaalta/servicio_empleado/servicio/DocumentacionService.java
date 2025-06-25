package ar.org.hospitalcuencaalta.servicio_empleado.servicio;

import ar.org.hospitalcuencaalta.servicio_empleado.excepcion.ResourceNotFoundException;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Documentacion;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Empleado;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.DocumentacionRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.EmpleadoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.DocumentacionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class DocumentacionService {

    private final DocumentacionRepository repo;
    private final EmpleadoRepository empleadoRepo;
    private final DocumentacionMapper mapper;

    public DocumentacionDto create(Long empleadoId, DocumentacionDto dto) {
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));

        Documentacion entidad = mapper.toEntity(dto);
        entidad.setEmpleado(empleado);

        Documentacion saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    public DocumentacionDto update(Long empleadoId, Long id, DocumentacionDto dto) {
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));

        Documentacion entidad = mapper.toEntity(dto);
        entidad.setId(id);
        entidad.setEmpleado(empleado);

        Documentacion saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    public void delete(Long empleadoId, Long id) {
        Documentacion entidad = repo.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Documentacion", id));
        repo.delete(entidad);
    }

    public DocumentacionDetalleDto get(Long empleadoId, Long id) {
        Documentacion entidad = repo.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Documentacion", id));
        return mapper.toDetalleDto(entidad);
    }

    public Page<DocumentacionDetalleDto> allDetailed(Long empleadoId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByEmpleadoId(empleadoId, pageable)
                .map(mapper::toDetalleDto);
    }

    public Page<DocumentacionDto> all(Long empleadoId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByEmpleadoId(empleadoId, pageable)
                .map(mapper::toDto);
    }
}

