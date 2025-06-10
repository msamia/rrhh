package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.AsistenciaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.AsistenciaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AsistenciaProjectionMapper {
    AsistenciaProjection toAsistencia(AsistenciaDto dto);
}
