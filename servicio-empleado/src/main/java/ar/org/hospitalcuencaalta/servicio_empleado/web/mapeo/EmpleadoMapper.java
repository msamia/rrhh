package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Empleado;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper {
    EmpleadoDto toDto(Empleado e);

    Empleado toEntity(EmpleadoDto d);
}

