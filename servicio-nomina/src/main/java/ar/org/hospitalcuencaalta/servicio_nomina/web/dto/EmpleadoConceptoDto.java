package ar.org.hospitalcuencaalta.servicio_nomina.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoConceptoDto {
    private Long id;
    private Long empleadoId;
    private Long conceptoId;
}
