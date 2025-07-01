package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EmpleadoConceptoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.EmpleadoConceptoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpleadoConceptoProjectionMapper {
    @Mapping(target = "empleado.id", source = "empleadoId")
    @Mapping(target = "concepto.id", source = "conceptoId")
    EmpleadoConceptoProjection toEmpleadoConcepto(EmpleadoConceptoDto dto);
}
