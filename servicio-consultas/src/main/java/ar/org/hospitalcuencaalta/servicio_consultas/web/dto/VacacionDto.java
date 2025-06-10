package ar.org.hospitalcuencaalta.servicio_consultas.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacacionDto {
    private Long id;
    private Integer ejercicio;
    private Integer diasOtorgados;
    private Integer diasTomados;
    private Long empleadoId;
}
