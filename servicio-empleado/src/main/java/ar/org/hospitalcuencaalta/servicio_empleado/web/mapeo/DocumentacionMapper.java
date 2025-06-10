package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Documentacion;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentacionMapper {
    DocumentacionDto toDto(Documentacion e);

    Documentacion toEntity(DocumentacionDto d);
}
