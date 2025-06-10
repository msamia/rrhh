package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.Capacitacion;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDto;
import org.mapstruct.Mapper;

/**
 * Mapper para la vista resumida de Capacitacion
 */
@Mapper(componentModel = "spring")
public interface CapacitacionMapper {
    CapacitacionDto toDto(Capacitacion entity);
    Capacitacion toEntity(CapacitacionDto dto);
}