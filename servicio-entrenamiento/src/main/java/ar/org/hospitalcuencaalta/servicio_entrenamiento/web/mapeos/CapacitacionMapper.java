package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.Capacitacion;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para la vista resumida de Capacitacion
 */
@Mapper(componentModel = "spring")
public interface CapacitacionMapper {

    /**
     * Entidad {@link Capacitacion} -> DTO. Sólo se expone el identificador
     * del empleado asociado.
     */
    @Mapping(source = "empleado.id", target = "empleadoId")
    CapacitacionDto toDto(Capacitacion entity);

    /**
     * DTO -> Entidad {@link Capacitacion}. Se establece únicamente el id del
     * empleado a través de su relación.
     */
    @Mapping(target = "empleado.id", source = "empleadoId")
    Capacitacion toEntity(CapacitacionDto dto);
}