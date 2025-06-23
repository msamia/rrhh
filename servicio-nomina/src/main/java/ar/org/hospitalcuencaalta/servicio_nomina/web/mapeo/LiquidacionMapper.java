package ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.Liquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface LiquidacionMapper {
    LiquidacionDto toDto(Liquidacion e);

    @Mapping(target = "empleado", ignore = true)
    Liquidacion toEntity(LiquidacionDto d);
}

