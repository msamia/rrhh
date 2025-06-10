package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EvaluacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EvaluacionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvaluacionProjectionMapper {
    EvaluacionProjection toEvaluacion(EvaluacionDto dto);
}
