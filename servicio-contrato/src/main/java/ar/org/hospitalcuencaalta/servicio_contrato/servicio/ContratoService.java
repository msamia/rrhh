package ar.org.hospitalcuencaalta.servicio_contrato.servicio;

import ar.org.hospitalcuencaalta.servicio_contrato.feign.EmpleadoClient;
import ar.org.hospitalcuencaalta.servicio_contrato.modelo.ContratoLaboral;
import ar.org.hospitalcuencaalta.servicio_contrato.repositorio.ContratoLaboralRepository;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDto;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.EmpleadoRegistryDto;
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

    public ContratoLaboralDto create(ContratoLaboralDto dto) {
        if (dto.getEmpleadoId() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "empleadoId es obligatorio");
        }

        // 1) Verificar existencia del empleado en servicio-empleado
        EmpleadoRegistryDto empleado;
        try {
            empleado = empleadoClient.getById(dto.getEmpleadoId());
        } catch (Exception fe) {
            log.warn("[ContratoService] Error consultando a servicio-empleado: {}", fe.toString());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "No se pudo validar empleado");
        }
        if (empleado == null || empleado.getId() == null) {
            throw new ResponseStatusException(NOT_FOUND,
                    "Empleado con id=" + dto.getEmpleadoId() + " no existe");
        }

        // 2) Mapear DTO → entidad y guardar
        ContratoLaboral entidad = ContratoLaboral.builder()
                .tipoContrato(dto.getTipoContrato())
                .regimen(dto.getRegimen())
                .fechaDesde(dto.getFechaDesde())
                .fechaHasta(dto.getFechaHasta())
                .empleadoId(dto.getEmpleadoId())
                .build();

        ContratoLaboral guardado = repo.save(entidad);

        // 3) Devolver DTO con id generado
        return ContratoLaboralDto.builder()
                .id(guardado.getId())
                .tipoContrato(guardado.getTipoContrato())
                .regimen(guardado.getRegimen())
                .fechaDesde(guardado.getFechaDesde())
                .fechaHasta(guardado.getFechaHasta())
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
                        .empleadoId(c.getEmpleadoId())
                        .build())
                .toList();
    }

    public void deleteByEmpleadoId(Long empleadoId) {
        repo.deleteByEmpleadoId(empleadoId);
    }
}
