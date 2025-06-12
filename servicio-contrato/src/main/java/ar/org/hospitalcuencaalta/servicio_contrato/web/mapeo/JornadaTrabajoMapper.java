package ar.org.hospitalcuencaalta.servicio_contrato.web.mapeo;


import ar.org.hospitalcuencaalta.servicio_contrato.modelo.JornadaTrabajo;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.JornadaTrabajoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JornadaTrabajoMapper {


    /**
     * Entidad → DTO (mapea id, tipoJornada, horasSemanales y contratoId automáticamente
     * porque los nombres coinciden).
     */
    JornadaTrabajoDto toDto(JornadaTrabajo entity);

    /**
     * DTO → Entidad
     *
     * Aquí le decimos a MapStruct que cuando vea el campo contratoId
     * lo ponga en la propiedad anidada contratoLaboral.id.
     */
    @Mapping(source = "contratoId", target = "contratoLaboral.id")

    JornadaTrabajo toEntity(JornadaTrabajoDto dto);
}
