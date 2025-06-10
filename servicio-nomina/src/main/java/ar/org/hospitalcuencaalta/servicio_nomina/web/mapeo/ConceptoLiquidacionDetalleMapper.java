package ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.ConceptoLiquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDetalleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring", uses=LiquidacionMapper.class)
public interface ConceptoLiquidacionDetalleMapper { ConceptoLiquidacionDetalleDto toDetalleDto(ConceptoLiquidacion e); }
