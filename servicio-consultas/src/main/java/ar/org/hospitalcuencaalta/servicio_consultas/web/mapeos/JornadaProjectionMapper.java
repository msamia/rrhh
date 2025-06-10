package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.JornadaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.JornadaTrabajoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JornadaProjectionMapper {
    JornadaProjection toJornada(JornadaTrabajoDto dto);
}
