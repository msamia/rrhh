package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EmpleadoRegistry;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EmpleadoRegistryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpleadoRegistryMapper {
    EmpleadoRegistryDto toDto(EmpleadoRegistry entity);
    EmpleadoRegistry toEntity(EmpleadoRegistryDto dto);
}