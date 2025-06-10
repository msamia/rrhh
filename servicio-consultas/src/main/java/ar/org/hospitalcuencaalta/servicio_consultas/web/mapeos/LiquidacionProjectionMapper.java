package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.LiquidacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.LiquidacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LiquidacionProjectionMapper {
    LiquidacionProjection toLiquidacion(LiquidacionDto dto);
}
