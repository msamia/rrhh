package ar.org.hospitalcuencaalta.servicio_contrato.servicio;

import ar.org.hospitalcuencaalta.servicio_contrato.feign.EmpleadoClient;
import ar.org.hospitalcuencaalta.servicio_contrato.modelo.ContratoLaboral;
import ar.org.hospitalcuencaalta.servicio_contrato.repositorio.ContratoLaboralRepository;
import ar.org.hospitalcuencaalta.servicio_contrato.repositorio.EmpleadoRegistryRepository;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDto;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.EmpleadoRegistryDto;
import ar.org.hospitalcuencaalta.servicio_contrato.modelo.EmpleadoRegistry;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoLaboralRepository repo;
    private final EmpleadoClient empleadoClient;
    private final EmpleadoRegistryRepository empleadoRegistryRepo;

    /**
     * Alta de contrato laboral. Con @Transactional garantizamos que todas las
     * verificaciones, la sincronización y la persistencia se ejecuten en una
     * única transacción. Si en un futuro se publican eventos, se hará tras el
     * commit.
     */
    @Transactional
    public ContratoLaboralDto create(ContratoLaboralDto dto) {
        if (dto.getEmpleadoId() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "empleadoId es obligatorio");
        }

        if (dto.getFechaDesde() == null || dto.getFechaHasta() == null ||
                dto.getTipoContrato() == null || dto.getRegimen() == null ||
                dto.getSalario() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Campos obligatorios faltantes");
        }

        // 1) Verificar existencia del empleado localmente o consultando a servicio-empleado
        EmpleadoRegistryDto emp;
        try {
            emp = empleadoClient.getById(dto.getEmpleadoId());
        } catch (FeignException.NotFound nf) {
            throw new ResponseStatusException(NOT_FOUND,
                    "Empleado con id=" + dto.getEmpleadoId() + " no existe");
        } catch (Exception ex) {
            log.warn("[ContratoService] Error consultando a servicio-empleado: {}", ex.toString());
            throw new ResponseStatusException(SERVICE_UNAVAILABLE,
                    "No se pudo validar empleado");
        }

        if (!empleadoRegistryRepo.existsByIdAndDocumento(emp.getId(), emp.getDocumento())) {
            // Sincronizar localmente para futuros intentos
            empleadoRegistryRepo.save(EmpleadoRegistry.builder()
                    .id(emp.getId())
                    .documento(emp.getDocumento())
                    .nombre(emp.getNombre())
                    .apellido(emp.getApellido())
                    .build());
            // No se pudo confirmar recepción del evento
            throw new ResponseStatusException(SERVICE_UNAVAILABLE,
                    "Empleado no sincronizado aun");
        }

        // 2) Mapear DTO → entidad y guardar
        ContratoLaboral entidad = ContratoLaboral.builder()
                .tipoContrato(dto.getTipoContrato())
                .regimen(dto.getRegimen())
                .fechaDesde(dto.getFechaDesde())
                .fechaHasta(dto.getFechaHasta())
                .salario(dto.getSalario())
                .empleadoId(dto.getEmpleadoId())
                .build();

        ContratoLaboral guardado;
        try {
            guardado = repo.save(entidad);
        } catch (Exception ex) {
            throw new RuntimeException("Error al persistir contrato", ex);
        }

        // 3) Devolver DTO con id generado
        return ContratoLaboralDto.builder()
                .id(guardado.getId())
                .tipoContrato(guardado.getTipoContrato())
                .regimen(guardado.getRegimen())
                .fechaDesde(guardado.getFechaDesde())
                .fechaHasta(guardado.getFechaHasta())
                .salario(guardado.getSalario())
                .empleadoId(guardado.getEmpleadoId())
                .build();
    }

    public List<ContratoLaboralDto> findAll() {
        return repo.findAll().stream()
                .map(c -> ContratoLaboralDto.builder()
                        .id(c.getId())
                        .tipoContrato(c.getTipoContrato())
                        .regimen(c.getRegimen())
                        .fechaDesde(c.getFechaDesde())
                        .fechaHasta(c.getFechaHasta())
                        .salario(c.getSalario())
                        .empleadoId(c.getEmpleadoId())
                        .build())
                .toList();
    }

    /**
     * Actualización de contrato laboral. La anotación asegura que las lecturas
     * previas y el guardado queden en la misma transacción, evitando estados
     * intermedios inconsistentes.
     */
    @Transactional
    public ContratoLaboralDto update(Long id, ContratoLaboralDto dto) {
        ContratoLaboral existente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Contrato " + id + " no existe"));

        existente.setTipoContrato(dto.getTipoContrato());
        existente.setRegimen(dto.getRegimen());
        existente.setFechaDesde(dto.getFechaDesde());
        existente.setFechaHasta(dto.getFechaHasta());
        existente.setSalario(dto.getSalario());
        if (dto.getEmpleadoId() != null) {
            existente.setEmpleadoId(dto.getEmpleadoId());
        }

        ContratoLaboral guardado = repo.save(existente);

        return ContratoLaboralDto.builder()
                .id(guardado.getId())
                .tipoContrato(guardado.getTipoContrato())
                .regimen(guardado.getRegimen())
                .fechaDesde(guardado.getFechaDesde())
                .fechaHasta(guardado.getFechaHasta())
                .salario(guardado.getSalario())
                .empleadoId(guardado.getEmpleadoId())
                .build();
    }

    /**
     * Eliminación de un contrato. Se marca transaccional para facilitar
     * futuras tareas de compensación o eventos que dependan del borrado.
     */
    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Borrado de contratos por empleado. Declaramos @Transactional para que si
     * se agregan otros pasos (por ejemplo, registro histórico) se realicen bajo
     * la misma transacción.
     */
    @Transactional
    public void deleteByEmpleadoId(Long empleadoId) {
        repo.deleteByEmpleadoId(empleadoId);
    }
}
