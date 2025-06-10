package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.VacacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.VacacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VacacionProjectionMapper {
    VacacionProjection toVacacion(VacacionDto dto);
}
