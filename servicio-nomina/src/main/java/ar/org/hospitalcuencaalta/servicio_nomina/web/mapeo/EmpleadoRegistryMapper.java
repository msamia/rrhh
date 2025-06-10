package ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.EmpleadoRegistry;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.EmpleadoRegistryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface EmpleadoRegistryMapper { EmpleadoRegistryDto toDto(EmpleadoRegistry e); }
