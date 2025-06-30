package ar.org.hospitalcuencaalta.servicio_empleado.servicio;

import ar.org.hospitalcuencaalta.servicio_empleado.excepcion.ResourceNotFoundException;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Empleado;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Sindicato;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.EmpleadoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.SindicatoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDetalleDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.SindicatoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class SindicatoService {

    private final SindicatoRepository repo;
    private final EmpleadoRepository empleadoRepo;
    private final SindicatoMapper mapper;

    /**
     * Alta de sindicato de un empleado. Declaramos @Transactional para asegurar
     * que la relación con el empleado y el guardado queden en una única
     * transacción. Así se simplifican futuras extensiones como eventos o
     * auditoría.
     */
    @Transactional
    public SindicatoDto create(Long empleadoId, SindicatoDto dto) {
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));

        Sindicato entidad = mapper.toEntity(dto);
        entidad.setEmpleado(empleado);

        Sindicato saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    /**
     * Actualización de la relación con el sindicato. Con @Transactional se
     * garantiza que la búsqueda del empleado y el guardado se completen juntos.
     */
    @Transactional
    public SindicatoDto update(Long empleadoId, Long id, SindicatoDto dto) {
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));

        Sindicato entidad = mapper.toEntity(dto);
        entidad.setId(id);
        entidad.setEmpleado(empleado);

        Sindicato saved = repo.save(entidad);
        return mapper.toDto(saved);
    }

    /**
     * Eliminación de la afiliación sindical. Se anota con @Transactional para
     * mantener la consistencia si se añaden operaciones adicionales en el
     * futuro.
     */
    @Transactional
    public void delete(Long empleadoId, Long id) {
        Sindicato entidad = repo.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Sindicato", id));
        repo.delete(entidad);
    }

    public SindicatoDetalleDto get(Long empleadoId, Long id) {
        Sindicato entidad = repo.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Sindicato", id));
        return mapper.toDetalleDto(entidad);
    }

    public Page<SindicatoDetalleDto> allDetailed(Long empleadoId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByEmpleadoId(empleadoId, pageable)
                .map(mapper::toDetalleDto);
    }

    public Page<SindicatoDto> all(Long empleadoId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByEmpleadoId(empleadoId, pageable)
                .map(mapper::toDto);
    }
}

