package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.AsistenciaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.AsistenciaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AsistenciaProjectionMapper {
    @Mapping(target = "turno.id", source = "turnoId")
    @Mapping(target = "empleado.id", source = "empleadoId")
    AsistenciaProjection toAsistencia(AsistenciaDto dto);
}
