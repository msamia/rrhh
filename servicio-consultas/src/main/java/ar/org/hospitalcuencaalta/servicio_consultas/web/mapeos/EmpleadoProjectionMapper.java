package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EmpleadoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EmpleadoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpleadoProjectionMapper {
    EmpleadoProjection toEmpleado(EmpleadoDto dto);
}
