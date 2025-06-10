package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.Capacitacion;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDetalleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para la vista detallada de Capacitacion
 */
@Mapper(componentModel = "spring")
public interface CapacitacionDetalleMapper {
    CapacitacionDetalleDto toDto(Capacitacion entity);
    Capacitacion toEntity(CapacitacionDetalleDto dto);
}

