package ar.org.hospitalcuencaalta.servicio_contrato.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacacionDetalleDto {
    private Long id;
    private Integer ejercicio;
    private Integer diasOtorgados;
    private Integer diasTomados;
    private EmpleadoRegistryDto empleado;
}
