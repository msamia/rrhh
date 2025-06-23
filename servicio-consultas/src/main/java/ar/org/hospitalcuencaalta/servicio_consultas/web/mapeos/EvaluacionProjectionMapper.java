package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EvaluacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EvaluacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EvaluacionProjectionMapper {
    @Mapping(target = "empleado.id", source = "empleadoId")
    @Mapping(target = "evaluador.id", source = "evaluadorId")
    EvaluacionProjection toEvaluacion(EvaluacionDto dto);
}
