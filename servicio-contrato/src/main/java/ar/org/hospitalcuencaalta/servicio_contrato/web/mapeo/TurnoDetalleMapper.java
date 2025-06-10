package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_contrato.modelo.Turno;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.TurnoDetalleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TurnoDetalleMapper {
    TurnoDetalleDto toDto(Turno entity);
}
