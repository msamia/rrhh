package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.LicenciaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.LicenciaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LicenciaProjectionMapper {
    LicenciaProjection toLicencia(LicenciaDto dto);
}
