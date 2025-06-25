package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Departamento;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDetalleDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.EmpleadoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        uses = EmpleadoMapper.class)
public interface DepartamentoMapper {
    DepartamentoDto toDto(Departamento e);

    DepartamentoDetalleDto toDetalleDto(Departamento e);

    Departamento toEntity(DepartamentoDto d);
}
