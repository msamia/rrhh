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

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoLaboralRepository repo;
    private final EmpleadoClient empleadoClient;
    private final EmpleadoRegistryRepository empleadoRegistryRepo;

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
        if (!empleadoRegistryRepo.existsById(dto.getEmpleadoId())) {
            try {
                EmpleadoRegistryDto emp = empleadoClient.getById(dto.getEmpleadoId());
                // Sincronizar localmente para futuros intentos
                empleadoRegistryRepo.save(EmpleadoRegistry.builder()
                        .id(emp.getId())
                        .legajo(emp.getLegajo())
                        .nombre(emp.getNombre())
                        .apellido(emp.getApellido())
                        .build());
                // No se pudo confirmar recepción del evento
                throw new ResponseStatusException(SERVICE_UNAVAILABLE,
                        "Empleado no sincronizado aun");
            } catch (FeignException.NotFound nf) {
                throw new ResponseStatusException(NOT_FOUND,
                        "Empleado con id=" + dto.getEmpleadoId() + " no existe");
            } catch (Exception ex) {
                log.warn("[ContratoService] Error consultando a servicio-empleado: {}", ex.toString());
                throw new ResponseStatusException(SERVICE_UNAVAILABLE,
                        "No se pudo validar empleado");
            }
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

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void deleteByEmpleadoId(Long empleadoId) {
        repo.deleteByEmpleadoId(empleadoId);
    }
}
