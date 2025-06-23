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

    /**
     * Entidad -> DTO. Se utilizan mapeos explícitos para las relaciones
     * con el empleado evaluado y el evaluador.
     */
    @Mappings({
            @Mapping(source = "periodo", target = "periodo"),
            @Mapping(source = "empleado.id", target = "empleadoId"),
            @Mapping(source = "evaluador.id", target = "evaluadorId")
    })
    EvaluacionDto toDto(EvaluacionDesempeno entity);

    /**
     * DTO -> Entidad. Solo seteamos los identificadores dentro de las
     * relaciones correspondientes.
     */
    @Mappings({
            @Mapping(source = "periodo", target = "periodo"),
            @Mapping(target = "empleado.id", source = "empleadoId"),
            @Mapping(target = "evaluador.id", source = "evaluadorId")
    })
    EvaluacionDesempeno toEntity(EvaluacionDto dto);
}