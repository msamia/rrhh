package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ConceptoLiquidacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.ConceptoLiquidacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConceptoLiquidacionProjectionMapper {
    ConceptoLiquidacionProjection toConcepto(ConceptoLiquidacionDto dto);
}
