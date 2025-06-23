package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.LiquidacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.LiquidacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LiquidacionProjectionMapper {
    @Mapping(target = "empleado.id", source = "empleadoId")
    LiquidacionProjection toLiquidacion(LiquidacionDto dto);
}
