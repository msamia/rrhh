package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.VacacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.VacacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VacacionProjectionMapper {
    @Mapping(target = "empleado.id", source = "empleadoId")
    VacacionProjection toVacacion(VacacionDto dto);
}
