// src/main/java/ar/org/hospitalcuencaalta/servicio_contrato/web/mapeo/JornadaTrabajoDetalleMapper.java
package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_contrato.modelo.JornadaTrabajo;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.JornadaTrabajoDetalleDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * Mapper para convertir entre JornadaTrabajo ⇄ JornadaTrabajoDetalleDto,
 * delegando en ContratoLaboralMapper para el mapeo del contrato.
 */
@Mapper(
    componentModel = "spring",
    uses = { ContratoLaboralMapper.class }
)
public interface JornadaTrabajoDetalleMapper {

    /**
     * Entidad → DTO (incluye nested ContratoLaboralDetalleDto)
     */
    JornadaTrabajoDetalleDto toDetalleDto(JornadaTrabajo entity);

    /**
     * DTO → Entidad
     * Hereda automáticamente la configuración inversa de toDetalleDto().
     */
    @InheritInverseConfiguration
    JornadaTrabajo toJornadaTrabajo(JornadaTrabajoDetalleDto dto);
}
