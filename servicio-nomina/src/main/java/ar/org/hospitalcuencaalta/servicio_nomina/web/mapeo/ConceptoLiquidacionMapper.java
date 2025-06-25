package ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.ConceptoLiquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface ConceptoLiquidacionMapper {
    ConceptoLiquidacionDto toDto(ConceptoLiquidacion e);

    @Mapping(target = "liquidacion", ignore = true)
    @Mapping(target = "empleado", ignore = true)
    ConceptoLiquidacion toEntity(ConceptoLiquidacionDto d);
}
