package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Departamento;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {
    DepartamentoDto toDto(Departamento e);

    Departamento toEntity(DepartamentoDto d);
}
