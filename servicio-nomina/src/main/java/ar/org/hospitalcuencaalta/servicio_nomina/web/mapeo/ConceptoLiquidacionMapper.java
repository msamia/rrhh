package ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.ConceptoLiquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ConceptoLiquidacionMapper {
    ConceptoLiquidacionDto toDto(ConceptoLiquidacion e);

    ConceptoLiquidacion toEntity(ConceptoLiquidacionDto d);
}
