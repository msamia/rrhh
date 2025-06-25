package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Documentacion;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.EmpleadoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        uses = EmpleadoMapper.class)
public interface DocumentacionMapper {
    DocumentacionDto toDto(Documentacion e);

    DocumentacionDetalleDto toDetalleDto(Documentacion e);

    Documentacion toEntity(DocumentacionDto d);
}
