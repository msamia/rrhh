package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.CapacitacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.CapacitacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacitacionProjectionMapper {
    @Mapping(target = "empleado.id", source = "empleadoId")
    CapacitacionProjection toCapacitacion(CapacitacionDto dto);
}
