package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EvaluacionDesempeno;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDetalleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper para la vista detallada de EvaluacionDesempeno
 */
@Mapper(componentModel = "spring", uses = YearMonthMapper.class)
public interface EvaluacionDetalleMapper {
    @Mappings({
            @Mapping(source = "periodo", target = "periodo")
    })
    EvaluacionDetalleDto toDto(EvaluacionDesempeno entity);

    @Mappings({
            @Mapping(source = "periodo", target = "periodo")
    })
    EvaluacionDesempeno toEntity(EvaluacionDetalleDto dto);
}
