package ar.org.hospitalcuencaalta.servicio_empleado.servicio;

import ar.org.hospitalcuencaalta.servicio_empleado.evento.EmpleadoEventPublisher;
import ar.org.hospitalcuencaalta.servicio_empleado.excepcion.ResourceNotFoundException;
import ar.org.hospitalcuencaalta.servicio_empleado.excepcion.BadRequestException;
import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Empleado;
import ar.org.hospitalcuencaalta.servicio_empleado.repositorio.EmpleadoRepository;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.EmpleadoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private final EmpleadoMapper mapper;
    private final EmpleadoEventPublisher publisher;

    /**
     * Operación de alta del empleado. Se marca transaccional para asegurar que la
     * inserción y la posterior publicación del evento se ejecuten de forma
     * atómica. Si en el futuro se añaden pasos adicionales (por ejemplo,
     * registros en otras tablas) quedarán dentro de la misma transacción.
     */
    @Transactional
    public EmpleadoDto create(EmpleadoDto dto) {
        try {
            if (repo.findByDocumento(dto.getDocumento()).isPresent()) {
                throw new BadRequestException(
                        "Ya existe un empleado con documento '" + dto.getDocumento() + "'");
            }

            Empleado entidad = mapper.toEntity(dto);
            Empleado guardado = repo.save(entidad);
            EmpleadoDto out = mapper.toDto(guardado);
            publisher.publishCreated(out);
            return out;
        } catch (Exception ex) {
            throw new RuntimeException("Error al persistir empleado", ex);
        }
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

    public EmpleadoDto findByDocumento(String documento) {
        Empleado entidad = repo.findByDocumento(documento)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado documento", documento));
        return mapper.toDto(entidad);
    }

    /**
     * Actualización del empleado. Se utiliza @Transactional para que la lectura,
     * validaciones y guardado se efectúen en la misma transacción y el evento se
     * publique solo al confirmarse el commit.
     */
    @Transactional
    public EmpleadoDto update(Long id, EmpleadoDto dto) {
        Empleado existente = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", id));

        repo.findByDocumento(dto.getDocumento())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new BadRequestException(
                            "Ya existe un empleado con documento '" + dto.getDocumento() + "'");
                });

        dto.setId(id);
        Empleado entidad = mapper.toEntity(dto);
        Empleado actualizado = repo.save(entidad);
        EmpleadoDto out = mapper.toDto(actualizado);
        publisher.publishUpdated(out);
        return out;
    }

    /**
     * Borrado de empleado. Se declara transaccional para garantizar que la
     * eliminación y el evento asociado ocurran juntos. Cualquier modificación
     * futura sobre tablas relacionadas también quedará protegida.
     */
    @Transactional
    public void delete(Long id) {
        Empleado entidad = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", id));
        repo.delete(entidad);
        publisher.publishDeleted(id);
    }

}
