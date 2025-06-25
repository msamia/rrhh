package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Sindicato;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDetalleDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.EmpleadoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        uses = EmpleadoMapper.class)
public interface SindicatoMapper {
    SindicatoDto toDto(Sindicato e);

    SindicatoDetalleDto toDetalleDto(Sindicato e);

    Sindicato toEntity(SindicatoDto d);
}
