package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.JornadaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.JornadaTrabajoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JornadaProjectionMapper {
    @Mapping(target = "contrato.id", source = "contratoId")
    JornadaProjection toJornada(JornadaTrabajoDto dto);
}
