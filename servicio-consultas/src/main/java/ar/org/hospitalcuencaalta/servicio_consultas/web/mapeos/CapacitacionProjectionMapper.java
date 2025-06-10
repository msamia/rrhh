package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.CapacitacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.CapacitacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapacitacionProjectionMapper {
    CapacitacionProjection toCapacitacion(CapacitacionDto dto);
}
