package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ar.org.hospitalcuencaalta.servicio_contrato.modelo.ContratoLaboral;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDto;
import org.mapstruct.Mapping;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDetalleDto;

@Mapper(
    componentModel = "spring",
    uses = EmpleadoRegistryMapper.class
)
public interface ContratoLaboralMapper {

    // métodos existentes
    ContratoLaboralDto toContratoLaboralDto(ContratoLaboral contrato);
    ContratoLaboral toContratoLaboral(ContratoLaboralDto dto);
    // "empleado" contiene los datos de EmpleadoRegistry; la entidad ContratoLaboral
    // únicamente almacena el id, por eso se ignora en esta dirección de mapeo
    @Mapping(target = "empleado", ignore = true)
    ContratoLaboralDetalleDto toContratoLaboralDetalleDto(ContratoLaboral contrato);

    @InheritInverseConfiguration(name = "toContratoLaboralDetalleDto")
    @Mapping(target = "empleadoId", source = "empleado.id")
    ContratoLaboral toContratoLaboralFromDetalleDto(ContratoLaboralDetalleDto dto);

    // ALIAS para tu servicio
    default ContratoLaboralDto toDto(ContratoLaboral contrato) {
        return toContratoLaboralDto(contrato);
    }

    default ContratoLaboral toEntity(ContratoLaboralDto dto) {
        return toContratoLaboral(dto);
    }
}
