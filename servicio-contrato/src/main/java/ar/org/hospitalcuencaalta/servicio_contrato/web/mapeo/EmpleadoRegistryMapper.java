package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_contrato.modelo.EmpleadoRegistry;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.EmpleadoRegistryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpleadoRegistryMapper {
    EmpleadoRegistryDto toDto(EmpleadoRegistry entity);
}
