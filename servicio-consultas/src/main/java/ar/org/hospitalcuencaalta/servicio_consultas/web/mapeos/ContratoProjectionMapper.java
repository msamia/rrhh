package ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ContratoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.ContratoLaboralDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContratoProjectionMapper {
    ContratoProjection toContrato(ContratoLaboralDto dto);
}
