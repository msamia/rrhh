package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Puesto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PuestoMapper {
    PuestoDto toDto(Puesto e);

    Puesto toEntity(PuestoDto d);
}
