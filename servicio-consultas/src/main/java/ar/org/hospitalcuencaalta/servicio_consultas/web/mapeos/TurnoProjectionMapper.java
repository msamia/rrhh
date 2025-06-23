package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.TurnoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.TurnoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TurnoProjectionMapper {
    @Mapping(target = "jornada.id", source = "jornadaId")
    TurnoProjection toTurno(TurnoDto dto);
}
