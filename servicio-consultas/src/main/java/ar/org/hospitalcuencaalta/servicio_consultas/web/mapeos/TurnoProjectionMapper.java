package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.TurnoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.TurnoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TurnoProjectionMapper {
    TurnoProjection toTurno(TurnoDto dto);
}
