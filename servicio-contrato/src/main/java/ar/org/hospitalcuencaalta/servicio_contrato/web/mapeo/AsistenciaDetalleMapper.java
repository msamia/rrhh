package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.org.hospitalcuencaalta.servicio_contrato.modelo.Asistencia;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.AsistenciaDetalleDto;

@Component
public class AsistenciaDetalleMapper {

    @Autowired
    private TurnoDetalleMapper turnoMapper;

    @Autowired
    private EmpleadoRegistryMapper empleadoMapper;

    public AsistenciaDetalleDto toDetalleDto(Asistencia a) {
        if (a == null) {
            return null;
        }
        AsistenciaDetalleDto dto = AsistenciaDetalleDto.builder()
                .id(a.getId())
                .marcacionEntrada(a.getMarcacionEntrada())
                .marcacionSalida(a.getMarcacionSalida())
                .estado(a.getEstado())
                .turnoDetalleDto(turnoMapper.toDto(a.getTurno()))
                .empleado(empleadoMapper.toDto(a.getEmpleado()))
                .build();
        return dto;
    }
}