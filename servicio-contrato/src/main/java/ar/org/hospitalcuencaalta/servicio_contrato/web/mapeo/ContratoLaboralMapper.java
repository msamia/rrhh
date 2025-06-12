package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_contrato.modelo.ContratoLaboral;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDetalleDto;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContratoLaboralMapper {

    /**
     * Entidad → DTO simple (lista)
     * Mapea solo el ID del empleado (entidad tiene empleadoId)
     */
    @Mapping(source = "empleadoId", target = "empleadoId")
    ContratoLaboralDto toDto(ContratoLaboral entidad);

    @InheritInverseConfiguration
    ContratoLaboral toEntity(ContratoLaboralDto dto);

    /**
     * Entidad → DTO detalle (incluye nested EmpleadoRegistryDto)
     * Aquí solo alimentamos el id del empleado; el resto quedará null
     * (si quieres todos los campos, antes deberías recuperar la entidad EmpleadoRegistry).
     */
    @Mapping(source = "empleadoId", target = "empleado.id")
    ContratoLaboralDetalleDto toDetalleDto(ContratoLaboral entidad);

    /**
     * DTO detalle → Entidad
     * Convierte empleado.id → empleadoId en la entidad
     */
    @Mapping(source = "empleado.id", target = "empleadoId")
    @InheritInverseConfiguration(name = "toDetalleDto")
    ContratoLaboral fromDetalleDto(ContratoLaboralDetalleDto dto);
}


