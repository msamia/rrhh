package ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.Liquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDetalleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring", uses=EmpleadoRegistryMapper.class)
public interface LiquidacionDetalleMapper { LiquidacionDetalleDto toDetalleDto(Liquidacion e); }
