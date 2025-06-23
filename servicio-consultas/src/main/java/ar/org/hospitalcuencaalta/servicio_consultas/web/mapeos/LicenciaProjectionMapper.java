package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.LicenciaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.LicenciaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LicenciaProjectionMapper {
    @Mapping(target = "empleado.id", source = "empleadoId")
    LicenciaProjection toLicencia(LicenciaDto dto);
}
