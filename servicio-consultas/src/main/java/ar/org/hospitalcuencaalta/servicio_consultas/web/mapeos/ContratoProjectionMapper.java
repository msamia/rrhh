package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ContratoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.ContratoLaboralDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContratoProjectionMapper {
    @Mapping(target = "empleado.id", source = "empleadoId")
    ContratoProjection toContrato(ContratoLaboralDto dto);
}
