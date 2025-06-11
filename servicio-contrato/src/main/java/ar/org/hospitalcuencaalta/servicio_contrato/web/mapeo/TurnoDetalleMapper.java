package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_contrato.modelo.Turno;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.TurnoDetalleDto;
import org.mapstruct.Mapper;
import ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo.JornadaTrabajoDetalleMapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = JornadaTrabajoDetalleMapper.class)
public interface TurnoDetalleMapper {
    @Mapping(target = "jornadaTrabajoDetalleDto", source = "jornada")
    TurnoDetalleDto toDto(Turno entity);
}
