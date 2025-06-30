package ar.org.hospitalcuencaalta.servicio_empleado.servicio;

import ar.org.hospitalcuencaalta.servicio_empleado.excepcion.ResourceNotFoundException;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Departamento;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Empleado;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.DepartamentoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.EmpleadoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDetalleDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.DepartamentoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository repo;
    private final EmpleadoRepository empleadoRepo;
    private final DepartamentoMapper mapper;

    /**
     * Alta de departamento asociado al empleado. Usamos @Transactional para que
     * el vínculo con el empleado y el guardado se ejecuten de forma atómica. Si
     * en el futuro se publica un evento, quedará dentro de la misma transacción.
     */
    @Transactional
    public DepartamentoDto create(Long empleadoId, DepartamentoDto dto) {
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));

        Departamento entidad = mapper.toEntity(dto);
        entidad.setEmpleado(empleado);

        Departamento saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    /**
     * Actualización de departamento. Se asegura que la carga del empleado y la
     * persistencia de los cambios se realicen dentro de una única transacción.
     */
    @Transactional
    public DepartamentoDto update(Long empleadoId, Long id, DepartamentoDto dto) {
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));

        Departamento entidad = mapper.toEntity(dto);
        entidad.setId(id);
        entidad.setEmpleado(empleado);

        Departamento saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    /**
     * Eliminación de departamento. Declarar la transacción permite añadir en el
     * futuro operaciones complementarias sin perder atomicidad.
     */
    @Transactional
    public void delete(Long empleadoId, Long id) {
        Departamento entidad = repo.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        repo.delete(entidad);
    }

    public DepartamentoDetalleDto get(Long empleadoId, Long id) {
        Departamento entidad = repo.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        return mapper.toDetalleDto(entidad);
    }

    public Page<DepartamentoDetalleDto> allDetailed(Long empleadoId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByEmpleadoId(empleadoId, pageable)
                .map(mapper::toDetalleDto);
    }

    public Page<DepartamentoDto> all(Long empleadoId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByEmpleadoId(empleadoId, pageable)
                .map(mapper::toDto);
    }
}

