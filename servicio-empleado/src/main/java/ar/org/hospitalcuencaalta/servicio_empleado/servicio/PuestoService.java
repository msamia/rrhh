package ar.org.hospitalcuencaalta.servicio_empleado.servicio;

import ar.org.hospitalcuencaalta.servicio_empleado.excepcion.ResourceNotFoundException;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Empleado;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Puesto;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.EmpleadoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.PuestoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDetalleDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.PuestoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class PuestoService {

    private final PuestoRepository repo;
    private final EmpleadoRepository empleadoRepo;
    private final PuestoMapper mapper;

    /**
     * Alta de puesto. Se ejecuta de forma transaccional para asegurar que la
     * asociación con el empleado y el guardado se completen sin errores. Futuras
     * acciones (por ejemplo, eventos) quedarán cubiertas en la misma transacción.
     */
    @Transactional
    public PuestoDto create(Long empleadoId, PuestoDto dto) {
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));

        Puesto entidad = mapper.toEntity(dto);
        entidad.setEmpleado(empleado);

        Puesto saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    /**
     * Actualiza un puesto existente. Con @Transactional se encapsula la carga
     * del empleado y la persistencia de la entidad en una única transacción.
     */
    @Transactional
    public PuestoDto update(Long empleadoId, Long id, PuestoDto dto) {
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));

        Puesto entidad = mapper.toEntity(dto);
        entidad.setId(id);
        entidad.setEmpleado(empleado);

        Puesto saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    /**
     * Borrado de puesto. La anotación posibilita sumar operaciones adicionales
     * sin perder atomicidad, por ejemplo enviar eventos de dominio.
     */
    @Transactional
    public void delete(Long empleadoId, Long id) {
        Puesto entidad = repo.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto", id));
        repo.delete(entidad);
    }

    public PuestoDetalleDto get(Long empleadoId, Long id) {
        Puesto entidad = repo.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto", id));
        return mapper.toDetalleDto(entidad);
    }

    public Page<PuestoDetalleDto> allDetailed(Long empleadoId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByEmpleadoId(empleadoId, pageable)
                .map(mapper::toDetalleDto);
    }

    public Page<PuestoDto> all(Long empleadoId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByEmpleadoId(empleadoId, pageable)
                .map(mapper::toDto);
    }
}

