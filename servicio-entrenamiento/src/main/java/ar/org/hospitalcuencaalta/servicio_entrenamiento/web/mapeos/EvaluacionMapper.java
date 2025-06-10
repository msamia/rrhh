package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EvaluacionDesempeno;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper para la vista resumida de EvaluacionDesempeno
 */
@Mapper(componentModel = "spring", uses = YearMonthMapper.class)
public interface EvaluacionMapper {
    @Mappings({
            @Mapping(source = "periodo", target = "periodo")
    })
    EvaluacionDto toDto(EvaluacionDesempeno entity);

    @Mappings({
            @Mapping(source = "periodo", target = "periodo")
    })
    EvaluacionDesempeno toEntity(EvaluacionDto dto);
}