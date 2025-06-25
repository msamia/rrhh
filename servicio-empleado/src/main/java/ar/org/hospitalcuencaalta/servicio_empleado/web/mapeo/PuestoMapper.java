package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Puesto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDetalleDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.EmpleadoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        uses = EmpleadoMapper.class)
public interface PuestoMapper {
    PuestoDto toDto(Puesto e);

    PuestoDetalleDto toDetalleDto(Puesto e);

    Puesto toEntity(PuestoDto d);
}
