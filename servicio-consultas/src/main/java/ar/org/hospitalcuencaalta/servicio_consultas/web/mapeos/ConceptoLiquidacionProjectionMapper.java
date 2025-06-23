package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ConceptoLiquidacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.ConceptoLiquidacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConceptoLiquidacionProjectionMapper {
    @Mapping(target = "liquidacion.id", source = "liquidacionId")
    ConceptoLiquidacionProjection toConcepto(ConceptoLiquidacionDto dto);
}
