// src/main/java/ar/org/hospitalcuencaalta/servicio_contrato/web/mapeo/JornadaTrabajoDetalleMapper.java
package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_contrato.modelo.JornadaTrabajo;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.JornadaTrabajoDetalleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre JornadaTrabajo ⇄ JornadaTrabajoDetalleDto,
 * delegando en ContratoLaboralMapper para el mapeo del contrato.
 */
@Mapper(
        componentModel = "spring",
        uses = { ContratoLaboralMapper.class }   // para anidar automáticamente el mapeo DTO ⇄ entidad
)
public interface JornadaTrabajoDetalleMapper {

    /** Entidad → DTO detalle */
    @Mapping(source = "contratoId",      target = "contratoId")
    @Mapping(source = "contratoLaboral", target = "contrato")
    JornadaTrabajoDetalleDto toDetalleDto(JornadaTrabajo entity);

    /** DTO detalle → Entidad */
    @Mapping(source = "contrato", target = "contratoLaboral")
    JornadaTrabajo toEntity(JornadaTrabajoDetalleDto dto);
}

