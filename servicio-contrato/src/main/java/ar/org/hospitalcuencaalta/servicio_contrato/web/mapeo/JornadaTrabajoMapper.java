package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ar.org.hospitalcuencaalta.servicio_contrato.modelo.JornadaTrabajo;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.JornadaTrabajoDto;

@Mapper(componentModel = "spring")
public interface JornadaTrabajoMapper {

    JornadaTrabajoDto toDto(JornadaTrabajo entity);

    JornadaTrabajo toEntity(JornadaTrabajoDto dto);
}