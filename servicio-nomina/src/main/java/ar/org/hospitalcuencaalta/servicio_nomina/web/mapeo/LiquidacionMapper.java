package ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.Liquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface LiquidacionMapper { LiquidacionDto toDto(Liquidacion e); Liquidacion toEntity(LiquidacionDto d); }

