package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ar.org.hospitalcuencaalta.servicio_contrato.modelo.ContratoLaboral;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDto;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDetalleDto;

@Mapper(
    componentModel = "spring",
    uses = EmpleadoRegistryMapper.class
)
public interface ContratoLaboralMapper {

    // métodos existentes
    ContratoLaboralDto toContratoLaboralDto(ContratoLaboral contrato);
    ContratoLaboral toContratoLaboral(ContratoLaboralDto dto);
    ContratoLaboralDetalleDto toContratoLaboralDetalleDto(ContratoLaboral contrato);
    @InheritInverseConfiguration(name = "toContratoLaboralDetalleDto")
    ContratoLaboral toContratoLaboralFromDetalleDto(ContratoLaboralDetalleDto dto);

    // ALIAS para tu servicio
    default ContratoLaboralDto toDto(ContratoLaboral contrato) {
        return toContratoLaboralDto(contrato);
    }

    default ContratoLaboral toEntity(ContratoLaboralDto dto) {
        return toContratoLaboral(dto);
    }
}
