package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Sindicato;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SindicatoMapper {
    SindicatoDto toDto(Sindicato e);

    Sindicato toEntity(SindicatoDto d);
}
